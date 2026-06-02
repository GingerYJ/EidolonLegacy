package elucent.eidolon.item;

import elucent.eidolon.registries.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AthameItem extends ItemSword {
    private static final List<HarvestEntry> HARVEST_ENTRIES = Arrays.asList(
            new HarvestEntry(
                    Collections.singletonList(new ItemStack(Blocks.TALLGRASS, 1, BlockTallGrass.EnumType.FERN.getMeta())),
                    () -> new ItemStack(ModBlocks.AVENNIAN_SPRIG_ITEM),
                    "gui.eidolon.athame_harvest.source.fern"
            ),
            new HarvestEntry(
                    Collections.singletonList(new ItemStack(Blocks.RED_FLOWER, 1, BlockRedFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
                    () -> new ItemStack(ModBlocks.MERAMMER_ROOT_ITEM),
                    "gui.eidolon.athame_harvest.source.oxeye_daisy"
            ),
            new HarvestEntry(
                    Collections.singletonList(new ItemStack(Blocks.WATERLILY)),
                    () -> new ItemStack(ModBlocks.OANNA_BLOOM_ITEM),
                    "gui.eidolon.athame_harvest.source.lily_pad"
            ),
            new HarvestEntry(
                    Collections.singletonList(new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.JUNGLE.getMetadata())),
                    () -> new ItemStack(ModBlocks.SILDRIAN_SEED_ITEM),
                    "gui.eidolon.athame_harvest.source.jungle_leaves"
            )
    );

    public AthameItem(ToolMaterial material) {
        super(material);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLooting(LootingLevelEvent event) {
        DamageSource source = event.getDamageSource();
        if (source != null && source.getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
            if (attacker.getHeldItemMainhand().getItem() instanceof AthameItem) {
                event.setLootingLevel(event.getLootingLevel() * 2 + 1);
            }
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source == null || !(source.getTrueSource() instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
        if (!(attacker.getHeldItemMainhand().getItem() instanceof AthameItem)) {
            return;
        }
        EntityLivingBase target = event.getEntityLiving();
        if (target instanceof EntityEnderman
                || target instanceof EntityEndermite
                || target instanceof EntityDragon
                || target instanceof EntityShulker) {
            event.setAmount(event.getAmount() * 4.0F);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        ItemStack harvest = getHarvestable(state);
        if (harvest.isEmpty() && !isSoftPlant(state)) {
            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        if (!worldIn.isRemote) {
            spawnPlantBreakParticles(worldIn, pos, state);
            worldIn.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS,
                    0.5F, 0.9F + itemRand.nextFloat() * 0.2F);
            if (itemRand.nextInt(5) == 0) {
                BlockPos destroyPos = getDestroyPos(state, pos);
                worldIn.destroyBlock(destroyPos, false);
                if (!harvest.isEmpty() && itemRand.nextInt(3) == 0) {
                    worldIn.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS,
                            0.5F, 0.9F + itemRand.nextFloat() * 0.2F);
                    worldIn.spawnEntity(new EntityItem(worldIn,
                            pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, harvest.copy()));
                    if (!player.capabilities.isCreativeMode) {
                        player.getHeldItem(hand).damageItem(1, player);
                    }
                }
            }
        }
        return EnumActionResult.SUCCESS;
    }

    private void spawnPlantBreakParticles(World world, BlockPos pos, IBlockState state) {
        if (world instanceof WorldServer) {
            ((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    4, 0.15D, 0.15D, 0.15D, 0.03D, Block.getStateId(state));
        }
    }

    private BlockPos getDestroyPos(IBlockState state, BlockPos pos) {
        if (state.getBlock() == Blocks.DOUBLE_PLANT
                && state.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            return pos.down();
        }
        return pos;
    }

    private boolean isSoftPlant(IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.PLANTS
                || material == Material.VINE
                || material == Material.LEAVES;
    }

    private ItemStack getHarvestable(IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.WATERLILY) {
            return new ItemStack(ModBlocks.OANNA_BLOOM_ITEM);
        }
        if (block == Blocks.TALLGRASS && state.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.FERN) {
            return new ItemStack(ModBlocks.AVENNIAN_SPRIG_ITEM);
        }
        if (block == Blocks.RED_FLOWER
                && state.getValue(((BlockFlower) block).getTypeProperty()) == BlockFlower.EnumFlowerType.OXEYE_DAISY) {
            return new ItemStack(ModBlocks.MERAMMER_ROOT_ITEM);
        }
        if (block == Blocks.LEAVES
                && state.getValue(BlockOldLeaf.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
            return new ItemStack(ModBlocks.SILDRIAN_SEED_ITEM);
        }
        return ItemStack.EMPTY;
    }

    public static List<HarvestEntry> getHarvestEntries() {
        return HARVEST_ENTRIES;
    }

    public static final class HarvestEntry {
        private final List<ItemStack> sources;
        private final Supplier<ItemStack> result;
        private final String sourceKey;

        private HarvestEntry(List<ItemStack> sources, Supplier<ItemStack> result, String sourceKey) {
            this.sources = sources;
            this.result = result;
            this.sourceKey = sourceKey;
        }

        public List<ItemStack> getSources() {
            return sources;
        }

        public ItemStack getResult() {
            return result.get();
        }

        public String getSourceKey() {
            return sourceKey;
        }
    }
}
