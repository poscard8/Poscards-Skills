package github.poscard8.poscardsskills.skill.reward;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/**
 * The XP here is game XP, not skill XP.
 */
public class XpReward extends Reward {

    public final int xp;

    protected XpReward(ResourceLocation skillKey, Set<Integer> at, int xp) {

        super(skillKey, at);
        this.xp = xp;
    }

}