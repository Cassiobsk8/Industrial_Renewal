package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.tileentity.TileEntityGutter;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockGutter extends BlockHorizontalFacing
{
    public static final PropertyBool ACTIVE_LEFT = PropertyBool.create("active_left");
    public static final PropertyBool ACTIVE_RIGHT = PropertyBool.create("active_right");
    public static final PropertyBool ACTIVE_DOWN = PropertyBool.create("active_down");
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 1.0D, 0.75D, 0.3125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.6875D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 0.3125D, 0.75D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.6875D, 0.4375D, 0.0D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB NC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 0.8125D);
    protected static final AxisAlignedBB SC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB WC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.75D, 1.0D);
    protected static final AxisAlignedBB EC_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockGutter(String name, CreativeTabs tab) {
        super(name, tab, Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + ": ");
        tooltip.add(Blocks.WATER.getLocalizedName()
                + (" 1")
                + " mB/t "
                + I18n.format("info.industrialrenewal.gutter"));
        tooltip.add(I18n.format("info.industrialrenewal.gutter2"));
        super.addInformation(stack, player, tooltip, advanced);
    }

    private Boolean downConnected(IBlockAccess world, BlockPos pos) {
        final TileEntity tileEntityS = world.getTileEntity(pos.offset(EnumFacing.DOWN));
        return tileEntityS != null && !tileEntityS.isInvalid() && tileEntityS.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    }

    private Boolean leftConnected(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        Block block = world.getBlockState(pos.offset(face.rotateY().getOpposite())).getBlock();
        if (block instanceof BlockGutter) {
            EnumFacing leftFace = world.getBlockState(pos.offset(face.rotateY().getOpposite())).getValue(FACING);
            return !(leftFace == face);
        }
        return true;
    }

    private Boolean rightConnected(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        Block block = world.getBlockState(pos.offset(face.rotateY())).getBlock();
        if (block instanceof BlockGutter) {
            EnumFacing rightFace = world.getBlockState(pos.offset(face.rotateY())).getValue(FACING);
            return !(rightFace == face);
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        state = state.withProperty(ACTIVE_DOWN, downConnected(world, pos)).withProperty(ACTIVE_LEFT, leftConnected(state, world, pos)).withProperty(ACTIVE_RIGHT, rightConnected(state, world, pos));
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE_LEFT, ACTIVE_RIGHT, ACTIVE_DOWN);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = state.getActualState(worldIn, pos);
        EnumFacing face = actualState.getValue(FACING);
        Boolean active = actualState.getValue(ACTIVE_DOWN);
        if (face == EnumFacing.NORTH) {
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            } else {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            }

        }
        if (face == EnumFacing.SOUTH) {
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            } else {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            }
        }
        if (face == EnumFacing.WEST) {
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            } else {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
        if (face == EnumFacing.EAST) {
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            } else {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = state.getActualState(source, pos);
        EnumFacing face = actualState.getValue(FACING);
        Boolean active = actualState.getValue(ACTIVE_DOWN);
        if (face == EnumFacing.NORTH) {
            if (active) {
                return NC_AABB;
            } else {
                return NORTH_AABB;
            }

        }
        if (face == EnumFacing.SOUTH) {
            if (active) {
                return SC_AABB;
            } else {
                return SOUTH_AABB;
            }
        }
        if (face == EnumFacing.WEST) {
            if (active) {
                return WC_AABB;
            } else {
                return WEST_AABB;
            }
        }
        if (face == EnumFacing.EAST) {
            if (active) {
                return EC_AABB;
            } else {
                return EAST_AABB;
            }
        }
        return null;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityGutter createTileEntity(World world, IBlockState state) {
        return new TileEntityGutter();
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
