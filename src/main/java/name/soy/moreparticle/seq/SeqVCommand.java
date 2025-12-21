package name.soy.moreparticle.seq;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@AllArgsConstructor
public class SeqVCommand implements Serializable {
	Optional<Double> x, y, z;
	Optional<Float>	ax, ay, az;
	Optional<Integer> color, light;
	Optional<Float> alpha;
	Optional<Boolean> noLerp;
 	Optional<String> texture;
	public static final MapCodec<SeqVCommand> CODEC = RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codec.DOUBLE.optionalFieldOf("x").forGetter(e -> e.x),
			Codec.DOUBLE.optionalFieldOf("y").forGetter(e -> e.y),
			Codec.DOUBLE.optionalFieldOf("z").forGetter(e -> e.z),
			Codec.FLOAT.optionalFieldOf("ax").forGetter(e -> e.ax),
			Codec.FLOAT.optionalFieldOf("ay").forGetter(e -> e.ay),
			Codec.FLOAT.optionalFieldOf("az").forGetter(e -> e.az),
			Codec.INT.optionalFieldOf("color").forGetter(e -> e.color),
			Codec.INT.optionalFieldOf("light").forGetter(e -> e.light),
			Codec.FLOAT.optionalFieldOf("alpha").forGetter(e -> e.alpha),
			Codec.BOOL.optionalFieldOf("noLerp").forGetter(e -> e.noLerp),
			Codec.STRING.optionalFieldOf("texture").forGetter(e -> e.texture)
		).apply(instance, SeqVCommand::new);
	});

}
