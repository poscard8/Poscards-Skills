package github.poscard8.poscardsskills.skill.misc;

import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.SkillInstance;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public record SkillRequisite(Skill skill, int level) implements RequisiteHolder, Predicate<Player> {

    @Override
    public SkillRequisite getRequisite() { return this; }

    @Override
    public boolean test(Player player) { return test(SkillData.of(player)); }

    public boolean test(SkillData data) { return test(data.getSkill(skill)); }

    public boolean test(SkillInstance instance) { return instance.skill == skill && instance.level >= level; }

}
