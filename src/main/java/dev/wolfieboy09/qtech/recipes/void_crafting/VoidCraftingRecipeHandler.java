package dev.wolfieboy09.qtech.recipes.void_crafting;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.result.ItemStackChanceResult;
import dev.wolfieboy09.qtech.registries.QTRecipeTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

@NothingNullByDefault
@EventBusSubscriber(modid = QuantiumizedTech.MOD_ID)
public class VoidCraftingRecipeHandler {

    @SubscribeEvent
    public static void onItemTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof ItemEntity item)) return;

        Level level = item.level();
        if (level.isClientSide) return;

        DimensionType type = level.dimensionType();

        if (item.getPersistentData().getBoolean("IsResult")) {

            double targetY = item.getPersistentData().getDouble("TargetY");

            if (item.getY() < targetY) {
                item.setDeltaMovement(0, 0.05, 0);
            } else {
                item.setDeltaMovement(Vec3.ZERO);
                item.setNoGravity(true);
            }
            return;
        }

        if (!item.getPersistentData().contains("BaseY")) {
            item.getPersistentData().putDouble("BaseY", item.getY());
        }

        if (item.getY() <= type.minY()) {
            convertItem(item, level);
        }
    }

    private static void convertItem(ItemEntity item, Level level) {
        double baseY = item.getPersistentData().getDouble("BaseY");
        double targetY = baseY + 0.5;

        List<ItemEntity> nearbyItems = getNearbyItems(level, item);
        // Add the current item to the list so the recipe wrapper can get that as an ingredient
        nearbyItems.add(item);
        RecipeWrapper recipeWrapper = createRecipeWrapper(nearbyItems);

        Optional<RecipeHolder<VoidCraftingRecipe>> recipe = QTRecipeTypes.VOID_CRAFTING.find(recipeWrapper, level);

        if (recipe.isEmpty()) return;
        VoidCraftingRecipe craftingRecipe = recipe.get().value();

        // If the dimensions are empty, use that as a wildcard, otherise check
        if (craftingRecipe.getDimensions().isEmpty() || craftingRecipe.getDimensions().contains(level.dimension().location())) {
            spawnParticles(level, item.getX(), item.getY(), item.getZ());

            for (ItemStackChanceResult chanceResult : craftingRecipe.getRollableResults()) {
                Optional<ItemStack> rolledResult = chanceResult.getIfRolled();
                if (rolledResult.isEmpty()) continue;

                ItemEntity resultEntity = getResultEntity(item, level, rolledResult.get());

                resultEntity.getPersistentData().putBoolean("IsResult", true);
                resultEntity.getPersistentData().putDouble("TargetY", targetY);

                level.addFreshEntity(resultEntity);
                consumeIngredient(nearbyItems, craftingRecipe);
            }

            item.discard();
            for (ItemEntity nearbyItem : nearbyItems) {
                nearbyItem.discard();
            }
        }
    }

    private static ItemEntity getResultEntity(ItemEntity item, Level level, ItemStack rolledResult) {
        ItemEntity resultEntity = new ItemEntity(
                level,
                item.getX(),
                item.getY() + 0.05, // Prevent the void from deleting the result, so raise it up a bit
                item.getZ(),
                rolledResult,
                0,
                0,
                0
        );

        resultEntity.setNoGravity(true);
        resultEntity.setGlowingTag(true);
        resultEntity.setDeltaMovement(Vec3.ZERO);
        return resultEntity;
    }

    private static List<ItemEntity> getNearbyItems(Level level, ItemEntity center) {
        return level.getEntitiesOfClass(
                ItemEntity.class,
                center.getBoundingBox().inflate(2),
                e -> e != center && !e.getPersistentData().getBoolean("IsResult")
        );
    }


    private static void spawnParticles(Level level, double x, double y, double z) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 20; i++) {
                double angle = (i / 20.0) * Math.PI * 2;
                double offsetX = Math.cos(angle) * 0.5;
                double offsetZ = Math.sin(angle) * 0.5;

                serverLevel.sendParticles(
                        ParticleTypes.PORTAL,
                        x + offsetX,
                        y + 0.5,
                        z + offsetZ,
                        1,
                        0,
                        0,
                        0,
                        0.1
                );
            }

            for (int i = 0; i < 10; i++) {
                serverLevel.sendParticles(
                        ParticleTypes.ENCHANT,
                        x + (level.random.nextDouble() - 0.5),
                        y,
                        z + (level.random.nextDouble() - 0.5),
                        2,
                        0.1,
                        0.1,
                        0.1,
                        0.05
                );
            }
        }
    }

    private static void consumeIngredient(List<ItemEntity> items, VoidCraftingRecipe recipe) {
        for (ItemEntity itemEntity : items) {
            ItemStack stack = itemEntity.getItem();
            stack.shrink(1);

            if (stack.isEmpty()) {
                itemEntity.discard();
            } else {
                itemEntity.setItem(stack);
            }
        }
    }

    private static RecipeWrapper createRecipeWrapper(List<ItemEntity> items) {
        return new RecipeWrapper(new IItemHandler() {
            @Override
            public int getSlots() {
                return items.size();
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                if (slot < 0 || slot >= items.size()) return ItemStack.EMPTY;
                return items.get(slot).getItem();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return stack;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return true;
            }
        });
    }
}
