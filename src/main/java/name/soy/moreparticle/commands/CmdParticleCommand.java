package name.soy.moreparticle.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.HashMap;
import java.util.Map;

public class CmdParticleCommand {
	public static Map<String, Class<? extends CommandableParticle<?>>> commandRegistry = HashMap.newHashMap(10);


	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("cmdparticle").
			requires(s -> s.hasPermission(2))
			.then(Commands.argument("type", StringArgumentType.string())
				.then(Commands.argument("tag", StringArgumentType.string())
					.then(Commands.argument("action",StringArgumentType.string()))

				)
			)

		);
	}
}
