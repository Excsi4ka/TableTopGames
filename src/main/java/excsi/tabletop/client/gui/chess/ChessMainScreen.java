package excsi.tabletop.client.gui.chess;

import com.mojang.blaze3d.matrix.MatrixStack;
import excsi.tabletop.common.tiles.TileChessBoard;
import excsi.tabletop.common.util.AssetLib;
import excsi.tabletop.gameengines.chess.*;
import excsi.tabletop.gameengines.chess.pieces.AbstractChessPiece;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class ChessMainScreen extends Screen {

    public Minecraft mc;

    public TileChessBoard tile;

    public Board gameEngineBoard;

    public AbstractChessPiece currentlySelected;

    public PieceColor currentTurn, checkedColor, promotionLockedColor;

    public List<Move> currentPieceMoves;

    public boolean gameStarted,gameOver;

    public ChessMainScreen(TileChessBoard board) {
        super(new TranslationTextComponent("chess.gui"));
        tile = board;
        gameEngineBoard = new Board(tile);
        currentTurn = gameEngineBoard.currentTurn;
        checkedColor = tile.checkedColor;
        promotionLockedColor = tile.promotionLockedColor;
        gameStarted = tile.gameStarted;
        gameOver = tile.gameOver;
    }

    @Override
    public void tick() {
        if(tile.shouldUpdate) {
            gameEngineBoard.syncInformation(tile);
            currentTurn = gameEngineBoard.currentTurn;
            checkedColor = tile.checkedColor;
            promotionLockedColor = tile.promotionLockedColor;
            gameStarted = tile.gameStarted;
            gameOver = tile.gameOver;
            tile.shouldUpdate=false;
            if(tile.gameStartReInit) {
                buttons.clear();
                children.clear();
                setListener((IGuiEventListener)null);
                init();
                tile.gameStartReInit=false;
            }
        }
        if(currentlySelected != null && currentPieceMoves == null){
            currentPieceMoves = currentlySelected.getLegalMoves();
        }
    }

    @Override
    protected void init() {
        mc = Minecraft.getInstance();
        int x = width / 2 - 104;
        int y = height / 2 + 78;
        boolean white = tile.playerWhite.equals(mc.player.getName().getString());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                addButton(new ChessTileButton(x, y, (Button b) -> {
                    ChessTileButton cb = (ChessTileButton) b;
                    if (currentlySelected == null && gameEngineBoard.grid[cb.rowIndex][cb.columnIndex] != null && gameEngineBoard.grid[cb.rowIndex][cb.columnIndex].pieceColor == currentTurn) {
                        gameEngineBoard.selectPiece(cb.rowIndex, cb.columnIndex);
                        currentlySelected = gameEngineBoard.grid[cb.rowIndex][cb.columnIndex];
                    } else if (currentlySelected != null && !currentlySelected.isSamePosition(cb.rowIndex, cb.columnIndex) && currentlySelected.pieceColor == currentTurn) {
                        if (gameEngineBoard.grid[cb.rowIndex][cb.columnIndex] == null || (gameEngineBoard.grid[cb.rowIndex][cb.columnIndex] != null && !(gameEngineBoard.grid[cb.rowIndex][cb.columnIndex].pieceColor == currentTurn))) {
                            if (gameEngineBoard.handleMove(cb.rowIndex, cb.columnIndex)) {
                                currentlySelected = null;
                                currentPieceMoves = null;
                                return;
                            }
                        }
                        if (gameEngineBoard.grid[cb.rowIndex][cb.columnIndex] != null && gameEngineBoard.grid[cb.rowIndex][cb.columnIndex].pieceColor == currentTurn) {
                            gameEngineBoard.selectPiece(cb.rowIndex, cb.columnIndex);
                            currentlySelected = gameEngineBoard.grid[cb.rowIndex][cb.columnIndex];
                            currentPieceMoves = null;
                        }
                    } else if (currentlySelected != null && currentlySelected.isSamePosition(cb.rowIndex, cb.columnIndex)) {
                        gameEngineBoard.currentlySelected = null;
                        currentlySelected = null;
                        currentPieceMoves = null;
                    }
                },white?i:7-i,white?j:7-j, this));
                x += 26;
            }
            x = width / 2 - 104;
            y -= 26;
        }
        x = width / 2 - 52;
        y = height / 2 - 13;
        for (int i = 0; i < 4; i++) {
            addButton(new ChessPromotionChoiceButton(x, y, (Button b) -> {
                ChessTileButton cb = (ChessTileButton) b;
                gameEngineBoard.sendPromotionChoice(AbstractChessPiece.promotionChars[cb.rowIndex]);
            }, i, 0, this));
            x += 26;
        }
        x = width/2-90;
        y = height/2-60;
        addButton(new GameStartButton(x+75,y+25,100,20,this));
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        renderBackground(ms);
        mc.getTextureManager().bindTexture(AssetLib.TEXTURE);
        int middleX = width/2-128;
        int middleY = height/2-128;
        int x = width/2-104;
        int y = height/2+78;
        if(gameStarted)
            drawBoardAndRelated(ms,middleX,middleY,x,y);
        else
            drawPreScreen(ms);
        if(currentTurn==promotionLockedColor)
            darkenScreen(ms);
        super.render(ms,mouseX,mouseY,partialTicks);
        if(gameOver) {
            darkenScreen(ms);
            String s;
            if(tile.winner.equals("DRAW"))
                s = new TranslationTextComponent("tabletop.draw").getString();
            else
                s = new TranslationTextComponent("tabletop.winner",new Object[]{tile.winner}).getString();
            int offset = mc.fontRenderer.getStringWidth(s)/2;
            mc.fontRenderer.drawStringWithShadow(ms,s,width/2-offset,height/2,0xFFFFFF);
            s = new TranslationTextComponent("tabletop.notifyReset").getString();
            offset = mc.fontRenderer.getStringWidth(s)/2;
            mc.fontRenderer.drawStringWithShadow(ms,s,width/2-offset,height/2+7,0xFFFFFF);
        }
    }

    public void darkenScreen(MatrixStack matrixStack){
        this.fillGradient(matrixStack, 0, 0, this.width, this.height, -1072689136, -804253680);
    }

    public void drawBoardAndRelated(MatrixStack ms, int middleX, int middleY, int x, int y) {
        blit(ms, middleX, middleY, 0, 0, 320, 256, 512, 512);
        if(tile.playerBlack.equals(mc.player.getName().getString())) {
            if(currentPieceMoves != null) {
                for(Move m:currentPieceMoves) {
                    blit(ms,x+26*(7-m.newColumn),y-26*(7-m.newRow),346,0,26,26,512,512);
                }
            }
            for(int i=7;i>=0;i--) {
                for(int j=7;j>=0;j--) {
                    AbstractChessPiece piece = gameEngineBoard.grid[i][j];
                    if(piece!=null) {
                        boolean white = piece.pieceColor.isWhite();
                        int ordinal = piece.getType().ordinal();
                        if(currentlySelected != null && currentlySelected.row == piece.row && currentlySelected.column == piece.column) {
                            blit(ms,x,y,320,0,26,26,512,512);
                        }
                        if(piece.getType() == PieceType.KING && piece.pieceColor == checkedColor)
                            blit(ms,x,y,398,0,26,26,512,512);
                        blit(ms, x, y, ordinal * 26, 256 + (white?26:0), 26, 26, 512, 512);
                    }
                    x+=26;
                }
                x=width/2-104;
                y-=26;
            }
            String name = currentTurn.isWhite()?tile.playerWhite:tile.playerBlack;
            String text = new TranslationTextComponent("tabletop.currentTurn",new Object[]{name}).getString();
            int offset = mc.fontRenderer.getStringWidth(text)/2;
            mc.fontRenderer.drawStringWithShadow(ms,text,middleX+128-offset,middleY+14,0xFFFFFF);
            return;
        }
        if(currentPieceMoves != null) {
            for(Move m:currentPieceMoves) {
                blit(ms,x+26*m.newColumn,y-26*m.newRow,346,0,26,26,512,512);
            }
        }
        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                AbstractChessPiece piece = gameEngineBoard.grid[i][j];
                if(piece!=null) {
                    boolean white = piece.pieceColor.isWhite();
                    int ordinal = piece.getType().ordinal();
                    if(currentlySelected != null && currentlySelected.row == piece.row && currentlySelected.column == piece.column) {
                        blit(ms,x,y,320,0,26,26,512,512);
                    }
                    if(piece.getType() == PieceType.KING && piece.pieceColor == checkedColor)
                        blit(ms,x,y,398,0,26,26,512,512);
                    blit(ms, x, y, ordinal * 26, 256 + (white?26:0), 26, 26, 512, 512);
                }
                x+=26;
            }
            x=width/2-104;
            y-=26;
        }
        String name = currentTurn.isWhite()?tile.playerWhite:tile.playerBlack;
        String text = new TranslationTextComponent("tabletop.currentTurn",new Object[]{name}).getString();
        int offset = mc.fontRenderer.getStringWidth(text)/2;
        mc.fontRenderer.drawStringWithShadow(ms,text,middleX+128-offset,middleY+14,0xFFFFFF);
    }

    public void drawPreScreen(MatrixStack ms) {
        int middleX = width/2-90;
        int middleY = height/2-60;
        blit(ms,middleX,middleY,0,392,180,120,512,512);
        blit(ms,middleX+15,middleY+65,180,472,150,40,512,512);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!gameStarted)
            return super.mouseClicked(mouseX,mouseY,button);
        if(currentTurn.isWhite() && !mc.player.getName().getString().equals(tile.playerWhite))
            return false;
        if(!currentTurn.isWhite() && !mc.player.getName().getString().equals(tile.playerBlack))
            return false;
        return super.mouseClicked(mouseX,mouseY,button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
