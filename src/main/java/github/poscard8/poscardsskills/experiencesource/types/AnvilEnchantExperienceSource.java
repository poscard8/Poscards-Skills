package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Gives xp when the player enchants items on an anvil.
 * {@code WAITING_XP_MAP} enables players to gain their xp <b>after</b>
 * closing the anvil menu.
 * <p>See the wiki for the format.</p>
 */
public final class AnvilEnchantExperienceSource implements ExperienceSource {

    static final Map<ServerPlayer, Integer> WAITING_XP_MAP = new HashMap<>();

    public final Skill skill;
    public final int xp;

    AnvilEnchantExperienceSource(Skill skill, int xp) {

        this.skill = skill;
        this.xp = xp;
    }

    public static AnvilEnchantExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");

        return new AnvilEnchantExperienceSource(skill, xp);
    }

    public static void handlePlayer(@Nullable ServerPlayer player) {

        if (player == null) return;
        if (getWaitingXP(player) == 0) return;

        for (AnvilEnchantExperienceSource xpSource : ExperienceSource.filterBy(AnvilEnchantExperienceSource.class)) {

            if (!player.isSpectator() && !player.isCreative()) {

                xpSource.applyTo(player, getWaitingXP(player));
                removeWaitingXP(player);
            }
        }
    }

    /**
     * Method that calculates the xp multiplier.
     * XP gain is multiplied by this result.
     */
    public static int evaluateItemStack(ItemStack stack) {

        if (stack.isEmpty()) return 0;

        int value = 0;
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {

            value += (entry.getKey().getRarity().ordinal() + 1) * entry.getValue();
        }
        return value;
    }

    public static int getWaitingXP(ServerPlayer player) { return WAITING_XP_MAP.getOrDefault(player, 0); }

    public static void addWaitingXP(ServerPlayer player, int xp) { WAITING_XP_MAP.put(player, getWaitingXP(player) + xp); }

    public static void addWaitingXP(ServerPlayer player, ItemStack stack) { addWaitingXP(player, evaluateItemStack(stack)); }

    public static void removeWaitingXP(ServerPlayer player) { WAITING_XP_MAP.put(player, 0); }

    public int getXP() { return xp; }

    @Override
    public Skill getSkill() { return skill; }


}
