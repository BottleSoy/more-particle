package name.soy.moreparticle.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface WorldRenderInvoker {
    @Accessor()
    Minecraft getMinecraft();
    @Invoker("addParticleInternal")
    Particle addParticle(ParticleOptions parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal,
                         double x, double y, double z,
                         double velocityX, double velocityY, double velocityZ);
}
