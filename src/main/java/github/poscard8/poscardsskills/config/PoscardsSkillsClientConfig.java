package github.poscard8.poscardsskills.config;

import github.poscard8.poscardsskills.util.component.*;
import net.minecraftforge.common.ForgeConfigSpec;

public class PoscardsSkillsClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.EnumValue<ColorPalette> COLOR_PALETTE;
    public static final ForgeConfigSpec.EnumValue<NumeralType> NUMERAL_TYPE;
    public static final ForgeConfigSpec.EnumValue<LevelUpMessageType> LEVEL_UP_MESSAGE_TYPE;
    public static final ForgeConfigSpec.EnumValue<ProgressMessageType> PROGRESS_MESSAGE_TYPE;
    public static final ForgeConfigSpec.EnumValue<EnchantmentColor> ENCHANTMENT_TEXT_COLOR;

    public static final ForgeConfigSpec.BooleanValue

            SKILL_DESCRIPTIONS,
            SPLASH_TEXTS,

            LEVEL_UP_PARTICLES,
            ASCENSION_PARTICLES,

            XP_GAIN_SOUND,
            LEVEL_UP_SOUND,
            ASCENSION_SOUND;


    public static final ForgeConfigSpec.IntValue MINIMUM_XP_FOR_PROGRESS_MESSAGE;

    static {

        BUILDER.push("Texts");

        COLOR_PALETTE = BUILDER
                .comment("Color palette for UI texts")
                .defineEnum("colorPalette", ColorPalette.DEFAULT);

        NUMERAL_TYPE = BUILDER
                .comment("")
                .comment("The way skill levels are displayed")
                .comment("Example: 'Mining XXXV' or 'Mining 35'")
                .defineEnum("numeralType", NumeralType.ROMAN);

        LEVEL_UP_MESSAGE_TYPE = BUILDER
                .comment("")
                .comment("The message displayed in the chat when the player levels up")
                .comment("LONG: Shows the new level, attribute buffs, and rewards")
                .comment("SHORT: Only shows the new level")
                .comment("NONE: No message, obviously")
                .defineEnum("levelUpMessageType", LevelUpMessageType.LONG);

        PROGRESS_MESSAGE_TYPE = BUILDER
                .comment("")
                .comment("The way skill progress is displayed when the player gains XP")
                .comment("Example: '+10 Mining (80/200)' or '+10 Mining (40%)'")
                .defineEnum("progressMessageType", ProgressMessageType.NUMERIC);

        ENCHANTMENT_TEXT_COLOR = BUILDER
                .comment("")
                .comment("Color of the enchantment texts")
                .comment("Note: Only used in mod enchantments, not vanilla enchantments")
                .defineEnum("enchantmentTextColor", EnchantmentColor.GRAY);

        SKILL_DESCRIPTIONS = BUILDER
                .comment("")
                .comment("Descriptions that appear when hovering over skill in main menu")
                .define("skillDescriptions", true);

        SPLASH_TEXTS = BUILDER
                .comment("")
                .comment("Splash texts are random messages similar to the ones in Minecraft's main menu")
                .comment("They are displayed when the player levels up a skill (50% chance)")
                .define("splashTexts", true);

        MINIMUM_XP_FOR_PROGRESS_MESSAGE = BUILDER
                .comment("")
                .comment("The minimum amount of XP required for a progress message to be displayed")
                .comment("When the player gains XP less than the value, no message is displayed")
                .defineInRange("minimumXPForProgressMessage", 1, 0, 1000000);

        BUILDER.pop();
        BUILDER.push("Particles");

        LEVEL_UP_PARTICLES = BUILDER
                .comment("Particles displayed whenever the player levels up a skill")
                .define("levelUpParticles", true);

        ASCENSION_PARTICLES = BUILDER
                .comment("")
                .comment("Particles displayed whenever the player ascends")
                .define("ascensionParticles", true);

        BUILDER.pop();
        BUILDER.push("Sounds");

        XP_GAIN_SOUND = BUILDER
                .comment("A sound effect played whenever the player gains skill XP")
                .define("xpGainSound", true);

        LEVEL_UP_SOUND = BUILDER
                .comment("")
                .comment("A sound effect played whenever the player levels up a skill")
                .define("levelUpSound", true);

        ASCENSION_SOUND = BUILDER
                .comment("")
                .comment("A sound effect played whenever the player ascends")
                .define("ascensionSound", true);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }

}
