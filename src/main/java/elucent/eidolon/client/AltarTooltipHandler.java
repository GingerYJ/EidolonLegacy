package elucent.eidolon.client;

import elucent.eidolon.Reference;
import elucent.eidolon.spell.AltarEntries;
import elucent.eidolon.spell.AltarEntry;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
public final class AltarTooltipHandler {
    private AltarTooltipHandler() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        AltarEntry entry = AltarEntries.find(event.getItemStack());
        if (entry == null) {
            return;
        }
        if (entry.getCapacity() > 0.0D) {
            event.getToolTip().add(TextFormatting.DARK_PURPLE
                    + I18n.format("tooltip.eidolon.altar_capacity", format(entry.getCapacity())));
        }
        if (entry.getPower() > 0.0D) {
            event.getToolTip().add(TextFormatting.DARK_PURPLE
                    + I18n.format("tooltip.eidolon.altar_power", format(entry.getPower())));
        }
    }

    private static String format(double value) {
        return value == (int) value ? Integer.toString((int) value) : Double.toString(value);
    }
}
