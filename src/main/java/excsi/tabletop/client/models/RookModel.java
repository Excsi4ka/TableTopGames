package excsi.tabletop.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RookModel extends Model {

	private final ModelRenderer rook;

	public RookModel() {
		super(RenderType::getEntitySolid);
		textureWidth = 16;
		textureHeight = 16;

		rook = new ModelRenderer(this);
		rook.setRotationPoint(0.0F, 16.0F, 0.0F);
		rook.setTextureOffset(0, 0).addBox(-0.8F, 7.4F, -0.8F, 1.6F, 0.8F, 1.6F, -0.2F, false);
		rook.setTextureOffset(0, 0).addBox(-0.9F, 5.5F, -0.9F, 1.8F, 1.3F, 1.8F, -0.4F, true);
		rook.setTextureOffset(1, 3).addBox(-0.5F, 6.9F, -0.5F, 1.0F, 0.8F, 1.0F, -0.1F, false);
		rook.setTextureOffset(0, 1).addBox(-0.7F, 5.9F, -0.7F, 1.4F, 1.4F, 1.4F, -0.3F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		rook.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}