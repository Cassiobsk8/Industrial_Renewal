package net.cassiokf.industrialrenewal.ber;


import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbinePillar;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class BERWindTurbinePillar extends BERBase<BlockEntityWindTurbinePillar> {
    
    public BERWindTurbinePillar(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityWindTurbinePillar blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;
        
        if (blockEntity.isBase()) {
            Direction facing = blockEntity.getBlockFacing();
            doTheMath(facing, x, z, 0.78, 0);
            renderText(stack, buffer, facing, xPos, y + 0.72, zPos, blockEntity.getText(), 0.006F);
            doTheMath(facing, x, z, 0.78, 0.11f);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.845, zPos, blockEntity.getGenerationforGauge(), pointerLong, 0.38F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.845, zPos, blockEntity.getPotentialValue(), limiter, 0.57F);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
