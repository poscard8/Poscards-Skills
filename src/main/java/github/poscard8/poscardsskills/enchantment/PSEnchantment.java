package github.poscard8.poscardsskills.enchantment;

import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.util.component.EnchantmentColor;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Mod enchantments that are unlocked at level 50 skills.
 * <p>-Cannot be stacked</p>
 * <p>-Max level of 3 (1 if extra progression is disabled)</p>
 * <p>-Fancy text color (configurable)</p>
 * <p>-Configurable values</p>
 * <p>-Can be disabled</p>
 */
public abstract class PSEnchantment extends Enchantment {

    public PSEnchantment(EnchantmentCategory category, EquipmentSlot[] slots) { super(Rarity.VERY_RARE, category, slots); }

    protected abstract ForgeConfigSpec.DoubleValue[] configs();

    protected double defaultValue() { return 0; }

    public double getValue(int level) {

        double value;
        switch (level) {

            case 1 -> value = configs()[0].get();
            case 2 -> value = configs()[1].get();
            case 3 -> value = configs()[2].get();
            default -> value = defaultValue();
        }
        return value;
    }

    /**
     * Adding the ability to customize the color.
     */
    @Override
    @NotNull
    public Component getFullname(int level) {

        EnchantmentColor enchantmentColor = PoscardsSkillsClientConfig.ENCHANTMENT_TEXT_COLOR.get();
        MutableComponent mutablecomponent = enchantmentColor.applyTo(Component.translatable(getDescriptionId()));

        if (level != 1 || getMaxLevel() != 1) {

            mutablecomponent.append(PSComponents.space()).append(Component.translatable("enchantment.level." + level));
        }

        return mutablecomponent;
    }

    @Override
    public int getMaxLevel() { return 3; }

    @Override
    public boolean isTradeable() { return false; }

    @Override
    public boolean isDiscoverable() { return false; }

    @Override
    public boolean isTreasureOnly() { return true; }

    @Override
    public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories) {

        return PoscardsSkillsCommonConfig.MOD_ENCHANTMENTS.get() && super.allowedInCreativeTab(book, allowedCategories);
    }

}
