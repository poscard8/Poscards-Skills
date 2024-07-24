package github.poscard8.poscardsskills.config;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.LevelUpMessageType;
import github.poscard8.poscardsskills.util.component.NumeralType;
import github.poscard8.poscardsskills.util.component.ProgressMessageType;
import net.minecraftforge.common.ForgeConfigSpec;

public class PoscardsSkillsClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.EnumValue<ColorPalette> COLOR_PALETTE;
    public static final ForgeConfigSpec.EnumValue<NumeralType> NUMERAL_TYPE;
    public static final ForgeConfigSpec.EnumValue<LevelUpMessageType> LEVEL_UP_MESSAGE_TYPE;
    public static final ForgeConfigSpec.EnumValue<ProgressMessageType> PROGRESS_MESSAGE_TYPE;
    public static final ForgeConfigSpec.BooleanValue XP_GAIN_SOUND;
    public static final ForgeConfigSpec.BooleanValue LEVEL_UP_SOUND;
    public static final ForgeConfigSpec.BooleanValue SKILL_DESCRIPTIONS;
    public static final ForgeConfigSpec.IntValue MINIMUM_XP_FOR_PROGRESS_MESSAGE;

    static {

        BUILDER.push(String.format("Configs for %s", PoscardsSkills.NAME));

        COLOR_PALETTE = BUILDER
                .comment("Color palette for UI texts")
                .defineEnum("colorPalette", ColorPalette.DEFAULT);

        NUMERAL_TYPE = BUILDER
                .comment("The way skill levels are displayed")
                .comment("Example: 'Mining XXXV' or 'Mining 35'")
                .defineEnum("numeralType", NumeralType.ROMAN);

        LEVEL_UP_MESSAGE_TYPE = BUILDER
                .comment("The message displayed in the chat when the player levels up")
                .comment("LONG: Shows the new level, attribute buffs, and rewards")
                .comment("SHORT: Only shows the new level")
                .comment("NONE: No message, obviously")
                .defineEnum("levelUpMessageType", LevelUpMessageType.LONG);

        PROGRESS_MESSAGE_TYPE = BUILDER
                .comment("The way skill progress is displayed when the player gains XP")
                .comment("Example: '+10 Mining (80/200)' or '+10 Mining (40%)'")
                .defineEnum("progressMessageType", ProgressMessageType.NUMERIC);

        XP_GAIN_SOUND = BUILDER
                .comment("Enables a sound effect whenever the player gains skill XP")
                .define("xpGainSound", true);

        LEVEL_UP_SOUND = BUILDER
                .comment("Enables a sound effect whenever the player levels up a skill")
                .define("levelUpSound", true);

        SKILL_DESCRIPTIONS = BUILDER
                .comment("Descriptions that appear when hovering over skill in main menu")
                .define("skillDescriptions", true);

        MINIMUM_XP_FOR_PROGRESS_MESSAGE = BUILDER
                .comment("The minimum amount of XP required for a progress message to be displayed")
                .comment("When the player gains XP less than the value, no message is displayed")
                .defineInRange("minimumXPForProgressMessage", 1, 0, 1000);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }

}
