package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.enums.enumproperty.EnumFaceRotation;
import cassiokf.industrialrenewal.tileentity.TileEntityToggleableBase;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockToggleableBase<TE extends TileEntityToggleableBase> extends BlockTileEntity<TE>
{

    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final IProperty<EnumFaceRotation> FACE_ROTATION = PropertyEnum.create("face_rotation", EnumFaceRotation.class);
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.125D, 0.875D, 0.875D, 0.875D);

    public BlockToggleableBase(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setHardness(3f);
        setResistance(5f);
        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, ACTIVE, FACE_ROTATION);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();

            TileEntityToggleableBase te = getTileEntity(world, pos);
            te.playSwitchSound();
            boolean active = !state.getValue(ACTIVE);
            state = state.withProperty(ACTIVE, active);
            te.setActive(active);
            world.setBlockState(pos, state, 3);
            spawnPartivle(world, i, j, k);
            world.notifyNeighborsOfStateChange(pos, this, false);
            return true;
        }
        return false;
    }

    public void spawnPartivle(World world, double i, double j, double k)
    {
        world.spawnParticle(EnumParticleTypes.WATER_DROP, (double) i, (double) j, (double) k, 1.0D, 1.0D, 1.0D);
    }

    public void setFace(World world, BlockPos pos)
    {
        TileEntityToggleableBase tileEntity = getTileEntity(world, pos);
        EnumFacing vFace = getFacing(world, pos);
        EnumFaceRotation rFace = getFaceRotation(world, pos);
        if (vFace == EnumFacing.UP || vFace == EnumFacing.DOWN)
        {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN)
            {
                tileEntity.activeFacing(EnumFacing.EAST);
                tileEntity.activeFacing(EnumFacing.WEST);
                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
            } else
            {
                tileEntity.activeFacing(EnumFacing.SOUTH);
                tileEntity.activeFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
            }
        }
        if (vFace == EnumFacing.NORTH || vFace == EnumFacing.SOUTH)
        {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN)
            {
                tileEntity.activeFacing(EnumFacing.EAST);
                tileEntity.activeFacing(EnumFacing.WEST);
                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
            }
            if (rFace == EnumFaceRotation.LEFT || rFace == EnumFaceRotation.RIGHT)
            {
                tileEntity.activeFacing(EnumFacing.UP);
                tileEntity.activeFacing(EnumFacing.DOWN);
                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
            }
        }
        if (vFace == EnumFacing.WEST || vFace == EnumFacing.EAST)
        {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN)
            {
                tileEntity.activeFacing(EnumFacing.NORTH);
                tileEntity.activeFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
            }
            if (rFace == EnumFaceRotation.LEFT || rFace == EnumFaceRotation.RIGHT)
            {
                tileEntity.activeFacing(EnumFacing.UP);
                tileEntity.activeFacing(EnumFacing.DOWN);
                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
            }
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        rotateFace(world, pos);
        setFace(world, pos);
        return true;
    }

    @SuppressWarnings("deprecation")
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

    @SuppressWarnings("deprecation")
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

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return false;
    }


    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack)
    {

        final TileEntityToggleableBase tileEntity = getTileEntity(world, pos);

        setFacing(world, pos, EnumFacing.getDirectionFromEntityLiving(pos, placer));
        setFace(world, pos);

        tileEntity.markDirty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTopSolid(final IBlockState state)
    {
        return false;
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

    @SuppressWarnings("deprecation")
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
}