package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.util.component.PSComponents;
import github.poscard8.poscardsskills.util.item.PSItemUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class BrilliantBookItem extends ItemWithDescription {

    public BrilliantBookItem(Properties property) { super(property); }

    public static List<EnchantmentInstance> getPossibleEnchants(ItemStack stack) {

        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        Collection<Enchantment> all = ForgeRegistries.ENCHANTMENTS.getValues();
        List<Enchantment> possible = new ArrayList<>();
        List<EnchantmentInstance> instances = new ArrayList<>();

        for (Enchantment enchantment : all) {

            Collection<Enchantment> existing = map.keySet();
            if (EnchantmentHelper.isEnchantmentCompatible(existing, enchantment) && !enchantment.isCurse() && enchantment.canEnchant(stack)) possible.add(enchantment);
        }

        for (Enchantment enchantment : possible) {

            int min;
            int max = enchantment.getMaxLevel() == 1 ? 1 : enchantment.getMaxLevel() + 1;

            if (map.containsKey(enchantment) && enchantment.getMaxLevel() > 1) {

                min = map.get(enchantment) == enchantment.getMaxLevel() ? enchantment.getMaxLevel() : enchantment.getMaxLevel() - 1;

            } else min = enchantment.getMaxLevel() == 1 ? 1 : enchantment.getMaxLevel() - 1;

            for (int lvl = min; lvl <= max; lvl++) instances.add(new EnchantmentInstance(enchantment, lvl));
        }

        return instances;
    }

    public static ItemStack applyRandom(ItemStack stack) { return applyRandom(stack, false); }

    public static ItemStack applyRandom(ItemStack stack, boolean showEnchantments) {

        List<EnchantmentInstance> instances = getPossibleEnchants(stack);
        if (!canApplyTo(stack) || instances.size() == 0) return ItemStack.EMPTY;

        EnchantmentInstance instance = instances.get(new Random().nextInt(instances.size()));

        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        map.put(instance.enchantment, instance.level);

        ItemStack copy = stack.copy();
        EnchantmentHelper.setEnchantments(map, copy);

        if (!showEnchantments) {

            copy.hideTooltipPart(ItemStack.TooltipPart.ENCHANTMENTS);
            copy.getOrCreateTag().putBoolean("pendingEnchantments", true);
            PSItemUtils.addText(copy, PSComponents.tripleQuestionMark());
        }
        return copy;
    }

    public static List<ItemStack> getApplicableItems() { return ForgeRegistries.ITEMS.getValues().stream().map(Item::getDefaultInstance).filter(BrilliantBookItem::canApplyTo).toList(); }

    public static boolean canApplyTo(ItemStack stack) { return canApplyTo(stack.getItem()); }

    public static boolean canApplyTo(Item item) { return item.getDefaultInstance().isEnchantable() && item.getDefaultInstance().isDamageableItem(); }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public boolean isEnchantable(ItemStack stack) { return false; }

}
