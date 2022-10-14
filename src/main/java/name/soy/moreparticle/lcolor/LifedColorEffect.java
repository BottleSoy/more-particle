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
public class LifedColorEffect implements ParticleEffect {
	public static ParticleType<LifedColorEffect> type;
	public static ParticleType<LifedColorEffect> register(){
		type = MoreParticle.register(
				new Identifier("soy", "life_color_particle"),
				LifedColorEffect.PARAMETERS_FACTORY,
				(particleType) -> LifedColorEffect.CODEC
		);
		return type;
	}
	public static final Codec<LifedColorEffect> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.INT.fieldOf("color").forGetter(e -> e.color),
					Codec.INT.fieldOf("life").forGetter(e -> e.life),
					Codec.INT.fieldOf("rand").forGetter(e -> e.rand)
			).apply(instance, LifedColorEffect::new));
	public static final Factory<LifedColorEffect> PARAMETERS_FACTORY = new Factory<LifedColorEffect>() {
		public LifedColorEffect read(ParticleType<LifedColorEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			int color, life, rand;
			stringReader.expect(' ');
			color = stringReader.readInt();
			stringReader.expect(' ');
			life = stringReader.readInt();
			stringReader.expect(' ');
			rand = stringReader.readInt();
			return new LifedColorEffect(color, life, rand);
		}

		public LifedColorEffect read(ParticleType<LifedColorEffect> particleType, PacketByteBuf packetByteBuf) {
			return new LifedColorEffect(packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt());
		}
	};
	int color, life, rand;

	@Override
	public ParticleType<?> getType() {
		return type;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(color);
		buf.writeInt(life);
		buf.writeInt(rand);
	}

	@Override
	public String asString() {
		return toString();
	}
}
