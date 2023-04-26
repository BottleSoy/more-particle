package name.soy.moreparticle;

import com.mojang.serialization.Codec;
import io.netty.buffer.Unpooled;
import name.soy.moreparticle.calc.BestEffect;
import name.soy.moreparticle.color.ColorEffect;
import name.soy.moreparticle.command.KillParticleCommand;
import name.soy.moreparticle.lcolor.LifedColorEffect;
import name.soy.moreparticle.life.LifeEndRodEffect;
import name.soy.moreparticle.seq.SeqEffect;
import name.soy.moreparticle.speed.DelayedEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class MoreParticle implements ModInitializer {
	public static final List<ParticleS2CPacket> packets = new ArrayList<>();
	public static final Identifier id = new Identifier("soy", "more-particle");

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
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (!packets.isEmpty()) {
				var bytes = new PacketByteBuf(Unpooled.buffer());
				bytes.writeInt(-1);
				bytes.writeInt(packets.size());
				packets.forEach(packet -> packet.write(bytes));
				packets.clear();
				server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(id, bytes));
			}
		});
	}

	public static <T extends ParticleEffect> ParticleType<T> register(Identifier name, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> function) {
		return Registry.register(Registry.PARTICLE_TYPE, name, new ParticleType<T>(false, factory) {
			public Codec<T> getCodec() {
				return function.apply(this);
			}
		});
	}
}
