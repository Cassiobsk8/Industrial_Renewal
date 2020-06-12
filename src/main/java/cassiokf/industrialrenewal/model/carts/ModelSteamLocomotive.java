package cassiokf.industrialrenewal.model.carts;// Made with Blockbench 3.5.4
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSteamLocomotive extends ModelBase
{
    private final ModelRenderer Truck;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer wheel;
    private final ModelRenderer wheel2;
    private final ModelRenderer wheel3;
    private final ModelRenderer wheel4;
    private final ModelRenderer shaftr;
    private final ModelRenderer bone6;
    private final ModelRenderer bone8;
    private final ModelRenderer bone7;
    private final ModelRenderer shaftr2;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;

    public ModelSteamLocomotive()
    {
        textureWidth = 256;
        textureHeight = 256;

        Truck = new ModelRenderer(this);
        Truck.setRotationPoint(0.0F, 24.0F, 0.0F);
        Truck.cubeList.add(new ModelBox(Truck, 56, 56, -9.0F, -25.0F, -9.0F, 18, 1, 18, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 73, 6, -22.0F, -25.0F, -6.5F, 13, 1, 13, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 102, -23.0F, -25.0F, -6.0F, 1, 4, 12, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 26, 102, -22.0F, -35.0F, -5.0F, 1, 8, 10, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 70, -22.25F, -34.0F, -4.0F, 1, 3, 8, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 62, 106, 11.0F, -32.0F, 8.25F, 10, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 38, 103, 11.0F, -32.0F, -9.25F, 10, 4, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 46, 108, -25.0F, -24.0F, -5.0F, 2, 2, 2, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 38, 108, -25.0F, -24.0F, 3.0F, 2, 2, 2, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 62, 70, -22.0F, -38.0F, -1.0F, 2, 2, 2, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 10, 70, -26.0F, -24.5F, -5.5F, 1, 3, 3, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 70, -26.0F, -24.5F, 2.5F, 1, 3, 3, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 72, 75, -15.0F, -23.0F, -5.0F, 9, 2, 10, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 20, 75, -2.1F, -24.0F, -5.0F, 15, 3, 10, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 67, 56, -11.5F, -24.0F, -0.875F, 2, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 68, 33, 9.0F, -23.0F, -9.0F, 13, 1, 18, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 70, 9.0F, -37.0F, -9.0F, 1, 14, 18, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 54, 75, 9.0F, -40.0F, -8.0F, 1, 1, 16, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 60, 75, 9.0F, -39.0F, -9.0F, 1, 2, 3, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 20, 74, 9.0F, -39.0F, 6.0F, 1, 2, 3, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 48, 103, 9.0F, -41.0F, -6.0F, 1, 1, 12, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 73, 20, 8.0F, -41.2F, -5.8F, 15, 1, 11, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 19, -21.25F, -25.25F, 5.95F, 3, 5, 3, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 0, -21.25F, -25.25F, -9.3F, 3, 5, 3, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 113, 33, -22.25F, -22.35F, 6.6F, 1, 2, 2, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 117, 10, -22.25F, -22.35F, -8.7F, 1, 2, 2, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 113, 46, -22.25F, -24.9F, 6.7F, 1, 2, 2, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 115, 21, -22.25F, -24.9F, -8.6F, 1, 2, 2, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 74, 112, 10.0F, -33.0F, 8.0F, 12, 10, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 6, 102, 20.0F, -39.0F, 8.0F, 2, 6, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 51, 20.0F, -39.0F, -9.0F, 2, 6, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 0, 102, 10.0F, -39.0F, 8.0F, 2, 6, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 72, 75, 10.0F, -39.0F, -9.0F, 2, 6, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 110, 56, 10.0F, -33.0F, -9.0F, 12, 10, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 67, 54, -19.0F, -34.0F, 7.0F, 28, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 38, 88, -6.0F, -38.5F, -0.5F, 9, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 67, 52, -19.0F, -34.0F, -8.0F, 28, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 112, 16, -19.0F, -34.0F, 6.0F, 1, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 100, 112, -8.0F, -34.0F, 6.0F, 1, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 25, 74, 2.0F, -37.5F, -0.5F, 1, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 112, 112, -19.0F, -34.0F, -7.0F, 1, 1, 1, 0.0F, false));
        Truck.cubeList.add(new ModelBox(Truck, 48, 112, -8.0F, -34.0F, -7.0F, 1, 1, 1, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(23.0F, -39.0F, -8.0F);
        Truck.addChild(bone);
        setRotationAngle(bone, 0.4363F, 0.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 100, 75, -15.0F, -1.0F, -2.0F, 15, 1, 5, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(23.0F, -39.0F, 8.0F);
        Truck.addChild(bone2);
        setRotationAngle(bone2, -0.4363F, 0.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 72, 0, -15.0F, -1.0F, -3.0F, 15, 1, 5, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        Truck.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 0, 51, -21.0F, -37.0F, -3.5F, 30, 12, 7, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 14, 102, 10.0F, -37.0F, -3.5F, 3, 3, 7, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -21.0F, -34.5F, -6.0F, 30, 7, 12, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 76, 87, 10.0F, -34.5F, -6.0F, 3, 7, 12, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 94, 98, 10.0F, -24.5F, -6.0F, 3, 2, 12, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 76, 92, 10.0F, -27.5F, -6.0F, 3, 3, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 65, 75, 13.0F, -29.0F, -0.5F, 1, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 76, 12.0F, -29.0F, 5.5F, 3, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 73, 29, 13.0F, -24.0F, 6.75F, 3, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 112, 8, 14.0F, -36.0F, 5.5F, 1, 7, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 78, 75, 14.0F, -30.0F, 6.75F, 1, 7, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 73, 42, 10.0F, -27.5F, 3.0F, 3, 3, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 73, 35, -19.0F, -41.0F, -1.5F, 3, 4, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 112, 0, -19.5F, -44.0F, -2.25F, 4, 4, 4, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 100, 112, -9.5F, -41.0F, -2.25F, 4, 4, 4, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 94, 87, -6.0F, -32.0F, 4.75F, 15, 7, 4, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 110, 67, -20.0F, -29.0F, -6.5F, 5, 4, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 81, -20.0F, -29.0F, 3.0F, 5, 4, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 38, 92, -6.0F, -32.0F, -9.25F, 15, 7, 4, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(12.5F, -28.5F, 0.0F);
        bone3.addChild(bone12);
        setRotationAngle(bone12, -0.3491F, 0.0F, 0.0F);
        bone12.cubeList.add(new ModelBox(bone12, 73, 20, 0.0F, 0.0F, -4.0F, 1, 5, 4, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(12.5F, -28.5F, 0.0F);
        bone3.addChild(bone13);
        setRotationAngle(bone13, 0.3491F, 0.0F, 0.0F);
        bone13.cubeList.add(new ModelBox(bone13, 0, 35, 0.0F, 0.0F, 0.0F, 1, 5, 4, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-6.0F, -32.6263F, 0.6364F);
        bone3.addChild(bone4);
        setRotationAngle(bone4, -0.7854F, 0.0F, 0.0F);
        bone4.cubeList.add(new ModelBox(bone4, 0, 35, -15.0F, -0.3029F, -6.0F, 30, 3, 13, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 27, 16.0F, -0.3029F, -6.0F, 3, 3, 2, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(-6.0F, -31.6364F, -1.6263F);
        bone3.addChild(bone5);
        setRotationAngle(bone5, 0.7854F, 0.0F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 0, 19, -15.0F, -0.3029F, -6.0F, 30, 3, 13, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 72, 6, 16.0F, -0.3029F, 4.4F, 3, 3, 3, 0.0F, false));

        wheel = new ModelRenderer(this);
        wheel.setRotationPoint(0.0F, 24.0F, 0.0F);
        wheel.cubeList.add(new ModelBox(wheel, 108, 81, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 27, 102, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 14, 112, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 62, 103, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 62, 111, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 46, 90, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 70, 111, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 72, 92, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 54, 110, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 38, 92, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

        wheel2 = new ModelRenderer(this);
        wheel2.setRotationPoint(6.0F, 24.0F, 0.0F);
        wheel2.cubeList.add(new ModelBox(wheel2, 107, 0, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 100, 81, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 0, 111, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 38, 90, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 110, 85, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 80, 89, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 8, 109, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 79, 20, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 100, 106, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 73, 20, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

        wheel3 = new ModelRenderer(this);
        wheel3.setRotationPoint(13.0F, 24.0F, 0.0F);
        wheel3.cubeList.add(new ModelBox(wheel3, 92, 106, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 60, 80, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 83, 110, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 72, 89, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 0, 109, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 58, 88, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 17, 105, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 72, 0, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 14, 102, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 70, 70, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

        wheel4 = new ModelRenderer(this);
        wheel4.setRotationPoint(19.0F, 24.0F, 0.0F);
        wheel4.cubeList.add(new ModelBox(wheel4, 84, 106, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 20, 79, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 52, 108, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 72, 83, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 27, 106, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 20, 83, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 100, 75, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 6, 35, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 94, 87, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
        wheel4.cubeList.add(new ModelBox(wheel4, 0, 35, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

        shaftr = new ModelRenderer(this);
        shaftr.setRotationPoint(0.0F, 24.0F, 0.0F);
        shaftr.cubeList.add(new ModelBox(shaftr, 22, 112, 5.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));
        shaftr.cubeList.add(new ModelBox(shaftr, 20, 72, -14.0F, -21.0F, 7.0F, 20, 1, 1, 0.0F, false));
        shaftr.cubeList.add(new ModelBox(shaftr, 66, 88, -8.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));
        shaftr.cubeList.add(new ModelBox(shaftr, 44, 108, -1.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));
        shaftr.cubeList.add(new ModelBox(shaftr, 70, 103, -14.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(-14.0F, -20.0F, 7.5F);
        shaftr.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, 0.1745F);
        bone6.cubeList.add(new ModelBox(bone6, 0, 44, -5.0F, -1.0F, -0.5F, 5, 1, 1, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(-13.7219F, -23.8494F, 7.5F);
        shaftr.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.0F, 0.1745F);
        bone8.cubeList.add(new ModelBox(bone8, 0, 10, -5.0F, 0.0F, -0.3F, 5, 1, 1, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(-14.0F, -21.0F, 7.5F);
        shaftr.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, 1.309F);
        bone7.cubeList.add(new ModelBox(bone7, 0, 46, -2.7F, -1.0F, -0.3F, 4, 1, 1, 0.0F, false));

        shaftr2 = new ModelRenderer(this);
        shaftr2.setRotationPoint(0.0F, 24.0F, -15.0F);
        shaftr2.cubeList.add(new ModelBox(shaftr2, 82, 42, 5.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));
        shaftr2.cubeList.add(new ModelBox(shaftr2, 20, 70, -14.0F, -23.0F, 7.0F, 20, 1, 1, 0.0F, false));
        shaftr2.cubeList.add(new ModelBox(shaftr2, 82, 35, -8.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));
        shaftr2.cubeList.add(new ModelBox(shaftr2, 81, 6, -1.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));
        shaftr2.cubeList.add(new ModelBox(shaftr2, 13, 81, -14.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(-14.0F, -23.0F, 7.5F);
        shaftr2.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.2618F);
        bone9.cubeList.add(new ModelBox(bone9, 0, 8, -5.0F, 0.0F, -0.5F, 5, 1, 1, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(-13.214F, -20.174F, 7.3F);
        shaftr2.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, 0.6109F);
        bone10.cubeList.add(new ModelBox(bone10, 72, 87, -7.0F, -1.0F, -0.5F, 7, 1, 1, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(-14.0F, -23.0F, 7.5F);
        shaftr2.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.0F, 1.309F);
        bone11.cubeList.add(new ModelBox(bone11, 10, 76, 0.0F, -1.0F, -0.7F, 3, 1, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        Truck.render(f5);
        wheel.render(f5);
        wheel2.render(f5);
        wheel3.render(f5);
        wheel4.render(f5);
        shaftr.render(f5);
        shaftr2.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}