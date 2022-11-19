package name.soy.moreparticle.mixin;

import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

//	@Inject(method = "sendToPlayerIfNearby", at = @At("RETURN"), cancellable = true)
//	private void alwaysSend(ServerPlayerEntity player, boolean force, double x, double y, double z, Packet<?> packet, CallbackInfoReturnable<Boolean> cir) {
//		cir.setReturnValue(true);
//	}
}
