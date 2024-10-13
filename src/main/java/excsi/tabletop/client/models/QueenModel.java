package excsi.tabletop.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class QueenModel extends Model {

	private final ModelRenderer queen;

	public QueenModel() {
		super(RenderType::getEntitySolid);
		textureWidth = 16;
		textureHeight = 16;

		queen = new ModelRenderer(this);
		queen.setRotationPoint(0.0F, 16.0F, -0.1F);
		queen.setTextureOffset(0, 0).addBox(-0.8F, 7.4F, -0.7F, 1.6F, 0.8F, 1.6F, -0.2F, false);
		queen.setTextureOffset(0, 0).addBox(-0.8F, 5.4F, -0.7F, 1.6F, 1.2F, 1.6F, -0.4F, false);
		queen.setTextureOffset(0, 0).addBox(-0.9F, 5.2F, -0.8F, 1.8F, 1.2F, 1.8F, -0.4F, false);
		queen.setTextureOffset(1, 4).addBox(-0.5F, 5.1F, -0.4F, 1.0F, 1.4F, 1.0F, -0.3F, false);
		queen.setTextureOffset(0, 1).addBox(-0.5F, 6.9F, -0.4F, 1.0F, 0.8F, 1.0F, -0.1F, false);
		queen.setTextureOffset(0, 0).addBox(-0.5F, 5.4F, -0.4F, 1.0F, 2.3F, 1.0F, -0.3F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		queen.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}