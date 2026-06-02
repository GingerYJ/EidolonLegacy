package elucent.eidolon.item;

import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.registries.ModItems;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

public class NotetakingToolsItem extends Item {
    public NotetakingToolsItem() {
        setMaxStackSize(16);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult hit = rayTrace(world, player, true);
        if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK) {
            Block block = world.getBlockState(hit.getBlockPos()).getBlock();
            if (createNotes(player, stack, hand, Researches.getFluidResearches(block))) {
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }
        if (createNotes(player, stack, hand, Researches.getDimensionResearches(player.dimension))) {
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block block = world.getBlockState(pos).getBlock();
        return createNotes(player, player.getHeldItem(hand), hand, Researches.getBlockResearches(block))
                ? EnumActionResult.SUCCESS
                : EnumActionResult.PASS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        return createNotes(player, stack, hand, Researches.getEntityResearches(target));
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
        if (stack.isEmpty() || stack.getItem() != ModItems.NOTETAKING_TOOLS || !(event.getTarget() instanceof EntityLivingBase)) {
            return;
        }
        if (((NotetakingToolsItem) stack.getItem()).itemInteractionForEntity(
                stack, event.getEntityPlayer(), (EntityLivingBase) event.getTarget(), event.getHand())) {
            event.setCancellationResult(EnumActionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static boolean createNotes(EntityPlayer player, ItemStack tools, EnumHand hand, Collection<Research> researches) {
        Research research = chooseResearch(player, researches);
        if (research == null) {
            return false;
        }
        if (!player.world.isRemote) {
            ItemStack notes = Researches.createNotes(research);
            tools.shrink(1);
            if (tools.isEmpty()) {
                player.setHeldItem(hand, notes);
            } else if (!player.inventory.addItemStackToInventory(notes)) {
                player.dropItem(notes, false);
            }
        }
        return true;
    }

    private static Research chooseResearch(EntityPlayer player, Collection<Research> researches) {
        Research fallback = null;
        for (Research research : researches) {
            if (research.isUnlockedFor(player)) {
                if (fallback == null) {
                    fallback = research;
                }
                if (!KnowledgeUtil.knowsResearch(player, research.getId())) {
                    return research;
                }
            }
        }
        return fallback;
    }
}
