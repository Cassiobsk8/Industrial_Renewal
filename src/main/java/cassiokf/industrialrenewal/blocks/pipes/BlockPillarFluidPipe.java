package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockPillar;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
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
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.0f;

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
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        ItemStack itemst = new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.fluidPipe));
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
                    player.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(ModBlocks.fluidPipe)));
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

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState eState = (IExtendedBlockState) state;
            return eState.withProperty(MASTER, isMaster(world, pos))
                    .withProperty(SOUTH, canConnectToPipe(world, pos, EnumFacing.SOUTH)).withProperty(NORTH, canConnectToPipe(world, pos, EnumFacing.NORTH))
                    .withProperty(EAST, canConnectToPipe(world, pos, EnumFacing.EAST)).withProperty(WEST, canConnectToPipe(world, pos, EnumFacing.WEST))
                    .withProperty(UP, canConnectToPipe(world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectToPipe(world, pos, EnumFacing.DOWN))
                    .withProperty(CSOUTH, canConnectToCapability(world, pos, EnumFacing.SOUTH)).withProperty(CNORTH, canConnectToCapability(world, pos, EnumFacing.NORTH))
                    .withProperty(CEAST, canConnectToCapability(world, pos, EnumFacing.EAST)).withProperty(CWEST, canConnectToCapability(world, pos, EnumFacing.WEST))
                    .withProperty(CUP, canConnectToCapability(world, pos, EnumFacing.UP)).withProperty(CDOWN, canConnectToCapability(world, pos, EnumFacing.DOWN))
                    .withProperty(WSOUTH, BlockPillar.canConnectTo(world, pos, EnumFacing.SOUTH)).withProperty(WNORTH, BlockPillar.canConnectTo(world, pos, EnumFacing.NORTH))
                    .withProperty(WEAST, BlockPillar.canConnectTo(world, pos, EnumFacing.EAST)).withProperty(WWEST, BlockPillar.canConnectTo(world, pos, EnumFacing.WEST))
                    .withProperty(WUP, BlockPillar.canConnectTo(world, pos, EnumFacing.UP)).withProperty(WDOWN, BlockPillar.canConnectTo(world, pos, EnumFacing.DOWN));
        }
        return state;
    }

    public final boolean isConnected(IBlockAccess world, BlockPos pos, IBlockState state, final EnumFacing facing)
    {
        if (state instanceof IExtendedBlockState)
        {
            state = getExtendedState(state, world, pos);
            IExtendedBlockState eState = (IExtendedBlockState) state;
            switch (facing)
            {
                case DOWN:
                    return eState.getValue(WDOWN);
                case UP:
                    return eState.getValue(WUP);
                case NORTH:
                    return eState.getValue(WNORTH);
                case SOUTH:
                    return eState.getValue(WSOUTH);
                case WEST:
                    return eState.getValue(WWEST);
                case EAST:
                    return eState.getValue(WEAST);
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        if (isConnected(worldIn, pos, state, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.0f;
        } else
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, EnumFacing.WEST))
        {
            WESTX1 = 0.0f;
        } else
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, EnumFacing.EAST))
        {
            EASTX2 = 1.0f;
        } else
        {
            EASTX2 = 0.750f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (isConnected(worldIn, pos, state, EnumFacing.NORTH))
        {
            NORTHZ1 = 0.0f;
        } else
        {
            NORTHZ1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, EnumFacing.SOUTH))
        {
            SOUTHZ2 = 1.0f;
        } else
        {
            SOUTHZ2 = 0.750f;
        }
        if (isConnected(worldIn, pos, state, EnumFacing.WEST))
        {
            WESTX1 = 0.0f;
        } else
        {
            WESTX1 = 0.250f;
        }
        if (isConnected(worldIn, pos, state, EnumFacing.EAST))
        {
            EASTX2 = 1.0f;
        } else
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
