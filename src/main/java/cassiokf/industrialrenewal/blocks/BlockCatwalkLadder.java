package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

public class BlockCatwalkLadder extends BlockHorizontalFacing
{
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.03125D, 1.0D);
    protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.03125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockCatwalkLadder(String name, CreativeTabs tab) {
        super(name, tab, Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Item playerItem = entity.inventory.getCurrentItem().getItem();
        if (playerItem.equals(ModItems.screwDrive)) {
            ItemPowerScrewDrive.playDrillSound(world, pos);
            world.setBlockState(pos, state.withProperty(ACTIVE, !state.getValue(ACTIVE)), 3);
            return true;
        }
        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.iladder)) || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.sladder))) {
            BlockPos posOffset = pos.up();
            IBlockState stateOffset = world.getBlockState(posOffset);
            if (stateOffset.getBlock().isAir(stateOffset, world, posOffset) || stateOffset.getBlock().isReplaceable(world, posOffset)) {
                EnumFacing direction = state.getValue(FACING);
                world.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().withProperty(FACING, direction).withProperty(ACTIVE, !OpenIf(world, posOffset)), 3);
                if (!entity.isCreative()) {
                    entity.inventory.clearMatchingItems(playerItem, 0, 1, null);
                }
            }
            return true;
        }
        return false;
    }

    private boolean downConnection(BlockPos pos, IBlockAccess world) {
        Block downB = world.getBlockState(pos.down()).getBlock();
        return !(downB instanceof BlockLadder || downB instanceof BlockCatwalkLadder || downB instanceof BlockCatwalkHatch
                || downB instanceof BlockCatwalkStair || downB instanceof BlockStairs || downB instanceof BlockTrapDoor);
    }

    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        state = state.withProperty(DOWN, downConnection(pos, world));
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, DOWN);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(ACTIVE, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();

        if (state.getValue(ACTIVE)) {
            i |= 4;
        }
        return i;
    }

    protected boolean OpenIf(final IBlockAccess WorldIn, BlockPos ownPos) {
        final BlockPos downpos = ownPos.down();
        final BlockPos twoDownPos = downpos.down();
        final IBlockState downState = WorldIn.getBlockState(downpos);
        final IBlockState twoDownState = WorldIn.getBlockState(twoDownPos);
        return downState.isFullBlock() || (downState.getBlock() instanceof BlockCatwalkLadder && !downState.getValue(ACTIVE) && twoDownState.isFullBlock());
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(ACTIVE, !OpenIf(worldIn, pos));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return LADDER_SOUTH_AABB;
            case SOUTH:
                return LADDER_NORTH_AABB;
            case WEST:
                return LADDER_EAST_AABB;
            case EAST:
            default:
                return LADDER_WEST_AABB;
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = state.getActualState(worldIn, pos);
        EnumFacing face = actualState.getValue(FACING);
        boolean active = actualState.getValue(ACTIVE);
        boolean down = actualState.getValue(DOWN);

        if (face == EnumFacing.NORTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_SOUTH_AABB);
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
        if (face == EnumFacing.SOUTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_NORTH_AABB);
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
        if (face == EnumFacing.WEST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_EAST_AABB);
            if (active) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            }
        }
        if (face == EnumFacing.EAST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, LADDER_WEST_AABB);
            if (active)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            }
        }
        if (down)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
