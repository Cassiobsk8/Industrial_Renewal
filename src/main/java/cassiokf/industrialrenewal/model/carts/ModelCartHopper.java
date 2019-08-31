package cassiokf.industrialrenewal.model.carts;//Made with Blockbench - CassioKF
//Paste this code into your mod.

import cassiokf.industrialrenewal.entity.EntityHopperCart;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCartHopper extends ModelBase
{
    private final ModelRenderer Weels;
    private final ModelRenderer Shafts;
    private final ModelRenderer Truck;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer canvas1;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer canvas2;
    private final ModelRenderer canvas3;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;

    public ModelCartHopper()
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
        Shafts.cubeList.add(new ModelBox(Shafts, 1, 6, 8.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 1, 6, 4.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 1, 6, -5.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 1, 6, -9.0F, -21.0F, -5.0F, 1, 1, 10, 0.0F, false));
        Shafts.cubeList.add(new ModelBox(Shafts, 56, 19, -14.0F, -22.0F, -1.0F, 28, 1, 2, 0.0F, false));

        Truck = new ModelRenderer(this);
        Truck.setRotationPoint(0.0F, 24.0F, 0.0F);
        Truck.cubeList.add(new ModelBox(Truck, 26, 0, 2.0F, -23.0F, -7.0F, 9, 1, 14, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 58, 4, -11.0F, -23.0F, -7.0F, 9, 1, 14, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 22, -13.0F, -24.0F, -9.0F, 26, 1, 18, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(7.0F, 0.0F, 0.0F);
        setRotationAngle(bone, 0.0F, 0.0F, 0.2618F);
        bone.cubeList.add(new ModelBox(bone, 0, 44, -1.0F, -13.0F, -8.0F, 2, 13, 16, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(-7.0F, 0.0F, 0.0F);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.2618F);
        bone2.cubeList.add(new ModelBox(bone2, 0, 44, -1.0F, -13.0F, -8.0F, 2, 13, 16, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone3.cubeList.add(new ModelBox(bone3, 0, 77, -8.0F, -27.0F, 7.0F, 16, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 77, -8.0F, -27.0F, -8.0F, 16, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 85, -8.95F, -30.0F, 7.0F, 18, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 85, -8.85F, -30.0F, -8.0F, 18, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 91, -10.0F, -33.0F, 7.0F, 20, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 91, -9.8F, -33.0F, -8.0F, 20, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 97, -10.0F, -36.0F, 7.0F, 20, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 97, -10.25F, -36.0F, -8.0F, 20, 3, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 104, -10.5F, -36.5F, 7.0F, 21, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 104, -10.5F, -36.5F, -8.0F, 21, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 113, -7.75F, -25.0F, -7.0F, 15, 1, 14, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, 11.0F, -36.0F, 7.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, -12.0F, -36.0F, 7.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, 6.0F, -36.0F, 7.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, 6.0F, -36.0F, -8.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, -7.0F, -36.0F, 7.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, -7.0F, -36.0F, -8.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, -0.5F, -36.0F, 7.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, -0.5F, -36.0F, -8.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, 11.0F, -36.0F, -8.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 36, 63, 11.0F, -36.85F, -8.0F, 1, 1, 16, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 36, 63, -12.0F, -36.85F, -8.0F, 1, 1, 16, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 36, 83, -12.0F, -36.85F, 7.25F, 24, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 36, 83, -12.0F, -36.85F, -8.25F, 24, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, -12.0F, -36.0F, -8.25F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, 11.0F, -36.0F, -0.5F, 1, 12, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 45, 48, -12.0F, -36.0F, -0.5F, 1, 12, 1, 0.0F, false));

        canvas1 = new ModelRenderer(this);
        canvas1.setRotationPoint(0.0F, 24.0F, 0.0F);
        canvas1.cubeList.add(new ModelBox(canvas1, 48, 111, 9.75F, -37.0F, -8.0F, 2, 1, 16, 0.0F, false));
        canvas1.cubeList.add(new ModelBox(canvas1, 48, 111, -11.75F, -37.0F, -8.0F, 2, 1, 16, 0.0F, false));
        canvas1.cubeList.add(new ModelBox(canvas1, 71, 123, -10.25F, -37.0F, -8.0F, 20, 1, 1, 0.0F, false));
        canvas1.cubeList.add(new ModelBox(canvas1, 71, 123, -10.25F, -37.0F, 7.0F, 20, 1, 1, 0.0F, false));
        canvas1.cubeList.add(new ModelBox(canvas1, 39, 121, -4.75F, -35.0F, -1.5F, 9, 1, 3, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(9.75F, -37.0F, 0.0F);
        setRotationAngle(bone4, 0.0F, 0.0F, -0.3491F);
        canvas1.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 45, 92, -6.0F, 0.0F, -7.9F, 6, 1, 16, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(-9.75F, -37.0F, 0.0F);
        setRotationAngle(bone5, 0.0F, 0.0F, 0.3491F);
        canvas1.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 44, 92, 0.0F, 0.0F, -7.9F, 6, 1, 16, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, -37.0F, -7.0F);
        setRotationAngle(bone6, -0.3491F, 0.0F, 0.0F);
        canvas1.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 72, 109, -10.25F, 0.0F, 0.0F, 20, 1, 6, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, -37.0F, 7.0F);
        setRotationAngle(bone7, 0.3491F, 0.0F, 0.0F);
        canvas1.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 72, 109, -10.25F, 0.0F, -6.0F, 20, 1, 6, 0.0F, false));

        canvas2 = new ModelRenderer(this);
        canvas2.setRotationPoint(0.0F, 24.0F, 0.0F);
        canvas2.cubeList.add(new ModelBox(canvas2, 50, 44, -11.75F, -37.0F, -8.0F, 23, 1, 16, 0.0F, false));

        canvas3 = new ModelRenderer(this);
        canvas3.setRotationPoint(0.0F, 24.0F, 0.0F);
        canvas3.cubeList.add(new ModelBox(canvas3, 48, 111, 9.75F, -37.0F, -8.0F, 2, 1, 16, 0.0F, false));
        canvas3.cubeList.add(new ModelBox(canvas3, 48, 111, -11.75F, -37.0F, -8.0F, 2, 1, 16, 0.0F, false));
        canvas3.cubeList.add(new ModelBox(canvas3, 71, 123, -10.25F, -37.0F, -8.0F, 20, 1, 1, 0.0F, false));
        canvas3.cubeList.add(new ModelBox(canvas3, 71, 123, -10.25F, -37.0F, 7.0F, 20, 1, 1, 0.0F, false));
        canvas3.cubeList.add(new ModelBox(canvas3, 76, 89, -4.75F, -39.0F, -8.0F, 9, 1, 16, 0.0F, false));
        canvas3.cubeList.add(new ModelBox(canvas3, 72, 118, -7.3F, -38.0F, -7.9F, 14, 1, 1, 0.0F, false));
        canvas3.cubeList.add(new ModelBox(canvas3, 72, 118, -7.35F, -38.0F, 6.9F, 14, 1, 1, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(9.75F, -37.0F, 0.0F);
        setRotationAngle(bone8, 0.0F, 0.0F, 0.3491F);
        canvas3.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 52, 92, -6.0F, 0.0F, -8.0F, 6, 1, 16, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(-9.75F, -37.0F, 0.0F);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.3491F);
        canvas3.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 84, 69, -0.6F, -0.2F, -8.0F, 6, 1, 16, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, -37.0F, -7.0F);
        setRotationAngle(bone10, -0.3491F, 0.0F, 0.0F);
        canvas3.addChild(bone10);

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, -37.0F, 7.0F);
        setRotationAngle(bone11, 0.3491F, 0.0F, 0.0F);
        canvas3.addChild(bone11);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        Weels.render(f5);
        Shafts.render(f5);
        Truck.render(f5);
        bone.render(f5);
        bone2.render(f5);
        bone3.render(f5);
        int items = ((EntityHopperCart) entity).invItensCount;
        if (items > 0 && items < 18) canvas1.render(f5);
        else if (items > 17 && items < 27) canvas2.render(f5);
        else if (items > 26) canvas3.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}