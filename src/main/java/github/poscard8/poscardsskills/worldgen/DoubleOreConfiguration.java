package github.poscard8.poscardsskills.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class DoubleOreConfiguration implements FeatureConfiguration {

    @SuppressWarnings("ALL")
    public static final Codec<DoubleOreConfiguration> CODEC = RecordCodecBuilder.create(instance -> {

        return instance.group(
                Codec.of(OreConfiguration.TargetBlockState.CODEC, OreConfiguration.TargetBlockState.CODEC).fieldOf("inner").forGetter(configuration -> configuration.inner),
                Codec.of(OreConfiguration.TargetBlockState.CODEC, OreConfiguration.TargetBlockState.CODEC).fieldOf("outer").forGetter(configuration -> configuration.outer),
                Codec.intRange(0, 256).fieldOf("size").forGetter(configuration -> configuration.size)).apply(instance, DoubleOreConfiguration::new);
    });

    public final OreConfiguration.TargetBlockState inner;
    public final OreConfiguration.TargetBlockState outer;
    public final int size;

    public DoubleOreConfiguration(RuleTest ruleTest, BlockState inner, BlockState outer, int size) { this(OreConfiguration.target(ruleTest, inner), OreConfiguration.target(ruleTest, outer), size); }

    public DoubleOreConfiguration(OreConfiguration.TargetBlockState innerTargetState, OreConfiguration.TargetBlockState outerTargetState, int size) {

        this.inner = innerTargetState;
        this.outer = outerTargetState;
        this.size = size;
    }

}
