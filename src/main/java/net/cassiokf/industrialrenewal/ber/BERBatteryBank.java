package net.cassiokf.industrialrenewal.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cassiokf.industrialrenewal.block.BlockBatteryBank;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityBatteryBank;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BERBatteryBank extends BERBase<BlockEntityBatteryBank> {

    public BERBatteryBank(BlockEntityRendererProvider.Context context){
        super(context);
    }

    @Override
    public void render(BlockEntityBatteryBank blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0;
        int y = 0;
        int z = 0;

        if(blockEntity!=null){
            Direction facing = blockEntity.getBlockState().getValue(BlockBatteryBank.FACING);
            doTheMath(facing, x, z, 1.024, 0);
            renderText(stack, buffer, facing, xPos, y + 0.43, zPos, blockEntity.getText(), 0.005F);
//            Utils.debug("RENDERING", blockEntity.getTankFill());
            renderBarLevel(blockEntity.getLevel(), stack, combinedOverlay, buffer, facing, xPos, y + 0.49, zPos, blockEntity.getTankFill(), 0.7F);
        }
    }
}
