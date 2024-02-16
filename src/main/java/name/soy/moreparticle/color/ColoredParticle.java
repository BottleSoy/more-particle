package name.soy.moreparticle.color;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class ColoredParticle extends AnimatedParticle {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(ColorEffect.type, Factory::new);
	}

	protected ColoredParticle(
			ClientWorld world,
			double x, double y, double z,
			double velocityX, double velocityY, double velocityZ,
			SpriteProvider spriteProvider, int color
	) {
		super(world, x, y, z, spriteProvider, -5.0E-4F);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.scale *= 0.75F;
		this.setColor(color);
		this.setSpriteForAge(spriteProvider);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<ColorEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(ColorEffect effect, ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz) {
			return new ColoredParticle(clientWorld, x, y, z, dx, dy, dz, this.spriteProvider, effect.color);
		}
	}
}
