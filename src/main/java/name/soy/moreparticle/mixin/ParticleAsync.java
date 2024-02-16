package name.soy.moreparticle.mixin;//package name.soy.moreparticle.mixin;
//
//import com.google.common.collect.Lists;
//import lombok.val;
//import name.soy.moreparticle.client.MoreParticleClient;
//import net.minecraft.client.particle.EmitterParticle;
//import net.minecraft.client.particle.Particle;
//import net.minecraft.client.particle.ParticleManager;
//import net.minecraft.client.particle.ParticleTextureSheet;
//import net.minecraft.client.world.ClientWorld;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * 测试多线程性能使用
// * Testing for muti Thread
// */
//@Mixin(ParticleManager.class)
//public abstract class ParticleAsync {
//	private static final ExecutorService exec = Executors.newFixedThreadPool(14);
//
//	@Shadow
//	protected abstract void tickParticle(Particle particle);
//
//	@Shadow protected ClientWorld world;
//
//	/**
//	 * @author soy
//	 * @reason no
//	 */
//	@Overwrite
//	private void tickParticles(Collection<Particle> particles) {
//		if (!particles.isEmpty()) {
//			Iterator<Particle> iterator = particles.iterator();
//			List<Particle> tempParticles = new ArrayList<>();
//			int i = 0;
//			while (iterator.hasNext()) {
//				val part = iterator.next();
//				if (!part.isAlive()) {
//					String tag = MoreParticleClient.tagParticles.remove(part);
//					if (tag != null) MoreParticleClient.particleTags.get(tag).remove(part);
//					iterator.remove();
//					continue;
//				}
//				tempParticles.add(part);
//				i++;
//				if (i > 4000) {
//					i = 0;
//					List<Particle> res = new ArrayList<>(tempParticles);
//					exec.execute(() -> res.forEach(this::tickParticle));
//					tempParticles.clear();
//				}
//			}
//			exec.execute(() -> tempParticles.forEach(this::tickParticle));
//		}
//
//	}
//
//}
