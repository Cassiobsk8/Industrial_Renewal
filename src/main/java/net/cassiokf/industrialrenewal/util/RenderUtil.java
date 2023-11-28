package net.cassiokf.industrialrenewal.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderUtil {

    public static Vector3f adjustBeamToEyes(Vector3f from, Vector3f to, BlockEntity be) {
        //This method takes the player's position into account, and adjusts the beam so that its rendered properly whereever you stand
        Player player = Minecraft.getInstance().player;
        Vector3f P = new Vector3f((float) player.getX() - be.getBlockPos().getX(), (float) player.getEyeY() - be.getBlockPos().getY(), (float) player.getZ() - be.getBlockPos().getZ());

        Vector3f PS = new Vector3f(from);
        PS.sub(P);
        Vector3f SE = new Vector3f(to);
        SE.sub(from);

        Vector3f adjustedVec = new Vector3f(PS);
        adjustedVec.cross(SE);
        adjustedVec.normalize();
        return adjustedVec;
    }

    public static void drawLaser(VertexConsumer builder, Matrix4f positionMatrix, Vector3f from, Vector3f to, float r, float g, float b, float alpha, float thickness, double v1, double v2, BlockEntity be) {
        Vector3f adjustedVec = adjustBeamToEyes(from, to, be);
        adjustedVec.mul(thickness); //Determines how thick the beam is
//        Vector3f adjustedVec = new Vector3f(thickness, thickness, thickness);

        Vector3f p1 = new Vector3f(from);
        p1.add(adjustedVec);
        Vector3f p2 = new Vector3f(from);
        p2.sub(adjustedVec);
        Vector3f p3 = new Vector3f(to);
        p3.add(adjustedVec);
        Vector3f p4 = new Vector3f(to);
        p4.sub(adjustedVec);

        builder.vertex(positionMatrix, p1.x(), p1.y(), p1.z())
                .color(r, g, b, alpha)
                .uv(1, (float) v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .endVertex();
        builder.vertex(positionMatrix, p3.x(), p3.y(), p3.z())
                .color(r, g, b, alpha)
                .uv(1, (float) v2)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .endVertex();
        builder.vertex(positionMatrix, p4.x(), p4.y(), p4.z())
                .color(r, g, b, alpha)
                .uv(0, (float) v2)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .endVertex();
        builder.vertex(positionMatrix, p2.x(), p2.y(), p2.z())
                .color(r, g, b, alpha)
                .uv(0, (float) v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .endVertex();
    }
}
