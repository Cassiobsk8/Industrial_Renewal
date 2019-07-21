package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockRail;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCatwalkStair extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE_LEFT = PropertyBool.create("active_left");
    public static final PropertyBool ACTIVE_RIGHT = PropertyBool.create("active_right");
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 0.03125D);
    protected static final AxisAlignedBB SC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 2.0D, 1.0D);
    protected static final AxisAlignedBB WC_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 2.0D, 1.0D);
    protected static final AxisAlignedBB EC_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);

    public BlockCatwalkStair(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Item playerItem = player.inventory.getCurrentItem().getItem();
        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catwalkStair)) || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catwalkStairSteel))) {
            BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
            IBlockState stateOffset = world.getBlockState(posOffset);
            if (stateOffset.getBlock().isAir(stateOffset, world, posOffset) || stateOffset.getBlock().isReplaceable(world, posOffset)) {
                world.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().withProperty(BlockCatwalkStair.FACING, player.getHorizontalFacing()), 3);
                if (!player.isCreative()) {
                    player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                }
                return true;
            }
            return false;
        }
        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catWalk)) || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catWalkSteel))) {
            BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
            IBlockState stateOffset = world.getBlockState(posOffset);
            if (stateOffset.getBlock().isAir(stateOffset, world, posOffset) || stateOffset.getBlock().isReplaceable(world, posOffset)) {
                world.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState(), 3);
                if (!player.isCreative()) {
                    player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private Boolean leftConnected(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        BlockPos posOffset = pos.offset(face.rotateY().getOpposite());
        IBlockState stateOffset = world.getBlockState(posOffset);
        Block block = stateOffset.getBlock();
        if (block instanceof BlockRail) {
            return !(stateOffset.getValue(BlockRail.SHAPE).toString().equals("ascending_" + face));
        }
        if (block instanceof BlockCatwalkStair) {
            EnumFacing leftFace = stateOffset.getValue(FACING);
            return !(leftFace == face);
        }
        return true;
    }

    private Boolean rightConnected(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        BlockPos posOffset = pos.offset(face.rotateY());
        IBlockState stateOffset = world.getBlockState(posOffset);
        Block block = stateOffset.getBlock();
        if (block instanceof BlockRail) {
            return !(stateOffset.getValue(BlockRail.SHAPE).toString().equals("ascending_" + face));
        }
        if (block instanceof BlockCatwalkStair) {
            EnumFacing rightFace = stateOffset.getValue(FACING);
            return !(rightFace == face);
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        state = state.withProperty(ACTIVE_LEFT, leftConnected(state, world, pos)).withProperty(ACTIVE_RIGHT, rightConnected(state, world, pos));
        return state;
    }
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE_LEFT, ACTIVE_RIGHT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        return i;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = getActualState(state, worldIn, pos);

        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);

        EnumFacing face = actualState.getValue(FACING);
        Boolean left = actualState.getValue(ACTIVE_LEFT);
        Boolean right = actualState.getValue(ACTIVE_RIGHT);
        if (face == EnumFacing.NORTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }

        }
        if (face == EnumFacing.SOUTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
        }
        if (face == EnumFacing.WEST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
        }
        if (face == EnumFacing.EAST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            if (left) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
            if (right) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
        }
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
