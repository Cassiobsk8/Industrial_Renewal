package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.Registry.ModBlocks;
import cassiokf.industrialrenewal.Registry.ModItems;
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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BlockFloorLamp extends BlockBase {

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES)
                    .map(facing -> PropertyBool.create(facing.getName()))
                    .collect(Collectors.toList())
    );

    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.625D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB UP_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB NONE_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB C_UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    protected static final AxisAlignedBB C_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB C_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockFloorLamp(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setLightLevel(1.0F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = getActualState(state, source, pos);
        if (!isConnected(actualState, EnumFacing.DOWN)) {
            return UP_AABB;
        }
        if (isConnected(actualState, EnumFacing.DOWN)) {
            return DOWN_AABB;
        }
        return UP_DOWN_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = getActualState(state, worldIn, pos);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, C_UP_AABB);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (entity.inventory.getCurrentItem().getItem() == ModItems.screwDrive) {
            world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:drill")), SoundCategory.BLOCKS, 1.0F, 1.0F);
            rotateBlock(world, pos, state);
            return true;
        }
        return false;
    }

    public void rotateBlock(World world, BlockPos pos, IBlockState state) {
        IBlockState actualState = getActualState(state, world, pos);
        if (world.getBlockState(pos.up()).getBlock() == ModBlocks.dummy) {
            world.setBlockToAir(pos.up());
        } else if (world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos)) {
            world.setBlockState(pos.up(), ModBlocks.dummy.getDefaultState(), 3);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
        return new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor)).getItem();
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        ItemStack itemst = new ItemStack(Item.getItemFromBlock(ModBlocks.fluorescent));
        EntityItem entity = new EntityItem(world, x, y, z, itemst);
        if (!world.isRemote) {
            world.spawnEntity(entity);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor));
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
        if (neighbourDirection != EnumFacing.UP) {
            return nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable
                    || (neighbourDirection != EnumFacing.DOWN && nb instanceof BlockDoor)
                    || (neighbourDirection == EnumFacing.DOWN && nb instanceof BlockCatwalkLadder)
                    //start check for horizontal Iladder
                    || ((neighbourDirection == EnumFacing.NORTH || neighbourDirection == EnumFacing.SOUTH || neighbourDirection == EnumFacing.EAST || neighbourDirection == EnumFacing.WEST)
                    && nb instanceof BlockCatwalkLadder && !neighbourState.getValue(BlockCatwalkLadder.ACTIVE))
                    //end
                    ;
        }
        //TODO Tem que ter uma forma melhor de fazer isso
        return !(nb instanceof BlockDummy);
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

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
        if (face == EnumFacing.DOWN) {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }
}