package name.soy.moreparticle.mixin;

import name.soy.moreparticle.client.MoreParticleClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
//	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"))
//	private <K, V> V addUnlimitedQuene(Map<K, V> instance, K k, Function<? super K, ? extends V> function) {
//		return instance.computeIfAbsent(k, a-> (V) new LinkedList<>());
//	}

	@Redirect(method = "tickParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;isAlive()Z"))
	private boolean onTick(Particle instance) {
		boolean alive = instance.isAlive();
		if (!alive) {
			String tag = MoreParticleClient.tagParticles.remove(instance);
			if (tag != null) {
				MoreParticleClient.particleTags.get(tag).remove(instance);
			}
		}
		return alive;
	}

}
