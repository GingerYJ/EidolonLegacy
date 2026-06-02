package elucent.eidolon.registries;

import elucent.eidolon.Reference;
import elucent.eidolon.potion.UndeathPotion;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class ModPotions {
    public static final Potion UNDEATH = setup(new UndeathPotion(), "undeath");

    private ModPotions() {
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(UNDEATH);
    }

    private static Potion setup(Potion potion, String name) {
        potion.setRegistryName(Reference.MOD_ID, name);
        potion.setPotionName("potion." + Reference.MOD_ID + "." + name);
        return potion;
    }
}
