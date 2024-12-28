package github.poscard8.poscardsskills.util.item;

import net.minecraft.world.item.Rarity;

/**
 * Rarities for runes.
 */
public class PSRarities {

    public static final Rarity

            ELEGANT = Rarity.create("poscardsskills:elegant", style -> PSStyles.ELEGANT),
            ETHEREAL = Rarity.create("poscardsskills:ethereal", style -> PSStyles.ETHEREAL),
            CLASSICAL = Rarity.create("poscardsskills:classical", style -> PSStyles.CLASSICAL);

}
