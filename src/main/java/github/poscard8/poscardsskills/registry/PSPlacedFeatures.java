package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@SuppressWarnings("unused")
public class PSPlacedFeatures {

    public static final DeferredRegister<PlacedFeature> ALL = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PoscardsSkills.ID);

    public static final RegistryObject<PlacedFeature>

            ORE_JADE_PLACED = ALL.register("ore_jade", () -> new PlacedFeature(PSConfiguredFeatures.ORE_JADE.getHolder().orElseThrow(), psOrePlacement())),
            ORE_JASPER_PLACED = ALL.register("ore_jasper", () -> new PlacedFeature(PSConfiguredFeatures.ORE_JASPER.getHolder().orElseThrow(), psOrePlacement())),
            ORE_MARBLE_PLACED = ALL.register("ore_marble", () -> new PlacedFeature(PSConfiguredFeatures.ORE_MARBLE.getHolder().orElseThrow(), psOrePlacement()));

    static List<PlacementModifier> psOrePlacement() {

        PlacementModifier count = RarityFilter.onAverageOnceEvery(3);
        PlacementModifier spread = InSquarePlacement.spread();
        PlacementModifier height = HeightRangePlacement.uniform(VerticalAnchor.absolute(8), VerticalAnchor.absolute(56));
        PlacementModifier biome = BiomeFilter.biome();

        return List.of(count, spread, height, biome);
    }

    public static void register(IEventBus bus) { ALL.register(bus); }

}
