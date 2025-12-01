package dev.wolfieboy09.qtech.integration.kubejs.events.handler;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.wolfieboy09.qtech.api.events.void_crafting.VoidCraftingEvent;
import dev.wolfieboy09.qtech.api.recipes.data.void_crafting.VoidCraftingRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidCraftingEventHandler implements KubeEvent {
    private final VoidCraftingEvent event;

    public VoidCraftingEventHandler(VoidCraftingEvent event) {
        this.event = event;
    }

    @Info("Returns the recipe that is going to be made")
    public VoidCraftingRecipe getRecipe() {
        return this.event.getRecipe();
    }

    @Info("Returns the entity who threw the items. Can return null for no entity")
    public @Nullable Entity getThrower() {
        return this.event.getOwner();
    }

    @Info("Returns the level this recipe is being produced on")
    public ServerLevel getLevel() {
        // The event is posted on the server only... so we may as well
        return (ServerLevel) this.event.getLevel();
    }

    @Info("The position of where this recipe is being produced")
    public Vec3 getPosition() {
        return this.event.getPosition();
    }

    @Info("The item entities being consumed in this craft")
    public List<ItemEntity> getIngredients() {
        return this.event.getIngredients();
    }

    @Info("""
            The results that will be spawned (can be empty if all rolls failed)
            This can be modified. You can add/remove/change the results
            """)
    public List<ItemStack> getResults() {
        return this.event.getResults();
    }

    @Info("Whether any results will be produced from this craft")
    public boolean hasResults() {
        return this.event.hasResults();
    }
}
