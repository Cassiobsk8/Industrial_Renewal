package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityMiner;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BERMining extends BERBase<BlockEntityMiner>{

    public BERMining(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityMiner blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        if (blockEntity!= null && blockEntity.isMaster())
        {
            int x = 0;
            int y = 0;
            int z = 0;
            Direction facing = blockEntity.getMasterFacing();
            if (blockEntity.hasDrill())
            {
                render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), x + 0.5, y - 0.8f - blockEntity.getSlide(), z + 0.5, blockEntity.getDrill(), 5.0f, false, blockEntity.getRotation(), 0, 1, 0);
            }

            //Screen
            doTheMath(facing, x, z, 1.99, -1.29);
            renderScreenTexts(stack, buffer, facing, xPos, y + 1.4, zPos, blockEntity.getScreenTexts(), 0.1f, 0.004F);

            //Energy bar
            doTheMath(facing, x, z, 1.96, -1.15);
            renderText(stack, buffer, facing, xPos, y + 0.165, zPos, blockEntity.getEnergyText(2), 0.007F);
            //doTheMath(facing, x, z, 1.96, -1.15);
            renderBarLevel(blockEntity.getLevel(), stack, combinedOverlay, buffer, facing, xPos, y + 0.22, zPos, blockEntity.getEnergyFill(), 0.7f);

            //Coolant
            doTheMath(facing, x, z, 1.95, +1.16);
            renderText(stack, buffer, facing, xPos, y + 0.18, zPos, blockEntity.getWaterText(1), 0.008F);
            renderText(stack, buffer, facing, xPos, y + 0.07, zPos, blockEntity.getWaterText(2), 0.008F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.43, zPos, blockEntity.getWaterFill(), pointer, 0.3F);

            //indicator
            doTheMath(facing, x, z, 1.95, -0.8);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.5, zPos, getIndicator(blockEntity.isRunning()), 1f, true);
            //switch
            doTheMath(facing, x, z, 1.95, -0.8);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.32, zPos, getSwitch(blockEntity.isRunning()), 1f, true);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
