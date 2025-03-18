package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityPortableGenerator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BERPortableGenerator extends BERBase<BlockEntityPortableGenerator> {
    
    
    public BERPortableGenerator(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(BlockEntityPortableGenerator blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        if (blockEntity != null) {
            int x = 0;
            int y = 0;
            int z = 0;
            Direction facing = blockEntity.getBlockFacing();
            
            doTheMath(facing, x, z, 1.02, -0.26);
            renderText(stack, buffer, facing, xPos, y + 0.514, zPos, blockEntity.getTankText(), 0.005F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.68, zPos, blockEntity.getTankFill(), pointer, 0.2F);
            
            doTheMath(facing, x, z, 1.02, 0.27);
            renderText(stack, buffer, facing, xPos, y + 0.514, zPos, blockEntity.getEnergyText(), 0.005F);
            doTheMath(facing, x, z, 1.02, 0.332);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, blockEntity.getLevel(), facing, xPos, y + 0.627, zPos, blockEntity.getEnergyFill(), pointerLong, 0.35F);
            
            doTheMath(facing, x, z, 1.02, 0);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.77, zPos, getIndicator(blockEntity.isWorking()), 1f, true);
            //switch
            doTheMath(facing, x, z, 1.02, 0);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.62, zPos, getSwitch(blockEntity.isWorking()), 1f, true);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
