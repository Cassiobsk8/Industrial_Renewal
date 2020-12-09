package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockPillar;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPillarFluidPipe extends BlockFluidPipe
{
    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static final float DOWNY1 = 0.0f;
    private static final float UPY2 = 1.0f;

    public BlockPillarFluidPipe(String name, CreativeTabs tab)
    {
        super(name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[]{}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN, WSOUTH, WNORTH, WEAST, WWEST, WUP, WDOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, BlockState state)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        ItemStack itemst = new ItemStack(ItemBlock.getItemFromBlock(BlocksRegistration.fluidPipe));
        EntityItem entity = new EntityItem(worldIn, x, y, z, itemst);
        if (!worldIn.isRemote)
        {
            worldIn.spawnEntity(entity);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state)
    {
        return new ItemStack(ItemBlock.getItemFromBlock(BlocksRegistration.pillar));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateFromMeta(final int meta)
    {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final BlockState state)
    {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final BlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final BlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(BlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, EnumHand hand, Direction side, float hitX, float hitY, float hitZ)
    {
        ItemStack playerStack = player.getHeldItem(EnumHand.MAIN_HAND);
        Item playerItem = playerStack.getItem();
        if (playerItem instanceof ItemPowerScrewDrive)
        {
            if (!world.isRemote)
            {
                world.setBlockState(pos, BlocksRegistration.pillar.getDefaultState(), 3);
                if (!player.isCreative())
                    player.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(BlocksRegistration.fluidPipe)));
                ItemPowerScrewDrive.playDrillSound(world, pos);
            }
            return true;
        }
        if (playerItem.equals(ItemBlock.getItemFromBlock(BlocksRegistration.pillar)))
        {
            int n = 1;
            while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarEnergyCable
                    || world.getBlockState(pos.up(n)).getBlock() instanceof BlockPillarFluidPipe
                    || world.getBlockState(pos.up(n)).getBlock() instanceof BlockPillar)
            {
                n++;
            }
            if (world.getBlockState(pos.up(n)).getBlock().isReplaceable(world, pos.up(n)))
            {
                if (!world.isRemote)
                {
                    world.setBlockState(pos.up(n), getBlockFromItem(playerItem).getDefaultState(), 3);
                    world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) state;
            return eState.with(MASTER, isMaster(world, pos))
                    .with(SOUTH, canConnectToPipe(world, pos, Direction.SOUTH)).with(NORTH, canConnectToPipe(world, pos, Direction.NORTH))
                    .with(EAST, canConnectToPipe(world, pos, Direction.EAST)).with(WEST, canConnectToPipe(world, pos, Direction.WEST))
                    .with(UP, canConnectToPipe(world, pos, Direction.UP)).with(DOWN, canConnectToPipe(world, pos, Direction.DOWN))
                    .with(CSOUTH, canConnectToCapability(world, pos, Direction.SOUTH)).with(CNORTH, canConnectToCapability(world, pos, Direction.NORTH))
                    .with(CEAST, canConnectToCapability(world, pos, Direction.EAST)).with(CWEST, canConnectToCapability(world, pos, Direction.WEST))
                    .with(CUP, canConnectToCapability(world, pos, Direction.UP)).with(CDOWN, canConnectToCapability(world, pos, Direction.DOWN))
                    .with(WSOUTH, BlockPillar.canConnectTo(world, pos, Direction.SOUTH)).with(WNORTH, BlockPillar.canConnectTo(world, pos, Direction.NORTH))
                    .with(WEAST, BlockPillar.canConnectTo(world, pos, Direction.EAST)).with(WWEST, BlockPillar.canConnectTo(world, pos, Direction.WEST))
                    .with(WUP, BlockPillar.canConnectTo(world, pos, Direction.UP)).with(WDOWN, BlockPillar.canConnectTo(world, pos, Direction.DOWN));
        }
        return state;
    }

    public final boolean isConnected(IBlockAccess world, BlockPos pos, BlockState state, final Direction facing)
    {
        if (state instanceof IExtendedBlockState)
        {
            state = getExtendedState(state, world, pos);
            IExtendedBlockState eState = (IExtendedBlockState) state;
            switch (facing)
            {
                case DOWN:
                    return eState.get(WDOWN);
                case UP:
                    return eState.get(WUP);
                case NORTH:
                    return eState.get(WNORTH);
                case SOUTH:
                    return eState.get(WSOUTH);
                case WEST:
                    return eState.get(WWEST);
                case EAST:
                    return eState.get(WEAST);
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(BlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        if (isConnected(worldIn, pos, state, Direction.NORTH))
        {
            NORTHZ1 = 0.0f;
        }
        else
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        }
        else
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, Direction.WEST))
        {
            WESTX1 = 0.0f;
        }
        else
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.EAST))
        {
            EASTX2 = 1.0f;
        }
        else
        {
            EASTX2 = 0.750f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (isConnected(worldIn, pos, state, Direction.NORTH))
        {
            NORTHZ1 = 0.0f;
        }
        else
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        }
        else
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, Direction.WEST))
        {
            WESTX1 = 0.0f;
        }
        else
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, Direction.EAST))
        {
            EASTX2 = 1.0f;
        }
        else
        {
            EASTX2 = 0.750f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
    {
        if (face == Direction.EAST || face == Direction.WEST || face == Direction.NORTH || face == Direction.SOUTH)
        {
            return BlockFaceShape.SOLID;
        }
        else
        {
            return BlockFaceShape.UNDEFINED;
        }
    }
}
