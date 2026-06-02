package elucent.eidolon.research;

import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Research {
    private final ResourceLocation id;
    private final int stars;
    private final List<ResourceLocation> prerequisites = new ArrayList<>();
    private final Map<Integer, List<ResearchTask>> specialTasks = new HashMap<>();
    private String sourceKey = "";

    public Research(ResourceLocation id, int stars) {
        if (stars <= 0) {
            throw new IllegalArgumentException("Research difficulty must be at least one star");
        }
        if (stars > 10) {
            throw new IllegalArgumentException("Research difficulty cannot exceed ten stars");
        }
        this.id = id;
        this.stars = stars;
    }

    public Research addSpecialTasks(int step, ResearchTask... tasks) {
        List<ResearchTask> list = new ArrayList<>();
        for (ResearchTask task : tasks) {
            list.add(task);
        }
        specialTasks.put(step, list);
        return this;
    }

    public Research requires(ResourceLocation... researchIds) {
        Collections.addAll(prerequisites, researchIds);
        return this;
    }

    public Research source(String sourceKey) {
        this.sourceKey = sourceKey;
        return this;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public List<ResourceLocation> getPrerequisites() {
        return new ArrayList<>(prerequisites);
    }

    public boolean isUnlockedFor(EntityPlayer player) {
        for (ResourceLocation prerequisite : prerequisites) {
            if (!KnowledgeUtil.knowsResearch(player, prerequisite)) {
                return false;
            }
        }
        return true;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getTranslationKey() {
        return "research." + id.getNamespace() + "." + id.getPath();
    }

    public String getDescriptionKey() {
        return "research_desc." + id.getNamespace() + "." + id.getPath();
    }

    public int getStars() {
        return stars;
    }

    public List<ResearchTask> getTasks(int rootSeed, int stepsDone) {
        if (specialTasks.containsKey(stepsDone)) {
            return new ArrayList<>(specialTasks.get(stepsDone));
        }
        Random random = new Random(getSeed(rootSeed, stepsDone));
        List<ResearchTask> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tasks.add(Researches.getRandomTask(random));
        }
        return tasks;
    }

    public int getSeed(int rootSeed, int stepsDone) {
        return id.hashCode() * 384780223 ^ stepsDone * 844955129 ^ rootSeed * 112041199 + 6;
    }
}
