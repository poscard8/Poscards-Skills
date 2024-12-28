package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.util.PSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

import static github.poscard8.poscardsskills.PoscardsSkills.asResource;

/**
 * Advancement trigger for carrying magic shards. {@link TriggerInstance#count} specifies
 * the amount of shards the player has to carry on their <b>offhand</b>.
 * See {@code "data/poscardsskills/tags/items/magic_shards.json"} for magic shard items.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarryMagicShardTrigger extends SimpleCriterionTrigger<CarryMagicShardTrigger.TriggerInstance> {

    public static final ResourceLocation ID = asResource("carry_magic_shard");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext ctx) {

        int count = GsonHelper.getAsInt(jsonObject, "count");
        return new TriggerInstance(predicate, count);
    }

    public void trigger(ServerPlayer player, ItemStack stack) { trigger(player, triggerInstance -> triggerInstance.matches(stack)); }

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

        public boolean matches(ItemStack stack) { return stack.is(PSTags.Items.MAGIC_SHARDS) && stack.getCount() >= count; }

    }

}
