package github.poscard8.poscardsskills.worldgen;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

/**
 * Feature placement register.
 */
public class PSPlacedFeatures
{
    public static final ResourceKey<PlacedFeature>

            ORE_JADE = register("ore_jade"),
            ORE_JASPER = register("ore_jasper"),
            ORE_MARBLE = register("ore_marble");

    public static void register(BootstapContext<PlacedFeature> ctx)
    {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureGetter = ctx.lookup(Registries.CONFIGURED_FEATURE);

        ctx.register(ORE_JADE, new PlacedFeature(configuredFeatureGetter.getOrThrow(PSConfiguredFeatures.ORE_JADE), placement()));
        ctx.register(ORE_JASPER, new PlacedFeature(configuredFeatureGetter.getOrThrow(PSConfiguredFeatures.ORE_JASPER), placement()));
        ctx.register(ORE_MARBLE, new PlacedFeature(configuredFeatureGetter.getOrThrow(PSConfiguredFeatures.ORE_MARBLE), placement()));

    }

    static List<PlacementModifier> placement()
    {
        PlacementModifier rarity = RarityFilter.onAverageOnceEvery(2);
        PlacementModifier spread = InSquarePlacement.spread();
        PlacementModifier height = HeightRangePlacement.uniform(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56));
        PlacementModifier biome = BiomeFilter.biome();

        return List.of(rarity, spread, height, biome);
    }

    static ResourceKey<PlacedFeature> register(String name) { return ResourceKey.create(Registries.PLACED_FEATURE, PoscardsSkills.asResource(name)); }


}
