package github.poscard8.poscardsskills.util.wrapper;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.block.LayeredBlock;
import github.poscard8.poscardsskills.block.PillarBlock;
import github.poscard8.poscardsskills.block.ShinyBlock;
import github.poscard8.poscardsskills.datagen.PSLootTableProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link #nameFormat} and {@link #textureFormat} define the id/texture name of the block
 * depending on the {@link BlockSet}.
 * Functions define how JSON files will be generated.
 */
public enum BlockType {

    BRICKS("%s_bricks", "%s_bricks", BlockType::blockRegister, BlockType::blockModel),
    BRICK_STAIRS("%s_brick_stairs", "%s_bricks", BlockType::stairsRegister, BlockType::stairModel),
    BRICK_SLAB("%s_brick_slab", "%s_bricks", BlockType::slabRegister, BlockType::slabModel, BlockType::blockItemModel, BlockType::slabLoot),
    BRICK_WALL("%s_brick_wall", "%s_bricks", BlockType::wallRegister, BlockType::wallModel, BlockType::wallItemModel, BlockType::blockLoot),
    CHISELED("chiseled_%s", "chiseled_%s", BlockType::blockRegister, BlockType::chiseledModel),
    PILLAR("%s_pillar", "%s_pillar", BlockType::pillarRegister, BlockType::pillarModel),
    LAYERED("layered_%s", "layered_%s", BlockType::layeredRegister, BlockType::layeredModel, BlockType::layeredItemModel, BlockType::blockLoot),
    SHINY("shiny_%s", "shiny_%s", BlockType::shinyRegister, BlockType::shinyModel),
    ROUGH("rough_%s", "rough_%s", BlockType::blockRegister, BlockType::roughModel);

    final String nameFormat;
    final String textureFormat;

    final Function<BlockSet, Supplier<Block>> registerFunction;
    final BiConsumer<BlockStateProvider, BlockWrapper> blockModelFunction;
    final BiConsumer<ItemModelProvider, BlockWrapper> itemModelFunction;
    final BiConsumer<BlockLootSubProvider, BlockWrapper> lootTableFunction;

    BlockType(String nameFormat, String textureFormat, Function<BlockSet, Supplier<Block>> registerFunction, BiConsumer<BlockStateProvider, BlockWrapper> modelFunction) {

        this(nameFormat, textureFormat, registerFunction, modelFunction, BlockType::blockItemModel, BlockType::blockLoot);
    }

    BlockType(String nameFormat, String textureFormat, Function<BlockSet, Supplier<Block>> registerFunction,
              BiConsumer<BlockStateProvider, BlockWrapper> blockModelFunction, BiConsumer<ItemModelProvider, BlockWrapper> itemModelFunction, BiConsumer<BlockLootSubProvider, BlockWrapper> lootTableFunction) {

        this.nameFormat = nameFormat;
        this.textureFormat = textureFormat;
        this.registerFunction = registerFunction;
        this.blockModelFunction = blockModelFunction;
        this.itemModelFunction = itemModelFunction;
        this.lootTableFunction = lootTableFunction;
    }

    private static Supplier<Block> blockRegister(BlockSet blockSet) { return () -> new Block(blockSet.getProperties()); }

    @SuppressWarnings("deprecation")
    static Supplier<Block> stairsRegister(BlockSet blockSet) { return () -> new StairBlock(Blocks.STONE.defaultBlockState(), blockSet.getProperties()); }

    static Supplier<Block> slabRegister(BlockSet blockSet) { return () -> new SlabBlock(blockSet.getProperties()); }

    static Supplier<Block> wallRegister(BlockSet blockSet) { return () -> new WallBlock(blockSet.getProperties()); }

    static Supplier<Block> pillarRegister(BlockSet blockSet) { return () -> new PillarBlock(blockSet.getProperties()); }

    static Supplier<Block> layeredRegister(BlockSet blockSet) { return () -> new LayeredBlock(blockSet.getProperties()); }

    static Supplier<Block> shinyRegister(BlockSet blockSet) { return () -> new ShinyBlock(blockSet.getProperties()); }

    static void blockModel(BlockStateProvider provider, BlockWrapper wrapper) { provider.simpleBlock(wrapper.get()); }

    static void stairModel(BlockStateProvider provider, BlockWrapper wrapper) { provider.stairsBlock((StairBlock) wrapper.get(), wrapper.textureLocation()); }

    static void slabModel(BlockStateProvider provider, BlockWrapper wrapper) { provider.slabBlock((SlabBlock) wrapper.get(), wrapper.textureLocation(), wrapper.textureLocation()); }

    static void wallModel(BlockStateProvider provider, BlockWrapper wrapper) { provider.wallBlock((WallBlock) wrapper.get(), wrapper.textureLocation()); }

    static void chiseledModel(BlockStateProvider provider, BlockWrapper wrapper) {

        ModelFile modelFile = provider.models().withExistingParent(wrapper.path(), "block/cube_column").texture("side", wrapper.textureLocation()).texture("end", wrapper.textureLocation("_top"));
        ConfiguredModel configuredModel = ConfiguredModel.builder().modelFile(modelFile).buildLast();

        provider.getVariantBuilder(wrapper.get()).partialState().addModels(configuredModel);
    }

    static void pillarModel(BlockStateProvider provider, BlockWrapper wrapper) {

        ModelFile modelFile = provider.models().withExistingParent(wrapper.path(), PoscardsSkills.asResource("block/pillar_block")).texture("default", wrapper.textureLocation()).texture("top", wrapper.textureLocation("_top")).texture("ends", wrapper.textureLocation("_ends"));

        provider.getVariantBuilder(wrapper.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).addModels(ConfiguredModel.builder().modelFile(modelFile).rotationX(90).rotationY(90).buildLast())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).addModels(ConfiguredModel.builder().modelFile(modelFile).buildLast())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).addModels(ConfiguredModel.builder().modelFile(modelFile).rotationX(90).buildLast());

    }

    static void layeredModel(BlockStateProvider provider, BlockWrapper wrapper) {

        ConfiguredModel configuredModel = ConfiguredModel.builder().modelFile(provider.models().withExistingParent(wrapper.path(), PoscardsSkills.asResource("block/layered_block"))
                .texture("default", wrapper.textureLocation()).texture("middle", wrapper.textureLocation("_middle")).texture("side", wrapper.textureLocation("_side")).texture("inner", wrapper.textureLocation("_inner"))).buildLast();

        provider.getVariantBuilder(wrapper.get()).partialState().addModels(configuredModel);
    }

    static void shinyModel(BlockStateProvider provider, BlockWrapper wrapper) {

        ModelFile modelFile = provider.models().withExistingParent(wrapper.path(), PoscardsSkills.asResource("block/shiny_block")).texture("top", wrapper.textureLocation("_top")).texture("x", wrapper.textureLocation("_0")).texture("z", wrapper.textureLocation("_1"));

        provider.getVariantBuilder(wrapper.get())
                .partialState().with(ShinyBlock.PHASE, 0).addModels(ConfiguredModel.builder().modelFile(modelFile).buildLast())
                .partialState().with(ShinyBlock.PHASE, 1).addModels(ConfiguredModel.builder().modelFile(modelFile).rotationY(90).uvLock(true).buildLast());
    }

    static void roughModel(BlockStateProvider provider, BlockWrapper wrapper) {

        ConfiguredModel[] configuredModels = new ConfiguredModel[16];

        for (int x = 0; x < 4; x++) {

            for (int y = 0; y < 4; y++) configuredModels[x + 4 * y] = ConfiguredModel.builder().modelFile(provider.cubeAll(wrapper.get())).rotationX(x * 90).rotationY(y * 90).buildLast();
        }
        provider.getVariantBuilder(wrapper.get()).partialState().addModels(configuredModels);
    }

    static void blockItemModel(ItemModelProvider provider, BlockWrapper wrapper) { provider.withExistingParent(wrapper.path(), PoscardsSkills.asResource(String.format("block/%s", wrapper.path()))); }

    static void wallItemModel(ItemModelProvider provider, BlockWrapper wrapper) { provider.wallInventory(wrapper.path(), wrapper.textureLocation()); }

    private static void layeredItemModel(ItemModelProvider provider, BlockWrapper wrapper) {

        provider.withExistingParent(wrapper.path(), PoscardsSkills.asResource("item/layered_block"))
                .texture("default", wrapper.textureLocation()).texture("middle", wrapper.textureLocation("_middle")).texture("side", wrapper.textureLocation("_side")).texture("inner", wrapper.textureLocation("_inner"));
    }

    static void blockLoot(BlockLootSubProvider provider, BlockWrapper wrapper) { ((PSLootTableProvider.Sub) provider).dropSelf(wrapper); }

    static void slabLoot(BlockLootSubProvider provider, BlockWrapper wrapper) { ((PSLootTableProvider.Sub) provider).slab(wrapper); }

    public String getName(BlockSet blockSet) { return String.format(nameFormat, blockSet); }

    public String getTexture(BlockSet blockSet) { return String.format(textureFormat, blockSet); }

    public Supplier<Block> getBlock(BlockSet blockSet) { return registerFunction.apply(blockSet); }

}
