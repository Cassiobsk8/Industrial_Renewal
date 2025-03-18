package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.transport.BEFluidPipeGauge;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BERFluidPipeGauge extends BERBase<BEFluidPipeGauge> {
    public BERFluidPipeGauge(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BEFluidPipeGauge blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0, y = 0, z = 0;
        Direction facing = blockEntity.getBlockFacing();
        doTheMath(facing, x, z, 0.72, 0);
        renderText(stack, buffer, facing, xPos, y + 0.56, zPos, blockEntity.getText(), 0.009F);
        renderPointer(stack, LightTexture.FULL_BRIGHT, combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.73F, zPos, blockEntity.getOutPutAngle(), pointer, 0.16F);
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
