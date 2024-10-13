package excsi.tabletop.common.util;

import excsi.tabletop.client.models.*;
import net.minecraft.util.ResourceLocation;

public class AssetLib {

    public static final ChessBoardModel chessBoardModel = new ChessBoardModel();

    public static final PawnModel pawnModel = new PawnModel();

    public static final RookModel rookModel = new RookModel();

    public static final BishopModel bishopModel = new BishopModel();

    public static final KnightModel knightModel = new KnightModel();

    public static final QueenModel queenModel = new QueenModel();

    public static final KingModel kingModel = new KingModel();

    public static final ResourceLocation TEXTURE = new ResourceLocation("tabletop:textures/gui/chess.png");

    public static final ResourceLocation boardTexture = new ResourceLocation("tabletop","textures/block/chess_board.png");

    public static final ResourceLocation rookTextureWhite = new ResourceLocation("tabletop","textures/block/rook_white.png");

    public static final ResourceLocation rookTextureBlack = new ResourceLocation("tabletop","textures/block/rook_black.png");

    public static final ResourceLocation pawnTextureWhite = new ResourceLocation("tabletop","textures/block/pawn_white.png");

    public static final ResourceLocation pawnTextureBlack = new ResourceLocation("tabletop","textures/block/pawn_black.png");

    public static final ResourceLocation queenTextureBlack = new ResourceLocation("tabletop","textures/block/queen_black.png");

    public static final ResourceLocation queenTextureWhite = new ResourceLocation("tabletop","textures/block/queen_white.png");

    public static final ResourceLocation kingTextureBlack = new ResourceLocation("tabletop","textures/block/king_black.png");

    public static final ResourceLocation kingTextureWhite = new ResourceLocation("tabletop","textures/block/king_white.png");

    public static final ResourceLocation bishopTextureBlack= new ResourceLocation("tabletop","textures/block/bishop_black.png");

    public static final ResourceLocation bishopTextureWhite = new ResourceLocation("tabletop","textures/block/bishop_white.png");

    public static final ResourceLocation knightTextureBlack = new ResourceLocation("tabletop","textures/block/knight_black.png");

    public static final ResourceLocation knightTextureWhite = new ResourceLocation("tabletop","textures/block/knight_white.png");
}
