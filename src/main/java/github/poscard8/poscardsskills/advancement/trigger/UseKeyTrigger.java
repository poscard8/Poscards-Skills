package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class UseKeyTrigger extends SimpleCriterionTrigger<UseKeyTrigger.TriggerInstance> {

    public static final ResourceLocation ID = PoscardsSkills.asResource("use_key");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite player, DeserializationContext ctx) { return new TriggerInstance(player); }

    public void trigger(ServerPlayer serverPlayer) { trigger(serverPlayer, triggerInstance -> true); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(EntityPredicate.Composite player) { super(ID, player); }

    }

}
