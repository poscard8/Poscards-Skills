package github.poscard8.poscardsskills.util.component;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;

/**
 * Attribute styling used for mod UI.
 */
public class AttributeStyle {

    public final ChatFormatting color;
    public final String icon;
    public final boolean percentage;

    public AttributeStyle(ChatFormatting color, String icon) { this(color, icon, false); }

    public AttributeStyle(ChatFormatting color, String icon, boolean percentage) {

        this.color = color;
        this.icon = icon;
        this.percentage = percentage;
    }

    public static AttributeStyle empty() { return new AttributeStyle(ChatFormatting.WHITE, ""); }

    public Component applyTo(Attribute attribute) {

        MutableComponent name = Component.translatable(attribute.getDescriptionId()).withStyle(color);
        Component iconComponent = Component.literal(icon).withStyle(color);

        return name.append(PSComponents.space()).append(iconComponent);
    }

}
