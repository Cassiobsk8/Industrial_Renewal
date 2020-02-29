package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ItemsRegistration;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBatteryBank extends BlockAbstractHorizontalFacing
{
    public BlockBatteryBank()
    {
        super(Block.Properties.create(Material.IRON));
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (handIn.equals(Hand.MAIN_HAND) && player.getHeldItem(Hand.MAIN_HAND).getItem().equals(ItemsRegistration.SCREWDRIVE.get()))
        {
            TileEntityBatteryBank te = (TileEntityBatteryBank) worldIn.getTileEntity(pos);
            if (te != null) te.toggleFacing(hit.getFace());
            worldIn.notifyBlockUpdate(pos, state, state, 3);
            if (!worldIn.isRemote) ItemPowerScrewDrive.playDrillSound(worldIn, pos);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
    {
        return new Direction[0];
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityBatteryBank createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBatteryBank();
    }
}
