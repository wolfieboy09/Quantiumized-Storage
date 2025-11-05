package dev.wolfieboy09.qtech.api.gas;

import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenCustomHashSet;

import javax.annotation.Nullable;
import java.util.Set;

public class GasStackLinkedSet {
    public static final Hash.Strategy<? super GasStack> TYPE_AND_COMPONENTS = new Hash.Strategy<>() {
        @Override
        public int hashCode(@Nullable GasStack stack) {
            return GasStack.hashGasAndComponents(stack);
        }

        @Override
        public boolean equals(@Nullable GasStack first, @Nullable GasStack second) {
            return first == second
                    || first != null
                    && second != null
                    && first.isEmpty() == second.isEmpty()
                    && GasStack.isSameGasSameComponents(first, second);
        }
    };

    public static Set<GasStack> createTypeAndComponentsSet() {
        return new ObjectLinkedOpenCustomHashSet<>(TYPE_AND_COMPONENTS);
    }
}
