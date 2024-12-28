package github.poscard8.poscardsskills.registry;


import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.worldgen.feature.DoubleOreConfiguration;
import github.poscard8.poscardsskills.worldgen.feature.DoubleOreFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSFeatures {

    public static final DeferredRegister<Feature<?>> ALL = DeferredRegister.create(ForgeRegistries.FEATURES, PoscardsSkills.ID);

    public static final RegistryObject<Feature<DoubleOreConfiguration>> DOUBLE_ORE = ALL.register("double_ore", () -> new DoubleOreFeature(DoubleOreConfiguration.CODEC));

    public static void register(IEventBus bus) { ALL.register(bus); }

}