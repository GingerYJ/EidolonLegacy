package elucent.eidolon.item.curio;

import elucent.eidolon.Eidolon;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

/**
 * Keeps optional Baubles item classes behind a class-loading boundary.
 *
 * The item classes implement the official Baubles API and must not be loaded
 * when Baubles is absent, otherwise their missing interface would prevent the
 * base mod from starting.
 */
public final class BaubleItemFactory {
    private static final String BAUBLES_MOD_ID = "baubles";
    private static final String ITEM_PACKAGE = "elucent.eidolon.item.curio.";

    private BaubleItemFactory() {
    }

    public static boolean isBaublesLoaded() {
        if (!Loader.isModLoaded(BAUBLES_MOD_ID)) {
            return false;
        }
        try {
            Class.forName("baubles.api.BaublesApi", false, BaubleItemFactory.class.getClassLoader());
            return true;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            return false;
        }
    }

    public static Item create(String className) {
        if (!isBaublesLoaded()) {
            return fallbackItem();
        }
        try {
            Class<?> itemClass = Class.forName(ITEM_PACKAGE + className, true, BaubleItemFactory.class.getClassLoader());
            Object item = itemClass.getDeclaredConstructor().newInstance();
            if (item instanceof Item) {
                return (Item) item;
            }
            throw new IllegalStateException(itemClass.getName() + " is not an Item");
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            Eidolon.LOGGER.error("Unable to load optional Baubles item {}", className, e);
            return fallbackItem();
        }
    }

    private static Item fallbackItem() {
        Item item = new Item();
        item.setMaxStackSize(1);
        return item;
    }
}
