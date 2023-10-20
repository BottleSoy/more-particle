package name.soy.moreparticle.mixin;

import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimatedParticle.class)
public class AnimatedParticleMixin {
	@Inject(method = "getType", at = @At("RETURN"), cancellable = true)
	private void getType(CallbackInfoReturnable<ParticleTextureSheet> cir) {
		cir.setReturnValue(ParticleTextureSheet.PARTICLE_SHEET_LIT);
	}
}
