package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRailGate extends BlockNormalRailBase
{

    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    protected static final AxisAlignedBB CNORTH_AABB = new AxisAlignedBB(-0.25D, 0.0D, 0.375D, 1.25D, 2D, 0.625D);
    protected static final AxisAlignedBB CWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, -0.25D, 0.625D, 2D, 1.25D);
    protected String name;

    public BlockRailGate(Block.Properties properties)
    {
        super(properties);
        setDefaultState(getDefaultState().with(OPEN, false));
    }
/*
    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        EnumRailDirection direction = state.get(SHAPE);
        if (!state.get(OPEN))
        {
            switch (direction)
            {
                case NORTH_SOUTH:
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, CNORTH_AABB);
                    break;
                case EAST_WEST:
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, CWEST_AABB);
                    break;
            }
        }
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos)
    {
        EnumRailDirection direction = state.get(SHAPE);
        switch (direction)
        {
            default:
            case NORTH_SOUTH:
                return CNORTH_AABB;
            case EAST_WEST:
                return CWEST_AABB;
        }
    }
*/

    @Override
    protected void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        boolean flag1 = state.get(OPEN);
        boolean flag2 = worldIn.isBlockPowered(pos);
        if (flag1 != flag2)
        {
            worldIn.setBlockState(pos, state.with(OPEN, flag2), 3);
            //Sound
            Random r = new Random();
            float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
            if (flag2)
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F, pitch);
            } else
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F, pitch);
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(OPEN, SHAPE, SNOW);
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }
}