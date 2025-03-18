package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityEnergyLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BEREnergyLevel extends BERBase<BlockEntityEnergyLevel> {
    
    public BEREnergyLevel(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityEnergyLevel blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0, y = 0, z = 0;
        Direction facing = blockEntity.getGaugeFacing();
        doTheMath(facing, x, z, 0.6, 0);
        renderText(stack, buffer, facing, xPos, y + 0.18, zPos, blockEntity.GetText(), 0.008F);
        renderBarLevel(blockEntity.getLevel(), stack, combinedOverlay, buffer, facing, xPos, y + 0.27, zPos, blockEntity.getTankFill(), 1.2F);
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
