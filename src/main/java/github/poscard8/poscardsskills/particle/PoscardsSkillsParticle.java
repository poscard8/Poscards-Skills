package github.poscard8.poscardsskills.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Custom particle type. Some of the code is taken from {@link GlowParticle}.
 */
@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class PoscardsSkillsParticle extends TextureSheetParticle {

    public final SpriteSet spriteSet;

    public PoscardsSkillsParticle(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet) {

        super(clientLevel, x, y, z, xd, yd, zd);
        this.spriteSet = spriteSet;
        this.friction = 0.96F;
        this.quadSize *= 0.9F;
        this.hasPhysics = false;
        setSpriteFromAge(spriteSet);
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }

    @Override
    protected int getLightColor(float v) { return 15 << 20 | super.getLightColor(v); }

    @Override
    public void tick() {

        super.tick();
        setSpriteFromAge(spriteSet);
    }

    /**
     * Has only 1 color array (4 colors) and high speed.
     */
    @OnlyIn(Dist.CLIENT)
    public static class LevelUpProvider implements ParticleProvider<SimpleParticleType> {

        protected final float[] rgbArray = new float[]{1F, 0.667F, 0F, 0.333F, 0.333F, 1F, 0.333F, 1F, 0.333F, 0.333F, 1F, 1F};

        protected final SpriteSet spriteSet;

        public LevelUpProvider(SpriteSet spriteSet) { this.spriteSet = spriteSet; }

        @Override
        @Nullable
        public PoscardsSkillsParticle createParticle(SimpleParticleType particleType, ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd) {

            PoscardsSkillsParticle particle = new PoscardsSkillsParticle(clientLevel, x, y, z, xd, yd, zd, spriteSet);
            int colorIndex = new Random().nextInt(4);

            particle.setColor(getColor(colorIndex, 0), getColor(colorIndex, 1), getColor(colorIndex, 2));
            particle.setParticleSpeed(xd * 0.6D, yd * 0.6D, zd * 0.6D);
            particle.setLifetime(getLifetime());

            return particle;
        }

        public float getColor(int colorIndex, int relative) { return rgbArray[3 * colorIndex + relative]; }

        public int getLifetime() { return 5 + new Random().nextInt(10); }

    }

    /**
     * Has 3 color arrays (brilliant, blessed, divine) and slow speed.
     * Used by catalyst items.
     */
    @OnlyIn(Dist.CLIENT)
    public static class AscensionProvider implements ParticleProvider<SimpleParticleType> {

        protected final float[] rgbArray;
        protected final SpriteSet spriteSet;

        public AscensionProvider(float[] rgbArray, SpriteSet spriteSet) {

            this.rgbArray = rgbArray;
            this.spriteSet = spriteSet;
        }

        public static AscensionProvider brilliant(SpriteSet spriteSet) {

            float[] rgbArray = new float[]{1F, 1F, 1F, 0.25F, 0.25F, 0.25F, 0.271F, 0.914F, 1F, 0.871F, 0.173F, 0.278F};
            return new AscensionProvider(rgbArray, spriteSet);
        }

        public static AscensionProvider blessed(SpriteSet spriteSet) {

            float[] rgbArray = new float[]{0.686F, 0.38F, 0.91F, 0.827F, 0.275F, 0F, 0.859F, 0.784F, 0.239F, 0.333F, 0.757F, 0.278F};
            return new AscensionProvider(rgbArray, spriteSet);
        }

        public static AscensionProvider divine(SpriteSet spriteSet) {

            float[] rgbArray = new float[]{0.271F, 0.914F, 1F, 0.333F, 0.776F, 0.443F, 0.259F, 0F, 0.627F, 0.012F, 0.075F, 0.467F};
            return new AscensionProvider(rgbArray, spriteSet);
        }

        @Override
        @Nullable
        public PoscardsSkillsParticle createParticle(SimpleParticleType particleType, ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd) {

            PoscardsSkillsParticle particle = new PoscardsSkillsParticle(clientLevel, x, y, z, xd, yd, zd, spriteSet);
            int colorIndex = new Random().nextInt(4);

            particle.setColor(getColor(colorIndex, 0), getColor(colorIndex, 1), getColor(colorIndex, 2));
            particle.setParticleSpeed(xd * 0.25D, yd * 0.25D, zd * 0.25D);
            particle.setLifetime(getLifetime());

            return particle;
        }

        public float getColor(int colorIndex, int relative) { return rgbArray[3 * colorIndex + relative]; }

        public int getLifetime() { return 30 + new Random().nextInt(40); }

    }


}
