package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockFluidPipe extends BlockPipeBase implements IFluidHandler {
    public BlockFluidPipe(String name) {
        super(name);
    }

    @Override
    protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        // Connect if the neighbouring block is another pipe
        if (super.isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection)) {
            return true;
        }

        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final Block neighbourBlock = neighbourState.getBlock();

        // Connect if the neighbouring block has a TileEntity with an IFluidHandler for the adjacent face
        if (neighbourBlock.hasTileEntity(neighbourState)) {
            final TileEntity tileEntity = world.getTileEntity(neighbourPos);
            return tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite());
        }

        // Connect if the neighbouring block is a fluid/liquid, FluidUtil.getFluidHandler will provide an IFluidHandler wrapper to drain from it
        return neighbourBlock instanceof IFluidBlock || neighbourBlock instanceof BlockLiquid;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    //IFluidHandler

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{new FluidTankProperties(null, 1000, true, false)};
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 1000;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

}
