package name.soy.moreparticle.calc;

import lombok.RequiredArgsConstructor;
import name.soy.moreparticle.utils.CustomFunctions;
import name.soy.moreparticle.utils.CustomOperators;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Random;
@Environment(EnvType.CLIENT)
public class CalcParticle extends SimpleAnimatedParticle {

    public static void register() {
        ParticleFactoryRegistry.getInstance().register(CalcEffect.type, Factory::new);
    }

    public Expression xfun, yfun, zfun, cfun;
    public double lx = 0, ly = 0, lz = 0;

    protected CalcParticle(ClientLevel world, double x, double y, double z,
                           SpriteSet spriteProvider,
                           String xfun, String yfun, String zfun,
                           int age, int random, String cfun) {
        super(world, x, y, z, spriteProvider, 0.005f);
        this.xfun = new ExpressionBuilder(xfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();
        this.yfun = new ExpressionBuilder(yfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();
        this.zfun = new ExpressionBuilder(zfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();
        this.cfun = new ExpressionBuilder(cfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();

        this.hasPhysics = false;
        this.setColor((int) this.cfun.setVariable("t", 0).evaluate());
        this.lifetime = new Random().nextInt(random) + age;
        this.setSpriteFromAge(spriteProvider);

    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
            double cx, cy, cz;
//			try {
//				this.setColor(fc.get().intValue());
//				cx = fx.get();
//				cy = fy.get();
//				cz = fz.get();
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
            try {
                this.setColor((int) cfun.setVariable("t", this.age).evaluate());
                cx = xfun.setVariable("t", this.age).evaluate();
                cy = yfun.setVariable("t", this.age).evaluate();
                cz = zfun.setVariable("t", this.age).evaluate();
            } catch (ArithmeticException e) {
                return;
            }

            xd = cx - lx;
            yd = cy - ly;
            zd = cz - lz;
            this.move(this.xd, this.yd, this.zd);
            lx = cx;
            ly = cy;
            lz = cz;
//			fc = this.cfun.setVariable("t", this.age + 1).evaluateAsync(exec);
//			fx = this.xfun.setVariable("t", this.age + 1).evaluateAsync(exec);
//			fy = this.yfun.setVariable("t", this.age + 1).evaluateAsync(exec);
//			fz = this.zfun.setVariable("t", this.age + 1).evaluateAsync(exec);
        }
    }

    @RequiredArgsConstructor
    public static class Factory implements ParticleProvider<CalcEffect> {
        private final SpriteSet spriteProvider;

        @Override
        public Particle createParticle(CalcEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new CalcParticle(world, x, y, z, spriteProvider, parameters.xfun, parameters.yfun, parameters.zfun, parameters.age, parameters.random, parameters.cfun);
        }
    }
}
