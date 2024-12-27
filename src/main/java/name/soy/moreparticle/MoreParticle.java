package name.soy.moreparticle;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.Unpooled;
import name.soy.moreparticle.calc.CalcEffect;
import name.soy.moreparticle.command.KillParticleCommand;
import name.soy.moreparticle.seq.SeqEffect;
import name.soy.moreparticle.seq.SeqTEffect;
import name.soy.moreparticle.seq.SeqVEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.impl.networking.CustomPayloadTypeProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class MoreParticle implements ModInitializer {
	public static final List<ClientboundLevelParticlesPacket> packets = new ArrayList<>();
	public static final ResourceLocation id = ResourceLocation.fromNamespaceAndPath("soy", "more-particle");


	@Override
	public void onInitialize() {
		CalcEffect.register();
		SeqEffect.register();
		SeqTEffect.register();
		SeqVEffect.register();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			KillParticleCommand.register(dispatcher);
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (!packets.isEmpty()) {
				RegistryFriendlyByteBuf bytes = RegistryFriendlyByteBuf.decorator(server.registryAccess()).apply(Unpooled.buffer());
				bytes.writeInt(-1);
				bytes.writeInt(packets.size());
				packets.forEach(packet -> ClientboundLevelParticlesPacket.STREAM_CODEC.encode(bytes, packet));
				packets.clear();
				server.getPlayerList().broadcastAll(new ClientboundCustomPayloadPacket(new MoreParticlePayload(bytes)));
			}
		});
	}

	public static <T extends ParticleOptions> ParticleType<T> register(
		ResourceLocation name,
		final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> stream,
		final Function<ParticleType<T>, MapCodec<T>> text) {
		ParticleType<T> type = new ParticleType<>(true) {
			@Override @NotNull
			public MapCodec<T> codec() {
				return text.apply(this);
			}

			@Override @NotNull
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return stream.apply(this);
			}
		};
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, name, type);
	}
}
