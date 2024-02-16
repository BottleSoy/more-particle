package name.soy.moreparticle.life;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import name.soy.moreparticle.MoreParticle;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

public class LifeEndRodEffect extends LifeEffect {
    public static ParticleType<LifeEndRodEffect> type;

    public static void register() {
        type = MoreParticle.register(
                new Identifier("soy", "life_end_rod"),
                LifeEndRodEffect.PARAMETERS_FACTORY,
                (particleType) -> LifeEndRodEffect.CODEC
        );
    }

    public static final Codec<LifeEndRodEffect> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.INT.fieldOf("base").forGetter((gravity) -> gravity.base),
                    Codec.INT.fieldOf("random").forGetter((gravity) -> gravity.random)
            ).apply(instance, LifeEndRodEffect::new));
    public static final Factory<LifeEndRodEffect> PARAMETERS_FACTORY = new Factory<LifeEndRodEffect>() {
        public LifeEndRodEffect read(ParticleType<LifeEndRodEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            int base = stringReader.readInt();
            stringReader.expect(' ');
            int random = stringReader.readInt();
            return new LifeEndRodEffect(base, random);
        }

        public LifeEndRodEffect read(ParticleType<LifeEndRodEffect> particleType, PacketByteBuf packetByteBuf) {
            return new LifeEndRodEffect(packetByteBuf.readInt(), packetByteBuf.readInt());
        }
    };

    public LifeEndRodEffect(int base, int random) {
        super(base, random);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
