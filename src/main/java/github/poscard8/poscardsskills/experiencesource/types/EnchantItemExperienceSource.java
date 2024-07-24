package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnchantItemExperienceSource implements ExperienceSource {

    private static final Map<Player, Integer> WAITING_XP_MAP = new HashMap<>();

    public final Skill skill;
    public final int xp;

    EnchantItemExperienceSource(Skill skill, int xp) {

        this.skill = skill;
        this.xp = xp;
    }

    public static EnchantItemExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");

        return new EnchantItemExperienceSource(skill, xp);
    }

    public static void handlePlayer(Player player) {

        if (player == null) return;
        if (getWaitingXP(player) == 0) return;

        for (EnchantItemExperienceSource xpSource : ExperienceSource.filterBy(EnchantItemExperienceSource.class)) {

            if (!player.isSpectator() && !player.isCreative()) {

                xpSource.applyTo(player, getWaitingXP(player));
                removeWaitingXP(player);
            }
        }
    }

    public static int getWaitingXP(Player player) { return WAITING_XP_MAP.getOrDefault(player, 0); }

    public static void addWaitingXP(Player player, int xp) { WAITING_XP_MAP.put(player, getWaitingXP(player) + xp); }

    public static void removeWaitingXP(Player player) { WAITING_XP_MAP.put(player, 0); }

    @Override
    public int getXP() { return xp; }

    @Override
    public Skill getSkill() { return skill; }


}
