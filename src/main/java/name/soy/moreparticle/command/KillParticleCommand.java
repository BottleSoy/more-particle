package name.soy.moreparticle.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import name.soy.moreparticle.MoreParticle;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class KillParticleCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("killparticle")
			.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
			.executes(context -> {
				killParicle(Lists.newArrayList(context.getSource().getPlayer()), null);
				return 1;
			})
			.then(CommandManager.argument("players", EntityArgumentType.players())
				.executes(context -> {
					killParicle(EntityArgumentType.getPlayers(context, "players"), null);
					return 1;
				})
				.then(CommandManager.argument("tag", StringArgumentType.string())
					.executes(context -> {
						killParicle(EntityArgumentType.getPlayers(context, "players"), StringArgumentType.getString(context, "tag"));
						return 1;
					})
				)
			)
		);
	}

	public static void killParicle(Collection<ServerPlayerEntity> players, String tag) {
		players.forEach(player -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(1);
			buf.writeString(tag);
			player.networkHandler.sendPacket(new CustomPayloadS2CPacket(MoreParticle.id, buf));
		});
	}
}
