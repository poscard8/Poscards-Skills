package github.poscard8.poscardsskills.worldgen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSBlocks;
import github.poscard8.poscardsskills.registry.PSFeatures;
import github.poscard8.poscardsskills.worldgen.feature.DoubleOreConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

/**
 * Feature register.
 */
public class PSConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>>

            ORE_JADE = register("ore_jade"),
            ORE_JASPER = register("ore_jasper"),
            ORE_MARBLE = register("ore_marble");

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> ctx) {

        ctx.register(ORE_JADE, new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), PSBlocks.ROUGH_JADE.defaultBlockState(), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), 96)));
        ctx.register(ORE_JASPER, new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), PSBlocks.ROUGH_JASPER.defaultBlockState(), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), 96)));
        ctx.register(ORE_MARBLE, new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), PSBlocks.ROUGH_MARBLE.defaultBlockState(), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), 96)));

    }

    static ResourceKey<ConfiguredFeature<?, ?>> register(String name) { return ResourceKey.create(Registries.CONFIGURED_FEATURE, PoscardsSkills.asResource(name)); }

}
