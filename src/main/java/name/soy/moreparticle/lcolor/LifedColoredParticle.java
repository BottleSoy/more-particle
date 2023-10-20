package name.soy.moreparticle.lcolor;

import lombok.RequiredArgsConstructor;
import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.client.MoreParticleClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

import java.util.Random;

public class LifedColoredParticle extends AnimatedParticle {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(LifedColorEffect.type, Factory::new);
		ParticleFactoryRegistry.getInstance().register(LifedColorTextureEffect.type, TFactory::new);


	}

	protected LifedColoredParticle(
		ClientWorld world,
		double x, double y, double z,
		double velocityX, double velocityY, double velocityZ,
		SpriteProvider spriteProvider, int color,
		int time, int random, float scale
	) {
		super(world, x, y, z, spriteProvider, -5.0E-4F);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.scale *= scale;
		this.setMaxAge(time + new Random().nextInt(random));
		this.setColor(color);
		this.setSpriteForAge(spriteProvider);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_LIT;
	}

	@Environment(EnvType.CLIENT)
	@RequiredArgsConstructor
	public static class TFactory implements ParticleFactory<LifedColorTextureEffect> {
		private final SpriteProvider spriteProvider;

		public Particle createParticle(LifedColorTextureEffect effect, ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz) {

			SpriteProvider provider = ((ParticleManagerAccessor) MoreParticleClient.pm).getSpriteAwareFactories().get(new Identifier(effect.texture));
			System.out.println(provider);
			return new LifedColoredParticle(clientWorld, x, y, z, dx, dy, dz, provider != null ? provider : spriteProvider, effect.color, effect.life, effect.rand, effect.scale);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<LifedColorEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(LifedColorEffect effect, ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz) {
			return new LifedColoredParticle(clientWorld, x, y, z, dx, dy, dz, this.spriteProvider, effect.color, effect.life, effect.rand, 0.75f);
		}
	}
}
