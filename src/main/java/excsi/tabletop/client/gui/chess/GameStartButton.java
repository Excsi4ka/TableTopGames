package excsi.tabletop.client.gui.chess;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import excsi.tabletop.client.gui.MultiWidget;
import excsi.tabletop.common.util.AssetLib;
import excsi.tabletop.gameengines.chess.PieceColor;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GameStartButton extends MultiWidget {

    public ChessMainScreen parentScreen;

    public PieceColor chosenColor = PieceColor.WHITE;

    public GameStartButton(int x, int y, int width, int height, ChessMainScreen screen) {
        super(x, y, width, height);
        parentScreen =  screen;
    }

    @Override
    public void setupSubWidgets() {
        subWidgets.add(new Button(x,y,20,20,new StringTextComponent(""),b -> chosenColor = PieceColor.WHITE) {

            @Override
            public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                mc.getTextureManager().bindTexture(AssetLib.TEXTURE);
                blit(matrixStack,this.x,this.y,0,372,20,20,512,512);
                blit(matrixStack,this.x+3,this.y+2,89,376,14,16,512,512);
                String[] queue = parentScreen.tile.playerQueue.split(";");
                if(!queue[0].equals(" ")) {
                    RenderSystem.enableBlend();
                    RenderSystem.enableAlphaTest();
                    RenderSystem.color4f(1f, 1f, 1f, 0.7f);
                    blit(matrixStack,this.x,this.y,0,352,20,20,512,512);
                    RenderSystem.disableBlend();
                    RenderSystem.disableAlphaTest();
                    if(queue[0].equals(mc.player.getName().getString()))
                        blit(matrixStack,this.x,this.y,20,352,20,20,512,512);
                    return;
                }
                if(this.isMouseOver(mouseX,mouseY))
                    parentScreen.renderTooltip(matrixStack,new TranslationTextComponent("tabletop.chooseWhite"),mouseX,mouseY);
            }
        });
        subWidgets.add(new Button(x+22,y,20,20,new StringTextComponent(""),b -> chosenColor = PieceColor.BLACK) {

            @Override
            public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                mc.getTextureManager().bindTexture(AssetLib.TEXTURE);
                blit(matrixStack,this.x,this.y,0,372,20,20,512,512);
                blit(matrixStack,this.x+3,this.y+2,103,376,14,16,512,512);
                String[] queue = parentScreen.tile.playerQueue.split(";");
                if(!queue[1].equals(" ")) {
                    RenderSystem.enableBlend();
                    RenderSystem.enableAlphaTest();
                    RenderSystem.color4f(1f, 1f, 1f, 0.7f);
                    blit(matrixStack,this.x,this.y,0,352,20,20,512,512);
                    RenderSystem.disableBlend();
                    RenderSystem.disableAlphaTest();
                    if(queue[1].equals(mc.player.getName().getString()))
                        blit(matrixStack,this.x,this.y,20,352,20,20,512,512);
                    return;
                }
                if(this.isMouseOver(mouseX,mouseY))
                    parentScreen.renderTooltip(matrixStack,new TranslationTextComponent("tabletop.chooseBlack"),mouseX,mouseY);
            }
        });
        subWidgets.add(new Button(x+44,y,20,20,new StringTextComponent(""),b -> parentScreen.gameEngineBoard.sendReady(chosenColor.ordinal())) {

            @Override
            public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                mc.getTextureManager().bindTexture(AssetLib.TEXTURE);
                String name = mc.player.getName().getString();
                String[] queue = parentScreen.tile.playerQueue.split(";");
                boolean ready = false;
                for (String s:queue) {
                    if(s.equals(name)){
                        ready = true;
                        break;
                    }
                }
                if(ready)
                    blit(matrixStack,this.x,this.y,54,372,20,20,512,512);
                else
                    blit(matrixStack,this.x,this.y,34,372,20,20,512,512);
                if(this.isMouseOver(mouseX,mouseY) && ready)
                    parentScreen.renderTooltip(matrixStack,new TranslationTextComponent("tabletop.Ready"),mouseX,mouseY);
                else if (this.isMouseOver(mouseX,mouseY) && !ready)
                    parentScreen.renderTooltip(matrixStack,new TranslationTextComponent("tabletop.notReady"),mouseX,mouseY);
            }
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(parentScreen.gameStarted)
            return false;
        return super.mouseClicked(mouseX,mouseY,button);
    }

    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(parentScreen.gameStarted)
            return;
        mc.getTextureManager().bindTexture(AssetLib.TEXTURE);
        blit(matrixStack,x-60,y-10,180,472,150,40,512,512);
        mc.fontRenderer.drawStringWithShadow(matrixStack,mc.player.getName().getString(),x-55,y+6,0xFFFFFF);
        int offset = mc.fontRenderer.getStringWidth("VS")/2;
        mc.fontRenderer.drawStringWithShadow(matrixStack,"VS",x+15-offset,y+32,0xFFFFFF);
        super.renderWidget(matrixStack,mouseX,mouseY,partialTicks);
        String[] queue = parentScreen.tile.playerQueue.split(";");
        boolean hasOtherPlayerInQueue = false;
        String opponentName = "";
        int i = 0;
        for(String name : queue) {
            if(!name.equals(mc.player.getName().getString()) && !name.equals(" ")) {
                hasOtherPlayerInQueue = true;
                opponentName = name;
                break;
            }
            i++;
        }
        if(!hasOtherPlayerInQueue)
            return;
        mc.fontRenderer.drawStringWithShadow(matrixStack,opponentName,x-55,y+56,0xFFFFFF);
        mc.getTextureManager().bindTexture(AssetLib.TEXTURE);
        blit(matrixStack,x,y+50,0,372,20,20,512,512);
        blit(matrixStack,x+3,y+52,89+14*i,376,14,16,512,512);
    }
}
