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

import static name.soy.moreparticle.utils.WriteUtils.*;


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
			Codec.INT.fieldOf("random").orElse(1).forGetter(e -> e.random),
			Codec.list(Codec.INT).fieldOf("clist").orElse(List.of(16777215)).forGetter(e -> e.clist),
			Codec.list(Codec.FLOAT).fieldOf("alist").orElse(List.of(0.25f)).forGetter(e -> e.alist),
			Codec.list(Codec.INT).fieldOf("light").orElse(List.of(15728880)).forGetter(e -> e.light),
			Codec.STRING.fieldOf("render").xmap(RenderType::valueOf, Enum::name).orElse(RenderType.PARTICLE_SHEET_TRANSLUCENT).forGetter(e -> e.renderType),
			Codec.STRING.fieldOf("texture").forGetter(e -> e.texture)
		).apply(instance, (xlist, ylist, zlist, age, random, clist, alist, light, renderType, texture) ->
			new SeqTEffect(
				new ArrayList<>(xlist),
				new ArrayList<>(ylist),
				new ArrayList<>(zlist),
				age,
				random,
				new ArrayList<>(clist),
				new ArrayList<>(alist),
				new ArrayList<>(light),
				renderType,
				texture
			)));

	public SeqTEffect(ArrayList<Double> xlist, ArrayList<Double> ylist, ArrayList<Double> zlist,
	                  int age, int random, ArrayList<Integer> clist,
	                  ArrayList<Float> alist, ArrayList<Integer> light, RenderType renderType,
	                  String texture) {
		super(xlist, ylist, zlist, age, random, clist, alist, light, renderType);
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
			writeIntArray(buf, effect.light);
			buf.writeEnum(effect.renderType);
			buf.writeUtf(effect.texture);
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
				readIntArray(buf),
				buf.readEnum(RenderType.class),
				buf.readUtf()
			);
		}
	};

	@Override
	public @NotNull ParticleType<?> getType() {
		return type;
	}

}
