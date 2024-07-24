package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class GainXPTrigger extends SimpleCriterionTrigger<GainXPTrigger.TriggerInstance> {

    public static final ResourceLocation ID = PoscardsSkills.asResource("gain_xp");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite player, DeserializationContext ctx) {

        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        return new TriggerInstance(player, xp);
    }

    public void trigger(ServerPlayer serverPlayer, int totalXP) { trigger(serverPlayer, triggerInstance -> triggerInstance.matches(totalXP)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final int xp;

        public TriggerInstance(EntityPredicate.Composite player, int xp) {

            super(ID, player);
            this.xp = xp;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);
            jsonObject.addProperty("xp", xp);
            return jsonObject;
        }

        public boolean matches(int totalXP) { return totalXP >= xp; }

    }

}
