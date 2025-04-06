package dev.wolfieboy09.qstorage.mixins;

import dev.latvian.mods.kubejs.recipe.component.EitherRecipeComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EitherRecipeComponent.class)
public class EitherRecipeComponentMixin {
    // Thanks to Uncandango for this mixin that fixes kubejs on 7.1

    @Redirect(method = "validate(Lcom/mojang/datafixers/util/Either;)V", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isEmpty()Z"))
    private boolean fixEmptyCheck(Optional instance){
        return instance.isPresent();
    }
}