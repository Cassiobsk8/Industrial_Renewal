package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import net.minecraft.block.SoundType;
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

import javax.annotation.Nullable;
import java.util.List;

public class BlockPillarEnergyCable extends BlockEnergyCable
{

    public static final PropertyBool WSOUTH = PropertyBool.create("w_south");
    public static final PropertyBool WNORTH = PropertyBool.create("w_north");
    public static final PropertyBool WEAST = PropertyBool.create("w_east");
    public static final PropertyBool WWEST = PropertyBool.create("w_west");
    public static final PropertyBool WUP = PropertyBool.create("w_up");
    public static final PropertyBool WDOWN = PropertyBool.create("w_down");

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

    public BlockPillarEnergyCable(String name, CreativeTabs tab)
    {
        super(name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, SOUTH, NORTH, EAST, WEST, UP, DOWN, WSOUTH, WNORTH, WEAST, WWEST, WUP, WDOWN);
    }

    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        ItemStack itemst = new ItemStack(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.energyCable));
        EntityItem entity = new EntityItem(worldIn, x, y, z, itemst);
        if (!worldIn.isRemote)
        {
            worldIn.spawnEntity(entity);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.pillar));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta)
    {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state)
    {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack playerStack = player.getHeldItem(EnumHand.MAIN_HAND);
        Item playerItem = playerStack.getItem();
        if (playerItem instanceof ItemPowerScrewDrive)
        {
            if (!world.isRemote)
            {
                world.setBlockState(pos, ModBlocks.pillar.getDefaultState(), 3);
                if (!player.isCreative())
                    player.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(ModBlocks.energyCable)));
                ItemPowerScrewDrive.playDrillSound(world, pos);
            }
            return true;
        }
        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.pillar)))
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
                    world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) playerStack.shrink(1);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        state = state.withProperty(SOUTH, canConnectPipe(world, pos, EnumFacing.SOUTH)).withProperty(NORTH, canConnectPipe(world, pos, EnumFacing.NORTH))
                .withProperty(EAST, canConnectPipe(world, pos, EnumFacing.EAST)).withProperty(WEST, canConnectPipe(world, pos, EnumFacing.WEST))
                .withProperty(UP, canConnectPipe(world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectPipe(world, pos, EnumFacing.DOWN))
                .withProperty(WSOUTH, BlockPillar.canConnectTo(world, pos, EnumFacing.SOUTH)).withProperty(WNORTH, BlockPillar.canConnectTo(world, pos, EnumFacing.NORTH))
                .withProperty(WEAST, BlockPillar.canConnectTo(world, pos, EnumFacing.EAST)).withProperty(WWEST, BlockPillar.canConnectTo(world, pos, EnumFacing.WEST))
                .withProperty(WUP, BlockPillar.canConnectTo(world, pos, EnumFacing.UP)).withProperty(WDOWN, BlockPillar.canConnectTo(world, pos, EnumFacing.DOWN));
        return state;
    }

    private boolean canConnectPipe(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        return canConnectToPipe(world, pos, facing) || canConnectToCapability(world, pos, facing);
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing)
    {
        switch (facing)
        {
            case DOWN:
                return state.getValue(WDOWN);
            case UP:
                return state.getValue(WUP);
            case NORTH:
                return state.getValue(WNORTH);
            case SOUTH:
                return state.getValue(WSOUTH);
            case WEST:
                return state.getValue(WWEST);
            case EAST:
                return state.getValue(WEAST);
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        if (!isActualState)
        {
            state = state.getActualState(worldIn, pos);
        }
        if (isConnected(state, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(state, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(state, EnumFacing.WEST))
        {
            WESTX1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.WEST))
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(state, EnumFacing.EAST))
        {
            EASTX2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.EAST))
        {
            EASTX2 = 0.750f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        IBlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(actualState, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(actualState, EnumFacing.WEST))
        {
            WESTX1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.WEST))
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(actualState, EnumFacing.EAST))
        {
            EASTX2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.EAST))
        {
            EASTX2 = 0.750f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH)
        {
            return BlockFaceShape.SOLID;
        } else
        {
            return BlockFaceShape.UNDEFINED;
        }
    }
}
