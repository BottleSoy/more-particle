package name.soy.moreparticle.mixin;

import name.soy.moreparticle.client.MoreParticleClient;
import name.soy.moreparticle.vertex.VertexParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;


@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(ClientLevel clientLevel, TextureManager textureManager, CallbackInfo ci) {
		MoreParticleClient.pm = (ParticleEngine) (Object) this;
	}
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
	private Iterator<ParticleRenderType> onTick(List<ParticleRenderType> instance) {
		var newlist = new ArrayList<ParticleRenderType>(instance);
		newlist.add(VertexParticle.VERTEX_RENDER);
		return newlist.iterator();
	}
	@Redirect(method = "loadParticleDescription", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"))
	private boolean alwaysLoadParticle(Map<ResourceLocation, SpriteSet> instance, Object o) {
		ResourceLocation key = (ResourceLocation) o;
		if (!instance.containsKey(key))
			instance.put(key, new ParticleEngine.MutableSpriteSet());
		return true;
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
