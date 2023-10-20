package name.soy.moreparticle.mixin;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
@Mixin(ServerWorld.class)
public interface ServerWorldAccessor {
	@Invoker("sendToPlayerIfNearby")
	boolean shouldSendParticle(ServerPlayerEntity player, boolean force, double x, double y, double z, Packet<ClientPlayPacketListener> packet);

}