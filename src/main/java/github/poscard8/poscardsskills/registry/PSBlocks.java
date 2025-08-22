package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.block.BlockSet;
import github.poscard8.poscardsskills.util.block.BlockType;
import github.poscard8.poscardsskills.util.block.BlockWrapper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Jade, Jasper, and Marble blocks are registered via {@link BlockWrapper}.
 */
@SuppressWarnings("unused")
public class PSBlocks
{
    public static final DeferredRegister<Block> ALL = DeferredRegister.create(ForgeRegistries.BLOCKS, PoscardsSkills.ID);

    public static final BlockWrapper

            JADE = new BlockWrapper(BlockSet.JADE, BlockType.DEFAULT),
            JADE_STAIRS = new BlockWrapper(BlockSet.JADE, BlockType.STAIRS),
            JADE_SLAB = new BlockWrapper(BlockSet.JADE, BlockType.SLAB),
            JADE_WALL = new BlockWrapper(BlockSet.JADE, BlockType.WALL),
            JADE_BRICKS = new BlockWrapper(BlockSet.JADE, BlockType.BRICKS),
            JADE_BRICK_STAIRS = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_STAIRS),
            JADE_BRICK_SLAB = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_SLAB),
            JADE_BRICK_WALL = new BlockWrapper(BlockSet.JADE, BlockType.BRICK_WALL),
            CHISELED_JADE_BRICKS = new BlockWrapper(BlockSet.JADE, BlockType.CHISELED_BRICKS),

            JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.DEFAULT),
            JASPER_STAIRS = new BlockWrapper(BlockSet.JASPER, BlockType.STAIRS),
            JASPER_SLAB = new BlockWrapper(BlockSet.JASPER, BlockType.SLAB),
            JASPER_WALL = new BlockWrapper(BlockSet.JASPER, BlockType.WALL),
            JASPER_BRICKS = new BlockWrapper(BlockSet.JASPER, BlockType.BRICKS),
            JASPER_BRICK_STAIRS = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_STAIRS),
            JASPER_BRICK_SLAB = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_SLAB),
            JASPER_BRICK_WALL = new BlockWrapper(BlockSet.JASPER, BlockType.BRICK_WALL),
            CHISELED_JASPER_BRICKS = new BlockWrapper(BlockSet.JASPER, BlockType.CHISELED_BRICKS),

            MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.DEFAULT),
            MARBLE_STAIRS = new BlockWrapper(BlockSet.MARBLE, BlockType.STAIRS),
            MARBLE_SLAB = new BlockWrapper(BlockSet.MARBLE, BlockType.SLAB),
            MARBLE_WALL = new BlockWrapper(BlockSet.MARBLE, BlockType.WALL),
            MARBLE_BRICKS = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICKS),
            MARBLE_BRICK_STAIRS = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_STAIRS),
            MARBLE_BRICK_SLAB = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_SLAB),
            MARBLE_BRICK_WALL = new BlockWrapper(BlockSet.MARBLE, BlockType.BRICK_WALL),
            CHISELED_MARBLE_BRICKS = new BlockWrapper(BlockSet.MARBLE, BlockType.CHISELED_BRICKS).exclude(),
            MARBLE_PILLAR = new BlockWrapper(BlockSet.MARBLE, BlockType.PILLAR),
            LAYERED_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.LAYERED);

    public static void register(IEventBus bus)
    {
        ALL.register(bus);
        BlockWrapper.BLOCK_REGISTRY.register(bus);
        BlockWrapper.ITEM_REGISTRY.register(bus);
    }

}
