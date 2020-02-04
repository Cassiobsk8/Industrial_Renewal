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
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEnergyCable extends BlockPipeBase<TileEntityEnergyCable> implements ITileEntityProvider
{
    public EnumEnergyCableType type;

    public BlockEnergyCable(EnumEnergyCableType type, String name, CreativeTabs tab)
    {
        super(name, tab);
        this.type = type;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        int amount;
        switch (type)
        {
            default:
            case LV:
                amount = IRConfig.MainConfig.Main.maxLVEnergyCableTransferAmount;
                break;
            case MV:
                amount = IRConfig.MainConfig.Main.maxMVEnergyCableTransferAmount;
                break;
            case HV:
                amount = IRConfig.MainConfig.Main.maxHVEnergyCableTransferAmount;
                break;
        }
        tooltip.add(amount + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
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
    public boolean canConnectToPipe(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos otherPos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(otherPos);
        Block block = state.getBlock();
        return (block instanceof BlockEnergyCable && type.equals(((BlockEnergyCable) block).type))
                || (block instanceof BlockCableTray && ((BlockCableTray) block).isCablePresent(worldIn, otherPos, convertFromType(type)));
    }

    @Override
    public boolean canConnectToCapability(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        BlockPos pos = ownPos.offset(neighbourDirection);
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        return !(state.getBlock() instanceof BlockEnergyCable) && te != null && te.hasCapability(CapabilityEnergy.ENERGY, neighbourDirection.getOpposite());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack playerStack = entity.getHeldItem(EnumHand.MAIN_HAND);
        if (playerStack.getItem() == ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor))
        {
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1.0F, 1.0F);
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
                world.setBlockState(pos, block.getDefaultState(), 3);
                if (!entity.isCreative())
                {
                    playerStack.shrink(1);
                }
            }
            return true;
        }
        if (playerStack.getItem() == ItemBlock.getItemFromBlock(ModBlocks.energyLevel))
        {
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1.0F, 1.0F);
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
                world.setBlockState(pos, block.getDefaultState().withProperty(BlockEnergyCableGauge.FACING, entity.getHorizontalFacing()), 3);
                if (!entity.isCreative())
                {
                    playerStack.shrink(1);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass()
    {
        switch (type)
        {
            default:
            case LV:
                return TileEntityEnergyCableLV.class;
            case MV:
                return TileEntityEnergyCableMV.class;
            case HV:
                return TileEntityEnergyCableHV.class;
        }
    }

    @Nullable
    @Override
    public TileEntityEnergyCable createTileEntity(World world, IBlockState state)
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
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
