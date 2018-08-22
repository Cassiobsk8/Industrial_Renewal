package cassiokf.industrialrenewal.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Fluid Container - CassioKF
 * Created using Tabula 7.0.0
 */
public class FluidContainerModel extends ModelBase {
    public ModelRenderer Axis1;
    public ModelRenderer Axis2;
    public ModelRenderer Wheel1;
    public ModelRenderer Wheel2;
    public ModelRenderer Wheel3;
    public ModelRenderer Wheel4;
    public ModelRenderer TruckBase1;
    public ModelRenderer Axis3;
    public ModelRenderer Axis4;
    public ModelRenderer Wheel5;
    public ModelRenderer Wheel6;
    public ModelRenderer Wheel7;
    public ModelRenderer Wheel8;
    public ModelRenderer TruckBase2;
    public ModelRenderer Base1;
    public ModelRenderer shape22;
    public ModelRenderer shape25;
    public ModelRenderer shape26;
    public ModelRenderer shape27;
    public ModelRenderer shape28;
    public ModelRenderer shape29;
    public ModelRenderer shape30;
    public ModelRenderer shape31;
    public ModelRenderer shape32;
    public ModelRenderer shape33;

    public FluidContainerModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Wheel1 = new ModelRenderer(this, 8, 0);
        this.Wheel1.setRotationPoint(-10.0F, 2.0F, -6.0F);
        this.Wheel1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape33 = new ModelRenderer(this, 0, 74);
        this.shape33.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.shape33.addBox(-12.0F, -4.0F, -1.0F, 24, 4, 8, 0.0F);
        this.Axis2 = new ModelRenderer(this, 4, 0);
        this.Axis2.addBox(-5.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis2, 1.5707963267948966F, 0.0F, 0.0F);
        this.Wheel8 = new ModelRenderer(this, 8, 0);
        this.Wheel8.setRotationPoint(7.0F, 5.0F, 5.0F);
        this.Wheel8.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel6 = new ModelRenderer(this, 8, 0);
        this.Wheel6.setRotationPoint(3.0F, 5.0F, 5.0F);
        this.Wheel6.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.TruckBase2 = new ModelRenderer(this, 26, 0);
        this.TruckBase2.addBox(2.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.shape22 = new ModelRenderer(this, 0, 15);
        this.shape22.addBox(-14.0F, 2.0F, -1.0F, 28, 1, 2, 0.0F);
        this.shape30 = new ModelRenderer(this, 70, 19);
        this.shape30.setRotationPoint(0.0F, -12.0F, -8.0F);
        this.shape30.addBox(-12.0F, -6.0F, 0.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape30, -0.7853981633974483F, 0.0F, 0.0F);
        this.shape29 = new ModelRenderer(this, 64, 38);
        this.shape29.setRotationPoint(0.0F, -12.0F, 8.0F);
        this.shape29.addBox(-12.0F, -6.0F, -4.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape29, 0.7853981633974483F, 0.0F, 0.0F);
        this.shape25 = new ModelRenderer(this, 0, 38);
        this.shape25.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.shape25.addBox(0.0F, -12.0F, -8.0F, 24, 8, 16, 0.0F);
        this.Wheel3 = new ModelRenderer(this, 8, 0);
        this.Wheel3.addBox(-6.0F, 2.0F, -6.0F, 3, 3, 1, 0.0F);
        this.Wheel2 = new ModelRenderer(this, 8, 0);
        this.Wheel2.setRotationPoint(-10.0F, 5.0F, 5.0F);
        this.Wheel2.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape28 = new ModelRenderer(this, 60, 72);
        this.shape28.setRotationPoint(0.0F, -4.0F, 8.0F);
        this.shape28.addBox(-12.0F, 0.0F, -4.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape28, -0.7853981633974483F, 0.0F, 0.0F);
        this.Wheel4 = new ModelRenderer(this, 8, 0);
        this.Wheel4.setRotationPoint(-6.0F, 5.0F, 5.0F);
        this.Wheel4.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.TruckBase1 = new ModelRenderer(this, 58, 4);
        this.TruckBase1.addBox(-11.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.Base1 = new ModelRenderer(this, 0, 19);
        this.Base1.addBox(-13.0F, 0.0F, -9.0F, 26, 1, 18, 0.0F);
        this.Axis4 = new ModelRenderer(this, 62, 0);
        this.Axis4.addBox(8.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis4, 1.5707963267948966F, 0.0F, 0.0F);
        this.shape32 = new ModelRenderer(this, 8, 4);
        this.shape32.addBox(-3.0F, -19.0F, -3.0F, 6, 1, 6, 0.0F);
        this.shape26 = new ModelRenderer(this, 0, 62);
        this.shape26.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.shape26.addBox(-12.0F, -16.0F, -1.0F, 24, 4, 8, 0.0F);
        this.Wheel5 = new ModelRenderer(this, 8, 0);
        this.Wheel5.setRotationPoint(3.0F, 5.0F, -6.0F);
        this.Wheel5.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Axis3 = new ModelRenderer(this, 58, 0);
        this.Axis3.addBox(4.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis3, 1.5707963267948966F, 0.0F, 0.0F);
        this.shape31 = new ModelRenderer(this, 94, 0);
        this.shape31.addBox(-2.0F, -18.0F, -2.0F, 4, 2, 4, 0.0F);
        this.Axis1 = new ModelRenderer(this, 0, 0);
        this.Axis1.addBox(-9.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis1, 1.5707963267948966F, 0.0F, 0.0F);
        this.shape27 = new ModelRenderer(this, 64, 62);
        this.shape27.setRotationPoint(0.0F, -4.0F, -8.0F);
        this.shape27.addBox(-12.0F, 0.0F, 0.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape27, 0.7853981633974483F, 0.0F, 0.0F);
        this.Wheel7 = new ModelRenderer(this, 82, 0);
        this.Wheel7.setRotationPoint(7.0F, 5.0F, -6.0F);
        this.Wheel7.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Wheel1.render(f5);
        this.shape33.render(f5);
        this.Axis2.render(f5);
        this.Wheel8.render(f5);
        this.Wheel6.render(f5);
        this.TruckBase2.render(f5);
        this.shape22.render(f5);
        this.shape30.render(f5);
        this.shape29.render(f5);
        this.shape25.render(f5);
        this.Wheel3.render(f5);
        this.Wheel2.render(f5);
        this.shape28.render(f5);
        this.Wheel4.render(f5);
        this.TruckBase1.render(f5);
        this.Base1.render(f5);
        this.Axis4.render(f5);
        this.shape32.render(f5);
        this.shape26.render(f5);
        this.Wheel5.render(f5);
        this.Axis3.render(f5);
        this.shape31.render(f5);
        this.Axis1.render(f5);
        this.shape27.render(f5);
        this.Wheel7.render(f5);
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
