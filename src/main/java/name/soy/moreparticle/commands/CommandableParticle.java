package name.soy.moreparticle.commands;

public interface CommandableParticle<CommandData> {
	void apply(CommandData data);
	default boolean tickOnCommand(){
		return false;
	}
}
