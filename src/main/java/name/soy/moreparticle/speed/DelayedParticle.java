package name.soy.moreparticle.speed;

import lombok.RequiredArgsConstructor;
import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.lcolor.LifedColorEffect;
import name.soy.moreparticle.lcolor.LifedColoredParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class DelayedParticle extends LifedColoredParticle {

	int delayTime;
	double delayedSpeed;
	public static void register() {

		ParticleFactoryRegistry.getInstance().register(DelayedEffect.type, Factory::new);
	}
	protected DelayedParticle(ClientWorld world, double x, double y, double z,
	                          double velocityX, double velocityY, double velocityZ,
	                          SpriteProvider spriteProvider, int color, int time, int random,
	                          int delayTime, double delayedSpeed) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider, color, time, random);
		this.delayTime = delayTime;
		this.delayedSpeed = delayedSpeed;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
			if (this.age > this.maxAge / 2) {
				this.setAlpha(1.0F - ((float) this.age - (float) (this.maxAge / 2)) / (float) this.maxAge);
			}
			if (age > delayTime) {
				this.velocityX *= 0.91F;
				this.velocityY *= 0.91F;
				this.velocityZ *= 0.91F;
				this.move(this.velocityX, this.velocityY, this.velocityZ);
			} else {
				this.move(delayedSpeed * velocityX, delayedSpeed * velocityY, delayedSpeed * velocityZ);
			}

		}
	}
	@RequiredArgsConstructor
	public static class Factory implements ParticleFactory<DelayedEffect> {
		private final SpriteProvider spriteProvider;

		@Nullable
		@Override
		public Particle createParticle(DelayedEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new DelayedParticle(world,x,y,z,velocityX,velocityY,velocityZ,spriteProvider,parameters.color,
					parameters.life, parameters.rand, parameters.delayTime, parameters.delayedSpeed);
		}
	}
}