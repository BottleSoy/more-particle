package name.soy.moreparticle.client;

import name.soy.moreparticle.calc.CalcParticle;
import name.soy.moreparticle.seq.SeqParticle;
import name.soy.moreparticle.seq.SeqVParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

@Environment(EnvType.CLIENT)
public class MoreParticleClient implements ClientModInitializer {
	public static ParticleEngine pm;
	public static final HashMap<String, HashSet<Particle>> particleTags = new HashMap<>();
	public static final HashMap<Particle, String> tagParticles = new HashMap<>();

	public static boolean noParticle = new File("config/more-particle/nop").exists();

	@Override
	public void onInitializeClient() {
		CalcParticle.register();
		SeqParticle.register();
		SeqVParticle.register();

	}
}
