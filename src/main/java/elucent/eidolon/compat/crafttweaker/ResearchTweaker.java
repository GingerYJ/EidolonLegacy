package elucent.eidolon.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import elucent.eidolon.research.Research;
import elucent.eidolon.research.ResearchTask;
import elucent.eidolon.research.Researches;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.eidolon.Research")
public final class ResearchTweaker {
    private ResearchTweaker() {
    }

    @ZenMethod
    public static void addResearch(String id, String name, int stars, String[] prerequisites,
                                   IItemStack[][] stepItems, @Optional String source,
                                   @Optional String description) {
        ResourceLocation researchId = TweakerUtil.id(id);
        Research research = new Research(researchId, stars)
                .displayName(name)
                .sourceText(source)
                .description(description);
        for (ResourceLocation prerequisite : prerequisites(prerequisites)) {
            research.requires(prerequisite);
        }
        addStepTasks(research, stepItems, stars);
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon research " + researchId) {
            @Override
            public void apply() {
                Researches.addResearch(research);
            }
        });
    }

    @ZenMethod
    public static void addResearchLang(String id, int stars, String[] prerequisites,
                                       IItemStack[][] stepItems, @Optional String sourceKey) {
        ResourceLocation researchId = TweakerUtil.id(id);
        Research research = new Research(researchId, stars).source(sourceKey);
        for (ResourceLocation prerequisite : prerequisites(prerequisites)) {
            research.requires(prerequisite);
        }
        addStepTasks(research, stepItems, stars);
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon research " + researchId) {
            @Override
            public void apply() {
                Researches.addResearch(research);
            }
        });
    }

    @ZenMethod
    public static void removeById(String id) {
        ResourceLocation researchId = TweakerUtil.id(id);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon research " + researchId) {
            @Override
            public void apply() {
                Researches.removeResearch(researchId);
            }
        });
    }

    @ZenMethod
    public static void removeAll() {
        CraftTweakerAPI.apply(new NamedAction("Removing all Eidolon researches") {
            @Override
            public void apply() {
                Researches.removeAllResearches();
            }
        });
    }

    @ZenMethod
    public static void addBlockTrigger(String blockId, String researchId) {
        ResourceLocation block = TweakerUtil.id(blockId);
        ResourceLocation research = TweakerUtil.id(researchId);
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon block research trigger " + block + " -> " + research) {
            @Override
            public void apply() {
                Researches.addBlockResearch(block, research);
            }
        });
    }

    @ZenMethod
    public static void addEntityTrigger(String entityId, String researchId) {
        ResourceLocation entity = TweakerUtil.id(entityId);
        ResourceLocation research = TweakerUtil.id(researchId);
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon entity research trigger " + entity + " -> " + research) {
            @Override
            public void apply() {
                Researches.addEntityResearch(entity, research);
            }
        });
    }

    @ZenMethod
    public static void addDimensionTrigger(int dimension, String researchId) {
        ResourceLocation research = TweakerUtil.id(researchId);
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon dimension research trigger " + dimension + " -> " + research) {
            @Override
            public void apply() {
                Researches.addDimensionResearch(dimension, research);
            }
        });
    }

    @ZenMethod
    public static void addFluidTrigger(String blockId, String researchId) {
        ResourceLocation fluidBlock = TweakerUtil.id(blockId);
        ResourceLocation research = TweakerUtil.id(researchId);
        CraftTweakerAPI.apply(new NamedAction("Adding Eidolon fluid research trigger " + fluidBlock + " -> " + research) {
            @Override
            public void apply() {
                Researches.addFluidResearch(fluidBlock, research);
            }
        });
    }

    @ZenMethod
    public static void removeBlockTriggers(String blockId) {
        ResourceLocation block = TweakerUtil.id(blockId);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon block research triggers for " + block) {
            @Override
            public void apply() {
                Researches.removeBlockResearches(block);
            }
        });
    }

    @ZenMethod
    public static void removeEntityTriggers(String entityId) {
        ResourceLocation entity = TweakerUtil.id(entityId);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon entity research triggers for " + entity) {
            @Override
            public void apply() {
                Researches.removeEntityResearches(entity);
            }
        });
    }

    @ZenMethod
    public static void removeDimensionTriggers(int dimension) {
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon dimension research triggers for " + dimension) {
            @Override
            public void apply() {
                Researches.removeDimensionResearches(dimension);
            }
        });
    }

    @ZenMethod
    public static void removeFluidTriggers(String blockId) {
        ResourceLocation fluidBlock = TweakerUtil.id(blockId);
        CraftTweakerAPI.apply(new NamedAction("Removing Eidolon fluid research triggers for " + fluidBlock) {
            @Override
            public void apply() {
                Researches.removeFluidResearches(fluidBlock);
            }
        });
    }

    @ZenMethod
    public static void removeAllTriggers() {
        CraftTweakerAPI.apply(new NamedAction("Removing all Eidolon research triggers") {
            @Override
            public void apply() {
                Researches.removeAllTriggers();
            }
        });
    }

    private static void addStepTasks(Research research, IItemStack[][] stepItems, int stars) {
        if (stepItems == null || stepItems.length != stars) {
            throw new IllegalArgumentException("Research stepItems must contain exactly one entry per star");
        }
        for (int i = 0; i < stepItems.length; i++) {
            ItemStack[] stacks = stacks(stepItems[i], "Research step " + (i + 1));
            ResearchTask[] tasks = new ResearchTask[stacks.length];
            for (int j = 0; j < stacks.length; j++) {
                tasks[j] = new ResearchTask.ItemsTask(stacks[j]);
            }
            research.addSpecialTasks(i, tasks);
        }
    }

    private static ItemStack[] stacks(IItemStack[] items, String label) {
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException(label + " must contain at least one item");
        }
        if (items.length > 9) {
            throw new IllegalArgumentException(label + " cannot contain more than 9 items");
        }
        ItemStack[] stacks = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            stacks[i] = TweakerUtil.stack(items[i]);
            if (stacks[i].isEmpty()) {
                throw new IllegalArgumentException(label + " contains an empty item at index " + i);
            }
        }
        return stacks;
    }

    private static List<ResourceLocation> prerequisites(String[] prerequisites) {
        List<ResourceLocation> ids = new ArrayList<>();
        if (prerequisites != null) {
            for (String prerequisite : prerequisites) {
                if (prerequisite != null && !prerequisite.isEmpty()) {
                    ids.add(TweakerUtil.id(prerequisite));
                }
            }
        }
        return ids;
    }
}
