package name.soy.moreparticle.lcolor;

import name.soy.moreparticle.MoreParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

import java.util.Random;

public class LifedColoredParticle extends AnimatedParticle {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(LifedColorEffect.type, Factory::new);
	}

	protected LifedColoredParticle(
			ClientWorld world,
			double x, double y, double z,
			double velocityX, double velocityY, double velocityZ,
			SpriteProvider spriteProvider, int color,
			int time, int random
	) {
		super(world, x, y, z, spriteProvider, -5.0E-4F);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.scale *= 0.75F;
		this.setMaxAge(time + new Random().nextInt(random));
		this.setColor(color);
		this.setSpriteForAge(spriteProvider);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<LifedColorEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(LifedColorEffect effect, ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz) {
			return new LifedColoredParticle(clientWorld, x, y, z, dx, dy, dz, this.spriteProvider, effect.color, effect.life, effect.rand);
		}
	}
}
