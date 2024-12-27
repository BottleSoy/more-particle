package name.soy.moreparticle.seq;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.ToString;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@ToString
public class SeqTEffect extends SeqEffect implements ParticleOptions, Serializable {
	public static ParticleType<SeqTEffect> type;

	public static void register() {
		type = MoreParticle.register(ResourceLocation.fromNamespaceAndPath("soy", "seqt"),
			(particleType) -> SeqTEffect.STREAM_CODEC,
			(particleType) -> SeqTEffect.CODEC);
	}

	public static final MapCodec<SeqTEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
			Codec.list(Codec.DOUBLE).fieldOf("xlist").forGetter(e -> e.xlist),
			Codec.list(Codec.DOUBLE).fieldOf("ylist").forGetter(e -> e.ylist),
			Codec.list(Codec.DOUBLE).fieldOf("zlist").forGetter(e -> e.zlist),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("random").forGetter(e -> e.random),
			Codec.list(Codec.INT).fieldOf("clist").forGetter(e -> e.clist),
			Codec.list(Codec.FLOAT).fieldOf("alist").forGetter(e -> e.alist),
			Codec.STRING.fieldOf("texture").forGetter(e -> e.texture),
			Codec.list(Codec.INT).fieldOf("light").forGetter(e -> e.light)

			).apply(instance, SeqTEffect::new));

	public SeqTEffect(List<Double> xlist, List<Double> ylist, List<Double> zlist, int age, int random, List<Integer> clist, List<Float> alist, String texture, List<Integer> light) {
		super(xlist, ylist, zlist, age, random, clist, alist, light);
		this.texture = texture;
	}

	public String texture;
	public static final StreamCodec<RegistryFriendlyByteBuf, SeqTEffect> STREAM_CODEC = new StreamCodec<>() {

		@Override
		public void encode(RegistryFriendlyByteBuf buf, SeqTEffect effect) {
			writeDoubleArray(buf, effect.xlist);
			writeDoubleArray(buf, effect.ylist);
			writeDoubleArray(buf, effect.zlist);
			buf.writeVarInt(effect.age);
			buf.writeVarInt(effect.random);
			writeIntArray(buf, effect.clist);
			writeFloatArray(buf, effect.alist);
			buf.writeUtf(effect.texture);
			writeIntArray(buf,effect.light);
		}

		@Override
		public @NotNull SeqTEffect decode(RegistryFriendlyByteBuf buf) {
			return new SeqTEffect(
				readDoubleArray(buf),
				readDoubleArray(buf),
				readDoubleArray(buf),
				buf.readVarInt(), buf.readVarInt(),
				readIntArray(buf),
				readFloatArray(buf),
				buf.readUtf(),
				readIntArray(buf)
			);
		}
	};
//	public static final Factory<SeqTEffect> PARAMETERS_FACTORY = new Factory<>() {
//		public SeqTEffect read(ParticleType<SeqTEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
//			stringReader.expect(' ');
//			stringReader.expect('\'');
//			String base64 = stringReader.readStringUntil('\'');
//			byte[] data = Base64.getDecoder().decode(base64);
//			ByteBuf buf = Unpooled.wrappedBuffer(data);
//			return read(type, new PacketByteBuf(buf));
//		}
//
//		public SeqTEffect read(ParticleType<SeqTEffect> particleType, PacketByteBuf buf) {
//			return new SeqTEffect(
//				readDoubleArray(buf),
//				readDoubleArray(buf),
//				readDoubleArray(buf),
//				buf.readVarInt(), buf.readVarInt(),
//				readIntArray(buf),
//				readFloatArray(buf),
//				buf.readString()
//			);
//		}
//	};


	@Override
	public @NotNull ParticleType<?> getType() {
		return type;
	}

}
