package name.soy.moreparticle.commands;

import com.mojang.brigadier.CommandDispatcher;
import name.soy.moreparticle.network.ParticleNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ParticleNetworkCommand {
	public static Map<ResourceLocation, ParticleNetwork> networkMap;

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("particlenetwork")
			.requires((CommandSourceStack serverCommandSource) -> serverCommandSource.hasPermission(2))
			.then(Commands.argument("packet", ResourceLocationArgument.id())
				.executes(context -> {

					return 1;
				})
			)
		);
	}
}
