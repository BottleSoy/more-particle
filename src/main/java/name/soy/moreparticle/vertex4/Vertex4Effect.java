// Vertex4Effect.java
package name.soy.moreparticle.vertex4;

import com.google.common.collect.Lists;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import name.soy.moreparticle.MoreParticle;
import name.soy.moreparticle.utils.WriteUtils;
import name.soy.moreparticle.vertex.VertexEffect;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static name.soy.moreparticle.utils.WriteUtils.*;

public class Vertex4Effect implements ParticleOptions, Serializable, WriteUtils {
	public static ParticleType<Vertex4Effect> type;

	public static void register() {
		type = MoreParticle.register(
			ResourceLocation.fromNamespaceAndPath("soy", "vertex4"),
			(particleType) -> Vertex4Effect.STREAM_CODEC,
			(particleType) -> Vertex4Effect.MAP_CODEC
		);

	}

	public int age;

	//四个顶点的坐标
	public ArrayList<Double> p1x, p1y, p1z;
	public ArrayList<Double> p2x, p2y, p2z;
	public ArrayList<Double> p3x, p3y, p3z;
	public ArrayList<Double> p4x, p4y, p4z;

	public ArrayList<Integer> l1, l2, l3, l4;//顶点的光照
	public ArrayList<Integer> c1, c2, c3, c4;//顶点的颜色
	public String texture;//材质

	public Vertex4Effect(
		int age,
		ArrayList<Double> p1x, ArrayList<Double> p1y, ArrayList<Double> p1z,
		ArrayList<Double> p2x, ArrayList<Double> p2y, ArrayList<Double> p2z,
		ArrayList<Double> p3x, ArrayList<Double> p3y, ArrayList<Double> p3z,
		ArrayList<Double> p4x, ArrayList<Double> p4y, ArrayList<Double> p4z,
		ArrayList<Integer> l1, ArrayList<Integer> l2, ArrayList<Integer> l3, ArrayList<Integer> l4,
		ArrayList<Integer> c1, ArrayList<Integer> c2, ArrayList<Integer> c3, ArrayList<Integer> c4,
		String texture
	) {
		this.age = age;
		this.p1x = p1x;
		this.p1y = p1y;
		this.p1z = p1z;
		this.p2x = p2x;
		this.p2y = p2y;
		this.p2z = p2z;
		this.p3x = p3x;
		this.p3y = p3y;
		this.p3z = p3z;
		this.p4x = p4x;
		this.p4y = p4y;
		this.p4z = p4z;
		this.l1 = l1;
		this.l2 = l2;
		this.l3 = l3;
		this.l4 = l4;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.texture = texture;
	}

	public static final StreamCodec<RegistryFriendlyByteBuf, Vertex4Effect> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public void encode(RegistryFriendlyByteBuf buf, Vertex4Effect effect) {
			buf.writeVarInt(effect.age);

			writeDoubleArray(buf, effect.p1x);
			writeDoubleArray(buf, effect.p1y);
			writeDoubleArray(buf, effect.p1z);

			writeDoubleArray(buf, effect.p2x);
			writeDoubleArray(buf, effect.p2y);
			writeDoubleArray(buf, effect.p2z);

			writeDoubleArray(buf, effect.p3x);
			writeDoubleArray(buf, effect.p3y);
			writeDoubleArray(buf, effect.p3z);

			writeDoubleArray(buf, effect.p4x);
			writeDoubleArray(buf, effect.p4y);
			writeDoubleArray(buf, effect.p4z);

			writeIntArray(buf, effect.l1);
			writeIntArray(buf, effect.l2);
			writeIntArray(buf, effect.l3);
			writeIntArray(buf, effect.l4);

			writeIntArray(buf, effect.c1);
			writeIntArray(buf, effect.c2);
			writeIntArray(buf, effect.c3);
			writeIntArray(buf, effect.c4);

			buf.writeUtf(effect.texture);
		}

		@Override
		public @NotNull Vertex4Effect decode(RegistryFriendlyByteBuf buf) {
			return new Vertex4Effect(
				buf.readVarInt(),
				readDoubleArray(buf), readDoubleArray(buf), readDoubleArray(buf),
				readDoubleArray(buf), readDoubleArray(buf), readDoubleArray(buf),
				readDoubleArray(buf), readDoubleArray(buf), readDoubleArray(buf),
				readDoubleArray(buf), readDoubleArray(buf), readDoubleArray(buf),
				readIntArray(buf), readIntArray(buf), readIntArray(buf), readIntArray(buf),
				readIntArray(buf), readIntArray(buf), readIntArray(buf), readIntArray(buf),
				buf.readUtf()
			);
		}
	};

	public static final MapCodec<Vertex4Effect> MAP_CODEC = new MapCodec<Vertex4Effect>() {
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
		public <T> DataResult<Vertex4Effect> decode(DynamicOps<T> ops, MapLike<T> input) {
			var age = Codec.INT.fieldOf(("age")).decode(ops, input);
			var p1x = Codec.list(Codec.DOUBLE).fieldOf("p1x").orElse(List.of(.0)).decode(ops, input);
			var p1y = Codec.list(Codec.DOUBLE).fieldOf("p1y").orElse(List.of(.0)).decode(ops, input);
			var p1z = Codec.list(Codec.DOUBLE).fieldOf("p1z").orElse(List.of(.0)).decode(ops, input);
			var p2x = Codec.list(Codec.DOUBLE).fieldOf("p2x").orElse(List.of(.0)).decode(ops, input);
			var p2y = Codec.list(Codec.DOUBLE).fieldOf("p2y").orElse(List.of(.0)).decode(ops, input);
			var p2z = Codec.list(Codec.DOUBLE).fieldOf("p2z").orElse(List.of(.0)).decode(ops, input);
			var p3x = Codec.list(Codec.DOUBLE).fieldOf("p3x").orElse(List.of(.0)).decode(ops, input);
			var p3y = Codec.list(Codec.DOUBLE).fieldOf("p3y").orElse(List.of(.0)).decode(ops, input);
			var p3z = Codec.list(Codec.DOUBLE).fieldOf("p3z").orElse(List.of(.0)).decode(ops, input);
			var p4x = Codec.list(Codec.DOUBLE).fieldOf("p4x").orElse(List.of(.0)).decode(ops, input);
			var p4y = Codec.list(Codec.DOUBLE).fieldOf("p4y").orElse(List.of(.0)).decode(ops, input);
			var p4z = Codec.list(Codec.DOUBLE).fieldOf("p4z").orElse(List.of(.0)).decode(ops, input);
			var l1 = Codec.list(Codec.INT).fieldOf("l1").orElse(List.of(15728880)).decode(ops, input);
			var l2 = Codec.list(Codec.INT).fieldOf("l2").orElse(List.of(15728880)).decode(ops, input);
			var l3 = Codec.list(Codec.INT).fieldOf("l3").orElse(List.of(15728880)).decode(ops, input);
			var l4 = Codec.list(Codec.INT).fieldOf("l4").orElse(List.of(15728880)).decode(ops, input);
			var c1 = Codec.list(Codec.INT).fieldOf("c1").orElse(List.of(16777215)).decode(ops, input);
			var c2 = Codec.list(Codec.INT).fieldOf("c2").orElse(List.of(16777215)).decode(ops, input);
			var c3 = Codec.list(Codec.INT).fieldOf("c3").orElse(List.of(16777215)).decode(ops, input);
			var c4 = Codec.list(Codec.INT).fieldOf("c4").orElse(List.of(16777215)).decode(ops, input);
			var texture = Codec.STRING.fieldOf("texture").orElse("").decode(ops, input);
			// 全部有值则构造对象
			try {
				var obj = new Vertex4Effect(
					age.getOrThrow(),
					new ArrayList<>(p1x.getOrThrow()), new ArrayList<>(p1y.getOrThrow()), new ArrayList<>(p1z.getOrThrow()),
					new ArrayList<>(p2x.getOrThrow()), new ArrayList<>(p2y.getOrThrow()), new ArrayList<>(p2z.getOrThrow()),
					new ArrayList<>(p3x.getOrThrow()), new ArrayList<>(p3y.getOrThrow()), new ArrayList<>(p3z.getOrThrow()),
					new ArrayList<>(p4x.getOrThrow()), new ArrayList<>(p4y.getOrThrow()), new ArrayList<>(p4z.getOrThrow()),
					new ArrayList<>(l1.getOrThrow()), new ArrayList<>(l2.getOrThrow()), new ArrayList<>(l3.getOrThrow()), new ArrayList<>(l4.getOrThrow()),
					new ArrayList<>(c1.getOrThrow()), new ArrayList<>(c2.getOrThrow()), new ArrayList<>(c3.getOrThrow()), new ArrayList<>(c4.getOrThrow()),
					texture.getOrThrow()
				);
				return DataResult.success(obj);
			} catch (Exception e) {
				return DataResult.error(e::getMessage);
			}
		}

		@Override
		public <T> RecordBuilder<T> encode(Vertex4Effect vertex, DynamicOps<T> ops, RecordBuilder<T> prefix) {
			return prefix
				// 基本类型
				.add("age", vertex.age, Codec.INT)

				// 坐标列表（Double 列表）
				.add("p1x", vertex.p1x, Codec.DOUBLE.listOf())
				.add("p1y", vertex.p1y, Codec.DOUBLE.listOf())
				.add("p1z", vertex.p1z, Codec.DOUBLE.listOf())

				.add("p2x", vertex.p2x, Codec.DOUBLE.listOf())
				.add("p2y", vertex.p2y, Codec.DOUBLE.listOf())
				.add("p2z", vertex.p2z, Codec.DOUBLE.listOf())

				.add("p3x", vertex.p3x, Codec.DOUBLE.listOf())
				.add("p3y", vertex.p3y, Codec.DOUBLE.listOf())
				.add("p3z", vertex.p3z, Codec.DOUBLE.listOf())

				.add("p4x", vertex.p4x, Codec.DOUBLE.listOf())
				.add("p4y", vertex.p4y, Codec.DOUBLE.listOf())
				.add("p4z", vertex.p4z, Codec.DOUBLE.listOf())

				// 光照（Integer 列表）
				.add("l1", vertex.l1, Codec.INT.listOf())
				.add("l2", vertex.l2, Codec.INT.listOf())
				.add("l3", vertex.l3, Codec.INT.listOf())
				.add("l4", vertex.l4, Codec.INT.listOf())

				// 颜色（Integer 列表）
				.add("c1", vertex.c1, Codec.INT.listOf())
				.add("c2", vertex.c2, Codec.INT.listOf())
				.add("c3", vertex.c3, Codec.INT.listOf())
				.add("c4", vertex.c4, Codec.INT.listOf())

				// 材质
				.add("texture", vertex.texture, Codec.STRING);

		}
	};

	@Override
	public ParticleType<?> getType() {
		return type;
	}

}
