package dev.wolfieboy09.qtech.api.recipes.data.void_crafting;

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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
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

        if (item.getPersistentData().getBoolean("qtech_IsResult")) {

            double targetY = item.getPersistentData().getDouble("qtech_TargetY");

            if (item.getY() < targetY) {
                item.setDeltaMovement(0, 0.05, 0);
            } else {
                item.setDeltaMovement(Vec3.ZERO);
                item.setNoGravity(true);
            }
            return;
        }

        if (!item.getPersistentData().contains("qtech_BaseY")) {
            item.getPersistentData().putDouble("qtech_BaseY", item.getY());
        }

        if (item.getY() <= type.minY()) {
            convertItem(item, level);
        }
    }

    private static void convertItem(ItemEntity item, Level level) {
        double baseY = item.getPersistentData().getDouble("qtech_BaseY");
        double targetY = baseY + 0.5;

        List<ItemEntity> nearbyItems = getNearbyItems(level, item);
        nearbyItems.add(item);
        RecipeWrapper recipeWrapper = createRecipeWrapper(nearbyItems);

        Optional<RecipeHolder<VoidCraftingRecipe>> recipe = QTRecipeTypes.VOID_CRAFTING.find(recipeWrapper, level);

        if (recipe.isEmpty()) return;
        VoidCraftingRecipe craftingRecipe = recipe.get().value();

        // If the dimensions are empty, use that as a wildcard, otherwise check
        if (craftingRecipe.getDimensions().isEmpty() || craftingRecipe.getDimensions().contains(level.dimension().location())) {
            boolean anyResultsProduced = false;

            for (ItemStackChanceResult chanceResult : craftingRecipe.getRollableResults()) {
                Optional<ItemStack> rolledResult = chanceResult.getIfRolled(level);

                if (rolledResult.isPresent()) {
                    anyResultsProduced = true;
                    ItemStack resultStack = rolledResult.get().copy();

                    ItemEntity resultEntity = getResultEntity(item, level, resultStack);
                    resultEntity.getPersistentData().putBoolean("qtech_IsResult", true);
                    resultEntity.getPersistentData().putDouble("qtech_TargetY", targetY);

                    level.addFreshEntity(resultEntity);
                }
            }

            if (anyResultsProduced) {
                spawnParticles(level, item.getX(), item.getY(), item.getZ());
            } else {
                spawnFailedParticles(level, item.getX(), item.getY(), item.getZ());
            }

            // Always consume ingredients after attempting the craft
            consumeIngredients(nearbyItems, craftingRecipe);
        }
    }

    private static void consumeIngredients(List<ItemEntity> items, VoidCraftingRecipe recipe) {
        for (SizedIngredient sizedIngredient : recipe.getItemIngredients()) {
            if (sizedIngredient.ingredient().isEmpty()) continue;

            int toConsume = sizedIngredient.count();

            for (ItemEntity itemEntity : items) {
                if (toConsume <= 0) break;

                ItemStack stack = itemEntity.getItem();
                if (sizedIngredient.test(stack)) {
                    int consumed = Math.min(stack.getCount(), toConsume);
                    stack.shrink(consumed);
                    toConsume -= consumed;

                    if (stack.isEmpty()) {
                        itemEntity.discard();
                    } else {
                        itemEntity.setItem(stack);
                    }
                }
            }
        }
    }

    private static ItemEntity getResultEntity(ItemEntity item, Level level, ItemStack rolledResult) {
        ItemEntity resultEntity = new ItemEntity(
                level,
                item.getX(),
                item.getY() + 0.05,
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
                e -> e != center && !e.getPersistentData().getBoolean("qtech_IsResult")
        );
    }

    private static void spawnFailedParticles(Level level, double x, double y, double z) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 25; i++) {
                double angle = (i / 25.0) * Math.PI * 3;
                double height = (i / 25.0) * 0.5;
                double radius = 0.4 - (i / 25.0) * 0.3;

                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;

                serverLevel.sendParticles(
                        ParticleTypes.SOUL,
                        x + offsetX,
                        y + 0.5 - height,
                        z + offsetZ,
                        1,
                        0,
                        -0.02,
                        0,
                        0.01
                );
            }

            serverLevel.sendParticles(
                    ParticleTypes.WARPED_SPORE,
                    x,
                    y + 0.3,
                    z,
                    20,
                    0.3,
                    0.2,
                    0.3,
                    0.02
            );

            serverLevel.sendParticles(
                    ParticleTypes.POOF,
                    x,
                    y + 0.2,
                    z,
                    5,
                    0.1,
                    0.1,
                    0.1,
                    0.01
            );
        }
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