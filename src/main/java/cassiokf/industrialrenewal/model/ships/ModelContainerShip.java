package cassiokf.industrialrenewal.model.ships;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelContainerShip extends ModelBase
{
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer cube_r1;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bb_main;

    public ModelContainerShip()
    {
        textureWidth = 256;
        textureHeight = 256;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 34, 34, 7.0F, -30.0F, -16.0F, 1, 12, 32, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -8.0F, -30.0F, -16.0F, 1, 12, 32, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 84, 62, -7.0F, -28.0F, -16.0F, 14, 1, 16, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 36, 78, -7.0F, -19.0F, -16.0F, 14, 1, 16, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 80, 80, -7.0F, -28.0F, 0.0F, 14, 1, 16, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 68, 37, -7.0F, -19.0F, 0.0F, 14, 1, 16, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 48, -7.0F, -28.0F, 16.0F, 14, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 27, 62, -7.0F, -28.0F, -19.0F, 14, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 44, -7.0F, -19.0F, -19.0F, 14, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 4, -6.0F, -28.0F, -22.0F, 12, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -6.0F, -19.0F, -22.0F, 12, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 12, -5.0F, -28.0F, -25.0F, 10, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 8, -5.0F, -19.0F, -25.0F, 10, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 20, -4.0F, -28.0F, -28.0F, 8, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 16, -4.0F, -19.0F, -28.0F, 8, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 28, -3.0F, -28.0F, -31.0F, 6, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 24, -3.0F, -19.0F, -31.0F, 6, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 18, 28, -2.0F, -28.0F, -34.0F, 4, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 18, 24, -2.0F, -19.0F, -34.0F, 4, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 51, 44, -1.0F, -28.0F, -37.0F, 2, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 19, 19, -1.0F, -19.0F, -37.0F, 2, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 34, 23, -8.0F, -25.0F, 16.0F, 16, 7, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 38, 0, 6.99F, -30.0F, 16.0F, 1, 5, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 26, 16, -7.99F, -30.0F, 16.0F, 1, 5, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 52, 6.99F, -30.0F, 17.0F, 1, 4, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 51, 48, -7.99F, -30.0F, 17.0F, 1, 4, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 47, 6.99F, -30.0F, 18.0F, 1, 3, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 38, 6, -7.99F, -30.0F, 18.0F, 1, 3, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 15, 24, 6.99F, -30.0F, 19.0F, 1, 2, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 19, 16, -7.99F, -30.0F, 19.0F, 1, 2, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 27, 0, 6.99F, -30.0F, 20.0F, 1, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 23, 8, -7.99F, -30.0F, 20.0F, 1, 1, 1, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(8.0F, -6.0F, -16.0F);
        bone.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.3054F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 68, 0, -1.0F, -24.0F, -25.0F, 1, 12, 25, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-8.0F, -6.0F, -16.0F);
        bone.addChild(bone3);
        setRotationAngle(bone3, 0.0F, -0.3054F, 0.0F);
        bone3.cubeList.add(new ModelBox(bone3, 0, 53, 0.0F, -24.0F, -25.0F, 1, 12, 25, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone.addChild(bone4);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(1.0F, -25.0F, 16.0F);
        bone4.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7418F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 34, 15, -9.0F, -7.0F, 0.0F, 16, 7, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 0, 90, -5.0F, -36.0F, 7.0F, 10, 8, 10, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 34, 0, -9.0F, -41.0F, 7.0F, 18, 5, 10, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone6.cubeList.add(new ModelBox(bone6, 82, 97, -7.0F, -36.0F, -1.0F, 14, 8, 7, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 95, 0, -7.0F, -36.0F, -9.0F, 14, 8, 7, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 40, 95, -7.0F, -36.0F, -17.0F, 14, 8, 7, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 27, 44, -3.5F, -36.0F, -28.0F, 7, 8, 10, 0.0F, false));

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 34, 0, -1.0F, -28.0F, 19.0F, 1, 9, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 26, 8, -1.0F, -25.0F, 20.0F, 1, 6, 2, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        bone.render(f5);
        bone5.render(f5);
        bone6.render(f5);
        bb_main.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}