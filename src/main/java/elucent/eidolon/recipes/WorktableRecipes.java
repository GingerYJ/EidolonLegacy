package elucent.eidolon.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.fml.common.Loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class WorktableRecipes {
    private static final String RESOURCE_DIR = "assets/eidolon/worktable_recipes";
    private static final JsonContext JSON_CONTEXT = new JsonContext(Reference.MOD_ID);
    private static final Map<ResourceLocation, WorktableRecipe> RECIPES = new LinkedHashMap<>();

    private WorktableRecipes() {
    }

    public static void init() {
        RECIPES.clear();
        loadFromClasspath();
        loadFromModSource();
        Eidolon.LOGGER.info("Loaded {} Eidolon worktable recipes", RECIPES.size());
    }

    private static void loadFromClasspath() {
        try {
            Enumeration<java.net.URL> urls = WorktableRecipes.class.getClassLoader().getResources(RESOURCE_DIR);
            while (urls.hasMoreElements()) {
                java.net.URL url = urls.nextElement();
                if ("file".equals(url.getProtocol())) {
                    loadFromDirectory(Paths.get(url.toURI()));
                } else if ("jar".equals(url.getProtocol())) {
                    String path = url.getPath();
                    int separator = path.indexOf("!/");
                    if (separator >= 0) {
                        loadFromJar(Paths.get(new java.net.URI(path.substring(0, separator))).toFile());
                    }
                }
            }
        } catch (IOException e) {
            Eidolon.LOGGER.error("Failed to scan Eidolon worktable recipes", e);
        } catch (Exception e) {
            Eidolon.LOGGER.error("Failed to locate Eidolon worktable recipe resources", e);
        }
    }

    private static void loadFromModSource() {
        try {
            Path root = Loader.instance().getIndexedModList().get(Reference.MOD_ID).getSource().toPath();
            if (Files.isDirectory(root)) {
                loadFromDirectory(root.resolve(RESOURCE_DIR));
            } else if (Files.isRegularFile(root)) {
                loadFromJar(root.toFile());
            }
        } catch (Exception e) {
            Eidolon.LOGGER.error("Failed to scan Eidolon worktable recipes from mod source", e);
        }
    }

    public static List<WorktableRecipe> getRecipes() {
        return Collections.unmodifiableList(new ArrayList<>(RECIPES.values()));
    }

    public static WorktableRecipe getRecipe(ResourceLocation id) {
        return RECIPES.get(id);
    }

    public static WorktableRecipe findMatch(ItemStack[] inputGrid, ItemStack[] inputReagents) {
        for (WorktableRecipe recipe : RECIPES.values()) {
            if (recipe.matches(inputGrid, inputReagents)) {
                return recipe;
            }
        }
        return null;
    }

    private static void loadFromDirectory(Path recipeDir) throws IOException {
        if (!Files.isDirectory(recipeDir)) {
            return;
        }
        Files.walk(recipeDir)
                .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .sorted()
                .forEach(WorktableRecipes::loadRecipe);
    }

    private static void loadFromJar(java.io.File file) throws IOException {
        try (JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().startsWith(RESOURCE_DIR + "/") && entry.getName().endsWith(".json")) {
                    ResourceLocation id = new ResourceLocation(Reference.MOD_ID, stripExtension(Paths.get(entry.getName()).getFileName().toString()));
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(jar.getInputStream(entry), StandardCharsets.UTF_8))) {
                        loadRecipe(id, reader);
                    } catch (Exception e) {
                        Eidolon.LOGGER.error("Failed to load worktable recipe {}", id, e);
                    }
                }
            }
        }
    }

    private static void loadRecipe(Path path) {
        ResourceLocation id = new ResourceLocation(Reference.MOD_ID, stripExtension(path.getFileName().toString()));
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            loadRecipe(id, reader);
        } catch (Exception e) {
            Eidolon.LOGGER.error("Failed to load worktable recipe {}", id, e);
        }
    }

    private static void loadRecipe(ResourceLocation id, BufferedReader reader) {
        JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
        String type = JsonUtils.getString(json, "type", "");
        if (!"eidolon:worktable".equals(type)) {
            return;
        }
        WorktableRecipe recipe = parseRecipe(id, json);
        RECIPES.put(id, recipe);
    }

    private static WorktableRecipe parseRecipe(ResourceLocation id, JsonObject json) {
        Map<Character, Ingredient> key = parseKey(JsonUtils.getJsonObject(json, "key"));
        Ingredient[] grid = parsePattern(JsonUtils.getJsonArray(json, "pattern"), key);
        Ingredient[] reagents = parseReagents(JsonUtils.getString(json, "reagents", ""), key);
        ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), JSON_CONTEXT);
        return new WorktableRecipe(id, grid, reagents, result);
    }

    private static Map<Character, Ingredient> parseKey(JsonObject keyJson) {
        Map<Character, Ingredient> key = new LinkedHashMap<>();
        key.put(' ', Ingredient.EMPTY);
        for (Map.Entry<String, JsonElement> entry : keyJson.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new IllegalArgumentException("Worktable key entries must be single characters");
            }
            key.put(entry.getKey().charAt(0), parseIngredient(entry.getValue()));
        }
        return key;
    }

    private static Ingredient parseIngredient(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            if (object.has("tag")) {
                return new OreIngredient(mapTagToOre(JsonUtils.getString(object, "tag")));
            }
            if (object.has("item")) {
                JsonObject mapped = mapItemObject(object);
                return Ingredient.fromStacks(CraftingHelper.getItemStack(mapped, JSON_CONTEXT));
            }
        }
        return CraftingHelper.getIngredient(element);
    }

    private static JsonObject mapItemObject(JsonObject object) {
        String item = JsonUtils.getString(object, "item");
        JsonObject mapped = object.deepCopy();
        switch (item) {
            case "minecraft:bone_meal":
                mapped.addProperty("item", "minecraft:dye");
                mapped.addProperty("data", 15);
                break;
            case "minecraft:skeleton_skull":
                mapped.addProperty("item", "minecraft:skull");
                mapped.addProperty("data", 0);
                break;
            case "minecraft:wither_skeleton_skull":
                mapped.addProperty("item", "minecraft:skull");
                mapped.addProperty("data", 1);
                break;
            case "minecraft:crying_obsidian":
                mapped.addProperty("item", "minecraft:obsidian");
                break;
            case "minecraft:dirt":
                mapped.addProperty("data", 0);
                break;
            case "minecraft:stone":
                mapped.addProperty("data", 0);
                break;
            case "minecraft:smooth_stone":
                mapped.addProperty("item", "minecraft:stone");
                mapped.addProperty("data", 0);
                break;
            case "minecraft:smooth_stone_slab":
                mapped.addProperty("item", "minecraft:stone_slab");
                mapped.addProperty("data", 0);
                break;
            case "minecraft:white_wool":
                mapped.addProperty("item", "minecraft:wool");
                mapped.addProperty("data", 0);
                break;
            default:
                break;
        }
        return mapped;
    }

    private static String mapTagToOre(String tag) {
        switch (tag) {
            case "forge:ingots/arcane_gold":
                return "ingotArcaneGold";
            case "forge:ingots/lead":
                return "ingotLead";
            case "forge:ingots/pewter":
                return "ingotPewter";
            case "forge:ingots/silver":
                return "ingotSilver";
            case "forge:nuggets/gold":
                return "nuggetGold";
            case "forge:nuggets/silver":
                return "nuggetSilver";
            case "forge:ender_pearls":
                return "enderpearl";
            case "forge:feathers":
                return "feather";
            case "forge:gems/diamond":
                return "gemDiamond";
            case "forge:gems/quartz":
                return "gemQuartz";
            case "forge:bones":
                return "bone";
            case "forge:rods/wooden":
                return "stickWood";
            case "forge:storage_blocks/diamond":
                return "blockDiamond";
            case "forge:storage_blocks/lapis":
                return "blockLapis";
            case "forge:dyes/blue":
                return "dyeBlue";
            case "minecraft:planks":
                return "plankWood";
            default:
                throw new IllegalArgumentException("Unsupported worktable tag: " + tag);
        }
    }

    private static Ingredient[] parsePattern(JsonArray patternJson, Map<Character, Ingredient> key) {
        if (patternJson.size() > 3) {
            throw new IllegalArgumentException("Worktable pattern must have at most 3 rows");
        }
        Ingredient[] grid = emptyIngredients(WorktableRecipe.GRID_SIZE);
        for (int row = 0; row < patternJson.size(); row++) {
            String line = patternJson.get(row).getAsString();
            if (line.length() > 3) {
                throw new IllegalArgumentException("Worktable pattern rows must be at most 3 columns");
            }
            for (int col = 0; col < line.length(); col++) {
                grid[row * 3 + col] = ingredientFor(key, line.charAt(col));
            }
        }
        return grid;
    }

    private static Ingredient[] parseReagents(String reagentText, Map<Character, Ingredient> key) {
        Ingredient[] reagents = emptyIngredients(WorktableRecipe.REAGENT_SIZE);
        for (int i = 0; i < Math.min(reagentText.length(), WorktableRecipe.REAGENT_SIZE); i++) {
            reagents[i] = ingredientFor(key, reagentText.charAt(i));
        }
        return reagents;
    }

    private static Ingredient[] emptyIngredients(int size) {
        Ingredient[] ingredients = new Ingredient[size];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = Ingredient.EMPTY;
        }
        return ingredients;
    }

    private static Ingredient ingredientFor(Map<Character, Ingredient> key, char symbol) {
        Ingredient ingredient = key.get(symbol);
        if (ingredient == null) {
            throw new IllegalArgumentException("Undefined worktable key '" + symbol + "'");
        }
        return ingredient;
    }

    private static String stripExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 ? fileName.substring(0, dot) : fileName;
    }
}
