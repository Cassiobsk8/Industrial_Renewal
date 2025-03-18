package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityFluidTank;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class BERFluidTank extends BERBase<BlockEntityFluidTank> {
    
    public BERFluidTank(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityFluidTank blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        if (blockEntity != null && blockEntity.isMaster() && blockEntity.isBase()) {
            double x = 0, y = 0, z = 0;
            Direction facing = blockEntity.getMasterFacing();
            doTheMath(facing, x, z, 1.98, 0);
            renderText(stack, buffer, facing, xPos, y + 0.36, zPos, blockEntity.getFluidName(), 0.008F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.63, zPos, blockEntity.getFluidAngle(), pointer, 0.3F);
        }
    }
}
