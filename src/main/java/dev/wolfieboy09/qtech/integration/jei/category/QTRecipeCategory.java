package dev.wolfieboy09.qtech.integration.jei.category;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.IRecipeTypeInfo;
import dev.wolfieboy09.qtech.api.recipes.result.ChanceResult;
import dev.wolfieboy09.qtech.integration.jei.QTJEIPlugin;
import dev.wolfieboy09.qtech.registries.QTGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("removal")
@NothingNullByDefault
public abstract class QTRecipeCategory<T extends Recipe<?>> implements IRecipeCategory<RecipeHolder<T>> {
    private static final IDrawable BASIC_SLOT = asDrawable(QTGuiTextures.JEI_SLOT);
    private static final IDrawable CHANCE_SLOT = asDrawable(QTGuiTextures.JEI_CHANCE_SLOT);

    protected final RecipeType<RecipeHolder<T>> type;
    protected final Component title;
    protected final IDrawable background;
    protected final IDrawable icon;

    private final Supplier<List<RecipeHolder<T>>> recipes;
    private final List<Supplier<? extends ItemStack>> catalysts;

    public QTRecipeCategory(Info<T> info) {
        this.type = info.recipeType();
        this.title = info.title();
        this.background = info.background();
        this.icon = info.icon();
        this.recipes = info.recipes();
        this.catalysts = info.catalysts();
    }

    @Override
    public RecipeType<RecipeHolder<T>> getRecipeType() {
        return type;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<T> holder, IFocusGroup focuses) {
        setRecipe(builder, holder.value(), focuses);
    }

    @Override
    public void draw(RecipeHolder<T> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY) {
        draw(holder.value(), recipeSlotsView, gui, mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipStrings(RecipeHolder<T> holder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return getTooltipStrings(holder.value(), recipeSlotsView, mouseX, mouseY);
    }

    protected abstract void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses);

    protected abstract void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY);

    protected List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return List.of();
    }

    public static IRecipeSlotRichTooltipCallback addChanceTooltip(ChanceResult<?> output) {
        return (view, tooltip) -> {
            float chance = output.getChance();
            if (chance != 1) {
                tooltip.add(Component.translatable("qtech.recipe.processing.chance", chance < 0.01 ? "< 1" : (int) (chance * 100)).withStyle(ChatFormatting.GOLD));
            }
        };
    }

    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(type, recipes.get());
    }

    public void registerCatalysts(IRecipeCatalystRegistration registration) {
        catalysts.forEach(s -> registration.addRecipeCatalyst(s.get(), type));
    }

    public IDrawable getRenderedSlot() {
        return BASIC_SLOT;
    }

    public IDrawable getRenderedSlot(ChanceResult<?> output) {
        return getRenderedSlot(output.getChance());
    }

    public IDrawable getRenderedSlot(float chance) {
        return chance == 1 ? BASIC_SLOT : CHANCE_SLOT;
    }

    public static ItemStack getResultItem(Recipe<?> recipe) {
        ClientLevel level = Minecraft.getInstance().level;
        return level == null ? ItemStack.EMPTY : recipe.getResultItem(level.registryAccess());
    }

    protected static IDrawable asDrawable(QTGuiTextures texture) {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return texture.getWidth();
            }

            @Override
            public int getHeight() {
                return texture.getHeight();
            }

            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
                texture.render(guiGraphics, xOffset, yOffset);
            }
        };
    }




    public record Info<T extends Recipe<?>>(RecipeType<RecipeHolder<T>> recipeType, Component title, IDrawable background,
                                            IDrawable icon, Supplier<List<RecipeHolder<T>>> recipes,
                                            List<Supplier<? extends ItemStack>> catalysts) {
    }

    public interface Factory<T extends Recipe<?>> {
        QTRecipeCategory<T> create(Info<T> info);
    }

    public static class Builder<T extends Recipe<? extends RecipeInput>> {
        private final Class<? extends T> recipeClass;
        private Supplier<Boolean> config = () -> true;

        private IDrawable background;
        private IDrawable icon;

        private final List<Consumer<List<RecipeHolder<T>>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

        public Builder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public Builder<T> enableWhen(Supplier<Boolean> predicate) {
            this.config = predicate;
            return this;
        }

        public Builder<T> addRecipeListConsumer(Consumer<List<RecipeHolder<T>>> consumer) {
            this.recipeListConsumers.add(consumer);
            return this;
        }

        public Builder<T> addRecipes(Supplier<Collection<? extends RecipeHolder<T>>> collection) {
            return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
        }

        public Builder<T> catalystStack(Supplier<ItemStack> supplier) {
            this.catalysts.add(supplier);
            return this;
        }

        public Builder<T> catalyst(Supplier<ItemLike> supplier) {
            return catalystStack(() -> new ItemStack(supplier.get().asItem()));
        }

        public Builder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }


        public Builder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return addTypedRecipes(recipeTypeEntry::getType);
        }

        public <I extends RecipeInput, R extends Recipe<I>> Builder<T> addTypedRecipes(Supplier<net.minecraft.world.item.crafting.RecipeType<R>> recipeType) {
            return addRecipeListConsumer(recipes -> QTJEIPlugin.<T>consumeTypedRecipes(recipe -> {
                if (recipeClass.isInstance(recipe.value()))
                    //noinspection unchecked - checked by if statement above
                    recipes.add((RecipeHolder<T>) recipe);
            }, recipeType.get()));
        }

        public QTRecipeCategory<T> build(ResourceLocation location, Factory<T> factory) {
            Supplier<List<RecipeHolder<T>>> recipeSupplier;
            if (this.config.get()) {
                recipeSupplier = () -> {
                    List<RecipeHolder<T>> recipes = new ArrayList<>();
                    for (Consumer<List<RecipeHolder<T>>> consumer : this.recipeListConsumers) {
                        consumer.accept(recipes);
                    }
                    return recipes;
                };
            } else {
                recipeSupplier = Collections::emptyList;
            }
            Info<T> info = new Info<>(
                    RecipeType.createRecipeHolderType(location),
                    Component.translatable(location.getNamespace() + ".recipe." + location.getPath()),
                    this.background,
                    this.icon,
                    recipeSupplier,
                    this.catalysts
            );
            return factory.create(info);
        }
    }
}
