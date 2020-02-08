package cassiokf.industrialrenewal.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class SafetyBeltModel extends BipedModel<PlayerEntity>
{
    private ModelRenderer shape15;
    private ModelRenderer shape16;
    private ModelRenderer shape17;
    private ModelRenderer shape18;
    private ModelRenderer shape19;
    private ModelRenderer shape21;
    private ModelRenderer shape20;
    private ModelRenderer shape22;
    private ModelRenderer shape23;
    private ModelRenderer shape24;
    private ModelRenderer shape25;
    private ModelRenderer shape26;
    private ModelRenderer shape27;
    private ModelRenderer shape28;
    private ModelRenderer shape29;
    private ModelRenderer shape30;
    private ModelRenderer shape31;

    public SafetyBeltModel() {
        super(1f);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shape19 = new ModelRenderer(this, 45, 10);
        this.shape19.addBox(-4.2F, 8.0F, -2.3F, 8, 2, 1, 0.0F);
        this.shape21 = new ModelRenderer(this, 47, 13);
        this.shape21.addBox(-4.2F, 8.0F, -1.85F, 2, 2, 4, 0.0F);
        this.shape26 = new ModelRenderer(this, 48, 19);
        this.shape26.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.shape26.addBox(-0.5F, -3.5F, -2.1F, 1, 4, 1, 0.0F);
        this.setRotateAngle(shape26, 0.0F, 0.0F, 0.7853981633974483F);
        this.shape24 = new ModelRenderer(this, 49, 6);
        this.shape24.addBox(-0.5F, 11.5F, -2.2F, 1, 1, 3, 0.0F);
        this.shape16 = new ModelRenderer(this, 41, 0);
        this.shape16.addBox(1.7F, -1.2F, 1.1F, 1, 10, 1, 0.0F);
        this.setRotateAngle(shape16, 0.0F, 0.0F, 0.5759586531581287F);
        this.shape15 = new ModelRenderer(this, 33, 0);
        this.shape15.addBox(-3.0F, -0.1F, -2.1F, 1, 9, 1, 0.0F);
        this.shape18 = new ModelRenderer(this, 45, 0);
        this.shape18.addBox(2.0F, -0.1F, -2.1F, 1, 9, 1, 0.0F);
        this.shape23 = new ModelRenderer(this, 34, 18);
        this.shape23.addBox(-3.0F, 2.0F, -2.2F, 6, 1, 1, 0.0F);
        this.shape31 = new ModelRenderer(this, 50, 0);
        this.shape31.addBox(-3.0F, -0.1F, -2.0F, 1, 1, 4, 0.0F);
        this.shape25 = new ModelRenderer(this, 57, 0);
        this.shape25.addBox(-0.5F, 11.5F, 0.2F, 1, 1, 2, 0.0F);
        this.shape28 = new ModelRenderer(this, 59, 13);
        this.shape28.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.shape28.addBox(-0.5F, -3.5F, 1.1F, 1, 4, 1, 0.0F);
        this.setRotateAngle(shape28, 0.0F, 0.0F, -0.7853981633974483F);
        this.shape27 = new ModelRenderer(this, 58, 18);
        this.shape27.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.shape27.addBox(-0.5F, -3.5F, -2.1F, 1, 4, 1, 0.0F);
        this.setRotateAngle(shape27, 0.0F, 0.0F, -0.7853981633974483F);
        this.shape29 = new ModelRenderer(this, 60, 5);
        this.shape29.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.shape29.addBox(-0.5F, -3.5F, 1.1F, 1, 4, 1, 0.0F);
        this.setRotateAngle(shape29, 0.0F, 0.0F, 0.7853981633974483F);
        this.shape22 = new ModelRenderer(this, 49, 0);
        this.shape22.addBox(2.1F, 8.0F, -2.15F, 2, 2, 4, 0.0F);
        this.shape30 = new ModelRenderer(this, 50, 0);
        this.shape30.addBox(2.0F, -0.1F, -2.0F, 1, 1, 4, 0.0F);
        this.shape17 = new ModelRenderer(this, 37, 0);
        this.shape17.addBox(-2.7F, -1.3F, 1.1F, 1, 10, 1, 0.0F);
        this.setRotateAngle(shape17, 0.0F, 0.0F, -0.593411945678072F);
        this.shape20 = new ModelRenderer(this, 33, 13);
        this.shape20.addBox(-3.8F, 8.0F, 1.3F, 8, 2, 1, 0.0F);

        bipedBody.addChild(this.shape15);
        bipedBody.addChild(this.shape16);
        bipedBody.addChild(this.shape17);
        bipedBody.addChild(this.shape18);
        bipedBody.addChild(this.shape19);
        bipedBody.addChild(this.shape20);
        bipedBody.addChild(this.shape21);
        bipedBody.addChild(this.shape22);
        bipedBody.addChild(this.shape23);
        bipedBody.addChild(this.shape24);
        bipedBody.addChild(this.shape25);
        bipedBody.addChild(this.shape26);
        bipedBody.addChild(this.shape27);
        bipedBody.addChild(this.shape28);
        bipedBody.addChild(this.shape29);
        bipedBody.addChild(this.shape30);
        bipedBody.addChild(this.shape31);
    }

    @Override
    public void render(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
