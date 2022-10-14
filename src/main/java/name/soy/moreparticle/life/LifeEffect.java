package name.soy.moreparticle.life;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

@Data
@AllArgsConstructor
public abstract class LifeEffect implements ParticleEffect {

	int base, random;

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(base);
		buf.writeInt(random);
	}

	@Override
	public String asString() {
		return this.toString();
	}
}
