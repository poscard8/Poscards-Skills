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
 * Advancement trigger for leveling up. {@link TriggerInstance#level}
 * specifies the new skill level.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LevelUpTrigger extends SimpleCriterionTrigger<LevelUpTrigger.TriggerInstance> {

    public static final ResourceLocation ID = asResource("level_up");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext ctx) {

        int level = GsonHelper.getAsInt(jsonObject, "level");
        return new TriggerInstance(predicate, level);
    }

    public void trigger(ServerPlayer player, int newLevel) { trigger(player, triggerInstance -> triggerInstance.matches(newLevel)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        final int level;

        public TriggerInstance(EntityPredicate.Composite predicate, int level) {

            super(ID, predicate);
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
