package github.poscard8.poscardsskills.datagen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.worldgen.PSBiomeModifiers;
import github.poscard8.poscardsskills.worldgen.PSConfiguredFeatures;
import github.poscard8.poscardsskills.worldgen.PSPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Worldgen file generator. See {@code "github/poscard8/poscardsskills/worldgen"} for more info.
 */
public class PSWorldGenProvider extends DatapackBuiltinEntriesProvider {

    protected static final RegistrySetBuilder SET_BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, PSConfiguredFeatures::register)
            .add(Registries.PLACED_FEATURE, PSPlacedFeatures::register)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, PSBiomeModifiers::register);

    public PSWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {

        super(output, registries, SET_BUILDER, Set.of(PoscardsSkills.ID));
    }

}
