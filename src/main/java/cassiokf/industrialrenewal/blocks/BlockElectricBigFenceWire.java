package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockElectricBigFenceWire extends BlockBasicElectricFence {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger INDEX = PropertyInteger.create("index", 0, 2);

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB CBASE_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 1.0D, 0.9D);

    public BlockElectricBigFenceWire(String name, CreativeTabs tab) {
        super(name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(INDEX) == 0) {
            worldIn.setBlockState(pos.up(), state.withProperty(INDEX, 1));
            worldIn.setBlockState(pos.up(2), state.withProperty(INDEX, 2));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        switch (state.getValue(INDEX)) {
            case 0:
                if (IsBigFence(worldIn, pos.up())) worldIn.setBlockToAir(pos.up());
                if (IsBigFence(worldIn, pos.up(2))) worldIn.setBlockToAir(pos.up(2));
                break;
            case 1:
                if (IsBigFence(worldIn, pos.down())) worldIn.setBlockToAir(pos.down());
                if (IsBigFence(worldIn, pos.up())) worldIn.setBlockToAir(pos.up());
                break;
            case 2:
                if (IsBigFence(worldIn, pos.down())) worldIn.setBlockToAir(pos.down());
                if (IsBigFence(worldIn, pos.down(2))) worldIn.setBlockToAir(pos.down(2));
                break;
        }
        super.breakBlock(worldIn, pos, state);

    }

    private boolean IsBigFence(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceWire;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
                && worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())
                && worldIn.getBlockState(pos.up(2)).getBlock().isReplaceable(worldIn, pos.up(2));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BASE_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        addCollisionBoxToList(pos, entityBox, collidingBoxes, CBASE_AABB);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, INDEX);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(INDEX, 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        int directionIndex = meta;
        if (meta > 3 && meta < 8) directionIndex -= 4;
        if (meta > 7) directionIndex -= 8;
        int index = 0;
        if (meta > 3 && meta < 8) index = 1;
        if (meta > 7) index = 2;
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(directionIndex)).withProperty(INDEX, index);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(INDEX) == 1) i += 4;
        if (state.getValue(INDEX) == 2) i += 8;
        return i;
    }
}
