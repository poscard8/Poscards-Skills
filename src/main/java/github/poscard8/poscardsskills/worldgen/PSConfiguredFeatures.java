package github.poscard8.poscardsskills.worldgen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSBlocks;
import github.poscard8.poscardsskills.registry.PSFeatures;
import github.poscard8.poscardsskills.worldgen.feature.DoubleOreConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * Feature register.
 */
public class PSConfiguredFeatures
{
    public static final ResourceKey<ConfiguredFeature<?, ?>>

            ORE_JADE = register("ore_jade"),
            ORE_JASPER = register("ore_jasper"),
            ORE_MARBLE = register("ore_marble");

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> ctx)
    {
        ctx.register(ORE_JADE,   new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(PSBlocks.JADE.defaultBlockState())));
        ctx.register(ORE_JASPER, new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(PSBlocks.JASPER.defaultBlockState())));
        ctx.register(ORE_MARBLE, new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(PSBlocks.MARBLE.defaultBlockState())));

    }

    static ResourceKey<ConfiguredFeature<?, ?>> register(String name) { return ResourceKey.create(Registries.CONFIGURED_FEATURE, PoscardsSkills.asResource(name)); }

}
