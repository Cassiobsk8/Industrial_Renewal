package net.cassiokf.industrialrenewal.block.dam;

import net.cassiokf.industrialrenewal.block.abstracts.BlockAbstractHorizontalFacing;
import net.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamIntake;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockDamIntake extends BlockAbstractHorizontalFacing implements EntityBlock{
    public BlockDamIntake() {
        super(stoneBasicProperties.strength(1f));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide){
            if(player.getMainHandItem().isEmpty()){
                BlockEntity te = worldIn.getBlockEntity(pos);
                if(te != null && te instanceof BlockEntityDamIntake i){
                    if (i.onBlockActivated(player)) return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
//        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.DAM_INTAKE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityDamIntake)blockEntity).tick();
    }
}
