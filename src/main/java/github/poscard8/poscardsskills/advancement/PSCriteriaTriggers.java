package github.poscard8.poscardsskills.advancement;

import github.poscard8.poscardsskills.advancement.trigger.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for advancement criteria triggers.
 */
public class PSCriteriaTriggers {

    public static final List<CriterionTrigger<?>> ALL = new ArrayList<>();

    public static final AscensionTrigger ASCENSION = register(new AscensionTrigger());
    public static final CarryMagicShardTrigger CARRY_BRILLIANT_SHARD = register(new CarryMagicShardTrigger());
    public static final GainXPTrigger GAIN_XP = register(new GainXPTrigger());
    public static final LevelUpTrigger LEVEL_UP = register(new LevelUpTrigger());
    public static final SecretTrigger SECRET = register(new SecretTrigger());
    public static final SkillCraftingTrigger SKILL_CRAFTING = register(new SkillCraftingTrigger());

    public static void register() { ALL.forEach(CriteriaTriggers::register); }

    static <T extends CriterionTrigger<?>> T register(T trigger) {

        ALL.add(trigger);
        return trigger;
    }

}
