package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntity;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.TileEntityBoxConnector;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFuseBoxConnector extends BlockTileEntity<TileEntityBoxConnector>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger UPCONDUIT = PropertyInteger.create("up", 0, 2);


    private static final AxisAlignedBB WEST_EAST_BLOCK_AABB = new AxisAlignedBB(0.375D, 0D, 0D, 0.625D, 0.125D, 1D);
    private static final AxisAlignedBB NORTH_SOUTH_BLOCK_AABB = new AxisAlignedBB(0D, 0D, 0.375D, 1D, 0.125D, 0.625D);

    public BlockFuseBoxConnector(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return side == state.getValue(FACING).rotateY() || side == state.getValue(FACING).rotateYCCW();
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        TileEntityBoxConnector te = (TileEntityBoxConnector) world.getTileEntity(pos);
        int value = te.passRedstone();
        return state.getValue(FACING).rotateYCCW() == side ? value : 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (entity.inventory.getCurrentItem().getItem() == ModItems.screwDrive)
        {
            rotateBlock(world, pos, state);
            return true;
        }
        return false;
    }
/*
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, neighborPos);
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityBoxConnector) {
            te.passRedstone();
        }
    }*/

    private void rotateBlock(World world, BlockPos pos, IBlockState state)
    {
        EnumFacing newFace = state.getValue(FACING).getOpposite();
        world.setBlockState(pos, state.withProperty(FACING, newFace));
    }

    private int canConnectConduit(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        IBlockState upState = world.getBlockState(pos.up());
        if (upState.getBlock() instanceof BlockFuseBox || upState.getBlock() instanceof BlockFuseBoxConduitExtension)
        {
            if (state.getValue(FACING) == upState.getValue(FACING))
            {
                return 1;
            }
            if (state.getValue(FACING) == upState.getValue(FACING).getOpposite())
            {
                return 2;
            }
        }
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        state = state.withProperty(UPCONDUIT, canConnectConduit(state, world, pos));
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(UPCONDUIT, 0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, UPCONDUIT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getActualState(source, pos).getValue(FACING))
        {
            default:
            case NORTH:
            case SOUTH:
                return NORTH_SOUTH_BLOCK_AABB;
            case EAST:
            case WEST:
                return WEST_EAST_BLOCK_AABB;
        }
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public TileEntityBoxConnector createTileEntity(World world, IBlockState state)
    {
        return new TileEntityBoxConnector();
    }
}
