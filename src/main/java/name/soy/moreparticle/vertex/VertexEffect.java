package name.soy.moreparticle.vertex;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.commands.CmdParticleCommand;
import name.soy.moreparticle.seq.SeqEffect;
import name.soy.moreparticle.seq.SeqVEffect;
import name.soy.moreparticle.utils.WriteUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import static name.soy.moreparticle.utils.WriteUtils.*;

@AllArgsConstructor
public class VertexEffect implements ParticleOptions, Serializable, WriteUtils {

	public static ParticleType<VertexEffect> type;

	public static void register() {
		type = MoreParticle.register(
			ResourceLocation.fromNamespaceAndPath("soy", "vertex"),
			(particleType) -> VertexEffect.STREAM_CODEC,
			(particleType) -> VertexEffect.CODEC
		);

	}

	public int age;
	public ArrayList<Double> p1x, p1y, p1z,
		p2x, p2y, p2z,
		p3x, p3y, p3z;

	public ArrayList<Integer> l1, l2, l3;//顶点的光照
	public ArrayList<Integer> c1, c2, c3;//顶点的颜色


	public static final StreamCodec<RegistryFriendlyByteBuf, VertexEffect> STREAM_CODEC = new StreamCodec<>() {

		@Override
		public void encode(RegistryFriendlyByteBuf buf, VertexEffect effect) {
			buf.writeVarInt(effect.age);

			writeDoubleArray(buf, effect.p1x);
			writeDoubleArray(buf, effect.p1y);
			writeDoubleArray(buf, effect.p1z);

			writeDoubleArray(buf, effect.p2x);
			writeDoubleArray(buf, effect.p2y);
			writeDoubleArray(buf, effect.p2z);

			writeDoubleArray(buf, effect.p3x);
			writeDoubleArray(buf, effect.p3y);
			writeDoubleArray(buf, effect.p3z);

			writeIntArray(buf, effect.l1);
			writeIntArray(buf, effect.l2);
			writeIntArray(buf, effect.l3);

			writeIntArray(buf, effect.c1);
			writeIntArray(buf, effect.c2);
			writeIntArray(buf, effect.c3);
		}

		@Override
		public @NotNull VertexEffect decode(RegistryFriendlyByteBuf buf) {
			return new VertexEffect(
				buf.readVarInt(),
				readDoubleArray(buf), readDoubleArray(buf), readDoubleArray(buf),
				readDoubleArray(buf), readDoubleArray(buf), readDoubleArray(buf),
				readDoubleArray(buf), readDoubleArray(buf), readDoubleArray(buf),
				readIntArray(buf), readIntArray(buf), readIntArray(buf),
				readIntArray(buf), readIntArray(buf), readIntArray(buf)
			);
		}
	};
	public static final MapCodec<VertexEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codec.INT.fieldOf("age").forGetter(e -> e.age),

			Codec.list(Codec.DOUBLE).fieldOf("p1x").orElse(List.of()).forGetter(e -> e.p1x),
			Codec.list(Codec.DOUBLE).fieldOf("p1y").orElse(List.of()).forGetter(e -> e.p1y),
			Codec.list(Codec.DOUBLE).fieldOf("p1z").orElse(List.of()).forGetter(e -> e.p1z),

			Codec.list(Codec.DOUBLE).fieldOf("p2x").orElse(List.of()).forGetter(e -> e.p2x),
			Codec.list(Codec.DOUBLE).fieldOf("p2y").orElse(List.of()).forGetter(e -> e.p2y),
			Codec.list(Codec.DOUBLE).fieldOf("p2z").orElse(List.of()).forGetter(e -> e.p2z),

			Codec.list(Codec.DOUBLE).fieldOf("p3x").orElse(List.of()).forGetter(e -> e.p3x),
			Codec.list(Codec.DOUBLE).fieldOf("p3y").orElse(List.of()).forGetter(e -> e.p3y),
			Codec.list(Codec.DOUBLE).fieldOf("p3z").orElse(List.of()).forGetter(e -> e.p3z),

			Codec.list(Codec.INT).fieldOf("l1").orElse(List.of(15728880)).forGetter(e -> e.l1),
			Codec.list(Codec.INT).fieldOf("l2").orElse(List.of(15728880)).forGetter(e -> e.l2),
			Codec.list(Codec.INT).fieldOf("l3").orElse(List.of(15728880)).forGetter(e -> e.l3),

			Codec.list(Codec.INT).fieldOf("c1").orElse(List.of(16777215)).forGetter(e -> e.c1),
			Codec.list(Codec.INT).fieldOf("c2").orElse(List.of(16777215)).forGetter(e -> e.c2),
			Codec.list(Codec.INT).fieldOf("c3").orElse(List.of(16777215)).forGetter(e -> e.c3)

		).apply(instance, (age, p1x, p1y, p1z, p2x, p2y, p2z, p3x, p3y, p3z, l1, l2, l3, c1, c2, c3) -> {
			return new VertexEffect(
				age,
				new ArrayList<>(p1x), new ArrayList<>(p1y), new ArrayList<>(p1z),
				new ArrayList<>(p2x), new ArrayList<>(p2y), new ArrayList<>(p2z),
				new ArrayList<>(p3x), new ArrayList<>(p3y), new ArrayList<>(p3z),
				new ArrayList<>(l1), new ArrayList<>(l2), new ArrayList<>(l3),
				new ArrayList<>(c1), new ArrayList<>(c2), new ArrayList<>(c3)
			);
		});
	});

	@Override
	public ParticleType<?> getType() {
		return type;
	}
}
