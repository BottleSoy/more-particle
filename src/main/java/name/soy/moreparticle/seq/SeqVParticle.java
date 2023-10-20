package name.soy.moreparticle.seq;

import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SeqVParticle extends SeqParticle {
	public float preAX, preAY, preAZ;
	public float ax, ay, az;

	protected SeqVParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, SeqVEffect parameters) {
		super(world, x, y, z, spriteProvider, parameters);
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {

		Vec3d vec3d = camera.getPos();
		float relativeX = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float relativeY = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float relativeZ = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternionf quaternionf;
		quaternionf = new Quaternionf(camera.getRotation());
		quaternionf.rotateXYZ(
			MathHelper.lerp(tickDelta, this.preAX, this.ax),
			MathHelper.lerp(tickDelta, this.preAY, this.ay),
			MathHelper.lerp(tickDelta, this.preAZ, this.az)
		);

		Vector3f[] vector3fs = new Vector3f[]{
			new Vector3f(-1.0f, -1.0f, 0.0f),
			new Vector3f(-1.0f, 1.0f, 0.0f),
			new Vector3f(1.0f, 1.0f, 0.0f),
			new Vector3f(1.0f, -1.0f, 0.0f)
		};
		float size = this.getSize(tickDelta);
		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
			vector3f.rotate(quaternionf);
			vector3f.mul(size);
			vector3f.add(relativeX, relativeY, relativeZ);
		}
		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();
		int o = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(o).next();


	}
}
