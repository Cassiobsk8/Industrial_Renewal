package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BlockIndustrialFloor extends BlockBase {

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES)
                    .map(facing -> PropertyBool.create(facing.getName()))
                    .collect(Collectors.toList())
    );
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.875D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB UP_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB NONE_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB C_UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    protected static final AxisAlignedBB C_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB C_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockIndustrialFloor(String name) {
        super(Material.IRON, name);
        setHardness(0.8f);
        setSoundType(SoundType.METAL);
        //setLightOpacity(255);

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = getActualState(state, source, pos);
        if (isConnected(actualState, EnumFacing.UP) && !isConnected(actualState, EnumFacing.DOWN)) {
            return UP_AABB;
        }
        if (!isConnected(actualState, EnumFacing.UP) && isConnected(actualState, EnumFacing.DOWN)) {
            return DOWN_AABB;
        }
        if (!isConnected(actualState, EnumFacing.UP) && !isConnected(actualState, EnumFacing.DOWN)) {
            return NONE_AABB;
        }
        return UP_DOWN_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = getActualState(state, worldIn, pos);
        if (isConnected(actualState, EnumFacing.UP)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_UP_AABB);
        }
        if (isConnected(actualState, EnumFacing.DOWN)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_DOWN_AABB);
        }
        if (isConnected(actualState, EnumFacing.NORTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_NORTH_AABB);
        }
        if (isConnected(actualState, EnumFacing.SOUTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_SOUTH_AABB);
        }
        if (isConnected(actualState, EnumFacing.WEST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_WEST_AABB);
        }
        if (isConnected(actualState, EnumFacing.EAST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_EAST_AABB);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
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

    /**
     * Is the neighbouring block a valid connection for this pipe?
     *
     * @param ownState           This pipe's state
     * @param neighbourState     The neighbouring block's state
     * @param world              The world
     * @param ownPos             This pipe's position
     * @param neighbourDirection The direction of the neighbouring block
     * @return Is the neighbouring block a valid connection?
     */
    protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable
                || (neighbourDirection != EnumFacing.DOWN && neighbourDirection != EnumFacing.UP && nb instanceof BlockDoor)
                || (neighbourDirection == EnumFacing.DOWN && nb instanceof BlockILadder)
                //start check for horizontal Iladder
                || ((neighbourDirection == EnumFacing.NORTH || neighbourDirection == EnumFacing.SOUTH || neighbourDirection == EnumFacing.EAST || neighbourDirection == EnumFacing.WEST)
                && nb instanceof BlockILadder && !neighbourState.getValue(BlockILadder.ACTIVE))
                //end
                ;
    }

    /**
     * Can this pipe connect to the neighbouring block?
     *
     * @param ownState           This pipe's state
     * @param worldIn            The world
     * @param ownPos             This pipe's position
     * @param neighbourDirection The direction of the neighbouring block
     * @return Can this pipe connect?
     */
    private boolean canConnectTo(final IBlockState ownState, final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
        final Block neighbourBlock = neighbourState.getBlock();

        final boolean neighbourIsValidForThis = !isValidConnection(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
        final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BlockIndustrialFloor) || ((BlockIndustrialFloor) neighbourBlock).isValidConnection(neighbourState, ownState, worldIn, neighbourPos, neighbourDirection.getOpposite());

        return neighbourIsValidForThis && thisIsValidForNeighbour;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.VALUES) {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(state, world, pos, facing));
        }

        return state;
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
        return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        if (entity.inventory.getCurrentItem().getItem() == net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.fluidPipe)) {
            world.playSound(null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.setBlockState(new BlockPos(i, j, k), ModBlocks.floorPipe.getDefaultState(), 3);
            if (!entity.isCreative()) {
                entity.inventory.clearMatchingItems(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.fluidPipe), 0, 1, null);
            }
            return true;
        }
        if (entity.inventory.getCurrentItem().getItem() == net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.energyCable)) {
            world.playSound(null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.setBlockState(new BlockPos(i, j, k), ModBlocks.floorCable.getDefaultState(), 3);
            if (!entity.isCreative()) {
                entity.inventory.clearMatchingItems(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.energyCable), 0, 1, null);
            }
            return true;
        }
        if (entity.inventory.getCurrentItem().getItem() == net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.fluorescent)) {
            world.playSound(null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.setBlockState(pos, ModBlocks.floorLamp.getDefaultState(), 3);
            if (entity.getHorizontalFacing() == EnumFacing.EAST || entity.getHorizontalFacing() == EnumFacing.WEST) {
                world.setBlockState(pos.up(), ModBlocks.dummy.getDefaultState(), 3);
            }
            if (!entity.isCreative()) {
                entity.inventory.clearMatchingItems(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.fluorescent), 0, 1, null);
            }
            return true;
        }
        return false;
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

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }
}