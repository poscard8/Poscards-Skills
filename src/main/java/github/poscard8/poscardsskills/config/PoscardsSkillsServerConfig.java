package github.poscard8.poscardsskills.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PoscardsSkillsServerConfig
{
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue LOCK_DIAMOND_GEAR;
    public static final ForgeConfigSpec.BooleanValue LOCK_NETHERITE_GEAR;
    public static final ForgeConfigSpec.BooleanValue LOCK_VOLATILE_GEAR;
    public static final ForgeConfigSpec.DoubleValue LUCKY_KEY_LUCK_VALUE;
    public static final ForgeConfigSpec.DoubleValue LUCKY_KEY_WISDOM_VALUE;
    public static final ForgeConfigSpec.DoubleValue MIGHTY_KEY_LUCK_VALUE;
    public static final ForgeConfigSpec.DoubleValue MIGHTY_KEY_WISDOM_VALUE;
    public static final ForgeConfigSpec.IntValue REPAIR_STONE_RESTORE_VALUE;

    public static final ForgeConfigSpec.BooleanValue HAS_COMBAT_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_EXPLORING_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_FARMING_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_FISHING_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_MAGIC_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_MINING_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_SMITHING_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_SOCIAL_SKILL;
    public static final ForgeConfigSpec.BooleanValue HAS_WOODCUTTING_SKILL;

    static
    {
        BUILDER.push("Items");

        LOCK_DIAMOND_GEAR = BUILDER
                .comment("Locking diamond tools/weapons behind skills")
                .define("lockDiamondGear", false);

        LOCK_NETHERITE_GEAR = BUILDER
                .comment("Locking netherite tools/weapons behind skills")
                .define("lockNetheriteGear", false);

        LOCK_VOLATILE_GEAR = BUILDER
                .comment("Locking volatile tools/weapons behind skills")
                .define("lockVolatileGear", true);

        LUCKY_KEY_LUCK_VALUE = BUILDER
                .comment("The amount of chest luck a lucky key provides")
                .defineInRange("luckyKeyLuckValue", 25.0D, 0, 500);

        LUCKY_KEY_WISDOM_VALUE = BUILDER
                .comment("The amount of wisdom a lucky key provides")
                .defineInRange("luckyKeyWisdomValue", 0.0D, 0, 500);

        MIGHTY_KEY_LUCK_VALUE = BUILDER
                .comment("The amount of chest luck a mighty key provides")
                .defineInRange("mightyKeyLuckValue", 100.0D, 0, 500);

        MIGHTY_KEY_WISDOM_VALUE = BUILDER
                .comment("The amount of wisdom a mighty key provides")
                .defineInRange("mightyKeyWisdomValue", 25.0D, 0, 500);

        REPAIR_STONE_RESTORE_VALUE = BUILDER
                .comment("The amount of durability a repair stone restores in tools")
                .defineInRange("repairStoneRestoreValue", 128, 0, 10000);

        BUILDER.pop();

        BUILDER.push("Built-in Skills");

        HAS_COMBAT_SKILL = BUILDER.define("hasCombatSkill", true);
        HAS_EXPLORING_SKILL = BUILDER.define("hasExploringSkill", true);
        HAS_FARMING_SKILL = BUILDER.define("hasFarmingSkill", true);
        HAS_FISHING_SKILL = BUILDER.define("hasFishingSkill", true);
        HAS_MAGIC_SKILL = BUILDER.define("hasMagicSkill", true);
        HAS_MINING_SKILL = BUILDER.define("hasMiningSkill", true);
        HAS_SMITHING_SKILL = BUILDER.define("hasSmithingSkill", true);
        HAS_SOCIAL_SKILL = BUILDER.define("hasSocialSkill", true);
        HAS_WOODCUTTING_SKILL = BUILDER.define("hasWoodcuttingSkill", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
