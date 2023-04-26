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
import name.soy.moreparticle.MoreParticle;
import net.minecraft.block.CommandBlock;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ParticleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(ParticleCommand.class)
public class ParticleCommandMixin {
	@Shadow
	@Final
	private static SimpleCommandExceptionType FAILED_EXCEPTION;


	//force
	@Redirect(method = "register", at =    @At(value = "INVOKE", ordinal = 0, target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
	private static ArgumentBuilder<ServerCommandSource, ?> beforeRegForce(LiteralArgumentBuilder<ServerCommandSource> instance, ArgumentBuilder<ServerCommandSource, ?> argumentBuilder) {
		return instance.then(argumentBuilder.then(CommandManager.argument("tag", StringArgumentType.string())
			.executes(context -> sendParticleWithTag(context, true))
		));
	}

	@Redirect(method = "register", at = @At(value = "INVOKE", ordinal = 1, target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
	private static ArgumentBuilder<ServerCommandSource, ?> beforeRegNormal(LiteralArgumentBuilder<ServerCommandSource> instance, ArgumentBuilder<ServerCommandSource, ?> argumentBuilder) {
		return instance.then(argumentBuilder.then(CommandManager.argument("tag", StringArgumentType.string())
			.executes(context -> sendParticleWithTag(context, false))));
	}

	private static int sendParticleWithTag(CommandContext<ServerCommandSource> context, boolean force) throws CommandSyntaxException {

		ServerCommandSource source = context.getSource();
		ParticleEffect parameters = ParticleEffectArgumentType.getParticle(context, "name");
		Vec3d pos = Vec3ArgumentType.getVec3(context, "pos");
		Vec3d delta = Vec3ArgumentType.getVec3(context, "delta");
		float speed = FloatArgumentType.getFloat(context, "speed");
		int count = IntegerArgumentType.getInteger(context, "count");
		Collection<ServerPlayerEntity> viewers = EntityArgumentType.getPlayers(context, "viewers");

		String tag = StringArgumentType.getString(context, "tag");
		int i = 0;

		for (ServerPlayerEntity serverPlayerEntity : viewers) {

			if (sendTagParticleToPlayer(source.getWorld(), serverPlayerEntity, parameters, force, pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed, tag)) {
				++i;
			}
		}

		if (i == 0) {
			throw FAILED_EXCEPTION.create();
		} else {
			source.sendFeedback(Text.translatable("commands.particle.success", Registry.PARTICLE_TYPE.getId(parameters.getType()).toString()), true);
			return i;
		}
	}


	private static boolean sendTagParticleToPlayer(ServerWorld world, ServerPlayerEntity player, ParticleEffect parameters, boolean force, double x, double y, double z, int count, double dx, double dy, double dz, float speed, String tag) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		Packet<?> packet = new ParticleS2CPacket(parameters, force, x, y, z, (float) dx, (float) dy, (float) dz, (float) speed, count);

		buf.writeInt(0);
		buf.writeString(tag);
		packet.write(buf);
		if (((ServerWorldAccessor) world).shouldSendParticle(player, force, x, y, z, packet)) {

			player.networkHandler.sendPacket(new CustomPayloadS2CPacket(MoreParticle.id, buf));
			return true;
		} else return false;
	}

}
