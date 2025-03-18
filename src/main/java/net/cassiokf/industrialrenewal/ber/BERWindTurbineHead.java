package net.cassiokf.industrialrenewal.ber;


import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbineHead;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class BERWindTurbineHead extends BERBase<BlockEntityWindTurbineHead> {
    public static final ItemStack blade = new ItemStack(ModItems.WIND_BLADE.get());
    
    public BERWindTurbineHead(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityWindTurbineHead blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;
        
        if (blockEntity.hasBlade()) {
            Direction facing = blockEntity.getBlockFacing();
            doTheMath(facing, x, z, 0, 0);
            float rotation = smoothAnimation(blockEntity.getRotation(), blockEntity.getOldRotation(), partialTick, true);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.5f, zPos, blade, 12.0f, false, true, rotation, 0, 1, 0, true, false);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
