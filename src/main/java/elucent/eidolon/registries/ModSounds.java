package elucent.eidolon.registries;

import elucent.eidolon.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class ModSounds {
    public static final SoundEvent PAROUSIA = sound("parousia");
    public static final SoundEvent CAST_SOULFIRE = sound("cast_soulfire");
    public static final SoundEvent CAST_BONECHILL = sound("cast_bonechill");
    public static final SoundEvent SPLASH_SOULFIRE = sound("splash_soulfire");
    public static final SoundEvent SPLASH_BONECHILL = sound("splash_bonechill");

    private ModSounds() {
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                PAROUSIA,
                CAST_SOULFIRE,
                CAST_BONECHILL,
                SPLASH_SOULFIRE,
                SPLASH_BONECHILL
        );
    }

    private static SoundEvent sound(String name) {
        ResourceLocation location = new ResourceLocation(Reference.MOD_ID, name);
        SoundEvent sound = new SoundEvent(location);
        sound.setRegistryName(location);
        return sound;
    }
}
