package elucent.eidolon.registries;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Reference;
import elucent.eidolon.entity.BonechillProjectileEntity;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public final class ModEntities {
    private static int nextId = 0;

    private ModEntities() {
    }

    public static void init() {
        register("soulfire_projectile", SoulfireProjectileEntity.class, 64, 10, true);
        register("bonechill_projectile", BonechillProjectileEntity.class, 64, 10, true);
    }

    private static void register(String name, Class entityClass, int trackingRange, int updateFrequency, boolean sendVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, name), entityClass, name,
                nextId++, Eidolon.instance, trackingRange, updateFrequency, sendVelocityUpdates);
    }
}
