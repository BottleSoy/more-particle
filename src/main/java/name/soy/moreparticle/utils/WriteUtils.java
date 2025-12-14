package name.soy.moreparticle.utils;

import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public interface WriteUtils {

	public static void writeDoubleMatrix(FriendlyByteBuf buf, ArrayList<List<Double>> doubles) {
		buf.writeVarInt(doubles.size());
		for (List<Double> ds : doubles) {
			ds.forEach(buf::writeDouble);
		}
	}
	public static ArrayList<List<Double>> readDoubleMatrix(FriendlyByteBuf buf,int size) {
		var c = buf.readVarInt();
		var list = new ArrayList<List<Double>>(c);
		for (int i = 0; i < size; i++) {
			var cl = new ArrayList<Double>();
			for (int j = 0; j < size; j++) {
				cl.add(buf.readDouble());
			}
			list.add(cl);
		}
		return list;
	}
	public static void writeIntMatrix(FriendlyByteBuf buf, ArrayList<List<Integer>> ints) {
		buf.writeVarInt(ints.size());
		for (List<Integer> ds : ints) {
			ds.forEach(buf::writeInt);
		}
	}
	public static ArrayList<List<Integer>> readIntMatrix(FriendlyByteBuf buf, int size) {
		var c = buf.readVarInt();
		var list = new ArrayList<List<Integer>>(c);
		for (int i = 0; i < size; i++) {
			var cl = new ArrayList<Integer>();
			for (int j = 0; j < size; j++) {
				cl.add(buf.readInt());
			}
			list.add(cl);
		}
		return list;
	}
	public static void writeFloatArray(FriendlyByteBuf buf, ArrayList<Float> floats) {
		buf.writeVarInt(floats.size());
		for (float f : floats) {
			buf.writeFloat(f);
		}
	}

	public static ArrayList<Float> readFloatArray(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		ArrayList<Float> d = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			d.add(buf.readFloat());
		}
		return d;
	}

	public static void writeIntArray(FriendlyByteBuf buf, ArrayList<Integer> ints) {
		buf.writeVarInt(ints.size());
		for (int d : ints) {
			buf.writeInt(d);
		}
	}

	public static ArrayList<Integer> readIntArray(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		ArrayList<Integer> d = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			d.add(buf.readInt());
		}
		return d;
	}

	public static void writeDoubleArray(FriendlyByteBuf buf, ArrayList<Double> doubles) {
		buf.writeVarInt(doubles.size());
		for (double d : doubles) {
			buf.writeDouble(d);
		}
	}

	public static ArrayList<Double> readDoubleArray(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		ArrayList<Double> d = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			d.add(buf.readDouble());
		}
		return d;
	}
}
