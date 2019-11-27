package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.tubes.TileEntityEnergyCable;
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

    public BlockEnergyCable(String name, CreativeTabs tab) {
        super(name, tab);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(IRConfig.MainConfig.Main.maxEnergyCableTransferAmount + " FE/t");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean canConnectToPipe(IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection)
    {
        IBlockState state = worldIn.getBlockState(ownPos.offset(neighbourDirection));
        return state.getBlock() instanceof BlockEnergyCable;
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
    public Class<TileEntityEnergyCable> getTileEntityClass()
    {
        return TileEntityEnergyCable.class;
    }

    @Nullable
    @Override
    public TileEntityEnergyCable createTileEntity(World world, IBlockState state)
    {
        return new TileEntityEnergyCable();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack playerStack = entity.getHeldItem(EnumHand.MAIN_HAND);
        if (playerStack.getItem() == ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor))
        {
            if (!world.isRemote)
            {
                world.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(pos, ModBlocks.floorCable.getDefaultState(), 3);
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
                world.setBlockState(pos, ModBlocks.energyCableGauge.getDefaultState().withProperty(BlockEnergyCableGauge.FACING, entity.getHorizontalFacing()), 3);
                if (!entity.isCreative())
                {
                    playerStack.shrink(1);
                }
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnergyCable();
    }
}
