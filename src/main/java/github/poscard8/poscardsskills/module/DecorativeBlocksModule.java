package github.poscard8.poscardsskills.module;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.wrapper.BlockSet;
import github.poscard8.poscardsskills.util.wrapper.BlockType;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public final class DecorativeBlocksModule extends Module {

    public static final String CONFIG_KEY = "decorativeBlocksModule";


    DecorativeBlocksModule() { super(CONFIG_KEY); }

    @Override
    protected void whenPresent(IEventBus bus) {

        BlockWrapper.DECORATIVE_BLOCK_REGISTRY.register(bus);
        BlockWrapper.DECORATIVE_ITEM_REGISTRY.register(bus);

        Blocks.IGNORED.register(bus);
    }

    @Override
    protected void whenAbsent(IEventBus bus) {

        clearAllEntries(ForgeRegistries.BLOCKS, BlockWrapper.DECORATIVE_BLOCK_REGISTRY);
        clearAllEntries(ForgeRegistries.ITEMS, BlockWrapper.DECORATIVE_ITEM_REGISTRY);

        Blocks.IGNORED.register(bus);
    }

    public static class Blocks {

        // is there so RegisterEvent reads this class
        static DeferredRegister<Block> IGNORED = DeferredRegister.create(ForgeRegistries.BLOCKS, PoscardsSkills.ID);

        public static final BlockWrapper

                JADE_BRICKS = new BlockWrapper(BlockSet.JADE, BlockType.BRICKS),
                JADE_BRICK_STAIRS = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_STAIRS),
                JADE_BRICK_SLAB = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_SLAB),
                JADE_BRICK_WALL = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_WALL),
                CHISELED_JADE = new BlockWrapper(BlockSet.JADE, BlockType.CHISELED),
                JADE_PILLAR = new BlockWrapper(BlockSet.JADE, BlockType.PILLAR),
                LAYERED_JADE = new BlockWrapper(BlockSet.JADE, BlockType.LAYERED),
                SHINY_JADE = new BlockWrapper(BlockSet.JADE, BlockType.SHINY),

                JASPER_BRICKS = new BlockWrapper(BlockSet.JASPER, BlockType.BRICKS),
                JASPER_BRICK_STAIRS = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_STAIRS),
                JASPER_BRICK_SLAB = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_SLAB),
                JASPER_BRICK_WALL = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_WALL),
                CHISELED_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.CHISELED),
                JASPER_PILLAR = new BlockWrapper(BlockSet.JASPER, BlockType.PILLAR),
                LAYERED_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.LAYERED),
                SHINY_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.SHINY),

                MARBLE_BRICKS = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICKS),
                MARBLE_BRICK_STAIRS = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_STAIRS),
                MARBLE_BRICK_SLAB = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_SLAB),
                MARBLE_BRICK_WALL = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_WALL),
                CHISELED_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.CHISELED),
                MARBLE_PILLAR = new BlockWrapper(BlockSet.MARBLE, BlockType.PILLAR),
                LAYERED_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.LAYERED),
                SHINY_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.SHINY);

    }

}
