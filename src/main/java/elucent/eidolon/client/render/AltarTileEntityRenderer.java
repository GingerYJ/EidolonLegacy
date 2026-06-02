package elucent.eidolon.client.render;

import elucent.eidolon.tile.AltarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class AltarTileEntityRenderer extends TileEntitySpecialRenderer<AltarTileEntity> {
    @Override
    public void render(AltarTileEntity te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = te.getOffering();
        if (stack.isEmpty()) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 1.02D, z + 0.5D);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.45F, 0.45F, 0.45F);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
