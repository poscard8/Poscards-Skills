package github.poscard8.poscardsskills.experiencesource;

import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

/**
 * Abstract class used as a base for other experience sources.
 */
public abstract class SimpleExperienceSource<T> implements ExperienceSource  {

    public final Skill skill;
    public final int xp;
    public final Predicate<T> predicate;

    protected SimpleExperienceSource(Skill skill, int xp, Predicate<T> predicate) {

        this.skill = skill;
        this.xp = xp;
        this.predicate = predicate;
    }

    protected void applyIfMeetsConditions(ServerPlayer player, T t) { applyIfMeetsConditions(player, t, 1); }

    protected void applyIfMeetsConditions(ServerPlayer player, T t, int multiplier) { if (meetsConditions(player, t)) applyTo(player, multiplier); }

    protected boolean meetsConditions(Player player, T t) { return predicate.test(t) && !player.isCreative() && !player.isSpectator(); }

    @Override
    public int getXP() { return xp; }

    @Override
    public Skill getSkill() { return skill; }

}
