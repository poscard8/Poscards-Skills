package github.poscard8.poscardsskills.util.component;

import net.minecraft.ChatFormatting;

import java.util.HashMap;
import java.util.Map;

public enum ColorPalette {
    DEFAULT(
            ChatFormatting.GRAY, ChatFormatting.DARK_GRAY, ChatFormatting.YELLOW,
            ChatFormatting.GREEN, ChatFormatting.RED, ChatFormatting.DARK_AQUA,
            ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE,
            ChatFormatting.AQUA, ChatFormatting.GREEN, ChatFormatting.DARK_GRAY,
            ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.GOLD
    ),
    WARM(
            ChatFormatting.GRAY, ChatFormatting.DARK_GRAY, ChatFormatting.YELLOW,
            ChatFormatting.GREEN, ChatFormatting.RED, ChatFormatting.GOLD,
            ChatFormatting.GOLD, ChatFormatting.RED, ChatFormatting.DARK_RED,
            ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.WHITE,
            ChatFormatting.RED, ChatFormatting.DARK_RED, ChatFormatting.GOLD
    ),
    COLD(
            ChatFormatting.GRAY, ChatFormatting.DARK_GRAY, ChatFormatting.AQUA,
            ChatFormatting.GREEN, ChatFormatting.RED, ChatFormatting.BLUE,
            ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_BLUE,
            ChatFormatting.AQUA, ChatFormatting.GREEN, ChatFormatting.WHITE,
            ChatFormatting.BLUE, ChatFormatting.DARK_AQUA, ChatFormatting.DARK_PURPLE
    ),
    HIGH_CONTRAST(
            ChatFormatting.WHITE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.YELLOW,
            ChatFormatting.GREEN, ChatFormatting.YELLOW, ChatFormatting.AQUA,
            ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE,
            ChatFormatting.AQUA, ChatFormatting.GREEN, ChatFormatting.WHITE,
            ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.YELLOW
    ),
    ALL_WHITE(
            ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.WHITE,
            ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.WHITE,
            ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.WHITE,
            ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.WHITE,
            ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.WHITE
    );

    private final Map<Key, ChatFormatting> colorMap;

    ColorPalette(ChatFormatting... colors) {

        if (colors.length == Key.values().length) {

            Map<Key, ChatFormatting> map = new HashMap<>();
            int index = 0;

            for (Key key : Key.values()) {

                map.put(key, colors[index]);
                index++;
            }

            this.colorMap = map;

        } else throw new RuntimeException(String.format("Invalid number of colors for color palette %s.", this));
    }

    public ChatFormatting colorOf(Key key) { return colorMap.get(key); }


    public enum Key {

        DESCRIPTION,
        INSTRUCTIONS,
        MOD_INSTRUCTIONS,
        ALLOW,
        DENY,
        SKILL_AND_XP,
        SKILL_UI,
        REWARD,
        RECIPE,
        ITEM,
        EXPERIENCE,
        BAR_EMPTY,
        BAR_FULL,
        FILLER,
        MISC
    }

}
