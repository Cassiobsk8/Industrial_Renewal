package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityCatWalkStair;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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

public class BlockCatwalkStair extends BlockHorizontalFacing
{
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
        super(name, tab, Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,  BlockState state, PlayerEntity player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand.equals(EnumHand.MAIN_HAND))
        {
            Item playerItem = player.inventory.getCurrentItem().getItem();
            if (playerItem.equals(ModItems.screwDrive))
            {
                TileEntityCatWalkStair te = (TileEntityCatWalkStair) world.getTileEntity(pos);
                if (te != null)
                {
                    te.toggleFacing(side);
                    ItemPowerScrewDrive.playDrillSound(world, pos);
                    world.notifyBlockUpdate(pos, state, state, 2);
                }
            }
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catwalkStair)) || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catwalkStairSteel)))
            {
                BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
                 BlockState stateOffset = world.getBlockState(posOffset);
                if (stateOffset.getBlock().isAir(stateOffset, world, posOffset) || stateOffset.getBlock().isReplaceable(world, posOffset))
                {
                    world.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().withProperty(BlockCatwalkStair.FACING, player.getHorizontalFacing()), 3);
                    if (!player.isCreative())
                    {
                        player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                    }
                    return true;
                }
                return false;
            }
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catWalk)) || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catWalkSteel)))
            {
                BlockPos posOffset = pos.offset(player.getHorizontalFacing()).up();
                 BlockState stateOffset = world.getBlockState(posOffset);
                if (stateOffset.getBlock().isAir(stateOffset, world, posOffset) || stateOffset.getBlock().isReplaceable(world, posOffset))
                {
                    world.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState(), 3);
                    if (!player.isCreative())
                    {
                        player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private Boolean leftConnected(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        TileEntityCatWalkStair te = (TileEntityCatWalkStair) world.getTileEntity(pos);
        if (te != null && te.isFacingBlackListed(face.rotateYCCW())) return false;

        BlockPos posOffset = pos.offset(face.rotateY().getOpposite());
         BlockState stateOffset = world.getBlockState(posOffset);

        if (stateOffset.getBlock() instanceof BlockCatwalkStair)
        {
            EnumFacing leftFace = stateOffset.getValue(FACING);
            return !(leftFace == face);
        }
        return true;
    }

    private Boolean rightConnected(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        TileEntityCatWalkStair te = (TileEntityCatWalkStair) world.getTileEntity(pos);
        if (te != null && te.isFacingBlackListed(face.rotateY())) return false;

        BlockPos posOffset = pos.offset(face.rotateY());
         BlockState stateOffset = world.getBlockState(posOffset);

        if (stateOffset.getBlock() instanceof BlockCatwalkStair)
        {
            EnumFacing rightFace = stateOffset.getValue(FACING);
            return !(rightFace == face);
        }
        return true;
    }

    @Override
    public  BlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        state = state.withProperty(ACTIVE_LEFT, leftConnected(state, world, pos)).withProperty(ACTIVE_RIGHT, rightConnected(state, world, pos));
        return state;
    }
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE_LEFT, ACTIVE_RIGHT);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
         BlockState actualState = getActualState(state, worldIn, pos);

        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);

        EnumFacing face = actualState.getValue(FACING);
        Boolean left = actualState.getValue(ACTIVE_LEFT);
        Boolean right = actualState.getValue(ACTIVE_RIGHT);
        if (face == EnumFacing.NORTH)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
            if (left)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
            if (right)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }

        } else if (face == EnumFacing.SOUTH)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
            if (left)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, EC_AABB);
            }
            if (right)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, WC_AABB);
            }
        } else if (face == EnumFacing.WEST)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
            if (left)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
            if (right)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
        } else if (face == EnumFacing.EAST)
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
            if (left)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, NC_AABB);
            }
            if (right)
            {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, SC_AABB);
            }
        }
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityCatWalkStair createTileEntity(World world,  BlockState state)
    {
        return new TileEntityCatWalkStair();
    }
}
