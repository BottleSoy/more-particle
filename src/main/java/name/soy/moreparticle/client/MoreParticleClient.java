package name.soy.moreparticle.client;

import name.soy.moreparticle.calc.BestParticle;
import name.soy.moreparticle.color.ColoredParticle;
import name.soy.moreparticle.lcolor.LifedColoredParticle;
import name.soy.moreparticle.life.LifeEndRodParticle;
import name.soy.moreparticle.life.LifeFireWorkParticle;
import name.soy.moreparticle.seq.SeqParticle;
import name.soy.moreparticle.seq.SeqVParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

@Environment(EnvType.CLIENT)
public class MoreParticleClient implements ClientModInitializer {
	public static ParticleManager pm;
	public static final HashMap<String, HashSet<Particle>> particleTags = new HashMap<>();
	public static final HashMap<Particle, String> tagParticles = new HashMap<>();

	public static boolean noParticle = new File("config/more-particle/nop").exists();

	@Override
	public void onInitializeClient() {
		ColoredParticle.register();
		LifeEndRodParticle.register();
		LifeFireWorkParticle.register();
		BestParticle.register();
		LifedColoredParticle.register();
		SeqParticle.register();
		SeqVParticle.register();
//		ClientPlayNetworking.registerGlobalReceiver(MoreParticle.id, (client, handler, buf, responseSender) -> {
//
//		});
	}
}
