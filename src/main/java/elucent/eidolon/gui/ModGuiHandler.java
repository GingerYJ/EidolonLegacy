package elucent.eidolon.gui;

import elucent.eidolon.Reference;
import elucent.eidolon.tile.ResearchTableTileEntity;
import elucent.eidolon.tile.WorktableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
    public static final int WORKTABLE = 0;
    public static final int RESEARCH_TABLE = 1;
    public static final int CODEX = 2;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == WORKTABLE) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof WorktableTileEntity) {
                return new WorktableContainer(player.inventory, (WorktableTileEntity) tile);
            }
        } else if (id == RESEARCH_TABLE) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof ResearchTableTileEntity) {
                return new ResearchTableContainer(player.inventory, (ResearchTableTileEntity) tile);
            }
        } else if (id == CODEX) {
            return null;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == WORKTABLE) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof WorktableTileEntity) {
                return new WorktableGui(player.inventory, (WorktableTileEntity) tile);
            }
        } else if (id == RESEARCH_TABLE) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof ResearchTableTileEntity) {
                return new ResearchTableGui(player.inventory, (ResearchTableTileEntity) tile);
            }
        } else if (id == CODEX) {
            return new CodexGui(player);
        }
        return null;
    }
}
