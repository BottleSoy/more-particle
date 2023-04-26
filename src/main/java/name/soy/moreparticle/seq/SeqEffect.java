package name.soy.moreparticle.seq;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.ToString;
import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.utils.ZipUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@AllArgsConstructor
@ToString
public class SeqEffect implements ParticleEffect, Serializable {
	public static ParticleType<SeqEffect> type;

	public static void register() {
		type = MoreParticle.register(
			new Identifier("soy", "seq"),
			SeqEffect.PARAMETERS_FACTORY,
			(particleType) -> SeqEffect.CODEC
		);
	}

	public static final Codec<SeqEffect> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.list(Codec.DOUBLE).fieldOf("xlist").forGetter(e -> e.xlist),
			Codec.list(Codec.DOUBLE).fieldOf("ylist").forGetter(e -> e.ylist),
			Codec.list(Codec.DOUBLE).fieldOf("zlist").forGetter(e -> e.zlist),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("random").forGetter(e -> e.random),
			Codec.list(Codec.INT).fieldOf("clist").forGetter(e -> e.clist),
			Codec.list(Codec.FLOAT).fieldOf("alist").forGetter(e -> e.alist)
		).apply(instance, SeqEffect::new));

	List<Double> xlist, ylist, zlist;
	int age, random;
	List<Integer> clist;

	List<Float> alist;
	public static final Factory<SeqEffect> PARAMETERS_FACTORY = new Factory<SeqEffect>() {
		public SeqEffect read(ParticleType<SeqEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			char firstChar = stringReader.peek();
			if (firstChar == 'C') {
				stringReader.skip();
				stringReader.expect(' ');
				stringReader.expect('\'');
				String base64 = stringReader.readStringUntil('\'');
				byte[] data = Base64.getDecoder().decode(base64);
				ByteBuf buf = Unpooled.wrappedBuffer(ZipUtils.decompress(data));
				return read(type, new PacketByteBuf(buf));
			} else if (firstChar == 'R') {
				stringReader.skip();
				stringReader.expect(' ');
				stringReader.expect('\'');
				String base64 = stringReader.readStringUntil('\'');
				byte[] data = Base64.getDecoder().decode(base64);
				ByteBuf buf = Unpooled.wrappedBuffer(data);
				return read(type, new PacketByteBuf(buf));
			} else {
				stringReader.expect('[');

				List<Double> xlist = new ArrayList<>();
				String xs = stringReader.readStringUntil(']');
				for (String s : xs.split(",")) {
					xlist.add(Double.parseDouble(s.trim()));
				}
				stringReader.expect(' ');
				stringReader.expect('[');
				List<Double> ylist = new ArrayList<>();
				String ys = stringReader.readStringUntil(']');
				for (String s : ys.split(",")) {
					ylist.add(Double.parseDouble(s.trim()));
				}

				stringReader.expect(' ');
				stringReader.expect('[');
				String zs = stringReader.readStringUntil(']');

				List<Double> zlist = new ArrayList<>();
				for (String s : zs.split(",")) {
					zlist.add(Double.parseDouble(s.trim()));
				}

				stringReader.expect(' ');
				int age = stringReader.readInt();
				stringReader.expect(' ');
				int random = stringReader.readInt();
				stringReader.expect(' ');
				stringReader.expect('[');
				List<Integer> clist = new ArrayList<>();
				String cs = stringReader.readStringUntil(']');
				for (String s : cs.split(",")) {
					clist.add(Integer.parseInt(s.trim()));
				}
				stringReader.expect(' ');
				stringReader.expect('[');
				List<Float> alist = new ArrayList<>();
				String as = stringReader.readStringUntil(']');
				for (String s : as.split(",")) {
					alist.add(Float.parseFloat(s.trim()));
				}
				return new SeqEffect(xlist, ylist, zlist, age, random, clist, alist);
			}
		}

		public SeqEffect read(ParticleType<SeqEffect> particleType, PacketByteBuf buf) {
			return new SeqEffect(
				readDoubleArray(buf),
				readDoubleArray(buf),
				readDoubleArray(buf),
				buf.readVarInt(),
				buf.readVarInt(),
				readIntArray(buf),
				readFloatArray(buf));
		}
	};


	@Override
	public ParticleType<?> getType() {
		return type;
	}

	@Override
	public void write(PacketByteBuf buf) {
		writeDoubleArray(buf, xlist);
		writeDoubleArray(buf, ylist);
		writeDoubleArray(buf, zlist);
		buf.writeVarInt(age);
		buf.writeVarInt(random);
		writeIntArray(buf, clist);
		writeFloatArray(buf, alist);
	}

	@Override
	public String asString() {
		return toString();
	}

	private static void writeFloatArray(PacketByteBuf buf, List<Float> floats) {
		buf.writeVarInt(floats.size());
		for (float f : floats) {
			buf.writeFloat(f);
		}
	}

	private static List<Float> readFloatArray(PacketByteBuf buf) {
		int size = buf.readVarInt();
		List<Float> d = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			d.add(buf.readFloat());
		}
		return d;
	}

	public static void writeIntArray(PacketByteBuf buf, List<Integer> ints) {
		buf.writeVarInt(ints.size());
		for (int d : ints) {
			buf.writeInt(d);
		}
	}

	public static List<Integer> readIntArray(PacketByteBuf buf) {
		int size = buf.readVarInt();
		List<Integer> d = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			d.add(buf.readInt());
		}
		return d;
	}

	public static void writeDoubleArray(PacketByteBuf buf, List<Double> doubles) {
		buf.writeVarInt(doubles.size());
		for (double d : doubles) {
			buf.writeDouble(d);
		}
	}

	public static List<Double> readDoubleArray(PacketByteBuf buf) {
		int size = buf.readVarInt();
		List<Double> d = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			d.add(buf.readDouble());
		}
		return d;
	}
}
