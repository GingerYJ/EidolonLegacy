package elucent.eidolon.spell;

import net.minecraft.util.ResourceLocation;

public class AltarEntry {
    private final ResourceLocation key;
    private final double capacity;
    private final double power;

    AltarEntry(ResourceLocation key, double capacity, double power) {
        this.key = key;
        this.capacity = capacity;
        this.power = power;
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
}
