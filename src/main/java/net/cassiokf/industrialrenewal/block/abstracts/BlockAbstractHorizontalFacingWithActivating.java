package net.cassiokf.industrialrenewal.block.abstracts;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockAbstractHorizontalFacingWithActivating extends BlockAbstractHorizontalFacing{

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockAbstractHorizontalFacingWithActivating(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if (!worldIn.isClientSide) {
            if (state.getValue(ACTIVE))
            {
                //worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_CLOSE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            } else
            {
                //worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_OPEN.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            state = state.cycle(ACTIVE);
            worldIn.setBlock(pos, state, 2);
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    //    @Override
//    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
//    {
//        return worldIn.getBlockState(pos).get(ACTIVE);
//    }
}
