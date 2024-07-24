package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.module.BrilliantUtilitiesModule;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UseAnvilExperienceSource implements ExperienceSource {

    private static final Map<Player, Integer> WAITING_XP_MAP = new HashMap<>();

    public final Skill skill;
    public final int xp;

    UseAnvilExperienceSource(Skill skill, int xp) {

        this.skill = skill;
        this.xp = xp;
    }

    public static UseAnvilExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");

        return new UseAnvilExperienceSource(skill, xp);
    }

    public static void handlePlayer(Player player) {

        if (player == null) return;
        if (getWaitingXP(player) == 0) return;

        for (UseAnvilExperienceSource xpSource : ExperienceSource.filterBy(UseAnvilExperienceSource.class)) {

            if (!player.isSpectator() && !player.isCreative()) {

                xpSource.applyTo(player, getWaitingXP(player));
                removeWaitingXP(player);
            }
        }
    }

    public static int evaluateItemStack(ItemStack stack) {

        if (stack.isEmpty()) return 0;
        if (stack.is(BrilliantUtilitiesModule.Items.BRILLIANT_BOOK.get())) return 15;

        int value = 0;
        int multiplier = 1;

        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {

            for (Enchantment.Rarity rarity : Enchantment.Rarity.values()) {

                if (rarity.equals(entry.getKey().getRarity())) break;
                multiplier++;
            }
            value += multiplier * entry.getValue();
        }
        return value;
    }

    public static int getWaitingXP(Player player) { return WAITING_XP_MAP.getOrDefault(player, 0); }

    public static void addWaitingXP(Player player, int xp) { WAITING_XP_MAP.put(player, getWaitingXP(player) + xp); }

    public static void addWaitingXP(Player player, ItemStack stack) { addWaitingXP(player, evaluateItemStack(stack)); }

    public static void removeWaitingXP(Player player) { WAITING_XP_MAP.put(player, 0); }

    public int getXP() { return xp; }

    @Override
    public Skill getSkill() { return skill; }


}
