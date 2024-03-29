package name.soy.moreparticle.mixin;

import com.google.common.collect.EvictingQueue;
import name.soy.moreparticle.client.MoreParticleClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(ClientWorld world, TextureManager textureManager, CallbackInfo ci) {
		MoreParticleClient.pm = (ParticleManager) (Object) this;
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
