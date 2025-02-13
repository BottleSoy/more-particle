package name.soy.moreparticle.seq;


import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.RequiredArgsConstructor;
import lombok.val;
import name.soy.moreparticle.client.MoreParticleClient;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class SeqVParticle extends SeqParticle {
	public float preAX, preAY, preAZ;//之前的朝向
	public float ax, ay, az; //朝向
	List<Float> angleX, angleY, angleZ;
	public float lAx = 0, lAy = 0, lAz = 0;//最后的有效朝向
	public boolean relative;//朝向是否相对于玩家

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SeqVEffect.type, VProvider::new);
	}

	protected SeqVParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteProvider, SeqVEffect parameters) {
		super(world, x, y, z, spriteProvider, parameters);
		this.relative = parameters.relative;
		this.angleX = parameters.angleX;
		this.angleY = parameters.angleY;
		this.angleZ = parameters.angleZ;
		if (age < this.angleX.size()) {
			this.preAX = this.lAx = this.ax = angleX.get(age);
		}
		if (age < this.angleY.size()) {
			this.preAY = this.lAy = this.ay = angleY.get(age);
		}
		if (age < this.angleZ.size()) {
			this.preAZ = this.lAz = this.az = angleZ.get(age);
		}

	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		this.preAX = this.ax;
		this.preAY = this.ay;
		this.preAZ = this.az;

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
			float cAx, cAy, cAz;
			if (age >= angleX.size()) {
				cAx = lAx;
			} else {
				cAx = angleX.get(age);
			}
			if (age >= angleY.size()) {
				cAy = lAy;
			} else {
				cAy = angleY.get(age);
			}
			if (age >= angleZ.size()) {
				cAz = lAz;
			} else {
				cAz = angleZ.get(age);
			}
			xd = cx - lx;
			yd = cy - ly;
			zd = cz - lz;
			this.move(this.xd, this.yd, this.zd);

			this.lAx = this.ax = cAx;
			this.lAy = this.ay = cAy;
			this.lAz = this.az = cAz;

			lx = cx;
			ly = cy;
			lz = cz;
		}

	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Quaternionf quaternionf = new Quaternionf();
		if (relative)
			this.getFacingCameraMode().setRotation(quaternionf, camera, tickDelta);
		quaternionf.rotateX(Mth.lerp(tickDelta, preAX, ax));
		quaternionf.rotateY(Mth.lerp(tickDelta, preAY, ay));
		quaternionf.rotateZ(Mth.lerp(tickDelta, preAZ, az));

		this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta);
	}

	protected void renderRotatedQuad(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float f) {
		Vec3 vec3 = camera.getPosition();
		float g = (float) (Mth.lerp(f, this.xo, this.x) - vec3.x());
		float h = (float) (Mth.lerp(f, this.yo, this.y) - vec3.y());
		float i = (float) (Mth.lerp(f, this.zo, this.z) - vec3.z());
		this.renderRotatedQuad(vertexConsumer, quaternionf, g, h, i, f);
	}

	protected void renderRotatedQuad(VertexConsumer vertexConsumer, Quaternionf quaternionf, float f, float g, float h, float i) {
		float j = this.getQuadSize(i);
		float k = this.getU0();
		float l = this.getU1();
		float m = this.getV0();
		float n = this.getV1();
		int o = this.getLightColor(i);
		this.renderVertex(vertexConsumer, quaternionf, f, g, h, 1.0F, -1.0F, j, l, n, o);
		this.renderVertex(vertexConsumer, quaternionf, f, g, h, 1.0F, 1.0F, j, l, m, o);
		this.renderVertex(vertexConsumer, quaternionf, f, g, h, -1.0F, 1.0F, j, k, m, o);
		this.renderVertex(vertexConsumer, quaternionf, f, g, h, -1.0F, -1.0F, j, k, n, o);
	}

	private void renderVertex(
		VertexConsumer vertexConsumer, Quaternionf quaternionf, float f, float g, float h, float i, float j, float k, float l, float m, int n
	) {
		Vector3f vector3f = new Vector3f(i, j, 0.0F).rotate(quaternionf).mul(k).add(f, g, h);
		vertexConsumer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(l, m).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
	}

	@RequiredArgsConstructor
	public static class VProvider implements ParticleProvider<SeqVEffect> {
		private final SpriteSet spriteProvider;

		@Override
		public Particle createParticle(SeqVEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			SpriteSet provider = null;
			if (!parameters.texture.isEmpty())
				provider = ((ParticleManagerAccessor) MoreParticleClient.pm).getSpriteAwareFactories().get(ResourceLocation.parse(parameters.texture));
			return new SeqVParticle(world, x, y, z, provider != null ? provider : spriteProvider, parameters);
		}
	}
}
