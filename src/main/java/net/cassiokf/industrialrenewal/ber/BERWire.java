package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityHVIsolator;
import net.cassiokf.industrialrenewal.util.IRRenderType;
import net.cassiokf.industrialrenewal.util.RenderUtil;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class BERWire extends BERBase<BlockEntityHVIsolator> {
    
    private static final Color c = new Color(56, 56, 56, 255);
    private static final Color c2 = new Color(43, 43, 43, 255);
    
    public BERWire(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityHVIsolator blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        BlockPos source = blockEntity.getBlockPos();
        for (BlockPos node : blockEntity.neighbors) {
            renderWire(stack, buffer, source, node, blockEntity);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
    
    public void renderWire(PoseStack matrixStack, MultiBufferSource renderTypeBuffer, BlockPos startPos, BlockPos endPos, BlockEntity be) {
        float xOffset = 0.5f;
        float zOffset = 0.5f;
        float yOffset = 0.5f;
        
        Vector3d source = new Vector3d(startPos.getX() + xOffset, startPos.getY() + zOffset, startPos.getZ() + yOffset);
        Vector3d destination = new Vector3d(endPos.getX() + xOffset, endPos.getY() + zOffset, endPos.getZ() + yOffset);
        
        Vector3d midPoint = Utils.midPoint(source, destination, 0);
        midPoint = new Vector3d(midPoint.x, Math.min(Math.min(midPoint.y, source.y), destination.y - 0.25f), midPoint.z);
        
        
        float f = Utils.normalizeClamped(c2.getRed(), 0, 255);
        float f1 = Utils.normalizeClamped(c2.getGreen(), 0, 255);
        float f2 = Utils.normalizeClamped(c2.getBlue(), 0, 255);
        
        float finalDeltaX = source.y == destination.y ? (float) (midPoint.x - source.x) : (float) (destination.x - source.x);
        float finalDeltaY = source.y == destination.y ? (float) (midPoint.y - source.y) : (float) (destination.y - source.y);
        float finalDeltaZ = source.y == destination.y ? (float) (midPoint.z - source.z) : (float) (destination.z - source.z);
        
        Vector3f finalStartPos = new Vector3f(xOffset, yOffset, zOffset);
        Vector3f finalEndPos = new Vector3f(finalDeltaX, finalDeltaY, finalDeltaZ);
        finalEndPos.add(finalStartPos);
        
        float lerpY = 0f;
        
        
        if (source.y >= destination.y) {
            int numSegments = 12;
            matrixStack.pushPose();
            PoseStack.Pose p = matrixStack.last();
            
            Level level = be.getLevel();
            long gameTime = level.getGameTime();
            double v = gameTime * 0.04;
            
            VertexConsumer builder = renderTypeBuffer.getBuffer(IRRenderType.WIRE_RENDER_TYPE);
            Vector3f startVec = new Vector3f(xOffset, yOffset, zOffset);
            Vector3f endVec;
            
            for (int i = 0; i < numSegments; i++) {
                endVec = Utils.lerp(finalStartPos, finalEndPos, (float) (i + 1) / numSegments);
                lerpY = Utils.lerp(lerpY, finalDeltaY, source.y == destination.y ? 0.25f : 0.33f);
                
                RenderUtil.drawLaser(builder, p.pose(), new Vector3f(endVec.x(), lerpY + yOffset, endVec.z()), startVec, f, f1, f2, 1, 0.025f, v, v + endVec.y() * 1.5, be);
                startVec = new Vector3f(endVec.x(), lerpY + yOffset, endVec.z());
                
            }
            matrixStack.popPose();
        }
    }
}
