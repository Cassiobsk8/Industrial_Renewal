package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.blocks.BlockBase;
import cassiokf.industrialrenewal.blocks.BlockCatwalkHatch;
import cassiokf.industrialrenewal.blocks.BlockCatwalkLadder;
import cassiokf.industrialrenewal.init.ModBlocks;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BlockIndustrialFloor extends BlockBase
{

    public static final ImmutableList<IUnlistedProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES)
                    .map(facing -> new Properties.PropertyAdapter<>(PropertyBool.create(facing.getName())))
                    .collect(Collectors.toList())
    );
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.875D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB NONE_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB C_UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    protected static final AxisAlignedBB C_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB C_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C_EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockIndustrialFloor(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
        setSoundType(SoundType.METAL);
        //setLightOpacity(255);

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (isConnected(worldIn, pos, state, EnumFacing.UP) && !isConnected(worldIn, pos, state, EnumFacing.DOWN))
        {
            return UP_AABB;
        }
        if (!isConnected(worldIn, pos, state, EnumFacing.UP) && isConnected(worldIn, pos, state, EnumFacing.DOWN))
        {
            return DOWN_AABB;
        }
        if (!isConnected(worldIn, pos, state, EnumFacing.UP) && !isConnected(worldIn, pos, state, EnumFacing.DOWN))
        {
            return NONE_AABB;
        }
        return FULL_BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        if (isConnected(worldIn, pos, state, EnumFacing.UP))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_UP_AABB);
        }
        if (isConnected(worldIn, pos, state, EnumFacing.DOWN))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_DOWN_AABB);
        }
        if (isConnected(worldIn, pos, state, EnumFacing.NORTH))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_NORTH_AABB);
        }
        if (isConnected(worldIn, pos, state, EnumFacing.SOUTH))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_SOUTH_AABB);
        }
        if (isConnected(worldIn, pos, state, EnumFacing.WEST))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_WEST_AABB);
        }
        if (isConnected(worldIn, pos, state, EnumFacing.EAST))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_EAST_AABB);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[]{}; // listed properties
        IUnlistedProperty[] unlistedProperties = CONNECTED_PROPERTIES.toArray(new IUnlistedProperty[CONNECTED_PROPERTIES.size()]);
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
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

    private static boolean isValidConnection(final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        return nb instanceof BlockIndustrialFloor
                || nb instanceof BlockFloorPipe
                || nb instanceof BlockFloorCable
                || (nb instanceof BlockDoor && neighbourState.getValue(BlockDoor.FACING).equals(neighbourDirection))
                || (neighbourDirection.equals(EnumFacing.DOWN) && nb instanceof BlockCatwalkLadder)
                || (neighbourDirection.equals(EnumFacing.UP) && nb instanceof BlockCatwalkHatch)
                //start check for horizontal Iladder
                || ((neighbourDirection != EnumFacing.UP && neighbourDirection != EnumFacing.DOWN)
                && nb instanceof BlockCatwalkLadder && !neighbourState.getValue(BlockCatwalkLadder.ACTIVE))
                //end
                ;
    }

    public static boolean canConnectTo(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);

        return !isValidConnection(neighbourState, worldIn, ownPos, neighbourDirection);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) state;
            for (final EnumFacing facing : EnumFacing.VALUES)
            {
                eState = eState.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(world, pos, facing));
            }
            return eState;
        }
        return state;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    public final boolean isConnected(IBlockAccess world, BlockPos pos, IBlockState state, final EnumFacing facing)
    {
        if (state instanceof IExtendedBlockState)
        {
            state = getExtendedState(state, world, pos);
            IExtendedBlockState eState = (IExtendedBlockState) state;
            return eState.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
        }
        return false;
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {


        ItemStack playerStack = entity.getHeldItem(EnumHand.MAIN_HAND);
        Item playerItem = playerStack.getItem();

        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.fluidPipe)))
        {
            world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
            world.setBlockState(pos, ModBlocks.floorPipe.getDefaultState(), 3);
            if (!entity.isCreative())
            {
                playerStack.shrink(1);
            }
            return true;
        }
        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableMV))
                || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableLV))
                || playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableHV)))
        {
            world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
            Block block;
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableMV))) block = ModBlocks.floorCableMV;
            else if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableLV)))
                block = ModBlocks.floorCableLV;
            else block = ModBlocks.floorCableHV;
            world.setBlockState(pos, block.getDefaultState(), 3);
            if (!entity.isCreative())
            {
                playerStack.shrink(1);
            }
            return true;
        }
        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.fluorescent)))
        {
            world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.setBlockState(pos, ModBlocks.floorLamp.getDefaultState(), 3);
            if (entity.getHorizontalFacing() == EnumFacing.EAST || entity.getHorizontalFacing() == EnumFacing.WEST)
            {
                world.setBlockState(pos.up(), ModBlocks.dummy.getDefaultState(), 3);
            }
            if (!entity.isCreative())
            {
                playerStack.shrink(1);
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
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Deprecated
    public boolean isTopSolid(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side.equals(EnumFacing.UP);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        if (face == EnumFacing.UP || face == EnumFacing.DOWN)
        {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }
}