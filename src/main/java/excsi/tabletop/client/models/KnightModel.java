package excsi.tabletop.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class KnightModel extends Model {
	private final ModelRenderer knight;
	private final ModelRenderer cube_r1;

	public KnightModel() {
		super(RenderType::getEntitySolid);
		textureWidth = 16;
		textureHeight = 16;

		knight = new ModelRenderer(this);
		knight.setRotationPoint(0.0F, 16.0F, -0.1F);
		setRotationAngle(knight, 0.0F, 1.5708F, 0.0F);
		knight.setTextureOffset(0, 0).addBox(-0.9F, 7.4F, -0.8F, 1.6F, 0.8F, 1.6F, -0.2F, false);
		knight.setTextureOffset(0, 5).addBox(-0.5F, 6.9F, -0.4F, 0.8F, 0.8F, 0.8F, -0.1F, false);
		knight.setTextureOffset(0, 2).addBox(-0.7F, 5.6F, -0.65F, 1.2F, 1.9F, 1.4F, -0.28F, true);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-0.1F, 8.2F, 0.0F);
		knight.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.3927F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(0, 3).addBox(-0.5F, -2.0947F, -1.3849F, 1.0F, 1.4F, 1.0F, -0.3F, false);
		cube_r1.setTextureOffset(0, 1).addBox(-0.7F, -2.6626F, -1.2382F, 1.4F, 1.3F, 1.6F, -0.4F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		knight.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}