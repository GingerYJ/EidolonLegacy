package elucent.eidolon.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class InvisibleEntityRenderer<T extends Entity> extends Render<T> {
    public InvisibleEntityRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}
