package dev.wolfieboy09.qstorage.registries.util;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockStateGen {
    public static <T extends Block> void simpleCustomModel(@NotNull DataGenContext<Block, T> blockEntry, @NotNull RegistrateBlockstateProvider provider) {
        provider.simpleBlock(blockEntry.get(), provider.models().getExistingFile(provider.modLoc("block/" + blockEntry.getName())));
    }

}
