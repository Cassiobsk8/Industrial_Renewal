package cassiokf.industrialrenewal.armour.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Safety Helmet - CassioKF
 * Created using Tabula 7.0.0
 */
public class SafetyHelmetModel extends ModelBiped {
    private ModelRenderer shape1;
    private ModelRenderer shape2;
    private ModelRenderer shape3;

    public SafetyHelmetModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.shape3 = new ModelRenderer(this, 30, 1);
        this.shape3.addBox(-2.0F, -10.0F, -5.0F, 4, 4, 10, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 11);
        this.shape2.addBox(-4.5F, -9.0F, -4.5F, 9, 3, 9, 0.0F);
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.addBox(-5.0F, -6.0F, -5.0F, 10, 1, 10, 0.0F);

        bipedHead.addChild(this.shape1);
        bipedHead.addChild(this.shape2);
        bipedHead.addChild(this.shape3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
