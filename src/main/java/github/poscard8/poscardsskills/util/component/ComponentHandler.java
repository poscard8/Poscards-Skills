package github.poscard8.poscardsskills.util.component;

import com.mojang.datafixers.util.Pair;
import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ComponentHandler {

    private final Map<Supplier<Attribute>, Pair<ChatFormatting, String>> attributeStyleMap = new HashMap<>();


    public ColorPalette getColorPalette() { return PoscardsSkillsClientConfig.COLOR_PALETTE.get(); }

    public NumeralType getNumeralType() { return PoscardsSkillsClientConfig.NUMERAL_TYPE.get(); }

    public LevelUpMessageType getLevelUpMessageType() { return PoscardsSkillsClientConfig.LEVEL_UP_MESSAGE_TYPE.get(); }

    public ProgressMessageType getProgressMessageType() { return PoscardsSkillsClientConfig.PROGRESS_MESSAGE_TYPE.get(); }

    public boolean hasSkillDescriptions() { return PoscardsSkillsClientConfig.SKILL_DESCRIPTIONS.get(); }

    public ComponentHandler setAttributeStyle(Supplier<Attribute> supplier, ChatFormatting color, String icon) {

        attributeStyleMap.put(supplier, Pair.of(color, icon));
        return this;
    }

    public ChatFormatting getAttributeColor(Attribute attribute) {

        for (Supplier<Attribute> supplier : attributeStyleMap.keySet()) {

            if (supplier.get().equals(attribute)) return attributeStyleMap.get(supplier).getFirst();
        }
        return ChatFormatting.WHITE;
    }

    public String getAttributeIcon(Attribute attribute) {

        for (Supplier<Attribute> supplier : attributeStyleMap.keySet()) {

            if (supplier.get().equals(attribute)) return attributeStyleMap.get(supplier).getSecond();
        }
        return "";
    }


}
