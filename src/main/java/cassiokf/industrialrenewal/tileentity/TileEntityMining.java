package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.IRSoundHandler;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemDrill;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityMultiBlockBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.VoltsEnergyContainer;
import cassiokf.industrialrenewal.world.generation.OreGeneration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityMining extends TileEntityMultiBlockBase<TileEntityMining>
{
    private final VoltsEnergyContainer energyContainer = new VoltsEnergyContainer(100000, 10240, 10240)
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
    public final FluidTank waterTank = new FluidTank(32000)
    {
        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            return fluid != null && fluid.getFluid().equals(FluidRegistry.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntityMining.this.sync();
        }
    };
    public final ItemStackHandler drillInv = new ItemStackHandler(1)
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
    public final ItemStackHandler internalInv = new ItemStackHandler(1)
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

    public static int waterPerTick = IRConfig.MainConfig.Main.miningWaterPerTick;
    public static int energyPerTick = IRConfig.MainConfig.Main.miningEnergyPerTick;
    public static int deepEnergyPerTick = IRConfig.MainConfig.Main.miningDeepEnergyPerTick;
    public static float volume = IRConfig.MainConfig.Sounds.miningVolume * IRConfig.MainConfig.Sounds.masterVolumeMult;
    private static final int cooldown = IRConfig.MainConfig.Main.miningCooldown;
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
            IRSoundHandler.playRepeatableSound(IRSoundRegister.MINING_RESOURCEL, volume, 1f, pos);
        }
        else
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
        isDeepMine = drillInv.getStackInSlot(0).getItem() == ModItems.drillDeep;
        depleted = false;
        sync();
    }

    private void consumeEnergy()
    {
        energyContainer.extractEnergy(isDeepMine() ? deepEnergyPerTick : energyPerTick, false);
        waterTank.drain(waterPerTick, true);
    }

    private int getFortune()
    {
        return drillInv.getStackInSlot(0).getItem().equals(ModItems.drillDiamond) ? 2 : 1;
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
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getMasterFacing().getOpposite()))
        {
            IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getMasterFacing().getOpposite());
            if (handler != null)
            {
                Utils.moveItemToInventory(internalInv, 0, handler);
            }
        } else
        {
            IBlockState state = world.getBlockState(outPos);
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
        Chunk chunk = world.getChunk(pos);
        int a = chunk.x * 16;
        int b = chunk.z * 16;
        for (double y = 1; y <= pos.getY() - 2; y++)
        {
            for (double x = 0; x <= 15; x++)
            {
                for (double z = 0; z <= 15; z++)
                {
                    BlockPos actualPosition = new BlockPos(a + x, y, b + z);
                    IBlockState state = chunk.getBlockState(actualPosition);
                    if (OreGeneration.MINERABLE_ORES.contains(Item.getItemFromBlock(state.getBlock())))
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
                        spawnFluidParticle(pos.getX() + 0.5f, pos.getY() - 1f, pos.getZ() + 0.5f, waterTank.getFluid().getFluid().getBlock());
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
        ((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(block.getDefaultState()));
    }

    private boolean canCheckOre()
    {
        return !depleted
                && energyContainer.getEnergyStored() >= (isDeepMine() ? deepEnergyPerTick : energyPerTick)
                && !drillInv.getStackInSlot(0).isEmpty();
    }

    private boolean canRun()
    {
        EnumFacing facing = getMasterFacing();
        BlockPos posPort = pos.offset(facing.rotateYCCW()).offset(facing.getOpposite()).down();
        return !depleted
                && (world.isBlockPowered(posPort) || world.isBlockPowered(posPort.offset(facing.getOpposite())))
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
        return waterTank.getFluid() != null ? waterTank.getFluid().getLocalizedName() : "---";
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
        texts.add("Drill condition: " + (maxD - getDrill().getItemDamage()) + "/" + maxD);

        String[] itemsArray = new String[texts.size()];
        itemsArray = texts.toArray(itemsArray);
        return itemsArray;
    }

    public float getWaterFill() //0 ~ 180
    {
        return Utils.normalize(this.waterTank.getFluidAmount(), 0, this.waterTank.getCapacity()) * 180f;
    }

    public float getEnergyFill() //0 ~ 180
    {
        return Utils.normalize(this.energyContainer.getEnergyStored(), 0, this.energyContainer.getMaxEnergyStored());
    }

    public float getHeatFill() //0 ~ 180
    {
        return Utils.normalize(drillHeat, 0, maxHeat) * 180f;
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
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound waterTag = new NBTTagCompound();
        this.waterTank.writeToNBT(waterTag);
        compound.setTag("water", waterTag);
        compound.setTag("firebox", this.drillInv.serializeNBT());
        compound.setTag("StoredIR", this.energyContainer.serializeNBT());
        compound.setInteger("heat", drillHeat);
        compound.setBoolean("running", running);
        compound.setBoolean("depleted", depleted);
        compound.setBoolean("deep", isDeepMine);
        compound.setInteger("vsize", size);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.waterTank.readFromNBT(compound.getCompoundTag("water"));
        this.drillInv.deserializeNBT(compound.getCompoundTag("firebox"));
        this.energyContainer.deserializeNBT(compound.getCompoundTag("StoredIR"));
        this.drillHeat = compound.getInteger("heat");
        this.running = compound.getBoolean("running");
        this.depleted = compound.getBoolean("depleted");
        this.isDeepMine = compound.getBoolean("deep");
        this.size = compound.getInteger("vsize");
        super.readFromNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        TileEntityMining masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        EnumFacing face = masterTE.getMasterFacing();

        if (facing == face && this.pos.equals(masterTE.getPos().down().offset(face).offset(face.rotateY())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(masterTE.waterTank);
        if (facing == EnumFacing.UP && this.pos.equals(masterTE.getPos().offset(face.getOpposite()).up()) && capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(masterTE.energyContainer);
        return super.getCapability(capability, facing);
    }

    public IItemHandler getDrillHandler()
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.getMaster().drillInv);
    }

    public class OreMining
    {
        public final IBlockState state;
        public final BlockPos pos;

        OreMining(IBlockState state, BlockPos pos)
        {
            this.state = state;
            this.pos = pos;
        }
    }
}
