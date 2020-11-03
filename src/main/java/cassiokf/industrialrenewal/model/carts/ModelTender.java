package cassiokf.industrialrenewal.model.carts;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTender extends ModelBase
{
	private final ModelRenderer Truck;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone12;
	private final ModelRenderer bone13;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;
	private final ModelRenderer wheel2;
	private final ModelRenderer wheel3;
	private final ModelRenderer bb_main;

	public ModelTender() {
		textureWidth = 128;
		textureHeight = 128;

		Truck = new ModelRenderer(this);
		Truck.setRotationPoint(0.0F, 24.0F, 0.0F);
		Truck.cubeList.add(new ModelBox(Truck, 0, 0, -9.0F, -25.0F, -9.0F, 18, 1, 18, 0.0F, true));
		Truck.cubeList.add(new ModelBox(Truck, 36, 36, -5.5F, -23.0F, -5.0F, 11, 2, 10, 0.0F, true));
		Truck.cubeList.add(new ModelBox(Truck, 8, 4, -1.0F, -24.0F, -1.0F, 2, 1, 2, 0.0F, true));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(23.0F, -39.0F, -8.0F);
		Truck.addChild(bone);
		setRotationAngle(bone, 0.4363F, 0.0F, 0.0F);


		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(23.0F, -39.0F, 8.0F);
		Truck.addChild(bone2);
		setRotationAngle(bone2, -0.4363F, 0.0F, 0.0F);
		

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
		Truck.addChild(bone3);
		

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(12.5F, -28.5F, 0.0F);
		bone3.addChild(bone12);
		setRotationAngle(bone12, -0.3491F, 0.0F, 0.0F);
		

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(12.5F, -28.5F, 0.0F);
		bone3.addChild(bone13);
		setRotationAngle(bone13, 0.3491F, 0.0F, 0.0F);
		

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-6.0F, -32.6263F, 0.6364F);
		bone3.addChild(bone4);
		setRotationAngle(bone4, -0.7854F, 0.0F, 0.0F);


		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(-6.0F, -31.6364F, -1.6263F);
		bone3.addChild(bone5);
		setRotationAngle(bone5, 0.7854F, 0.0F, 0.0F);


		wheel2 = new ModelRenderer(this);
		wheel2.setRotationPoint(8.0F, 24.0F, 0.0F);
		wheel2.cubeList.add(new ModelBox(wheel2, 8, 0, -5.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 7, 7, -5.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 0, 45, -5.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 0, 16, -5.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 7, 15, -5.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 0, 14, -5.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 0, 47, -6.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 46, 25, -6.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 22, 45, -2.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, true));
		wheel2.cubeList.add(new ModelBox(wheel2, 18, 45, -2.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, true));

		wheel3 = new ModelRenderer(this);
		wheel3.setRotationPoint(17.0F, 24.0F, 0.0F);
		wheel3.cubeList.add(new ModelBox(wheel3, 0, 4, -23.0F, -23.0F, 5.0F, 3, 3, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 0, 0, -23.0F, -23.0F, -6.0F, 3, 3, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 7, 13, -23.0F, -20.0F, 5.0F, 3, 1, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 0, 12, -23.0F, -20.0F, -6.0F, 3, 1, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 7, 11, -23.0F, -24.0F, 5.0F, 3, 1, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 0, 10, -23.0F, -24.0F, -6.0F, 3, 1, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 12, 45, -24.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 8, 45, -24.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 0, 32, -20.0F, -23.0F, 5.0F, 1, 3, 1, 0.0F, true));
		wheel3.cubeList.add(new ModelBox(wheel3, 0, 19, -20.0F, -23.0F, -6.0F, 1, 3, 1, 0.0F, true));

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 32, -9.0F, -33.0F, -9.0F, 18, 8, 5, 0.0F, true));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 19, -9.0F, -33.0F, 4.0F, 18, 8, 5, 0.0F, true));
		bb_main.cubeList.add(new ModelBox(bb_main, 18, 45, 5.0F, -33.0F, -4.0F, 4, 8, 8, 0.0F, true));
		bb_main.cubeList.add(new ModelBox(bb_main, 41, 32, -9.0F, -36.0F, -9.0F, 18, 3, 1, 0.0F, true));
		bb_main.cubeList.add(new ModelBox(bb_main, 41, 19, -9.0F, -36.0F, 8.0F, 18, 3, 1, 0.0F, true));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 45, 8.0F, -36.0F, -8.0F, 1, 3, 16, 0.0F, true));
		bb_main.cubeList.add(new ModelBox(bb_main, 46, 23, -11.0F, -24.0F, -0.5F, 10, 1, 1, 0.0F, true));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		Truck.render(f5);
		wheel2.render(f5);
		wheel3.render(f5);
		bb_main.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}