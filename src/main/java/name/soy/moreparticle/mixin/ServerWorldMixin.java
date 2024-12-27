package name.soy.moreparticle.mixin;

import name.soy.moreparticle.MoreParticle;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
	//	@Inject(method = "sendToPlayerIfNearby", at = @At("RETURN"), cancellable = true)
//	private void alwaysSend(ServerPlayerEntity player, boolean force, double x, double y, double z, Packet<?> packet, CallbackInfoReturnable<Boolean> cir) {
//		if (!cir.getReturnValue())
//			player.networkHandler.sendPacket(packet);
//		cir.setReturnValue(true);
//	}

	@Redirect(method = "sendParticles(Lnet/minecraft/server/level/ServerPlayer;ZDDDLnet/minecraft/network/protocol/Packet;)Z", at = @At(value = "INVOKE",target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
	private void topackets(ServerGamePacketListenerImpl instance, Packet<?> packet) {
		MoreParticle.packets.add((ClientboundLevelParticlesPacket) packet);
	}
}
