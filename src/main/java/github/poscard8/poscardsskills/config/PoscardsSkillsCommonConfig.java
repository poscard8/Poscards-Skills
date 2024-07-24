package github.poscard8.poscardsskills.config;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.module.BrilliantGearModule;
import github.poscard8.poscardsskills.module.BrilliantUtilitiesModule;
import github.poscard8.poscardsskills.module.DecorativeBlocksModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class PoscardsSkillsCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue

            DECORATIVE_BLOCKS,
            BRILLIANT_UTILITIES,
            BRILLIANT_GEAR;

    public static final ForgeConfigSpec.IntValue MAX_SKILL_LEVEL;

    public static final ForgeConfigSpec.BooleanValue

            MINING_SKILL,
            FARMING_SKILL,
            COMBAT_SKILL,
            MAGIC_SKILL,
            EXPLORING_SKILL;

    public static final ForgeConfigSpec.DoubleValue RARE_DROP_MULTIPLIER;

    static {

        BUILDER.push(String.format("Configs for %s", PoscardsSkills.NAME));

        DECORATIVE_BLOCKS = BUILDER
                .comment("")
                .comment("Decorative Blocks Module")
                .comment("Adds:")
                .comment(" -Jade, Jasper and Marble block sets")
                .comment("")
                .comment("Set true to enable")
                .define(DecorativeBlocksModule.CONFIG_KEY, true);

        BRILLIANT_UTILITIES = BUILDER
                .comment("")
                .comment("Brilliant Utilities Module")
                .comment("Adds:")
                .comment(" -Brilliant Fertilizer")
                .comment(" -Brilliant Pearl")
                .comment(" -Book o' Brilliance")
                .comment(" -Brilliant Key")
                .comment("")
                .comment("Set true to enable")
                .define(BrilliantUtilitiesModule.CONFIG_KEY, true);

        BRILLIANT_GEAR = BUILDER
                .comment("")
                .comment("Brilliant Gear Module")
                .comment("Adds:")
                .comment(" -Brilliant Sword, Shovel, Pickaxe, Axe, Hoe")
                .comment(" -Brilliant Helmet, Chestplate, Leggings, Boots")
                .comment("")
                .comment("Set true to enable")
                .define(BrilliantGearModule.CONFIG_KEY, false);

        MAX_SKILL_LEVEL = BUILDER
                .comment("Max level for skills")
                .defineInRange("maxSkillLevel", 50, 10, 200);

        MINING_SKILL = BUILDER
                .comment("Switches for built-in skills")
                .comment("Skill progress is saved in the game files even if the skill is removed")
                .define("hasMiningSkill", true);

        FARMING_SKILL = BUILDER.define("hasFarmingSkill", true);
        COMBAT_SKILL = BUILDER.define("hasCombatSkill", true);
        MAGIC_SKILL = BUILDER.define("hasMagicSkill", true);
        EXPLORING_SKILL = BUILDER.define("hasExploringSkill", true);

        RARE_DROP_MULTIPLIER = BUILDER
                .comment("Chance of a player getting a rare drop")
                .comment("Note: this number is just a multiplier")
                .comment("The chances of a player getting a rare drop is (the xp player has gained) x (this value)")
                .comment("Set 0 to disable")
                .defineInRange("rareDropMultiplier", 0.0001D, 0.0D, 0.1D);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }


}
