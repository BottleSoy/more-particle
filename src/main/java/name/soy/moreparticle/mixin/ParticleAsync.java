//package name.soy.moreparticle.mixin;
//
//import com.google.common.collect.Lists;
//import lombok.val;
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
//import java.util.concurrent.Future;
//
///**
// * 测试多线程性能使用
// * Testing for muti Thread
// */
//@Mixin(ParticleManager.class)
//public abstract class ParticleAsync {
//	private static final ExecutorService exec = Executors.newFixedThreadPool(32);
//	private static final List<Future<List<Particle>>> submits = new ArrayList<>();
//
//	@Shadow
//	protected abstract void tickParticle(Particle particle);
//
//	@Shadow
//	@Final
//	private Map<ParticleTextureSheet, Queue<Particle>> particles;
//
//	@Shadow @Final private Queue<Particle> newParticles;
//
//	@Shadow @Final private Queue<EmitterParticle> newEmitterParticles;
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
//	/**
//	 * @author soy
//	 * @reason test
//	 */
//	@Overwrite
//	public void tick() {
//		this.particles.forEach((particleTextureSheet, queue) -> {
//			this.world.getProfiler().push(particleTextureSheet.toString());
//			this.tickParticles(queue);
//			this.world.getProfiler().pop();
//		});
//		if (!this.newEmitterParticles.isEmpty()) {
//			List<EmitterParticle> list = Lists.newArrayList();
//			for (EmitterParticle emitterParticle : this.newEmitterParticles) {
//				emitterParticle.tick();
//				if (!emitterParticle.isAlive()) {
//					list.add(emitterParticle);
//				}
//			}
//
//			this.newEmitterParticles.removeAll(list);
//		}
//
//		Particle particle;
//		if (!this.newParticles.isEmpty()) {
//			while((particle = this.newParticles.poll()) != null) {
//				this.particles.computeIfAbsent(particle.getType(), particleTextureSheet -> new LinkedList<>()).add(particle);
//			}
//		}
//	}
//}
