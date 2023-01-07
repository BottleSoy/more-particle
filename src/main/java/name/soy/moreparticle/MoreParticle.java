package name.soy.moreparticle;

import com.mojang.serialization.Codec;
import name.soy.moreparticle.calc.BestEffect;
import name.soy.moreparticle.color.ColorEffect;
import name.soy.moreparticle.command.KillParticleCommand;
import name.soy.moreparticle.lcolor.LifedColorEffect;
import name.soy.moreparticle.life.LifeEndRodEffect;
import name.soy.moreparticle.seq.SeqEffect;
import name.soy.moreparticle.speed.DelayedEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;


public class MoreParticle implements ModInitializer {

    public static final Identifier id = new Identifier("soy","more-particle");
    @Override
    public void onInitialize() {
        ColorEffect.register();
        BestEffect.register();
        LifedColorEffect.register();
        LifeEndRodEffect.register();
        DelayedEffect.register();
        SeqEffect.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            KillParticleCommand.register(dispatcher);
        });

    }

    public static <T extends ParticleEffect> ParticleType<T> register(Identifier name, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> function) {
        return Registry.register(Registries.PARTICLE_TYPE, name, new ParticleType<T>(false, factory) {
            public Codec<T> getCodec() {
                return function.apply(this);
            }
        });
    }
}
