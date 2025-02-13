package name.soy.moreparticle.cmd;

import net.minecraft.network.FriendlyByteBuf;

public class ParticleAction {

	boolean relative = true;//位置是否是相对的
	double x = Double.NaN, y = Double.NaN, z = Double.NaN;
	int color = -1;
	float alpha = -1f;
	int light = -1;
	boolean cameraRelative = true;
	float angleX = Float.NaN, angleY = Float.NaN, angleZ = Float.NaN;


	private int calcuateSign() {
		int sign = 0;
		if (relative) sign += 1 << 0;
		if (!Double.isNaN(x)) sign += 1 << 1;
		if (!Double.isNaN(y)) sign += 1 << 2;
		if (!Double.isNaN(z)) sign += 1 << 3;
		if (color != -1) sign += 1 << 4;
		if (alpha < 0f) sign += 1 << 5;
		if (light < 0) sign += 1 << 6;
		if (cameraRelative) sign += 1 << 7;
		if (!Float.isNaN(angleX)) sign += 1 << 8;
		if (!Float.isNaN(angleY)) sign += 1 << 9;
		if (!Float.isNaN(angleZ)) sign += 1 << 10;

		return sign;
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(calcuateSign());
		if (!Double.isNaN(x)) buf.writeDouble(x);
		if (!Double.isNaN(y)) buf.writeDouble(y);
		if (!Double.isNaN(z)) buf.writeDouble(z);
		if (color != -1) buf.writeInt(color);
		if (alpha < 0f) buf.writeFloat(alpha);
		if (light < 0) buf.writeVarInt(light);
		if (!Float.isNaN(angleX)) buf.writeFloat(angleX);
		if (!Float.isNaN(angleY)) buf.writeFloat(angleY);
		if (!Float.isNaN(angleZ)) buf.writeFloat(angleZ);
	}

	public void read(FriendlyByteBuf buf) {
		int sign = buf.readVarInt();
		relative = (sign >> 0 & 1) == 1;
		if ((sign >> 1 & 1) == 1) x = buf.readDouble();
		if ((sign >> 2 & 1) == 1) y = buf.readDouble();
		if ((sign >> 3 & 1) == 1) z = buf.readDouble();
		if ((sign >> 4 & 1) == 1) color = buf.readInt();
		if ((sign >> 5 & 1) == 1) alpha = buf.readFloat();
		if ((sign >> 6 & 1) == 1) light = buf.readVarInt();
		cameraRelative = (sign >> 7 & 1) == 1;
		if ((sign >> 8 & 1) == 1) angleX = buf.readFloat();
		if ((sign >> 9 & 1) == 1) angleY = buf.readFloat();
		if ((sign >> 10 & 1) == 1) angleZ = buf.readFloat();

	}
}
