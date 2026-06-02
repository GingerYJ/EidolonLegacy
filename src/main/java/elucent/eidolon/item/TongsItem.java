package elucent.eidolon.item;

import elucent.eidolon.registries.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TongsItem extends Item {
    public TongsItem() {
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (ModBlocks.setGlassTubeOutput(worldIn, pos, facing)) {
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
