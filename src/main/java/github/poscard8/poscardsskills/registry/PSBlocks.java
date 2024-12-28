package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.wrapper.BlockSet;
import github.poscard8.poscardsskills.util.wrapper.BlockType;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Jade, Jasper, and Marble blocks are registered via {@link BlockWrapper}.
 */
public final class PSBlocks {

    public static final DeferredRegister<Block> ALL = DeferredRegister.create(ForgeRegistries.BLOCKS, PoscardsSkills.ID);

    public static final BlockWrapper

            JADE_BRICKS = new BlockWrapper(BlockSet.JADE, BlockType.BRICKS),
            JADE_BRICK_STAIRS = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_STAIRS),
            JADE_BRICK_SLAB = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_SLAB),
            JADE_BRICK_WALL = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_WALL),
            CHISELED_JADE = new BlockWrapper(BlockSet.JADE, BlockType.CHISELED),
            JADE_PILLAR = new BlockWrapper(BlockSet.JADE, BlockType.PILLAR),
            LAYERED_JADE = new BlockWrapper(BlockSet.JADE, BlockType.LAYERED),
            SHINY_JADE = new BlockWrapper(BlockSet.JADE, BlockType.SHINY),
            ROUGH_JADE = new BlockWrapper(BlockSet.JADE, BlockType.ROUGH),

            JASPER_BRICKS = new BlockWrapper(BlockSet.JASPER, BlockType.BRICKS),
            JASPER_BRICK_STAIRS = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_STAIRS),
            JASPER_BRICK_SLAB = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_SLAB),
            JASPER_BRICK_WALL = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_WALL),
            CHISELED_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.CHISELED),
            JASPER_PILLAR = new BlockWrapper(BlockSet.JASPER, BlockType.PILLAR),
            LAYERED_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.LAYERED),
            SHINY_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.SHINY),
            ROUGH_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.ROUGH),

            MARBLE_BRICKS = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICKS),
            MARBLE_BRICK_STAIRS = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_STAIRS),
            MARBLE_BRICK_SLAB = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_SLAB),
            MARBLE_BRICK_WALL = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_WALL),
            CHISELED_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.CHISELED),
            MARBLE_PILLAR = new BlockWrapper(BlockSet.MARBLE, BlockType.PILLAR),
            LAYERED_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.LAYERED),
            SHINY_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.SHINY),
            ROUGH_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.ROUGH);

    public static void register(IEventBus bus) {

        ALL.register(bus);
        BlockWrapper.BLOCK_REGISTRY.register(bus);
        BlockWrapper.ITEM_REGISTRY.register(bus);
    }

}
