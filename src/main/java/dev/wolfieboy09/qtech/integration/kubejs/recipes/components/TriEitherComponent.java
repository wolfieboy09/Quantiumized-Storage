package dev.wolfieboy09.qtech.integration.kubejs.recipes.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.RecipeValidationContext;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.kubejs.util.OpsContainer;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.codecs.TriEitherCodec;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.api.util.TriEither;

import java.util.List;

@NothingNullByDefault
public record TriEitherComponent<L, M, R>(
        RecipeComponent<L> left,
        RecipeComponent<M> middle,
        RecipeComponent<R> right,
        Codec<TriEither<L, M, R>> codec,
        TypeInfo typeInfo
) implements RecipeComponent<TriEither<L, M, R>> {
    public static final RecipeComponentType<?> TYPE =
            RecipeComponentType.<TriEitherComponent<?, ?, ?>>dynamic(
                    ResourceHelper.asResource("tri_either"),
                    (type, ctx) -> RecordCodecBuilder.mapCodec(instance -> instance.group(
                            ctx.recipeComponentCodec().fieldOf("left").forGetter(TriEitherComponent::left),
                            ctx.recipeComponentCodec().fieldOf("middle").forGetter(TriEitherComponent::middle),
                            ctx.recipeComponentCodec().fieldOf("right").forGetter(TriEitherComponent::right)
                    ).apply(instance, TriEitherComponent::new))
            );


    public TriEitherComponent(RecipeComponent<L> left, RecipeComponent<M> middle, RecipeComponent<R> right) {
        this(left, middle, right, TriEitherCodec.triEither(left.codec(), middle.codec(), right.codec()), left.typeInfo().or(middle.typeInfo().or(right.typeInfo())));
    }

    public static <L, M, R> TriEitherComponent<L, M, R> of(RecipeComponent<L> left, RecipeComponent<M> middle, RecipeComponent<R> right) {
        return new TriEitherComponent<>(left, middle, right);
    }

    @Override
    public RecipeComponentType<?> type() {
        return TYPE;
    }

    @Override
    public TypeInfo typeInfo() {
        return typeInfo;
    }

    @Override
    public TriEither<L, M, R> wrap(RecipeScriptContext cx, Object from) {
        if (left.hasPriority(cx, from)) {
            var value = left.wrap(cx, from);
            if (left.allowEmpty() || !left.isEmpty(value)) {
                return TriEither.left(value);
            }
        }
        if (middle.hasPriority(cx, from)) {
            var value = middle.wrap(cx, from);
            if (middle.allowEmpty() || !middle.isEmpty(value)) {
                return TriEither.middle(value);
            }
        }
        if (right.hasPriority(cx, from)) {
            var value = right.wrap(cx, from);
            if (right.allowEmpty() || !right.isEmpty(value)) {
                return TriEither.right(value);
            }
        }

        Exception ex1 = null;
        Exception ex2 = null;

        try {
            var value = left.wrap(cx, from);

            if (left.allowEmpty() || !left.isEmpty(value)) {
                left.validate(cx, value);
                return TriEither.left(value);
            }
        } catch (Exception ex) {
            ex1 = ex;
        }
        try {
            var value = middle.wrap(cx, from);

            if (middle.allowEmpty() || !middle.isEmpty(value)) {
                middle.validate(cx, value);
                return TriEither.middle(value);
            }
        } catch (Exception ex) {
            ex2 = ex;
        }
        try {
            var value = right.wrap(cx, from);

            if (right.allowEmpty() || !right.isEmpty(value)) {
                right.validate(cx, value);
                return TriEither.right(value);
            }
        } catch (Exception ex) {
            ConsoleJS.SERVER.warn("Failed to read %s (left %s)!".formatted(from, left), ex1);
            ConsoleJS.SERVER.warn("Failed to read %s (middle %s)!".formatted(from, middle), ex2);
            ConsoleJS.SERVER.warn("Failed to read %s (right %s)!".formatted(from, right), ex);
        }

        throw new KubeRuntimeException("Failed to read %s as tri-either %s, %s or %s!".formatted(from, left, middle, right)).source(cx.recipe().sourceLine);
    }

    @Override
    public boolean matches(RecipeMatchContext cx, TriEither<L, M, R> value, ReplacementMatchInfo match) {
        var l = value.left();
        var m = value.middle();
        var r = value.right();

        return l.map(l1 -> left.matches(cx, l1, match)).orElseGet(() -> m.map(m1 -> middle.matches(cx, m1, match)).orElseGet(() -> r.map(r1 -> right.matches(cx, r1, match)).orElseThrow()));
    }

    @Override
    public TriEither<L, M, R> replace(RecipeScriptContext cx, TriEither<L, M, R> original, ReplacementMatchInfo match, Object with) {
        var l = original.left();
        var m = original.middle();

        if (l.isPresent()) {
            var r = left.replace(cx, l.get(), match, with);
            return r == l.get() ? original : TriEither.left(r);
        } else if (m.isPresent()) {
            var r = middle.replace(cx, m.get(), match, with);
            return r == m.get() ? original : TriEither.middle(r);
        } else {
            var r = right.replace(cx, original.right().get(), match, with);
            return r == original.right().get() ? original : TriEither.right(r);
        }
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, TriEither<L, M, R> value) {
        var l = value.left();
        var m = value.middle();
        if (l.isPresent()) {
            left.buildUniqueId(builder, l.get());
        } else if (m.isPresent()) {
            middle.buildUniqueId(builder, m.get());
        } else {
            right.buildUniqueId(builder, value.right().get());
        }
    }

    @Override
    public void validate(RecipeValidationContext ctx, TriEither<L, M, R> value) {
        ctx.errors().push(this);
        var l = value.left();
        var m = value.middle();

        if (l.isPresent()) {
            ctx.errors().setKey("left");
            left.validate(ctx, l.get());
        } else if (m.isPresent()) {
            ctx.errors().setKey("middle");
            middle.validate(ctx, m.get());
        } else {
            ctx.errors().setKey("right");
            right.validate(ctx, value.right().get());
        }

        ctx.errors().pop();
    }

    @Override
    public String toString() {
        return "trieither<" + left + ", " + middle + ", " + right + ">";
    }

    @Override
    public String toString(OpsContainer ops, TriEither<L, M, R> value) {
        var l = value.left();
        var m = value.middle();

        if (l.isPresent()) {
            return left.toString(ops, l.get());
        } else if (m.isPresent()) {
            return middle.toString(ops, m.get());
        } else  {
            return right.toString(ops, value.right().get());
        }
    }

    @Override
    public List<?> spread(TriEither<L, M, R> value) {
        var l = value.left();
        var m = value.middle();

        if (l.isPresent()) {
            return left.spread(l.get());
        } else if (m.isPresent()) {
            return middle.spread(m.get());
        } else {
            return right.spread(value.right().get());
        }
    }
}
