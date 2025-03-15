package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BEREnergyCableMeter extends BERBase<BlockEntityEnergyCable>
{
    
    public BEREnergyCableMeter(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityEnergyCable blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x=0,y=0,z=0;
        Direction facing = blockEntity.getBlockFacing();
        doTheMath(facing, x, z, 0.69, 0.12f);
        renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.645, zPos, blockEntity.getOutPutAngle(), pointerLong, 0.4F);
        doTheMath(facing, x, z, 0.69, 0f);
        renderText(stack, buffer, facing, xPos, y + 0.48, zPos, blockEntity.GetText(), 0.008F);
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
