package elucent.eidolon.research;

import elucent.eidolon.registries.ModBlocks;
import elucent.eidolon.registries.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public abstract class ResearchTask {
    public static final class Completion {
        private final int nextSlot;
        private final boolean complete;

        public Completion(int nextSlot, boolean complete) {
            this.nextSlot = nextSlot;
            this.complete = complete;
        }

        public int getNextSlot() {
            return nextSlot;
        }

        public boolean isComplete() {
            return complete;
        }
    }

    public int getSlotCount() {
        return 0;
    }

    public int getDisplayWidth() {
        return 64;
    }

    public abstract String getType();

    public void drawIcon(int x, int y) {
    }

    public int drawCustom(int x, int y) {
        return 0;
    }

    public List<String> getTooltip() {
        return new ArrayList<>();
    }

    public boolean isComplete(List<ItemStack> inputs, EntityPlayer player) {
        return false;
    }

    public void consume(List<ItemStack> inputs, EntityPlayer player) {
    }

    public static class ItemsTask extends ResearchTask {
        private final List<ItemStack> items;

        public ItemsTask(ItemStack... stacks) {
            this.items = new ArrayList<>();
            for (ItemStack stack : stacks) {
                this.items.add(stack.copy());
            }
        }

        public List<ItemStack> getItems() {
            List<ItemStack> copy = new ArrayList<>();
            for (ItemStack stack : items) {
                copy.add(stack.copy());
            }
            return copy;
        }

        @Override
        public int getSlotCount() {
            return items.size();
        }

        @Override
        public int getDisplayWidth() {
            return 72 + 17 * items.size();
        }

        @Override
        public String getType() {
            return "items";
        }

        @Override
        public void drawIcon(int x, int y) {
            int offset = (items.size() - 1) * -4;
            for (int i = 0; i < items.size(); i++) {
                ItemStack stack = items.get(i);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x + i * 8 + offset, y);
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, stack, x + i * 8 + offset, y, null);
            }
        }

        @Override
        public int drawCustom(int x, int y) {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 88, 224, 1, 32, 256, 256);
            x += 1;
            for (int i = 0; i < items.size(); i++) {
                if (items.size() == 1) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 192, 0, 22, 32, 256, 256);
                    x += 22;
                } else if (i == 0) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 192, 32, 20, 32, 256, 256);
                    x += 19;
                } else if (i == items.size() - 1) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 228, 32, 20, 32, 256, 256);
                    x += 20;
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 211, 32, 18, 32, 256, 256);
                    x += 17;
                }
            }
            Gui.drawModalRectWithCustomSizedTexture(x, y, 88, 224, 2, 32, 256, 256);
            return 8 + 17 * items.size();
        }

        @Override
        public List<String> getTooltip() {
            List<String> tooltip = new ArrayList<>();
            for (ItemStack stack : items) {
                String name = stack.getDisplayName();
                if (stack.getCount() > 1) {
                    name += " x" + stack.getCount();
                }
                tooltip.add(name);
            }
            return tooltip;
        }

        @Override
        public boolean isComplete(List<ItemStack> inputs, EntityPlayer player) {
            if (inputs.size() < items.size()) {
                return false;
            }
            for (int i = 0; i < items.size(); i++) {
                ItemStack expected = items.get(i);
                ItemStack actual = inputs.get(i);
                if (actual.isEmpty()
                        || actual.getItem() != expected.getItem()
                        || actual.getMetadata() != expected.getMetadata()
                        || actual.getCount() < expected.getCount()
                        || (expected.hasTagCompound() && !ItemStack.areItemStackTagsEqual(actual, expected))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void consume(List<ItemStack> inputs, EntityPlayer player) {
            for (int i = 0; i < items.size(); i++) {
                inputs.get(i).shrink(items.get(i).getCount());
            }
        }
    }

    public static final class ScrivenerItems extends ItemsTask {
        private static final List<Function<Random, ItemStack>> ITEM_POOL = new ArrayList<>();

        static {
            ITEM_POOL.add(random -> new ItemStack(ModItems.MAGIC_INK, 1 + random.nextInt(2)));
            ITEM_POOL.add(random -> new ItemStack(ModItems.RAVEN_FEATHER));
            ITEM_POOL.add(random -> new ItemStack(ModItems.PARCHMENT, 1 + random.nextInt(2)));
            ITEM_POOL.add(random -> new ItemStack(ModBlocks.CANDLE));
            ITEM_POOL.add(random -> new ItemStack(Items.COAL, 1 + random.nextInt(2), 1));
            ITEM_POOL.add(random -> new ItemStack(Items.BOOK));
        }

        public ScrivenerItems(Random random) {
            super(ITEM_POOL.get(random.nextInt(ITEM_POOL.size())).apply(random));
        }
    }

    public static final class Xp extends ResearchTask {
        private final int levels;

        public Xp(Random random) {
            this.levels = 1 + random.nextInt(5);
        }

        public int getLevels() {
            return levels;
        }

        @Override
        public String getType() {
            return "xp";
        }

        @Override
        public void drawIcon(int x, int y) {
            int offY = Minecraft.getMinecraft().player.experienceLevel < levels ? 16 : 0;
            Gui.drawModalRectWithCustomSizedTexture(x, y, (levels - 1) * 16, 224 + offY, 16, 16, 256, 256);
        }

        @Override
        public List<String> getTooltip() {
            List<String> tooltip = new ArrayList<>();
            if (levels == 1) {
                tooltip.add(new TextComponentTranslation("container.enchant.level.one").getFormattedText());
            } else {
                tooltip.add(new TextComponentTranslation("container.enchant.level.many", levels).getFormattedText());
            }
            return tooltip;
        }

        @Override
        public boolean isComplete(List<ItemStack> inputs, EntityPlayer player) {
            return player.experienceLevel >= levels;
        }

        @Override
        public void consume(List<ItemStack> inputs, EntityPlayer player) {
            player.addExperienceLevel(-levels);
        }
    }
}
