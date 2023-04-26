package name.soy.moreparticle.mixin;

import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin {
	@ModifyConstant(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = 0x100000))
	private int toMax(int constant) {
		return Integer.MAX_VALUE;
	}

	@ModifyConstant(method = "<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = 0x100000))
	private int toMax2(int constant) {
		return Integer.MAX_VALUE;
	}
}
