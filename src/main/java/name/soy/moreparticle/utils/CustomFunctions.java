package name.soy.moreparticle.utils;

import javafx.scene.paint.Color;
import net.objecthunter.exp4j.function.Function;

import java.util.ArrayList;
import java.util.List;

public class CustomFunctions {
    public static final List<Function> funcs = new ArrayList<>();

    static {
        funcs.add(new Function("clamp", 3) {
            @Override
            public double apply(double... args) {
                return Math.min(Math.max(args[0], args[1]), args[2]);
            }
        });
        funcs.add(new Function("min", 2) {
            @Override
            public double apply(double... args) {
                return Math.min(args[0], args[1]);
            }
        });
        funcs.add(new Function("max", 2) {
            @Override
            public double apply(double... args) {
                return Math.max(args[0], args[1]);
            }
        });
        funcs.add(new Function("if", 3) {
            @Override
            public double apply(double... args) {
                return args[0] != 0 ? args[1] : args[2];
            }
        });
        funcs.add(new Function("deg") {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(args[0]);
            }
        });
        funcs.add(new Function("radian") {
            @Override
            public double apply(double... args) {
                return Math.toRadians(args[0]);
            }
        });
        funcs.add(new Function("and", 2) {
            @Override
            public double apply(double... args) {
                return args[0] != 0 && args[1] != 0 ? 1 : 0;
            }
        });
        funcs.add(new Function("not", 1) {
            @Override
            public double apply(double... args) {
                return args[0] != 0 ? 0 : 1;
            }
        });
        funcs.add(new Function("or", 2) {
            @Override
            public double apply(double... args) {
                return args[0] != 0 || args[1] != 0 ? 1 : 0;
            }
        });
        funcs.add(new Function("rgb", 3) {
            @Override
            public double apply(double... args) {
                return Math.round(args[0]) * 65536 + Math.round(args[1]) * 256 + Math.round(args[2]);
            }
        });

        funcs.add(new Function("hsb", 3) {
            @Override
            public double apply(double... args) {
                Color color = Color.hsb(args[0], args[1], args[2]);
                return Math.round(color.getRed() * 255) * 65536 + Math.round(color.getGreen() * 255) * 256 + Math.round(color.getBlue() * 255);
            }
        });
    }
}
