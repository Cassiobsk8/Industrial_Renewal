package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityIndustrialBatteryBank;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class BERIndustrialBatteryBank extends BERBase<BlockEntityIndustrialBatteryBank> {
    
    private static final ItemStack lBattery = new ItemStack(ModItems.BATTERY_LITHIUM.get());
    
    public BERIndustrialBatteryBank(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityIndustrialBatteryBank blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0;
        double y = 0;
        double z = 0;
        
        if (blockEntity != null && blockEntity.isMaster()) {
            Direction facing = blockEntity.getMasterFacing();
            if (blockEntity.isBase()) {
                doTheMath(facing, x, z, 1.97, -0.586);
                renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.486, zPos, blockEntity.getInPutAngle(), pointerLong, 0.6F);
                doTheMath(facing, x, z, 1.97, -0.72);
                renderText(stack, buffer, facing, xPos, y + 0.21, zPos, blockEntity.getInPutText(), 0.008F);
                render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.96f, zPos, label_5, 1.6f, false);
                renderText(stack, buffer, facing, xPos, y + 0.984f, zPos, blockEntity.getInPutIndicatorText(), 0.008F);
                
                doTheMath(facing, x, z, 1.97, 0);
                renderText(stack, buffer, facing, xPos, y + 1.0, zPos, blockEntity.getEnergyText(), 0.006F);
                renderBarLevel(blockEntity.getLevel(), stack, combinedOverlay, buffer, facing, xPos, y + 1.14, zPos, blockEntity.getBatteryFill(), 1.2F);
                
                doTheMath(facing, x, z, 1.97, 0.846f);
                renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.486, zPos, blockEntity.getOutPutAngle(), pointerLong, 0.6F);
                doTheMath(facing, x, z, 1.97, 0.72f);
                renderText(stack, buffer, facing, xPos, y + 0.21, zPos, blockEntity.getOutPutText(), 0.008F);
                render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.96f, zPos, label_5, 1.6f, false);
                renderText(stack, buffer, facing, xPos, y + 0.984f, zPos, blockEntity.getOutPutIndicatorText(), 0.008F);
            }
            int quantity = blockEntity.getBatteries();
            renderBatteries(stack, buffer, lighting(blockEntity), combinedOverlay, blockEntity.getLevel(), quantity, facing, x, y, z);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
    
    private void renderBatteries(PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLightIn, int combinedOverlayIn, Level world, int quantity, Direction facing, Double x, Double y, Double z) {
        if (quantity > 0) {
            float offset = 1.3f;
            float spacing = 0.83f;
            float yOffset = 0.46f;
            float ySpacing = 0.67f;
            
            //Left Side
            float yOff = yOffset;
            for (int r = 0; r < 4; r++) {
                float off = offset;
                for (int zb = 0; zb < 3; zb++) {
                    doTheMath(facing, x, z, off, -0.28f);
                    render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, world, xPos, y - yOff, zPos, lBattery, 1.72f, false, true, -90, 1, 0, 0, false, true);
                    off -= spacing;
                    quantity--;
                    if (quantity == 0) return;
                }
                yOff -= ySpacing;
            }
            //Right Side
            yOff = yOffset;
            for (int r = 0; r < 4; r++) {
                float off = offset;
                for (int zb = 0; zb < 3; zb++) {
                    doTheMath(facing, x, z, off, 0.28f);
                    render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, world, xPos, y - yOff, zPos, lBattery, 1.72f, false, true, 90, 1, 0, 0, false, true);
                    off -= spacing;
                    quantity--;
                    if (quantity == 0) return;
                }
                yOff -= ySpacing;
            }
        }
    }
}
