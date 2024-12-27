package name.soy.moreparticle.mixin;

import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.MoreParticlePayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.network.protocol.common.custom.CustomPacketPayload$1")
public class CustomPayloadS2CPacketMixin {
	@Inject(method = "findCodec", at = @At("RETURN"), cancellable = true)
	private <B extends FriendlyByteBuf> void addMPP(ResourceLocation resourceLocation, CallbackInfoReturnable<StreamCodec<? super B, ? extends CustomPacketPayload>> cir) {
		System.out.println(cir.getReturnValue());
		if (resourceLocation.equals(MoreParticle.id)) {
			cir.setReturnValue((StreamCodec<? super B, ? extends CustomPacketPayload>) MoreParticlePayload.STREAM_CODEC);
		}
	}
}
