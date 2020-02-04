package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityCatWalk;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockCatWalk extends BlockTileEntity<TileEntityCatWalk>
{

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES).map(facing -> PropertyBool.create(facing.getName())).collect(Collectors.toList()));
    protected static final AxisAlignedBB RENDER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1876D, 1.0D);
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.03125D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.5D, 0.03125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 1.5D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 1.5D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D);

    public BlockCatWalk(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.MAIN_HAND)
        {
            Item playerItem = player.getHeldItem(EnumHand.MAIN_HAND).getItem();
            if (playerItem.equals(ModItems.screwDrive))
            {
                TileEntityCatWalk te = (TileEntityCatWalk) world.getTileEntity(pos);
                if (te != null)
                {
                    te.toggleFacing(side);
                    if (!world.isRemote) ItemPowerScrewDrive.playDrillSound(world, pos);
                    world.notifyBlockUpdate(pos, state, state, 2);
                }
            }
            BlockPos posOffset = pos.offset(player.getHorizontalFacing());
            IBlockState stateOffset = world.getBlockState(posOffset);
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catWalk))
                    || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catWalkSteel)))
            {
                if (side == EnumFacing.UP)
                {
                    if (stateOffset.getBlock().isReplaceable(world, posOffset))
                    {
                        world.setBlockState(pos.offset(player.getHorizontalFacing()), getBlockFromItem(playerItem).getDefaultState(), 3);
                        if (!player.isCreative())
                        {
                            player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                        }
                        return true;
                    }
                    return true;
                }
                return false;
            }
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catwalkStair)) || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.catwalkStairSteel)))
            {
                if (stateOffset.getBlock().isAir(stateOffset, world, posOffset))
                {
                    world.setBlockState(posOffset, getBlockFromItem(playerItem).getDefaultState().withProperty(BlockCatwalkStair.FACING, player.getHorizontalFacing()), 3);
                    if (!player.isCreative())
                    {
                        player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                    }
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    protected boolean isValidConnection(final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        TileEntityCatWalk te = (TileEntityCatWalk) world.getTileEntity(ownPos);
        if (te != null && te.isFacingBlackListed(neighbourDirection)) return true;

        IBlockState downstate = world.getBlockState(ownPos.offset(neighbourDirection).down());
        Block nb = neighbourState.getBlock();

        if (neighbourDirection != EnumFacing.UP && neighbourDirection != EnumFacing.DOWN) {
            return nb instanceof BlockCatWalk
                    || nb instanceof BlockDoor
                    || nb instanceof BlockElectricGate
                    || (nb instanceof BlockStairs && (neighbourState.getValue(BlockStairs.FACING) == neighbourDirection || neighbourState.getValue(BlockStairs.FACING) == neighbourDirection.getOpposite()))
                    || (downstate.getBlock() instanceof BlockStairs && downstate.getValue(BlockStairs.FACING) == neighbourDirection.getOpposite())
                    || (nb instanceof BlockCatwalkHatch && neighbourState.getValue(BlockCatwalkHatch.FACING) == neighbourDirection)
                    || (nb instanceof BlockCatwalkGate && neighbourState.getValue(BlockCatwalkGate.FACING) == neighbourDirection.getOpposite())
                    || (nb instanceof BlockCatwalkStair && neighbourState.getValue(BlockCatwalkStair.FACING) == neighbourDirection)
                    || (downstate.getBlock() instanceof BlockCatwalkStair && downstate.getValue(BlockCatwalkStair.FACING) == neighbourDirection.getOpposite())
                    || (downstate.getBlock() instanceof BlockCatwalkLadder && downstate.getValue(BlockCatwalkLadder.FACING) == neighbourDirection.getOpposite())
                    || (nb instanceof BlockCatwalkLadder && neighbourState.getValue(BlockCatwalkLadder.FACING) == neighbourDirection && !neighbourState.getValue(BlockCatwalkLadder.ACTIVE));
        }
        if (neighbourDirection == EnumFacing.DOWN) {
            return nb instanceof BlockCatwalkLadder
                    || nb instanceof BlockLadder
                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorCable || nb instanceof BlockFloorPipe
                    || nb instanceof BlockCatWalk;
        }
        return !(neighbourState.getBlock() instanceof BlockEnergyCable);
    }

    private boolean canConnectTo(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);

        return !isValidConnection(neighbourState, worldIn, ownPos, neighbourDirection);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.VALUES) {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()),
                    canConnectTo(world, pos, facing));
        }
        return state;
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
        return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {

        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        if (isConnected(state, EnumFacing.DOWN)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
        }
        if (isConnected(state, EnumFacing.NORTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
        }
        if (isConnected(state, EnumFacing.SOUTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        }
        if (isConnected(state, EnumFacing.WEST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
        }
        if (isConnected(state, EnumFacing.EAST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
        }

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return RENDER_AABB;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public Class<TileEntityCatWalk> getTileEntityClass()
    {
        return TileEntityCatWalk.class;
    }

    @Nullable
    @Override
    public TileEntityCatWalk createTileEntity(World world, IBlockState state)
    {
        return new TileEntityCatWalk();
    }
}
