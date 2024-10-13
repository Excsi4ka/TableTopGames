package excsi.tabletop.client.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import excsi.tabletop.common.blocks.BlockChessBoard;
import excsi.tabletop.common.tiles.TileChessBoard;
import excsi.tabletop.common.util.AssetLib;
import excsi.tabletop.gameengines.chess.PieceColor;
import excsi.tabletop.gameengines.chess.PieceRenderInfo;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class ChessBoardRenderer extends TileEntityRenderer<TileChessBoard> {

    public final double pt = 0.0625;

    public ChessBoardRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(TileChessBoard tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockstate = tileEntityIn.getBlockState();
        Direction direction = blockstate.get(BlockChessBoard.FACING);
        matrixStackIn.push();
        RenderType layer = RenderType.getEntitySolid(AssetLib.boardTexture);
        IVertexBuilder buffer = bufferIn.getBuffer(layer);
        float f = direction.rotateY().getHorizontalAngle();
        switch (direction) {
            case SOUTH:
                matrixStackIn.translate(1, 0, 1);
                break;
            case WEST:
                matrixStackIn.translate(0, 0, 1);
                break;
            case EAST:
                matrixStackIn.translate(1, 0, 0);
                break;
        }
        matrixStackIn.rotate(Vector3f.YN.rotationDegrees(f + 90));
        matrixStackIn.translate(0.5625, 1.5, 0.5625);
        matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(180));
        AssetLib.chessBoardModel.render(matrixStackIn, buffer, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        //render pieces here
        matrixStackIn.push();
        matrixStackIn.translate(0, -0.125, 0);
        String[] rows = tileEntityIn.gameData.split("/");
        for (int i = 0; i < 8; ++i) {
            char[] rowData = rows[i].toCharArray();
            int column = 0;
            for (char c : rowData) {
                if (Character.isLetter(c)) {
                    PieceRenderInfo type = PieceRenderInfo.fromChar(c);
                    PieceColor pieceColor = Character.isUpperCase(c)?PieceColor.WHITE:PieceColor.BLACK;
                    RenderType layerAlt = RenderType.getEntitySolid(pieceColor.isWhite()?type.whitePiece:type.blackPiece);
                    IVertexBuilder bufferAlt = bufferIn.getBuffer(layerAlt);
                    matrixStackIn.push();
                    matrixStackIn.translate(pt * (8 - column * 2), 0, pt * (6 - (7-i) * 2));
                    type.model.render(matrixStackIn, bufferAlt, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
                    matrixStackIn.pop();
                    column++;
                }else {
                    column += (c - '0');
                }
            }
        }
        matrixStackIn.pop();
        //
        matrixStackIn.pop();
    }
}
