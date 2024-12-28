package github.poscard8.poscardsskills.util.component;

import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Utility class for text-related configs.
 */
public class ComponentHandler {

    final Map<Supplier<Attribute>, AttributeStyle> attributeStyleMap;


    public ComponentHandler() { this.attributeStyleMap = new HashMap<>(); }

    public ColorPalette getColorPalette() { return PoscardsSkillsClientConfig.COLOR_PALETTE.get(); }

    public NumeralType getNumeralType() { return PoscardsSkillsClientConfig.NUMERAL_TYPE.get(); }

    public LevelUpMessageType getLevelUpMessageType() { return PoscardsSkillsClientConfig.LEVEL_UP_MESSAGE_TYPE.get(); }

    public ProgressMessageType getProgressMessageType() { return PoscardsSkillsClientConfig.PROGRESS_MESSAGE_TYPE.get(); }

    public boolean hasSkillDescriptions() { return PoscardsSkillsClientConfig.SKILL_DESCRIPTIONS.get(); }

    public boolean hasSplashTexts() { return PoscardsSkillsClientConfig.SPLASH_TEXTS.get(); }

    public ComponentHandler setAttributeStyle(Attribute attribute, AttributeStyle style) { return setAttributeStyle(() -> attribute, style); }

    public ComponentHandler setAttributeStyle(Supplier<Attribute> supplier, AttributeStyle style) {

        attributeStyleMap.put(supplier, style);
        return this;
    }

    public AttributeStyle getAttributeStyle(Attribute attribute) {

        for (Supplier<Attribute> supplier : attributeStyleMap.keySet()) {

            if (supplier.get().equals(attribute)) return attributeStyleMap.get(supplier);
        }
        return AttributeStyle.empty();
    }

    public Component getAttributeComponent(Attribute attribute) {

        AttributeStyle style = AttributeStyle.empty();

        for (Supplier<Attribute> supplier : attributeStyleMap.keySet()) {

            if (supplier.get().equals(attribute)) {

                style = attributeStyleMap.get(supplier);
                break;
            }
        }
        return style.applyTo(attribute);
    }


}
