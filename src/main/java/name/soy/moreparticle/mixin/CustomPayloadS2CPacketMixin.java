package name.soy.moreparticle.mixin;

import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.MoreParticlePayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin {
	@Inject(method = "readPayload", at = @At("HEAD"), cancellable = true)
	private static void readMPP(Identifier id, PacketByteBuf buf, CallbackInfoReturnable<CustomPayload> cir) {
		if (id.equals(MoreParticle.id)) {
			cir.setReturnValue(new MoreParticlePayload(buf));
		}
	}

//	@ModifyConstant(method = "<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = 0x100000))
//	private int toMax2(int constant) {
//		return Integer.MAX_VALUE;
//	}
}
