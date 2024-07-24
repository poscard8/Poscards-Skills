package github.poscard8.poscardsskills.datagen;

import com.mojang.datafixers.util.Pair;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PSLootTableProvider extends LootTableProvider {

    private final Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet> subProvider = Pair.of(SubProvider::new, LootContextParamSets.BLOCK);

    public PSLootTableProvider(DataGenerator gen) { super(gen); }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() { return List.of(subProvider); }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {}


    public static class SubProvider extends BlockLoot {

        @Override
        protected void addTables() { BlockWrapper.forEach(wrapper -> wrapper.addLootTable(this)); }

        @Override
        protected Iterable<Block> getKnownBlocks() { return BlockWrapper.VALUES.stream().map(BlockWrapper::get).collect(Collectors.toSet()); }

        public void slab(Block block) { add(block, createSlabItemTable(block)); }

    }
}
