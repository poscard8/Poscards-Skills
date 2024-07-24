package github.poscard8.poscardsskills.util.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class PSItemUtils {

    private PSItemUtils() {}

    public static void addText(ItemStack stack, Component... components) {

        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag display = new CompoundTag();
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf(""));

        for (Component component : components) {

            StringTag stringTag = StringTag.valueOf(Component.Serializer.toJson(component));
            lore.add(stringTag);
        }
        display.put("Lore", lore);
        tag.put("display", display);
        stack.setTag(tag);
    }

    public static void removeText(ItemStack stack) {

        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag display = new CompoundTag();

        display.put("Lore", new ListTag());
        tag.put("display", display);
        stack.setTag(tag);
    }

}
