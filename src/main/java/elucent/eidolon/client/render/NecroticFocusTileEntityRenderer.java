package elucent.eidolon.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import elucent.eidolon.tile.NecroticFocusTileEntity;

public class NecroticFocusTileEntityRenderer extends TileEntitySpecialRenderer<NecroticFocusTileEntity> {
    @Override
    public void render(NecroticFocusTileEntity te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = te.getRenderStack();
        if (stack.isEmpty()) {
            return;
        }

        EnumFacing facing = EnumFacing.NORTH;
        if (te.getWorld() != null) {
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            for (IProperty<?> property : state.getProperties().keySet()) {
                if ("facing".equals(property.getName())) {
                    Object value = state.getValue(property);
                    if (value instanceof EnumFacing) {
                        facing = (EnumFacing) value;
                    }
                    break;
                }
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D + facing.getXOffset() * 0.16D, y + 0.5D, z + 0.5D + facing.getZOffset() * 0.16D);
        long time = Minecraft.getMinecraft().world == null ? 0L : Minecraft.getMinecraft().world.getTotalWorldTime();
        GlStateManager.rotate((time + partialTicks) * 1.2F, 0.0F, 1.0F, 0.0F);
        float consumeScale = 1.0F - te.getConsumeEffectProgress(partialTicks) * 0.75F;
        GlStateManager.scale(0.45F * consumeScale, 0.45F * consumeScale, 0.45F * consumeScale);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
