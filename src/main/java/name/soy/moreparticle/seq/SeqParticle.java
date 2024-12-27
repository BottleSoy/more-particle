package name.soy.moreparticle.seq;

import lombok.RequiredArgsConstructor;
import lombok.val;
import name.soy.moreparticle.client.MoreParticleClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SeqParticle extends TextureSheetParticle {


	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SeqEffect.type, Provider::new);
		ParticleFactoryRegistry.getInstance().register(SeqTEffect.type, TProvider::new);
	}

	protected final SpriteSet sprites;
	SeqEffect effect;

	public double lx = 0, ly = 0, lz = 0;
	public int lightColor;

	protected SeqParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteProvider, SeqEffect parameters) {
		super(world, x, y, z);
		this.effect = parameters;
		this.hasPhysics = false;
		if (age < effect.clist.size()) {
			this.setColor(effect.clist.get(age));
		}
		if (age < effect.alist.size()) {
			this.quadSize = effect.alist.get(age);
		}
		double cx = 0, cy = 0, cz = 0;
		if (age < effect.xlist.size()) {
			cx = effect.xlist.get(age);
		}
		if (age < effect.ylist.size()) {
			cy = effect.ylist.get(age);
		}
		if (age < effect.xlist.size()) {
			cz = effect.zlist.get(age);
		}
		this.move(cx, cy, cz);
		this.lifetime = new Random().nextInt(effect.random) + effect.age;
		this.sprites = spriteProvider;
		if (age < effect.light.size()) {
			this.lightColor = effect.light.get(age);
		}
		this.setSpriteFromAge(this.sprites);
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.sprites);
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
				this.quadSize = effect.alist.get(age);
			}
			if (age < effect.light.size()) {
				this.lightColor = effect.light.get(age);
			}
			xd = cx - lx;
			yd = cy - ly;
			zd = cz - lz;
			this.move(this.xd, this.yd, this.zd);

			lx = cx;
			ly = cy;
			lz = cz;
		}

	}

	public void setColor(int i) {
		float f = (float) ((i & 0xFF0000) >> 16) / 255.0F;
		float g = (float) ((i & 0xFF00) >> 8) / 255.0F;
		float h = (float) ((i & 0xFF) >> 0) / 255.0F;
		this.setColor(f, g, h);
	}

	@Override
	protected int getLightColor(float f) {
		return lightColor;
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	@RequiredArgsConstructor
	public static class TProvider implements ParticleProvider<SeqTEffect> {
		private final SpriteSet spriteProvider;

		@Override
		public Particle createParticle(SeqTEffect parameters, ClientLevel clientWorld, double x, double y, double z, double dx, double dy, double dz) {
			SpriteSet provider = ((ParticleManagerAccessor) MoreParticleClient.pm).getSpriteAwareFactories().get(ResourceLocation.parse(parameters.texture));
			return new SeqParticle(clientWorld, x, y, z, provider != null ? provider : spriteProvider, parameters);
		}
	}

	@RequiredArgsConstructor
	public static class Provider implements ParticleProvider<SeqEffect> {
		private final SpriteSet spriteProvider;

		@Override
		public Particle createParticle(SeqEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new SeqParticle(world, x, y, z, spriteProvider, parameters);
		}
	}
}
