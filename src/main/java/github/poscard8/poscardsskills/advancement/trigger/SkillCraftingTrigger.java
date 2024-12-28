package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static github.poscard8.poscardsskills.PoscardsSkills.asResource;

/**
 * Advancement trigger for skill crafting. {@link TriggerInstance#itemPredicate} specifies the output item.
 * If {@link TriggerInstance#itemPredicate} is empty, any skill recipe will trigger.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkillCraftingTrigger extends SimpleCriterionTrigger<SkillCraftingTrigger.TriggerInstance> {

    public static final ResourceLocation ID = asResource("skill_crafting");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext ctx) {

        if (!jsonObject.has("item")) return new TriggerInstance(predicate, null);

        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(predicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, ItemStack output) { trigger(player, triggerInstance -> triggerInstance.matches(output)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        @Nullable
        final ItemPredicate itemPredicate;

        public TriggerInstance(ContextAwarePredicate predicate, @Nullable ItemPredicate itemPredicate) {

            super(ID, predicate);
            this.itemPredicate = itemPredicate;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);

            if (itemPredicate != null) jsonObject.add("item", itemPredicate.serializeToJson());
            return jsonObject;
        }

        public boolean matches(ItemStack stack) { return itemPredicate == null || itemPredicate.matches(stack); }

    }

}
