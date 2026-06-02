package elucent.eidolon.recipes;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Reference;
import elucent.eidolon.registries.ModBlocks;
import elucent.eidolon.registries.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CrucibleRecipes {
    private static final Map<ResourceLocation, CrucibleRecipe> RECIPES = new LinkedHashMap<>();
    private static final List<Runnable> CUSTOMIZATIONS = new ArrayList<>();
    private static boolean initialized;

    private CrucibleRecipes() {
    }

    public static void init() {
        initialized = false;
        RECIPES.clear();
        registerBuiltIns();
        initialized = true;
        applyCustomizations();
        Eidolon.LOGGER.info("Loaded {} Eidolon crucible recipes", RECIPES.size());
    }

    public static List<CrucibleRecipe> getRecipes() {
        return Collections.unmodifiableList(new ArrayList<>(RECIPES.values()));
    }

    public static CrucibleRecipe find(ResourceLocation id) {
        return RECIPES.get(id);
    }

    public static void addRecipe(ResourceLocation id, ItemStack result, Ingredient stirrer, FluidStack fluid,
                                 List<CrucibleRecipe.Step> steps) {
        ItemStack resultCopy = result.copy();
        FluidStack fluidCopy = fluid.copy();
        List<CrucibleRecipe.Step> stepCopy = new ArrayList<>(steps);
        addCustomization(() -> RECIPES.put(id, new CrucibleRecipe(id, stepCopy, resultCopy, stirrer, fluidCopy)));
    }

    public static boolean removeRecipe(ResourceLocation id) {
        addCustomization(() -> RECIPES.remove(id));
        return initialized && !RECIPES.containsKey(id);
    }

    public static int removeRecipesByOutput(Ingredient output) {
        int count = initialized ? countRecipesByOutput(output) : 0;
        addCustomization(() -> RECIPES.entrySet().removeIf(entry -> output.apply(entry.getValue().getResult())));
        return count;
    }

    public static int removeAllRecipes() {
        int count = initialized ? RECIPES.size() : 0;
        addCustomization(RECIPES::clear);
        return count;
    }

    public static CrucibleRecipe.Step makeStep(int stirs, List<Ingredient> ingredients) {
        return new CrucibleRecipe.Step(stirs, ingredients);
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

    private static int countRecipesByOutput(Ingredient output) {
        int count = 0;
        for (CrucibleRecipe recipe : RECIPES.values()) {
            if (output.apply(recipe.getResult())) {
                count++;
            }
        }
        return count;
    }

    public static CrucibleRecipe find(List<CrucibleRecipe.ProvidedStep> steps, FluidStack fluid) {
        for (CrucibleRecipe recipe : RECIPES.values()) {
            if (recipe.matches(steps, fluid)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean matchesAnyPrefix(List<CrucibleRecipe.ProvidedStep> steps, FluidStack fluid) {
        for (CrucibleRecipe recipe : RECIPES.values()) {
            if (recipe.matchesPrefix(steps, fluid)) {
                return true;
            }
        }
        return false;
    }

    public static boolean acceptsFluid(FluidStack fluid) {
        if (fluid == null) {
            return false;
        }
        for (CrucibleRecipe recipe : RECIPES.values()) {
            if (recipe.matchesFluid(fluid)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStirrer(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (CrucibleRecipe recipe : RECIPES.values()) {
            if (recipe.getStirrer().apply(stack)) {
                return true;
            }
        }
        return false;
    }

    private static void registerBuiltIns() {
        register("arcane_gold", stack(ModItems.ARCANE_GOLD_INGOT, 2),
                step(0, ore("dustRedstone"), ore("dustRedstone"), item(ModItems.SOUL_SHARD)),
                step(0, ore("ingotGold"), ore("ingotGold")));
        register("lesser_soul_gem", stack(ModItems.LESSER_SOUL_GEM, 1),
                step(0, ore("dustRedstone"), ore("dustRedstone"), ore("gemLapis"), ore("gemLapis")),
                step(2, item(ModItems.SOUL_SHARD), item(ModItems.SOUL_SHARD), item(ModItems.SOUL_SHARD), item(ModItems.SOUL_SHARD)),
                step(0, ore("gemQuartz")));
        register("shadow_gem", stack(ModItems.SHADOW_GEM, 1),
                step(0, item(Items.COAL)),
                step(1, item(Items.GHAST_TEAR), item(ModItems.DEATH_ESSENCE)),
                step(1, item(ModItems.SOUL_SHARD), item(ModItems.SOUL_SHARD), item(ModItems.DEATH_ESSENCE)),
                step(0, ore("gemDiamond")));
        register("sulfur", stack(ModItems.SULFUR, 2),
                step(0, item(Items.COAL), item(ModBlocks.ENCHANTED_ASH)));
        register("ender_calx", stack(ModItems.ENDER_CALX, 2),
                step(0, ore("enderpearl"), item(ModBlocks.ENCHANTED_ASH)));
        register("leather_from_flesh", new ItemStack(Items.LEATHER),
                step(0, item(ModBlocks.ENCHANTED_ASH), item(ModBlocks.ENCHANTED_ASH)),
                step(2, item(Items.ROTTEN_FLESH)));
        register("rotten_beef", new ItemStack(Items.ROTTEN_FLESH),
                step(0, item(Items.BEEF), ore("cropMushroom")));
        register("rotten_pork", new ItemStack(Items.ROTTEN_FLESH),
                step(0, item(Items.PORKCHOP), ore("cropMushroom")));
        register("rotten_mutton", new ItemStack(Items.ROTTEN_FLESH),
                step(0, item(Items.MUTTON), ore("cropMushroom")));
        register("rotten_chicken", new ItemStack(Items.ROTTEN_FLESH),
                step(0, item(Items.CHICKEN), ore("cropMushroom")));
        register("rotten_rabbit", new ItemStack(Items.ROTTEN_FLESH),
                step(0, item(Items.RABBIT), ore("cropMushroom")));
        register("gunpowder", new ItemStack(Items.GUNPOWDER, 4),
                step(0, item(ModItems.SULFUR), item(new ItemStack(Items.DYE, 1, 15))),
                step(1, item(new ItemStack(Items.COAL, 1, 1))));
        register("gilded_apple", new ItemStack(Items.GOLDEN_APPLE),
                step(0, ore("ingotGold"), ore("ingotGold")),
                step(2, item(ModBlocks.ENCHANTED_ASH)),
                step(0, item(Items.APPLE)));
        register("gilded_carrot", new ItemStack(Items.GOLDEN_CARROT),
                step(0, ore("nuggetGold"), ore("nuggetGold")),
                step(2, item(ModBlocks.ENCHANTED_ASH)),
                step(0, item(Items.CARROT)));
        register("gilded_melon", new ItemStack(Items.SPECKLED_MELON),
                step(0, ore("nuggetGold"), ore("nuggetGold")),
                step(2, item(ModBlocks.ENCHANTED_ASH)),
                step(0, item(Items.MELON)));
        register("death_essence", stack(ModItems.DEATH_ESSENCE, 4),
                step(0, item(ModItems.ZOMBIE_HEART), item(Items.ROTTEN_FLESH)),
                step(2, item(new ItemStack(Items.DYE, 1, 15)), item(new ItemStack(Items.DYE, 1, 15))),
                step(0, item(new ItemStack(Items.COAL, 1, 1))));
        register("fungus_sprouts", stack(ModItems.FUNGUS_SPROUTS, 2),
                step(0, ore("cropMushroom")),
                step(2, item(new ItemStack(Items.DYE, 1, 15))),
                step(0, item(Items.WHEAT_SEEDS)));
        register("polished_planks", new ItemStack(ModBlocks.POLISHED_PLANKS, 32),
                step(0, planks32()),
                step(1, item(ModItems.SOUL_SHARD), item(ModBlocks.ENCHANTED_ASH)));
    }

    private static void register(String name, ItemStack result, CrucibleRecipe.Step... steps) {
        ResourceLocation id = new ResourceLocation(Reference.MOD_ID, name);
        List<CrucibleRecipe.Step> stepList = new ArrayList<>();
        Collections.addAll(stepList, steps);
        RECIPES.put(id, new CrucibleRecipe(id, stepList, result, CrucibleRecipe.defaultStirrer(), CrucibleRecipe.defaultFluid()));
    }

    private static void register(String name, ItemStack result, Ingredient stirrer, CrucibleRecipe.Step... steps) {
        register(name, result, stirrer, CrucibleRecipe.defaultFluid(), steps);
    }

    private static void register(String name, ItemStack result, Ingredient stirrer, FluidStack fluid, CrucibleRecipe.Step... steps) {
        ResourceLocation id = new ResourceLocation(Reference.MOD_ID, name);
        List<CrucibleRecipe.Step> stepList = new ArrayList<>();
        Collections.addAll(stepList, steps);
        RECIPES.put(id, new CrucibleRecipe(id, stepList, result, stirrer, fluid));
    }

    private static CrucibleRecipe.Step step(int stirs, Ingredient... ingredients) {
        List<Ingredient> list = new ArrayList<>();
        Collections.addAll(list, ingredients);
        return new CrucibleRecipe.Step(stirs, list);
    }

    private static Ingredient[] planks32() {
        Ingredient[] ingredients = new Ingredient[32];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = ore("plankWood");
        }
        return ingredients;
    }

    private static Ingredient item(net.minecraft.item.Item item) {
        return Ingredient.fromStacks(new ItemStack(item));
    }

    private static Ingredient item(net.minecraft.block.Block block) {
        return Ingredient.fromStacks(new ItemStack(block));
    }

    private static Ingredient item(ItemStack stack) {
        return Ingredient.fromStacks(stack);
    }

    private static Ingredient ore(String key) {
        return new OreIngredient(key);
    }

    private static ItemStack stack(net.minecraft.item.Item item, int count) {
        return new ItemStack(item, count);
    }
}
