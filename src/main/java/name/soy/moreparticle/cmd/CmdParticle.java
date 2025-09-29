package name.soy.moreparticle.cmd;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;


import java.util.ArrayList;
import java.util.LinkedList;

public class CmdParticle extends TextureSheetParticle {
	protected CmdParticle(ClientLevel clientLevel, double x, double y, double z) {
		super(clientLevel, x, y, z);
		hasPhysics = false;
	}

	int lightColor = 250;
	LinkedList<ArrayList<ParticleAction>> actions = new LinkedList<>();

	public void addAction(int time, ParticleAction action) {
		if (time >= actions.size()) {
			for (int i = 0; i < time - actions.size() + 1; i++) {
				actions.add(new ArrayList<>());
			}
		}
		actions.get(time).add(action);
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (!actions.isEmpty()) {
			actions.removeFirst().forEach(action -> {
				if (!(Double.isNaN(action.x) && Double.isNaN(action.y) && Double.isNaN(action.z))) {
					double vx, vy, vz;
					if (action.relative) {
						vx = Double.isNaN(action.x) ? 0 : action.x;
						vy = Double.isNaN(action.y) ? 0 : action.y;
						vz = Double.isNaN(action.z) ? 0 : action.z;
					} else {
						vx = Double.isNaN(action.x) ? -this.x : action.x - this.x;
						vy = Double.isNaN(action.y) ? -this.y : action.y - this.y;
						vz = Double.isNaN(action.z) ? -this.z : action.z - this.z;
					}
					this.move(vx, vy, vz);
				}
				if (action.alpha > 0f) {
					this.scale(action.alpha);
				}
				if (action.light > 0) {
					lightColor = action.light;
				}
				if (!(Double.isNaN(action.angleX) && Double.isNaN(action.angleY) && Double.isNaN(action.angleZ))) {
					if (action.cameraRelative) {

					} else {

					}
				}

			});
		}
	}
	boolean cameraRelative;
	@Override
	protected int getLightColor(float f) {
		return lightColor;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
}
