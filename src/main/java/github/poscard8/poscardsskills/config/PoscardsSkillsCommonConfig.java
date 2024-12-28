package github.poscard8.poscardsskills.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PoscardsSkillsCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue

            KEEP_SKILL_RECIPES,
            KEEP_UNLOCKED_ITEMS,
            KEEP_SECRETS,
            KEEP_CLAIMED_REWARDS,

            SURVIVAL_UTILITIES,
            DECORATIVE_BLOCKS,
            MOD_ENCHANTMENTS,
            EXTRA_PROGRESSION,

            WOODCUTTING_SKILL,
            MINING_SKILL,
            FARMING_SKILL,
            COMBAT_SKILL,
            EXPLORING_SKILL,
            ENCHANTING_SKILL;

    public static final ForgeConfigSpec.IntValue

            XP_FOR_ASCENSION,
            XP_INCREASE_FOR_ASCENSION,

            BRILLIANT_REPAIR_STONE_DURABILITY,
            BLESSED_REPAIR_STONE_DURABILITY,
            DIVINE_REPAIR_STONE_DURABILITY;

    public static final ForgeConfigSpec.DoubleValue

            UNIVERSAL_XP_MULTIPLIER,

            BRILLIANT_SHARD_WISDOM,
            BLESSED_SHARD_WISDOM,
            DIVINE_SHARD_WISDOM,

            TRUE_EFFICIENCY_1_BREAK_VALUE,
            TRUE_EFFICIENCY_2_BREAK_VALUE,
            TRUE_EFFICIENCY_3_BREAK_VALUE,

            VITALITY_1_BOOST,
            VITALITY_2_BOOST,
            VITALITY_3_BOOST,

            EXPLOITATION_1_CHANCE,
            EXPLOITATION_2_CHANCE,
            EXPLOITATION_3_CHANCE;

    static {

        BUILDER.push("Gameplay");

        UNIVERSAL_XP_MULTIPLIER = BUILDER
                .comment("Multiplier for any xp source")
                .comment("Example: If the value is 2, the player will get double the xp from any source")
                .comment("Note: Does not affect xp given by commands")
                .defineInRange("universalXPMultiplier", 1D, 0, 25);

        XP_FOR_ASCENSION = BUILDER
                .comment("")
                .comment("Minimum total xp for ascending")
                .comment("This number also dictates how many rewards the player will get")
                .comment("Example: If the total xp is 1,000,000 and xpForAscension is 200,000; the player will get 5 rewards")
                .defineInRange("xpForAscension", 100000, 0, 100000000);

        XP_INCREASE_FOR_ASCENSION = BUILDER
                .comment("")
                .comment("Every time the player ascends, the minimum xp for next ascension increases")
                .comment("The increase in xp for each ascension")
                .defineInRange("xpIncreaseForAscension", 10000, 0, 100000000);

        KEEP_SKILL_RECIPES = BUILDER
                .comment("")
                .comment("Keeping unlocked skill recipes after ascension")
                .define("keepSkillRecipes", true);

        KEEP_UNLOCKED_ITEMS = BUILDER
                .comment("")
                .comment("External skills may lock certain items behind certain skill levels")
                .comment("Keeping unlocked items after ascensions")
                .define("keepUnlockedItems", true);

        KEEP_SECRETS = BUILDER
                .comment("")
                .comment("Keeping unlocked secrets after ascension")
                .define("keepSecrets", true);

        KEEP_CLAIMED_REWARDS = BUILDER
                .comment("")
                .comment("Keeping reward claim data after ascension")
                .comment("If true, claimed rewards will not re-generate after ascension")
                .define("keepClaimedRewards", false);

        BUILDER.pop();
        BUILDER.push("Modules");

        SURVIVAL_UTILITIES = BUILDER
                .comment("Enchanted tool recipes unlocked through skill progression")
                .define("survivalUtilities", true);

        DECORATIVE_BLOCKS = BUILDER
                .comment("")
                .comment("Recipes for Jade, Jasper and Marble block sets")
                .define("decorativeBlocks", true);

        MOD_ENCHANTMENTS = BUILDER
                .comment("")
                .comment("Recipes for custom enchanted books")
                .define("modEnchantments", true);

        EXTRA_PROGRESSION = BUILDER
                .comment("")
                .comment("Ability to obtain Blessed and Divine Shards and related items")
                .comment("Note: Changes the recipes for custom enchanted books")
                .define("extraProgression", true);

        BUILDER.pop();
        BUILDER.push("Skills");

        WOODCUTTING_SKILL = BUILDER.define("hasWoodcuttingSkill", true);
        MINING_SKILL = BUILDER.define("hasMiningSkill", true);
        FARMING_SKILL = BUILDER.define("hasFarmingSkill", true);
        COMBAT_SKILL = BUILDER.define("hasCombatSkill", true);
        EXPLORING_SKILL = BUILDER.define("hasExploringSkill", true);
        ENCHANTING_SKILL = BUILDER.define("hasEnchantingSkill", true);

        BUILDER.pop();
        BUILDER.push("Items");

        BRILLIANT_SHARD_WISDOM = BUILDER
                .comment("Wisdom values of magic shards")
                .defineInRange("brilliantShardWisdom", 1.25D, 0, 1024);

        BLESSED_SHARD_WISDOM = BUILDER.defineInRange("blessedShardWisdom", 3.5D, 0, 1024);
        DIVINE_SHARD_WISDOM = BUILDER.defineInRange("divineShardWisdom", 7D, 0, 1024);

        BRILLIANT_REPAIR_STONE_DURABILITY = BUILDER
                .comment("")
                .comment("Durability restored by repair stones")
                .defineInRange("brilliantRepairStoneDurability", 256, 0, 100000000);

        BLESSED_REPAIR_STONE_DURABILITY = BUILDER.defineInRange("blessedRepairStoneDurability", 1024, 0, 100000000);
        DIVINE_REPAIR_STONE_DURABILITY = BUILDER.defineInRange("divineRepairStoneDurability", 4096, 0, 100000000);

        BUILDER.pop();
        BUILDER.push("Enchantments");

        TRUE_EFFICIENCY_1_BREAK_VALUE = BUILDER
                .comment("Note: To configure the Dominance enchantment, see the tags")
                .comment("'dominance_x_target' in 'data/github.poscard8.poscardsskills/tags/entity_types'")
                .comment("")
                .comment("Additional blocks broken for each True Efficiency level")
                .comment("Example: If the value is 3.5, 3 adjacent blocks will be broken with a 100% chance")
                .comment("and another adjacent block will be broken with a 50% chance")
                .defineInRange("trueEfficiency1BreakValue", 0.6D, 0, 25);

        TRUE_EFFICIENCY_2_BREAK_VALUE = BUILDER.defineInRange("trueEfficiency2BreakValue", 1.1D, 0, 25);
        TRUE_EFFICIENCY_3_BREAK_VALUE = BUILDER.defineInRange("trueEfficiency3BreakValue", 1.5D, 0, 25);

        VITALITY_1_BOOST = BUILDER
                .comment("")
                .comment("The additional healing for each Vitality level")
                .comment("Example: If the boost is 0.2, the player will restore 1.2x the HP (or 20% more)")
                .defineInRange("vitality1Boost", 0.1D, 0, 25);

        VITALITY_2_BOOST = BUILDER.defineInRange("vitality2Boost", 0.18D, 0, 25);
        VITALITY_3_BOOST = BUILDER.defineInRange("vitality3Boost", 0.25D, 0, 25);

        EXPLOITATION_1_CHANCE = BUILDER
                .comment("")
                .comment("Chance of a weapon (with Exploitation) turning non-critical hits to critical ones")
                .comment("Example: If the chance is 0.2, the weapon has a 20% chance of landing a critical hit")
                .defineInRange("exploitation1Chance", 0.15D, 0, 1);

        EXPLOITATION_2_CHANCE = BUILDER.defineInRange("exploitation2Chance", 0.25D, 0, 1);
        EXPLOITATION_3_CHANCE = BUILDER.defineInRange("exploitation3Chance", 0.35D, 0, 1);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }


}
