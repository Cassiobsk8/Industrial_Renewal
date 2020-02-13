package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBatteryBank extends BlockTileEntityConnected<TileEntityBatteryBank>
{
    public BlockBatteryBank(Block.Properties property)
    {
        super(property);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.capacity")
                + ": "
                + (IRConfig.Main.batteryBankCapacity.get())
                + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (handIn.equals(Hand.MAIN_HAND) && player.getHeldItem(Hand.MAIN_HAND).getItem().equals(ModItems.screwDrive) && worldIn.getTileEntity(pos) instanceof TileEntityBatteryBank)
        {
            TileEntityBatteryBank te = (TileEntityBatteryBank) worldIn.getTileEntity(pos);
            if (te != null) te.toggleFacing(p_225533_6_.getFace());
            worldIn.notifyBlockUpdate(pos, state, state, 3);
            if (!worldIn.isRemote) ItemPowerScrewDrive.playDrillSound(worldIn, pos);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntityBatteryBank te = (TileEntityBatteryBank) worldIn.getTileEntity(pos);
        if (te != null) te.forceFaceCheck();
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state;
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
    {
        //if (state instanceof IExtendedBlockState)
        //{
        //    Direction facing = state.get(FACING);
        //    IExtendedBlockState eState = (IExtendedBlockState) state;
        //    TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(pos);
        //    if (te != null) return eState.with(SOUTH, te.isFacingOutput(facing.getOpposite()))
        //            .with(NORTH, te.isFacingOutput(facing))
        //            .with(EAST, te.isFacingOutput(facing.rotateY()))
        //            .with(WEST, te.isFacingOutput(facing.rotateYCCW()))
        //            .with(UP, te.isFacingOutput(Direction.UP))
        //            .with(DOWN, te.isFacingOutput(Direction.DOWN));
        //}
        return state;
    }

    @Nullable
    @Override
    public TileEntityBatteryBank createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBatteryBank();
    }
}
