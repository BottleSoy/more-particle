package name.soy.moreparticle.seq;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ToString
public class SeqEffect implements ParticleOptions, Serializable {
	public static ParticleType<SeqEffect> type;

	@RequiredArgsConstructor
	public enum RenderType {
		TERRAIN_SHEET(ParticleRenderType.TERRAIN_SHEET),
		PARTICLE_SHEET_OPAQUE(ParticleRenderType.PARTICLE_SHEET_OPAQUE),
		PARTICLE_SHEET_TRANSLUCENT(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT),
		NO_RENDER(ParticleRenderType.NO_RENDER);
		public final ParticleRenderType type;
	}

	public static void register() {
		type = MoreParticle.register(
			ResourceLocation.fromNamespaceAndPath("soy", "seq"),
			(particleType) -> SeqEffect.STREAM_CODEC,
			(particleType) -> SeqEffect.CODEC
		);
	}

	public static final StreamCodec<RegistryFriendlyByteBuf, SeqEffect> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public void encode(RegistryFriendlyByteBuf buf, SeqEffect effect) {
			writeDoubleArray(buf, effect.xlist);
			writeDoubleArray(buf, effect.ylist);
			writeDoubleArray(buf, effect.zlist);
			buf.writeVarInt(effect.age);
			buf.writeVarInt(effect.random);
			writeIntArray(buf, effect.clist);
			writeFloatArray(buf, effect.alist);
			writeIntArray(buf, effect.light);
			buf.writeEnum(effect.renderType);
		}

		@Override
		public @NotNull SeqEffect decode(RegistryFriendlyByteBuf buf) {
			return new SeqEffect(
				readDoubleArray(buf),
				readDoubleArray(buf),
				readDoubleArray(buf),
				buf.readVarInt(), buf.readVarInt(),
				readIntArray(buf),
				readFloatArray(buf),
				readIntArray(buf),
				buf.readEnum(RenderType.class)
			);
		}
	};
	public static final MapCodec<SeqEffect> CODEC = RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<SeqEffect> instance) -> instance.group(
			Codec.list(Codec.DOUBLE).fieldOf("xlist").forGetter(e -> e.xlist),
			Codec.list(Codec.DOUBLE).fieldOf("ylist").forGetter(e -> e.ylist),
			Codec.list(Codec.DOUBLE).fieldOf("zlist").forGetter(e -> e.zlist),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("random").orElse(1).forGetter(e -> e.random),
			Codec.list(Codec.INT).fieldOf("clist").orElse(List.of(16777215)).forGetter(e -> e.clist),
			Codec.list(Codec.FLOAT).fieldOf("alist").orElse(List.of(0.2f * 0.75f)).forGetter(e -> e.alist),
			Codec.list(Codec.INT).fieldOf("light").orElse(List.of(15728880)).forGetter(e -> e.light),
			Codec.STRING.fieldOf("render").xmap(RenderType::valueOf, Enum::name).orElse(RenderType.PARTICLE_SHEET_TRANSLUCENT).forGetter(e -> e.renderType)
		).apply(instance, SeqEffect::new)
	);

	public List<Double> xlist, ylist, zlist;
	public int age, random;
	public List<Integer> clist;
	public List<Float> alist;
	public List<Integer> light;
	public RenderType renderType;

	@Override
	public @NotNull ParticleType<?> getType() {
		return type;
	}

	public void write(FriendlyByteBuf buf) {
		writeDoubleArray(buf, xlist);
		writeDoubleArray(buf, ylist);
		writeDoubleArray(buf, zlist);
		buf.writeVarInt(age);
		buf.writeVarInt(random);
		writeIntArray(buf, clist);
		writeFloatArray(buf, alist);
	}


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
