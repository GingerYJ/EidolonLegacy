package elucent.eidolon.client.render;

import elucent.eidolon.tile.ItemHolderTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class ItemHolderTileEntityRenderer<T extends ItemHolderTileEntity> extends TileEntitySpecialRenderer<T> {
    @Override
    public void render(T te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = te.getRenderStack();
        if (stack.isEmpty()) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.86D, z + 0.5D);
        long time = Minecraft.getMinecraft().world == null ? 0L : Minecraft.getMinecraft().world.getTotalWorldTime();
        GlStateManager.rotate((time + partialTicks) * 1.2F, 0.0F, 1.0F, 0.0F);
        float consumeScale = 1.0F - te.getConsumeEffectProgress(partialTicks) * 0.75F;
        GlStateManager.scale(0.45F * consumeScale, 0.45F * consumeScale, 0.45F * consumeScale);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
