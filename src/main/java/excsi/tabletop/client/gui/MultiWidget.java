package excsi.tabletop.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public abstract class MultiWidget extends Button {

    public ArrayList<Button> subWidgets = new ArrayList<>();

    public Minecraft mc;

    public MultiWidget(int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent(""), (b) -> {});
        mc = Minecraft.getInstance();
        setupSubWidgets();
    }

    public abstract void setupSubWidgets();

    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        for(Button b:subWidgets) b.renderWidget(matrixStack,mouseX,mouseY,partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(Button b : subWidgets) {
            if(isValidClickButton(button) && b.isMouseOver(mouseX,mouseY)) {
                return b.mouseClicked(mouseX,mouseY,button);
            }
        }
        return false;
    }
}
