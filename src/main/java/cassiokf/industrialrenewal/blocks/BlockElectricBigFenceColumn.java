package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class BlockElectricBigFenceColumn extends BlockBasicElectricFence {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public static final PropertyInteger INDEX = PropertyInteger.create("index", 0, 2);

    public static final IUnlistedProperty<Boolean> CORE = new Properties.PropertyAdapter<>(PropertyBool.create("core"));
    public static final IUnlistedProperty<Boolean> ACTIVE_LEFT = new Properties.PropertyAdapter<>(PropertyBool.create("active_left"));
    public static final IUnlistedProperty<Boolean> ACTIVE_RIGHT = new Properties.PropertyAdapter<>(PropertyBool.create("active_right"));
    public static final IUnlistedProperty<Boolean> ACTIVE_LEFT_TOP = new Properties.PropertyAdapter<>(PropertyBool.create("active_left_top"));
    public static final IUnlistedProperty<Boolean> ACTIVE_RIGHT_TOP = new Properties.PropertyAdapter<>(PropertyBool.create("active_right_top"));
    public static final IUnlistedProperty<Boolean> ACTIVE_LEFT_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("active_left_down"));
    public static final IUnlistedProperty<Boolean> ACTIVE_RIGHT_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("active_right_down"));


    public BlockElectricBigFenceColumn(String name, CreativeTabs tab) {
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

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    public boolean IsBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceColumn;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
                && worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())
                && worldIn.getBlockState(pos.up(2)).getBlock().isReplaceable(worldIn, pos.up(2));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[]{FACING, INDEX}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{CORE, ACTIVE_LEFT, ACTIVE_RIGHT, ACTIVE_LEFT_TOP, ACTIVE_RIGHT_TOP, ACTIVE_LEFT_DOWN, ACTIVE_RIGHT_DOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
    }

    @Override
    @Deprecated
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(INDEX) == 2 ? 15 : 0;
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
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(directionIndex)).withProperty(INDEX, index);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(INDEX) == 1) i += 4;
        if (state.getValue(INDEX) == 2) i += 8;
        return i;
    }

    public boolean ActiveSide(IBlockAccess world, BlockPos pos, IBlockState state, boolean left, boolean top, boolean down)
    {
        int index = state.getValue(INDEX);
        if (!top && index == 2) return false;
        if (top && index != 2) return false;
        if (!down && index == 0) return false;
        if (down && index != 0) return false;
        EnumFacing facing = state.getValue(FACING);
        for (final EnumFacing face : EnumFacing.HORIZONTALS) {
            if ((left && face == facing.rotateYCCW()) || (!left && face == facing.rotateY())) {
                IBlockState sideState = world.getBlockState(pos.offset(face));
                Block block = sideState.getBlock();
                return sideState.isFullBlock() || block instanceof BlockElectricGate || block instanceof BlockBasicElectricFence;
            }
        }
        return false;
    }

    private boolean isCore(IBlockState state)
    {
        int index = state.getValue(INDEX);
        return index == 1;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) state;
            return eState.withProperty(CORE, isCore(state))
                    .withProperty(ACTIVE_LEFT, ActiveSide(world, pos, state, true, false, false))
                    .withProperty(ACTIVE_RIGHT, ActiveSide(world, pos, state, false, false, false))
                    .withProperty(ACTIVE_LEFT_TOP, ActiveSide(world, pos, state, true, true, false))
                    .withProperty(ACTIVE_RIGHT_TOP, ActiveSide(world, pos, state, false, true, false))
                    .withProperty(ACTIVE_LEFT_DOWN, ActiveSide(world, pos, state, true, false, true))
                    .withProperty(ACTIVE_RIGHT_DOWN, ActiveSide(world, pos, state, false, false, true));
        }
        return state;
    }
}
