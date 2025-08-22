package github.poscard8.poscardsskills.datagen;

import github.poscard8.poscardsskills.util.block.BlockType;
import github.poscard8.poscardsskills.util.block.BlockWrapper;
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

    public PSLootTableProvider(PackOutput packOutput) { super(packOutput, Set.of(), List.of(new SubProviderEntry(SubProvider::new, LootContextParamSets.BLOCK))); }

    public static class SubProvider extends BlockLootSubProvider {

        protected SubProvider()
        {
            super(BlockWrapper.VALUES.stream().filter(wrapper -> wrapper.blockType != BlockType.DEFAULT).map(BlockWrapper::asItem).collect(Collectors.toSet()), FeatureFlags.DEFAULT_FLAGS);
        }

        @Override
        @NotNull
        protected Iterable<Block> getKnownBlocks()
        {
            return BlockWrapper.VALUES.stream()
                    .filter(wrapper -> wrapper.blockType != BlockType.DEFAULT)
                    .map(BlockWrapper::get)
                    .collect(Collectors.toSet());
        }

        @Override
        protected void generate()
        {
            BlockWrapper.VALUES.stream()
                    .filter(wrapper -> wrapper.blockType != BlockType.DEFAULT)
                    .forEach(wrapper -> wrapper.addLootTable(this));
        }

        public void dropSelf(BlockWrapper wrapper) { dropSelf(wrapper.get()); }

        public void slab(BlockWrapper wrapper) { this.add(wrapper.get(), createSlabItemTable(wrapper.get())); }

    }

}
