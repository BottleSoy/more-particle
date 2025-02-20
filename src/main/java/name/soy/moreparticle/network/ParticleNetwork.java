package name.soy.moreparticle.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public record ParticleNetwork(
	ResourceLocation id,
	File file
) {
	public void process(List<ServerPlayer> players) throws IOException {
		var input = new FileInputStream(file);
		players.forEach(player -> {

		});
		input.close();
	}
}
