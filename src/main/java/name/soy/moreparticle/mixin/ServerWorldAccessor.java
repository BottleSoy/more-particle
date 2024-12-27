package name.soy.moreparticle.mixin;


import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
@Mixin(ServerLevel.class)
public interface ServerWorldAccessor {
	@Invoker("sendParticles")
	boolean shouldSendParticle(ServerPlayer player, boolean force, double x, double y, double z, Packet<?> packet);

}