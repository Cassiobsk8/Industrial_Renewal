package cassiokf.industrialrenewal.model.carts;//Made with Blockbench
//Paste this code into your mod.

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCartFlat extends ModelBase
{
    private final ModelRenderer Weels;
    private final ModelRenderer Shafts;
    private final ModelRenderer Truck;

    public ModelCartFlat()
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
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        Weels.render(f5);
        Shafts.render(f5);
        Truck.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}