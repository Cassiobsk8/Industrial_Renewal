package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntitySolarPanelBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSolarPanel extends BlockTileEntity<TileEntitySolarPanelBase>
{

    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    public BlockSolarPanel(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(
                I18n.format("info.industrialrenewal.produces")
                        + ": "
                        + (IRConfig.Main.baseSolarPanelMaxGeneration.get().toString())
                        + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /*
        @Override
        public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
            return BLOCK_AABB;
        }
    */
    @Nullable
    @Override
    public TileEntitySolarPanelBase createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySolarPanelBase();
    }
}
