package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SkillCraftingTrigger extends SimpleCriterionTrigger<SkillCraftingTrigger.TriggerInstance> {

    public static final ResourceLocation ID = PoscardsSkills.asResource("skill_crafting");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite player, DeserializationContext ctx) {

        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(player, itemPredicate);
    }

    public void trigger(ServerPlayer serverPlayer, ItemStack output) { trigger(serverPlayer, triggerInstance -> triggerInstance.matches(output)); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate itemPredicate;

        public TriggerInstance(EntityPredicate.Composite player, ItemPredicate itemPredicate) {

            super(ID, player);
            this.itemPredicate = itemPredicate;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);
            jsonObject.add("item", itemPredicate.serializeToJson());
            return jsonObject;
        }

        public boolean matches(ItemStack stack) { return itemPredicate.matches(stack); }

    }

}
