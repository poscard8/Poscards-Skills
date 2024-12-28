package github.poscard8.poscardsskills.skill.misc;

import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillData;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

/**
 *
 * @param highScore If {@code true}, all-time high score of that skill is tested. If {@code false}, current level is tested.
 */
public record SkillRequisite(Skill skill, int level, boolean highScore) implements RequisiteHolder, Predicate<ServerPlayer> {

    @Override
    public SkillRequisite getRequisite() { return this; }

    @Override
    public boolean test(ServerPlayer player) { return test(SkillData.of(player)); }

    public boolean test(SkillData skillData) { return highScore ? test(skill, skillData.getHighScore(skill)) : test(skill, skillData.getLevel(skill)); }

    public boolean test(Skill skill, int level) { return skill == this.skill && level >= this.level; }

}
