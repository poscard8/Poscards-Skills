package github.poscard8.poscardsskills.secret.types;

import github.poscard8.poscardsskills.secret.Secret;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillMilestone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Secret unlocked via reaching a skill milestone.
 */
public class SkillSecret extends Secret {

    public final ResourceLocation skillKey;
    public final int level;

    public SkillSecret(ResourceLocation skillKey, int level) { this(skillKey, level, 2); }

    public SkillSecret(ResourceLocation skillKey, int level, int weight) {

        super(weight);
        this.skillKey = skillKey;
        this.level = level;
    }

    @Nullable
    public Skill getSkill() { return Skill.byKey(skillKey); }

    public boolean canUnlock(SkillMilestone milestone) {

        return milestone.isUnlocked() && skillKey.equals(milestone.skill.key) && level == milestone.level;
    }

    public void tryUnlock(ServerPlayer serverPlayer, SkillMilestone milestone) {

        if (canUnlock(milestone)) unlock(serverPlayer);
    }

}
