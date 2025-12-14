package name.soy.moreparticle.mixin;

import io.netty.buffer.ByteBuf;
import name.soy.moreparticle.MoreParticlePayload;
import name.soy.moreparticle.client.MoreParticleClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientCommonPacketListener {

	@Final
	@Shadow
	protected Minecraft minecraft;


	@Shadow
	@Final
	protected Connection connection;

	@Inject(method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
		at = @At("HEAD"), cancellable = true)
	private void onCustomPayload(ClientboundCustomPayloadPacket payload, CallbackInfo ci) {
		if (!(((ClientCommonPacketListener) this) instanceof ClientPacketListener)) return;
		ClientCommonPacketListener lc = this;

		if (payload.payload() instanceof MoreParticlePayload mpp) {
			PacketUtils.ensureRunningOnSameThread(payload, this, minecraft);
			RandomSource random = ((ClientPacketListener) lc).getLevel().random;
			ci.cancel();
			FriendlyByteBuf buf = new FriendlyByteBuf(mpp.data);
			int action = buf.readInt();
			if (action == -1) {
				if (MoreParticleClient.noParticle) return;

				var count = buf.readInt();
				RegistryFriendlyByteBuf packetBuf = RegistryFriendlyByteBuf.decorator(this.minecraft.getConnection().registryAccess()).apply(buf);

				for (int i = 0; i < count; i++) {
					ClientboundLevelParticlesPacket packet = ClientboundLevelParticlesPacket.STREAM_CODEC.decode(packetBuf);
					((ClientPacketListener) lc).handleParticleEvent(packet);
				}
			} else if (action == 0) {
				String tag = buf.readUtf();
				RegistryFriendlyByteBuf packetBuf = RegistryFriendlyByteBuf.decorator(this.minecraft.getConnection().registryAccess()).apply(buf);

				ClientboundLevelParticlesPacket packet = ClientboundLevelParticlesPacket.STREAM_CODEC.decode(packetBuf);
				ClientWorldAccessor world = (ClientWorldAccessor) ((ClientPacketListener) lc).getLevel();
				WorldRenderInvoker render = (WorldRenderInvoker) world.getLevelRenderer();
				if (packet.getCount() == 0) {
					double d = packet.getMaxSpeed() * packet.getXDist();
					double e = packet.getMaxSpeed() * packet.getYDist();
					double f = packet.getMaxSpeed() * packet.getZDist();
					Particle particle = render.addParticle(packet.getParticle(), true, false,
						packet.getX(), packet.getY(), packet.getZ(), d, e, f);

					configParticleTag(tag, particle);
				} else {
					for (int i = 0; i < packet.getCount(); ++i) {
						double g = random.nextGaussian() * (double) packet.getXDist();
						double h = random.nextGaussian() * (double) packet.getYDist();
						double j = random.nextGaussian() * (double) packet.getZDist();
						double k = random.nextGaussian() * (double) packet.getMaxSpeed();
						double l = random.nextGaussian() * (double) packet.getMaxSpeed();
						double m = random.nextGaussian() * (double) packet.getMaxSpeed();

						Particle particle = render.addParticle(packet.getParticle(), true, false,
							g, h, j,
							k, l, m);
						configParticleTag(tag, particle);
					}
				}
			} else if (action == 1) {
				String tag = buf.readUtf();
				HashSet<Particle> removeparticles = MoreParticleClient.particleTags.remove(tag);
				if (removeparticles != null)
					for (Particle particle : removeparticles) {
						if (particle != null) {
							MoreParticleClient.tagParticles.remove(particle);
							particle.remove();
						}
					}
			} else if (action == 2) {
				MoreParticleClient.pm.setLevel(minecraft.level);
			}
			buf.release();
		}
	}

	@Unique
	private void configParticleTag(String tag, Particle particle) {
		MoreParticleClient.tagParticles.put(particle, tag);
		MoreParticleClient.particleTags.computeIfPresent(tag, (s, particles) -> {
			particles.add(particle);
			return particles;
		});
		MoreParticleClient.particleTags.computeIfAbsent(tag, (s) -> {
			HashSet<Particle> list = new HashSet<>();
			list.add(particle);
			return list;
		});
	}
}
