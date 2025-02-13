package name.soy.moreparticle.cmd;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public sealed interface CmdCommand {
	void process(CmdParticle particle);
}

/**
 * 设置粒子状态的命令
 */
@RequiredArgsConstructor
@ToString
final class SetCommand implements CmdCommand {
	final ParticleAction action;

	@Override
	public void process(CmdParticle particle) {

	}
}

final class SequenceCommand implements CmdCommand {

	@Override
	public void process(CmdParticle particle) {

	}
}

final class LerpCommand implements CmdCommand {

	@Override
	public void process(CmdParticle particle) {

	}
}