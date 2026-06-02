package elucent.eidolon;

import elucent.eidolon.command.ResearchCommand;
import elucent.eidolon.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class Eidolon {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    @Mod.Instance(Reference.MOD_ID)
    public static Eidolon instance;

    @SidedProxy(modId = Reference.MOD_ID, clientSide = "elucent.eidolon.proxy.ClientProxy", serverSide = "elucent.eidolon.proxy.CommonProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("{} preInit", Reference.MOD_NAME);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("{} init", Reference.MOD_NAME);
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("{} postInit", Reference.MOD_NAME);
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ResearchCommand());
    }

}
