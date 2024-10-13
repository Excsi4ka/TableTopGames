package excsi.tabletop.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ChessBoardModel extends Model {

	private final ModelRenderer model;

	public ChessBoardModel() {
		super(RenderType::getEntitySolid);
		textureWidth = 128;
		textureHeight = 128;

		model = new ModelRenderer(this);
		model.setRotationPoint(8.0F, 24.0F, -8.0F);
		model.setTextureOffset(0, 0).addBox(-16.0F, -2.0F, -2.0F, 18.0F, 2.0F, 18.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		model.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}