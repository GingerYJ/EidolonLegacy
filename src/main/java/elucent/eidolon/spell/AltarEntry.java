package elucent.eidolon.spell;

import net.minecraft.util.ResourceLocation;

public class AltarEntry {
    private final ResourceLocation key;
    private final double capacity;
    private final double power;
    private final boolean blockOffering;
    private final boolean plateOffering;

    AltarEntry(ResourceLocation key, double capacity, double power) {
        this(key, capacity, power, true, true);
    }

    AltarEntry(ResourceLocation key, double capacity, double power, boolean blockOffering, boolean plateOffering) {
        this.key = key;
        this.capacity = capacity;
        this.power = power;
        this.blockOffering = blockOffering;
        this.plateOffering = plateOffering;
    }

    void apply(AltarInfo info) {
        if (capacity > 0.0D) {
            info.increaseCapacity(key, capacity);
        }
        if (power > 0.0D) {
            info.increasePower(key, power);
        }
    }

    public double getCapacity() {
        return capacity;
    }

    public double getPower() {
        return power;
    }

    public ResourceLocation getKey() {
        return key;
    }

    public boolean canApplyFromBlock() {
        return blockOffering;
    }

    public boolean canApplyFromPlate() {
        return plateOffering;
    }
}
