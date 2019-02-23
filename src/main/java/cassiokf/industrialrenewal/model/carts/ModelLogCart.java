package cassiokf.industrialrenewal.model.carts;

import cassiokf.industrialrenewal.entity.EntityLogCart;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelLogCart - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelLogCart extends ModelBase {
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
    public ModelRenderer shape23;
    public ModelRenderer shape22;
    public ModelRenderer Axis1;
    public ModelRenderer Axis2;
    public ModelRenderer Axis3;
    public ModelRenderer Axis4;
    public ModelRenderer shape23_7;
    public ModelRenderer shape23_8;
    public ModelRenderer shape23_5;
    public ModelRenderer shape23_6;
    public ModelRenderer shape18;
    public ModelRenderer TruckBase2;
    public ModelRenderer TruckBase1;
    public ModelRenderer shape23_3;
    public ModelRenderer shape23_4;
    public ModelRenderer shape23_1;
    public ModelRenderer Base1;
    public ModelRenderer shape23_2;

    public ModelLogCart() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Axis3 = new ModelRenderer(this, 58, 0);
        this.Axis3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis3.addBox(4.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis3, 1.5707963705062866F, 0.0F, 0.0F);
        this.Wheel5 = new ModelRenderer(this, 8, 0);
        this.Wheel5.setRotationPoint(3.0F, 5.0F, -6.0F);
        this.Wheel5.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel1 = new ModelRenderer(this, 8, 0);
        this.Wheel1.setRotationPoint(-10.0F, 2.0F, -6.0F);
        this.Wheel1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape23_8 = new ModelRenderer(this, 0, 25);
        this.shape23_8.setRotationPoint(-12.5F, -12.0F, -7.0F);
        this.shape23_8.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.TruckBase2 = new ModelRenderer(this, 26, 0);
        this.TruckBase2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TruckBase2.addBox(2.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.shape23_4 = new ModelRenderer(this, 0, 25);
        this.shape23_4.setRotationPoint(-13.0F, -8.0F, -2.0F);
        this.shape23_4.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.Base1 = new ModelRenderer(this, 0, 55);
        this.Base1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Base1.addBox(-13.0F, 0.0F, -9.0F, 26, 1, 18, 0.0F);
        this.shape18 = new ModelRenderer(this, 0, 12);
        this.shape18.setRotationPoint(-10.0F, -11.0F, -8.0F);
        this.shape18.addBox(0.0F, 0.0F, 0.0F, 1, 11, 1, 0.0F);
        this.shape23_6 = new ModelRenderer(this, 0, 25);
        this.shape23_6.setRotationPoint(-12.5F, -12.0F, 3.0F);
        this.shape23_6.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.shape23 = new ModelRenderer(this, 0, 25);
        this.shape23.setRotationPoint(-12.5F, -4.0F, 3.0F);
        this.shape23.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.Wheel4 = new ModelRenderer(this, 8, 0);
        this.Wheel4.setRotationPoint(-6.0F, 5.0F, 5.0F);
        this.Wheel4.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape22 = new ModelRenderer(this, 56, 19);
        this.shape22.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape22.addBox(-14.0F, 2.0F, -1.0F, 28, 1, 2, 0.0F);
        this.shape23_3 = new ModelRenderer(this, 0, 25);
        this.shape23_3.setRotationPoint(-12.5F, -8.0F, 2.5999999046325684F);
        this.shape23_3.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.Wheel3 = new ModelRenderer(this, 8, 0);
        this.Wheel3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Wheel3.addBox(-6.0F, 2.0F, -6.0F, 3, 3, 1, 0.0F);
        this.shape18_3 = new ModelRenderer(this, 0, 12);
        this.shape18_3.setRotationPoint(9.0F, -11.0F, 7.0F);
        this.shape18_3.addBox(0.0F, 0.0F, 0.0F, 1, 11, 1, 0.0F);
        this.shape23_2 = new ModelRenderer(this, 0, 25);
        this.shape23_2.setRotationPoint(-12.5F, -4.0F, -7.0F);
        this.shape23_2.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.Wheel2 = new ModelRenderer(this, 8, 0);
        this.Wheel2.setRotationPoint(-10.0F, 5.0F, 5.0F);
        this.Wheel2.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape18_2 = new ModelRenderer(this, 0, 12);
        this.shape18_2.setRotationPoint(-10.0F, -11.0F, 7.0F);
        this.shape18_2.addBox(0.0F, 0.0F, 0.0F, 1, 11, 1, 0.0F);
        this.Wheel6 = new ModelRenderer(this, 8, 0);
        this.Wheel6.setRotationPoint(3.0F, 5.0F, 5.0F);
        this.Wheel6.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Axis1 = new ModelRenderer(this, 0, 0);
        this.Axis1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis1.addBox(-9.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis1, 1.5707963705062866F, 0.0F, 0.0F);
        this.shape23_1 = new ModelRenderer(this, 0, 25);
        this.shape23_1.setRotationPoint(-12.5F, -4.0F, -1.100000023841858F);
        this.shape23_1.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.setRotateAngle(shape23_1, 0.0F, 0.06981316953897476F, 0.0F);
        this.Wheel7 = new ModelRenderer(this, 8, 0);
        this.Wheel7.setRotationPoint(7.0F, 5.0F, -6.0F);
        this.Wheel7.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape18_1 = new ModelRenderer(this, 0, 12);
        this.shape18_1.setRotationPoint(9.0F, -11.0F, -8.0F);
        this.shape18_1.addBox(0.0F, 0.0F, 0.0F, 1, 11, 1, 0.0F);
        this.shape23_7 = new ModelRenderer(this, 0, 25);
        this.shape23_7.setRotationPoint(-13.0F, -12.0F, -2.0F);
        this.shape23_7.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.setRotateAngle(shape23_7, 0.0F, -0.04014257341623306F, 0.0F);
        this.shape23_5 = new ModelRenderer(this, 0, 25);
        this.shape23_5.setRotationPoint(-12.5F, -8.0F, -6.599999904632568F);
        this.shape23_5.addBox(0.0F, 0.0F, 0.0F, 25, 4, 4, 0.0F);
        this.Wheel8 = new ModelRenderer(this, 8, 0);
        this.Wheel8.setRotationPoint(7.0F, 5.0F, 5.0F);
        this.Wheel8.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Axis2 = new ModelRenderer(this, 4, 0);
        this.Axis2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis2.addBox(-5.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis2, 1.5707963705062866F, 0.0F, 0.0F);
        this.Axis4 = new ModelRenderer(this, 62, 0);
        this.Axis4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis4.addBox(8.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis4, 1.5707963705062866F, 0.0F, 0.0F);
        this.TruckBase1 = new ModelRenderer(this, 58, 4);
        this.TruckBase1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TruckBase1.addBox(-11.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.Wheel8.render(f5);
        this.Axis1.render(f5);
        this.TruckBase1.render(f5);
        this.shape18_3.render(f5);
        this.shape22.render(f5);
        this.shape18_1.render(f5);
        this.Axis4.render(f5);
        this.shape18_2.render(f5);
        this.shape18.render(f5);
        this.Wheel3.render(f5);
        this.Wheel4.render(f5);
        this.Base1.render(f5);
        this.Wheel5.render(f5);
        this.Axis2.render(f5);
        this.TruckBase2.render(f5);
        this.Wheel1.render(f5);
        this.Wheel2.render(f5);
        this.Axis3.render(f5);
        this.Wheel7.render(f5);
        this.Wheel6.render(f5);

        ((EntityLogCart) entity).GetInvNumber();
        int items = ((EntityLogCart) entity).invItensCount;
        //Logs
        if (items > 0) this.shape23.render(f5);
        if (items > 1) this.shape23_1.render(f5);
        if (items > 2) this.shape23_2.render(f5);
        if (items > 3) this.shape23_3.render(f5);
        if (items > 4) this.shape23_4.render(f5);
        if (items > 5) this.shape23_5.render(f5);
        if (items > 6) this.shape23_6.render(f5);
        if (items > 7) this.shape23_7.render(f5);
        if (items > 8) this.shape23_8.render(f5);
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
