package excsi.tabletop.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class KingModel extends Model {

	private final ModelRenderer king;

	public KingModel() {
		super(RenderType::getEntitySolid);

		textureWidth = 16;
		textureHeight = 16;

		king = new ModelRenderer(this);
		king.setRotationPoint(-0.25F, 16.0F, 0.1F);
		setRotationAngle(king, 0.0F, 1.5708F, 0.0F);
		king.setTextureOffset(0, 0).addBox(-0.7F, 7.4F, -0.6F, 1.6F, 0.8F, 1.6F, -0.2F, false);
		king.setTextureOffset(0, 0).addBox(-0.7F, 5.4F, -0.6F, 1.6F, 1.2F, 1.6F, -0.4F, false);
		king.setTextureOffset(0, 5).addBox(-0.8F, 5.2F, -0.7F, 1.8F, 1.2F, 1.8F, -0.4F, false);
		king.setTextureOffset(0, 5).addBox(-0.4F, 4.5F, -0.3F, 1.0F, 1.9F, 1.0F, -0.3F, false);
		king.setTextureOffset(1, 3).addBox(-0.4F, 4.7F, -0.5F, 1.0F, 1.0F, 1.4F, -0.3F, false);
		king.setTextureOffset(3, 3).addBox(-0.4F, 6.9F, -0.3F, 1.0F, 0.8F, 1.0F, -0.1F, false);
		king.setTextureOffset(2, 3).addBox(-0.4F, 5.4F, -0.3F, 1.0F, 2.3F, 1.0F, -0.3F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		king.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}