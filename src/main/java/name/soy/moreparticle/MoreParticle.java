package name.soy.moreparticle;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.Unpooled;
import name.soy.moreparticle.calc.CalcEffect;
import name.soy.moreparticle.commands.CmdParticleCommand;
import name.soy.moreparticle.commands.KillParticleCommand;
import name.soy.moreparticle.commands.ParticleCommandArgument;
import name.soy.moreparticle.seq.SeqEffect;
import name.soy.moreparticle.seq.SeqTEffect;
import name.soy.moreparticle.seq.SeqVCommand;
import name.soy.moreparticle.seq.SeqVEffect;
import name.soy.moreparticle.vertex.VertexEffect;
import name.soy.moreparticle.vertex4.Vertex4Effect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;


public class MoreParticle implements ModInitializer {
	public static final HashMap<ServerPlayer, ArrayList<ClientboundLevelParticlesPacket>> allpacket = new HashMap<>();
	public static final ResourceLocation id = ResourceLocation.fromNamespaceAndPath("soy", "more-particle");


	@Override
	public void onInitialize() {
		CalcEffect.register();
		SeqEffect.register();
		SeqTEffect.register();
		SeqVEffect.register();
		VertexEffect.register();
		Vertex4Effect.register();
		ArgumentTypeRegistry.registerArgumentType(ResourceLocation.parse("soy:particle_command"), ParticleCommandArgument.class,
			SingletonArgumentInfo.contextFree(() -> new ParticleCommandArgument<>(SeqVCommand.CODEC.codec())));
		for (ParticleType<?> particleType : BuiltInRegistries.PARTICLE_TYPE) {
			System.out.println("particle:" + BuiltInRegistries.PARTICLE_TYPE.getKey(particleType) + " ID:" + BuiltInRegistries.PARTICLE_TYPE.getId(particleType));
		}
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			KillParticleCommand.register(dispatcher);
			CmdParticleCommand.register(dispatcher);
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (!allpacket.isEmpty()) {
				allpacket.forEach((player, packets) -> {
					RegistryFriendlyByteBuf bytes = RegistryFriendlyByteBuf.decorator(server.registryAccess()).apply(Unpooled.buffer());
					bytes.writeInt(-1);
					bytes.writeInt(packets.size());
					packets.forEach((p) ->
						ClientboundLevelParticlesPacket.STREAM_CODEC.encode(bytes, p)
					);

					player.connection.send(new ClientboundCustomPayloadPacket(new MoreParticlePayload(bytes)));
				});

				allpacket.clear();
			}
		});

	}

	public static <T extends ParticleOptions> ParticleType<T> register(
		final ResourceLocation name,
		final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> stream,
		final Function<ParticleType<T>, MapCodec<T>> text) {
		ParticleType<T> type = new ParticleType<>(true) {
			@Override
			@NotNull
			public MapCodec<T> codec() {
				return text.apply(this);
			}

			@Override
			@NotNull
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return stream.apply(this);
			}
		};
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, name, type);
	}
}
