package elucent.eidolon.item.curio;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BaubleUtil {
    private static final Method ENTITY_HANDLER_METHOD = findEntityHandlerMethod();

    private BaubleUtil() {
    }

    public static boolean hasBauble(EntityLivingBase entity, Item item) {
        return !findBauble(entity, item).isEmpty();
    }

    public static ItemStack findBauble(EntityLivingBase entity, Item item) {
        if (entity == null || item == null) {
            return ItemStack.EMPTY;
        }
        IBaublesItemHandler handler = getBaublesHandler(entity);
        if (handler == null) {
            return ItemStack.EMPTY;
        }
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack != null && !stack.isEmpty() && stack.getItem() == item) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static IBaublesItemHandler getBaublesHandler(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return BaublesApi.getBaublesHandler((EntityPlayer) entity);
        }
        if (ENTITY_HANDLER_METHOD == null) {
            return null;
        }
        try {
            return (IBaublesItemHandler) ENTITY_HANDLER_METHOD.invoke(null, entity);
        } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
            return null;
        }
    }

    private static Method findEntityHandlerMethod() {
        try {
            return BaublesApi.class.getMethod("getBaublesHandler", EntityLivingBase.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
