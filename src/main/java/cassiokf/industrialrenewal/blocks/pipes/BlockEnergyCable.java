package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCable;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableHV;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableLV;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableMV;
import cassiokf.industrialrenewal.util.enums.EnumCableIn;
import cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEnergyCable extends BlockPipeBase<TileEntityEnergyCable>
{
    public EnumEnergyCableType type;

    public BlockEnergyCable(EnumEnergyCableType type)
    {
        super(Block.Properties.create(Material.IRON), 4, 4);
        this.type = type;
    }

    public static EnumCableIn convertFromType(EnumEnergyCableType type)
    {
        switch (type)
        {
            default:
            case LV:
                return EnumCableIn.LV;
            case MV:
                return EnumCableIn.MV;
            case HV:
                return EnumCableIn.HV;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        int amount;
        switch (type)
        {
            default:
            case LV:
                amount = IRConfig.Main.maxLVEnergyCableTransferAmount.get();
                break;
            case MV:
                amount = IRConfig.Main.maxMVEnergyCableTransferAmount.get();
                break;
            case HV:
                amount = IRConfig.Main.maxHVEnergyCableTransferAmount.get();
                break;
        }
        tooltip.add(new StringTextComponent(amount + " FE/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public Block getBlockFromType()
    {
        switch (type)
        {
            default:
            case LV:
                return BlocksRegistration.ENERGYCABLELV.get();
            case MV:
                return BlocksRegistration.ENERGYCABLEMV.get();
            case HV:
                return BlocksRegistration.ENERGYCABLEHV.get();
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack playerStack = player.getHeldItem(handIn);
        if (playerStack.getItem() == BlocksRegistration.INDFLOOR_ITEM.get())
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                Block block = getBlockFromType();
                worldIn.setBlockState(pos, block.getDefaultState(), 3);
                if (!player.isCreative())
                {
                    playerStack.shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
        if (playerStack.getItem() == BlocksRegistration.ENERGYLEVEL_ITEM.get())
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                Block block;
                switch (type)
                {
                    default:
                    case LV:
                        block = BlocksRegistration.ENERGYCABLEGAUGELV.get();
                        break;
                    case MV:
                        block = BlocksRegistration.ENERGYCABLEGAUGEMV.get();
                        break;
                    case HV:
                        block = BlocksRegistration.ENERGYCABLEGAUGEHV.get();
                        break;
                }
                worldIn.setBlockState(pos, block.getDefaultState().with(BlockEnergyCableGauge.FACING, player.getHorizontalFacing()), 3);
                if (!player.isCreative())
                {
                    playerStack.shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public TileEntityEnergyCable createTileEntity(BlockState state, IBlockReader world)
    {
        switch (type)
        {
            default:
            case LV:
                return new TileEntityEnergyCableLV();
            case MV:
                return new TileEntityEnergyCableMV();
            case HV:
                return new TileEntityEnergyCableHV();
        }
    }
}
