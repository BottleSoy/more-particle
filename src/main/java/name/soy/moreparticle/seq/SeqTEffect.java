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


@ToString
public class SeqTEffect extends SeqEffect implements ParticleEffect, Serializable {
	public static ParticleType<SeqTEffect> type;

	public static void register() {
		type = MoreParticle.register(new Identifier("soy", "seqt"),
			SeqTEffect.PARAMETERS_FACTORY,
			(particleType) -> SeqTEffect.CODEC);
	}

	public static final Codec<SeqTEffect> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.list(Codec.DOUBLE).fieldOf("xlist").forGetter(e -> e.xlist),
			Codec.list(Codec.DOUBLE).fieldOf("ylist").forGetter(e -> e.ylist),
			Codec.list(Codec.DOUBLE).fieldOf("zlist").forGetter(e -> e.zlist),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("random").forGetter(e -> e.random),
			Codec.list(Codec.INT).fieldOf("clist").forGetter(e -> e.clist),
			Codec.list(Codec.FLOAT).fieldOf("alist").forGetter(e -> e.alist),
			Codec.STRING.fieldOf("texture").forGetter(e -> e.texture)).apply(instance, SeqTEffect::new));

	public SeqTEffect(List<Double> xlist, List<Double> ylist, List<Double> zlist, int age, int random, List<Integer> clist, List<Float> alist, String texture) {
		super(xlist, ylist, zlist, age, random, clist, alist);
		this.texture = texture;
	}


	public String texture;
	public static final Factory<SeqTEffect> PARAMETERS_FACTORY = new Factory<>() {
		public SeqTEffect read(ParticleType<SeqTEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			stringReader.expect('\'');
			String base64 = stringReader.readStringUntil('\'');
			byte[] data = Base64.getDecoder().decode(base64);
			ByteBuf buf = Unpooled.wrappedBuffer(data);
			return read(type, new PacketByteBuf(buf));
		}

		public SeqTEffect read(ParticleType<SeqTEffect> particleType, PacketByteBuf buf) {
			return new SeqTEffect(
				readDoubleArray(buf),
				readDoubleArray(buf),
				readDoubleArray(buf),
				buf.readVarInt(), buf.readVarInt(),
				readIntArray(buf),
				readFloatArray(buf),
				buf.readString()
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
	}

	@Override
	public String asString() {
		return toString();
	}
}
