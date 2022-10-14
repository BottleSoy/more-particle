package name.soy.moreparticle.utils;

import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.util.ArrayList;
import java.util.List;

public class CustomOperators {
	public static final List<Operator> operators = new ArrayList<>();

	static {
		operators.add(new Operator(">", 2, true, 100000) {
			@Override
			public double apply(double... args) {
				return args[0] > args[1] ? 1 : 0;
			}
		});
		operators.add(new Operator("<", 2, true, 100000) {
			@Override
			public double apply(double... args) {
				return args[0] < args[1] ? 1 : 0;
			}
		});
		operators.add(new Operator("!", 1, false, 100000) {
			@Override
			public double apply(double... args) {
				return args[0] == 0 ? 1 : 0;
			}
		});
	}
}
