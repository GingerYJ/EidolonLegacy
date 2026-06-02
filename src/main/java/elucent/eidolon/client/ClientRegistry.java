package elucent.eidolon.client;

import elucent.eidolon.Reference;
import elucent.eidolon.registries.ModBlocks;
import elucent.eidolon.registries.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
public final class ClientRegistry {
    private ClientRegistry() {
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerModels(
                ModItems.TEST_SIGIL,
                ModItems.LEAD_INGOT,
                ModItems.RAW_LEAD,
                ModItems.LEAD_NUGGET,
                ModItems.SILVER_INGOT,
                ModItems.RAW_SILVER,
                ModItems.SILVER_NUGGET,
                ModItems.PEWTER_BLEND,
                ModItems.PEWTER_INGOT,
                ModItems.PEWTER_NUGGET,
                ModItems.PEWTER_INLAY,
                ModItems.ARCANE_GOLD_INGOT,
                ModItems.ARCANE_GOLD_NUGGET,
                ModItems.SHADOW_GEM,
                ModItems.ELDER_BRICK,
                ModItems.SULFUR,
                ModItems.GOLD_INLAY,
                ModItems.ZOMBIE_HEART,
                ModItems.WRAITH_HEART,
                ModItems.TATTERED_CLOTH,
                ModItems.SOUL_SHARD,
                ModItems.DEATH_ESSENCE,
                ModItems.CRIMSON_ESSENCE,
                ModItems.ENDER_CALX,
                ModItems.TALLOW,
                ModItems.LESSER_SOUL_GEM,
                ModItems.WICKED_WEAVE,
                ModItems.WITHERED_HEART,
                ModItems.IMBUED_BONES,
                ModItems.RAVEN_FEATHER,
                ModItems.MERAMMER_RESIN,
                ModItems.MAGIC_INK,
                ModItems.MAGICIANS_WAX,
                ModItems.ARCANE_SEAL,
                ModItems.PARCHMENT,
                ModItems.NOTETAKING_TOOLS,
                ModItems.RESEARCH_NOTES,
                ModItems.COMPLETED_RESEARCH,
                ModItems.OFFERTORY_PLATE,
                ModItems.GOLD_OFFERTORY_PLATE,
                ModItems.PEWTER_OFFERTORY_PLATE,
                ModItems.RED_CANDY,
                ModItems.GRAPE_CANDY,
                ModItems.CODEX,
                ModItems.ARCHIVE,
                ModItems.SCRIPTORIUM,
                ModItems.CABINET,
                ModItems.WOODEN_PODIUM,
                ModItems.FUNGUS_SPROUTS,
                ModItems.WARPED_SPROUTS,
                ModItems.MIRECAP,
                ModItems.GLASS_HAND,
                ModItems.UNHOLY_SYMBOL,
                ModItems.SILVER_SWORD,
                ModItems.SILVER_PICKAXE,
                ModItems.SILVER_AXE,
                ModItems.SILVER_SHOVEL,
                ModItems.SILVER_HOE,
                ModItems.SILVER_HELMET,
                ModItems.SILVER_CHESTPLATE,
                ModItems.SILVER_LEGGINGS,
                ModItems.SILVER_BOOTS,
                ModItems.ATHAME,
                ModItems.CLEAVING_AXE,
                ModItems.REVERSAL_PICK,
                ModItems.REAPER_SCYTHE,
                ModItems.DEATHBRINGER_SCYTHE,
                ModItems.SAPPING_SWORD,
                ModItems.BASIC_AMULET,
                ModItems.SANGUINE_AMULET,
                ModItems.VOID_AMULET,
                ModItems.SOULBONE_AMULET,
                ModItems.BASIC_RING,
                ModItems.ENERVATING_RING,
                ModItems.BASIC_BELT,
                ModItems.GRAVITY_BELT,
                ModItems.RESOLUTE_BELT,
                ModItems.MIND_SHIELDING_PLATE,
                ModItems.SOULFIRE_WAND,
                ModItems.BONECHILL_WAND,
                ModItems.SUMMONING_STAFF,
                ModItems.ALCHEMISTS_TONGS,
                ModItems.ANGELS_SIGHT,
                ModItems.TERMINUS_MIRROR,
                ModItems.PRESTIGIOUS_PALM,
                ModItems.COALFIRED_ENGINE,
                ModItems.WARLOCK_HAT,
                ModItems.WARLOCK_CLOAK,
                ModItems.WARLOCK_BOOTS,
                ModItems.BONELORD_HELM,
                ModItems.BONELORD_CHESTPLATE,
                ModItems.BONELORD_GREAVES,
                ModItems.RAVEN_CLOAK,
                ModItems.TOP_HAT,
                ModItems.WARDED_MAIL,
                ModItems.MUSIC_DISC_PAROUSIA,
                ModItems.SPAWN_WRAITH,
                ModItems.SPAWN_ZOMBIE_BRUTE,
                ModItems.SPAWN_NECROMANCER,
                ModItems.SPAWN_SLIMY_SLUG,
                ModItems.SPAWN_RAVEN
        );
        for (Item item : ModBlocks.ITEM_BLOCKS) {
            registerModel(item);
        }
        registerModel(ModBlocks.SMOOTH_STONE_BRICKS_WALL_ITEM, 1);
        registerModel(ModBlocks.ELDER_BRICKS_WALL_ITEM, 1);
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> tintIndex == 0 ? 0x1f2430 : 0x8cc7d8,
                ModItems.SPAWN_WRAITH
        );
        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> tintIndex == 0 ? 0x35512d : 0x8a5d45,
                ModItems.SPAWN_ZOMBIE_BRUTE
        );
        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> tintIndex == 0 ? 0x2d2238 : 0xb8d0c8,
                ModItems.SPAWN_NECROMANCER
        );
        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> tintIndex == 0 ? 0x4f7d48 : 0xb4d86f,
                ModItems.SPAWN_SLIMY_SLUG
        );
        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> tintIndex == 0 ? 0x10131b : 0x4e6078,
                ModItems.SPAWN_RAVEN
        );
    }

    private static void registerModels(Item... items) {
        for (Item item : items) {
            registerModel(item);
        }
    }

    private static void registerModel(Item item) {
        registerModel(item, 0);
    }

    private static void registerModel(Item item, int metadata) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                metadata,
                new ModelResourceLocation(item.getRegistryName(), "inventory")
        );
    }
}
