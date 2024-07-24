package github.poscard8.poscardsskills.experiencesource;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

/**
 * Explained in the wiki.
 */
public interface ExperienceSource {

    static Collection<ExperienceSource> filterBy(Skill skill) { return PoscardsSkills.getXPSourceHandler().values().filter(xpSource -> xpSource.getSkill().equals(skill)).toList(); }

    static <E extends ExperienceSource> Collection<E> filterBy(Class<E> clazz) { return PoscardsSkills.getXPSourceHandler().values().filter(xpSource -> xpSource.getClass().equals(clazz)).map(clazz::cast).toList(); }


    default void applyTo(Player player) { applyTo(player, 1); }

    default void applyTo(Player player, int multiplier) { SkillInstance.of(player, getSkill()).addXP(player, getXP() * multiplier, getTypeKey()); }

    default ResourceLocation getTypeKey() { return PoscardsSkills.getXPSourceHandler().typeKeyOf(this); }

    int getXP();

    Skill getSkill();

}
