package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.abstracts.TileEntityToggleableBase;
import cassiokf.industrialrenewal.util.enums.enumproperty.EnumFaceRotation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockToggleableBase<TE extends TileEntityToggleableBase> extends BlockHorizontalFacing
{
    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final IProperty<EnumFaceRotation> FACE_ROTATION = PropertyEnum.create("face_rotation", EnumFaceRotation.class);
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.125D, 0.875D, 0.875D, 0.875D);

    public BlockToggleableBase(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
        setHardness(3f);
        setResistance(5f);
        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityToggleableBase && (flag || blockIn.getDefaultState().canProvidePower()) && flag != ((TileEntityToggleableBase) te).powered)
        {
            ((TileEntityToggleableBase) te).setPowered(flag);
            if (flag != state.getValue(ACTIVE))
            {
                ((TileEntityToggleableBase) te).setActive(flag);
                worldIn.setBlockState(pos, state.withProperty(ACTIVE, flag), 3);
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, ACTIVE, FACE_ROTATION);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityToggleableBase te = (TileEntityToggleableBase) world.getTileEntity(pos);
        if (te == null) return false;
        te.playSwitchSound();
        boolean active = !state.getValue(ACTIVE);
        state = state.withProperty(ACTIVE, active);
        te.setActive(active);
        world.setBlockState(pos, state, 3);
        world.notifyNeighborsOfStateChange(pos, this, false);
        return true;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        rotateFace(world, pos);
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7)).withProperty(ACTIVE, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getIndex();

        if (state.getValue(ACTIVE))
        {
            i |= 8;
        }

        return i;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BLOCK_AABB;
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        setFacing(world, pos, EnumFacing.getDirectionFromEntityLiving(pos, placer));
        tileEntity.markDirty();
    }

    public EnumFacing getFacing(final IBlockAccess world, final BlockPos pos)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFacing() : EnumFacing.SOUTH;
    }

    public EnumFaceRotation getFaceRotation(final IBlockAccess world, final BlockPos pos)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFaceRotation() : EnumFaceRotation.UP;
    }

    public void setFacing(final IBlockAccess world, final BlockPos pos, final EnumFacing facing)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        if (tileEntity != null)
        {
            tileEntity.setFacing(facing);
        }
    }

    public void setFaceRotation(final IBlockAccess world, final BlockPos pos, final EnumFaceRotation faceRotation)
    {
        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        if (tileEntity != null)
        {
            tileEntity.setFaceRotation(faceRotation);
        }
    }

    private TileEntityToggleableBase getTileEntity(IBlockAccess world, BlockPos pos)
    {
        return (TileEntityToggleableBase) world.getTileEntity(pos);
    }

    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos)
    {
        return state.withProperty(FACING, getFacing(worldIn, pos)).withProperty(FACE_ROTATION, getFaceRotation(worldIn, pos)).withProperty(ACTIVE, state.getValue(ACTIVE));
    }

    public void rotateFace(final World world, final BlockPos pos)
    {
        final EnumFaceRotation faceRotation = getFaceRotation(world, pos);
        setFaceRotation(world, pos, faceRotation.rotateClockwise());
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);
}