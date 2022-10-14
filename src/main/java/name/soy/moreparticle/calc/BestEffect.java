package name.soy.moreparticle.calc;

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
public class BestEffect implements ParticleEffect {

    public static ParticleType<BestEffect> type;

    public static void register() {
        type = MoreParticle.register(
                new Identifier("soy", "best"),
                BestEffect.PARAMETERS_FACTORY,
                (particleType) -> BestEffect.CODEC
        );
    }

    public static final Codec<BestEffect> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.STRING.fieldOf("xfun").forGetter(e -> e.xfun),
                    Codec.STRING.fieldOf("yfun").forGetter(e -> e.yfun),
                    Codec.STRING.fieldOf("zfun").forGetter(e -> e.zfun),
                    Codec.INT.fieldOf("age").forGetter(e -> e.age),
                    Codec.INT.fieldOf("random").forGetter(e -> e.random),
                    Codec.STRING.fieldOf("cfun").forGetter(e -> e.cfun)
            ).apply(instance, BestEffect::new));
    public static final Factory<BestEffect> PARAMETERS_FACTORY = new Factory<BestEffect>() {
        public BestEffect read(ParticleType<BestEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            stringReader.expect('\'');
            String xfun = stringReader.readStringUntil('\'');
            stringReader.expect(' ');
            stringReader.expect('\'');
            String yfun = stringReader.readStringUntil('\'');
            stringReader.expect(' ');
            stringReader.expect('\'');
            String zfun = stringReader.readStringUntil('\'');
            stringReader.expect(' ');
            int age = stringReader.readInt();
            stringReader.expect(' ');
            int random = stringReader.readInt();
            stringReader.expect(' ');
            stringReader.expect('\'');
            String cfun = stringReader.readStringUntil('\'');
            return new BestEffect(xfun, yfun, zfun, age, random, cfun);
        }

        public BestEffect read(ParticleType<BestEffect> particleType, PacketByteBuf buf) {
            return new BestEffect(
                    buf.readString(),
                    buf.readString(),
                    buf.readString(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readString());
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

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(xfun);
        buf.writeString(yfun);
        buf.writeString(zfun);
        buf.writeInt(age);
        buf.writeInt(random);
        buf.writeString(cfun);
    }

    @Override
    public String asString() {
        return toString();
    }
}
