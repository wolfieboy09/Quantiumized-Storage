package dev.wolfieboy09.qtech.registries.tags;

import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public final class QTBlockTags {
    public static final TagKey<Block> CLEANROOM_TILE = modTag("cleanroom_tile");
    public static final TagKey<Block> CLEANROOM_GLASS = modTag("cleanroom_glass");
    public static final TagKey<Block> CLEANROOM_FILTER = modTag("cleanroom_filter");
    public static final TagKey<Block> CLEANROOM_STERILIZER = modTag("cleanroom_sterilizer");
    public static final TagKey<Block> CLEANROOM_VACUUM = modTag("cleanroom_vacuum");

    private static @NotNull TagKey<Block> modTag(String path) {
        return BlockTags.create(ResourceHelper.asResource(path));
    }
}
