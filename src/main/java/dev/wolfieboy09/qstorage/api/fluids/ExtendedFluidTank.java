package dev.wolfieboy09.qstorage.api.fluids;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;


public class ExtendedFluidTank extends FluidTank {
    private final Runnable onContentsChangedCallback;
    public ExtendedFluidTank(int capacity, Runnable onContentsChangedCallback) {
        super(capacity);
        this.onContentsChangedCallback = onContentsChangedCallback;
    }

    @Override
    protected void onContentsChanged() {
        if (onContentsChangedCallback != null) {
            onContentsChangedCallback.run();
        }
    }
}
