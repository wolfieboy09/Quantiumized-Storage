package dev.wolfieboy09.qtech.api.nbt;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.EnumMap;
import java.util.Map;

@NothingNullByDefault
public class MapBoolNBT<T extends Enum<T> & StringRepresentable> implements INBTSerializable<CompoundTag> {
    private final Map<T, Boolean> data;
    private final Class<T> enumClass;

    public MapBoolNBT(Class<T> enumClass) {
        this.enumClass = enumClass;
        this.data = new EnumMap<>(enumClass);
        for (T constant : enumClass.getEnumConstants()) {
            this.data.put(constant, false);
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<T, Boolean> entry : this.data.entrySet()) {
            tag.putBoolean(entry.getKey().getSerializedName(), entry.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        for (T constant : this.enumClass.getEnumConstants()) {
            String key = constant.getSerializedName();
            if (tag.contains(key)) {
                this.data.put(constant, tag.getBoolean(key));
            }
        }
    }

    public void setValue(T key, boolean value) {
        this.data.put(key, value);
    }

    public boolean getValue(T key) {
        return this.data.getOrDefault(key, false);
    }

    public boolean containsKey(T key) {
        return this.data.containsKey(key);
    }

    public Map<T, Boolean> getMap() {
        return this.data;
    }
}
