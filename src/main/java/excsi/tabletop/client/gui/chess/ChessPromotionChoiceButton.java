package excsi.tabletop.client.gui.chess;

import com.mojang.blaze3d.matrix.MatrixStack;
import excsi.tabletop.common.util.AssetLib;
import excsi.tabletop.gameengines.chess.PieceColor;
import net.minecraft.client.Minecraft;

public class ChessPromotionChoiceButton extends ChessTileButton {

    public ChessPromotionChoiceButton(int x, int y, IPressable press, int rowIndex, int columnIndex, ChessMainScreen screen) {
        super(x, y, press, rowIndex, columnIndex,screen);
    }

    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
        if(!(parentScreen.currentTurn == parentScreen.promotionLockedColor))
            return;
        Minecraft.getInstance().getTextureManager().bindTexture(AssetLib.TEXTURE);
        boolean white = parentScreen.promotionLockedColor== PieceColor.WHITE;
        blit(matrixStack,this.x,this.y,(this.rowIndex + 1)*26,256+(white?26:0),26,26,512,512);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(parentScreen.currentTurn != parentScreen.promotionLockedColor || parentScreen.gameOver)
            return false;
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean flag = this.clicked(mouseX, mouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundHandler());
                    this.onClick(mouseX, mouseY);
                    return true;
                }
            }
        }
        return false;
    }
}
