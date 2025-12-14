package name.soy.moreparticle.vertex4;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import lombok.RequiredArgsConstructor;
import name.soy.moreparticle.client.MoreParticleClient;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Vertex4Particle extends TextureSheetParticle {
	double pre1x, pre1y, pre1z;
	double pre2x, pre2y, pre2z;
	double pre3x, pre3y, pre3z;
	double pre4x, pre4y, pre4z;

	double p1x, p1y, p1z;
	double p2x, p2y, p2z;
	double p3x, p3y, p3z;
	double p4x, p4y, p4z;

	int l1, l2, l3, l4;
	int c1r, c2r, c3r, c4r;
	int c1g, c2g, c3g, c4g;
	int c1b, c2b, c3b, c4b;
	int c1a, c2a, c3a, c4a;
	Vertex4Effect effect;

	public Vertex4Particle(
		ClientLevel clientLevel, SpriteSet spriteProvider, Vertex4Effect effect
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

		this.pre4x = this.p4x;
		this.pre4y = this.p4y;
		this.pre4z = this.p4z;
	}

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

		this.pre4x = this.p4x;
		this.pre4y = this.p4y;
		this.pre4z = this.p4z;

		if (this.age++ >= this.lifetime) {
			this.remove();
			return;
		} else {
			update();
		}
	}

	public void update() {
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
		if (age < effect.p4x.size()) {
			this.p4x = effect.p4x.get(age);
		}
		if (age < effect.p4y.size()) {
			this.p4y = effect.p4y.get(age);
		}
		if (age < effect.p4z.size()) {
			this.p4z = effect.p4z.get(age);
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
		if (age < effect.l4.size()) {
			this.l4 = effect.l4.get(age);
		}

		if (age < effect.c1.size()) {
			int i = effect.c1.get(age);
			this.c1r = ((i & 0x00FF0000) >> 16);
			this.c1g = ((i & 0x0000FF00) >> 8);
			this.c1b = ((i & 0x000000FF) >> 0);
			this.c1a = 255 - ((i & 0xFF000000) >> 24);
		}
		if (age < effect.c2.size()) {
			int i = effect.c2.get(age);
			this.c2r = ((i & 0x00FF0000) >> 16);
			this.c2g = ((i & 0x0000FF00) >> 8);
			this.c2b = ((i & 0x000000FF) >> 0);
			this.c2a = 255 - ((i & 0xFF000000) >> 24);
		}
		if (age < effect.c3.size()) {
			int i = effect.c3.get(age);
			this.c3r = ((i & 0x00FF0000) >> 16);
			this.c3g = ((i & 0x0000FF00) >> 8);
			this.c3b = ((i & 0x000000FF) >> 0);
			this.c3a = 255 - ((i & 0xFF000000) >> 24);
		}
		if (age < effect.c4.size()) {
			int i = effect.c4.get(age);
			this.c4r = ((i & 0x00FF0000) >> 16);
			this.c4g = ((i & 0x0000FF00) >> 8);
			this.c4b = ((i & 0x000000FF) >> 0);
			this.c4a = 255 - ((i & 0xFF000000) >> 24);
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float f) {
		float j = this.getQuadSize(this.quadSize);

		float u0 = this.getU0();
		float u1 = this.getU1();
		float v0 = this.getV0();
		float v1 = this.getV1();

		Vec3 vec3 = camera.getPosition();
		float c1x = (float) (Mth.lerp(f, this.pre1x, this.p1x) - vec3.x());
		float c1y = (float) (Mth.lerp(f, this.pre1y, this.p1y) - vec3.y());
		float c1z = (float) (Mth.lerp(f, this.pre1z, this.p1z) - vec3.z());

		vertexConsumer.addVertex(c1x, c1y, c1z)
			.setUv(u1, v1).setColor(this.c1r, this.c1g, this.c1b, this.c1a).setLight(l1);

		float c2x = (float) (Mth.lerp(f, this.pre2x, this.p2x) - vec3.x());
		float c2y = (float) (Mth.lerp(f, this.pre2y, this.p2y) - vec3.y());
		float c2z = (float) (Mth.lerp(f, this.pre2z, this.p2z) - vec3.z());

		vertexConsumer.addVertex(c2x, c2y, c2z)
			.setUv(u1, v0).setColor(this.c2r, this.c2g, this.c2b, this.c2a).setLight(l2);

		float c3x = (float) (Mth.lerp(f, this.pre3x, this.p3x) - vec3.x());
		float c3y = (float) (Mth.lerp(f, this.pre3y, this.p3y) - vec3.y());
		float c3z = (float) (Mth.lerp(f, this.pre3z, this.p3z) - vec3.z());

		vertexConsumer.addVertex(c3x, c3y, c3z)
			.setUv(u0, v0).setColor(this.c3r, this.c3g, this.c3b, this.c3a).setLight(l3);

		float c4x = (float) (Mth.lerp(f, this.pre4x, this.p4x) - vec3.x());
		float c4y = (float) (Mth.lerp(f, this.pre4y, this.p4y) - vec3.y());
		float c4z = (float) (Mth.lerp(f, this.pre4z, this.p4z) - vec3.z());

		vertexConsumer.addVertex(c4x, c4y, c4z)
			.setUv(u0, v1).setColor(this.c4r, this.c4g, this.c4b, this.c4a).setLight(l4);

	}

	public static final ParticleRenderType VERTEX_RENDER = new ParticleRenderType() {

		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			RenderSystem.enableBlend();
			RenderSystem.enableCull();
			RenderSystem.defaultBlendFunc();
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}
	};
	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return VERTEX_RENDER;
	}
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(Vertex4Effect.type, Vertex4Particle.Provider::new);
	}

	@RequiredArgsConstructor
	public static class Provider implements ParticleProvider<Vertex4Effect> {
		private final SpriteSet spriteProvider;

		@Override
		public Particle createParticle(Vertex4Effect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			SpriteSet provider = null;
			if (!parameters.texture.isEmpty())
				provider = ((ParticleManagerAccessor) MoreParticleClient.pm).getSpriteAwareFactories().get(ResourceLocation.parse(parameters.texture));
			return new Vertex4Particle(world, provider, parameters);
		}
	}
}
