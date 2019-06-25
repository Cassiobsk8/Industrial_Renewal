package cassiokf.industrialrenewal.model.carts;//Made with Blockbench
//Paste this code into your mod.

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCartFluidTank extends ModelBase
{
    private final ModelRenderer Weels;
    private final ModelRenderer Shafts;
    private final ModelRenderer Truck;
    private final ModelRenderer Tank;
    private final ModelRenderer TankCorner;
    private final ModelRenderer Gauges;
    private final ModelRenderer GaugeCorners;
    private final ModelRenderer bone;
    private final ModelRenderer Gauges2;
    private final ModelRenderer GaugeCorners2;
    private final ModelRenderer bone2;

    public ModelCartFluidTank()
    {
        textureWidth = 128;
        textureHeight = 128;

        Weels = new ModelRenderer(this);
        Weels.setRotationPoint(0.0F, 24.0F, 0.0F);
        Weels.cubeList.add(new ModelBox(Weels, 8, 0, 7.0F, -22.0F, 5.0F, 3, 3, 1, 0.0F, false));
        Weels.cubeList.add(new ModelBox(Weels, 16, 0, 3.0F, -22.0F, 5.0F, 3, 3, 1, 0.0F, false));
        Weels.cubeList.add(new ModelBox(Weels, 24, 0, -6.0F, -22.0F, 5.0F, 3, 3, 1, 0.0F, false));
        Weels.cubeList.add(new ModelBox(Weels, 32, 0, -10.0F, -22.0F, 5.0F, 3, 3, 1, 0.0F, false));
        Weels.cubeList.add(new ModelBox(Weels, 66, 0, 7.0F, -22.0F, -6.0F, 3, 3, 1, 0.0F, false));
        Weels.cubeList.add(new ModelBox(Weels, 74, 0, 3.0F, -22.0F, -6.0F, 3, 3, 1, 0.0F, false));
        Weels.cubeList.add(new ModelBox(Weels, 82, 0, -6.0F, -22.0F, -6.0F, 3, 3, 1, 0.0F, false));
        Weels.cubeList.add(new ModelBox(Weels, 90, 0, -10.0F, -22.0F, -6.0F, 3, 3, 1, 0.0F, false));

        Shafts = new ModelRenderer(this);
        Shafts.setRotationPoint(0.0F, 24.0F, 0.0F);
        Shafts.cubeList.add(new ModelBox(Shafts, 32, 27, 8.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 32, 27, 4.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 32, 27, -5.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 32, 27, -9.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 0, 15, -14.0F, -22.0F, -1.0F, 28, 1, 2, 0.0F, false));

        Truck = new ModelRenderer(this);
        Truck.setRotationPoint(0.0F, 24.0F, 0.0F);
        Truck.cubeList.add(new ModelBox(Truck, 26, 0, 2.0F, -23.0F, -7.0F, 9, 1, 14, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 58, 4, -11.0F, -23.0F, -7.0F, 9, 1, 14, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 19, -13.0F, -24.0F, -9.0F, 26, 1, 18, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, -10.5F, -28.0F, -9.0F, 10, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, 0.5F, -28.0F, -9.0F, 10, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -0.5F, -28.0F, -9.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -11.5F, -28.0F, -9.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 10.5F, -28.0F, -9.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 5.0F, -42.0F, 4.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 5.0F, -42.0F, -5.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -6.0F, -42.0F, 4.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -6.0F, -42.0F, -5.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 19, 5.0F, -42.0F, -4.0F, 1, 1, 8, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 19, -6.0F, -42.0F, -4.0F, 1, 1, 8, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 2.0F, -42.0F, 4.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -3.0F, -42.0F, 4.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -3.0F, -42.0F, -5.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 2.0F, -42.0F, -5.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, 3.0F, -42.0F, 4.0F, 2, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, -5.0F, -42.0F, 4.0F, 2, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, -5.0F, -42.0F, -5.0F, 2, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, 3.0F, -42.0F, -5.0F, 2, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -13.0F, -31.0F, -7.0F, 1, 7, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -13.0F, -31.0F, 6.0F, 1, 7, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 92, 23, -13.0F, -31.0F, -6.0F, 1, 1, 12, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 92, 23, 12.0F, -31.0F, -6.0F, 1, 1, 12, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 12.0F, -31.0F, -7.0F, 1, 7, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 12.0F, -31.0F, 6.0F, 1, 7, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 92, 23, 12.0F, -28.0F, -6.0F, 1, 1, 12, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 92, 23, -13.0F, -28.0F, -6.0F, 1, 1, 12, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, 10.5F, -28.0F, 8.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, 0.5F, -28.0F, 8.0F, 10, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -0.5F, -28.0F, 8.0F, 1, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 16, 16, -10.5F, -28.0F, 8.0F, 10, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -11.5F, -28.0F, 8.0F, 1, 4, 1, 0.0F, false));

        Tank = new ModelRenderer(this);
        Tank.setRotationPoint(0.0F, -8.0F, 0.0F);
        Tank.cubeList.add(new ModelBox(Tank, 0, 40, -12.0F, -8.0F, -4.0F, 24, 16, 8, 0.0F, false));
        Tank.cubeList.add(new ModelBox(Tank, 0, 65, -12.0F, -4.0F, -8.0F, 24, 8, 16, 0.0F, false));
        Tank.cubeList.add(new ModelBox(Tank, 76, 39, -2.0F, -10.0F, -2.0F, 4, 2, 4, 0.0F, false));
        Tank.cubeList.add(new ModelBox(Tank, 8, 4, -3.0F, -11.0F, -3.0F, 6, 1, 6, 0.0F, false));

        TankCorner = new ModelRenderer(this);
        TankCorner.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(TankCorner, -0.7854F, 0.0F, 0.0F);
        Tank.addChild(TankCorner);
        TankCorner.cubeList.add(new ModelBox(TankCorner, 72, 40, -12.0F, -3.15F, -8.47F, 24, 6, 3, 0.0F, false));
        TankCorner.cubeList.add(new ModelBox(TankCorner, 68, 49, -12.0F, -8.47F, -3.16F, 24, 3, 6, 0.0F, false));
        TankCorner.cubeList.add(new ModelBox(TankCorner, 72, 58, -12.0F, -2.9F, 5.5F, 24, 6, 3, 0.0F, false));
        TankCorner.cubeList.add(new ModelBox(TankCorner, 72, 68, -12.0F, 5.5F, -2.9F, 24, 3, 6, 0.0F, false));

        Gauges = new ModelRenderer(this);
        Gauges.setRotationPoint(1.0F, 30.0F, 0.5F);
        Gauges.cubeList.add(new ModelBox(Gauges, 0, 51, -2.0F, -41.0F, -8.9F, 2, 4, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 14, 42, -3.0F, -40.0F, -8.9F, 4, 2, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 24, 21, -2.44F, -42.02F, -9.2F, 3, 1, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 24, 21, -2.42F, -37.19F, -9.2F, 3, 1, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 24, 21, 1.0F, -40.6F, -9.2F, 1, 3, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 24, 21, -3.9F, -40.6F, -9.2F, 1, 3, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 90, 7, -6.0F, -36.2F, -9.0F, 10, 2, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 118, 6, -2.1F, -41.3F, -9.1F, 2, 1, 1, 0.0F, false));
        Gauges.cubeList.add(new ModelBox(Gauges, 124, 11, 0.26F, -39.9F, -9.1F, 1, 1, 1, 0.0F, true));
        Gauges.cubeList.add(new ModelBox(Gauges, 117, 0, -3.14F, -39.9F, -9.1F, 1, 1, 1, 0.0F, true));

        GaugeCorners = new ModelRenderer(this);
        GaugeCorners.setRotationPoint(-1.0F, -39.0F, -8.5F);
        setRotationAngle(GaugeCorners, 0.0F, 0.0F, -0.7854F);
        Gauges.addChild(GaugeCorners);
        GaugeCorners.cubeList.add(new ModelBox(GaugeCorners, 14, 42, -2.65F, -1.05F, -0.4F, 5, 2, 1, 0.0F, false));
        GaugeCorners.cubeList.add(new ModelBox(GaugeCorners, 14, 42, -1.05F, -2.65F, -0.4F, 2, 5, 1, 0.0F, false));
        GaugeCorners.cubeList.add(new ModelBox(GaugeCorners, 24, 21, 2.28F, -1.02F, -0.7F, 1, 2, 1, 0.0F, false));
        GaugeCorners.cubeList.add(new ModelBox(GaugeCorners, 24, 21, -3.05F, -1.05F, -0.7F, 1, 2, 1, 0.0F, false));
        GaugeCorners.cubeList.add(new ModelBox(GaugeCorners, 106, 16, -1.0F, -2.43F, -0.6F, 2, 1, 1, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(-1.025F, -39.0F, -8.5F);
        setRotationAngle(bone, 0.0F, 0.0F, 0.7854F);
        Gauges.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 24, 21, -3.155F, -1.1F, -0.7F, 1, 2, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 24, 21, 2.125F, -1.2F, -0.7F, 1, 2, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 105, 19, -1.275F, -2.5F, -0.6F, 3, 1, 1, 0.0F, true));

        Gauges2 = new ModelRenderer(this);
        Gauges2.setRotationPoint(1.0F, 30.0F, -0.5F);
        Gauges2.cubeList.add(new ModelBox(Gauges2, 0, 51, -2.0F, -41.0F, 7.9F, 2, 4, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 14, 42, -3.0F, -40.0F, 7.9F, 4, 2, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 24, 21, -2.44F, -42.02F, 8.2F, 3, 1, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 24, 21, -2.42F, -37.19F, 8.2F, 3, 1, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 24, 21, 1.0F, -40.6F, 8.2F, 1, 3, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 24, 21, -3.9F, -40.6F, 8.2F, 1, 3, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 90, 12, -6.0F, -36.2F, 8.0F, 10, 2, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 118, 6, -2.1F, -41.3F, 8.1F, 2, 1, 1, 0.0F, false));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 124, 11, -3.14F, -39.9F, 8.1F, 1, 1, 1, 0.0F, true));
        Gauges2.cubeList.add(new ModelBox(Gauges2, 117, 0, 0.26F, -39.9F, 8.1F, 1, 1, 1, 0.0F, true));

        GaugeCorners2 = new ModelRenderer(this);
        GaugeCorners2.setRotationPoint(-1.0F, -39.0F, 8.5F);
        setRotationAngle(GaugeCorners2, 0.0F, 0.0F, -0.7854F);
        Gauges2.addChild(GaugeCorners2);
        GaugeCorners2.cubeList.add(new ModelBox(GaugeCorners2, 14, 42, -2.65F, -1.05F, -0.6F, 5, 2, 1, 0.0F, false));
        GaugeCorners2.cubeList.add(new ModelBox(GaugeCorners2, 14, 42, -1.05F, -2.65F, -0.6F, 2, 5, 1, 0.0F, false));
        GaugeCorners2.cubeList.add(new ModelBox(GaugeCorners2, 24, 21, 2.28F, -1.02F, -0.3F, 1, 2, 1, 0.0F, false));
        GaugeCorners2.cubeList.add(new ModelBox(GaugeCorners2, 24, 21, -3.05F, -1.05F, -0.3F, 1, 2, 1, 0.0F, false));
        GaugeCorners2.cubeList.add(new ModelBox(GaugeCorners2, 106, 16, -0.8F, -2.43F, -0.4F, 2, 1, 1, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(-1.025F, -39.0F, 8.5F);
        setRotationAngle(bone2, 0.0F, 0.0F, 0.7854F);
        Gauges2.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 24, 21, -3.155F, -1.1F, -0.3F, 1, 2, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 24, 21, 2.125F, -1.2F, -0.3F, 1, 2, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 105, 19, -0.875F, -2.5F, -0.4F, 2, 1, 1, 0.0F, true));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        Weels.render(f5);
        Shafts.render(f5);
        Truck.render(f5);
        Tank.render(f5);
        Gauges.render(f5);
        Gauges2.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}