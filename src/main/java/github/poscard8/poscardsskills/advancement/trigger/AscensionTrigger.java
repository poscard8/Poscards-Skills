package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.skill.SkillData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import javax.annotation.ParametersAreNonnullByDefault;

import static github.poscard8.poscardsskills.PoscardsSkills.asResource;

/**
 * Advancement trigger for ascension. {@link TriggerInstance#count} specifies
 * the amount of ascensions the player has made.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AscensionTrigger extends SimpleCriterionTrigger<AscensionTrigger.TriggerInstance> {

    public static final ResourceLocation ID = asResource("ascension");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext ctx) {

        int count = GsonHelper.getAsInt(jsonObject, "count");
        return new TriggerInstance(predicate, count);
    }

    public void trigger(ServerPlayer player) { trigger(player, triggerInstance -> triggerInstance.matches(SkillData.of(player).ascensions)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        final int count;

        public TriggerInstance(ContextAwarePredicate predicate, int count) {

            super(ID, predicate);
            this.count = count;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);
            jsonObject.addProperty("count", count);
            return jsonObject;
        }

        public boolean matches(int ascensions) { return ascensions >= count; }

    }

}
