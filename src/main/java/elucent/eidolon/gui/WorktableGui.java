package elucent.eidolon.gui;

import elucent.eidolon.Reference;
import elucent.eidolon.tile.WorktableTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class WorktableGui extends GuiContainer {
    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(Reference.MOD_ID, "textures/gui/worktable.png");

    public WorktableGui(InventoryPlayer playerInventory, WorktableTileEntity tile) {
        super(new WorktableContainer(playerInventory, tile));
        xSize = 192;
        ySize = 224;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(BACKGROUND);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }
}
