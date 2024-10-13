package excsi.tabletop.client.gui.chess;

import com.mojang.blaze3d.matrix.MatrixStack;
import excsi.tabletop.common.util.AssetLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class ChessTileButton extends Button {

    public int rowIndex, columnIndex;

    public ChessMainScreen parentScreen;

    public ChessTileButton(int x, int y, IPressable press, int row, int column, ChessMainScreen screen) {
        super(x, y, 26, 26, new StringTextComponent(""), press);
        parentScreen = screen;
        rowIndex = row;
        columnIndex = column;
    }

    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
        if(parentScreen.currentTurn==parentScreen.promotionLockedColor || parentScreen.gameOver || !parentScreen.gameStarted)
            return;
        Minecraft.getInstance().getTextureManager().bindTexture(AssetLib.TEXTURE);
        if (this.isHovered()) {
            blit(matrixStack, this.x, this.y, 372, 0, 26, 26, 512, 512);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!parentScreen.gameStarted)
            return false;
        if(parentScreen.currentTurn == parentScreen.promotionLockedColor || parentScreen.gameOver)
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
