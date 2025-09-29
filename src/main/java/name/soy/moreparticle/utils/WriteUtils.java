package name.soy.moreparticle.utils;

import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public interface WriteUtils {

	public static void writeFloatArray(FriendlyByteBuf buf, List<Float> floats) {
		buf.writeVarInt(floats.size());
		for (float f : floats) {
			buf.writeFloat(f);
		}
	}

	public static List<Float> readFloatArray(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		List<Float> d = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			d.add(buf.readFloat());
		}
		return d;
	}

	public static void writeIntArray(FriendlyByteBuf buf, List<Integer> ints) {
		buf.writeVarInt(ints.size());
		for (int d : ints) {
			buf.writeInt(d);
		}
	}

	public static List<Integer> readIntArray(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		List<Integer> d = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			d.add(buf.readInt());
		}
		return d;
	}

	public static void writeDoubleArray(FriendlyByteBuf buf, List<Double> doubles) {
		buf.writeVarInt(doubles.size());
		for (double d : doubles) {
			buf.writeDouble(d);
		}
	}

	public static List<Double> readDoubleArray(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		List<Double> d = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			d.add(buf.readDouble());
		}
		return d;
	}
}
