package dev.wolfieboy09.qtech.integration.kubejs.wrappers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DynamicOps;
import dev.latvian.mods.kubejs.component.DataComponentWrapper;
import dev.latvian.mods.kubejs.util.RegExpKJS;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.wolfieboy09.qtech.api.gas.crafting.DataComponentGasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.EmptyGasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.SizedGasIngredient;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.api.tags.GasTags;
import dev.wolfieboy09.qtech.integration.kubejs.gas.NamespaceGasIngredient;
import dev.wolfieboy09.qtech.integration.kubejs.gas.RegExGasIngredient;
import dev.wolfieboy09.qtech.registries.QTGasses;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface GasWrapper {
    TypeInfo TYPE_INFO = TypeInfo.of(GasStack.class);
    TypeInfo GAS_TYPE_INFO = TypeInfo.of(Gas.class);
    TypeInfo INGREDIENT_TYPE_INFO = TypeInfo.of(GasIngredient.class);
    TypeInfo SIZED_INGREDIENT_TYPE_INFO = TypeInfo.of(SizedGasIngredient.class);

    SizedGasIngredient EMPTY_SIZED = new SizedGasIngredient(GasIngredient.empty(), 1000);

    @HideFromJS
    static GasStack wrap(RegistryAccessContainer registries, Object o) {
        if (o == null || o == GasStack.EMPTY || o == QTGasses.EMPTY || o == EmptyGasIngredient.INSTANCE) {
            return GasStack.EMPTY;
        } else if (o instanceof GasStack stack) {
            return stack;
        } else if (o instanceof Gas gas) {
            return new GasStack(gas, 1000);
        } else if (o instanceof GasIngredient in) {
            return in.hasNoGasses() ? GasStack.EMPTY : in.getStacks()[0];
        } else if (o instanceof SizedGasIngredient s) {
            return s.getGasses()[0];
        } else {
            return ofString(registries.nbt(), o.toString());
        }
    }

    static GasIngredient ingredientOf(GasIngredient of) {
        return of;
    }

    @HideFromJS
    static GasIngredient wrapIngredient(RegistryAccessContainer registries, Object o) {
        if (o == null || o == GasStack.EMPTY || o == QTGasses.EMPTY || o == EmptyGasIngredient.INSTANCE) {
            return EmptyGasIngredient.INSTANCE;
        } else if (o instanceof GasStack stack) {
            return GasIngredient.of(stack);
        } else if (o instanceof Gas gas) {
            return GasIngredient.of(gas);
        } else if (o instanceof GasIngredient in) {
            return in;
        } else if (o instanceof SizedGasIngredient s) {
            return s.ingredient();
        } else {
            return ingredientOfString(registries.nbt(), o.toString());
        }
    }

    static SizedGasIngredient sizedIngredientOf(SizedGasIngredient of) {
        return of;
    }

    @HideFromJS
    static SizedGasIngredient wrapSizedIngredient(RegistryAccessContainer registries, Object o) {
        if (o == null || o == GasStack.EMPTY || o == QTGasses.EMPTY || o == EmptyGasIngredient.INSTANCE) {
            return EMPTY_SIZED;
        } else if (o instanceof SizedGasIngredient s) {
            return s;
        } else if (o instanceof GasStack stack) {
            return SizedGasIngredient.of(stack);
        } else if (o instanceof Gas fluid) {
            return SizedGasIngredient.of(fluid, 1000);
        } else if (o instanceof GasIngredient in) {
            return new SizedGasIngredient(in, 1000);
        } else {
            return sizedIngredientOfString(registries.nbt(), o.toString());
        }
    }

    static GasStack of(GasStack o) {
        return o;
    }

    static GasStack of(GasStack o, int amount) {
        o.setAmount(amount);
        return o;
    }

    static GasStack of(GasStack o, DataComponentMap components) {
        o.applyComponents(components);
        return o;
    }

    static GasStack of(GasStack o, int amount, DataComponentMap components) {
        o.setAmount(amount);
        o.applyComponents(components);
        return o;
    }

    static Gas getType(ResourceLocation id) {
        return QTRegistries.GAS.get(id);
    }

    static List<String> getTypes() {
        List<String> types = new ArrayList<>();

        for (Gas gas : QTRegistries.GAS) {
            types.add(gas.getResourceLocation().toString());
        }

        return types;
    }

    static GasStack getEmpty() {
        return GasStack.EMPTY;
    }

    static boolean exists(ResourceLocation id) {
        return QTRegistries.GAS.containsKey(id);
    }

    static ResourceLocation getId(Gas gas) {
        return QTRegistries.GAS.getKey(gas);
    }

    static GasStack ofString(DynamicOps<Tag> registryOps, String s) {
        if (s.isEmpty() || s.equals("-") || s.equals("empty") || s.equals("qtech:empty")) {
            return GasStack.EMPTY;
        } else {
            try {
                StringReader reader = new StringReader(s);
                reader.skipWhitespace();

                if (!reader.canRead()) {
                    return GasStack.EMPTY;
                }

                return read(registryOps, new StringReader(s));
            } catch (CommandSyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static GasStack read(DynamicOps<Tag> registryOps, StringReader reader) throws CommandSyntaxException {
        if (!reader.canRead()) {
            return GasStack.EMPTY;
        }

        if (reader.peek() == '-') {
            return GasStack.EMPTY;
        }

        long amount = readGasAmount(reader);
        ResourceLocation gasId = ResourceLocation.read(reader);
        GasStack gasStack = new GasStack(Objects.requireNonNull(QTRegistries.GAS.get(gasId)), (int) amount);

        char next = reader.canRead() ? reader.peek() : 0;

        if (next == '[' || next == '{') {
            gasStack.applyComponents(DataComponentWrapper.readPatch(registryOps, reader));
        }

        return gasStack;
    }

    static GasIngredient ingredientOfString(DynamicOps<Tag> registryOps, String s) {
        if (s.isEmpty() || s.equals("-") || s.equals("empty") || s.equals("qtech:empty")) {
            return GasIngredient.empty();
        } else {
            try {
                StringReader reader = new StringReader(s);
                reader.skipWhitespace();

                if (!reader.canRead()) {
                    return GasIngredient.empty();
                }

                return readIngredient(registryOps, new StringReader(s));
            } catch (CommandSyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static GasIngredient readIngredient(DynamicOps<Tag> registryOps, StringReader reader) throws CommandSyntaxException {
        if (!reader.canRead()) {
            return GasIngredient.empty();
        }

        if (reader.peek() == '-') {
            return GasIngredient.empty();
        } else if (reader.peek() == '#') {
            reader.skip();
            ResourceLocation tag = ResourceLocation.read(reader);
            return GasIngredient.tag(GasTags.create(tag));
        } else if (reader.peek() == '@') {
            reader.skip();
            String id = reader.readString();
            return new NamespaceGasIngredient(id);
        } else if (reader.peek() == '/') {
            reader.skip();
            var pattern = RegExpKJS.read(reader);
            return new RegExGasIngredient(pattern);
        }

        ResourceLocation gasId = ResourceLocation.read(reader);
        Gas gas = QTRegistries.GAS.get(gasId);

        char next = reader.canRead() ? reader.peek() : 0;

        if (next == '[' || next == '{') {
            DataComponentPredicate components = DataComponentWrapper.readPredicate(registryOps, reader);

            if (components != DataComponentPredicate.EMPTY) {
                return new DataComponentGasIngredient(HolderSet.direct(gas.builtInRegistryHolder()), components, false);
            }
        }

        return GasIngredient.of(gas);
    }

    static SizedGasIngredient sizedIngredientOfString(DynamicOps<Tag> registryOps, String s) {
        if (s.isEmpty() || s.equals("-") || s.equals("empty") || s.equals("qtech:empty")) {
            return EMPTY_SIZED;
        } else {
            try {
                StringReader reader = new StringReader(s);
                reader.skipWhitespace();

                if (!reader.canRead()) {
                    return EMPTY_SIZED;
                }

                return readSizedIngredient(registryOps, new StringReader(s));
            } catch (CommandSyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static SizedGasIngredient readSizedIngredient(DynamicOps<Tag> registryOps, StringReader reader) throws CommandSyntaxException {
        if (!reader.canRead()) {
            return EMPTY_SIZED;
        }

        long amount = readGasAmount(reader);
        return new SizedGasIngredient(readIngredient(registryOps, reader), (int) amount);
    }

    static long readGasAmount(StringReader reader) throws CommandSyntaxException {
        if (reader.canRead() && StringReader.isAllowedNumber(reader.peek())) {
            double amountD = reader.readDouble();
            reader.skipWhitespace();

            if (reader.peek() == 'b' || reader.peek() == 'B') {
                reader.skip();
                reader.skipWhitespace();
                amountD *= 1000;
            }

            if (reader.peek() == '/') {
                reader.skip();
                reader.skipWhitespace();
                amountD = amountD / reader.readDouble();
            }

            long amount = (long) amountD;
            reader.expect('x');
            reader.skipWhitespace();

            if (amount < 1L) {
                throw new IllegalStateException("Gas amount smaller than 1 is now allowed!");
            }

            return amount;
        }

        return 1000;
    }
}
