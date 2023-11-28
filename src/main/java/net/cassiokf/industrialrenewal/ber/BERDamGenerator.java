package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamGenerator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class BERDamGenerator extends BERBase<BlockEntityDamGenerator>{

    public BERDamGenerator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityDamGenerator blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        if (blockEntity!=null && blockEntity.isMaster())
        {
            double x = 0, y = 0, z = 0;
            Direction facing = blockEntity.getMasterFacing();
            //GENERATION
            doTheMath(facing, x, z, 1.98, 0);
            renderText(stack, buffer, facing, xPos, y + 0.43, zPos, blockEntity.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.98, 0.115);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.58, zPos, blockEntity.getGenerationFill(), pointerLong, 0.5F);
        }
    }
}
