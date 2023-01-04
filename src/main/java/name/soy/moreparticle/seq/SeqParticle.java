package name.soy.moreparticle.seq;

import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

import java.util.Random;

public class SeqParticle extends AnimatedParticle {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SeqEffect.type, SeqParticle.Factory::new);
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
				this.setColor(effect.clist.get(age));
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

	@RequiredArgsConstructor
	public static class Factory implements ParticleFactory<SeqEffect> {
		private final SpriteProvider spriteProvider;

		@Override
		public Particle createParticle(SeqEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new SeqParticle(world, x, y, z, spriteProvider, parameters);
		}
	}
}
