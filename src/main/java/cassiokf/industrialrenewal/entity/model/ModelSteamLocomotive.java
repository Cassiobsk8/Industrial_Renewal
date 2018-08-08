package cassiokf.industrialrenewal.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Cargo Container - CassioKF
 * Created using Tabula 7.0.0
 */
public class ModelSteamLocomotive extends ModelBase {
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
    public ModelRenderer Container1;
    public ModelRenderer Cabin;
    public ModelRenderer Chamney1;
    public ModelRenderer Chamney2;
    public ModelRenderer Rail1;
    public ModelRenderer Rail2;
    public ModelRenderer Rail3;
    public ModelRenderer Rail4;
    public ModelRenderer Container2;
    public ModelRenderer Base1;
    public ModelRenderer shape22;
    public ModelRenderer Grid;
    public ModelRenderer Light;

    public ModelSteamLocomotive() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Wheel4 = new ModelRenderer(this, 8, 0);
        this.Wheel4.setRotationPoint(-6.0F, 5.0F, 5.0F);
        this.Wheel4.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Base1 = new ModelRenderer(this, 0, 55);
        this.Base1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Base1.addBox(-13.0F, 0.0F, -9.0F, 26, 1, 18, 0.0F);
        this.Rail4 = new ModelRenderer(this, 0, 22);
        this.Rail4.setRotationPoint(11.0F, -7.0F, 8.0F);
        this.Rail4.addBox(0.0F, 0.0F, 0.0F, 1, 7, 1, 0.0F);
        this.Axis3 = new ModelRenderer(this, 58, 0);
        this.Axis3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis3.addBox(4.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis3, 1.5707963267948966F, 0.0F, 0.0F);
        this.Cabin = new ModelRenderer(this, 71, 23);
        this.Cabin.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Cabin.addBox(-13.0F, -18.0F, -9.0F, 8, 18, 18, 0.0F);
        this.Wheel7 = new ModelRenderer(this, 8, 0);
        this.Wheel7.setRotationPoint(7.0F, 5.0F, -6.0F);
        this.Wheel7.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel8 = new ModelRenderer(this, 8, 0);
        this.Wheel8.setRotationPoint(7.0F, 5.0F, 5.0F);
        this.Wheel8.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.TruckBase2 = new ModelRenderer(this, 26, 0);
        this.TruckBase2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TruckBase2.addBox(2.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.Wheel2 = new ModelRenderer(this, 8, 0);
        this.Wheel2.setRotationPoint(-10.0F, 5.0F, 5.0F);
        this.Wheel2.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Axis4 = new ModelRenderer(this, 62, 0);
        this.Axis4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis4.addBox(8.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis4, 1.5707963267948966F, 0.0F, 0.0F);
        this.Wheel5 = new ModelRenderer(this, 8, 0);
        this.Wheel5.setRotationPoint(3.0F, 5.0F, -6.0F);
        this.Wheel5.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Light = new ModelRenderer(this, 0, 53);
        this.Light.setRotationPoint(10.0F, -17.8F, -1.5F);
        this.Light.addBox(0.0F, 0.0F, 0.0F, 4, 3, 3, 0.0F);
        this.Rail1 = new ModelRenderer(this, 0, 50);
        this.Rail1.setRotationPoint(-5.0F, -7.0F, -9.0F);
        this.Rail1.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
        this.Grid = new ModelRenderer(this, 39, 75);
        this.Grid.setRotationPoint(13.0F, 0.0F, 0.0F);
        this.Grid.addBox(0.0F, 0.0F, -8.0F, 1, 5, 16, 0.0F);
        this.setRotateAngle(Grid, 0.0F, 0.0F, -0.4553564018453205F);
        this.Wheel1 = new ModelRenderer(this, 8, 0);
        this.Wheel1.setRotationPoint(-10.0F, 2.0F, -6.0F);
        this.Wheel1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel6 = new ModelRenderer(this, 8, 0);
        this.Wheel6.setRotationPoint(3.0F, 5.0F, 5.0F);
        this.Wheel6.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel3 = new ModelRenderer(this, 8, 0);
        this.Wheel3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Wheel3.addBox(-6.0F, 2.0F, -6.0F, 3, 3, 1, 0.0F);
        this.shape22 = new ModelRenderer(this, 56, 19);
        this.shape22.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape22.addBox(-14.0F, 2.0F, -1.0F, 28, 1, 2, 0.0F);
        this.Container1 = new ModelRenderer(this, 0, 16);
        this.Container1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Container1.addBox(-6.5F, -16.0F, -12.0F, 13, 16, 17, 0.0F);
        this.setRotateAngle(Container1, 0.005235987755982988F, 4.71238898038469F, 0.0F);
        this.Rail2 = new ModelRenderer(this, 0, 50);
        this.Rail2.setRotationPoint(-5.0F, -7.0F, 8.0F);
        this.Rail2.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
        this.Rail3 = new ModelRenderer(this, 0, 22);
        this.Rail3.setRotationPoint(11.0F, -7.0F, -9.0F);
        this.Rail3.addBox(0.0F, 0.0F, 0.0F, 1, 7, 1, 0.0F);
        this.Axis2 = new ModelRenderer(this, 4, 0);
        this.Axis2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis2.addBox(-5.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis2, 1.5707963267948966F, 0.0F, 0.0F);
        this.Container2 = new ModelRenderer(this, 0, 75);
        this.Container2.setRotationPoint(12.0F, -14.0F, -5.0F);
        this.Container2.addBox(0.0F, 0.0F, 0.0F, 1, 9, 10, 0.0F);
        this.Axis1 = new ModelRenderer(this, 0, 0);
        this.Axis1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Axis1.addBox(-9.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis1, 1.5707963267948966F, 0.0F, 0.0F);
        this.TruckBase1 = new ModelRenderer(this, 58, 4);
        this.TruckBase1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TruckBase1.addBox(-11.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.Chamney2 = new ModelRenderer(this, 0, 12);
        this.Chamney2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Chamney2.addBox(6.0F, -21.0F, -1.5F, 3, 5, 3, 0.0F);
        this.Chamney1 = new ModelRenderer(this, 0, 12);
        this.Chamney1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Chamney1.addBox(0.0F, -18.0F, -1.5F, 3, 2, 3, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.Wheel4.render(f5);
        this.Base1.render(f5);
        this.Rail4.render(f5);
        this.Axis3.render(f5);
        this.Cabin.render(f5);
        this.Wheel7.render(f5);
        this.Wheel8.render(f5);
        this.TruckBase2.render(f5);
        this.Wheel2.render(f5);
        this.Axis4.render(f5);
        this.Wheel5.render(f5);
        this.Light.render(f5);
        this.Rail1.render(f5);
        this.Grid.render(f5);
        this.Wheel1.render(f5);
        this.Wheel6.render(f5);
        this.Wheel3.render(f5);
        this.shape22.render(f5);
        this.Container1.render(f5);
        this.Rail2.render(f5);
        this.Rail3.render(f5);
        this.Axis2.render(f5);
        this.Container2.render(f5);
        this.Axis1.render(f5);
        this.TruckBase1.render(f5);
        this.Chamney2.render(f5);
        this.Chamney1.render(f5);
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
