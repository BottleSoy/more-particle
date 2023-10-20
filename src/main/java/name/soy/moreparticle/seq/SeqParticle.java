package name.soy.moreparticle.seq;

import lombok.RequiredArgsConstructor;
import lombok.val;
import name.soy.moreparticle.client.MoreParticleClient;
import name.soy.moreparticle.lcolor.LifedColorTextureEffect;
import name.soy.moreparticle.lcolor.LifedColoredParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import java.util.Random;

public class SeqParticle extends AnimatedParticle {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SeqEffect.type, Factory::new);
		ParticleFactoryRegistry.getInstance().register(SeqTEffect.type, TFactory::new);
	}

	SeqEffect effect;

	public double lx = 0, ly = 0, lz = 0;

	protected SeqParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, SeqEffect parameters) {
		super(world, x, y, z, spriteProvider, 0.005f);
		this.effect = parameters;
		this.collidesWithWorld = false;
		if (age < effect.clist.size()) {
			this.setColor(effect.clist.get(age));
		}
		if (age < effect.alist.size()) {
			this.scale = effect.alist.get(age);
		}
		this.maxAge = new Random().nextInt(effect.random) + effect.age;
		this.setSpriteForAge(spriteProvider);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_LIT;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
			double cx, cy, cz;
			if (age >= effect.xlist.size()) {
				cx = lx;
			} else {
				cx = effect.xlist.get(age);
			}
			if (age >= effect.ylist.size()) {
				cy = ly;
			} else {
				cy = effect.ylist.get(age);
			}
			if (age >= effect.zlist.size()) {
				cz = lz;
			} else {
				cz = effect.zlist.get(age);
			}
			if (age < effect.clist.size()) {
				val color = effect.clist.get(age);
				this.setColor(color);
				this.setAlpha(1 - (color >> 24) / 255F);
			}
			if (age < effect.alist.size()) {
				this.scale = effect.alist.get(age);
			}
			velocityX = cx - lx;
			velocityY = cy - ly;
			velocityZ = cz - lz;
			this.move(this.velocityX, this.velocityY, this.velocityZ);

			lx = cx;
			ly = cy;
			lz = cz;
		}

	}

	@Environment(EnvType.CLIENT)
	@RequiredArgsConstructor
	public static class TFactory implements ParticleFactory<SeqTEffect> {
		private final SpriteProvider spriteProvider;

		public Particle createParticle(SeqTEffect parameters, ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz) {
			SpriteProvider provider = ((ParticleManagerAccessor) MoreParticleClient.pm).getSpriteAwareFactories().get(new Identifier(parameters.texture));
			return new SeqParticle(clientWorld, x, y, z, provider != null ? provider : spriteProvider, parameters);
		}
	}

	@RequiredArgsConstructor
	public static class Factory implements ParticleFactory<SeqEffect> {
		private final SpriteProvider spriteProvider;

		@Override
		public Particle createParticle(SeqEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new SeqParticle(world, x, y, z, spriteProvider, parameters);
		}
	}
}
