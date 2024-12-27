package name.soy.moreparticle.mixin;

import name.soy.moreparticle.client.MoreParticleClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(ClientLevel clientLevel, TextureManager textureManager, CallbackInfo ci) {
		MoreParticleClient.pm = (ParticleEngine) (Object) this;
	}
//	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"))
//	private <K, V> V addUnlimitedQuene(Map<K, V> instance, K k, Function<? super K, ? extends V> function) {
//		return instance.computeIfAbsent(k, a -> (V) EvictingQueue.create(1048576));
//	}

//	@Redirect(method = "tickParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;isAlive()Z"))
//	private boolean onTick(Particle instance) {
//		boolean alive = instance.isAlive();
//		if (!alive) {
//
//		}
//		return alive;
//	}

}
