package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipe;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityFluidPipeGauge;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidPipeGauge extends BlockFluidPipe
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static float DOWNY1 = 0.250f;
    private static float UPY2 = 1f;

    public BlockFluidPipeGauge(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, SOUTH, NORTH, EAST, WEST, UP, DOWN);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        EnumFacing facing = state.getValue(FACING);
        state = state.withProperty(SOUTH, canConnectPipe(world, pos, facing.getOpposite()))
                .withProperty(NORTH, canConnectPipe(world, pos, facing))
                .withProperty(EAST, canConnectPipe(world, pos, facing.rotateY()))
                .withProperty(WEST, canConnectPipe(world, pos, facing.rotateYCCW()))
                .withProperty(UP, canConnectPipe(world, pos, EnumFacing.UP))
                .withProperty(DOWN, canConnectPipe(world, pos, EnumFacing.DOWN));
        return state;
    }

    private boolean canConnectPipe(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        return canConnectToPipe(world, pos, facing) || canConnectToCapability(world, pos, facing);
    }

    @Override
    public boolean canConnectToPipe(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        IBlockState state = worldIn.getBlockState(ownPos.offset(neighbourDirection));
        return state.getBlock() instanceof BlockFluidPipe;
    }

    @Override
    public boolean canConnectToCapability(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        return !(state.getBlock() instanceof BlockFluidPipe) && te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite());
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

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {

        if (!isActualState) state = state.getActualState(worldIn, pos);
        if (isConnected(worldIn, pos, state, NORTH)) NORTHZ1 = 0.0f;
        else NORTHZ1 = 0.250f;
        if (isConnected(worldIn, pos, state, SOUTH)) SOUTHZ2 = 1.0f;
        else SOUTHZ2 = 0.750f;
        if (isConnected(worldIn, pos, state, WEST)) WESTX1 = 0.0f;
        else WESTX1 = 0.250f;
        if (isConnected(worldIn, pos, state, EAST)) EASTX2 = 1.0f;
        else EASTX2 = 0.750f;
        if (isConnected(worldIn, pos, state, DOWN)) DOWNY1 = 0.0f;
        else DOWNY1 = 0.250f;
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState actualState = state.getActualState(worldIn, pos);
        if (isConnected(worldIn, pos, actualState, NORTH)) NORTHZ1 = 0.0f;
        else NORTHZ1 = 0.250f;
        if (isConnected(worldIn, pos, actualState, SOUTH)) SOUTHZ2 = 1.0f;
        else SOUTHZ2 = 0.750f;
        if (isConnected(worldIn, pos, actualState, WEST)) WESTX1 = 0.0f;
        else WESTX1 = 0.250f;
        if (isConnected(worldIn, pos, actualState, EAST)) EASTX2 = 1.0f;
        else EASTX2 = 0.750f;
        if (isConnected(worldIn, pos, actualState, DOWN)) DOWNY1 = 0.0f;
        else DOWNY1 = 0.250f;
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
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
