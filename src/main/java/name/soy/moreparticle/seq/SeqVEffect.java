package name.soy.moreparticle.seq;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

import java.util.Base64;
import java.util.List;

public class SeqVEffect extends SeqTEffect {
	List<Float> angleX;
	List<Float> angleY;
	List<Float> angleZ;

	public SeqVEffect(List<Double> xlist, List<Double> ylist, List<Double> zlist,
	                  int age, int random, List<Integer> clist, List<Float> alist, String texture,
	                  List<Float> angleX, List<Float> angleY, List<Float> angleZ
	) {
		super(xlist, ylist, zlist, age, random, clist, alist, texture);
		this.angleX = angleX;
		this.angleY = angleY;
		this.angleZ = angleZ;
	}

	public static ParticleType<SeqVEffect> type;

	public static void register() {
		type = MoreParticle.register(new Identifier("soy", "seqV"),
			SeqVEffect.PARAMETERS_FACTORY,
			(particleType) -> SeqVEffect.CODEC);
	}

	public static final Codec<SeqVEffect> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.list(Codec.DOUBLE).fieldOf("xlist").forGetter(e -> e.xlist),
			Codec.list(Codec.DOUBLE).fieldOf("ylist").forGetter(e -> e.ylist),
			Codec.list(Codec.DOUBLE).fieldOf("zlist").forGetter(e -> e.zlist),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("random").forGetter(e -> e.random),
			Codec.list(Codec.INT).fieldOf("clist").forGetter(e -> e.clist),
			Codec.list(Codec.FLOAT).fieldOf("alist").forGetter(e -> e.alist),
			Codec.STRING.fieldOf("texture").forGetter(e -> e.texture),
			Codec.list(Codec.FLOAT).fieldOf("angleX").forGetter(e -> e.angleX),
			Codec.list(Codec.FLOAT).fieldOf("angleY").forGetter(e -> e.angleY),
			Codec.list(Codec.FLOAT).fieldOf("angleZ").forGetter(e -> e.angleZ)
		).apply(instance, SeqVEffect::new));

	public static final Factory<SeqVEffect> PARAMETERS_FACTORY = new Factory<>() {
		public SeqVEffect read(ParticleType<SeqVEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			stringReader.expect('\'');
			String base64 = stringReader.readStringUntil('\'');
			byte[] data = Base64.getDecoder().decode(base64);
			ByteBuf buf = Unpooled.wrappedBuffer(data);
			return read(type, new PacketByteBuf(buf));
		}

		public SeqVEffect read(ParticleType<SeqVEffect> particleType, PacketByteBuf buf) {
			return new SeqVEffect(
				readDoubleArray(buf),
				readDoubleArray(buf),
				readDoubleArray(buf),
				buf.readVarInt(), buf.readVarInt(),
				readIntArray(buf),
				readFloatArray(buf),
				buf.readString(),
				readFloatArray(buf),
				readFloatArray(buf),
				readFloatArray(buf)
			);
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
		buf.writeString(texture);
		writeFloatArray(buf, angleX);
		writeFloatArray(buf, angleY);
		writeFloatArray(buf, angleZ);
	}

	@Override
	public String asString() {
		return toString();
	}
}
