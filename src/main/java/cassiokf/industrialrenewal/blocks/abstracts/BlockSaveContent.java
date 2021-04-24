package cassiokf.industrialrenewal.blocks.abstracts;

import cassiokf.industrialrenewal.tileentity.TileEntityBarrel;
import cassiokf.industrialrenewal.tileentity.TileEntityPortableGenerator;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySaveContent;
import cassiokf.industrialrenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockSaveContent extends BlockHorizontalFacing
{
    public BlockSaveContent(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            boolean acceptFluid = FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, hit.getFace());
            if (!acceptFluid && te instanceof TileEntityBarrel)
                player.sendMessage(new StringTextComponent(((TileEntityBarrel) te).GetChatQuantity()));
            else if (!acceptFluid) doAdditionalFunction(state, worldIn, pos, player, handIn, hit);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("FluidName") && nbt.contains("Amount"))
        {
            tooltip.add(new StringTextComponent(nbt.getString("FluidName") + ": " + nbt.getInt("Amount")));
        }
    }

    public void doAdditionalFunction(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityPortableGenerator && stack.getTag() != null && stack.getTag().contains("FluidName"))
        {
            ((TileEntityPortableGenerator) te).getTank().readFromNBT(stack.getTag());
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntitySaveContent)
        {
            ItemStack itemst = SaveStackContainer((TileEntitySaveContent) te);
            spawnAsEntity(worldIn, pos, itemst);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        return te instanceof TileEntitySaveContent
                ? (int) (Utils.normalizeClamped(((TileEntitySaveContent) te).getTank().getFluidAmount(), 0, ((TileEntitySaveContent) te).getTank().getCapacity()) * 15)
                : 0;
    }

    private ItemStack SaveStackContainer(TileEntitySaveContent te)
    {
        ItemStack stack = new ItemStack(getItemToDrop());
        if (te != null)
        {
            CompoundNBT nbt = stack.getTag();
            if (nbt == null) nbt = new CompoundNBT();
            if (te.getTank().getFluid() != null)
            {
                te.getTank().writeToNBT(nbt);
                stack.setTag(nbt);
            }
        }
        return stack;
    }

    public abstract Item getItemToDrop();

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        TileEntitySaveContent te = (TileEntitySaveContent) world.getTileEntity(pos);
        return SaveStackContainer(te);
    }
}
