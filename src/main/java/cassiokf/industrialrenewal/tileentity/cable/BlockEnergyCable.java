package cassiokf.industrialrenewal.tileentity.cable;

import cassiokf.industrialrenewal.blocks.BlockPipeBase;
import cassiokf.industrialrenewal.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class BlockEnergyCable extends BlockPipeBase implements ITileEntityProvider {

    public BlockEnergyCable(String name) {
        super(name);

    }

    @Override
    protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {

        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final Block neighbourBlock = neighbourState.getBlock();

        if (neighbourBlock.hasTileEntity(neighbourState)) {
            final TileEntity tileEntity = world.getTileEntity(neighbourPos);

            return tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, neighbourDirection.getOpposite());

        }

        return neighbourBlock instanceof IEnergyStorage;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        if (entity.inventory.getCurrentItem().getItem() == net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor)) {
            world.playSound(null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.setBlockState(new BlockPos(i, j, k), ModBlocks.floorCable.getDefaultState(), 3);
            if (!entity.isCreative()) {
                entity.inventory.clearMatchingItems(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor), 0, 1, null);
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
    //IEnergyStorage

}
