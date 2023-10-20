package name.soy.moreparticle.mixin;

import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.client.MoreParticleClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.HashSet;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener {

	@Final
	@Shadow
	private MinecraftClient client;


	@Shadow
	public abstract ClientWorld getWorld();

	@Shadow
	@Final
	private Random random;

	@Shadow
	public abstract void onParticle(ParticleS2CPacket packet);

	@Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
	private void onCustomPayload(CustomPayloadS2CPacket payload, CallbackInfo ci) {
		if (payload.getChannel().equals(MoreParticle.id)) {
			ci.cancel();
			PacketByteBuf buf = payload.getData();
			int action = buf.readInt();
			if (action == -1) {
				if (MoreParticleClient.noParticle) return;

				NetworkThreadUtils.forceMainThread(payload, this, client);
				var count = buf.readInt();
				for (int i = 0; i < count; i++) {
					var packet = new ParticleS2CPacket(buf);
					this.onParticle(packet);
				}
			} else if (action == 0) {
				String tag = buf.readString();
				NetworkThreadUtils.forceMainThread(payload, this, client);

				ParticleS2CPacket packet = new ParticleS2CPacket(buf);
				ClientWorldAccessor world = (ClientWorldAccessor) getWorld();
				WorldRenderInvoker render = (WorldRenderInvoker) world.getWorldRenderer();
				if (packet.getCount() == 0) {
					double d = packet.getSpeed() * packet.getOffsetX();
					double e = packet.getSpeed() * packet.getOffsetY();
					double f = packet.getSpeed() * packet.getOffsetZ();
					Particle particle = render.createParticle(packet.getParameters(), packet.isLongDistance(), false,
						packet.getX(), packet.getY(), packet.getZ(), d, e, f);

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
				} else {
					for (int i = 0; i < packet.getCount(); ++i) {
						double g = random.nextGaussian() * (double) packet.getOffsetX();
						double h = random.nextGaussian() * (double) packet.getOffsetY();
						double j = random.nextGaussian() * (double) packet.getOffsetZ();
						double k = random.nextGaussian() * (double) packet.getSpeed();
						double l = random.nextGaussian() * (double) packet.getSpeed();
						double m = random.nextGaussian() * (double) packet.getSpeed();

						Particle particle = render.createParticle(packet.getParameters(), packet.isLongDistance(), false,
							g, h, j,
							k, l, m);
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
			} else if (action == 1) {
				String tag = buf.readString();
				HashSet<Particle> removeparticles = MoreParticleClient.particleTags.remove(tag);
				if (removeparticles != null)
					for (Particle particle : removeparticles) {
						if (particle != null) {
							MoreParticleClient.tagParticles.remove(particle);
							particle.markDead();
						}
					}
			} else if (action == 2) {
				MoreParticleClient.pm.setWorld(MinecraftClient.getInstance().world);
			}
		}
	}
}
