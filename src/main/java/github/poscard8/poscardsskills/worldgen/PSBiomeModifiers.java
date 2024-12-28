package github.poscard8.poscardsskills.worldgen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.PSTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Biome modifier register.
 */
public class PSBiomeModifiers {

    public static final ResourceKey<BiomeModifier>

            ORE_JADE = register("ore_jade"),
            ORE_JASPER = register("ore_jasper"),
            ORE_MARBLE = register("ore_marble");

    public static void register(BootstapContext<BiomeModifier> ctx) {

        HolderGetter<Biome> biomeGetter = ctx.lookup(ForgeRegistries.Keys.BIOMES);
        HolderGetter<PlacedFeature> placedFeatureGetter = ctx.lookup(Registries.PLACED_FEATURE);

        ctx.register(ORE_JADE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(PSTags.Biomes.GENERATES_JADE),
                HolderSet.direct(placedFeatureGetter.getOrThrow(PSPlacedFeatures.ORE_JADE)),
                GenerationStep.Decoration.UNDERGROUND_ORES)
        );
        ctx.register(ORE_JASPER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(PSTags.Biomes.GENERATES_JASPER),
                HolderSet.direct(placedFeatureGetter.getOrThrow(PSPlacedFeatures.ORE_JASPER)),
                GenerationStep.Decoration.UNDERGROUND_ORES)
        );
        ctx.register(ORE_MARBLE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(PSTags.Biomes.GENERATES_MARBLE),
                HolderSet.direct(placedFeatureGetter.getOrThrow(PSPlacedFeatures.ORE_MARBLE)),
                GenerationStep.Decoration.UNDERGROUND_ORES)
        );

    }

    static ResourceKey<BiomeModifier> register(String name) { return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, PoscardsSkills.asResource(name)); }

}
