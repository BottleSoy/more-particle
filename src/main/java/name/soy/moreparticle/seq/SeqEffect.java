package name.soy.moreparticle.seq;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.utils.WriteUtils;
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

import static name.soy.moreparticle.utils.WriteUtils.*;


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

	public static final StreamCodec<RegistryFriendlyByteBuf, SeqEffect> STREAM_CODEC = new StreamCodec<>()  {
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
		).apply(instance, (xlist,ylist,zlist, age, random, clist, alist, light, renderType)->
		new SeqEffect(new ArrayList<>(xlist), new ArrayList<>(ylist), new ArrayList<>(zlist), age, random, new ArrayList<>(clist), new ArrayList<>(alist), new ArrayList<>(light), renderType))
	);

	public SeqEffect(ArrayList<Double> xlist, ArrayList<Double> ylist, ArrayList<Double> zlist, int age, int random, ArrayList<Integer> clist, ArrayList<Float> alist, ArrayList<Integer> light, RenderType renderType) {
		this.xlist = xlist;
		this.ylist = ylist;
		this.zlist = zlist;
		this.age = age;
		this.random = random;
		this.clist = clist;
		this.alist = alist;
		this.light = light;
		this.renderType = renderType;

	}

	public ArrayList<Double> xlist, ylist, zlist;
	public int age, random;
	public ArrayList<Integer> clist;
	public ArrayList<Float> alist;
	public ArrayList<Integer> light;
	public RenderType renderType;

	@Override
	public @NotNull ParticleType<?> getType() {
		return type;
	}


}
