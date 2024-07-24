package github.poscard8.poscardsskills.util.component;

import github.poscard8.poscardsskills.util.item.PSRarities;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.common.IExtensibleEnum;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

public enum AnimatedColor implements IExtensibleEnum {

    ELEGANT(PSRarities.Styles.ELEGANT, 0x67FF67, 0x007700),
    ETHEREAL(PSRarities.Styles.ETHEREAL, 0xFF5A5A, 0x770000),
    CLASSICAL(PSRarities.Styles.CLASSICAL, 0xFFF9D9, 0xF0CB3A),
    BRILLIANT(PSRarities.Styles.BRILLIANT, 0xFFFFFF, 0x444444, 0x45E9FF, 0xDE2C47),
    RADIANT(PSRarities.Styles.RADIANT, 0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFFF, 0x0000FF, 0xFF00FF);

    private final Style style;
    private final int[] colors;

    AnimatedColor(Style style, int... colors) {

        this.style = style;
        this.colors = colors;
    }

    @SuppressWarnings("unused")
    public static AnimatedColor create(String name, Style style, int... colors) { throw new IllegalStateException(); }

    public static Optional<AnimatedColor> ofStyle(Style style) { return Arrays.stream(values()).filter(animatedColor -> animatedColor.style == style).findFirst(); }

    public TextColor getColor() {

        if (colors.length == 1) return TextColor.fromRgb(colors[0]);

        Calendar calendar = Calendar.getInstance();
        int seconds = calendar.get(Calendar.SECOND);
        int milliseconds = calendar.get(Calendar.MILLISECOND);

        int index = seconds % colors.length;
        int next = index == colors.length - 1 ? 0 : index + 1;
        int m = milliseconds / 100;

        if (m == 0) return TextColor.fromRgb(colors[index]);

        int firstColor = colors[index];
        int secondColor = colors[next];

        int[] firstColorRGB = new int[]{firstColor / 65536, (firstColor / 256) % 256, firstColor % 256};
        int[] secondColorRGB = new int[]{secondColor / 65536, (secondColor / 256) % 256, secondColor % 256};

        int red = (firstColorRGB[0] * (10 - m) + secondColorRGB[0] * m) / 10;
        int green = (firstColorRGB[1] * (10 - m) + secondColorRGB[1] * m) / 10;
        int blue = (firstColorRGB[2] * (10 - m) + secondColorRGB[2] * m) / 10;

        int newColor = red * 65536 + green * 256 + blue;

        return TextColor.fromRgb(newColor);
    }
}
