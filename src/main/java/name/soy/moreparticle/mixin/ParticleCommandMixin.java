package name.soy.moreparticle.mixin;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.netty.buffer.Unpooled;
import name.soy.moreparticle.MoreParticlePayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.commands.ParticleCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(ParticleCommand.class)
public class ParticleCommandMixin {
	@Shadow
	@Final
	private static SimpleCommandExceptionType ERROR_FAILED;

	//force
	@Redirect(method = "register", at = @At(value = "INVOKE", ordinal = 0, target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
	private static ArgumentBuilder<CommandSourceStack, ?> beforeRegForce(LiteralArgumentBuilder<CommandSourceStack> instance, ArgumentBuilder<CommandSourceStack, ?> argumentBuilder) {
		return instance.then(argumentBuilder.then(Commands.argument("tag", StringArgumentType.string())
			.executes(context -> sendParticleWithTag(context, true))
		));
	}

	@Redirect(method = "register", at = @At(value = "INVOKE", ordinal = 1, target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
	private static ArgumentBuilder<CommandSourceStack, ?> beforeRegNormal(LiteralArgumentBuilder<CommandSourceStack> instance, ArgumentBuilder<CommandSourceStack, ?> argumentBuilder) {
		return instance.then(argumentBuilder.then(Commands.argument("tag", StringArgumentType.string())
			.executes(context -> sendParticleWithTag(context, false))));
	}

	@Unique
	private static int sendParticleWithTag(CommandContext<CommandSourceStack> context, boolean force) throws CommandSyntaxException {

		CommandSourceStack source = context.getSource();
		ParticleOptions parameters = ParticleArgument.getParticle(context, "name");
		Vec3 pos = Vec3Argument.getVec3(context, "pos");
		Vec3 delta = Vec3Argument.getVec3(context, "delta");
		float speed = FloatArgumentType.getFloat(context, "speed");
		int count = IntegerArgumentType.getInteger(context, "count");
		Collection<ServerPlayer> viewers = EntityArgument.getPlayers(context, "viewers");

		String tag = StringArgumentType.getString(context, "tag");
		int i = 0;

		for (ServerPlayer serverPlayerEntity : viewers) {

			if (sendTagParticleToPlayer(source.getLevel(), serverPlayerEntity, parameters, force, pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed, tag)) {
				++i;
			}
		}

		if (i == 0) {
			throw ERROR_FAILED.create();
		} else {
			source.sendSuccess(() -> Component.translatable("commands.particle.success", BuiltInRegistries.PARTICLE_TYPE.getId(parameters.getType())), true);
			return i;
		}
	}


	@Unique
	private static boolean sendTagParticleToPlayer(ServerLevel world, ServerPlayer player, ParticleOptions parameters, boolean force, double x, double y, double z, int count, double dx, double dy, double dz, float speed, String tag) {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(world.getServer().registryAccess()).apply(Unpooled.buffer());
		ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(parameters, force, x, y, z, (float) dx, (float) dy, (float) dz, (float) speed, count);
		buf.writeInt(0);
		buf.writeUtf(tag);
		ClientboundLevelParticlesPacket.STREAM_CODEC.encode(buf, packet);

		player.connection.send(new ClientboundCustomPayloadPacket(new MoreParticlePayload(buf)));
		return true;

	}

}
