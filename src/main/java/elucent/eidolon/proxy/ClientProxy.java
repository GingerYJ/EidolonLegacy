package elucent.eidolon.proxy;

import elucent.eidolon.client.render.AltarTileEntityRenderer;
import elucent.eidolon.client.render.BrazierTileEntityRenderer;
import elucent.eidolon.client.render.CrucibleTileEntityRenderer;
import elucent.eidolon.client.render.InvisibleEntityRenderer;
import elucent.eidolon.client.render.ItemHolderTileEntityRenderer;
import elucent.eidolon.client.render.NecroticFocusTileEntityRenderer;
import elucent.eidolon.client.render.OffertoryPlateTileEntityRenderer;
import elucent.eidolon.entity.BonechillProjectileEntity;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.tile.AltarTileEntity;
import elucent.eidolon.tile.BrazierTileEntity;
import elucent.eidolon.tile.CrucibleTileEntity;
import elucent.eidolon.tile.NecroticFocusTileEntity;
import elucent.eidolon.tile.OffertoryPlateTileEntity;
import elucent.eidolon.tile.StoneHandTileEntity;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ClientRegistry.bindTileEntitySpecialRenderer(CrucibleTileEntity.class, new CrucibleTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(AltarTileEntity.class, new AltarTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(BrazierTileEntity.class, new BrazierTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(StoneHandTileEntity.class, new ItemHolderTileEntityRenderer<>());
        ClientRegistry.bindTileEntitySpecialRenderer(NecroticFocusTileEntity.class, new NecroticFocusTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(OffertoryPlateTileEntity.class, new OffertoryPlateTileEntityRenderer());
        RenderingRegistry.registerEntityRenderingHandler(SoulfireProjectileEntity.class, InvisibleEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(BonechillProjectileEntity.class, InvisibleEntityRenderer::new);
    }

    @Override
    public void syncKnownResearchClient(ResourceLocation research, boolean known) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (Minecraft.getMinecraft().player != null) {
                KnowledgeUtil.setResearchKnown(Minecraft.getMinecraft().player, research, known);
            }
        });
    }
}
