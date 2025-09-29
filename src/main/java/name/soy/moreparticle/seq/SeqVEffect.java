package name.soy.moreparticle.seq;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.ToString;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

import static name.soy.moreparticle.utils.WriteUtils.*;
@ToString
public class SeqVEffect extends SeqTEffect implements ParticleOptions, Serializable {
	List<Float> angleX, angleY, angleZ;//旋转角XYZ(欧拉角)
	boolean relative;//是否相对于玩家视角旋转

	public SeqVEffect(List<Double> xlist, List<Double> ylist, List<Double> zlist,
	                  int age, int random, List<Integer> clist, List<Float> alist, List<Integer> light, RenderType renderType
		, String texture, List<Float> angleX, List<Float> angleY, List<Float> angleZ, boolean relative
	) {
		super(xlist, ylist, zlist, age, random, clist, alist, light, renderType, texture);
		this.angleX = angleX;
		this.angleY = angleY;
		this.angleZ = angleZ;
		this.relative = relative;
	}

	public static ParticleType<SeqVEffect> type;

	public static void register() {
		type = MoreParticle.register(ResourceLocation.fromNamespaceAndPath("soy", "seqv"),
			(particleType) -> SeqVEffect.STREAM_CODEC,
			(particleType) -> SeqVEffect.CODEC);
	}

	public static final StreamCodec<RegistryFriendlyByteBuf, SeqVEffect> STREAM_CODEC = new StreamCodec<>() {

		@Override
		public void encode(RegistryFriendlyByteBuf buf, SeqVEffect effect) {
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
			writeFloatArray(buf, effect.angleX);
			writeFloatArray(buf, effect.angleY);
			writeFloatArray(buf, effect.angleZ);
			buf.writeBoolean(effect.relative);
		}

		@Override
		public @NotNull SeqVEffect decode(RegistryFriendlyByteBuf buf) {
			return new SeqVEffect(
				readDoubleArray(buf),
				readDoubleArray(buf),
				readDoubleArray(buf),
				buf.readVarInt(), buf.readVarInt(),
				readIntArray(buf),
				readFloatArray(buf),
				readIntArray(buf),
				buf.readEnum(RenderType.class),
				buf.readUtf(),
				readFloatArray(buf),
				readFloatArray(buf),
				readFloatArray(buf),
				buf.readBoolean()
			);
		}
	};
	public static final MapCodec<SeqVEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
			Codec.list(Codec.DOUBLE).fieldOf("xlist").orElse(List.of()).forGetter(e -> e.xlist),
			Codec.list(Codec.DOUBLE).fieldOf("ylist").orElse(List.of()).forGetter(e -> e.ylist),
			Codec.list(Codec.DOUBLE).fieldOf("zlist").orElse(List.of()).forGetter(e -> e.zlist),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("random").orElse(1).forGetter(e -> e.random),
			Codec.list(Codec.INT).fieldOf("clist").orElse(List.of(16777215)).forGetter(e -> e.clist),
			Codec.list(Codec.FLOAT).fieldOf("alist").orElse(List.of(0.2f * 0.75f)).forGetter(e -> e.alist),
			Codec.list(Codec.INT).fieldOf("light").orElse(List.of(15728880)).forGetter(e -> e.light),
			Codec.STRING.fieldOf("render").xmap(RenderType::valueOf, Enum::name).orElse(RenderType.PARTICLE_SHEET_TRANSLUCENT).forGetter(e -> e.renderType),
			Codec.STRING.fieldOf("texture").orElse("").forGetter(e -> e.texture),
			Codec.list(Codec.FLOAT).fieldOf("angleX").orElse(List.of(0f)).forGetter(e -> e.angleX),
			Codec.list(Codec.FLOAT).fieldOf("angleY").orElse(List.of(0f)).forGetter(e -> e.angleY),
			Codec.list(Codec.FLOAT).fieldOf("angleZ").orElse(List.of(0f)).forGetter(e -> e.angleZ),
			Codec.BOOL.fieldOf("relative").orElse(true).forGetter(e -> e.relative)
		).apply(instance, SeqVEffect::new));

	@NotNull
	@Override
	public ParticleType<SeqVEffect> getType() {
		return type;
	}
}
