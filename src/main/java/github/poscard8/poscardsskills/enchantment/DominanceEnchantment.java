package github.poscard8.poscardsskills.enchantment;

import github.poscard8.poscardsskills.registry.PSEnchantments;
import github.poscard8.poscardsskills.util.PSTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

/**
 * Enchantment that prevents certain hostile mobs from attacking the player.
 * See {@code "data/poscarsskills/tags/entity_types/dominance_x_target.json"} for the affected mobs.
 */
public class DominanceEnchantment extends PSEnchantment {

    public DominanceEnchantment(EnchantmentCategory category, EquipmentSlot[] slots) { super(category, slots); }

    public static boolean shouldCancelTargeting(Mob mob, Player player) {

        int level = EnchantmentHelper.getEnchantmentLevel(PSEnchantments.DOMINANCE.get(), player);
        boolean dominanceApply = shouldDominanceApply(mob, level);

        if (dominanceApply) {

            @Nullable LivingEntity lastAttacker = mob.getLastAttacker();
            return lastAttacker != player;

        } else return false;
    }

    protected static boolean shouldDominanceApply(LivingEntity entity, int level) {

        boolean apply;

        switch (level) {

            case 1 -> apply = entity.getType().is(PSTags.EntityTypes.DOMINANCE_1_TARGETS);
            case 2 -> apply = entity.getType().is(PSTags.EntityTypes.DOMINANCE_2_TARGETS);
            case 3 -> apply = entity.getType().is(PSTags.EntityTypes.DOMINANCE_3_TARGETS);
            default -> apply = false;
        }
        return apply;
    }

    @Deprecated
    @Override
    protected ForgeConfigSpec.DoubleValue[] configs() { return new ForgeConfigSpec.DoubleValue[0]; }

    @Deprecated
    @Override
    public double getValue(int level) { return defaultValue(); }

}
