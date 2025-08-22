package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.util.item.VolatileTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class VolatileItem
{
    public static class Shovel extends ShovelItem
    {
        public Shovel(Properties property) { super(VolatileTier.INSTANCE, 0.5F, -3.0F, property); }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
        {
            components.add(Component.translatable("item.poscardsskills.volatile_tool.desc").withStyle(ChatFormatting.GRAY));
            super.appendHoverText(stack, level, components, flag);
        }

        @Override
        public boolean isEnchantable(ItemStack stack) { return false; }

        @Override
        public boolean isBookEnchantable(ItemStack stack, ItemStack book) { return false; }

        @Override
        public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }
    }

    public static class Pickaxe extends PickaxeItem
    {
        public Pickaxe(Properties property) { super(VolatileTier.INSTANCE, 0, -2.8F, property); }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
        {
            components.add(Component.translatable("item.poscardsskills.volatile_tool.desc").withStyle(ChatFormatting.GRAY));
            super.appendHoverText(stack, level, components, flag);
        }

        @Override
        public boolean isEnchantable(ItemStack stack) { return false; }

        @Override
        public boolean isBookEnchantable(ItemStack stack, ItemStack book) { return false; }

        @Override
        public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }
    }

    public static class Axe extends AxeItem
    {
        public Axe(Properties property) { super(VolatileTier.INSTANCE, 5.0F, -3.0F, property); }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
        {
            components.add(Component.translatable("item.poscardsskills.volatile_tool.desc").withStyle(ChatFormatting.GRAY));
            super.appendHoverText(stack, level, components, flag);
        }

        @Override
        public boolean isEnchantable(ItemStack stack) { return false; }

        @Override
        public boolean isBookEnchantable(ItemStack stack, ItemStack book) { return false; }

        @Override
        public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }
    }

    public static class Hoe extends HoeItem
    {
        public Hoe(Properties property) { super(VolatileTier.INSTANCE, -6, 0.0F, property); }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
        {
            components.add(Component.translatable("item.poscardsskills.volatile_tool.desc").withStyle(ChatFormatting.GRAY));
            super.appendHoverText(stack, level, components, flag);
        }

        @Override
        public boolean isEnchantable(ItemStack stack) { return false; }

        @Override
        public boolean isBookEnchantable(ItemStack stack, ItemStack book) { return false; }

        @Override
        public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }
    }

    public static class Sword extends SwordItem
    {
        public Sword(Properties property) { super(VolatileTier.INSTANCE, 3, -2.4F, property); }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
        {
            components.add(Component.translatable("item.poscardsskills.volatile_weapon.desc").withStyle(ChatFormatting.GRAY));
            super.appendHoverText(stack, level, components, flag);
        }

        @Override
        public boolean isEnchantable(ItemStack stack) { return false; }

        @Override
        public boolean isBookEnchantable(ItemStack stack, ItemStack book) { return false; }

        @Override
        public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }
    }

}
