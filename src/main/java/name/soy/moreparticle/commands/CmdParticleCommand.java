package name.soy.moreparticle.commands;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import name.soy.moreparticle.MoreParticlePayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CmdParticleCommand {
	public static Map<String, Codec<? extends Serializable>> commandRegistry = HashMap.newHashMap(10);

	public static void registerCodec(String name, Codec<? extends Serializable> codec) {
		commandRegistry.put(name, codec);
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		RequiredArgumentBuilder<CommandSourceStack, String> tagCommand = Commands.argument("tag", StringArgumentType.string());
		commandRegistry.forEach((name, codec) -> {
			tagCommand.then(Commands.literal(name)
				.then(Commands.argument("data", new ParticleCommandArgument<>(codec)).executes((CommandContext<CommandSourceStack> context) -> {
					var players = EntityArgument.getPlayers(context, "players");

					try {
						var data = context.getArgument("data", Serializable.class);
						var tag = StringArgumentType.getString(context, "tag");
						var buf = Unpooled.buffer();
						buf.writeInt(3);
						Utf8String.write(buf, tag, Short.MAX_VALUE);
						Utf8String.write(buf, name, Short.MAX_VALUE);
						var json = ((Codec) codec).<JsonElement>encode(data, JsonOps.INSTANCE, new JsonObject());
						if (json.isSuccess()) {
							Utf8String.write(buf, json.result().get().toString(),Short.MAX_VALUE);
							players.forEach(player -> {
								player.connection.send(new ClientboundCustomPayloadPacket(new MoreParticlePayload(buf)));
							});
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					return players.size();
				}))
			);
		});
		dispatcher.register(Commands.literal("cmdparticle").
			requires(s -> s.hasPermission(2))
			.then(Commands.argument("players", EntityArgument.players()).then(tagCommand))
		);
	}
}
