package elucent.eidolon.spell;

import elucent.eidolon.Reference;
import elucent.eidolon.registries.ModBlocks;
import elucent.eidolon.registries.ModItems;
import net.minecraft.init.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AltarEntries {
    private static final ResourceLocation LIGHT = new ResourceLocation(Reference.MOD_ID, "light");
    private static final ResourceLocation SKULL = new ResourceLocation(Reference.MOD_ID, "skull");
    private static final ResourceLocation GOBLET = new ResourceLocation(Reference.MOD_ID, "goblet");
    private static final ResourceLocation ESSENCE = new ResourceLocation(Reference.MOD_ID, "essence");
    private static final Map<Item, AltarEntry> ENTRIES = new LinkedHashMap<>();

    private AltarEntries() {
    }

    public static void init() {
        ENTRIES.clear();
        register(Item.getItemFromBlock(ModBlocks.CANDLE), LIGHT, 0.0D, 2.0D);
        register(Item.getItemFromBlock(ModBlocks.CANDLESTICK), LIGHT, 0.0D, 2.0D);
        register(Item.getItemFromBlock(ModBlocks.MAGIC_CANDLE), LIGHT, 1.0D, 2.0D);
        register(Item.getItemFromBlock(ModBlocks.MAGIC_CANDLESTICK), LIGHT, 1.0D, 2.0D);
        register(Item.getItemFromBlock(ModBlocks.GOBLET), GOBLET, 2.0D, 0.0D);
        register(ModItems.SOUL_SHARD, ESSENCE, 1.0D, 1.0D);
        register(ModItems.DEATH_ESSENCE, ESSENCE, 2.0D, 1.0D);
        register(ModItems.CRIMSON_ESSENCE, ESSENCE, 1.0D, 2.0D);
        register(ModItems.WITHERED_HEART, SKULL, 3.0D, 1.0D);
        register(ModItems.ZOMBIE_HEART, SKULL, 1.0D, 1.0D);
        register(Items.SKULL, SKULL, 2.0D, 0.0D);
    }

    public static AltarEntry find(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return find(stack.getItem());
    }

    public static AltarEntry findPlateOffering(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof net.minecraft.item.ItemBlock) {
            return null;
        }
        return find(stack.getItem());
    }

    public static AltarEntry find(IBlockState state) {
        return find(Item.getItemFromBlock(state.getBlock()));
    }

    private static AltarEntry find(Item item) {
        return ENTRIES.get(item);
    }

    public static Map<Item, AltarEntry> getEntries() {
        return Collections.unmodifiableMap(ENTRIES);
    }

    private static void register(Item item, ResourceLocation key, double capacity, double power) {
        if (item != null) {
            ENTRIES.put(item, new AltarEntry(key, capacity, power));
        }
    }
}
