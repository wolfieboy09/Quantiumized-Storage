package dev.wolfieboy09.qtech.api.multiblock.blocks.controller;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockHatchRule;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockPattern;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockPatternManager;
import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatchEntity;
import dev.wolfieboy09.qtech.api.multiblock.tracking.MultiblockTracker;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import dev.wolfieboy09.qtech.packets.HideMultiblockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NothingNullByDefault
public class BaseMultiblockControllerEntity extends GlobalBlockEntity {
    private boolean formed = false;
    private MultiblockType multiblockType;
    protected @Nullable MultiblockPattern currentPattern = null;
    protected Set<BlockPos> trackedPositions = new HashSet<>();

    private final Map<Block, Set<BlockPos>> blockCache = new HashMap<>();
    private final Map<TagKey<Block>, Set<BlockPos>> tagCache = new HashMap<>();


    public BaseMultiblockControllerEntity(BlockEntityType<? extends BaseMultiblockControllerEntity> type, MultiblockType multiblockType, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.multiblockType = multiblockType;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("Formed", this.formed);
        tag.putString("MultiblockType", this.multiblockType.getLocation().toString());
        if (this.currentPattern != null) {
            tag.putString("PatternName", this.currentPattern.name());
        }

        ListTag positionsList = new ListTag();
        for (BlockPos pos : trackedPositions) {
            positionsList.add(LongTag.valueOf(pos.asLong()));
        }

        tag.put("TrackedPositions", positionsList);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        this.formed = tag.getBoolean("Formed");
        this.multiblockType = Objects.requireNonNull(QTRegistries.MULTIBLOCK_TYPE.get(ResourceLocation.parse(tag.getString("MultiblockType"))));
        if (tag.contains("PatternName")) {
            String patternName = tag.getString("PatternName");
            List<MultiblockPattern> patterns = MultiblockPatternManager.getAllPatternsForType(this.multiblockType);
            for (MultiblockPattern pattern : patterns) {
                if (pattern.name().equals(patternName)) {
                    this.currentPattern = pattern;
                    break;
                }
            }
        }

        this.trackedPositions.clear();
        invalidateCaches();
        if (tag.contains("TrackedPositions")) {
            ListTag positionsList = tag.getList("TrackedPositions", CompoundTag.TAG_LONG);
            for (Tag posTag : positionsList) {
                if (posTag instanceof LongTag longTag) {
                    this.trackedPositions.add(BlockPos.of(longTag.getAsLong()));
                }
            }
        }

        // Re-register with tracker if formed
        if (this.formed && this.level != null) {
            MultiblockTracker.registerMultiblock(this.level, getBlockPos(), this.trackedPositions);
        }

        super.loadAdditional(tag, registries);
    }

    public boolean isFormed() {
        return this.formed;
    }

    protected void formMultiblock(MultiblockPattern pattern) {
        if (this.level == null || this.level.isClientSide()) return;
        PacketDistributor.sendToAllPlayers(new HideMultiblockPattern());
        this.formed = true;
        this.currentPattern = pattern;

        // Get all positions that are part of this multiblock
        Map<BlockPos, Character> positions = pattern.getAllPositions(this.getBlockPos(), this.level.getBlockState(getBlockPos()).getValue(BaseMultiblockController.FACING));
        this.trackedPositions.clear();
        this.trackedPositions.addAll(positions.keySet());

        invalidateCaches();

        // Register this controller with the multiblock tracker
        MultiblockTracker.registerMultiblock(level, this.getBlockPos(), this.trackedPositions);

        // Mark controller as formed
        if (level != null) {
            BlockState state = getBlockState();
            if (state.hasProperty(BaseMultiblockController.FORMED)) {
                level.setBlock(getBlockPos(), state.setValue(BaseMultiblockController.FORMED, true), 3);
            }
        }

        // Call hook for subclasses
        onFormed(pattern);

        setChanged();
    }

    public void breakMultiblock() {
        if (!this.formed) return;

        this.formed = false;

        // Unregister from tracker
        MultiblockTracker.unregisterMultiblock(level, this.getBlockPos());

        invalidateCaches();
        // Call hook for subclasses
        onBroken();

        setChanged();
    }

    public void validateStructure() {
        if (!formed || currentPattern == null || level == null) return;

        // Check if structure is still valid
        if (!currentPattern.matches(level, getBlockPos(), getBlockState())) {
            breakMultiblock();
            // Update block state
            BlockState state = getBlockState();
            if (state.hasProperty(BaseMultiblockController.FORMED)) {
                level.setBlock(getBlockPos(), state.setValue(BaseMultiblockController.FORMED, false), 3);
            }
        }
    }


    public void attemptFormation() {
        if (this.level == null || this.level.isClientSide()) return;
        List<MultiblockPattern> possiblePatterns = MultiblockPatternManager.getAllPatternsForType(this.multiblockType);
        for (MultiblockPattern pattern : possiblePatterns) {
            if (pattern.matches(this.level, this.getBlockPos(), this.getBlockState())) {
                formMultiblock(pattern);
                return;
            }
        }
        this.formed = false;
    }

    public void tick() {}

    public Set<BlockPos> getBlocksOf(Block block) {
        if (this.level == null || this.level.isClientSide()) return Set.of();
        return this.blockCache.computeIfAbsent(block, b ->
                this.trackedPositions.stream()
                        .filter(pos -> this.level.getBlockState(pos).is(b))
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    public Set<BlockPos> getBlocksOf(TagKey<Block> tag) {
        if (this.level == null || this.level.isClientSide()) return Set.of();
        return this.tagCache.computeIfAbsent(tag, t ->
                this.trackedPositions.stream()
                        .filter(pos -> this.level.getBlockState(pos).is(t))
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    @OverridingMethodsMustInvokeSuper
    public void invalidateCaches() {
        this.blockCache.clear();
        this.tagCache.clear();
        this.trackedPositions.clear();
        this.currentPattern = null;
    }

    protected void onFormed(MultiblockPattern pattern) {
    }

    protected void onBroken() {
    }

    public Set<BlockPos> getTrackedPositions() {
        return Collections.unmodifiableSet(this.trackedPositions);
    }

    public Set<BlockPos> getHatches(BiPredicate<BaseMultiblockHatchEntity<?>, MultiblockHatchRule> filter) {
        if (this.level == null || this.level.isClientSide) return Set.of();

        Set<BlockPos> found = new HashSet<>();
        for (BlockPos pos : getTrackedPositions()) {
            BlockEntity blockEntity = this.level.getBlockEntity(pos);
            if (blockEntity instanceof BaseMultiblockHatchEntity<?> hatch
                    && filter.test(hatch, hatch.getHatchRules())) {
                found.add(pos);
            }
        }
        return found;
    }
}
