package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;


public class LevelUpTrigger extends SimpleCriterionTrigger<LevelUpTrigger.TriggerInstance> {

    public static final ResourceLocation ID = PoscardsSkills.asResource("level_up");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite player, DeserializationContext ctx) {

        int level = GsonHelper.getAsInt(jsonObject, "level");
        return new TriggerInstance(player, level);
    }

    public void trigger(ServerPlayer serverPlayer, int newLevel) { trigger(serverPlayer, triggerInstance -> triggerInstance.matches(newLevel)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final int level;

        public TriggerInstance(EntityPredicate.Composite player, int level) {

            super(ID, player);
            this.level = level;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);
            jsonObject.addProperty("level", level);
            return jsonObject;
        }

        public boolean matches(int newLevel) { return newLevel >= level; }

    }

}
