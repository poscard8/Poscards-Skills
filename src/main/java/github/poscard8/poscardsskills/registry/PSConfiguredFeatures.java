package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.worldgen.feature.DoubleOreConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PSConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> ALL = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, PoscardsSkills.ID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_JADE = ALL.register("ore_jade",
            () -> new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(OreFeatures.NATURAL_STONE, PSBlocks.ROUGH_JADE.defaultBlockState(), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), 96)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_JASPER = ALL.register("ore_jasper",
            () -> new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(OreFeatures.NATURAL_STONE, PSBlocks.ROUGH_JASPER.defaultBlockState(), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), 96)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_MARBLE = ALL.register("ore_marble",
            () -> new ConfiguredFeature<>(PSFeatures.DOUBLE_ORE.get(), new DoubleOreConfiguration(OreFeatures.NATURAL_STONE, PSBlocks.ROUGH_MARBLE.defaultBlockState(), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), 96)));


    public static void register(IEventBus bus) { ALL.register(bus); }

}
