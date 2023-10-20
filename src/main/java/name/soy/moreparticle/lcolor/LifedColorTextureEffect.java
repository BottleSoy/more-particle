package name.soy.moreparticle.lcolor;

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
public class LifedColorTextureEffect implements ParticleEffect {
	public static ParticleType<LifedColorTextureEffect> type;

	public static ParticleType<LifedColorTextureEffect> register() {
		type = MoreParticle.register(
			new Identifier("soy", "life_color_particle_texture"),
			LifedColorTextureEffect.PARAMETERS_FACTORY,
			(particleType) -> LifedColorTextureEffect.CODEC
		);
		return type;
	}

	public static final Codec<LifedColorTextureEffect> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.INT.fieldOf("color").forGetter(e -> e.color),
			Codec.INT.fieldOf("life").forGetter(e -> e.life),
			Codec.INT.fieldOf("rand").forGetter(e -> e.rand),
			Codec.FLOAT.fieldOf("scale").forGetter(e -> e.scale),
			Codec.STRING.fieldOf("texture").forGetter(e -> e.texture)
		).apply(instance, LifedColorTextureEffect::new));
	public static final Factory<LifedColorTextureEffect> PARAMETERS_FACTORY = new Factory<>() {
		public LifedColorTextureEffect read(ParticleType<LifedColorTextureEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			int color, life, rand;
			stringReader.expect(' ');
			color = stringReader.readInt();
			stringReader.expect(' ');
			life = stringReader.readInt();
			stringReader.expect(' ');
			rand = stringReader.readInt();
			stringReader.expect(' ');
			float scale = stringReader.readFloat();
			stringReader.expect(' ');
			stringReader.expect('\'');
			String texture = stringReader.readStringUntil('\'');
			return new LifedColorTextureEffect(color, life, rand, scale, texture);
		}

		public LifedColorTextureEffect read(ParticleType<LifedColorTextureEffect> particleType, PacketByteBuf packetByteBuf) {
			return new LifedColorTextureEffect(
				packetByteBuf.readInt(),
				packetByteBuf.readInt(),
				packetByteBuf.readInt(),
				packetByteBuf.readFloat(),
				packetByteBuf.readString()
			);
		}
	};
	int color, life, rand;
	float scale;
	String texture;

	@Override
	public ParticleType<?> getType() {
		return type;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(color);
		buf.writeInt(life);
		buf.writeInt(rand);
		buf.writeFloat(scale);
		buf.writeString(texture);
	}

	@Override
	public String asString() {
		return toString();
	}
}
