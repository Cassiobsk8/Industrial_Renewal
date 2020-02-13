package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.enums.EnumCableIn;
import cassiokf.industrialrenewal.enums.EnumEnergyCableType;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCable;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableHV;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableLV;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCableMV;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEnergyCable extends BlockPipeBase<TileEntityEnergyCable>
{
    public EnumEnergyCableType type;

    public BlockEnergyCable(EnumEnergyCableType type, Block.Properties properties)
    {
        super(properties);
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

    @Override
    public boolean canConnectToPipe(IBlockReader worldIn, BlockPos ownPos, Direction neighbourDirection)
    {
        BlockPos otherPos = ownPos.offset(neighbourDirection);
        BlockState state = worldIn.getBlockState(otherPos);
        Block block = state.getBlock();
        return (block instanceof BlockEnergyCable && type.equals(((BlockEnergyCable) block).type))
                || (block instanceof BlockCableTray && ((BlockCableTray) block).isCablePresent(worldIn, otherPos, convertFromType(type)));
    }

    @Override
    public boolean canConnectToCapability(IBlockReader worldIn, BlockPos ownPos, Direction neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        BlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        return !(state.getBlock() instanceof BlockEnergyCable) && te != null && te.getCapability(CapabilityEnergy.ENERGY, neighbourDirection.getOpposite()).isPresent();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        ItemStack playerStack = player.getHeldItem(Hand.MAIN_HAND);
        if (playerStack.getItem() == BlockItem.getItemFromBlock(ModBlocks.blockIndFloor))
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                Block block;
                switch (type)
                {
                    default:
                    case LV:
                        block = ModBlocks.floorCableLV;
                        break;
                    case MV:
                        block = ModBlocks.floorCableMV;
                        break;
                    case HV:
                        block = ModBlocks.floorCableHV;
                        break;
                }
                worldIn.setBlockState(pos, block.getDefaultState(), 3);
                if (!player.isCreative())
                {
                    playerStack.shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
        if (playerStack.getItem() == BlockItem.getItemFromBlock(ModBlocks.energyLevel))
        {
            if (!worldIn.isRemote)
            {
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                Block block;
                switch (type)
                {
                    default:
                    case LV:
                        block = ModBlocks.energyCableGaugeLV;
                        break;
                    case MV:
                        block = ModBlocks.energyCableGaugeMV;
                        break;
                    case HV:
                        block = ModBlocks.energyCableGaugeHV;
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
