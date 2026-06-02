package elucent.eidolon.registries;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModRecipes {
    private ModRecipes() {
    }

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.LEAD_ORE, new ItemStack(ModItems.LEAD_INGOT), 0.7F);
        GameRegistry.addSmelting(ModBlocks.DEEP_LEAD_ORE, new ItemStack(ModItems.LEAD_INGOT), 0.7F);
        GameRegistry.addSmelting(ModItems.RAW_LEAD, new ItemStack(ModItems.LEAD_INGOT), 0.7F);
        GameRegistry.addSmelting(ModBlocks.SILVER_ORE, new ItemStack(ModItems.SILVER_INGOT), 0.7F);
        GameRegistry.addSmelting(ModBlocks.DEEP_SILVER_ORE, new ItemStack(ModItems.SILVER_INGOT), 0.7F);
        GameRegistry.addSmelting(ModItems.RAW_SILVER, new ItemStack(ModItems.SILVER_INGOT), 0.7F);
        GameRegistry.addSmelting(ModItems.PEWTER_BLEND, new ItemStack(ModItems.PEWTER_INGOT), 0.7F);
        GameRegistry.addSmelting(net.minecraft.init.Items.BONE, new ItemStack(ModBlocks.ENCHANTED_ASH, 2), 0.1F);
        GameRegistry.addSmelting(net.minecraft.init.Blocks.BONE_BLOCK, new ItemStack(ModBlocks.ENCHANTED_ASH, 6), 0.1F);
    }
}
