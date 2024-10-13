package excsi.tabletop.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PawnModel extends Model {

	private final ModelRenderer pawn;

	public PawnModel() {
		super(RenderType::getEntitySolid);
		textureWidth = 16;
		textureHeight = 16;

		pawn = new ModelRenderer(this);
		pawn.setRotationPoint(0.0F, 16.0F, 0.0F);
		pawn.setTextureOffset(0, 0).addBox(-0.7F, 7.4F, -0.7F, 1.4F, 0.8F, 1.4F, -0.2F, false);
		pawn.setTextureOffset(0, 1).addBox(-0.55F, 6.0F, -0.55F, 1.1F, 1.1F, 1.1F, -0.3F, true);
		pawn.setTextureOffset(2, 2).addBox(-0.4F, 7.1F, -0.4F, 0.8F, 0.6F, 0.8F, -0.1F, false);
		pawn.setTextureOffset(1, 1).addBox(-0.45F, 6.4F, -0.45F, 0.9F, 1.3F, 0.9F, -0.3F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		pawn.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}