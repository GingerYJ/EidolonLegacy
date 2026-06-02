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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AltarEntries {
    private static final ResourceLocation LIGHT = new ResourceLocation(Reference.MOD_ID, "light");
    private static final ResourceLocation SKULL = new ResourceLocation(Reference.MOD_ID, "skull");
    private static final ResourceLocation GOBLET = new ResourceLocation(Reference.MOD_ID, "goblet");
    private static final ResourceLocation ESSENCE = new ResourceLocation(Reference.MOD_ID, "essence");
    private static final Map<Item, AltarEntry> ENTRIES = new LinkedHashMap<>();
    private static final List<Runnable> CUSTOMIZATIONS = new ArrayList<>();
    private static boolean initialized;

    private AltarEntries() {
    }

    public static void init() {
        initialized = false;
        ENTRIES.clear();
        registerBlockOffering(Item.getItemFromBlock(ModBlocks.CANDLE), LIGHT, 0.0D, 2.0D);
        registerBlockOffering(Item.getItemFromBlock(ModBlocks.CANDLESTICK), LIGHT, 0.0D, 2.0D);
        registerBlockOffering(Item.getItemFromBlock(ModBlocks.MAGIC_CANDLE), LIGHT, 1.0D, 2.0D);
        registerBlockOffering(Item.getItemFromBlock(ModBlocks.MAGIC_CANDLESTICK), LIGHT, 1.0D, 2.0D);
        registerBlockOffering(Item.getItemFromBlock(ModBlocks.GOBLET), GOBLET, 2.0D, 0.0D);
        registerPlateOffering(ModItems.SOUL_SHARD, ESSENCE, 1.0D, 1.0D);
        registerPlateOffering(ModItems.DEATH_ESSENCE, ESSENCE, 2.0D, 1.0D);
        registerPlateOffering(ModItems.CRIMSON_ESSENCE, ESSENCE, 1.0D, 2.0D);
        registerPlateOffering(ModItems.WITHERED_HEART, SKULL, 3.0D, 1.0D);
        registerPlateOffering(ModItems.ZOMBIE_HEART, SKULL, 1.0D, 1.0D);
        register(Items.SKULL, SKULL, 2.0D, 0.0D, true, true);
        initialized = true;
        applyCustomizations();
    }

    public static AltarEntry find(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return find(stack.getItem());
    }

    public static AltarEntry findPlateOffering(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        AltarEntry entry = find(stack.getItem());
        return entry != null && entry.canApplyFromPlate() ? entry : null;
    }

    public static AltarEntry find(IBlockState state) {
        AltarEntry entry = find(Item.getItemFromBlock(state.getBlock()));
        return entry != null && entry.canApplyFromBlock() ? entry : null;
    }

    private static AltarEntry find(Item item) {
        return ENTRIES.get(item);
    }

    public static Map<Item, AltarEntry> getEntries() {
        return Collections.unmodifiableMap(ENTRIES);
    }

    public static void addEntry(Item item, ResourceLocation key, double capacity, double power,
                                boolean blockOffering, boolean plateOffering) {
        addCustomization(() -> register(item, key, capacity, power, blockOffering, plateOffering));
    }

    public static boolean removeEntry(ResourceLocation key) {
        addCustomization(() -> ENTRIES.entrySet().removeIf(entry -> entry.getValue().getKey().equals(key)));
        return initialized && ENTRIES.values().stream().noneMatch(entry -> entry.getKey().equals(key));
    }

    public static boolean removeEntry(Item item) {
        addCustomization(() -> ENTRIES.remove(item));
        return initialized && !ENTRIES.containsKey(item);
    }

    public static int removeAllEntries() {
        int count = ENTRIES.size();
        addCustomization(ENTRIES::clear);
        return count;
    }

    private static void addCustomization(Runnable customization) {
        CUSTOMIZATIONS.add(customization);
        if (initialized) {
            customization.run();
        }
    }

    private static void applyCustomizations() {
        for (Runnable customization : CUSTOMIZATIONS) {
            customization.run();
        }
    }

    private static void registerBlockOffering(Item item, ResourceLocation key, double capacity, double power) {
        register(item, key, capacity, power, true, false);
    }

    private static void registerPlateOffering(Item item, ResourceLocation key, double capacity, double power) {
        register(item, key, capacity, power, false, true);
    }

    private static void register(Item item, ResourceLocation key, double capacity, double power,
                                 boolean blockOffering, boolean plateOffering) {
        if (item != null) {
            ENTRIES.put(item, new AltarEntry(key, capacity, power, blockOffering, plateOffering));
        }
    }
}
