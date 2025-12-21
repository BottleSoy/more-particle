package name.soy.moreparticle.vertex4;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public class Vertex4Command implements Serializable {
	Optional<Double> p1x, p1y, p1z,
		p2x, p2y, p2z,
		p3x, p3y, p3z,
		p4x, p4y, p4z;

	Optional<Integer> l1, l2, l3, l4,
		c1, c2, c3, c4;
	Optional<String> texture;

	public static final MapCodec<Vertex4Command> MAP_CODEC = new MapCodec<Vertex4Command>() {
		@Override
		public <T> Stream<T> keys(DynamicOps<T> ops) {
			return Stream.of(
				ops.createString("age"),
				ops.createString("p1x"), ops.createString("p1y"), ops.createString("p1z"),
				ops.createString("p2x"), ops.createString("p2y"), ops.createString("p2z"),
				ops.createString("p3x"), ops.createString("p3y"), ops.createString("p3z"),
				ops.createString("p4x"), ops.createString("p4y"), ops.createString("p4z"),
				ops.createString("l1"), ops.createString("l2"), ops.createString("l3"), ops.createString("l4"),
				ops.createString("c1"), ops.createString("c2"), ops.createString("c3"), ops.createString("c4"),
				ops.createString("texture")
			);
		}

		@Override
		public <T> DataResult<Vertex4Command> decode(DynamicOps<T> ops, MapLike<T> input) {
			var p1x = Codec.DOUBLE.optionalFieldOf("p1x").decode(ops, input);
			var p1y = Codec.DOUBLE.optionalFieldOf("p1y").decode(ops, input);
			var p1z = Codec.DOUBLE.optionalFieldOf("p1z").decode(ops, input);
			var p2x = Codec.DOUBLE.optionalFieldOf("p2x").decode(ops, input);
			var p2y = Codec.DOUBLE.optionalFieldOf("p2y").decode(ops, input);
			var p2z = Codec.DOUBLE.optionalFieldOf("p2z").decode(ops, input);
			var p3x = Codec.DOUBLE.optionalFieldOf("p3x").decode(ops, input);
			var p3y = Codec.DOUBLE.optionalFieldOf("p3y").decode(ops, input);
			var p3z = Codec.DOUBLE.optionalFieldOf("p3z").decode(ops, input);
			var p4x = Codec.DOUBLE.optionalFieldOf("p4x").decode(ops, input);
			var p4y = Codec.DOUBLE.optionalFieldOf("p4y").decode(ops, input);
			var p4z = Codec.DOUBLE.optionalFieldOf("p4z").decode(ops, input);
			var l1 = Codec.INT.optionalFieldOf("l1").decode(ops, input);
			var l2 = Codec.INT.optionalFieldOf("l2").decode(ops, input);
			var l3 = Codec.INT.optionalFieldOf("l3").decode(ops, input);
			var l4 = Codec.INT.optionalFieldOf("l4").decode(ops, input);
			var c1 = Codec.INT.optionalFieldOf("c1").decode(ops, input);
			var c2 = Codec.INT.optionalFieldOf("c2").decode(ops, input);
			var c3 = Codec.INT.optionalFieldOf("c3").decode(ops, input);
			var c4 = Codec.INT.optionalFieldOf("c4").decode(ops, input);
			var texture = Codec.STRING.optionalFieldOf("texture").decode(ops, input);
			// 全部有值则构造对象
			try {
				var obj = new Vertex4Command(
					p1x.getOrThrow(),
					p1y.getOrThrow(),
					p1z.getOrThrow(),
					p2x.getOrThrow(),
					p2y.getOrThrow(),
					p2z.getOrThrow(),
					p3x.getOrThrow(),
					p3y.getOrThrow(),
					p3z.getOrThrow(),
					p4x.getOrThrow(),
					p4y.getOrThrow(),
					p4z.getOrThrow(),
					l1.getOrThrow(),
					l2.getOrThrow(),
					l3.getOrThrow(),
					l4.getOrThrow(),
					c1.getOrThrow(),
					c2.getOrThrow(),
					c3.getOrThrow(),
					c4.getOrThrow(),
					texture.getOrThrow()
				);
				return DataResult.success(obj);
			} catch (Exception e) {
				return DataResult.error(e::getMessage);
			}
		}

		@Override
		public <T> RecordBuilder<T> encode(Vertex4Command vertex, DynamicOps<T> ops, RecordBuilder<T> prefix) {
			vertex.p1x.ifPresent(aDouble -> prefix.add("p1x", aDouble, Codec.DOUBLE));
			vertex.p1y.ifPresent(aDouble -> prefix.add("p1y", aDouble, Codec.DOUBLE));
			vertex.p1z.ifPresent(aDouble -> prefix.add("p1z", aDouble, Codec.DOUBLE));
			vertex.p2x.ifPresent(aDouble -> prefix.add("p2x", aDouble, Codec.DOUBLE));
			vertex.p2y.ifPresent(aDouble -> prefix.add("p2y", aDouble, Codec.DOUBLE));
			vertex.p2z.ifPresent(aDouble -> prefix.add("p2z", aDouble, Codec.DOUBLE));
			vertex.p3x.ifPresent(aDouble -> prefix.add("p3x", aDouble, Codec.DOUBLE));
			vertex.p3y.ifPresent(aDouble -> prefix.add("p3y", aDouble, Codec.DOUBLE));
			vertex.p3z.ifPresent(aDouble -> prefix.add("p3z", aDouble, Codec.DOUBLE));
			vertex.p4x.ifPresent(aDouble -> prefix.add("p4x", aDouble, Codec.DOUBLE));
			vertex.p4y.ifPresent(aDouble -> prefix.add("p4y", aDouble, Codec.DOUBLE));
			vertex.p4z.ifPresent(aDouble -> prefix.add("p4z", aDouble, Codec.DOUBLE));
			vertex.l1.ifPresent(integer -> prefix.add("l1", integer, Codec.INT));
			vertex.l2.ifPresent(integer -> prefix.add("l2", integer, Codec.INT));
			vertex.l3.ifPresent(integer -> prefix.add("l3", integer, Codec.INT));
			vertex.l4.ifPresent(integer -> prefix.add("l4", integer, Codec.INT));
			vertex.c1.ifPresent(integer -> prefix.add("c1", integer, Codec.INT));
			vertex.c2.ifPresent(integer -> prefix.add("c2", integer, Codec.INT));
			vertex.c3.ifPresent(integer -> prefix.add("c3", integer, Codec.INT));
			vertex.c4.ifPresent(integer -> prefix.add("c4", integer, Codec.INT));
			vertex.texture.ifPresent(string -> prefix.add("texture", string, Codec.STRING));
			return prefix;
		}
	};

}
