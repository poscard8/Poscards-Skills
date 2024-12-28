package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Gives xp when the player enchants items using an enchanting table.
 * XP is given after the player closes the enchanting table and is
 * multiplied by the minecraft xp levels the enchantment required.
 * <p>See the wiki for the format.</p>
 */
public final class EnchantingTableExperienceSource implements ExperienceSource {

    static final Map<ServerPlayer, Integer> WAITING_XP_MAP = new HashMap<>();

    public final Skill skill;
    public final int xp;

    EnchantingTableExperienceSource(Skill skill, int xp) {

        this.skill = skill;
        this.xp = xp;
    }

    public static EnchantingTableExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");

        return new EnchantingTableExperienceSource(skill, xp);
    }

    public static void handlePlayer(@Nullable ServerPlayer player) {

        if (player == null) return;
        if (getWaitingXP(player) == 0) return;

        for (EnchantingTableExperienceSource xpSource : ExperienceSource.filterBy(EnchantingTableExperienceSource.class)) {

            if (!player.isSpectator() && !player.isCreative()) {

                xpSource.applyTo(player, getWaitingXP(player));
                removeWaitingXP(player);
            }
        }
    }

    public static int getWaitingXP(ServerPlayer player) { return WAITING_XP_MAP.getOrDefault(player, 0); }

    public static void addWaitingXP(ServerPlayer player, int xp) { WAITING_XP_MAP.put(player, getWaitingXP(player) + xp); }

    public static void removeWaitingXP(ServerPlayer player) { WAITING_XP_MAP.put(player, 0); }

    @Override
    public int getXP() { return xp; }

    @Override
    public Skill getSkill() { return skill; }


}
