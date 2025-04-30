    package dev.wolfieboy09.qtech.block.pipe.pipes;

    import dev.wolfieboy09.qtech.block.pipe.BasePipeBlock;
    import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
    import dev.wolfieboy09.qtech.block.pipe.ConnectionType;
    import dev.wolfieboy09.qtech.registries.QTBlockEntities;
    import net.minecraft.core.BlockPos;
    import net.minecraft.core.Direction;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.block.state.BlockState;
    import net.neoforged.neoforge.capabilities.Capabilities;
    import net.neoforged.neoforge.items.IItemHandler;
    import net.neoforged.neoforge.items.ItemHandlerHelper;

    import javax.annotation.ParametersAreNonnullByDefault;
    import java.util.*;

    @ParametersAreNonnullByDefault
    public class ItemPipeBlockEntity extends BasePipeBlockEntity<IItemHandler> {
        public ItemPipeBlockEntity(BlockPos pos, BlockState state) {
            super(QTBlockEntities.ITEM_PIPE.get(), pos, state);
        }

        @Override
        protected Set<Direction> getExtractableDirections() {
            Set<Direction> directions = new HashSet<>();
            for (Direction dir : Direction.values()) {
                ConnectionType type = getBlockState().getValue(BasePipeBlock.getPropertyFromDirection(dir));
                if (type == ConnectionType.BLOCK_EXTRACT) {
                    directions.add(dir);
                }
            }
            return directions;
        }

        @Override
        protected boolean isInsertDirectionValid(BlockPos targetPipe, Direction direction) {
            if (this.level == null || this.level.isClientSide) return false;
            ConnectionType type = this.level.getBlockState(targetPipe).getValue(BasePipeBlock.getPropertyFromDirection(direction));
            return type == ConnectionType.BLOCK_NORMAL;
        }

        @Override
        protected IItemHandler getSourceAt(BlockPos pos, Direction direction) {
            return this.level == null || this.level.isClientSide ? null : this.level.getCapability(Capabilities.ItemHandler.BLOCK, pos, direction);
        }

        @Override
        protected IItemHandler getTargetAt(BlockPos pos, Direction direction) {
            return this.level == null || this.level.isClientSide ? null : this.level.getCapability(Capabilities.ItemHandler.BLOCK, pos, direction);
        }

        @Override
        protected boolean canExtract(IItemHandler source) {
            for (int i = 0; i < source.getSlots(); i++) {
                if (!source.extractItem(i, 1, true).isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean tryTransfer(IItemHandler source, IItemHandler target) {
            for (int i = 0; i < source.getSlots(); i++) {
                ItemStack extracted = source.extractItem(i, 1, true);
                if (extracted.isEmpty()) continue; // Go to next slot if unable to take out

                ItemStack remainder = ItemHandlerHelper.insertItem(target, extracted, false);
                if (remainder.isEmpty()) {
                    source.extractItem(i, 1, false);
                    return true;
                }
            }
            return false;
        }
    }
