package dev.wolfieboy09.qtech.api.events.void_crafting;

import dev.wolfieboy09.qtech.api.recipes.data.void_crafting.VoidCraftingRecipe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidCraftingEvent extends Event implements IModBusEvent, ICancellableEvent {
    private final VoidCraftingRecipe recipe;
    private final Entity owner;
    private final Level level;
    private final Vec3 position;
    private final List<ItemEntity> ingredients;
    private final List<ItemStack> results;

    public VoidCraftingEvent(VoidCraftingRecipe recipe, @Nullable Entity owner, Level level, Vec3 position, List<ItemEntity> ingredients, List<ItemStack> results) {
        this.recipe = recipe;
        this.owner = owner;
        this.level = level;
        this.position = position;
        this.ingredients = ingredients;
        this.results = results;
    }

    public VoidCraftingRecipe getRecipe() {
        return this.recipe;
    }

    public @Nullable Entity getOwner() {
        return this.owner;
    }

    public Level getLevel() {
        return this.level;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    /**
     * The item entities being consumed in this craft
     */
    public List<ItemEntity> getIngredients() {
        return this.ingredients;
    }

    /**
     * The results that will be spawned (can be empty if all rolls failed)
     * This is modifiable - you can add/remove/change results
     */
    public List<ItemStack> getResults() {
        return this.results;
    }

    /**
     * Whether any results will be produced from this craft
     */
    public boolean hasResults() {
        return !this.results.isEmpty();
    }
}
