package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.BlockBase;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockButtonRed extends BlockBase
{

    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final PropertyBool PRESS = PropertyBool.create("press");
    private static final AxisAlignedBB DOWN_BLOCK_AABB = new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 0.4375F, 0.875F);
    private static final AxisAlignedBB UP_BLOCK_AABB = new AxisAlignedBB(0.125F, 1, 0.125F, 0.875F, 0.5625F, 0.875F);
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125F, 0.125F, 0.5625F, 0.875F, 0.875F);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.5625F, 0.875F, 0.875F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.4375F, 0.875F, 0.875F, 0);

    public BlockButtonRed(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) return true;
        world.setBlockState(pos, state.withProperty(PRESS, !state.getValue(PRESS)));
        world.playSound(null, pos, IRSoundRegister.TILEENTITY_VALVE_CHANGE, SoundCategory.BLOCKS, 1f * IRConfig.MainConfig.Sounds.masterVolumeMult, 1f);
        world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING)), this, true);
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        boolean active = !state.getActualState(world, pos).getValue(PRESS);
        return active ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing dir = state.getValue(FACING);
        switch (dir)
        {
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
            case DOWN:
                return DOWN_BLOCK_AABB;
            default:
                return UP_BLOCK_AABB;
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, PRESS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta > 6 ? meta - 7 : meta)).withProperty(PRESS, meta > 6);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i;
        i = state.getValue(FACING).getIndex();
        if (state.getValue(PRESS))
        {
            i += 7;
        }
        return i;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(PRESS, true);
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
}
