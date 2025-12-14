package name.soy.moreparticle.calc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
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
import org.jetbrains.annotations.NotNull;

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

	public static final MapCodec<CalcEffect> CODEC = RecordCodecBuilder.<CalcEffect>mapCodec((RecordCodecBuilder.Instance<CalcEffect> instance) -> {
		Products.P6<RecordCodecBuilder.Mu<CalcEffect>, String, String, String, Integer, Integer, String> group = instance.<String, String, String, Integer, Integer, String>group(
			Codec.STRING.fieldOf("xfun").<CalcEffect>forGetter(e -> e.xfun),
			Codec.STRING.fieldOf("yfun").<CalcEffect>forGetter(e -> e.yfun),
			Codec.STRING.fieldOf("zfun").<CalcEffect>forGetter(e -> e.zfun),
			Codec.INT.fieldOf("age").<CalcEffect>forGetter(e -> e.age),
			Codec.INT.fieldOf("random").orElse(1).<CalcEffect>forGetter(e -> e.random),
			Codec.STRING.fieldOf("cfun").orElse("16777215").<CalcEffect>forGetter(e -> e.cfun)
		);

		App<RecordCodecBuilder.Mu<CalcEffect>, CalcEffect> res = group.apply(instance, CalcEffect::new);
		return res;
	});

	public static final StreamCodec<RegistryFriendlyByteBuf, CalcEffect> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public @NotNull CalcEffect decode(RegistryFriendlyByteBuf buf) {
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
