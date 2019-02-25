package cassiokf.industrialrenewal.model.carts;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelPassengerCar - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelPassengerCar extends ModelBase {
    public ModelRenderer Wheel7;
    public ModelRenderer shape18_2;
    public ModelRenderer Wheel8;
    public ModelRenderer shape18_1;
    public ModelRenderer Wheel5;
    public ModelRenderer Wheel6;
    public ModelRenderer shape18_3;
    public ModelRenderer Wheel3;
    public ModelRenderer Wheel4;
    public ModelRenderer Wheel1;
    public ModelRenderer Wheel2;
    public ModelRenderer shape25;
    public ModelRenderer Shaft;
    public ModelRenderer Axis1;
    public ModelRenderer Axis2;
    public ModelRenderer Axis3;
    public ModelRenderer Axis4;
    public ModelRenderer shape18;
    public ModelRenderer TruckBase2;
    public ModelRenderer shape22_1;
    public ModelRenderer TruckBase1;
    public ModelRenderer shape25_1;
    public ModelRenderer shape22_2;
    public ModelRenderer Base1;
    public ModelRenderer shape22_3;
    public ModelRenderer shape26;
    public ModelRenderer shape26_1;
    public ModelRenderer shape26_2;
    public ModelRenderer shape30;
    public ModelRenderer shape31;
    public ModelRenderer shape31_1;
    public ModelRenderer shape31_2;
    public ModelRenderer shape34;
    public ModelRenderer shape35;
    public ModelRenderer shape35_1;
    public ModelRenderer shape35_2;

    public ModelPassengerCar() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Wheel6 = new ModelRenderer(this, 8, 0);
        this.Wheel6.setRotationPoint(3.0F, 5.0F, 5.0F);
        this.Wheel6.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel5 = new ModelRenderer(this, 8, 0);
        this.Wheel5.setRotationPoint(3.0F, 5.0F, -6.0F);
        this.Wheel5.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape31_2 = new ModelRenderer(this, 26, 72);
        this.shape31_2.setRotationPoint(12.0F, -6.0F, 3.0F);
        this.shape31_2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.Axis4 = new ModelRenderer(this, 62, 0);
        this.Axis4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis4.addBox(8.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis4, 1.5707963705062866F, 0.0F, 0.0F);
        this.shape18_1 = new ModelRenderer(this, 0, 76);
        this.shape18_1.setRotationPoint(-13.0F, -20.0F, 6.0F);
        this.shape18_1.addBox(0.0F, 0.0F, 0.0F, 1, 11, 3, 0.0F);
        this.shape30 = new ModelRenderer(this, 24, 72);
        this.shape30.setRotationPoint(12.0F, -7.0F, -6.0F);
        this.shape30.addBox(0.0F, 0.0F, 0.0F, 1, 1, 12, 0.0F);
        this.shape26 = new ModelRenderer(this, 0, 43);
        this.shape26.setRotationPoint(-15.0F, -22.0F, -2.0F);
        this.shape26.addBox(0.0F, 0.0F, 0.0F, 30, 2, 4, 0.0F);
        this.Axis1 = new ModelRenderer(this, 0, 0);
        this.Axis1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis1.addBox(-9.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis1, 1.5707963705062866F, 0.0F, 0.0F);
        this.TruckBase1 = new ModelRenderer(this, 58, 4);
        this.TruckBase1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TruckBase1.addBox(-11.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.Wheel1 = new ModelRenderer(this, 8, 0);
        this.Wheel1.setRotationPoint(-10.0F, 2.0F, -6.0F);
        this.Wheel1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel7 = new ModelRenderer(this, 8, 0);
        this.Wheel7.setRotationPoint(7.0F, 5.0F, -6.0F);
        this.Wheel7.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape26_2 = new ModelRenderer(this, 0, 61);
        this.shape26_2.setRotationPoint(-15.0F, -21.6F, -2.0F);
        this.shape26_2.addBox(0.0F, 0.0F, -9.0F, 30, 1, 9, 0.0F);
        this.setRotateAngle(shape26_2, 0.20943951023931953F, 0.0F, 0.0F);
        this.shape26_1 = new ModelRenderer(this, 0, 50);
        this.shape26_1.setRotationPoint(-15.0F, -21.6F, 2.0F);
        this.shape26_1.addBox(0.0F, 0.0F, 0.0F, 30, 1, 9, 0.0F);
        this.setRotateAngle(shape26_1, -0.20943951023931953F, 0.0F, 0.0F);
        this.Axis2 = new ModelRenderer(this, 4, 0);
        this.Axis2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis2.addBox(-5.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis2, 1.5707963705062866F, 0.0F, 0.0F);
        this.shape22_1 = new ModelRenderer(this, 9, 75);
        this.shape22_1.setRotationPoint(7.0F, -20.0F, -8.9F);
        this.shape22_1.addBox(0.0F, 0.0F, 0.0F, 1, 20, 6, 0.0F);
        this.shape35_1 = new ModelRenderer(this, 70, 62);
        this.shape35_1.setRotationPoint(3.0F, -28.0F, 0.0F);
        this.shape35_1.addBox(0.0F, 0.0F, 0.0F, 10, 1, 5, 0.0F);
        this.setRotateAngle(shape35_1, -0.24434609527920614F, 0.0F, 0.0F);
        this.shape18_3 = new ModelRenderer(this, 0, 76);
        this.shape18_3.setRotationPoint(12.0F, -20.0F, -9.0F);
        this.shape18_3.addBox(0.0F, 0.0F, 0.0F, 1, 20, 3, 0.0F);
        this.Shaft = new ModelRenderer(this, 56, 19);
        this.Shaft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Shaft.addBox(-14.0F, 2.0F, -1.0F, 28, 1, 2, 0.0F);
        this.TruckBase2 = new ModelRenderer(this, 26, 0);
        this.TruckBase2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TruckBase2.addBox(2.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.shape22_2 = new ModelRenderer(this, 29, 98);
        this.shape22_2.setRotationPoint(-13.0F, -9.0F, -8.0F);
        this.shape22_2.addBox(0.0F, 0.0F, 0.0F, 1, 9, 16, 0.0F);
        this.Wheel4 = new ModelRenderer(this, 8, 0);
        this.Wheel4.setRotationPoint(-6.0F, 5.0F, 5.0F);
        this.Wheel4.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape18 = new ModelRenderer(this, 0, 76);
        this.shape18.setRotationPoint(-13.0F, -20.0F, -9.0F);
        this.shape18.addBox(0.0F, 0.0F, 0.0F, 1, 11, 3, 0.0F);
        this.Base1 = new ModelRenderer(this, 0, 23);
        this.Base1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Base1.addBox(-13.0F, 0.0F, -9.0F, 26, 1, 18, 0.0F);
        this.shape31 = new ModelRenderer(this, 26, 72);
        this.shape31.setRotationPoint(12.0F, -6.0F, -4.0F);
        this.shape31.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.Wheel3 = new ModelRenderer(this, 8, 0);
        this.Wheel3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Wheel3.addBox(-6.0F, 2.0F, -6.0F, 3, 3, 1, 0.0F);
        this.shape18_2 = new ModelRenderer(this, 0, 76);
        this.shape18_2.setRotationPoint(12.0F, -20.0F, 6.0F);
        this.shape18_2.addBox(0.0F, 0.0F, 0.0F, 1, 20, 3, 0.0F);
        this.shape25 = new ModelRenderer(this, 0, 102);
        this.shape25.setRotationPoint(-13.0F, -9.0F, -9.0F);
        this.shape25.addBox(0.0F, 0.0F, 0.0F, 21, 9, 1, 0.0F);
        this.shape25_1 = new ModelRenderer(this, 48, 102);
        this.shape25_1.setRotationPoint(-13.0F, -9.0F, 8.0F);
        this.shape25_1.addBox(0.0F, 0.0F, 0.0F, 21, 9, 1, 0.0F);
        this.Axis3 = new ModelRenderer(this, 58, 0);
        this.Axis3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis3.addBox(4.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis3, 1.5707963705062866F, 0.0F, 0.0F);
        this.shape35_2 = new ModelRenderer(this, 97, 64);
        this.shape35_2.setRotationPoint(3.0F, -28.0F, 0.0F);
        this.shape35_2.addBox(0.0F, 0.0F, -5.0F, 10, 1, 5, 0.0F);
        this.setRotateAngle(shape35_2, 0.24434609527920614F, 0.0F, 0.0F);
        this.shape35 = new ModelRenderer(this, 51, 72);
        this.shape35.setRotationPoint(4.0F, -27.0F, -3.5F);
        this.shape35.addBox(0.0F, 0.0F, 0.0F, 8, 6, 7, 0.0F);
        this.Wheel8 = new ModelRenderer(this, 8, 0);
        this.Wheel8.setRotationPoint(7.0F, 5.0F, 5.0F);
        this.Wheel8.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel2 = new ModelRenderer(this, 8, 0);
        this.Wheel2.setRotationPoint(-10.0F, 5.0F, 5.0F);
        this.Wheel2.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape34 = new ModelRenderer(this, 79, 44);
        this.shape34.setRotationPoint(-6.0F, -2.0F, -6.0F);
        this.shape34.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.shape31_1 = new ModelRenderer(this, 26, 72);
        this.shape31_1.setRotationPoint(12.0F, -6.0F, -0.5F);
        this.shape31_1.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.shape22_3 = new ModelRenderer(this, 9, 75);
        this.shape22_3.setRotationPoint(7.0F, -20.0F, 2.9F);
        this.shape22_3.addBox(0.0F, 0.0F, 0.0F, 1, 20, 6, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.Wheel6.render(f5);
        this.Wheel5.render(f5);
        this.shape31_2.render(f5);
        this.Axis4.render(f5);
        this.shape18_1.render(f5);
        this.shape30.render(f5);
        this.shape26.render(f5);
        this.Axis1.render(f5);
        this.TruckBase1.render(f5);
        this.Wheel1.render(f5);
        this.Wheel7.render(f5);
        this.shape26_2.render(f5);
        this.shape26_1.render(f5);
        this.Axis2.render(f5);
        this.shape22_1.render(f5);
        this.shape35_1.render(f5);
        this.shape18_3.render(f5);
        this.Shaft.render(f5);
        this.TruckBase2.render(f5);
        this.shape22_2.render(f5);
        this.Wheel4.render(f5);
        this.shape18.render(f5);
        this.Base1.render(f5);
        this.shape31.render(f5);
        this.Wheel3.render(f5);
        this.shape18_2.render(f5);
        this.shape25.render(f5);
        this.shape25_1.render(f5);
        this.Axis3.render(f5);
        this.shape35_2.render(f5);
        this.shape35.render(f5);
        this.Wheel8.render(f5);
        this.Wheel2.render(f5);
        this.shape34.render(f5);
        this.shape31_1.render(f5);
        this.shape22_3.render(f5);
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
