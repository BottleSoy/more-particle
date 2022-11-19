package name.soy.moreparticle.life;

import name.soy.moreparticle.MoreParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

/**
 * 自定义生命周期的末地烛
 */
public class LifeEndRodParticle extends AnimatedParticle {

	public static void register() {

		ParticleFactoryRegistry.getInstance().register(LifeEndRodEffect.type, Factory::new);
	}

	LifeEffect effect;

	private LifeEndRodParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, LifeEffect effect) {
		super(world, x, y, z, spriteProvider, -5.0E-4F);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.scale *= 0.75F;
		this.effect = effect;
		this.maxAge = effect.base + this.random.nextInt(effect.random);
		this.setTargetColor(15916745);
		this.setSpriteForAge(spriteProvider);
	}

	public void move(double dx, double dy, double dz) {
		this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
		this.repositionFromBoundingBox();
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<LifeEndRodEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(LifeEndRodEffect effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new LifeEndRodParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider, effect);
		}
	}
}


