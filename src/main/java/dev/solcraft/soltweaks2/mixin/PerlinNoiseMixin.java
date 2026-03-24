package dev.solcraft.soltweaks2.mixin;

import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PerlinNoise.class)
public class PerlinNoiseMixin {
    @Inject(at=@At("TAIL"), method = "wrap", cancellable = true)
    private static void unwrap(double x, CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(x);
    }
}
