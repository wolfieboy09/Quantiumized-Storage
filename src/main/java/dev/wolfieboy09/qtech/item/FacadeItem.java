package dev.wolfieboy09.qtech.item;

import dev.wolfieboy09.qtech.api.components.FacadeComponent;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qtech.component.QTDataComponents;
import dev.wolfieboy09.qtech.registries.QTDamageTypes;
import dev.wolfieboy09.qtech.registries.QTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FacadeItem extends Item {
    public FacadeItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createFacade(Block block) {
        ItemStack stack = new ItemStack(QTItems.FACADE.get());
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (id != null) {
            FacadeComponent tag = stack.get(QTDataComponents.FACADE_COMPONENT);
            if (tag == null) {
                stack.set(QTDataComponents.FACADE_COMPONENT, new FacadeComponent(id.toString()));
            }
        }
        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level == null || level.isClientSide) return InteractionResult.PASS;

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();

        if (!stack.has(QTDataComponents.FACADE_COMPONENT)) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!(block instanceof BasePipeBlock<?> pipeBlock)) {
            return InteractionResult.PASS;
        }

        Block facadeBlock = getRepresentedBlock(stack);
        if (facadeBlock == null) return InteractionResult.FAIL;

        if (state.hasProperty(BasePipeBlock.FACADING)) {
            state = state.setValue(BasePipeBlock.FACADING, true);
            level.setBlockAndUpdate(pos, state);
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BasePipeBlockEntity<?> pipeEntity) {
            pipeEntity.updateFacadeBlock(facadeBlock.defaultBlockState());
        }

        if (player == null || !player.isCreative()) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }


    @Nullable
    public Block getRepresentedBlock(ItemStack stack) {
        return !stack.has(QTDataComponents.FACADE_COMPONENT) ? null : BuiltInRegistries.BLOCK.get(stack.get(QTDataComponents.FACADE_COMPONENT).parsedId());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(QTDataComponents.FACADE_COMPONENT)) {
            tooltipComponents.add(Component.literal("Holding: " + stack.get(QTDataComponents.FACADE_COMPONENT).id()));
        }
    }
}
