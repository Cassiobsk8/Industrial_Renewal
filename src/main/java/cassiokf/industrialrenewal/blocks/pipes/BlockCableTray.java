package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.enums.enumproperty.EnumBaseDirection;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.properties.PropertyBaseDirection;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityCableTray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCableTray extends BlockPipeBase<TileEntityCableTray>
{
    public static final IProperty<EnumBaseDirection> BASE = PropertyBaseDirection.create("base");

    public BlockCableTray()
    {
        super(Block.Properties.create(Material.IRON), 12, 12);
        this.setDefaultState(this.getDefaultState().with(BASE, EnumBaseDirection.NONE));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent("Can place energy Cable and Fluid Pipe in one block"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (handIn.equals(Hand.MAIN_HAND))
        {
            Item item = player.getHeldItemMainhand().getItem();
            Block block = Block.getBlockFromItem(item);
            if (item instanceof ItemPowerScrewDrive || block instanceof BlockFluidPipe || block instanceof BlockEnergyCable)
            {
                TileEntityCableTray te = (TileEntityCableTray) worldIn.getTileEntity(pos);
                if (te != null)
                {
                    boolean change = te.onBlockActivated(player, player.getHeldItemMainhand());
                    if (change) worldIn.notifyBlockUpdate(pos, state, state, 3);
                    return change ? ActionResultType.SUCCESS : ActionResultType.PASS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(BASE);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        EnumBaseDirection direction = EnumBaseDirection.NONE;
        Block block = context.getWorld().getBlockState(context.getPos().offset(context.getFace().getOpposite())).getBlock();
        if (!(block instanceof BlockCableTray))
        {
            direction = EnumBaseDirection.byIndex(context.getFace().getOpposite().getIndex());
        }

        return this.getDefaultState().with(BASE, direction);
    }

    @Nullable
    @Override
    public TileEntityCableTray createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCableTray();
    }
}
