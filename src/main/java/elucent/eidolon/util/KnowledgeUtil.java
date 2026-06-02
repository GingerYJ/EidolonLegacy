package elucent.eidolon.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public final class KnowledgeUtil {
    private static final String ROOT = "eidolonKnownResearch";

    private KnowledgeUtil() {
    }

    public static boolean knowsResearch(EntityPlayer player, ResourceLocation research) {
        return getResearchTag(player).getBoolean(research.toString());
    }

    public static void grantResearch(EntityPlayer player, ResourceLocation research) {
        getResearchTag(player).setBoolean(research.toString(), true);
    }

    public static void removeResearch(EntityPlayer player, ResourceLocation research) {
        getResearchTag(player).removeTag(research.toString());
    }

    public static void setResearchKnown(EntityPlayer player, ResourceLocation research, boolean known) {
        if (known) {
            grantResearch(player, research);
        } else {
            removeResearch(player, research);
        }
    }

    public static void clearResearch(EntityPlayer player) {
        getPersistedTag(player).removeTag(ROOT);
    }

    public static Set<String> getKnownResearchIds(EntityPlayer player) {
        return getResearchTag(player).getKeySet();
    }

    private static NBTTagCompound getResearchTag(EntityPlayer player) {
        NBTTagCompound persisted = getPersistedTag(player);
        if (!persisted.hasKey(ROOT)) {
            persisted.setTag(ROOT, new NBTTagCompound());
        }
        return persisted.getCompoundTag(ROOT);
    }

    private static NBTTagCompound getPersistedTag(EntityPlayer player) {
        NBTTagCompound entityData = player.getEntityData();
        if (!entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }
        return entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    }
}
