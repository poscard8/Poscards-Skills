package github.poscard8.poscardsskills.util;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Mod tags.
 */
@SuppressWarnings("unused")
public class PSTags
{
    public static class Blocks
    {
        public static final TagKey<Block>

                JADE_BLOCKS = create("jade_blocks"),
                JASPER_BLOCKS = create("jasper_blocks"),
                MARBLE_BLOCKS = create("marble_blocks"),
                MINERAL = create("mineral"),
                MINERAL_REPLACEABLE = create("mineral_replaceable");

        static TagKey<Block> create(String name) { return TagKey.create(ForgeRegistries.Keys.BLOCKS, PoscardsSkills.asResource(name)); }

    }

    public static class Items
    {
        public static final TagKey<Item>

                JADE_BLOCKS = create("jade_blocks"),
                JASPER_BLOCKS = create("jasper_blocks"),
                MARBLE_BLOCKS = create("marble_blocks"),
                GEMSTONES = create("gemstones"),
                EMPTY_RUNES = create("empty_runes"),
                NON_EMPTY_RUNES = create("non_empty_runes"),
                RUNES = create("runes"),
                RUNE_APPLICABLE = create("rune_applicable"),
                MINERAL = create("mineral");

        static TagKey<Item> create(String name) { return TagKey.create(ForgeRegistries.Keys.ITEMS, PoscardsSkills.asResource(name)); }

    }

    public static class Biomes
    {
        public static final TagKey<Biome>

                GENERATES_JADE = create("generates_jade"),
                GENERATES_JASPER = create("generates_jasper"),
                GENERATES_MARBLE = create("generates_marble");

        static TagKey<Biome> create(String name) { return TagKey.create(ForgeRegistries.Keys.BIOMES, PoscardsSkills.asResource(name)); }

    }

    public static class BannerPatterns
    {
        public static final TagKey<BannerPattern>

                ROMAN_NUMERALS = create("pattern_item/roman_numerals"),
                NUMBERS = create("pattern_item/numbers"),
                ICONS = create("pattern_item/icons");

        static TagKey<BannerPattern> create(String name) { return TagKey.create(Registries.BANNER_PATTERN, PoscardsSkills.asResource(name)); }

    }

}
