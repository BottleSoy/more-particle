package name.soy.moreparticle.life;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

public class LifeEndFireWorkEffect extends LifeEffect {

    public static ParticleType<LifeEndRodEffect> type;

    public static void register() {
        type = MoreParticle.register(
                new Identifier("soy", "life_firework"),
                LifeEndRodEffect.PARAMETERS_FACTORY,
                (particleType) -> LifeEndRodEffect.CODEC
        );
    }

    public static final Codec<LifeEndFireWorkEffect> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.INT.fieldOf("base").forGetter((gravity) -> gravity.base),
                    Codec.INT.fieldOf("random").forGetter((gravity) -> gravity.random)
            ).apply(instance, LifeEndFireWorkEffect::new));
    public static final Factory<LifeEndFireWorkEffect> PARAMETERS_FACTORY = new Factory<LifeEndFireWorkEffect>() {
        public LifeEndFireWorkEffect read(ParticleType<LifeEndFireWorkEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            int base = stringReader.readInt();
            stringReader.expect(' ');
            int random = stringReader.readInt();
            return new LifeEndFireWorkEffect(base, random);
        }

        public LifeEndFireWorkEffect read(ParticleType<LifeEndFireWorkEffect> particleType, PacketByteBuf packetByteBuf) {
            return new LifeEndFireWorkEffect(packetByteBuf.readInt(), packetByteBuf.readInt());
        }
    };

    public LifeEndFireWorkEffect(int base, int random) {
        super(base, random);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
