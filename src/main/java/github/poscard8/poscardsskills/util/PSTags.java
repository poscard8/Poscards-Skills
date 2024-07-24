package github.poscard8.poscardsskills.util;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public final class PSTags {

    private PSTags() {}


    public static class Blocks {

        public static final TagKey<Block>

                JADE_BLOCKS = create("jade_blocks"),
                JASPER_BLOCKS = create("jasper_blocks"),
                MARBLE_BLOCKS = create("marble_blocks"),
                ROUGH_STONES = create("rough_stones");

        private static TagKey<Block> create(String name) { return TagKey.create(ForgeRegistries.Keys.BLOCKS, PoscardsSkills.asResource(name)); }

    }

    public static class Items {

        public static final TagKey<Item>

                BRILLIANT_GEAR = create("brilliant_gear"),
                BRILLIANT_UTILITIES = create("brilliant_utilities"),
                JADE_BLOCKS = create("jade_blocks"),
                JASPER_BLOCKS = create("jasper_blocks"),
                MARBLE_BLOCKS = create("marble_blocks"),
                ROUGH_STONES = create("rough_stones"),
                RUNE_NOT_APPLICABLE = create("rune_not_applicable"),
                RUNES = create("runes");

        private static TagKey<Item> create(String name) { return TagKey.create(ForgeRegistries.Keys.ITEMS, PoscardsSkills.asResource(name)); }

    }

    public static class EntityTypes {

        public static final TagKey<EntityType<?>> COMMON_ENEMIES = create("common_enemies");

        private static TagKey<EntityType<?>> create(String name) { return TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, PoscardsSkills.asResource(name)); }

    }

    public static class Biomes {

        private static final TagKey<Biome>

                GENERATES_JADE = create("generates_jade"),
                GENERATES_JASPER = create("generates_jasper"),
                GENERATES_MARBLE = create("generates_marble");

        private static TagKey<Biome> create(String name) { return TagKey.create(ForgeRegistries.Keys.BIOMES, PoscardsSkills.asResource(name)); }

    }

}
