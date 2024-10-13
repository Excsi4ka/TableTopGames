package excsi.tabletop.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BishopModel extends Model {

	private final ModelRenderer bishop;

	public BishopModel() {
		super(RenderType::getEntitySolid);
		textureWidth = 16;
		textureHeight = 16;

		bishop = new ModelRenderer(this);
		bishop.setRotationPoint(0.0F, 17.6F, 0.0F);
		bishop.setTextureOffset(0, 0).addBox(-0.8F, 5.8F, -0.8F, 1.6F, 0.8F, 1.6F, -0.2F, false);
		bishop.setTextureOffset(0, 0).addBox(-0.8F, 3.9F, -0.8F, 1.6F, 1.3F, 1.6F, -0.4F, true);
		bishop.setTextureOffset(0, 1).addBox(-0.5F, 3.8F, -0.5F, 1.0F, 1.4F, 1.0F, -0.3F, false);
		bishop.setTextureOffset(0, 1).addBox(-0.5F, 5.3F, -0.5F, 1.0F, 0.8F, 1.0F, -0.1F, true);
		bishop.setTextureOffset(0, 4).addBox(-0.6F, 4.2F, -0.6F, 1.2F, 1.9F, 1.2F, -0.3F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bishop.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}