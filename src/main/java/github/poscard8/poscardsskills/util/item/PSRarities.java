package github.poscard8.poscardsskills.util.item;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;

public final class PSRarities {

    private PSRarities() {}

    public static final Rarity

            ELEGANT = Rarity.create("poscardsskills:elegant", style -> Styles.ELEGANT),
            ETHEREAL = Rarity.create("poscardsskills:ethereal", style -> Styles.ETHEREAL),
            CLASSICAL = Rarity.create("poscardsskills:classical", style -> Styles.CLASSICAL),
            BRILLIANT = Rarity.create("poscardsskills:brilliant", style -> Styles.BRILLIANT),
            RADIANT = Rarity.create("poscardsskills:radiant", style -> Styles.RADIANT);


    public static class Styles {

        public static final Style
                ELEGANT = Style.EMPTY.withColor(0xFFFFFA),
                ETHEREAL = Style.EMPTY.withColor(0xFFFFFB),
                CLASSICAL = Style.EMPTY.withColor(0xFFFFFC),
                BRILLIANT = Style.EMPTY.withColor(0xFFFFFD),
                RADIANT = Style.EMPTY.withColor(0xFFFFFE);

    }

}
