package net.cassiokf.industrialrenewal.blockentity.dam;

import net.cassiokf.industrialrenewal.block.dam.BlockDamIntake;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.util.Utils;
import net.cassiokf.industrialrenewal.util.capability.CustomFluidTank;
import net.cassiokf.industrialrenewal.util.capability.CustomPressureFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockEntityDamIntake extends BlockEntitySyncable {
    
    //private static final Map<BlockPos, BlockPos> WALLS = new HashMap<>();
    
    public BlockEntityDamIntake(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DAM_INTAKE.get(), pos, state);
    }
    
    private final List<BlockPos> connectedWalls = new CopyOnWriteArrayList<>();
    private final List<BlockPos> failBlocks = new CopyOnWriteArrayList<>();
    private final List<BlockPos> failWaters = new CopyOnWriteArrayList<>();
    private int waterAmount = -1;
    private BlockPos neighborPos = null;
    private int concreteAmount;
    private boolean firstTick = false;
    
    
    public final CustomPressureFluidTank tank = new CustomPressureFluidTank(0) {
        
        @Override
        public boolean canDrain() {
            return false;
        }
        
        @Override
        public boolean canFill() {
            return false;
        }
        
        @Override
        public boolean canPassCompressedFluid() {
            return false;
        }
    };
    
    public final LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(() -> tank);
    
    private static final int WIDTH = 13;
    private static final int HEIGHT = 11;
    private static final int DEPTH = 10;
    private static final int MAX_WATER = WIDTH * HEIGHT * DEPTH;
    // 40 buckets per tick; max 160 min 8
    public static int MAX_WATER_PRODUCTION = 10000;
    public int currentProduction = 0;
    public int tick = 0;
    
    public void tick() {
        if (level == null) return;
        if (!firstTick) {
            firstTick = true;
            firstTick();
        }
        if (!level.isClientSide && getWaterAmount() > 0) {
            passFluidOut();
        }
    }
    
    public void firstTick() {
        initializeMultiblockIfNecessary(true);
    }
    
    private void passFluidOut() {
        BlockEntity tileBehind = level.getBlockEntity(getOutPutPos());
        if (tileBehind != null) {
            IFluidHandler f = tileBehind.getCapability(ForgeCapabilities.FLUID_HANDLER, getFacing()).orElse(null);
            if (f != null) {
                if (f instanceof CustomPressureFluidTank c) {
                    c.receiveCompressedFluid(waterAmount, worldPosition.getY(), IFluidHandler.FluidAction.EXECUTE);
                    return;
                }
                f.fill(new FluidStack(Fluids.WATER, waterAmount), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
    
    private BlockPos getOutPutPos() {
        if (neighborPos != null) return neighborPos;
        return neighborPos = worldPosition.relative(getFacing().getOpposite());
    }
    
    public boolean onBlockActivated(Player player) {
        if (level == null || level.isClientSide()) return true;
        cleanWallCached();
        initializeMultiblockIfNecessary(true);
        int percentage = waterAmount / 100;
        boolean done = false;
        if (percentage < 100 && failWaters.size() > 0 && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.WATER_BUCKET) {
            if (tryFillDam(player)) done = true;
        }
        Utils.sendChatMessage(player, "Efficiency: " + percentage + "%");
        if (done) return true;
        
        if (percentage < 100) {
            if (concreteAmount < 143) {
                sendFailBlockMessage(player);
            } else {
                sendFailWaterBlockMessage(player);
            }
        }
        return true;
    }
    
    private boolean tryFillDam(Player player) {
        BlockPos bPos = getNextReplaceableBlockForWater();
        if (bPos != null) {
            BlockState state = level.getBlockState(bPos);
            if (state.canBeReplaced()) {
                level.setBlock(bPos, Blocks.WATER.defaultBlockState(), 3);
                failWaters.remove(bPos);
                
                if (!player.isCreative()) {
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    player.addItem(new ItemStack(Items.BUCKET));
                }
                return true;
            }
        }
        return false;
    }
    
    private void sendFailBlockMessage(Player player) {
        Utils.sendChatMessage(player, "Concrete Amount: " + concreteAmount + " / 143");
        for (BlockPos bPos : failBlocks) {
            Block block = level.getBlockState(bPos).getBlock();
            String msg;
            if (block.equals(ModBlocks.CONCRETE.get()))
                msg = "Concrete at " + bPos.toShortString() + " already used by another Intake";
            else msg = block.getDescriptionId() + " at: " + bPos.toShortString() + " is not a valid dam block";
            Utils.sendChatMessage(player, msg);
            break;
        }
    }
    
    private void sendFailWaterBlockMessage(Player player) {
        Utils.sendChatMessage(player, "Water Amount: " + waterAmount + " / 1000");
        for (BlockPos bPos : failWaters) {
            Utils.sendChatMessage(player, "Block at: " + bPos.toShortString() + " is not a water source");
        }
    }
    
    private BlockPos getNextReplaceableBlockForWater() {
        for (BlockPos bPos : failWaters) {
            BlockState state = level.getBlockState(bPos);
            if (state.canBeReplaced()) return bPos;
        }
        return null;
    }
    
    public int getWaterAmount() {
        initializeMultiblockIfNecessary(false);
        return waterAmount;
    }
    
    private void initializeMultiblockIfNecessary(boolean forced) {
        if (level == null || level.isClientSide()) return;
        if (waterAmount < 0 || forced) {
            cleanWallCached();
            
            Direction facing = getFacing();
            
            searchForConcrete(facing);
            searchForWater(facing);
        }
    }
    
    private void searchForWater(Direction facing) {
        failWaters.clear();
        waterAmount = 0;
        for (BlockPos wall : connectedWalls) {
            int f = 1;
            while (level.getBlockState(wall.relative(facing, f)).getBlock().equals(Blocks.WATER)
                    && level.getBlockState(wall.relative(facing, f)).getFluidState().isSource()
                    && f <= 10) {
                f++;
            }
            if (f < 10) failWaters.add(wall.relative(getFacing(), f));
            waterAmount += f - 1;
        }
        waterAmount = (int) (Utils.normalizeClamped(waterAmount, 0, 1430) * MAX_WATER_PRODUCTION);
    }
    
    private void searchForConcrete(Direction facing) {
        BlockPos pos = worldPosition;
        failBlocks.clear();
        for (int x = -6; x <= 6; x++) {
            for (int y = 0; y <= 10; y++) {
                BlockPos cPos = (facing == Direction.NORTH || facing == Direction.SOUTH)
                        ? (new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ()))
                        : (new BlockPos(pos.getX(), pos.getY() + y, pos.getZ() + x));
                
                BlockState state = level.getBlockState(cPos);
                if ((state.getBlock().equals(ModBlocks.CONCRETE.get()) || Utils.isSamePosition(cPos, pos))) {
//                    WALLS.put(cPos, pos);
                    connectedWalls.add(cPos);
                } else {
                    failBlocks.add(cPos);
                }
            }
        }
        concreteAmount = connectedWalls.size();
    }
    
    private void cleanWallCached() {
//        for (BlockPos wall : connectedWalls) {
//            WALLS.remove(wall);
//        }
        connectedWalls.clear();
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("production", currentProduction);
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        currentProduction = compoundTag.getInt("production");
        super.load(compoundTag);
    }
    
    
    public Direction getFacing() {
        return getBlockState().is(ModBlocks.DAM_INTAKE.get()) ? getBlockState().getValue(BlockDamIntake.FACING) : Direction.NORTH;
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side == null) return super.getCapability(cap, side);
        
        if (cap == ForgeCapabilities.FLUID_HANDLER && side == getFacing().getOpposite()) return tankHandler.cast();
        return super.getCapability(cap, side);
    }
}
