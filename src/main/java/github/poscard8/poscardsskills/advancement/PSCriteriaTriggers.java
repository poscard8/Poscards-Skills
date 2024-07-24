package github.poscard8.poscardsskills.advancement;

import github.poscard8.poscardsskills.advancement.trigger.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

import java.util.ArrayList;
import java.util.List;

public final class PSCriteriaTriggers {

    private PSCriteriaTriggers() {}


    private static final List<CriterionTrigger<?>> VALUES = new ArrayList<>();

    public static final CarryBrilliantShardTrigger CARRY_BRILLIANT_SHARD = register(new CarryBrilliantShardTrigger());
    public static final GainXPTrigger GAIN_XP = register(new GainXPTrigger());
    public static final LevelUpTrigger LEVEL_UP = register(new LevelUpTrigger());
    public static final RareDropTrigger RARE_DROP = register(new RareDropTrigger());
    public static final SkillCraftingTrigger SKILL_CRAFTING = register(new SkillCraftingTrigger());
    public static final UseKeyTrigger USE_KEY = register(new UseKeyTrigger());

    public static void registerAll() { VALUES.forEach(CriteriaTriggers::register); }

    private static <T extends CriterionTrigger<?>> T register(T trigger) {

        VALUES.add(trigger);
        return trigger;
    }

}
