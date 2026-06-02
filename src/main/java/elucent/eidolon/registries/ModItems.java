package elucent.eidolon.registries;

import elucent.eidolon.Reference;
import elucent.eidolon.Eidolon;
import elucent.eidolon.gui.ModGuiHandler;
import elucent.eidolon.item.AthameItem;
import elucent.eidolon.item.BonechillWandItem;
import elucent.eidolon.item.CleavingAxeItem;
import elucent.eidolon.item.DeathbringerScytheItem;
import elucent.eidolon.item.NotetakingToolsItem;
import elucent.eidolon.item.ReaperScytheItem;
import elucent.eidolon.item.ReversalPickItem;
import elucent.eidolon.item.SappingSwordItem;
import elucent.eidolon.item.SoulfireWandItem;
import elucent.eidolon.item.SummoningStaffItem;
import elucent.eidolon.item.TongsItem;
import elucent.eidolon.item.WandItem;
import elucent.eidolon.network.KnowledgeSyncPacket;
import elucent.eidolon.network.ModNetwork;
import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class ModItems {
    private static final Item.ToolMaterial SILVER_TOOL =
            EnumHelper.addToolMaterial("EIDOLON_SILVER", 2, 250, 6.0F, 2.0F, 22);
    private static final Item.ToolMaterial MAGIC_TOOL =
            EnumHelper.addToolMaterial("EIDOLON_MAGIC", 3, 1170, 7.0F, 3.0F, 30);
    private static final ItemArmor.ArmorMaterial SILVER_ARMOR =
            EnumHelper.addArmorMaterial("EIDOLON_SILVER", Reference.MOD_ID + ":silver", 15,
                    new int[]{2, 5, 6, 2}, 22, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    private static final ItemArmor.ArmorMaterial WARLOCK_ARMOR =
            EnumHelper.addArmorMaterial("EIDOLON_WARLOCK", Reference.MOD_ID + ":warlock", 8,
                    new int[]{1, 3, 4, 1}, 25, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    private static final ItemArmor.ArmorMaterial BONELORD_ARMOR =
            EnumHelper.addArmorMaterial("EIDOLON_BONELORD", Reference.MOD_ID + ":bonelord", 18,
                    new int[]{2, 6, 7, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    private static final ItemArmor.ArmorMaterial RAVEN_ARMOR =
            EnumHelper.addArmorMaterial("EIDOLON_RAVEN", Reference.MOD_ID + ":raven", 8,
                    new int[]{1, 3, 4, 1}, 25, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    private static final ItemArmor.ArmorMaterial TOP_HAT_ARMOR =
            EnumHelper.addArmorMaterial("EIDOLON_TOP_HAT", Reference.MOD_ID + ":top_hat", 5,
                    new int[]{1, 1, 1, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    private static final ItemArmor.ArmorMaterial WARDED_ARMOR =
            EnumHelper.addArmorMaterial("EIDOLON_WARDED", Reference.MOD_ID + ":warded", 15,
                    new int[]{2, 5, 6, 2}, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

    public static final Item TEST_SIGIL = item("test_sigil");
    public static final Item LEAD_INGOT = item("lead_ingot");
    public static final Item RAW_LEAD = item("raw_lead");
    public static final Item LEAD_NUGGET = item("lead_nugget");
    public static final Item SILVER_INGOT = item("silver_ingot");
    public static final Item RAW_SILVER = item("raw_silver");
    public static final Item SILVER_NUGGET = item("silver_nugget");
    public static final Item PEWTER_BLEND = item("pewter_blend");
    public static final Item PEWTER_INGOT = item("pewter_ingot");
    public static final Item PEWTER_NUGGET = item("pewter_nugget");
    public static final Item PEWTER_INLAY = item("pewter_inlay");
    public static final Item ARCANE_GOLD_INGOT = item("arcane_gold_ingot");
    public static final Item ARCANE_GOLD_NUGGET = item("arcane_gold_nugget");
    public static final Item SHADOW_GEM = item("shadow_gem");
    public static final Item ELDER_BRICK = item("elder_brick");
    public static final Item SULFUR = item("sulfur");
    public static final Item GOLD_INLAY = item("gold_inlay");
    public static final Item ZOMBIE_HEART = item("zombie_heart");
    public static final Item WRAITH_HEART = item("wraith_heart");
    public static final Item TATTERED_CLOTH = item("tattered_cloth");
    public static final Item SOUL_SHARD = item("soul_shard");
    public static final Item DEATH_ESSENCE = item("death_essence");
    public static final Item CRIMSON_ESSENCE = item("crimson_essence");
    public static final Item ENDER_CALX = item("ender_calx");
    public static final Item TALLOW = item("tallow");
    public static final Item LESSER_SOUL_GEM = item("lesser_soul_gem");
    public static final Item WICKED_WEAVE = item("wicked_weave");
    public static final Item WITHERED_HEART = item("withered_heart");
    public static final Item IMBUED_BONES = item("imbued_bones");
    public static final Item RAVEN_FEATHER = item("raven_feather");
    public static final Item MERAMMER_RESIN = item("merammer_resin");
    public static final Item MAGIC_INK = item("magic_ink");
    public static final Item MAGICIANS_WAX = item("magicians_wax");
    public static final Item ARCANE_SEAL = item("arcane_seal");
    public static final Item PARCHMENT = item("parchment");
    public static final Item NOTETAKING_TOOLS = notetakingTools("notetaking_tools");
    public static final Item RESEARCH_NOTES = researchNotes("research_notes");
    public static final Item COMPLETED_RESEARCH = completedResearch("completed_research");
    public static final Item OFFERTORY_PLATE = offertoryPlate("offertory_plate", () -> ModBlocks.OFFERTORY_PLATE_BLOCK);
    public static final Item GOLD_OFFERTORY_PLATE = offertoryPlate("gold_offertory_plate", () -> ModBlocks.GOLD_OFFERTORY_PLATE_BLOCK);
    public static final Item PEWTER_OFFERTORY_PLATE = offertoryPlate("pewter_offertory_plate", () -> ModBlocks.PEWTER_OFFERTORY_PLATE_BLOCK);
    public static final Item RED_CANDY = item("red_candy");
    public static final Item GRAPE_CANDY = item("grape_candy");
    public static final Item CODEX = codex("codex");
    public static final Item ARCHIVE = item("archive");
    public static final Item SCRIPTORIUM = item("scriptorium");
    public static final Item CABINET = item("cabinet");
    public static final Item WOODEN_PODIUM = item("wooden_podium");
    public static final Item FUNGUS_SPROUTS = item("fungus_sprouts");
    public static final Item WARPED_SPROUTS = item("warped_sprouts");
    public static final Item MIRECAP = item("mirecap");
    public static final Item GLASS_HAND = item("glass_hand");
    public static final Item UNHOLY_SYMBOL = item("unholy_symbol");
    public static final Item SILVER_SWORD = sword("silver_sword", SILVER_TOOL);
    public static final Item SILVER_PICKAXE = pickaxe("silver_pickaxe", SILVER_TOOL);
    public static final Item SILVER_AXE = axe("silver_axe", SILVER_TOOL);
    public static final Item SILVER_SHOVEL = shovel("silver_shovel", SILVER_TOOL);
    public static final Item SILVER_HOE = hoe("silver_hoe", SILVER_TOOL);
    public static final Item SILVER_HELMET = armor("silver_helmet", SILVER_ARMOR, EntityEquipmentSlot.HEAD);
    public static final Item SILVER_CHESTPLATE = armor("silver_chestplate", SILVER_ARMOR, EntityEquipmentSlot.CHEST);
    public static final Item SILVER_LEGGINGS = armor("silver_leggings", SILVER_ARMOR, EntityEquipmentSlot.LEGS);
    public static final Item SILVER_BOOTS = armor("silver_boots", SILVER_ARMOR, EntityEquipmentSlot.FEET);
    public static final Item ATHAME = athame("athame", SILVER_TOOL);
    public static final Item CLEAVING_AXE = cleavingAxe("cleaving_axe", SILVER_TOOL);
    public static final Item REVERSAL_PICK = reversalPick("reversal_pick", MAGIC_TOOL);
    public static final Item REAPER_SCYTHE = reaperScythe("reaper_scythe", SILVER_TOOL);
    public static final Item DEATHBRINGER_SCYTHE = deathbringerScythe("deathbringer_scythe", SILVER_TOOL);
    public static final Item SAPPING_SWORD = sappingSword("sapping_sword", SILVER_TOOL);
    public static final Item BASIC_AMULET = item("basic_amulet");
    public static final Item SANGUINE_AMULET = item("sanguine_amulet");
    public static final Item VOID_AMULET = item("void_amulet");
    public static final Item SOULBONE_AMULET = item("soulbone_amulet");
    public static final Item BASIC_RING = item("basic_ring");
    public static final Item ENERVATING_RING = item("enervating_ring");
    public static final Item BASIC_BELT = item("basic_belt");
    public static final Item GRAVITY_BELT = item("gravity_belt");
    public static final Item RESOLUTE_BELT = item("resolute_belt");
    public static final Item MIND_SHIELDING_PLATE = item("mind_shielding_plate");
    public static final Item SOULFIRE_WAND = soulfireWand("soulfire_wand");
    public static final Item BONECHILL_WAND = bonechillWand("bonechill_wand");
    public static final Item SUMMONING_STAFF = summoningStaff("summoning_staff");
    public static final Item ALCHEMISTS_TONGS = tongs("alchemists_tongs");
    public static final Item ANGELS_SIGHT = item("angels_sight");
    public static final Item TERMINUS_MIRROR = item("terminus_mirror");
    public static final Item PRESTIGIOUS_PALM = item("prestigious_palm");
    public static final Item COALFIRED_ENGINE = item("coalfired_engine");
    public static final Item WARLOCK_HAT = armor("warlock_hat", WARLOCK_ARMOR, EntityEquipmentSlot.HEAD);
    public static final Item WARLOCK_CLOAK = armor("warlock_cloak", WARLOCK_ARMOR, EntityEquipmentSlot.CHEST);
    public static final Item WARLOCK_BOOTS = armor("warlock_boots", WARLOCK_ARMOR, EntityEquipmentSlot.FEET);
    public static final Item BONELORD_HELM = armor("bonelord_helm", BONELORD_ARMOR, EntityEquipmentSlot.HEAD);
    public static final Item BONELORD_CHESTPLATE = armor("bonelord_chestplate", BONELORD_ARMOR, EntityEquipmentSlot.CHEST);
    public static final Item BONELORD_GREAVES = armor("bonelord_greaves", BONELORD_ARMOR, EntityEquipmentSlot.LEGS);
    public static final Item RAVEN_CLOAK = armor("raven_cloak", RAVEN_ARMOR, EntityEquipmentSlot.CHEST);
    public static final Item TOP_HAT = armor("top_hat", TOP_HAT_ARMOR, EntityEquipmentSlot.HEAD);
    public static final Item WARDED_MAIL = armor("warded_mail", WARDED_ARMOR, EntityEquipmentSlot.CHEST);
    public static final Item MUSIC_DISC_PAROUSIA = record("music_disc_parousia", ModSounds.PAROUSIA);
    public static final Item SPAWN_WRAITH = item("spawn_wraith");
    public static final Item SPAWN_ZOMBIE_BRUTE = item("spawn_zombie_brute");
    public static final Item SPAWN_NECROMANCER = item("spawn_necromancer");
    public static final Item SPAWN_SLIMY_SLUG = item("spawn_slimy_slug");
    public static final Item SPAWN_RAVEN = item("spawn_raven");

    private static final Item[] ITEMS = {
            TEST_SIGIL,
            LEAD_INGOT,
            RAW_LEAD,
            LEAD_NUGGET,
            SILVER_INGOT,
            RAW_SILVER,
            SILVER_NUGGET,
            PEWTER_BLEND,
            PEWTER_INGOT,
            PEWTER_NUGGET,
            PEWTER_INLAY,
            ARCANE_GOLD_INGOT,
            ARCANE_GOLD_NUGGET,
            SHADOW_GEM,
            ELDER_BRICK,
            SULFUR,
            GOLD_INLAY,
            ZOMBIE_HEART,
            WRAITH_HEART,
            TATTERED_CLOTH,
            SOUL_SHARD,
            DEATH_ESSENCE,
            CRIMSON_ESSENCE,
            ENDER_CALX,
            TALLOW,
            LESSER_SOUL_GEM,
            WICKED_WEAVE,
            WITHERED_HEART,
            IMBUED_BONES,
            RAVEN_FEATHER,
            MERAMMER_RESIN,
            MAGIC_INK,
            MAGICIANS_WAX,
            ARCANE_SEAL,
            PARCHMENT,
            NOTETAKING_TOOLS,
            RESEARCH_NOTES,
            COMPLETED_RESEARCH,
            OFFERTORY_PLATE,
            GOLD_OFFERTORY_PLATE,
            PEWTER_OFFERTORY_PLATE,
            RED_CANDY,
            GRAPE_CANDY,
            CODEX,
            ARCHIVE,
            SCRIPTORIUM,
            CABINET,
            WOODEN_PODIUM,
            FUNGUS_SPROUTS,
            WARPED_SPROUTS,
            MIRECAP,
            GLASS_HAND,
            UNHOLY_SYMBOL,
            SILVER_SWORD,
            SILVER_PICKAXE,
            SILVER_AXE,
            SILVER_SHOVEL,
            SILVER_HOE,
            SILVER_HELMET,
            SILVER_CHESTPLATE,
            SILVER_LEGGINGS,
            SILVER_BOOTS,
            ATHAME,
            CLEAVING_AXE,
            REVERSAL_PICK,
            REAPER_SCYTHE,
            DEATHBRINGER_SCYTHE,
            SAPPING_SWORD,
            BASIC_AMULET,
            SANGUINE_AMULET,
            VOID_AMULET,
            SOULBONE_AMULET,
            BASIC_RING,
            ENERVATING_RING,
            BASIC_BELT,
            GRAVITY_BELT,
            RESOLUTE_BELT,
            MIND_SHIELDING_PLATE,
            SOULFIRE_WAND,
            BONECHILL_WAND,
            SUMMONING_STAFF,
            ALCHEMISTS_TONGS,
            ANGELS_SIGHT,
            TERMINUS_MIRROR,
            PRESTIGIOUS_PALM,
            COALFIRED_ENGINE,
            WARLOCK_HAT,
            WARLOCK_CLOAK,
            WARLOCK_BOOTS,
            BONELORD_HELM,
            BONELORD_CHESTPLATE,
            BONELORD_GREAVES,
            RAVEN_CLOAK,
            TOP_HAT,
            WARDED_MAIL,
            MUSIC_DISC_PAROUSIA,
            SPAWN_WRAITH,
            SPAWN_ZOMBIE_BRUTE,
            SPAWN_NECROMANCER,
            SPAWN_SLIMY_SLUG,
            SPAWN_RAVEN
    };

    private ModItems() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEMS);
        event.getRegistry().registerAll(ModBlocks.ITEM_BLOCKS);
    }

    private static Item item(String name) {
        Item item = new Item();
        item.setRegistryName(Reference.MOD_ID, name);
        item.setTranslationKey(Reference.MOD_ID + "." + name);
        item.setCreativeTab(ModCreativeTabs.EIDOLON);
        return item;
    }

    private static Item researchNotes(String name) {
        return setup(new ResearchNotesItem(), name);
    }

    private static Item completedResearch(String name) {
        return setup(new CompletedResearchItem(), name);
    }

    private static Item notetakingTools(String name) {
        return setup(new NotetakingToolsItem(), name);
    }

    private static Item codex(String name) {
        return setup(new CodexItem(), name);
    }

    private static Item offertoryPlate(String name, java.util.function.Supplier<Block> blockSupplier) {
        return setup(new OffertoryPlateItem(blockSupplier), name);
    }

    private static Item wand(String name) {
        return setup(new WandItem(), name);
    }

    private static Item soulfireWand(String name) {
        return setup(new SoulfireWandItem(), name);
    }

    private static Item bonechillWand(String name) {
        return setup(new BonechillWandItem(), name);
    }

    private static Item summoningStaff(String name) {
        return setup(new SummoningStaffItem(), name);
    }

    private static Item tongs(String name) {
        return setup(new TongsItem(), name);
    }

    private static Item sword(String name, Item.ToolMaterial material) {
        return setup(new ItemSword(material), name);
    }

    private static Item athame(String name, Item.ToolMaterial material) {
        return setup(new AthameItem(material), name);
    }

    private static Item cleavingAxe(String name, Item.ToolMaterial material) {
        return setup(new CleavingAxeItem(material), name);
    }

    private static Item reversalPick(String name, Item.ToolMaterial material) {
        return setup(new ReversalPickItem(material), name);
    }

    private static Item reaperScythe(String name, Item.ToolMaterial material) {
        return setup(new ReaperScytheItem(material), name);
    }

    private static Item deathbringerScythe(String name, Item.ToolMaterial material) {
        return setup(new DeathbringerScytheItem(material), name);
    }

    private static Item sappingSword(String name, Item.ToolMaterial material) {
        return setup(new SappingSwordItem(material), name);
    }

    private static Item pickaxe(String name, Item.ToolMaterial material) {
        return setup(new SimplePickaxe(material), name);
    }

    private static Item axe(String name, Item.ToolMaterial material) {
        return setup(new SimpleAxe(material), name);
    }

    private static Item shovel(String name, Item.ToolMaterial material) {
        return setup(new ItemSpade(material), name);
    }

    private static Item hoe(String name, Item.ToolMaterial material) {
        return setup(new ItemHoe(material), name);
    }

    private static Item armor(String name, ItemArmor.ArmorMaterial material, EntityEquipmentSlot slot) {
        return setup(new ItemArmor(material, 0, slot), name);
    }

    private static Item record(String name, net.minecraft.util.SoundEvent sound) {
        return setup(new SimpleRecord(name, sound), name);
    }

    private static Item setup(Item item, String name) {
        item.setRegistryName(Reference.MOD_ID, name);
        item.setTranslationKey(Reference.MOD_ID + "." + name);
        item.setCreativeTab(ModCreativeTabs.EIDOLON);
        return item;
    }

    private static final class SimplePickaxe extends ItemPickaxe {
        private SimplePickaxe(Item.ToolMaterial material) {
            super(material);
        }
    }

    private static final class SimpleAxe extends ItemAxe {
        private SimpleAxe(Item.ToolMaterial material) {
            super(material, 8.0F, -3.1F);
        }
    }

    private static final class SimpleRecord extends ItemRecord {
        private SimpleRecord(String name, net.minecraft.util.SoundEvent sound) {
            super(name, sound);
        }
    }

    private static final class OffertoryPlateItem extends Item {
        private final java.util.function.Supplier<Block> blockSupplier;

        private OffertoryPlateItem(java.util.function.Supplier<Block> blockSupplier) {
            this.blockSupplier = blockSupplier;
        }

        @Override
        public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                          EnumFacing facing, float hitX, float hitY, float hitZ) {
            if (facing != EnumFacing.UP) {
                return EnumActionResult.FAIL;
            }
            BlockPos placePos = pos.up();
            ItemStack stack = player.getHeldItem(hand);
            Block block = blockSupplier.get();
            IBlockState state = block.getDefaultState();
            if (!worldIn.mayPlace(block, placePos, false, EnumFacing.UP, player) || !block.canPlaceBlockAt(worldIn, placePos)) {
                return EnumActionResult.FAIL;
            }
            if (!worldIn.isRemote) {
                worldIn.setBlockState(placePos, state, 3);
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
            }
            return EnumActionResult.SUCCESS;
        }
    }

    private static final class ResearchNotesItem extends Item {
        private ResearchNotesItem() {
            setMaxStackSize(1);
        }

        @Override
        public void addInformation(ItemStack stack, net.minecraft.world.World worldIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
            if (!stack.hasTagCompound()) {
                return;
            }
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null || !tag.hasKey("research")) {
                return;
            }
            Research research = Researches.find(new ResourceLocation(tag.getString("research")));
            if (research == null) {
                return;
            }
            int done = tag.getInteger("stepsDone");
            StringBuilder stars = new StringBuilder(TextFormatting.GOLD.toString());
            for (int i = 0; i < research.getStars(); i++) {
                if (i == done) {
                    stars.append(TextFormatting.GRAY);
                }
                stars.append(i < done ? "*" : "o");
            }
            tooltip.add(stars.toString());
            tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + researchName(research));
        }

        @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
            if (isInCreativeTab(tab)) {
                ItemStack stack = new ItemStack(this);
                elucent.eidolon.research.Researches.ensureDefaultResearch(stack);
                items.add(stack);
            }
        }
    }

    private static final class CompletedResearchItem extends Item {
        private CompletedResearchItem() {
            setMaxStackSize(1);
        }

        @Override
        public void addInformation(ItemStack stack, net.minecraft.world.World worldIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
            if (!stack.hasTagCompound()) {
                return;
            }
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey("research")) {
                Research research = Researches.find(new ResourceLocation(tag.getString("research")));
                String name = research == null ? tag.getString("research") : researchName(research);
                tooltip.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + name);
                if (research != null && isKnownClient(research)) {
                    tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("tooltip.eidolon.research_known"));
                } else {
                    tooltip.add(TextFormatting.DARK_GRAY + I18n.translateToLocal("tooltip.eidolon.research_use"));
                }
            }
        }

        @Override
        public ActionResult<ItemStack> onItemRightClick(net.minecraft.world.World worldIn, EntityPlayer playerIn, EnumHand handIn) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("research")) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }

            ResourceLocation id = new ResourceLocation(stack.getTagCompound().getString("research"));
            Research research = Researches.find(id);
            if (research == null) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }
            if (!worldIn.isRemote && !KnowledgeUtil.knowsResearch(playerIn, research.getId())) {
                KnowledgeUtil.grantResearch(playerIn, research.getId());
                if (playerIn instanceof EntityPlayerMP) {
                    ModNetwork.CHANNEL.sendTo(new KnowledgeSyncPacket(research.getId(), true), (EntityPlayerMP) playerIn);
                }
                stack.shrink(1);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        @SideOnly(Side.CLIENT)
        private boolean isKnownClient(Research research) {
            EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;
            return player != null && KnowledgeUtil.knowsResearch(player, research.getId());
        }
    }

    private static String researchName(Research research) {
        return research.hasDisplayNameOverride()
                ? research.getDisplayNameOverride()
                : I18n.translateToLocal(research.getTranslationKey());
    }

    private static final class CodexItem extends Item {
        @Override
        public ActionResult<ItemStack> onItemRightClick(net.minecraft.world.World worldIn, EntityPlayer playerIn, EnumHand handIn) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            if (worldIn.isRemote) {
                playerIn.openGui(Eidolon.instance, ModGuiHandler.CODEX, worldIn, 0, 0, 0);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }
}
