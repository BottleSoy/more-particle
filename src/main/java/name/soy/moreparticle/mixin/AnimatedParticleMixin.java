package name.soy.moreparticle.mixin;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleAnimatedParticle.class)
public class AnimatedParticleMixin {
	@Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
	private void getType(CallbackInfoReturnable<ParticleRenderType> cir) {
		cir.setReturnValue(ParticleRenderType.PARTICLE_SHEET_OPAQUE);
	}
}
