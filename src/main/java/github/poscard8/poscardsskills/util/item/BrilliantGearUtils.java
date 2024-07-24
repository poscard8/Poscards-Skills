package github.poscard8.poscardsskills.util.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TieredItem;

import java.util.*;

public final class BrilliantGearUtils {

    private BrilliantGearUtils() {}

    private static final UUID MAIN_HAND_UUID = UUID.fromString("CB3F55D3-5C64-4F38-A497-9C13A33DB559");

    public static Item.Properties baseProperties() { return new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(PoscardsSkills.CREATIVE_TAB); }

    public static List<Component> getComponents(ItemStack stack) {

        List<Component> components = new ArrayList<>();

        components.add(PSComponents.holdShift());

        if (Screen.hasShiftDown()) {

            ChatFormatting color = PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION);

            components.add(Component.empty());

            List<Component> descriptionComponents = PSComponents.split(Component.translatable("tooltip.poscardsskills.brilliant_gear_desc").withStyle(color));
            components.addAll(descriptionComponents);
        }

        if (stack.isEnchanted()) components.add(Component.empty());

        return components;
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack, UUID uuid) {

        UUID newUUID = stack.getItem() instanceof TieredItem ? MAIN_HAND_UUID : uuid;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        Map<Attribute, Float> map = new HashMap<>();

        for (Skill skill : PoscardsSkills.getSkillHandler().getValues()) {

            float existingValue = map.getOrDefault(skill.attribute, 0.0F);
            map.put(skill.attribute, existingValue + 3 * skill.attributeAmount);
        }

        for (Attribute attribute : map.keySet()) {

            AttributeModifier modifier = new AttributeModifier(newUUID, () -> "Poscard's Skills: Brilliant gear modifier", map.get(attribute), AttributeModifier.Operation.ADDITION);
            builder.put(attribute, modifier);
        }

        return builder.build();
    }

}
