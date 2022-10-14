package name.soy.moreparticle.color;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.ToString;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

@AllArgsConstructor
@ToString
public class ColorEffect implements ParticleEffect {
	public static ParticleType<ColorEffect> type;

	public static ParticleType<ColorEffect> register(){
		type = MoreParticle.register(
				new Identifier("soy", "color_particle"),
				ColorEffect.PARAMETERS_FACTORY,
				(particleType) -> ColorEffect.CODEC
		);
		return type;
	}

	public static final Codec<ColorEffect> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(Codec.INT.fieldOf("color").forGetter(e -> e.color))
					.apply(instance, ColorEffect::new));
	public static final ParticleEffect.Factory<ColorEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<ColorEffect>() {
		public ColorEffect read(ParticleType<ColorEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			return new ColorEffect(stringReader.readInt());
		}

		public ColorEffect read(ParticleType<ColorEffect> particleType, PacketByteBuf packetByteBuf) {
			return new ColorEffect(packetByteBuf.readInt());
		}
	};
	int color;

	@Override
	public ParticleType<?> getType() {
		return type;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(color);
	}

	@Override
	public String asString() {
		return toString();
	}
}
