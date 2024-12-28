package github.poscard8.poscardsskills.enchantment;

import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Enchantment that increases the healing rate of an entity (by any source).
 */
public class VitalityEnchantment extends PSEnchantment {

    public VitalityEnchantment(EnchantmentCategory category, EquipmentSlot[] slots) { super(category, slots); }

    @Override
    protected ForgeConfigSpec.DoubleValue[] configs() {

        return new ForgeConfigSpec.DoubleValue[]{

                PoscardsSkillsCommonConfig.VITALITY_1_BOOST,
                PoscardsSkillsCommonConfig.VITALITY_2_BOOST,
                PoscardsSkillsCommonConfig.VITALITY_3_BOOST
        };
    }

}
