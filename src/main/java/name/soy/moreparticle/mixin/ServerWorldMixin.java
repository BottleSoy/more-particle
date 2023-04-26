package name.soy.moreparticle.mixin;

import name.soy.moreparticle.MoreParticle;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	//	@Inject(method = "sendToPlayerIfNearby", at = @At("RETURN"), cancellable = true)
//	private void alwaysSend(ServerPlayerEntity player, boolean force, double x, double y, double z, Packet<?> packet, CallbackInfoReturnable<Boolean> cir) {
//		if (!cir.getReturnValue())
//			player.networkHandler.sendPacket(packet);
//		cir.setReturnValue(true);
//	}
	@Redirect(method = "sendToPlayerIfNearby", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"))
	private void topackets(ServerPlayNetworkHandler instance, Packet<ClientPlayPacketListener> packet) {
		MoreParticle.packets.add((ParticleS2CPacket) packet);
	}
}
