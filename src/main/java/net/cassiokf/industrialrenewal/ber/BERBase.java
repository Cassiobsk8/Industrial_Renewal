package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Quaternionf;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public abstract class BERBase<T extends BlockEntity> implements BlockEntityRenderer<T> {
    
    public static final ItemStack cutter = new ItemStack(ModItems.cutter.get());
    public static final ItemStack energyBarLevel = new ItemStack(ModItems.barLevel.get());
    public static final ItemStack pointerLong = new ItemStack(ModItems.pointerLong.get());
    public static final ItemStack pointer = new ItemStack(ModItems.pointer.get());
    public static final ItemStack limiter = new ItemStack(ModItems.limiter.get());
    public static final ItemStack indicator_on = new ItemStack(ModItems.indicator_on.get());
    public static final ItemStack indicator_off = new ItemStack(ModItems.indicator_off.get());
    public static final ItemStack switch_on = new ItemStack(ModItems.switch_on.get());
    public static final ItemStack switch_off = new ItemStack(ModItems.switch_off.get());
    public static final ItemStack push_button = new ItemStack(ModItems.push_button.get());
    public static final ItemStack label_5 = new ItemStack(ModItems.label_5.get());
    
    public double xPos = 0D;
    public double zPos = 0D;
    
    public BERBase(BlockEntityRendererProvider.Context context) {
    
    }
    
    public static void renderScreenTexts(PoseStack matrixStack, MultiBufferSource buffer, Direction facing, double x, double y, double z, String[] text, float spacing, float scale) {
        double lY = y;
        for (String line : text) {
            renderText(matrixStack, buffer, facing, x, lY, z, ChatFormatting.GREEN + line, scale, false);
            lY -= spacing;
        }
    }
    
    public static ItemStack getIndicator(boolean value) {
        return value ? indicator_on : indicator_off;
    }
    
    public static ItemStack getSwitch(boolean value) {
        return value ? switch_on : switch_off;
    }
    
    public static void renderText(PoseStack poseStack, MultiBufferSource buffer, Direction facing, double x, double y, double z, String st, float scale) {
        renderText(poseStack, buffer, facing, x, y, z, st, scale, true);
    }
    
    private static void renderText(PoseStack poseStack, MultiBufferSource buffer, Direction facing, double x, double y, double z, String st, float scale, boolean centerText) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        
        rotateAccordingly(facing, poseStack);
        
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        
        poseStack.scale(scale, scale, scale);
        Font font = Minecraft.getInstance().font;
        
        float xh = centerText ? (-(float) font.width(st) / 2) : 0;
        font.drawInBatch(st, xh, 0, Color.WHITE.getRGB(), false, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 255);
        
        poseStack.popPose();
    }
    
    public static void renderBarLevel(Level level, PoseStack matrixStack, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, double x, double y, double z, float fill, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        rotateAccordingly(facing, matrixStack);
        matrixStack.scale(scale, fill * scale, 0.05f);
        
        Minecraft.getInstance().getItemRenderer().renderStatic(energyBarLevel, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, combinedOverlayIn, matrixStack, buffetIn, level, 0);
        matrixStack.popPose();
    }
    
    public static void renderPointer(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Level level, Direction facing, double x, double y, double z, float angle, ItemStack pointer, float scale) {
        matrixStack.pushPose();
        
        matrixStack.translate(x, y, z);
        rotateAccordingly(facing, matrixStack);
        matrixStack.mulPose(Axis.ZP.rotationDegrees(90));
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Axis.ZP.rotationDegrees(-angle));
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        
        renderer.renderStatic(pointer, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, buffetIn, level, 0);
        
        matrixStack.popPose();
    }
    
    public static void render3dItem(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, Level world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight) {
        render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, buffetIn, facing, world, x, y, z, stack, scale, disableLight, false, 0, 0, 0, 0, false, false);
    }
    
    public static void render3dItem(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, Level world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, float rotation, float rX, float rY, float rZ) {
        render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, buffetIn, facing, world, x, y, z, stack, scale, disableLight, true, rotation, rX, rY, rZ, false, false);
    }
    
    public static void render3dItem(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, Level world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, boolean applyRotation, float rotation, float rX, float rY, float rZ, boolean rotateHorizontal, boolean rotateVertical) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        
        matrixStack.scale(scale, scale, scale);
        rotateAccordingly(facing, matrixStack);
        
        if (rotateHorizontal) matrixStack.mulPose(new Quaternionf(Axis.XP.rotationDegrees(90)));
        if (rotateVertical) matrixStack.mulPose(Axis.YP.rotationDegrees(90));
        if (applyRotation) {
            matrixStack.mulPose(Axis.XP.rotationDegrees(rotation * rX));
            matrixStack.mulPose(Axis.YP.rotationDegrees(rotation * rY));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(rotation * rZ));
        }
        
        BakedModel model = renderer.getModel(stack, world, null, 0);
        model = ForgeHooksClient.handleCameraTransforms(matrixStack, model, ItemDisplayContext.NONE, false);
        
        //        Utils.debug("", combinedLightIn);
        if (applyRotation) {
            renderer.render(stack, ItemDisplayContext.GROUND, true, matrixStack, buffetIn, combinedLightIn, OverlayTexture.NO_OVERLAY, model);
        } else {
            renderer.renderStatic(stack, ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStack, buffetIn, world, 1);
        }
        
        matrixStack.popPose();
    }
    
    public static float smoothAnimation(float rotation, float oldRotation, float partialTick, boolean invert) {
        //shift = shiftOld + (shift - shiftOld) * partialTick
        float r = oldRotation + (rotation - oldRotation) * partialTick;
        return invert ? -r : r;
    }
    
    public static void rotateAccordingly(Direction facing, PoseStack poseStack) {
        switch (facing) {
            default:
            case SOUTH:
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case NORTH:
                break;
            case WEST:
                //                poseStack.mulPose(new Quaternionf(0, 90, 0, 1));
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case EAST:
                poseStack.mulPose(Axis.YP.rotationDegrees(-90));
                break;
        }
    }
    
    public static int lighting(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        assert level != null;
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
    
    @Override
    public void render(T blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
    
    }
    
    public void doTheMath(Direction facing, double x, double z, double offset, double sidePlus) {
        switch (facing) {
            case SOUTH -> {
                xPos = x + (0.5 - sidePlus);
                zPos = z + (1 - offset);
            }
            case NORTH -> {
                xPos = x + (0.5 + sidePlus);
                zPos = z + offset;
            }
            case EAST -> {
                xPos = x + (1 - offset);
                zPos = z + (0.5 + sidePlus);
            }
            case WEST -> {
                xPos = x + offset;
                zPos = z + (0.5 - sidePlus);
            }
        }
    }
}
