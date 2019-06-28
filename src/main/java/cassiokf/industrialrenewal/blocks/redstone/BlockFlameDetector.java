package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityFlameDetector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockFlameDetector extends BlockTileEntity<TileEntityFlameDetector>
{

    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final IProperty<EnumFacing> BASE = PropertyDirection.create("base");

    public BlockFlameDetector(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.UP));

    }

    private boolean getActiveness(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityFlameDetector te = (TileEntityFlameDetector) world.getTileEntity(pos);
        if (te == null) {
            return false;
        }
        return te.passRedstone();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        TileEntityFlameDetector te = (TileEntityFlameDetector) world.getTileEntity(pos);
        return state.withProperty(ACTIVE, getActiveness(state, world, pos)).withProperty(BASE, te.getBlockFacing());
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return side != state.getValue(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        boolean active = state.getActualState(world, pos).getValue(ACTIVE);
        if (!active) {
            return 0;
        }
        return 15;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos) {
        IBlockState neighborState = worldIn.getBlockState(neighborPos);
        Block neighborBlock = neighborState.getBlock();
        if (neighborBlock instanceof BlockFire || neighborBlock instanceof BlockAir) {
            worldIn.notifyNeighborsOfStateChange(pos, this, true);
        }
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, BASE);
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing theFace;
        if (placer.isSneaking()) {
            theFace = facing;
        } else {
            theFace = placer.getHorizontalFacing();
        }
        return this.getDefaultState().withProperty(FACING, theFace).withProperty(BASE, facing.getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityFlameDetector te = (TileEntityFlameDetector) worldIn.getTileEntity(pos);
        EnumFacing facing = state.getValue(BASE);
        te.setBlockFacing(facing);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public Class<TileEntityFlameDetector> getTileEntityClass() {
        return TileEntityFlameDetector.class;
    }

    @Nullable
    @Override
    public TileEntityFlameDetector createTileEntity(World world, IBlockState state) {
        return new TileEntityFlameDetector();
    }
}