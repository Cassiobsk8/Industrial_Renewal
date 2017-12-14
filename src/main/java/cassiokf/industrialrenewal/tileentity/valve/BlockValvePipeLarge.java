package cassiokf.industrialrenewal.tileentity.valve;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.util.EnumFaceRotation;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class BlockValvePipeLarge extends BlockTileEntity<TileEntityValvePipeLarge> {


    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final IProperty<EnumFaceRotation> FACE_ROTATION = PropertyEnum.create("face_rotation", EnumFaceRotation.class);
    public static final PropertyBool ACTIVE = PropertyBool.create("active");


    public BlockValvePipeLarge(String name) {
        super(Material.IRON, name);
        setHardness(3f);
        setResistance(5f);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FACE_ROTATION, EnumFaceRotation.UP).withProperty(ACTIVE, false));
    }
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, FACE_ROTATION);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (entity.isSneaking()) { // If the player is sneaking, rotate the face
            boolean Vactive = state.getValue(ACTIVE);
            rotateFace(world, pos);
            resetFace(world, pos);
            setFace(world,pos);
            if (!Vactive) {
                shutDown(world, pos);
            }
            return true;
        } else {

            int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();

            world.playSound((EntityPlayer) null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:valve")), SoundCategory.BLOCKS, 1.0F, 1.0F);
            boolean Vactive = state.getValue(ACTIVE);
            if (!Vactive) {
                world.setBlockState(pos, state.withProperty(ACTIVE, true));
                resetFace(world, pos);
                setFace(world,pos);
                //tileEntity.fill(tileEntity.drain(250,true),true);
            } else {
                world.setBlockState(pos, state.withProperty(ACTIVE, false));
                resetFace(world, pos);
                setFace(world,pos);
                shutDown(world, pos);
            }
            return true;
        }
    }

    public void shutDown(World world,BlockPos pos) {
        EnumFacing vFace = world.getBlockState(pos).getValue(FACING);
        EnumFaceRotation rFace = getFaceRotation(world,pos);
        final TileEntityValvePipeLarge tileEntity = getTileEntity(world, pos);

        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.UP)||(vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.DOWN)||(vFace == EnumFacing.UP && rFace == EnumFaceRotation.UP)||(vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.UP)) {
            tileEntity.toggleFacing(EnumFacing.EAST);
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.DOWN)||(vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.UP)||(vFace == EnumFacing.UP && rFace == EnumFaceRotation.DOWN)||(vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.DOWN)) {
            tileEntity.toggleFacing(EnumFacing.WEST);
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.LEFT)||(vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.LEFT)||(vFace == EnumFacing.WEST && rFace == EnumFaceRotation.LEFT)||(vFace == EnumFacing.EAST && rFace == EnumFaceRotation.LEFT)) {
            tileEntity.toggleFacing(EnumFacing.DOWN);
        }
        if ((vFace == EnumFacing.NORTH && rFace == EnumFaceRotation.RIGHT)||(vFace == EnumFacing.SOUTH && rFace == EnumFaceRotation.RIGHT)||(vFace == EnumFacing.WEST && rFace == EnumFaceRotation.RIGHT)||(vFace == EnumFacing.EAST && rFace == EnumFaceRotation.RIGHT)) {
            tileEntity.toggleFacing(EnumFacing.UP);
        }
        if ((vFace == EnumFacing.EAST && rFace == EnumFaceRotation.UP)||(vFace == EnumFacing.WEST && rFace == EnumFaceRotation.DOWN)||(vFace == EnumFacing.UP && rFace == EnumFaceRotation.RIGHT)||(vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.LEFT)) {
            tileEntity.toggleFacing(EnumFacing.SOUTH);
        }
        if ((vFace == EnumFacing.WEST && rFace == EnumFaceRotation.UP)||(vFace == EnumFacing.EAST && rFace == EnumFaceRotation.DOWN)||(vFace == EnumFacing.UP && rFace == EnumFaceRotation.LEFT)||(vFace == EnumFacing.DOWN && rFace == EnumFaceRotation.RIGHT)) {
            tileEntity.toggleFacing(EnumFacing.NORTH);
        }
    }

    public void setFace(World world, BlockPos pos) {
        final TileEntityValvePipeLarge tileEntity = getTileEntity(world, pos);
        EnumFacing vFace = world.getBlockState(pos).getValue(FACING);
        EnumFaceRotation rFace = getFaceRotation(world,pos);
        if (vFace == EnumFacing.NORTH || vFace == EnumFacing.SOUTH) {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN) {
                tileEntity.activeFacing(EnumFacing.EAST);
                tileEntity.activeFacing(EnumFacing.WEST);
                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
            }
            if (rFace == EnumFaceRotation.LEFT || rFace == EnumFaceRotation.RIGHT) {
                tileEntity.activeFacing(EnumFacing.UP);
                tileEntity.activeFacing(EnumFacing.DOWN);

                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
            }
        }
        if (vFace == EnumFacing.WEST || vFace == EnumFacing.EAST) {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN) {
                tileEntity.activeFacing(EnumFacing.NORTH);
                tileEntity.activeFacing(EnumFacing.SOUTH);

                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
            }
            if (rFace == EnumFaceRotation.LEFT || rFace == EnumFaceRotation.RIGHT) {
                tileEntity.activeFacing(EnumFacing.UP);
                tileEntity.activeFacing(EnumFacing.DOWN);

                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
            }
        }
        if (vFace == EnumFacing.UP || vFace == EnumFacing.DOWN) {
            if (rFace == EnumFaceRotation.UP || rFace == EnumFaceRotation.DOWN) {
                tileEntity.activeFacing(EnumFacing.EAST);
                tileEntity.activeFacing(EnumFacing.WEST);

                tileEntity.disableFacing(EnumFacing.NORTH);
                tileEntity.disableFacing(EnumFacing.SOUTH);
                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
            }
            if (rFace == EnumFaceRotation.LEFT || rFace == EnumFaceRotation.RIGHT) {
                tileEntity.activeFacing(EnumFacing.NORTH);
                tileEntity.activeFacing(EnumFacing.SOUTH);

                tileEntity.disableFacing(EnumFacing.UP);
                tileEntity.disableFacing(EnumFacing.DOWN);
                tileEntity.disableFacing(EnumFacing.EAST);
                tileEntity.disableFacing(EnumFacing.WEST);
            }
        }
    }
    public void resetFace(World world, BlockPos pos) {
        final TileEntityValvePipeLarge tileEntity = getTileEntity(world, pos);
        tileEntity.disableFacing(EnumFacing.NORTH);
        tileEntity.disableFacing(EnumFacing.SOUTH);
        tileEntity.disableFacing(EnumFacing.EAST);
        tileEntity.disableFacing(EnumFacing.WEST);
        tileEntity.disableFacing(EnumFacing.UP);
        tileEntity.disableFacing(EnumFacing.DOWN);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront((meta > 8) ? meta - 8 : meta);
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingbits = state.getValue(FACING).getIndex();
        return facingbits;
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }
    @Override
    public Class<TileEntityValvePipeLarge> getTileEntityClass() {
        return TileEntityValvePipeLarge.class;
    }

    @Nullable
    @Override
    public TileEntityValvePipeLarge createTileEntity(World world, IBlockState state) {
        return new TileEntityValvePipeLarge();
    }

    @Nullable
    private IFluidHandler getFluidHandler(final IBlockAccess world, final BlockPos pos) {
        return getCapability(getTileEntity(world, pos), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        /** Arrumar essa porcaria de setFace e shutDown **/

        setFacing(world, pos, EnumFacing.getDirectionFromEntityLiving(pos, placer));
        resetFace(world, pos);
        setFace(world,pos);
        shutDown(world, pos);
        final IFluidHandler fluidHandler = getFluidHandler(world, pos);
        if (fluidHandler != null) {
            FluidUtil.tryEmptyContainer(stack, fluidHandler, Integer.MAX_VALUE, null, true);
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean isTopSolid(final IBlockState state) {
        return false;
    }
    @Nullable
    public static <T> T getCapability(@Nullable final ICapabilityProvider provider, final Capability<T> capability, @Nullable final EnumFacing facing) {
        return provider != null && provider.hasCapability(capability, facing) ? provider.getCapability(capability, facing) : null;
    }

    public EnumFacing getFacing(final IBlockAccess world, final BlockPos pos) {
        final TileEntityValvePipeLarge tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFacing() : EnumFacing.NORTH;
    }
    public EnumFaceRotation getFaceRotation(final IBlockAccess world, final BlockPos pos) {
        final TileEntityValvePipeLarge tileEntity = getTileEntity(world, pos);
        return tileEntity != null ? tileEntity.getFaceRotation() : EnumFaceRotation.UP;
    }
    public void setFacing(final IBlockAccess world, final BlockPos pos, final EnumFacing facing) {
        final TileEntityValvePipeLarge tileEntity = getTileEntity(world, pos);
        if (tileEntity != null) {
            tileEntity.setFacing(facing);
        }
    }
    public void setFaceRotation(final IBlockAccess world, final BlockPos pos, final EnumFaceRotation faceRotation) {
        final TileEntityValvePipeLarge tileEntity = getTileEntity(world, pos);
        if (tileEntity != null) {
            tileEntity.setFaceRotation(faceRotation);
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(FACING, getFacing(worldIn, pos)).withProperty(FACE_ROTATION, getFaceRotation(worldIn, pos)).withProperty(ACTIVE, state.getValue(ACTIVE));
    }
    public void rotateFace(final World world, final BlockPos pos) {
        final EnumFaceRotation faceRotation = getFaceRotation(world, pos);
        setFaceRotation(world, pos, faceRotation.rotateClockwise());
    }

    @Override
    public boolean rotateBlock(final World world, final BlockPos pos, final EnumFacing axis) {
        final EnumFacing facing = getFacing(world, pos);
        setFacing(world, pos, facing.rotateAround(axis.getAxis()));

        return true;
    }
}