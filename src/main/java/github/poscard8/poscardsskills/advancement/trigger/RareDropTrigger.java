package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class RareDropTrigger extends SimpleCriterionTrigger<RareDropTrigger.TriggerInstance> {

    public static final ResourceLocation ID = PoscardsSkills.asResource("rare_drop");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite player, DeserializationContext ctx) {

        boolean hasType = jsonObject.has("type");

        if (hasType) {

            String string = GsonHelper.getAsString(jsonObject, "type");
            ResourceLocation typeKey = ResourceLocation.tryParse(string);

            return new TriggerInstance(player, typeKey);

        } else return new TriggerInstance(player);
    }

    public void trigger(ServerPlayer serverPlayer, ResourceLocation typeKey) { trigger(serverPlayer, triggerInstance -> triggerInstance.matches(typeKey)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private static final Predicate<ResourceLocation> ALWAYS_TRUE = resourceLocation -> true;

        private final Predicate<ResourceLocation> typePredicate;
        private final @Nullable ResourceLocation typeKey;

        public TriggerInstance(EntityPredicate.Composite player) {

            super(ID, player);
            this.typePredicate = ALWAYS_TRUE;
            this.typeKey = null;
        }

        public TriggerInstance(EntityPredicate.Composite player, ResourceLocation typeKey) {

            super(ID, player);
            this.typePredicate = resourceLocation -> resourceLocation.equals(typeKey);
            this.typeKey = typeKey;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);
            if (typeKey != null) jsonObject.addProperty("type", typeKey.toString());

            return jsonObject;
        }

        public boolean matches(ResourceLocation typeKey) { return typePredicate.test(typeKey); }

    }

}
