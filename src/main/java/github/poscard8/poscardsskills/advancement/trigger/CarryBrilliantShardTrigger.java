package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.item.BrilliantShardItem;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class CarryBrilliantShardTrigger extends SimpleCriterionTrigger<CarryBrilliantShardTrigger.TriggerInstance> {

    public static final ResourceLocation ID = PoscardsSkills.asResource("carry_brilliant_shard");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite player, DeserializationContext ctx) {

        int count = GsonHelper.getAsInt(jsonObject, "count");
        return new CarryBrilliantShardTrigger.TriggerInstance(player, count);
    }

    public void trigger(ServerPlayer serverPlayer, ItemStack stack) { trigger(serverPlayer, triggerInstance -> triggerInstance.matches(stack)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final int count;

        public TriggerInstance(EntityPredicate.Composite player, int count) {

            super(ID, player);
            this.count = count;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);
            jsonObject.addProperty("count", count);
            return jsonObject;
        }

        public boolean matches(ItemStack stack) { return stack.getItem() instanceof BrilliantShardItem && stack.getCount() >= count; }

    }

}
