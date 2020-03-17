package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntityConnectedMultiblocks;
import cassiokf.industrialrenewal.init.ModBlocks;
import cassiokf.industrialrenewal.tileentity.TileEntityWindTurbinePillar;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class BlockWindTurbinePillar extends BlockTileEntityConnectedMultiblocks<TileEntityWindTurbinePillar>
{

    public BlockWindTurbinePillar(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        Item playerItem = player.inventory.getCurrentItem().getItem();
        Block clickedBlock = state.getBlock();
        if (playerItem.equals(ItemBlock.getItemFromBlock(ModBlocks.turbinePillar)) && clickedBlock.equals(ModBlocks.turbinePillar))
        {
            int n = 1;
            while (world.getBlockState(pos.up(n)).getBlock() instanceof BlockWindTurbinePillar)
            {
                n++;
            }
            if (world.getBlockState(pos.up(n)).getBlock().isReplaceable(world, pos.up(n)))
            {
                world.setBlockState(pos.up(n), getBlockFromItem(playerItem).getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
                if (!player.isCreative())
                {
                    player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                }
                return true;
            }
        }
        return false;
    }

    private boolean canConnectTo(final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);

        if (neighbourDirection == EnumFacing.DOWN)
        {
            return !(neighbourState.getBlock() instanceof BlockWindTurbinePillar);
        }
        TileEntity te = worldIn.getTileEntity(ownPos.offset(neighbourDirection));
        return te != null && te.hasCapability(CapabilityEnergy.ENERGY, neighbourDirection.getOpposite());
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            EnumFacing facing = state.getValue(FACING);
            IExtendedBlockState eState = (IExtendedBlockState) state;
            boolean down = canConnectTo(world, pos, EnumFacing.DOWN);
            eState = eState.withProperty(DOWN, down).withProperty(UP, false);
            if (down)
                eState = eState.withProperty(SOUTH, canConnectTo(world, pos, facing.getOpposite()))
                        .withProperty(NORTH, canConnectTo(world, pos, facing))
                        .withProperty(EAST, canConnectTo(world, pos, facing.rotateY()))
                        .withProperty(WEST, canConnectTo(world, pos, facing.rotateYCCW()));
            else
                eState = eState.withProperty(SOUTH, false)
                        .withProperty(NORTH, false)
                        .withProperty(EAST, false)
                        .withProperty(WEST, false);
            return eState;
        }
        return state;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public TileEntityWindTurbinePillar createTileEntity(World world, IBlockState state)
    {
        return new TileEntityWindTurbinePillar();
    }
}
