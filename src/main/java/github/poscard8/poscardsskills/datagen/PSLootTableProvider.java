package github.poscard8.poscardsskills.datagen;

import github.poscard8.poscardsskills.util.wrapper.BlockType;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loot table file generator. See {@link BlockWrapper} and {@link BlockType} for more info.
 */
public class PSLootTableProvider extends LootTableProvider {

    public PSLootTableProvider(PackOutput packOutput) { super(packOutput, Set.of(), List.of(new SubProviderEntry(Sub::new, LootContextParamSets.BLOCK))); }

    public static class Sub extends BlockLootSubProvider {

        protected Sub() { super(BlockWrapper.VALUES.stream().map(BlockWrapper::asItem).collect(Collectors.toSet()), FeatureFlags.DEFAULT_FLAGS); }

        @Override
        @NotNull
        protected Iterable<Block> getKnownBlocks() { return BlockWrapper.VALUES.stream().map(BlockWrapper::get).collect(Collectors.toSet()); }

        @Override
        protected void generate() { BlockWrapper.forEach(wrapper -> wrapper.addLootTable(this)); }

        public void dropSelf(BlockWrapper wrapper) { dropSelf(wrapper.get()); }

        public void slab(BlockWrapper wrapper) { this.add(wrapper.get(), createSlabItemTable(wrapper.get())); }

    }

}
