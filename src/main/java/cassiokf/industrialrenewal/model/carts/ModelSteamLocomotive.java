package cassiokf.industrialrenewal.model.carts;// Made with Blockbench 3.6.6
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

	public ModelSteamLocomotive() {
		textureWidth = 256;
		textureHeight = 256;

		Truck = new ModelRenderer(this);
		Truck.setRotationPoint(0.0F, 24.0F, 0.0F);
		Truck.cubeList.add(new ModelBox(Truck, 0, 38, -9.0F, -25.0F, -9.0F, 18, 1, 18, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 74, 20, -22.0F, -25.0F, -6.5F, 13, 1, 13, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 106, 106, -23.0F, -25.0F, -6.0F, 1, 4, 12, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 84, 110, -22.0F, -35.0F, -5.0F, 1, 8, 10, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 0, 38, -22.25F, -34.0F, -4.0F, 1, 3, 8, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 38, 98, 11.0F, -32.0F, 8.25F, 10, 4, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 20, 81, 11.0F, -32.0F, -9.25F, 10, 4, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 111, 11, -25.0F, -24.0F, -5.0F, 2, 2, 2, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 27, 111, -25.0F, -24.0F, 3.0F, 2, 2, 2, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 38, 92, -22.0F, -38.0F, -1.0F, 2, 2, 2, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 10, 38, -26.0F, -24.5F, -5.5F, 1, 3, 3, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 0, 38, -26.0F, -24.5F, 2.5F, 1, 3, 3, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 62, 91, -15.0F, -23.0F, -5.0F, 9, 2, 10, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 64, 78, -2.1F, -24.0F, -5.0F, 15, 3, 10, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 67, 24, -11.5F, -24.0F, -0.875F, 2, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 67, 1, 9.0F, -23.0F, -9.0F, 13, 1, 18, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 0, 68, 9.0F, -37.0F, -9.0F, 1, 14, 18, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 38, 81, 9.0F, -40.0F, -8.0F, 1, 1, 16, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 62, 75, 9.0F, -39.0F, -9.0F, 1, 2, 3, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 74, 28, 9.0F, -39.0F, 6.0F, 1, 2, 3, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 0, 111, 9.0F, -41.0F, -6.0F, 1, 1, 12, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 20, 68, 8.0F, -41.2F, -6.0F, 15, 1, 12, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 34, 114, -21.75F, -25.75F, 6.15F, 4, 6, 3, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 39, 83, -21.75F, -25.75F, -9.1F, 4, 6, 3, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 12, 76, -22.25F, -22.35F, 6.7F, 1, 2, 2, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 12, 68, -22.25F, -22.35F, -8.7F, 1, 2, 2, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 26, 68, -22.25F, -24.9F, 6.7F, 1, 2, 2, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 6, 0, -22.25F, -24.9F, -8.7F, 1, 2, 2, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 113, 20, 10.0F, -33.0F, 8.0F, 12, 10, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 90, 91, 20.0F, -39.0F, 8.0F, 2, 6, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 0, 19, 20.0F, -39.0F, -9.0F, 2, 6, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 0, 57, 10.0F, -39.0F, 8.0F, 2, 6, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 54, 38, 10.0F, -39.0F, -9.0F, 2, 6, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 111, 0, 10.0F, -33.0F, -9.0F, 12, 10, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 74, 34, -19.0F, -34.0F, 7.0F, 28, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 67, 20, -6.0F, -38.5F, -0.5F, 9, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 67, 61, -19.0F, -34.0F, -8.0F, 28, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 82, 113, -19.0F, -34.0F, 6.0F, 1, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 112, 112, -8.0F, -34.0F, 6.0F, 1, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 13, 49, 2.0F, -37.5F, -0.5F, 1, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 112, 110, -19.0F, -34.0F, -7.0F, 1, 1, 1, 0.0F, false));
		Truck.cubeList.add(new ModelBox(Truck, 33, 111, -8.0F, -34.0F, -7.0F, 1, 1, 1, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(23.0F, -39.0F, -8.0F);
		Truck.addChild(bone);
		setRotationAngle(bone, 0.4363F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 106, 91, -15.0F, -1.15F, -2.25F, 15, 1, 5, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(23.0F, -39.0F, 8.0F);
		Truck.addChild(bone2);
		setRotationAngle(bone2, -0.4363F, 0.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 104, 78, -15.0F, -1.15F, -2.75F, 15, 1, 5, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
		Truck.addChild(bone3);
		bone3.cubeList.add(new ModelBox(bone3, 0, 19, -21.0F, -37.0F, -3.5F, 30, 12, 7, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 14, 111, 10.0F, -37.0F, -3.5F, 3, 3, 7, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 0, -21.0F, -34.5F, -6.0F, 30, 7, 12, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 88, 91, 10.0F, -34.5F, -6.0F, 3, 7, 12, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 64, 103, 10.0F, -24.5F, -6.0F, 3, 2, 12, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 56, 91, 10.0F, -27.5F, -6.0F, 3, 3, 3, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 70, 53, 13.0F, -29.0F, -0.5F, 1, 1, 1, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 10, 44, 12.0F, -29.0F, 5.5F, 3, 1, 1, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 44, 13.0F, -24.0F, 6.75F, 3, 1, 1, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 82, 103, 14.0F, -36.0F, 5.5F, 1, 7, 1, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 96, 91, 14.0F, -30.0F, 6.75F, 1, 7, 1, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 74, 22, 10.0F, -27.5F, 3.0F, 3, 3, 3, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 72, 0, -19.0F, -41.0F, -1.5F, 3, 4, 3, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 76, -19.5F, -44.0F, -2.0F, 4, 4, 4, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 68, -9.5F, -41.0F, -2.0F, 4, 4, 4, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 38, 103, -5.5F, -32.0F, 4.75F, 15, 7, 4, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 54, 49, -19.5F, -29.0F, -6.5F, 5, 4, 3, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 49, -19.5F, -29.0F, 3.5F, 5, 4, 3, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 100, -5.5F, -32.0F, -9.25F, 15, 7, 4, 0.0F, false));

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(12.5F, -28.5F, 0.0F);
		bone3.addChild(bone12);
		setRotationAngle(bone12, -0.3491F, 0.0F, 0.0F);
		bone12.cubeList.add(new ModelBox(bone12, 20, 68, 0.0F, 0.0F, -4.0F, 1, 5, 4, 0.0F, false));

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(12.5F, -28.5F, 0.0F);
		bone3.addChild(bone13);
		setRotationAngle(bone13, 0.3491F, 0.0F, 0.0F);
		bone13.cubeList.add(new ModelBox(bone13, 0, 0, 0.0F, 0.0F, 0.0F, 1, 5, 4, 0.0F, false));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-6.0F, -32.6263F, 0.6364F);
		bone3.addChild(bone4);
		setRotationAngle(bone4, -0.7854F, 0.0F, 0.0F);
		bone4.cubeList.add(new ModelBox(bone4, 67, 50, -15.0F, -0.6237F, -6.0164F, 30, 4, 7, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 0, 57, -15.0F, -0.1737F, 0.4136F, 30, 4, 7, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 62, 68, 15.0F, -0.6237F, -6.0364F, 4, 4, 2, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(-6.0F, -31.6364F, -1.6263F);
		bone3.addChild(bone5);
		setRotationAngle(bone5, 0.7854F, 0.0F, 0.0F);
		bone5.cubeList.add(new ModelBox(bone5, 67, 67, -15.0F, -0.2F, -6.0F, 30, 4, 7, 0.0F, false));
		bone5.cubeList.add(new ModelBox(bone5, 54, 38, -15.0F, -0.6136F, 0.4263F, 30, 4, 7, 0.0F, false));
		bone5.cubeList.add(new ModelBox(bone5, 56, 81, 15.0F, -0.6136F, 4.4263F, 4, 4, 3, 0.0F, false));

		wheel = new ModelRenderer(this);
		wheel.setRotationPoint(0.0F, 24.0F, 0.0F);
		wheel.cubeList.add(new ModelBox(wheel, 111, 15, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 85, 110, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 45, 114, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 98, 65, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 113, 100, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 60, 98, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 14, 111, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 68, 91, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 8, 111, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel.cubeList.add(new ModelBox(wheel, 70, 81, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

		wheel2 = new ModelRenderer(this);
		wheel2.setRotationPoint(6.0F, 24.0F, 0.0F);
		wheel2.cubeList.add(new ModelBox(wheel2, 0, 111, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 106, 97, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 113, 31, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 90, 65, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 112, 86, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 56, 88, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 106, 91, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 50, 81, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 104, 78, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel2.cubeList.add(new ModelBox(wheel2, 0, 76, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

		wheel3 = new ModelRenderer(this);
		wheel3.setRotationPoint(13.0F, 24.0F, 0.0F);
		wheel3.cubeList.add(new ModelBox(wheel3, 104, 110, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 104, 84, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 112, 84, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 8, 84, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 106, 101, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 0, 84, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 72, 103, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 70, 49, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 38, 103, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel3.cubeList.add(new ModelBox(wheel3, 20, 68, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

		wheel4 = new ModelRenderer(this);
		wheel4.setRotationPoint(19.0F, 24.0F, 0.0F);
		wheel4.cubeList.add(new ModelBox(wheel4, 96, 110, -15.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 46, 92, -15.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 106, 65, -15.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 42, 81, -15.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 90, 99, -15.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 79, 28, -15.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 34, 100, -12.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 0, 68, -12.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 0, 100, -16.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, false));
		wheel4.cubeList.add(new ModelBox(wheel4, 0, 0, -16.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, false));

		shaftr = new ModelRenderer(this);
		shaftr.setRotationPoint(0.0F, 24.0F, 0.0F);
		shaftr.cubeList.add(new ModelBox(shaftr, 68, 98, 5.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));
		shaftr.cubeList.add(new ModelBox(shaftr, 74, 63, -14.0F, -21.0F, 7.0F, 20, 1, 1, 0.0F, false));
		shaftr.cubeList.add(new ModelBox(shaftr, 83, 22, -8.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));
		shaftr.cubeList.add(new ModelBox(shaftr, 68, 95, -1.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));
		shaftr.cubeList.add(new ModelBox(shaftr, 70, 85, -14.0F, -21.0F, 6.0F, 1, 1, 1, 0.0F, false));

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(-14.0F, -20.0F, 7.5F);
		shaftr.addChild(bone6);
		setRotationAngle(bone6, 0.0F, 0.0F, 0.1745F);
		bone6.cubeList.add(new ModelBox(bone6, 72, 9, -5.0F, -1.0F, -0.5F, 5, 1, 1, 0.0F, false));

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(-13.7219F, -23.8494F, 7.5F);
		shaftr.addChild(bone8);
		setRotationAngle(bone8, 0.0F, 0.0F, 0.1745F);
		bone8.cubeList.add(new ModelBox(bone8, 72, 7, -5.0F, 0.0F, -0.3F, 5, 1, 1, 0.0F, false));

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(-14.0F, -21.0F, 7.5F);
		shaftr.addChild(bone7);
		setRotationAngle(bone7, 0.0F, 0.0F, 1.309F);
		bone7.cubeList.add(new ModelBox(bone7, 67, 22, -2.7F, -1.0F, -0.3F, 4, 1, 1, 0.0F, false));

		shaftr2 = new ModelRenderer(this);
		shaftr2.setRotationPoint(0.0F, 24.0F, -15.0F);
		shaftr2.cubeList.add(new ModelBox(shaftr2, 82, 30, 5.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));
		shaftr2.cubeList.add(new ModelBox(shaftr2, 74, 36, -14.0F, -23.0F, 7.0F, 20, 1, 1, 0.0F, false));
		shaftr2.cubeList.add(new ModelBox(shaftr2, 81, 0, -8.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));
		shaftr2.cubeList.add(new ModelBox(shaftr2, 70, 78, -1.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));
		shaftr2.cubeList.add(new ModelBox(shaftr2, 28, 77, -14.0F, -23.0F, 8.0F, 1, 1, 1, 0.0F, false));

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(-14.0F, -23.0F, 7.5F);
		shaftr2.addChild(bone9);
		setRotationAngle(bone9, 0.0F, 0.0F, -0.2618F);
		bone9.cubeList.add(new ModelBox(bone9, 0, 9, -5.0F, 0.0F, -0.5F, 5, 1, 1, 0.0F, false));

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(-13.214F, -20.174F, 7.3F);
		shaftr2.addChild(bone10);
		setRotationAngle(bone10, 0.0F, 0.0F, 0.6109F);
		bone10.cubeList.add(new ModelBox(bone10, 74, 65, -7.0F, -1.0F, -0.5F, 7, 1, 1, 0.0F, false));

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(-14.0F, -23.0F, 7.5F);
		shaftr2.addChild(bone11);
		setRotationAngle(bone11, 0.0F, 0.0F, 1.309F);
		bone11.cubeList.add(new ModelBox(bone11, 20, 77, 0.0F, -1.0F, -0.7F, 3, 1, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		Truck.render(f5);
		wheel.render(f5);
		wheel2.render(f5);
		wheel3.render(f5);
		wheel4.render(f5);
		shaftr.render(f5);
		shaftr2.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}