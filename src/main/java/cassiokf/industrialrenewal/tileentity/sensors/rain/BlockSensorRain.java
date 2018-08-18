package cassiokf.industrialrenewal.tileentity.sensors.rain;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSensorRain extends BlockTileEntity<TileEntitySensorRain> {

    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    public BlockSensorRain(String name, CreativeTabs tab) {
        super(Material.CIRCUITS, name, tab);
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return state.getValue(POWER);
    }

    public void updatePower(World worldIn, BlockPos pos, int currentStrength) {
        if (worldIn.isRainingAt(pos) && worldIn.canSeeSky(pos)) {
            IBlockState state = worldIn.getBlockState(pos);
            int value = (int) (worldIn.rainingStrength * 15);
            if (value != currentStrength) {
                worldIn.setBlockState(pos, state.withProperty(POWER, value), 3);
            }
        } else if (currentStrength != 0) {
            IBlockState state = worldIn.getBlockState(pos);
            worldIn.setBlockState(pos, state.withProperty(POWER, 0), 3);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWER);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWER);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        world.scheduleUpdate(new BlockPos(i, j, k), this, this.tickRate(world));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BLOCK_AABB;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
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
    public Class<TileEntitySensorRain> getTileEntityClass() {
        return TileEntitySensorRain.class;
    }

    @Nullable
    @Override
    public TileEntitySensorRain createTileEntity(World world, IBlockState state) {
        return new TileEntitySensorRain();
    }
}
