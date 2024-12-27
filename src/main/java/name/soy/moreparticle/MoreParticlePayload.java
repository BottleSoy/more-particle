package name.soy.moreparticle;


import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.BreezeDebugPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MoreParticlePayload implements CustomPacketPayload {
	public static final Type<MoreParticlePayload> ID = new Type<>(MoreParticle.id);
	public static final StreamCodec<RegistryFriendlyByteBuf, MoreParticlePayload> STREAM_CODEC = CustomPacketPayload.<RegistryFriendlyByteBuf, MoreParticlePayload>codec(
		MoreParticlePayload::write,
		(RegistryFriendlyByteBuf buf) -> {
			int size = buf.readVarInt();
			ByteBuf bytes = buf.readBytes(size);

			return new MoreParticlePayload(bytes);
		});
	public final ByteBuf data;

	public MoreParticlePayload(ByteBuf data) {
		this.data = data;
	}

	public void write(RegistryFriendlyByteBuf output) {
		int size = data.writerIndex();
		output.writeVarInt(size);
		output.writeBytes(data, size);
	}

	@Override
	public @NotNull Type<MoreParticlePayload> type() {
		return ID;
	}

}
