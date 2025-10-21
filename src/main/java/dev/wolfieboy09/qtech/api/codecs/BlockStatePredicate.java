package dev.wolfieboy09.qtech.api.codecs;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

@NothingNullByDefault
public record BlockStatePredicate(Predicate<BlockState> test, String id) implements Predicate<BlockState> {
    @Override
    public boolean test(BlockState state) {
        return this.test.test(state);
    }

    private static BlockStatePredicate fromId(String id) {
        return switch (id) {
            case "air" -> new BlockStatePredicate(BlockBehaviour.BlockStateBase::isAir, "air");
            case "solid" -> new BlockStatePredicate(BlockState::isSolid, "solid");
            default -> new BlockStatePredicate(
                    s -> s.is(BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id))),
                    id
            );
        };
    }

    // it's the best way I could find it to be, so...
    public static final Codec<BlockStatePredicate> CODEC = Codec.STRING.xmap(
            BlockStatePredicate::fromId,
            BlockStatePredicate::id
    );

    public static final StreamCodec<ByteBuf, BlockStatePredicate> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(ByteBuf o, BlockStatePredicate blockStatePredicate) {
            ByteBufStrings.writeString(o, blockStatePredicate.id());
        }

        @Override
        public BlockStatePredicate decode(ByteBuf byteBuf) {
            return fromId(ByteBufStrings.readString(byteBuf));
        }
    };
}
