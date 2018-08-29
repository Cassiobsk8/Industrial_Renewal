package cassiokf.industrialrenewal.model.carts;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Fluid Container - CassioKF
 * Created using Tabula 7.0.0
 */
public class FluidContainerModel extends ModelBase {
    private ModelRenderer Axis1;
    private ModelRenderer Axis2;
    private ModelRenderer Wheel1;
    private ModelRenderer Wheel2;
    private ModelRenderer Wheel3;
    private ModelRenderer Wheel4;
    private ModelRenderer TruckBase1;
    private ModelRenderer Axis3;
    private ModelRenderer Axis4;
    private ModelRenderer Wheel5;
    private ModelRenderer Wheel6;
    private ModelRenderer Wheel7;
    private ModelRenderer Wheel8;
    private ModelRenderer TruckBase2;
    private ModelRenderer Base1;
    private ModelRenderer shape22;
    private ModelRenderer shape25;
    private ModelRenderer shape26;
    private ModelRenderer shape27;
    private ModelRenderer shape28;
    private ModelRenderer shape29;
    private ModelRenderer shape30;
    private ModelRenderer shape31;
    private ModelRenderer shape32;
    private ModelRenderer shape33;
    private ModelRenderer shape34;
    private ModelRenderer shape35;
    private ModelRenderer shape36;
    private ModelRenderer shape37;
    private ModelRenderer shape38;
    private ModelRenderer shape39;
    private ModelRenderer shape40;
    private ModelRenderer shape41;

    public FluidContainerModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.shape41 = new ModelRenderer(this, 7, 16);
        this.shape41.addBox(-11.0F, -4.0F, 8.0F, 22, 1, 1, 0.0F);
        this.Axis1 = new ModelRenderer(this, 0, 0);
        this.Axis1.addBox(-9.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis1, 1.5707963267948966F, 0.0F, 0.0F);
        this.TruckBase1 = new ModelRenderer(this, 58, 4);
        this.TruckBase1.addBox(-11.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.shape33 = new ModelRenderer(this, 0, 74);
        this.shape33.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.shape33.addBox(-12.0F, -4.0F, -1.0F, 24, 4, 8, 0.0F);
        this.shape40 = new ModelRenderer(this, 0, 0);
        this.shape40.addBox(11.0F, -4.0F, 8.0F, 1, 4, 1, 0.0F);
        this.shape36 = new ModelRenderer(this, 7, 16);
        this.shape36.addBox(-11.0F, -4.0F, -9.0F, 22, 1, 1, 0.0F);
        this.shape27 = new ModelRenderer(this, 64, 62);
        this.shape27.setRotationPoint(0.0F, -4.0F, -8.0F);
        this.shape27.addBox(-12.0F, 0.0F, 0.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape27, 0.7853981633974483F, 0.0F, 0.0F);
        this.Axis3 = new ModelRenderer(this, 58, 0);
        this.Axis3.addBox(4.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis3, 1.5707963267948966F, 0.0F, 0.0F);
        this.Axis2 = new ModelRenderer(this, 4, 0);
        this.Axis2.addBox(-5.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis2, 1.5707963267948966F, 0.0F, 0.0F);
        this.Wheel8 = new ModelRenderer(this, 90, 0);
        this.Wheel8.setRotationPoint(7.0F, 5.0F, 5.0F);
        this.Wheel8.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape39 = new ModelRenderer(this, 0, 0);
        this.shape39.addBox(0.0F, -3.0F, 8.0F, 1, 3, 1, 0.0F);
        this.shape30 = new ModelRenderer(this, 70, 19);
        this.shape30.setRotationPoint(0.0F, -12.0F, -8.0F);
        this.shape30.addBox(-12.0F, -6.0F, 0.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape30, -0.7853981633974483F, 0.0F, 0.0F);
        this.Wheel5 = new ModelRenderer(this, 66, 0);
        this.Wheel5.setRotationPoint(3.0F, 5.0F, -6.0F);
        this.Wheel5.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape29 = new ModelRenderer(this, 64, 38);
        this.shape29.setRotationPoint(0.0F, -12.0F, 8.0F);
        this.shape29.addBox(-12.0F, -6.0F, -4.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape29, 0.7853981633974483F, 0.0F, 0.0F);
        this.shape38 = new ModelRenderer(this, 0, 0);
        this.shape38.addBox(-12.0F, -4.0F, 8.0F, 1, 4, 1, 0.0F);
        this.shape37 = new ModelRenderer(this, 0, 0);
        this.shape37.addBox(0.0F, -3.0F, -9.0F, 1, 3, 1, 0.0F);
        this.shape26 = new ModelRenderer(this, 0, 62);
        this.shape26.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.shape26.addBox(-12.0F, -16.0F, -1.0F, 24, 4, 8, 0.0F);
        this.shape22 = new ModelRenderer(this, 0, 15);
        this.shape22.addBox(-14.0F, 2.0F, -1.0F, 28, 1, 2, 0.0F);
        this.Wheel3 = new ModelRenderer(this, 24, 0);
        this.Wheel3.addBox(-6.0F, 2.0F, -6.0F, 3, 3, 1, 0.0F);
        this.shape28 = new ModelRenderer(this, 60, 72);
        this.shape28.setRotationPoint(0.0F, -4.0F, 8.0F);
        this.shape28.addBox(-12.0F, 0.0F, -4.0F, 24, 6, 4, 0.0F);
        this.setRotateAngle(shape28, -0.7853981633974483F, 0.0F, 0.0F);
        this.Wheel1 = new ModelRenderer(this, 8, 0);
        this.Wheel1.setRotationPoint(-10.0F, 2.0F, -6.0F);
        this.Wheel1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Base1 = new ModelRenderer(this, 0, 19);
        this.Base1.addBox(-13.0F, 0.0F, -9.0F, 26, 1, 18, 0.0F);
        this.Axis4 = new ModelRenderer(this, 62, 0);
        this.Axis4.addBox(8.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis4, 1.5707963267948966F, 0.0F, 0.0F);
        this.shape34 = new ModelRenderer(this, 0, 0);
        this.shape34.addBox(-12.0F, -4.0F, -9.0F, 1, 4, 1, 0.0F);
        this.Wheel7 = new ModelRenderer(this, 82, 0);
        this.Wheel7.setRotationPoint(7.0F, 5.0F, -6.0F);
        this.Wheel7.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape32 = new ModelRenderer(this, 8, 4);
        this.shape32.addBox(-3.0F, -19.0F, -3.0F, 6, 1, 6, 0.0F);
        this.Wheel4 = new ModelRenderer(this, 32, 0);
        this.Wheel4.setRotationPoint(-6.0F, 5.0F, 5.0F);
        this.Wheel4.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape35 = new ModelRenderer(this, 0, 0);
        this.shape35.addBox(11.0F, -4.0F, -9.0F, 1, 4, 1, 0.0F);
        this.Wheel2 = new ModelRenderer(this, 16, 0);
        this.Wheel2.setRotationPoint(-10.0F, 5.0F, 5.0F);
        this.Wheel2.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape25 = new ModelRenderer(this, 0, 38);
        this.shape25.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.shape25.addBox(0.0F, -12.0F, -8.0F, 24, 8, 16, 0.0F);
        this.TruckBase2 = new ModelRenderer(this, 26, 0);
        this.TruckBase2.addBox(2.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.Wheel6 = new ModelRenderer(this, 74, 0);
        this.Wheel6.setRotationPoint(3.0F, 5.0F, 5.0F);
        this.Wheel6.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape31 = new ModelRenderer(this, 94, 0);
        this.shape31.addBox(-2.0F, -18.0F, -2.0F, 4, 2, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.shape41.render(f5);
        this.Axis1.render(f5);
        this.TruckBase1.render(f5);
        this.shape33.render(f5);
        this.shape40.render(f5);
        this.shape36.render(f5);
        this.shape27.render(f5);
        this.Axis3.render(f5);
        this.Axis2.render(f5);
        this.Wheel8.render(f5);
        this.shape39.render(f5);
        this.shape30.render(f5);
        this.Wheel5.render(f5);
        this.shape29.render(f5);
        this.shape38.render(f5);
        this.shape37.render(f5);
        this.shape26.render(f5);
        this.shape22.render(f5);
        this.Wheel3.render(f5);
        this.shape28.render(f5);
        this.Wheel1.render(f5);
        this.Base1.render(f5);
        this.Axis4.render(f5);
        this.shape34.render(f5);
        this.Wheel7.render(f5);
        this.shape32.render(f5);
        this.Wheel4.render(f5);
        this.shape35.render(f5);
        this.Wheel2.render(f5);
        this.shape25.render(f5);
        this.TruckBase2.render(f5);
        this.Wheel6.render(f5);
        this.shape31.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
