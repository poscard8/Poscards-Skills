package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import javax.annotation.ParametersAreNonnullByDefault;

import static github.poscard8.poscardsskills.PoscardsSkills.asResource;

/**
 * Advancement trigger for xp gain. {@link TriggerInstance#xp} specifies
 * the <b>total xp</b>, not the xp gained during the trigger.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GainXPTrigger extends SimpleCriterionTrigger<GainXPTrigger.TriggerInstance> {

    public static final ResourceLocation ID = asResource("gain_xp");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext ctx) {

        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        return new TriggerInstance(predicate, xp);
    }

    public void trigger(ServerPlayer player, int totalXP) { trigger(player, triggerInstance -> triggerInstance.matches(totalXP)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        final int xp;

        public TriggerInstance(EntityPredicate.Composite predicate, int xp) {

            super(ID, predicate);
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
