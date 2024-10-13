package excsi.tabletop.gameengines.chess;

import excsi.tabletop.common.util.AssetLib;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;

public enum PieceRenderInfo {

    PAWN(AssetLib.pawnModel,AssetLib.pawnTextureWhite,AssetLib.pawnTextureBlack),

    BISHOP(AssetLib.bishopModel,AssetLib.bishopTextureWhite,AssetLib.bishopTextureBlack),

    ROOK(AssetLib.rookModel,AssetLib.rookTextureWhite,AssetLib.rookTextureBlack),

    KNIGHT(AssetLib.knightModel,AssetLib.knightTextureWhite,AssetLib.knightTextureBlack),

    QUEEN(AssetLib.queenModel,AssetLib.queenTextureWhite,AssetLib.queenTextureBlack),

    KING(AssetLib.kingModel,AssetLib.kingTextureWhite,AssetLib.kingTextureBlack);

    public final Model model;

    public final ResourceLocation whitePiece;

    public final ResourceLocation blackPiece;

    PieceRenderInfo(Model model, ResourceLocation rl1, ResourceLocation rl2) {
        this.model=model;
        this.whitePiece=rl1;
        this.blackPiece=rl2;
    }

    public static PieceRenderInfo fromChar(char c) {
        switch (Character.toLowerCase(c)) {
            case 'b' : return BISHOP;
            case 'k' : return KING;
            case 'n' : return KNIGHT;
            case 'p' : return PAWN;
            case 'q' : return QUEEN;
            case 'r' : return ROOK;
            default : return null;
        }
    }
}
