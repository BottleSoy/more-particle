package name.soy.moreparticle.calc;

import lombok.RequiredArgsConstructor;
import name.soy.moreparticle.utils.CustomFunctions;
import name.soy.moreparticle.utils.CustomOperators;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Random;

public class BestParticle extends AnimatedParticle {

    public static void register() {
        ParticleFactoryRegistry.getInstance().register(BestEffect.type, Factory::new);
    }

    public Expression xfun, yfun, zfun, cfun;
    public double lx = 0, ly = 0, lz = 0;

    protected BestParticle(ClientWorld world, double x, double y, double z,
                           SpriteProvider spriteProvider,
                           String xfun, String yfun, String zfun,
                           int age, int random, String cfun) {
        super(world, x, y, z, spriteProvider, 0.005f);
        this.xfun = new ExpressionBuilder(xfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();
        this.yfun = new ExpressionBuilder(yfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();
        this.zfun = new ExpressionBuilder(zfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();
        this.cfun = new ExpressionBuilder(cfun).operator(CustomOperators.operators).functions(CustomFunctions.funcs).variables("t").build();
        this.collidesWithWorld = false;
        this.setColor((int) this.cfun.setVariable("t", 0).evaluate());
        this.maxAge = new Random().nextInt(random) + age;
        this.setSpriteForAge(spriteProvider);

    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
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

            velocityX = cx - lx;
            velocityY = cy - ly;
            velocityZ = cz - lz;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
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
    public static class Factory implements ParticleFactory<BestEffect> {
        private final SpriteProvider spriteProvider;

        @Override
        public Particle createParticle(BestEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new BestParticle(world, x, y, z, spriteProvider, parameters.xfun, parameters.yfun, parameters.zfun, parameters.age, parameters.random, parameters.cfun);
        }
    }
}
