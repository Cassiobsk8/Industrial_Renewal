package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntitySteamTurbine;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BERSteamTurbine extends BERBase<BlockEntitySteamTurbine> {
    public BERSteamTurbine(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntitySteamTurbine blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        if (blockEntity != null && blockEntity.isMaster()) {
            int x = 0;
            int z = 0;
            int y = 0;
            Direction facing = blockEntity.getMasterFacing();
            //STEAM
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(stack, buffer, facing, xPos, y + 1.25, zPos, Utils.STEAM_NAME, 0.01F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 1.5, zPos, blockEntity.getSteamFill(), pointer, 0.3F);
            //GENERATION
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(stack, buffer, facing, xPos, y + 0.5, zPos, blockEntity.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.95, -0.96);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.67, zPos, blockEntity.getGenerationFill(), pointerLong, 0.5F);
            //WATER
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(stack, buffer, facing, xPos, y - 0.25, zPos, blockEntity.getWaterText(), 0.01F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.01, zPos, blockEntity.getWaterFill(), pointer, 0.3F);
            //ROTATION
            doTheMath(facing, x, z, 1.95, 0);
            renderText(stack, buffer, facing, xPos, y + 1.25, zPos, blockEntity.getRotationText(), 0.01F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 1.5, zPos, blockEntity.getRotationFill(), pointer, 0.3F);
            //ENERGY
            doTheMath(facing, x, z, 1.95, +1.165);
            renderText(stack, buffer, facing, xPos, y + 0.1, zPos, blockEntity.getEnergyText(), 0.01F);
            doTheMath(facing, x, z, 1.95, +1.165);
            renderBarLevel(blockEntity.getLevel(), stack, combinedOverlay, buffer, facing, xPos, y + 0.184, zPos, blockEntity.getEnergyFill(), 1.2F);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
