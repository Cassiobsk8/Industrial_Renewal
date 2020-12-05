package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableGauge;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableHVGauge;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableLVGauge;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableMVGauge;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;

public class BlockEnergyCableGauge extends BlockEnergyCable
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockEnergyCableGauge(EnumEnergyCableType type, String name, CreativeTabs tab)
    {
        super(type, name, tab);
    }


    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public  BlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            EnumFacing facing = state.getValue(FACING);
            IExtendedBlockState eState = (IExtendedBlockState) state;
            return eState.withProperty(MASTER, isMaster(world, pos))
                    .withProperty(SOUTH, canConnectToPipe(world, pos, facing.getOpposite())).withProperty(NORTH, canConnectToPipe(world, pos, facing))
                    .withProperty(EAST, canConnectToPipe(world, pos, facing.rotateY())).withProperty(WEST, canConnectToPipe(world, pos, facing.rotateYCCW()))
                    .withProperty(UP, canConnectToPipe(world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectToPipe(world, pos, EnumFacing.DOWN))
                    .withProperty(CSOUTH, canConnectToCapability(world, pos, facing.getOpposite())).withProperty(CNORTH, canConnectToCapability(world, pos, facing))
                    .withProperty(CEAST, canConnectToCapability(world, pos, facing.rotateY())).withProperty(CWEST, canConnectToCapability(world, pos, facing.rotateYCCW()))
                    .withProperty(CUP, canConnectToCapability(world, pos, EnumFacing.UP)).withProperty(CDOWN, canConnectToCapability(world, pos, EnumFacing.DOWN));
        }
        return state;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,  BlockState state, PlayerEntity entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (entity.getHeldItemMainhand().getItem() instanceof ItemPowerScrewDrive)
        {
            if (!world.isRemote)
            {
                Block block;
                switch (type)
                {
                    default:
                    case LV:
                        block = ModBlocks.energyCableLV;
                        break;
                    case MV:
                        block = ModBlocks.energyCableMV;
                        break;
                    case HV:
                        block = ModBlocks.energyCableHV;
                        break;
                }
                world.setBlockState(pos, block.getDefaultState(), 3);
                if (!entity.isCreative())
                    entity.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(ModBlocks.energyLevel)));
                ItemPowerScrewDrive.playDrillSound(world, pos);
            }
        }
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos,  BlockState state)
    {
        Block block;
        switch (type)
        {
            default:
            case LV:
                block = ModBlocks.energyCableLV;
                break;
            case MV:
                block = ModBlocks.energyCableMV;
                break;
            case HV:
                block = ModBlocks.energyCableHV;
                break;
        }
        return new ItemStack(Item.getItemFromBlock(block));
    }

    @Override
    public  BlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos,  BlockState state)
    {
        if (!world.isRemote)
        {
            ItemStack itemst = new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.energyLevel));
            Utils.spawnItemStack(world, pos, itemst);
            Utils.spawnItemStack(world, pos, getItem(world, pos, state));
        }
        super.onPlayerDestroy(world, pos, state);
    }

    @Nullable
    @Override
    public TileEntityEnergyCableGauge createTileEntity(World world,  BlockState state)
    {
        switch (type)
        {
            default:
            case LV:
                return new TileEntityEnergyCableLVGauge();
            case MV:
                return new TileEntityEnergyCableMVGauge();
            case HV:
                return new TileEntityEnergyCableHVGauge();
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        switch (type)
        {
            default:
            case LV:
                return new TileEntityEnergyCableLVGauge();
            case MV:
                return new TileEntityEnergyCableMVGauge();
            case HV:
                return new TileEntityEnergyCableHVGauge();
        }
    }
}
