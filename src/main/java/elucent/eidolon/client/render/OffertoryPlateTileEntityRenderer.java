package elucent.eidolon.client.render;

import elucent.eidolon.tile.OffertoryPlateTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class OffertoryPlateTileEntityRenderer extends TileEntitySpecialRenderer<OffertoryPlateTileEntity> {
    @Override
    public void render(OffertoryPlateTileEntity te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = te.getRenderStack();
        if (stack.isEmpty()) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.18D, z + 0.5D);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        float consumeScale = 1.0F - te.getConsumeEffectProgress(partialTicks) * 0.75F;
        GlStateManager.scale(0.38F * consumeScale, 0.38F * consumeScale, 0.38F * consumeScale);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
