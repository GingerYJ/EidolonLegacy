package elucent.eidolon.compat.jei;

import elucent.eidolon.registries.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SoulShardHarvestRecipe {
    public static final SoulShardHarvestRecipe INSTANCE = new SoulShardHarvestRecipe();

    private SoulShardHarvestRecipe() {
    }

    public List<ItemStack> getSources() {
        return Arrays.asList(
                spawnEgg("minecraft:zombie"),
                spawnEgg("minecraft:skeleton"),
                spawnEgg("minecraft:husk"),
                spawnEgg("minecraft:stray"),
                spawnEgg("minecraft:wither_skeleton")
        );
    }

    public ItemStack getTool() {
        return new ItemStack(ModItems.REAPER_SCYTHE);
    }

    public ItemStack getResult() {
        return new ItemStack(ModItems.SOUL_SHARD);
    }

    public static List<SoulShardHarvestRecipe> getRecipes() {
        return Collections.singletonList(INSTANCE);
    }

    private static ItemStack spawnEgg(String entityId) {
        ItemStack stack = new ItemStack(Items.SPAWN_EGG);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("id", entityId);
        tag.setTag("EntityTag", entityTag);
        stack.setTagCompound(tag);
        return stack;
    }
}
