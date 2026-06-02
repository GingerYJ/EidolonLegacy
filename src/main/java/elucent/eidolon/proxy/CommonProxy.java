package elucent.eidolon.proxy;

import elucent.eidolon.Eidolon;
import elucent.eidolon.gui.ModGuiHandler;
import elucent.eidolon.item.NotetakingToolsItem;
import elucent.eidolon.network.ModNetwork;
import elucent.eidolon.recipes.CrucibleRecipes;
import elucent.eidolon.recipes.WorktableRecipes;
import elucent.eidolon.research.Researches;
import elucent.eidolon.registries.ModEntities;
import elucent.eidolon.registries.ModRecipes;
import elucent.eidolon.registries.ModOreDictionary;
import elucent.eidolon.registries.ModTileEntities;
import elucent.eidolon.spell.AltarEntries;
import elucent.eidolon.spell.AltarRituals;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModEntities.init();
        ModTileEntities.init();
        ModNetwork.init();
        Researches.init();
        MinecraftForge.EVENT_BUS.register(NotetakingToolsItem.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(Eidolon.instance, new ModGuiHandler());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ModOreDictionary.init();
        ModRecipes.init();
        WorktableRecipes.init();
        CrucibleRecipes.init();
        AltarEntries.init();
        AltarRituals.init();
    }
}
