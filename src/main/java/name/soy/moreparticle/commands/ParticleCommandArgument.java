package name.soy.moreparticle.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;

import java.io.Serializable;

@RequiredArgsConstructor
public class ParticleCommandArgument<T extends Serializable> implements ArgumentType<T> {
	public static final DynamicCommandExceptionType ERROR_UNKNOWN_COMMAND = new DynamicCommandExceptionType(object -> Component.translatableEscape("particle.notFound", object));
	final Codec<T> codec;
	@Override
	public T parse(StringReader stringReader) throws CommandSyntaxException {
		CompoundTag compoundTag = stringReader.canRead() && stringReader.peek() == '{' ? new TagParser(stringReader).readStruct() : new CompoundTag();
		return codec.parse(NbtOps.INSTANCE, compoundTag).getOrThrow(ERROR_UNKNOWN_COMMAND::create);
	}
}
