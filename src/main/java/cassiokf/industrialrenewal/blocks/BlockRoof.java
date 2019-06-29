package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRoof extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockRoof(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
        setLightOpacity(255);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, SOUTH, NORTH, EAST, WEST, DOWN);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    private Boolean isEven(BlockPos pos, boolean baseOnX)
    {
        Integer number = pos.getZ();
        if (baseOnX) number = pos.getX();
        if ((number % 2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean canConnectTo(final IBlockAccess world, final BlockPos ownPos, EnumFacing ownFacing, final EnumFacing neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = world.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        if (((neighbourDirection == EnumFacing.EAST || neighbourDirection == EnumFacing.WEST) && (ownFacing == EnumFacing.SOUTH || ownFacing == EnumFacing.NORTH))
                || ((neighbourDirection == EnumFacing.SOUTH || neighbourDirection == EnumFacing.NORTH) && (ownFacing == EnumFacing.WEST || ownFacing == EnumFacing.EAST)))
        {
            IBlockState dState = world.getBlockState(ownPos.offset(EnumFacing.DOWN));
            IBlockState sState = world.getBlockState(ownPos.offset(EnumFacing.SOUTH));
            IBlockState nState = world.getBlockState(ownPos.offset(EnumFacing.NORTH));
            Block sBlock = sState.getBlock();
            Block nBlock = nState.getBlock();
            if ((sBlock instanceof BlockRoof || sBlock instanceof BlockCatwalkLadder || sState.isFullCube()) && (nBlock instanceof BlockRoof || nBlock instanceof BlockCatwalkLadder || nState.isFullCube()))
            {
                // (block pos is Even) && (neighbour SW) && !down connection
                boolean invert = ownFacing == EnumFacing.WEST || ownFacing == EnumFacing.EAST;
                return isEven(ownPos, invert)
                        && (nb instanceof BlockRoof || neighbourState.isFullCube() || nb instanceof BlockPillar || nb instanceof BlockColumn)
                        && !dState.isFullCube();
            }
        }
        return neighbourDirection == EnumFacing.DOWN && neighbourState.isFullCube();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        state = state.withProperty(SOUTH, canConnectTo(world, pos, facing, facing.getOpposite())).withProperty(NORTH, canConnectTo(world, pos, facing, facing))
                .withProperty(EAST, canConnectTo(world, pos, facing, facing.rotateY())).withProperty(WEST, canConnectTo(world, pos, facing, facing.rotateYCCW()))
                .withProperty(DOWN, canConnectTo(world, pos, facing, EnumFacing.DOWN));
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = state.getActualState(worldIn, pos);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
        if (actualState.getValue(DOWN) || actualState.getValue(EAST) || actualState.getValue(WEST))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, BOT_AABB);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = state.getActualState(source, pos);
        if (actualState.getValue(DOWN) || actualState.getValue(EAST) || actualState.getValue(WEST))
        {
            return FULL_AABB;
        } else {
            return BASE_AABB;
        }
    }

    @Deprecated
    public boolean isTopSolid(IBlockState state) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.UP) {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }
}
