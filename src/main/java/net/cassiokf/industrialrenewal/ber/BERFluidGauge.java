package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityFluidGauge;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BERFluidGauge extends BERBase<BlockEntityFluidGauge> {
    public BERFluidGauge(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityFluidGauge blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0, y = 0, z = 0;
        Direction facing = blockEntity.getGaugeFacing();
        doTheMath(facing, x, z, 0.6, 0);
        renderText(stack, buffer, facing, xPos, y + 0.18, zPos, blockEntity.getText(), 0.01F);
        //renderPointer(facing, xPos, y + 0.45, zPos, blockEntity.getTankFill(), pointer, 0.3F);
        renderPointer(stack, LightTexture.FULL_BRIGHT, combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.45F, zPos, blockEntity.getTankFill(), pointer, 0.3F);
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
