package dev.wolfieboy09.qstorage.mixins;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderInstance.class)
public abstract class ShaderInstanceMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    private void inject(CallbackInfo ci) {
        Uniform uniform = ((ShaderInstance) (Object) this).getUniform("Severity");
        if (uniform != null) {
            uniform.set(3F);
        }
    }
}
