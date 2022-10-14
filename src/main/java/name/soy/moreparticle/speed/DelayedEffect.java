package name.soy.moreparticle.speed;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.lcolor.LifedColorEffect;
import name.soy.moreparticle.lcolor.LifedColoredParticle;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

@AllArgsConstructor
public class DelayedEffect implements ParticleEffect {
    public static ParticleType<DelayedEffect> type;

    public static void register() {
        type = MoreParticle.register(
                new Identifier("soy", "delayed_particle"),
                DelayedEffect.PARAMETERS_FACTORY,
                (particleType) -> DelayedEffect.CODEC
        );
    }

    public static final Codec<DelayedEffect> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("color").forGetter(e -> e.color),
                    Codec.INT.fieldOf("life").forGetter(e -> e.life),
                    Codec.INT.fieldOf("rand").forGetter(e -> e.rand),
                    Codec.INT.fieldOf("delayTime").forGetter(e -> e.delayTime),
                    Codec.DOUBLE.fieldOf("delayedSpeed").forGetter(e -> e.delayedSpeed)
            ).apply(instance, DelayedEffect::new));
    public static final Factory<DelayedEffect> PARAMETERS_FACTORY = new Factory<DelayedEffect>() {
        public DelayedEffect read(ParticleType<DelayedEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            int color, life, rand, delayTime;
            double delayedSpeed;
            stringReader.expect(' ');
            color = stringReader.readInt();
            stringReader.expect(' ');
            life = stringReader.readInt();
            stringReader.expect(' ');
            rand = stringReader.readInt();
            stringReader.expect(' ');
            delayTime = stringReader.readInt();
            stringReader.expect(' ');
            delayedSpeed = stringReader.readDouble();
            return new DelayedEffect(color, life, rand, delayTime, delayedSpeed);
        }

        public DelayedEffect read(ParticleType<DelayedEffect> particleType, PacketByteBuf buf) {
            return new DelayedEffect(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readDouble());
        }
    };
    int color, life, rand;
    int delayTime;
    double delayedSpeed;

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(color);
        buf.writeInt(life);
        buf.writeInt(rand);
        buf.writeInt(delayTime);
        buf.writeDouble(delayedSpeed);
    }

    @Override
    public String asString() {
        return toString();
    }
}
