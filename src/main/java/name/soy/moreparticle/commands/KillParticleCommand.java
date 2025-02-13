package name.soy.moreparticle.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.netty.buffer.Unpooled;
import name.soy.moreparticle.MoreParticlePayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class KillParticleCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("killparticle")
			.requires((CommandSourceStack serverCommandSource) -> serverCommandSource.hasPermission(2))
			.executes(context -> {
				killParicle(Lists.newArrayList(context.getSource().getPlayer()), null);
				return 1;
			})
			.then(Commands.argument("players", EntityArgument.players())
				.executes(context -> {
					killParicle(EntityArgument.getPlayers(context, "players"), null);
					return 1;
				})
				.then(Commands.argument("tag", StringArgumentType.string())
					.executes(context -> {
						killParicle(EntityArgument.getPlayers(context, "players"), StringArgumentType.getString(context, "tag"));
						return 1;
					})
				)
			)
		);
	}

	public static void killParicle(Collection<ServerPlayer> players, String tag) {
		players.forEach(player -> {
			var buf = RegistryFriendlyByteBuf.decorator(player.server.registryAccess()).apply(Unpooled.buffer());
			if (tag == null) {
				buf.writeInt(2);
			} else {
				buf.writeInt(1);
				buf.writeUtf(tag);
			}

			player.connection.send(new ClientboundCustomPayloadPacket(new MoreParticlePayload(buf)));
		});
	}
}
