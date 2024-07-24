package github.poscard8.poscardsskills.util.component;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Roman numeral system that works with numbers 1-1000.
 */
public class RomanNumeral {

    private static final String[] FIRST_DIGIT = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
    private static final String[] SECOND_DIGIT = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    private static final String[] THIRD_DIGIT = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};


    private final String string;

    private RomanNumeral(String string) { this.string = string; }

    public static RomanNumeral of(int number) {

        if (number < 0 || number > 1000) throw new IllegalArgumentException("Number out of range (0-999)");

        if (number == 1000) return new RomanNumeral("M");

        int firstDigit = number / 100;
        int secondDigit = (number % 100) / 10;
        int thirdDigit = number % 10;

        return new RomanNumeral(FIRST_DIGIT[firstDigit] + SECOND_DIGIT[secondDigit] + THIRD_DIGIT[thirdDigit]);
    }

    public MutableComponent component() { return Component.literal(string); }

    @Override
    public String toString() { return string; }

}
