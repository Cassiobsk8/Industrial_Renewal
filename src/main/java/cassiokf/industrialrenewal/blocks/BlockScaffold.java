package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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

public class BlockScaffold extends BlockBase {
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES)
                    .map(facing -> PropertyBool.create(facing.getName()))
                    .collect(Collectors.toList())
    );

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB CBASE_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 1.0D, 0.9D);

    public BlockScaffold(String name) {
        super(Material.IRON, name);
        setSoundType(SoundType.METAL);
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.inventory.getCurrentItem().getItem() == ItemBlock.getItemFromBlock(ModBlocks.scaffold)) {
            Integer n = 1;
            while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockScaffold) {
                n++;
            }
            if (world.getBlockState(pos.up(n)).getBlock().isAir(world.getBlockState(pos.up(n)), world, pos.up(n))) {
                world.setBlockState(pos.up(n), ModBlocks.scaffold.getDefaultState(), 3);
                if (!player.isCreative()) {
                    player.inventory.clearMatchingItems(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.scaffold), 0, 1, null);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (!(world.isSideSolid(pos.offset(EnumFacing.DOWN), EnumFacing.UP, true))) {
            this.dropBlockAsItem((World) world, pos, world.getBlockState(pos), 0);
            ((World) world).setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!(world.isSideSolid(pos.offset(EnumFacing.DOWN), EnumFacing.UP, true))) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BASE_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        addCollisionBoxToList(pos, entityBox, collidingBoxes, CBASE_AABB);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
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
        Block nbd = world.getBlockState(ownPos.offset(neighbourDirection).down()).getBlock();
        if (neighbourDirection == EnumFacing.DOWN) {
            return nb.isFullCube(neighbourState);
        }
        if (neighbourDirection != EnumFacing.UP) {
            return !isConnected(ownState, EnumFacing.UP) && !(nb instanceof BlockScaffold) && !(nbd instanceof BlockScaffold);
        }
        return nb.isFullCube(neighbourState) || nb instanceof BlockScaffold;
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

        final boolean neighbourIsValidForThis = isValidConnection(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
        final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BlockFloorLamp) || ((BlockFloorLamp) neighbourBlock).isValidConnection(neighbourState, ownState, worldIn, neighbourPos, neighbourDirection.getOpposite());

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

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP);
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

    @Deprecated

    public boolean isTopSolid(IBlockState state) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.UP) {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        if (entityIn instanceof EntityLivingBase && !((EntityLivingBase) entityIn).isOnLadder() && isLadder(state, worldIn, pos, (EntityLivingBase) entityIn)) {
            float f5 = 0.15F;
            if (entityIn.motionX < -f5)
                entityIn.motionX = -f5;
            if (entityIn.motionX > f5)
                entityIn.motionX = f5;
            if (entityIn.motionZ < -f5)
                entityIn.motionZ = -f5;
            if (entityIn.motionZ > f5)
                entityIn.motionZ = f5;

            entityIn.fallDistance = 0.0F;
            if (entityIn.motionY < -0.15D)
                entityIn.motionY = -0.15D;

            if (entityIn.motionY < 0 && entityIn instanceof EntityPlayer && entityIn.isSneaking()) {
                entityIn.motionY = 0;
                return;
            }
            if (entityIn.collidedHorizontally)
                entityIn.motionY = .2;
        }
    }
}
