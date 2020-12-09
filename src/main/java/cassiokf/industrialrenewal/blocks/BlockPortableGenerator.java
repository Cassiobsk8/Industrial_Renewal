package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockSaveContent;
import cassiokf.industrialrenewal.handlers.FluidGenerator;
import cassiokf.industrialrenewal.tileentity.TileEntityPortableGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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

public class BlockPortableGenerator extends BlockSaveContent
{
    public BlockPortableGenerator()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void doAdditionalFunction(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (hit.getFace() == state.get(FACING).getOpposite())
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityPortableGenerator)
            {
                ((TileEntityPortableGenerator) te).changeGenerator();
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.produces")
                + ": "
                + (FluidGenerator.energyPerTick)
                + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        BlockState result = super.rotate(state, world, pos, direction);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityPortableGenerator) ((TileEntityPortableGenerator) te).getBlockFacing(true);
        return result;
    }

    @Override
    public Item getItemToDrop()
    {
        return Item.getItemFromBlock(this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityPortableGenerator();
    }
}
