package net.cassiokf.industrialrenewal.blockentity;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.item.ItemDrill;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomEnergyStorage;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.cassiokf.industrialrenewal.util.capability.CustomItemStackHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.apache.logging.log4j.util.Strings.EMPTY_ARRAY;

public class BlockEntityMiner extends BlockEntity3x3x3MachineBase<BlockEntityMiner> {

    private static final int fluidTankCapacity = 32000;
    private static final int fluidEnergyCapacity = 100000;
    private static final int fluidEnergyReceive = 10240;

    private static final int CHUNK_RADIUS = 3;

    private final CustomFluidTank waterTank = new CustomFluidTank(fluidTankCapacity)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return stack != null && stack.getFluid().equals(Fluids.WATER);
        }
    }.setBlockEntity(this);

    private final LazyOptional<IItemHandler> drillInv = LazyOptional.of(this::createHandler);
    private final LazyOptional<IItemHandler> internalInv = LazyOptional.of(this::createInternalHandler);

    private static final int maxHeat = 18000;
    private int drillHeat;
    private int oldDrillHeat;
    private static final int waterPerTick = 10;
    private static final int energyPerTick = 768;
    private static final int deepEnergyPerTick = 1024;
    private static final int cooldown = 120;
    private static final int damageAmount = 1;

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(fluidEnergyCapacity, fluidEnergyReceive, energyPerTick).setBlockEntity(this)
        .setBlockEntity(this);
    private final LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);

    private int currentTick = 0;
    private boolean depleted = false;
    private boolean isDeepMine = false;
    private boolean running;
    private boolean oldRunning;

    //private Set<BlockPos> oresPos;
    private final Stack<OreMining> ores = new Stack<>();
    private final NonNullList<ItemStack> tempStack = NonNullList.create();
    private final ItemStack vein = ItemStack.EMPTY;
    private int size;

    //Client only
    private float rotation;
    private float ySlide = 0;
    private boolean revert;

    //Server
    private int particleTick;
    public boolean firstLoad = false;

    public BlockEntityMiner(BlockPos pos, BlockState state) {
        super(ModBlockEntity.MINER_TILE.get(), pos, state);
    }

    private CustomItemStackHandler createHandler()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return stack.getItem() instanceof ItemDrill;
            }
            
            @Override
            protected void onContentsChanged(int slot) {
                checkDeepMine();
            }
        };
    }

    private CustomItemStackHandler createInternalHandler()
    {
        return new CustomItemStackHandler(1).setBlockEntity(this);
    }

    public void tick() {
        if(level == null) return;
        if (hasLevel() && this.isMaster())
        {
//            running = canRun();
//            doAnimation();
            if (!level.isClientSide)
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
                        //Utils.debug("MINING...", size);
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

                drillHeat = Mth.clamp(drillHeat, 3200, maxHeat);
                if (running != oldRunning || drillHeat != oldDrillHeat)
                {
                    oldRunning = running;
                    oldDrillHeat = drillHeat;
                    sync();
                }
            }
            doAnimation();
            //updateSound();
        }
    }

    private int getOreSize()
    {
        return isDeepMine() ? vein.getCount() : ores.size();
    }
    private boolean isDeepMine()
    {
        return isDeepMine;
    }

    public void checkDeepMine()
    {
        isDeepMine = drillInv.orElse(null).getStackInSlot(0).getItem() == ModItems.DRILL_DEEP.get();
        depleted = false;
        sync();
    }

    private int getFortune()
    {
        IItemHandler iItemHandler = drillInv.orElse(null);
        if(iItemHandler == null)
            return 0;
        return iItemHandler.getStackInSlot(0).getItem().equals(ModItems.DRILL_DIAMOND.get()) ? 2 : 1;
    }

    private int getMaxCooldown()
    {
        int t = waterTank.getFluidAmount() >= waterPerTick ? cooldown : cooldown * 2;
        return isDeepMine() ? t * 2 : t;
    }

    private void mineOre()
    {
        if(level == null) return;
        //Utils.debug("Called mineOre");
        if (currentTick >= getMaxCooldown())
        {
            currentTick = 0;
            if (isDeepMine())
            {
                ItemStack s = vein.copy();
                s.setCount(1);
                tempStack.add(s);
                vein.shrink(1);
            } else
            {
                if (ores.isEmpty()) return;
                OreMining ore = ores.pop();
                if (level.getBlockState(ore.pos).getBlock() != ore.state.getBlock()) return;
                currentTick = 0;
                //int fortune = getFortune();
                Block block = ore.state.getBlock();
                List<ItemStack> drops = Block.getDrops(ore.state, level.getServer().getLevel(this.level.dimension()), ore.pos, this, null, getDrill());
                tempStack.addAll(drops);
                level.setBlock(ore.pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
//                Utils.debug("Ore mined, set block cobble");
            }
            ItemStack s = tempStack.get(0);
            if (!s.isEmpty())
            {
                internalInv.ifPresent(inv ->{
                    inv.insertItem(0, s, false);
                });
                tempStack.remove(s);
            }
            damageDrill();
        } else currentTick++;
    }

    private void damageDrill()
    {
        int damage = drillHeat <= 1 ? damageAmount : damageAmount * 4;
        IItemHandler itemHandler = drillInv.orElse(null);
        if(itemHandler == null)
            return;
        ItemStack stack = itemHandler.getStackInSlot(0);
        if (stack.hurt(damage, level.random, null))
            stack.shrink(stack.getCount());
    }

    private void consumeEnergy()
    {
        energyStorageHandler.ifPresent(iEnergyStorage -> {
            iEnergyStorage.extractEnergy(isDeepMine() ? deepEnergyPerTick : energyPerTick, false);
        });
        waterTank.drain(waterPerTick, IFluidHandler.FluidAction.EXECUTE);
    }

    private boolean canCheckOre()
    {
        return !depleted
                && energyStorageHandler.orElse(null).getEnergyStored() >= (isDeepMine() ? deepEnergyPerTick : energyPerTick)
                && !drillInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    private void outputOrSpawn()
    {
        if(level == null) return;
        if (!tempStack.isEmpty())
        {
            ItemStack s = tempStack.get(0);
            if (!s.isEmpty())
            {
                internalInv.ifPresent(inv ->{
                    inv.insertItem(0, s, false);
                });
                tempStack.remove(s);
            }
            return;
        }
        IItemHandler itemHandler = internalInv.orElse(null);
        if (internalInv.isPresent() && itemHandler.getStackInSlot(0).isEmpty()) return;

        BlockPos outPos = worldPosition.relative(getMasterFacing(), 2).below();
        BlockEntity te = level.getBlockEntity(outPos);
        if (te != null && te.getCapability(ForgeCapabilities.ITEM_HANDLER, getMasterFacing().getOpposite()).isPresent())
        {
                IItemHandler handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, getMasterFacing().getOpposite()).orElse(null);
                Utils.moveItemToInventory(internalInv.orElse(null), 0, handler);
        }
        else
        {
            BlockState state = level.getBlockState(outPos);
            if (state.isAir())
            {
                Utils.spawnItemStack(level, outPos, internalInv.orElse(null).getStackInSlot(0));
            }
        }
    }

    private void getOres()
    {
        if(level == null) return;
        tempStack.clear();
        int maxDepth = isDeepMine? 1 : -63;
        //Utils.debug("call getOres");
        if (isDeepMine())
        {
            //vein = OreGeneration.getChunkVein(level, worldPosition);
            //size = vein.getCount();
            depleted = size == 0;
            sync();
            return;
        }
        ChunkAccess chunk = level.getChunk(worldPosition);
        for(int q = -(CHUNK_RADIUS-1); q < CHUNK_RADIUS; q++){
            for(int r = -(CHUNK_RADIUS-1); r < CHUNK_RADIUS; r++){
                int a = (chunk.getPos().x + q) * 16;
                int b = (chunk.getPos().z + r) * 16;
                for (double y = maxDepth; y <= worldPosition.getY() - 2; y++)
                {
                    for (double x = 0; x <= 15; x++)
                    {
                        for (double z = 0; z <= 15; z++)
                        {
                            BlockPos actualPosition = new BlockPos((int) (a + x), (int) y, (int) (b + z));
                            BlockState state = level.getBlockState(actualPosition);

                            if(state.is(Tags.Blocks.ORES)){
                                ores.add(new OreMining(state, actualPosition));
//                                if(Config.MINER_BLACKLIST_AS_WHITE.get()){
//                                    if(Config.MINER_ORE_BLACKLIST.get().contains(state.getBlock().getRegistryName().toString())){
//                                        ores.add(new OreMining(state, actualPosition));
//                                    }
//                                }
//                                else{
//                                    if(!Config.MINER_ORE_BLACKLIST.get().contains(state.getBlock().getRegistryName().toString())){
//                                        ores.add(new OreMining(state, actualPosition));
//                                    }
//                                }
                            }
                        }
                    }
                }
            }
        }

        size = ores.size();
        depleted = (size == 0);
        sync();
    }

    private void doAnimation()
    {
        if(level == null) return;
        if (!level.isClientSide)
        {
            if (running)
            {
                if (waterTank.getFluidAmount() > 0 && particleTick >= 10)
                {
                    particleTick = 0;
                    spawnFluidParticle(worldPosition.getX() + 0.5f, worldPosition.getY() - 1f, worldPosition.getZ() + 0.5f, Blocks.STONE);
                    spawnFluidParticle(worldPosition.getX() + 0.5f, worldPosition.getY() - 1f, worldPosition.getZ() + 0.5f, waterTank.getFluid().getFluid().defaultFluidState().createLegacyBlock().getBlock());
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
    public boolean instanceOf(BlockEntity tileEntity)
    {
        return tileEntity instanceof BlockEntityMiner;
    }

    private void spawnFluidParticle(double x, double y, double z, Block block)
    {
        float f = (float) Mth.ceil(1.0F);
        double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
        int i = (int) (150.0D * d0);
        //((ServerWorld) world).addParticle(IParticleData., x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(block.getDefaultState()));
    }

    private boolean canRun()
    {
        if(level == null) return false;
        Direction facing = getMasterFacing();
        BlockPos posPort = worldPosition.relative(facing.getCounterClockWise()).relative(facing.getOpposite()).below();
        //Utils.debug("can run port pos", posPort, level.hasSignal(posPort, facing.getOpposite()));
        return !depleted
                && (level.hasSignal(posPort, facing.getOpposite()))
                && energyStorageHandler.orElse(null).getEnergyStored() >= energyPerTick
                && !drillInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void dropAllItems()
    {
        Block.popResource(level, worldPosition, drillInv.orElse(null).getStackInSlot(0));
    }

    public String getWaterText(int line)
    {
        //if (line == 1) return I18n.get("render.industrialrenewal.fluid") + ":";
        if (line == 1) return "Coolant: ";
        return Blocks.WATER.getName().getString();
    }

    public String getEnergyText(int line)
    {
        //if (line == 1) return I18n.get("render.industrialrenewal.energy") + ":";
        if (line == 1) return "Energy: ";
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage != null)
            return iEnergyStorage.getEnergyStored() + " FE";
        return "NULL FE";
    }


    public String getHeatText()
    {
        //String name = I18n.get("render.industrialrenewal.drillheat") + ": ";
        String name = "Drill Heat: ";
        return name + (int) Utils.getConvertedTemperature(drillHeat / 100F) + Utils.getTemperatureUnit();
    }


    public float getWaterFill() //0 ~ 180
    {
        //Utils.debug("currentAmount", getMaster().waterTank, waterTank);
        float currentAmount = waterTank.getFluidAmount() / 1000F;
        float totalCapacity = waterTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getEnergyFill() //0 ~ 180
    {
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage == null)
            return 0;

        float currentAmount = iEnergyStorage.getEnergyStored() / 1000F;
        float totalCapacity = iEnergyStorage.getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount;
    }

    public float getHeatFill() //0 ~ 180
    {
        float currentAmount = drillHeat;
        float totalCapacity = maxHeat;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public String[] getScreenTexts()
    {
        if (energyStorage.getEnergyStored() <= 0) return EMPTY_ARRAY;
        List<String> texts = new ArrayList<>();
        texts.add("Mining Drill Status: " + (running ? "Running" : ChatFormatting.RED + " Stopped"));
        texts.add("Mining Drill Mode: " + ChatFormatting.BLUE + (isDeepMine ? "Deep Mine" : "Surface Mine"));
        texts.add("Vein Size: " + (depleted ? "Depleted" :size));
        //Utils.debug("displayed size", ores.size(), isMaster(), worldPosition);
        texts.add("Consumption: " + (isDeepMine() ? deepEnergyPerTick : energyPerTick) + " FE/t");
        texts.add(getHeatText());
        int maxD = getDrill().getMaxDamage();
        texts.add("Drill condition: " + (maxD - getDrill().getDamageValue()) + "/" + maxD);

        String[] itemsArray = new String[texts.size()];
        itemsArray = texts.toArray(itemsArray);
        return itemsArray;
    }

    public boolean hasDrill()
    {
        IItemHandler iItemHandler = drillInv.orElse(null);
        return iItemHandler != null && !iItemHandler.getStackInSlot(0).isEmpty();
    }

    public ItemStack getDrill()
    {
        IItemHandler iItemHandler = drillInv.orElse(null);
        if(iItemHandler != null)
            return drillInv.orElse(null).getStackInSlot(0);
        return null;
    }

    public float getRotation()
    {
        return -rotation;
    }

    public float getSlide()
    {
        return ySlide;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        BlockEntityMiner masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();

        if (facing == null)
            return super.getCapability(capability, facing);

        if (facing == face && this.worldPosition.equals(masterTE.getBlockPos().below().relative(face).relative(face.getClockWise())) && capability == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (facing == Direction.UP && this.worldPosition.equals(masterTE.getBlockPos().relative(face.getOpposite()).above()) && capability == ForgeCapabilities.ENERGY)
            return masterTE.energyStorageHandler.cast();
        return super.getCapability(capability, facing);
    }

    public IItemHandler getDrillHandler()
    {
        if(getMaster() != null && getMaster().drillInv.isPresent())
            return getMaster().drillInv.orElse(null);
        return null;
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

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        CompoundTag waterTag = new CompoundTag();
        waterTank.writeToNBT(waterTag);
        compoundTag.put("water", waterTag);
        drillInv.ifPresent(h ->
        {
            CompoundTag tag = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            compoundTag.put("inv", tag);
        });
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        compoundTag.putInt("heat", drillHeat);
        compoundTag.putInt("size", size);
        compoundTag.putBoolean("running", running);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        this.waterTank.readFromNBT(compoundTag.getCompound("water"));
        drillInv.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(compoundTag.getCompound("inv")));
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        this.drillHeat = compoundTag.getInt("heat");
        this.size = compoundTag.getInt("size");
        this.running = compoundTag.getBoolean("running");
        super.load(compoundTag);
    }
}
