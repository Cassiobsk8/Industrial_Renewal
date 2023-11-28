package net.cassiokf.industrialrenewal.ber;


import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntitySolarPanelFrame;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BERSolarPanelFrame extends BERBase<BlockEntitySolarPanelFrame>{


    public BERSolarPanelFrame(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntitySolarPanelFrame blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;
        if (blockEntity!=null && blockEntity.hasPanel())
        {
            Direction facing = blockEntity.getBlockFacing();
            doTheMath(facing, x, z, -0.75, 0);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y - 2.30f, zPos, blockEntity.getPanel(), 16, false, 22.5f, 1, 0, 0);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }

}
