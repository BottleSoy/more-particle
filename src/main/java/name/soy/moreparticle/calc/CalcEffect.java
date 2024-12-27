package name.soy.moreparticle.calc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.ToString;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

@AllArgsConstructor
@ToString
public class CalcEffect implements ParticleOptions {

	public static ParticleType<CalcEffect> type;

	public static void register() {
		type = MoreParticle.register(
			ResourceLocation.fromNamespaceAndPath("soy", "calc"),
			(particleType) -> CalcEffect.STREAM_CODEC,
			(particleType) -> CalcEffect.CODEC
		);
	}

	public static final MapCodec<CalcEffect> CODEC = RecordCodecBuilder.mapCodec((instance) ->
		instance.group(
			Codec.STRING.fieldOf("xfun").forGetter(e -> e.xfun),
			Codec.STRING.fieldOf("yfun").forGetter(e -> e.yfun),
			Codec.STRING.fieldOf("zfun").forGetter(e -> e.zfun),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("random").forGetter(e -> e.random),
			Codec.STRING.fieldOf("cfun").forGetter(e -> e.cfun)
		).apply(instance, CalcEffect::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, CalcEffect> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public CalcEffect decode(RegistryFriendlyByteBuf buf) {
			return new CalcEffect(
				buf.readUtf(),
				buf.readUtf(),
				buf.readUtf(),
				buf.readInt(),
				buf.readInt(),
				buf.readUtf());
		}

		@Override
		public void encode(RegistryFriendlyByteBuf buf, CalcEffect effect) {
			buf.writeUtf(effect.xfun);
			buf.writeUtf(effect.yfun);
			buf.writeUtf(effect.zfun);
			buf.writeInt(effect.age);
			buf.writeInt(effect.random);
			buf.writeUtf(effect.cfun);
		}
	};
	//粒子运行函数
	public String xfun, yfun, zfun;
	public int age, random;
	public String cfun;

	@Override
	public ParticleType<?> getType() {
		return type;
	}
}
