package cassiokf.industrialrenewal.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Safety Helmet - CassioKF
 * Created using Tabula 7.0.0
 */
public class SafetyHelmetModel extends BipedModel<PlayerEntity>
{
    private ModelRenderer shape1;
    private ModelRenderer shape2;
    private ModelRenderer shape3;

    public SafetyHelmetModel()
    {
        super(1f);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.shape1 = new ModelRenderer(this, 0, 41);
        this.shape1.addBox(-5.0F, -6.0F, -5.0F, 10, 1, 10, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 52);
        this.shape2.addBox(-4.5F, -9.0F, -4.5F, 9, 3, 9, 0.0F);
        this.shape3 = new ModelRenderer(this, 30, 42);
        this.shape3.addBox(-2.0F, -10.0F, -5.0F, 4, 4, 10, 0.0F);

        bipedHead.addChild(this.shape1);
        bipedHead.addChild(this.shape2);
        bipedHead.addChild(this.shape3);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
