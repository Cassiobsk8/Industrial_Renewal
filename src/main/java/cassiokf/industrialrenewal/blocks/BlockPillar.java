package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorCable;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorLamp;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockFloorPipe;
import cassiokf.industrialrenewal.blocks.industrialfloor.BlockIndustrialFloor;
import cassiokf.industrialrenewal.blocks.pipes.BlockCableTray;
import cassiokf.industrialrenewal.blocks.pipes.BlockPillarEnergyCable;
import cassiokf.industrialrenewal.blocks.pipes.BlockPillarFluidPipe;
import cassiokf.industrialrenewal.blocks.pipes.BlockWireBase;
import cassiokf.industrialrenewal.blocks.redstone.BlockAlarm;
import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import cassiokf.industrialrenewal.init.ModBlocks;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockPillar extends BlockBase {

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES).map(facing -> PropertyBool.create(facing.getName())).collect(Collectors.toList()));
    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

    public BlockPillar(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
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

    private static boolean isValidConnection(IBlockAccess worldIn, BlockPos neightbourPos, final IBlockState neighbourState, final EnumFacing neighbourDirection)
    {
        Block nb = neighbourState.getBlock();
        if (neighbourDirection != EnumFacing.UP && neighbourDirection != EnumFacing.DOWN) {
            return nb instanceof BlockLever
                    || (nb instanceof BlockWireBase && neighbourState.getValue(BlockWireBase.FACING) == neighbourDirection.getOpposite())
                    || nb instanceof BlockRedstoneTorch
                    || nb instanceof BlockTripWireHook
                    || nb instanceof BlockColumn
                    || (nb instanceof BlockCableTray && neighbourState.getValue(BlockCableTray.BASE).equals(EnumBaseDirection.byIndex(neighbourDirection.getOpposite().getIndex())))
                    || nb instanceof BlockLadder
                    || (nb instanceof BlockLight && neighbourState.getValue(BlockLight.FACING) == neighbourDirection.getOpposite())
                    || nb instanceof BlockRoof
                    || (nb instanceof BlockBrace && Objects.equals(neighbourState.getValue(BlockBrace.FACING).getName(), neighbourDirection.getOpposite().getName()))
                    || (nb instanceof BlockBrace && Objects.equals(neighbourState.getValue(BlockBrace.FACING).getName(), "down_" + neighbourDirection.getName()))
                    || (nb instanceof BlockAlarm && neighbourState.getValue(BlockAlarm.FACING) == neighbourDirection)
                    || (nb instanceof BlockSignBase && neighbourState.getValue(BlockSignBase.ONWALL) && Objects.equals(neighbourState.getValue(BlockSignBase.FACING).getName(), neighbourDirection.getOpposite().getName()))
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:connector")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_decoration2")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:wooden_device1")
                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_device1")
                    //start Industrial floor side connection
                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
                    || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable;
            //end
        }
        if (neighbourDirection == EnumFacing.DOWN) {
            return nb.isFullCube(neighbourState)
                    || nb.isTopSolid(neighbourState);
        }
        return nb.isFullCube(neighbourState) || neighbourState.isSideSolid(worldIn, neightbourPos, EnumFacing.DOWN) || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
                || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable || nb instanceof BlockCatWalk;
    }

    public static boolean canConnectTo(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);

        return isValidConnection(worldIn, neighbourPos, neighbourState, neighbourDirection);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (this.name.equals("catwalk_pillar") && hand.equals(EnumHand.MAIN_HAND))
        {
            ItemStack playerStack = player.getHeldItem(EnumHand.MAIN_HAND);
            Item playerItem = playerStack.getItem();
            Block clickedBlock = state.getBlock();
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableMV)))
            {
                if (!world.isRemote)
                {
                    world.setBlockState(pos, ModBlocks.pillarEnergyCableMV.getDefaultState(), 3);
                    world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return true;
            }
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableLV)))
            {
                if (!world.isRemote)
                {
                    world.setBlockState(pos, ModBlocks.pillarEnergyCableLV.getDefaultState(), 3);
                    world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return true;
            }
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.energyCableHV)))
            {
                if (!world.isRemote)
                {
                    world.setBlockState(pos, ModBlocks.pillarEnergyCableHV.getDefaultState(), 3);
                    world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return true;
            }
            if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.fluidPipe)))
            {
                if (!world.isRemote)
                {
                    world.setBlockState(pos, ModBlocks.pillarFluidPipe.getDefaultState(), 3);
                    world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return true;
            }
            if ((playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.pillar)) && clickedBlock.equals(ModBlocks.pillar))
                    || (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.steel_pillar))) && clickedBlock.equals(ModBlocks.steel_pillar))
            {
                int n = 1;
                while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockPillar
                        || world.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarEnergyCable
                        || world.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarFluidPipe)
                {
                    n++;
                }
                if (world.getBlockState(pos.up(n)).getBlock().isReplaceable(world, pos.up(n)))
                {
                    if (!world.isRemote)
                    {
                        world.setBlockState(pos.up(n), getBlockFromItem(playerItem).getDefaultState(), 3);
                        world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                        if (!player.isCreative()) playerStack.shrink(1);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
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
        if (isConnected(state, EnumFacing.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(state, EnumFacing.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(state, EnumFacing.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(state, EnumFacing.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.EAST)) {
            EASTX2 = 0.750f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, EnumFacing.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.NORTH)) {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(actualState, EnumFacing.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.SOUTH)) {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(actualState, EnumFacing.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.WEST)) {
            WESTX1 = 0.250f;
        }
        if (isConnected(actualState, EnumFacing.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.EAST)) {
            EASTX2 = 0.750f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
            return BlockFaceShape.SOLID;
        } else {
            return BlockFaceShape.UNDEFINED;
        }
    }
}
