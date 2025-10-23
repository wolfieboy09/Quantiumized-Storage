package dev.wolfieboy09.qtech.api.multiblock.blocks;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

@NothingNullByDefault
public class BaseMultiblockEntityController extends GlobalBlockEntity {
    private boolean assembled = false;
    private MultiblockType multiblockType;

    public BaseMultiblockEntityController(BlockEntityType<? extends BaseMultiblockEntityController> type, MultiblockType multiblockType, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.multiblockType = multiblockType;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("Assembled", this.assembled);
        tag.putString("MultiblockType", this.multiblockType.toString());
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        this.assembled = tag.getBoolean("Assembled");
        this.multiblockType = Objects.requireNonNull(QTRegistries.MULTIBLOCK_TYPE.get(ResourceLocation.parse(tag.getString("MultiblockType"))));
        super.loadAdditional(tag, registries);
    }
}
