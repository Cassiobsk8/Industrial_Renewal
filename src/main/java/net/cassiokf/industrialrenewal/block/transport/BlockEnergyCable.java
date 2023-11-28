package net.cassiokf.industrialrenewal.block.transport;

import net.cassiokf.industrialrenewal.block.abstracts.BlockPipeBase;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.util.enums.EnumCableIn;
import net.cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEnergyCable extends BlockPipeBase<BlockEntityEnergyCable> implements EntityBlock {

    public EnumEnergyCableType type;

    public BlockEnergyCable(EnumEnergyCableType type){
        super(metalBasicProperties.strength(0.8f)
                .sound(SoundType.METAL).noOcclusion(), 4, 4);
        this.type = type;
    }

    public static EnumCableIn convertFromType(EnumEnergyCableType type)
    {
        switch (type)
        {
            default:
            case LV:
                return EnumCableIn.LV;
            case MV:
                return EnumCableIn.MV;
            case HV:
                return EnumCableIn.HV;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        int amount;
        switch (type)
        {
            default:
            case LV:
                amount = 256;
                break;
            case MV:
                amount = 1024;
                break;
            case HV:
                amount = 4096;
                break;
        }
        tooltip.add(Component.literal(amount + " FE/t"));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos(), defaultBlockState());

    }

    @Override
    public boolean canConnectToPipe(BlockGetter world, BlockPos pos, Direction facing) {
        Block block = world.getBlockState(pos.relative(facing)).getBlock();
        return (block instanceof BlockEnergyCable && type.equals(((BlockEnergyCable) block).type));
    }
    
    @Override
    public boolean canConnectToCapability(BlockGetter world, BlockPos pos, Direction facing) {
        BlockEntity te = world.getBlockEntity(pos.relative(facing));
        Block block = world.getBlockState(pos.relative(facing)).getBlock();
        return (te != null
                && !(block instanceof BlockEnergyCable)
                && te.getCapability(ForgeCapabilities.ENERGY, facing.getOpposite()).isPresent());
    }
    
    public BlockState getState(Level world, BlockPos pos, BlockState oldState){
        return oldState
                .setValue(UP, canConnectToPipe(world, pos, Direction.UP))
                .setValue(DOWN, canConnectToPipe(world, pos, Direction.DOWN))
                .setValue(NORTH, canConnectToPipe(world, pos, Direction.NORTH))
                .setValue(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH))
                .setValue(EAST, canConnectToPipe(world, pos, Direction.EAST))
                .setValue(WEST, canConnectToPipe(world, pos, Direction.WEST))
                .setValue(CUP, canConnectToCapability(world, pos, Direction.UP))
                .setValue(CDOWN, canConnectToCapability(world, pos, Direction.DOWN))
                .setValue(CNORTH, canConnectToCapability(world, pos, Direction.NORTH))
                .setValue(CSOUTH, canConnectToCapability(world, pos, Direction.SOUTH))
                .setValue(CEAST, canConnectToCapability(world, pos, Direction.EAST))
                .setValue(CWEST, canConnectToCapability(world, pos, Direction.WEST));
        
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        switch (type){
            case LV : return ModBlockEntity.ENERGYCABLE_LV_TILE.get().create(pos, state);
            case MV : return ModBlockEntity.ENERGYCABLE_MV_TILE.get().create(pos, state);
            case HV : return ModBlockEntity.ENERGYCABLE_HV_TILE.get().create(pos, state);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityEnergyCable)blockEntity).tick();
    }
}
