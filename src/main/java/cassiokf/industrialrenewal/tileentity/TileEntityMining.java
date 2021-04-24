package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.init.SoundsRegistration;
import cassiokf.industrialrenewal.item.ItemDrill;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.CustomEnergyStorage;
import cassiokf.industrialrenewal.util.CustomFluidTank;
import cassiokf.industrialrenewal.util.CustomItemStackHandler;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityMining extends TileEntityMultiBlockBase<TileEntityMining>
{
    private final CustomEnergyStorage energyContainer = new CustomEnergyStorage(100000, 10240, 10240)
    {
        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public void onEnergyChange()
        {
            TileEntityMining.this.sync();
        }
    };
    public final CustomFluidTank waterTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean canFill(FluidStack resource)
        {
            return fluid != null && fluid.getFluid().equals(FluidRegistry.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntityMining.this.sync();
        }
    };
    public final CustomItemStackHandler drillInv = new CustomItemStackHandler(1)
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return !stack.isEmpty() && stack.getItem() instanceof ItemDrill;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityMining.this.checkDeepMine();
        }
    };
    public final CustomItemStackHandler internalInv = new CustomItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityMining.this.markDirty();
        }
    };
    private static final int maxHeat = 18000;
    private int drillHeat;
    private int oldDrillHeat;

    public static int waterPerTick = IRConfig.Main.miningWaterPerTick.get();
    public static int energyPerTick = IRConfig.Main.miningEnergyPerTick.get();
    public static int deepEnergyPerTick = IRConfig.Main.miningDeepEnergyPerTick.get();
    public static float volume = IRConfig.Main.miningVolume.get().floatValue() * IRConfig.Main.masterVolumeMult.get().floatValue();
    private static final int cooldown = IRConfig.Main.miningCooldown.get();
    private static final int damageAmount = 1;
    private int currentTick = 0;

    private boolean running;
    private boolean oldRunning;
    private boolean depleted = false;
    private boolean isDeepMine = false;

    private final Stack<OreMining> ores = new Stack<>();
    private ItemStack vein = ItemStack.EMPTY;
    private int size;

    //Client only
    private float rotation;
    private float ySlide = 0;
    private boolean revert;

    //Server
    private int particleTick;

    public TileEntityMining(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onTick()
    {
        if (this.isMaster())
        {
            if (!world.isRemote)
            {
                outputOrSpawn();
                boolean canCheck = canCheckOre();
                if (canCheck && getOreSize() == 0) getOres();
                if (canCheck && canRun())
                {
                    if (getOreSize() > 0) running = true;

                    if (running)
                    {
                        consumeEnergy();
                        if (drillHeat < (waterTank.getFluidAmount() >= waterPerTick ? 9400 : 17300)) drillHeat += 20;
                        mineOre();
                        size = getOreSize();
                    } else
                    {
                        size = 0;
                        drillHeat -= 30;
                        currentTick = 0;
                    }
                } else
                {
                    size = getOreSize();
                    drillHeat -= 30;
                    running = false;
                    currentTick = 0;
                }

                drillHeat = MathHelper.clamp(drillHeat, 3200, maxHeat);
                if (running != oldRunning || drillHeat != oldDrillHeat)
                {
                    oldRunning = running;
                    oldDrillHeat = drillHeat;
                    sync();
                }
            }
            doAnimation();
            updateSound();
        }
    }

    private int getOreSize()
    {
        return isDeepMine() ? vein.getCount() : ores.size();
    }

    private void updateSound()
    {
        if (!world.isRemote) return;
        if (this.running)
        {
            IRSoundHandler.playRepeatableSound(SoundsRegistration.MINING_RESOURCEL, volume, 1f, pos);
        } else
        {
            IRSoundHandler.stopTileSound(pos);
        }
    }

    private boolean isDeepMine()
    {
        return isDeepMine;
    }

    public void checkDeepMine()
    {
        isDeepMine = drillInv.getStackInSlot(0).getItem() == ItemsRegistration.DRILLDEEP.get();
        depleted = false;
        sync();
    }

    private void consumeEnergy()
    {
        energyContainer.extractEnergy(isDeepMine() ? deepEnergyPerTick : energyPerTick, false);
        waterTank.drain(waterPerTick, IFluidHandler.FluidAction.EXECUTE);
    }

    private int getFortune()
    {
        return drillInv.getStackInSlot(0).getItem().equals(ItemsRegistration.DRILLDIAMOND.get()) ? 2 : 1;
    }

    private int getMaxCooldown()
    {
        int t = waterTank.getFluidAmount() >= waterPerTick ? cooldown : cooldown * 2;
        return isDeepMine() ? t * 2 : t;
    }

    private void mineOre()
    {
        if (currentTick >= getMaxCooldown())
        {
            ItemStack stack;
            int fortune = getFortune();
            if (isDeepMine())
            {
                currentTick = 0;

                stack = vein.copy();
                stack.setCount(1);
                vein.shrink(1);
            } else
            {
                if (ores.isEmpty()) return;
                OreMining ore = ores.pop();
                Block block = ore.state.getBlock();
                if (world.getBlockState(ore.pos).getBlock() != ore.state.getBlock()) return;
                currentTick = 0;
                int quantity = block.quantityDroppedWithBonus(fortune, world.rand);
                Item item = block.getItemDropped(ore.state, world.rand, fortune);
                stack = new ItemStack(item, quantity, block.damageDropped(ore.state));
                world.setBlockState(ore.pos, Blocks.COBBLESTONE.getDefaultState());
            }
            internalInv.insertItem(0, stack, false);
            damageDrill();
        } else currentTick++;
    }

    private void damageDrill()
    {
        int damage = drillHeat <= 13000 ? damageAmount : damageAmount * 4;
        ItemStack stack = drillInv.getStackInSlot(0);
        if (stack.attemptDamageItem(damage, world.rand, null))
        {
            stack.shrink(stack.getCount());
        }
    }

    private void outputOrSpawn()
    {
        if (internalInv.getStackInSlot(0).isEmpty()) return;

        BlockPos outPos = pos.offset(getMasterFacing(), 2).down();
        TileEntity te = world.getTileEntity(outPos);
        if (te != null)
        {
            IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getMasterFacing().getOpposite()).orElse(null);
            if (handler != null)
            {
                Utils.moveItemToInventory(internalInv, 0, handler);
            }
        } else
        {
            BlockState state = world.getBlockState(outPos);
            if (state.getBlock().isAir(state, world, outPos))
            {
                Utils.spawnItemStack(world, outPos, internalInv.getStackInSlot(0));
            }
        }
    }

    private void getOres()
    {
        if (isDeepMine())
        {
            vein = OreGeneration.getChunkVein(world, pos);
            size = vein.getCount();
            depleted = size == 0;
            sync();
            return;
        }
        IChunk chunk = world.getChunk(pos);
        int a = chunk.getPos().x * 16;
        int b = chunk.getPos().z * 16;

        for (double y = 1; y <= pos.getY() - 2; y++)
        {
            for (double x = 0; x <= 15; x++)
            {
                for (double z = 0; z <= 15; z++)
                {
                    BlockPos actualPosition = new BlockPos(a + x, y, b + z);
                    BlockState state = chunk.getBlockState(actualPosition);
                    if (OreGeneration.MINERABLE_ORES.contains(state.getBlock().asItem()))
                    {
                        ores.add(new OreMining(state, actualPosition));
                    }
                }
            }
        }
        size = ores.size();
        depleted = size == 0;
        sync();
    }

    private void doAnimation()
    {
        if (!world.isRemote)
        {
            if (running)
            {
                if (particleTick >= 10)
                {
                    particleTick = 0;
                    spawnFluidParticle(pos.getX() + 0.5f, pos.getY() - 1f, pos.getZ() + 0.5f, Blocks.STONE);
                    if (waterTank.getFluidAmount() > 0)
                        spawnFluidParticle(pos.getX() + 0.5f, pos.getY() - 1f, pos.getZ() + 0.5f, waterTank.getFluid().getFluid().getDefaultState().getBlockState().getBlock());
                }
                particleTick++;
            }
        } else
        {
            if (running)
            {
                rotation += 20f;
                if (rotation >= 360) rotation = 0;

                ySlide = Utils.lerp(ySlide, revert ? 0 : -1, 0.01f);
                if (ySlide >= -0.01 || ySlide <= -0.9) revert = !revert;
            } else
            {
                rotation = 0f;
                ySlide = Utils.lerp(ySlide, -1, 0.05f);
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityMining;
    }

    private void spawnFluidParticle(double x, double y, double z, Block block)
    {
        float f = (float) MathHelper.ceil(1.0F);
        double d0 = Math.min(0.2F + f / 15.0F, 2.5D);
        int i = (int) (150.0D * d0);
        ((ServerWorld) world).spawnParticle(ParticleTypes.DUST, x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(block.getDefaultState()));
    }

    private boolean canCheckOre()
    {
        return !depleted
                && energyContainer.getEnergyStored() >= (isDeepMine() ? deepEnergyPerTick : energyPerTick)
                && !drillInv.getStackInSlot(0).isEmpty();
    }

    private boolean canRun()
    {
        Direction facing = getMasterFacing();
        BlockPos posPort = pos.offset(facing.rotateYCCW()).offset(facing.getOpposite()).down();
        return !depleted
                && (world.isBlockPowered(posPort) || world.isBlockPowered(posPort.offset(facing.getOpposite())))
                && energyContainer.getEnergyStored() >= (isDeepMine() ? deepEnergyPerTick : energyPerTick)
                && !drillInv.getStackInSlot(0).isEmpty()
                && internalInv.getStackInSlot(0).isEmpty();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(world, pos, drillInv);
    }

    public String getWaterText(int line)
    {
        if (line == 1) return I18n.format("render.industrialrenewal.coolant") + ":";
        return !waterTank.getFluid().isEmpty() ? waterTank.getFluid().getDisplayName().getFormattedText() : "---";
    }

    public String getEnergyText(int line)
    {
        if (line == 1) return I18n.format("render.industrialrenewal.energy") + ":";
        return energyContainer.getEnergyStored() + " FE";
    }


    public String getHeatText()
    {
        String name = I18n.format("render.industrialrenewal.drillheat") + ": ";
        if (drillHeat > 13000) name += TextFormatting.RED;
        return name + Utils.getConvertedTemperatureString(drillHeat / 100F);
    }

    public String[] getScreenTexts()
    {
        if (energyContainer.getEnergyStored() <= 0) return EMPTY_ARRAY;
        List<String> texts = new ArrayList<>();
        texts.add("Mining Drill Status: " + (running ? "Running" : TextFormatting.RED + " Stoped"));
        texts.add("Mining Drill Mode: " + TextFormatting.BLUE + (isDeepMine ? "Deep Mine" : "Surface Mine"));
        texts.add("Vein Size: " + (depleted ? "Depleted" : size));
        texts.add("Consumption: " + (isDeepMine() ? deepEnergyPerTick : energyPerTick) + " FE/t");
        texts.add(getHeatText());
        int maxD = getDrill().getMaxDamage();
        texts.add("Drill condition: " + (maxD - getDrill().getDamage()) + "/" + maxD);

        String[] itemsArray = new String[texts.size()];
        itemsArray = texts.toArray(itemsArray);
        return itemsArray;
    }

    public float getWaterFill() //0 ~ 180
    {
        return Utils.normalizeClamped(this.waterTank.getFluidAmount(), 0, this.waterTank.getCapacity()) * 180f;
    }

    public float getEnergyFill() //0 ~ 180
    {
        return Utils.normalizeClamped(this.energyContainer.getEnergyStored(), 0, this.energyContainer.getMaxEnergyStored());
    }

    public float getHeatFill() //0 ~ 180
    {
        return Utils.normalizeClamped(drillHeat, 0, maxHeat) * 180f;
    }

    public boolean hasDrill()
    {
        return !drillInv.getStackInSlot(0).isEmpty();
    }

    public ItemStack getDrill()
    {
        return drillInv.getStackInSlot(0);
    }

    public float getRotation()
    {
        return -rotation;
    }

    public float getSlide()
    {
        return ySlide;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT waterTag = new CompoundNBT();
        waterTank.writeToNBT(waterTag);
        compound.put("water", waterTag);
        compound.put("firebox", drillInv.serializeNBT());
        compound.put("StoredIR", this.energyContainer.serializeNBT());
        compound.putInt("heat", drillHeat);
        compound.putBoolean("running", running);
        compound.putBoolean("depleted", depleted);
        compound.putBoolean("deep", isDeepMine);
        compound.putInt("vsize", size);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        waterTank.readFromNBT(compound.getCompound("water"));
        drillInv.deserializeNBT(compound.getCompound("firebox"));
        energyContainer.deserializeNBT(compound.getCompound("StoredIR"));
        drillHeat = compound.getInt("heat");
        running = compound.getBoolean("running");
        this.depleted = compound.getBoolean("depleted");
        isDeepMine = compound.getBoolean("deep");
        size = compound.getInt("vsize");
        super.read(compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        TileEntityMining masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();

        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(face).offset(face.rotateY())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (facing == Direction.UP && this.pos.equals(masterTE.getPos().offset(face.getOpposite()).up()) && capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> masterTE.energyContainer).cast();
        return super.getCapability(capability, facing);
    }

    public IItemHandler getDrillHandler()
    {
        return this.getMaster().drillInv;
    }

    public class OreMining
    {
        public final BlockState state;
        public final BlockPos pos;

        OreMining(BlockState state, BlockPos pos)
        {
            this.state = state;
            this.pos = pos;
        }
    }
}
