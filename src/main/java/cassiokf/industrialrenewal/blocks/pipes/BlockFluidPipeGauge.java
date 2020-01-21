package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipe;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeGauge;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidPipeGauge extends BlockFluidPipe
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockFluidPipeGauge(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[]{FACING}; // listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (entity.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPowerScrewDrive)
        {
            if (!world.isRemote)
            {
                world.setBlockState(pos, ModBlocks.fluidPipe.getDefaultState(), 3);
                if (!entity.isCreative())
                    entity.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(ModBlocks.gauge)));
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
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.fluidPipe));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("600" + " mB/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public Class<TileEntityFluidPipe> getTileEntityClass()
    {
        return TileEntityFluidPipe.class;
    }

    @Nullable
    @Override
    public TileEntityFluidPipeGauge createTileEntity(World world, IBlockState state)
    {
        return new TileEntityFluidPipeGauge();
    }
}
