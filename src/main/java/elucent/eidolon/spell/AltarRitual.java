package elucent.eidolon.spell;

import elucent.eidolon.item.IRechargeableWand;
import elucent.eidolon.item.SummoningStaffItem;
import elucent.eidolon.tile.AltarTileEntity;
import elucent.eidolon.tile.ItemHolderTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AltarRitual {
    public enum PerformResult {
        SUCCESS,
        NO_MATCH,
        ABSORPTION_TARGET_TOO_HEALTHY
    }

    public enum SetupResult {
        FAIL,
        PASS,
        SUCCEED
    }

    private static final class MatchResult {
        private final BlockPos focus;
        private final List<BlockPos> offerings;

        private MatchResult(BlockPos focus, List<BlockPos> offerings) {
            this.focus = focus;
            this.offerings = offerings;
        }

        private int size() {
            return offerings.size() + (focus == null ? 0 : 1);
        }
    }

    private static final class AbsorptionResult {
        private final Map<String, Integer> absorbed = new LinkedHashMap<>();

        private void add(String entityId) {
            absorbed.put(entityId, absorbed.containsKey(entityId) ? absorbed.get(entityId) + 1 : 1);
        }

        private int size() {
            int size = 0;
            for (Integer count : absorbed.values()) {
                size += count;
            }
            return size;
        }
    }

    public enum BehaviorType {
        ITEM_RESULT,
        ITEM_TRANSFORM,
        ITEM_CHARGE,
        ENTITY_SUMMON,
        ABSORPTION
    }

    private final ResourceLocation id;
    private final double requiredCapacity;
    private final double requiredPower;
    private final List<Ingredient> requiredOfferings;
    private final ItemStack result;
    private final BehaviorType behaviorType;
    private final Ingredient focus;
    private final Ingredient sacrifice;
    private final int providerOfferingStart;
    private final ResourceLocation entityId;
    private final float healthCost;

    AltarRitual(ResourceLocation id, double requiredCapacity, double requiredPower,
                ItemStack result, BehaviorType behaviorType, Ingredient focus, Ingredient... requiredOfferings) {
        this(id, requiredCapacity, requiredPower, result, behaviorType, focus, null, null, 0.0F, requiredOfferings);
    }

    AltarRitual(ResourceLocation id, double requiredCapacity, double requiredPower,
                ItemStack result, BehaviorType behaviorType, Ingredient focus, ResourceLocation entityId,
                Ingredient... requiredOfferings) {
        this(id, requiredCapacity, requiredPower, result, behaviorType, focus, null, entityId, 0.0F, requiredOfferings);
    }

    AltarRitual(ResourceLocation id, double requiredCapacity, double requiredPower,
                ItemStack result, BehaviorType behaviorType, Ingredient focus, ResourceLocation entityId,
                float healthCost, Ingredient... requiredOfferings) {
        this(id, requiredCapacity, requiredPower, result, behaviorType, focus, null, entityId, healthCost, requiredOfferings);
    }

    AltarRitual(ResourceLocation id, double requiredCapacity, double requiredPower,
                ItemStack result, BehaviorType behaviorType, Ingredient focus, Ingredient sacrifice,
                ResourceLocation entityId, float healthCost, Ingredient... requiredOfferings) {
        this.id = id;
        this.requiredCapacity = requiredCapacity;
        this.requiredPower = requiredPower;
        this.requiredOfferings = Collections.unmodifiableList(Arrays.asList(requiredOfferings));
        this.result = result.copy();
        this.behaviorType = behaviorType;
        this.focus = focus;
        this.sacrifice = sacrifice == null ? defaultSacrifice(result, focus, requiredOfferings) : sacrifice;
        this.providerOfferingStart = sacrifice == null && requiredOfferings.length > 0 ? 1 : 0;
        this.entityId = entityId;
        this.healthCost = healthCost;
    }

    private static Ingredient defaultSacrifice(ItemStack result, Ingredient focus, Ingredient[] offerings) {
        if (offerings.length > 0) {
            return offerings[0];
        }
        if (focus != Ingredient.EMPTY) {
            return focus;
        }
        return Ingredient.fromStacks(result.copy());
    }

    public ResourceLocation getId() {
        return id;
    }

    public double getRequiredCapacity() {
        return requiredCapacity;
    }

    public double getRequiredPower() {
        return requiredPower;
    }

    public List<Ingredient> getRequiredOfferings() {
        return requiredOfferings;
    }

    public List<Ingredient> getProviderOfferings() {
        return requiredOfferings.subList(Math.min(providerOfferingStart, requiredOfferings.size()), requiredOfferings.size());
    }

    public Ingredient getFocus() {
        return focus;
    }

    public Ingredient getSacrifice() {
        return sacrifice;
    }

    public boolean matchesSacrifice(ItemStack stack) {
        return matchesIngredient(sacrifice, stack, behaviorType == BehaviorType.ITEM_CHARGE);
    }

    public boolean hasFocus() {
        return focus != Ingredient.EMPTY;
    }

    public int getRequiredOfferingCount() {
        return requiredOfferings.size() + (hasFocus() ? 1 : 0);
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public BehaviorType getBehaviorType() {
        return behaviorType;
    }

    public ResourceLocation getEntityId() {
        return entityId;
    }

    public float getHealthCost() {
        return healthCost;
    }

    public boolean hasHealthCost() {
        return healthCost > 0.0F;
    }

    public SetupResult setupFromProviders(World world, BlockPos origin, int step) {
        List<Ingredient> providerOfferings = getProviderOfferings();
        if (step >= providerOfferings.size()) {
            return SetupResult.SUCCEED;
        }
        IRitualItemProvider provider = findProvider(world, origin, providerOfferings.get(step));
        if (provider == null) {
            return SetupResult.FAIL;
        }
        provider.take();
        return SetupResult.PASS;
    }

    public int getProviderOfferingCount() {
        return getProviderOfferings().size();
    }

    public boolean hasProviderFocusStep() {
        return hasFocus();
    }

    public PerformResult processFocusFromProviders(World world, BlockPos origin, EntityPlayer player) {
        if (!hasRequiredHealth(player)) {
            return PerformResult.NO_MATCH;
        }
        if (behaviorType == BehaviorType.ITEM_TRANSFORM) {
            return findFocusProvider(world, origin) == null ? PerformResult.NO_MATCH : PerformResult.SUCCESS;
        } else if (behaviorType == BehaviorType.ITEM_CHARGE) {
            return rechargeFocusFromProvider(world, origin);
        } else if (behaviorType == BehaviorType.ENTITY_SUMMON) {
            consumeFocusFromProvider(world, origin);
        } else if (behaviorType == BehaviorType.ABSORPTION) {
            return absorbFromFocusProvider(world, origin);
        }
        return PerformResult.SUCCESS;
    }

    public PerformResult finishFromProviders(World world, BlockPos origin, EntityPlayer player) {
        if (!hasRequiredHealth(player)) {
            return PerformResult.NO_MATCH;
        }
        consumeHealth(player);
        if (behaviorType == BehaviorType.ITEM_TRANSFORM) {
            return transformFocusFromProvider(world, origin);
        } else if (behaviorType == BehaviorType.ENTITY_SUMMON) {
            summonEntity(world, origin);
            playSuccessEffects(world, origin, EnumParticleTypes.SMOKE_LARGE, 24);
        } else if (behaviorType == BehaviorType.ITEM_RESULT) {
            spawnResult(world, origin);
            playSuccessEffects(world, origin, EnumParticleTypes.SPELL_WITCH, 18);
        }
        return PerformResult.SUCCESS;
    }

    public PerformResult performFromProviders(World world, BlockPos origin, EntityPlayer player) {
        if (!hasRequiredHealth(player)) {
            return PerformResult.NO_MATCH;
        }
        consumeHealth(player);
        if (behaviorType == BehaviorType.ITEM_TRANSFORM) {
            return transformFocusFromProvider(world, origin);
        } else if (behaviorType == BehaviorType.ITEM_CHARGE) {
            return rechargeFocusFromProvider(world, origin);
        } else if (behaviorType == BehaviorType.ENTITY_SUMMON) {
            consumeFocusFromProvider(world, origin);
            summonEntity(world, origin);
            playSuccessEffects(world, origin, EnumParticleTypes.SMOKE_LARGE, 24);
        } else if (behaviorType == BehaviorType.ABSORPTION) {
            return absorbFromFocusProvider(world, origin);
        } else {
            spawnResult(world, origin);
            playSuccessEffects(world, origin, EnumParticleTypes.SPELL_WITCH, 18);
        }
        return PerformResult.SUCCESS;
    }

    public boolean matches(AltarInfo info, EntityPlayer player) {
        MatchResult match = findMatch(info);
        return info.getOfferingCount() >= getRequiredOfferingCount()
                && match.size() == getRequiredOfferingCount()
                && hasAltarPower(info)
                && hasRequiredHealth(player);
    }

    public boolean hasAltarPower(AltarInfo info) {
        return info.getCapacity() >= requiredCapacity && info.getPower() >= requiredPower;
    }

    public boolean canStartFromProviders(World world, BlockPos origin, AltarInfo info) {
        if (!hasAltarPower(info)) {
            return false;
        }
        IRitualItemFocus focusProvider = null;
        if (hasFocus()) {
            focusProvider = findFocusProvider(world, origin);
            if (focusProvider == null) {
                return false;
            }
        }
        Set<TileEntity> used = new HashSet<>();
        if (focusProvider instanceof TileEntity) {
            used.add((TileEntity) focusProvider);
        }
        for (Ingredient ingredient : getProviderOfferings()) {
            TileEntity provider = findProviderTile(world, origin, ingredient, used);
            if (provider == null) {
                return false;
            }
            used.add(provider);
        }
        return true;
    }

    public PerformResult perform(World world, BlockPos origin, AltarInfo info, EntityPlayer player) {
        MatchResult match = findMatch(info);
        if (match.size() != getRequiredOfferingCount()) {
            return PerformResult.NO_MATCH;
        }
        if (!hasRequiredHealth(player)) {
            return PerformResult.NO_MATCH;
        }
        consumeHealth(player);
        if (behaviorType == BehaviorType.ITEM_TRANSFORM) {
            performItemTransform(world, origin, match);
        } else if (behaviorType == BehaviorType.ITEM_CHARGE) {
            performItemCharge(world, origin, match);
        } else if (behaviorType == BehaviorType.ENTITY_SUMMON) {
            performEntitySummon(world, origin, match);
        } else if (behaviorType == BehaviorType.ABSORPTION) {
            return performAbsorption(world, origin, match);
        } else if (behaviorType == BehaviorType.ITEM_RESULT) {
            performItemResult(world, origin, match);
        }
        return PerformResult.SUCCESS;
    }

    private void performItemResult(World world, BlockPos origin, MatchResult match) {
        consumeOfferings(world, match.offerings);
        spawnResult(world, origin);
        playSuccessEffects(world, origin, EnumParticleTypes.SPELL_WITCH, 18);
    }

    private void performItemTransform(World world, BlockPos origin, MatchResult match) {
        transformFocus(world, match.focus);
        consumeOfferings(world, match.offerings);
        playSuccessEffects(world, match.focus == null ? origin : match.focus, EnumParticleTypes.VILLAGER_HAPPY, 12);
    }

    private void performItemCharge(World world, BlockPos origin, MatchResult match) {
        rechargeFocus(world, match.focus);
        consumeOfferings(world, match.offerings);
        playSuccessEffects(world, match.focus == null ? origin : match.focus, EnumParticleTypes.SPELL, 18);
    }

    private void performEntitySummon(World world, BlockPos origin, MatchResult match) {
        consumeOffering(world, match.focus);
        consumeOfferings(world, match.offerings);
        summonEntity(world, origin);
        playSuccessEffects(world, origin, EnumParticleTypes.SMOKE_LARGE, 24);
    }

    private PerformResult performAbsorption(World world, BlockPos origin, MatchResult match) {
        AbsorptionResult absorbed = absorbNearbyUndead(world, origin);
        if (absorbed.size() <= 0) {
            return PerformResult.ABSORPTION_TARGET_TOO_HEALTHY;
        }
        chargeSummoningStaff(world, match.focus, absorbed.absorbed);
        consumeOfferings(world, match.offerings);
        playSuccessEffects(world, match.focus == null ? origin : match.focus, EnumParticleTypes.SPELL_MOB, 24);
        return PerformResult.SUCCESS;
    }

    private void consumeOfferings(World world, List<BlockPos> matchedOfferings) {
        for (BlockPos altarPos : matchedOfferings) {
            consumeOffering(world, altarPos);
        }
    }

    private void transformFocus(World world, BlockPos focusPos) {
        if (focusPos != null) {
            TileEntity tile = world.getTileEntity(focusPos);
            if (tile instanceof AltarTileEntity) {
                ((AltarTileEntity) tile).setOffering(result.copy());
            }
        }
    }

    private void rechargeFocus(World world, BlockPos focusPos) {
        if (focusPos != null) {
            TileEntity tile = world.getTileEntity(focusPos);
            if (tile instanceof AltarTileEntity) {
                AltarTileEntity altar = (AltarTileEntity) tile;
                ItemStack stack = altar.getOffering();
                if (!stack.isEmpty() && stack.getItem() instanceof IRechargeableWand) {
                    altar.setOffering(((IRechargeableWand) stack.getItem()).recharge(stack));
                }
            }
        }
    }

    private void chargeSummoningStaff(World world, BlockPos focusPos, Map<String, Integer> absorbed) {
        if (focusPos != null) {
            TileEntity tile = world.getTileEntity(focusPos);
            if (tile instanceof AltarTileEntity) {
                AltarTileEntity altar = (AltarTileEntity) tile;
                ItemStack stack = altar.getOffering();
                if (!stack.isEmpty() && stack.getItem() instanceof SummoningStaffItem) {
                    altar.setOffering(((SummoningStaffItem) stack.getItem()).addAbsorbedUndead(stack, absorbed));
                }
            }
        }
    }

    private void consumeOffering(World world, BlockPos altarPos) {
        if (altarPos != null) {
            TileEntity tile = world.getTileEntity(altarPos);
            if (tile instanceof AltarTileEntity) {
                ((AltarTileEntity) tile).removeOffering();
            }
        }
    }

    private void spawnResult(World world, BlockPos origin) {
        EntityItem item = new EntityItem(world, origin.getX() + 0.5D, origin.getY() + 1.1D, origin.getZ() + 0.5D, result.copy());
        world.spawnEntity(item);
    }

    private void summonEntity(World world, BlockPos origin) {
        if (entityId == null) {
            return;
        }
        Entity entity = EntityList.createEntityByIDFromName(entityId, world);
        if (entity == null) {
            return;
        }
        entity.setPosition(origin.getX() + 0.5D, origin.getY() + 1.5D, origin.getZ() + 0.5D);
        if (entity instanceof EntityLiving) {
            ((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(origin.up()), null);
        }
        world.spawnEntity(entity);
    }

    private AbsorptionResult absorbNearbyUndead(World world, BlockPos origin) {
        AxisAlignedBB bounds = new AxisAlignedBB(origin).grow(8.0D, 4.0D, 8.0D);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds,
                entity -> entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD
                        && !entity.getEntityData().getBoolean(SummoningStaffItem.SUMMONED_TAG)
                        && entity.getHealth() <= entity.getMaxHealth() / 5.0F);
        AbsorptionResult absorbed = new AbsorptionResult();
        for (EntityLivingBase entity : entities) {
            if (entity instanceof EntityPlayer) {
                continue;
            }
            ResourceLocation id = EntityList.getKey(entity);
            if (id != null) {
                absorbed.add(id.toString());
            }
            entity.setDead();
        }
        return absorbed;
    }

    private boolean hasRequiredHealth(EntityPlayer player) {
        return !hasHealthCost() || player != null && player.getHealth() >= healthCost;
    }

    private void consumeHealth(EntityPlayer player) {
        if (hasHealthCost() && player != null) {
            player.setHealth(Math.max(0.0F, player.getHealth() - healthCost));
        }
    }

    private void playSuccessEffects(World world, BlockPos pos, EnumParticleTypes particle, int count) {
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D,
                SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 0.45F, 1.35F);
        if (world instanceof WorldServer) {
            ((WorldServer) world).spawnParticle(particle,
                    pos.getX() + 0.5D, pos.getY() + 1.05D, pos.getZ() + 0.5D,
                    count, 0.28D, 0.2D, 0.28D, 0.02D);
        }
    }

    private IRitualItemProvider findProvider(World world, BlockPos origin, Ingredient ingredient) {
        TileEntity tile = findProviderTile(world, origin, ingredient, Collections.emptySet());
        return tile instanceof IRitualItemProvider ? (IRitualItemProvider) tile : null;
    }

    private TileEntity findProviderTile(World world, BlockPos origin, Ingredient ingredient, Set<TileEntity> used) {
        AxisAlignedBB bounds = new AxisAlignedBB(origin).grow(8.0D, 6.0D, 8.0D);
        for (TileEntity tile : getTilesWithin(world, bounds)) {
            if (used.contains(tile)) {
                continue;
            }
            if (tile instanceof IRitualItemFocus) {
                continue;
            }
            if (tile instanceof IRitualItemProvider) {
                IRitualItemProvider provider = (IRitualItemProvider) tile;
                if (matchesIngredient(ingredient, provider.provide(), false)) {
                    return tile;
                }
            }
        }
        return null;
    }

    private IRitualItemFocus findFocusProvider(World world, BlockPos origin) {
        AxisAlignedBB bounds = new AxisAlignedBB(origin).grow(8.0D, 6.0D, 8.0D);
        for (TileEntity tile : getTilesWithin(world, bounds)) {
            if (tile instanceof IRitualItemFocus) {
                IRitualItemFocus focusProvider = (IRitualItemFocus) tile;
                if (!hasFocus() || matchesIngredient(focus, focusProvider.provide(), behaviorType == BehaviorType.ITEM_CHARGE)) {
                    return focusProvider;
                }
            }
        }
        return null;
    }

    private List<TileEntity> getTilesWithin(World world, AxisAlignedBB bounds) {
        List<TileEntity> tiles = new ArrayList<>();
        int minX = (int) Math.floor(bounds.minX);
        int minY = (int) Math.floor(bounds.minY);
        int minZ = (int) Math.floor(bounds.minZ);
        int maxX = (int) Math.ceil(bounds.maxX);
        int maxY = (int) Math.ceil(bounds.maxY);
        int maxZ = (int) Math.ceil(bounds.maxZ);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                    if (tile != null) {
                        tiles.add(tile);
                    }
                }
            }
        }
        return tiles;
    }

    private void consumeFocusFromProvider(World world, BlockPos origin) {
        IRitualItemFocus focusProvider = findFocusProvider(world, origin);
        if (focusProvider != null) {
            focusProvider.take();
        }
    }

    private PerformResult transformFocusFromProvider(World world, BlockPos origin) {
        IRitualItemFocus focusProvider = findFocusProvider(world, origin);
        if (focusProvider == null) {
            return PerformResult.NO_MATCH;
        }
        focusProvider.replace(result.copy());
        playFocusProviderEffect(focusProvider);
        playSuccessEffects(world, origin, EnumParticleTypes.VILLAGER_HAPPY, 12);
        return PerformResult.SUCCESS;
    }

    private PerformResult rechargeFocusFromProvider(World world, BlockPos origin) {
        IRitualItemFocus focusProvider = findFocusProvider(world, origin);
        if (focusProvider == null) {
            return PerformResult.NO_MATCH;
        }
        ItemStack stack = focusProvider.provide();
        if (!stack.isEmpty() && stack.getItem() instanceof IRechargeableWand) {
            focusProvider.replace(((IRechargeableWand) stack.getItem()).recharge(stack));
            playFocusProviderEffect(focusProvider);
            playSuccessEffects(world, origin, EnumParticleTypes.SPELL, 18);
            return PerformResult.SUCCESS;
        }
        return PerformResult.NO_MATCH;
    }

    private PerformResult absorbFromFocusProvider(World world, BlockPos origin) {
        IRitualItemFocus focusProvider = findFocusProvider(world, origin);
        if (focusProvider == null) {
            return PerformResult.NO_MATCH;
        }
        AbsorptionResult absorbed = absorbNearbyUndead(world, origin);
        if (absorbed.size() <= 0) {
            return PerformResult.ABSORPTION_TARGET_TOO_HEALTHY;
        }
        ItemStack stack = focusProvider.provide();
        if (!stack.isEmpty() && stack.getItem() instanceof SummoningStaffItem) {
            focusProvider.replace(((SummoningStaffItem) stack.getItem()).addAbsorbedUndead(stack, absorbed.absorbed));
            playFocusProviderEffect(focusProvider);
            playSuccessEffects(world, origin, EnumParticleTypes.SPELL_MOB, 24);
            return PerformResult.SUCCESS;
        }
        return PerformResult.NO_MATCH;
    }

    private void playFocusProviderEffect(IRitualItemFocus focusProvider) {
        if (focusProvider instanceof ItemHolderTileEntity) {
            ((ItemHolderTileEntity) focusProvider).playFocusEffect();
        }
    }

    private MatchResult findMatch(AltarInfo info) {
        List<BlockPos> matchedOfferings = new ArrayList<>();
        Set<BlockPos> used = new HashSet<>();
        BlockPos focusMatch = null;
        if (hasFocus()) {
            focusMatch = findOffering(info, focus, used, behaviorType == BehaviorType.ITEM_CHARGE);
            if (focusMatch == null) {
                return new MatchResult(null, Collections.emptyList());
            }
            used.add(focusMatch);
        }
        for (Ingredient ingredient : requiredOfferings) {
            BlockPos match = findOffering(info, ingredient, used, false);
            if (match == null) {
                return new MatchResult(null, Collections.emptyList());
            }
            matchedOfferings.add(match);
            used.add(match);
        }
        return new MatchResult(focusMatch, matchedOfferings);
    }

    private BlockPos findOffering(AltarInfo info, Ingredient ingredient, Set<BlockPos> used, boolean ignoreDamage) {
        for (BlockPos altarPos : info.getAltarPositions()) {
            if (used.contains(altarPos)) {
                continue;
            }
            ItemStack offering = info.getOffering(altarPos);
            if (!offering.isEmpty() && matchesIngredient(ingredient, offering, ignoreDamage)) {
                return altarPos;
            }
        }
        return null;
    }

    private boolean matchesIngredient(Ingredient ingredient, ItemStack stack, boolean ignoreDamage) {
        if (ingredient.apply(stack)) {
            return true;
        }
        if (!ignoreDamage) {
            return false;
        }
        for (ItemStack match : ingredient.getMatchingStacks()) {
            if (!match.isEmpty() && match.getItem() == stack.getItem()) {
                return true;
            }
        }
        return false;
    }
}
