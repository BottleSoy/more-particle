package name.soy.moreparticle;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MoreParticlePayload(PacketByteBuf buf) implements CustomPayload {

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeBytes(this.buf);
	}

	@Override
	public Identifier id() {
		return MoreParticle.id;
	}
}
