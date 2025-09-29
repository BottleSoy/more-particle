package name.soy.moreparticle.vertex;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import lombok.RequiredArgsConstructor;
import name.soy.moreparticle.seq.SeqEffect;
import name.soy.moreparticle.seq.SeqParticle;
import name.soy.moreparticle.seq.SeqVEffect;
import name.soy.moreparticle.seq.SeqVParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class VertexParticle extends TextureSheetParticle {
	double pre1x, pre1y, pre1z;
	double pre2x, pre2y, pre2z;
	double pre3x, pre3y, pre3z;

	double p1x, p1y, p1z;
	double p2x, p2y, p2z;
	double p3x, p3y, p3z;

	int l1, l2, l3;
	float c1r, c2r, c3r;
	float c1g, c2g, c3g;
	float c1b, c2b, c3b;
	float c1a, c2a, c3a;
	VertexEffect effect;

	protected VertexParticle(
		ClientLevel clientLevel, SpriteSet spriteProvider, VertexEffect effect
	) {
		super(clientLevel, 0, 0, 0, 0, 0, 0);
		this.effect = effect;
		this.setSpriteFromAge(spriteProvider);
		this.lifetime = effect.age;
		update();
		this.pre1x = this.p1x;
		this.pre1y = this.p1y;
		this.pre1z = this.p1z;

		this.pre2x = this.p2x;
		this.pre2y = this.p2y;
		this.pre2z = this.p2z;

		this.pre3x = this.p3x;
		this.pre3y = this.p3y;
		this.pre3z = this.p3z;
	}

	@Override
	public void tick() {
		this.pre1x = this.p1x;
		this.pre1y = this.p1y;
		this.pre1z = this.p1z;

		this.pre2x = this.p2x;
		this.pre2y = this.p2y;
		this.pre2z = this.p2z;

		this.pre3x = this.p3x;
		this.pre3y = this.p3y;
		this.pre3z = this.p3z;
		if (this.age++ >= this.lifetime) {
			this.remove();
			return;
		} else {
			update();
		}
	}

	private void update() {
		if (age < effect.p1x.size()) {
			this.p1x = effect.p1x.get(age);
		}
		if (age < effect.p1y.size()) {
			this.p1y = effect.p1y.get(age);
		}
		if (age < effect.p1z.size()) {
			this.p1z = effect.p1z.get(age);
		}
		if (age < effect.p2x.size()) {
			this.p2x = effect.p2x.get(age);
		}
		if (age < effect.p2y.size()) {
			this.p2y = effect.p2y.get(age);
		}
		if (age < effect.p2z.size()) {
			this.p2z = effect.p2z.get(age);
		}
		if (age < effect.p3x.size()) {
			this.p3x = effect.p3x.get(age);
		}
		if (age < effect.p3y.size()) {
			this.p3y = effect.p3y.get(age);
		}
		if (age < effect.p3z.size()) {
			this.p3z = effect.p3z.get(age);
		}

		if (age < effect.l1.size()) {
			this.l1 = effect.l1.get(age);
		}
		if (age < effect.l2.size()) {
			this.l2 = effect.l2.get(age);
		}
		if (age < effect.l3.size()) {
			this.l3 = effect.l3.get(age);
		}

		if (age < effect.c1.size()) {
			int i = effect.c1.get(age);
			this.c1r = (float) ((i & 0x00FF0000) >> 16) / 255.0F;
			this.c1g = (float) ((i & 0x0000FF00) >> 8) / 255.0F;
			this.c1b = (float) ((i & 0x000000FF) >> 0) / 255.0F;
			this.c1a = 1 - (float) ((i & 0xFF000000) >> 24) / 255.0F;
		}
		if (age < effect.c2.size()) {
			int i = effect.c2.get(age);
			this.c2r = (float) ((i & 0x00FF0000) >> 16) / 255.0F;
			this.c2g = (float) ((i & 0x0000FF00) >> 8) / 255.0F;
			this.c2b = (float) ((i & 0x000000FF) >> 0) / 255.0F;
			this.c2a = 1 - (float) ((i & 0xFF000000) >> 24) / 255.0F;
		}
		if (age < effect.c3.size()) {
			int i = effect.c3.get(age);
			this.c3r = (float) ((i & 0x00FF0000) >> 16) / 255.0F;
			this.c3g = (float) ((i & 0x0000FF00) >> 8) / 255.0F;
			this.c3b = (float) ((i & 0x000000FF) >> 0) / 255.0F;
			this.c3a = 1 - (float) ((i & 0xFF000000) >> 24) / 255.0F;
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float f) {
		float j = this.getQuadSize(this.quadSize);

		float k = this.getU0();
		float l = this.getU1();
		float m = this.getV0();
		float n = this.getV1();

		Vec3 vec3 = camera.getPosition();
		float c1x = (float) (Mth.lerp(f, this.pre1x, this.p1x) - vec3.x());
		float c1y = (float) (Mth.lerp(f, this.pre1y, this.p1y) - vec3.y());
		float c1z = (float) (Mth.lerp(f, this.pre1z, this.p1z) - vec3.z());

		vertexConsumer.addVertex(c1x, c1y, c1z)
			.setUv(l, n).setColor(this.c1r, this.c1g, this.c1b, this.c1a).setLight(l1);
		float c2x = (float) (Mth.lerp(f, this.pre2x, this.p2x) - vec3.x());
		float c2y = (float) (Mth.lerp(f, this.pre2y, this.p2y) - vec3.y());
		float c2z = (float) (Mth.lerp(f, this.pre2z, this.p2z) - vec3.z());
		vertexConsumer.addVertex(c2x, c2y, c2z)
			.setUv(l, m).setColor(this.c2r, this.c2g, this.c2b, this.c2a).setLight(l2);

		float c3x = (float) (Mth.lerp(f, this.pre3x, this.p3x) - vec3.x());
		float c3y = (float) (Mth.lerp(f, this.pre3y, this.p3y) - vec3.y());
		float c3z = (float) (Mth.lerp(f, this.pre3z, this.p3z) - vec3.z());
		vertexConsumer.addVertex(c3x, c3y, c3z)
			.setUv(k, m).setColor(this.c3r, this.c3g, this.c3b, this.c3a).setLight(l3);
	}

	public static final ParticleRenderType VERTEX_RENDER = new ParticleRenderType() {

		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			RenderSystem.enableBlend();
			RenderSystem.enableCull();
			RenderSystem.defaultBlendFunc();
			return tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.PARTICLE);
		}
	};

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return VERTEX_RENDER;
	}

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(VertexEffect.type, VertexParticle.Provider::new);
	}

	@RequiredArgsConstructor
	public static class Provider implements ParticleProvider<VertexEffect> {
		private final SpriteSet spriteProvider;

		@Override
		public Particle createParticle(VertexEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new VertexParticle(world, spriteProvider, parameters);
		}
	}
}
