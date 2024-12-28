package github.poscard8.poscardsskills.advancement.trigger;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.secret.Secret;
import github.poscard8.poscardsskills.secret.SecretData;
import github.poscard8.poscardsskills.secret.Secrets;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

import static github.poscard8.poscardsskills.PoscardsSkills.asResource;

/**
 * Advancement trigger for unlocking secrets. {@link TriggerInstance#key} specifies the key of the secret.
 * <p>If the key is passed as {@code "all"} the final secret will trigger.</p>
 * <p>If the key is passed as {@code "any"} any secret will trigger.</p>
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SecretTrigger extends SimpleCriterionTrigger<SecretTrigger.TriggerInstance> {

    public static final ResourceLocation ID = asResource("secret");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext ctx) {

        if (jsonObject.has("key")) {

            String string = jsonObject.get("key").getAsString();

            if (string.equals("all")) {

                return new TriggerInstance(predicate, new ResourceLocation(""), true, false);

            } else if (string.equals("any")) {

                return new TriggerInstance(predicate, new ResourceLocation(""), false, true);

            } else {

                ResourceLocation key = ResourceLocation.tryParse(string);
                if (key == null) throw new RuntimeException("Invalid secret key.");

                return new TriggerInstance(predicate, key, false, false);
            }

        } else throw new RuntimeException("Secret trigger does not have argument 'key'.");
    }

    public void trigger(ServerPlayer player) { trigger(player, triggerInstance -> triggerInstance.matches(SecretData.of(player))); }

    @Override
    public ResourceLocation getId() { return ID; }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        final ResourceLocation key;
        final boolean all;
        final boolean any;

        public TriggerInstance(EntityPredicate.Composite predicate, ResourceLocation key, boolean all, boolean any) {

            super(ID, predicate);
            this.key = key;
            this.all = all;
            this.any = any;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext ctx) {

            JsonObject jsonObject = super.serializeToJson(ctx);
            jsonObject.addProperty("key", key.toString());

            return jsonObject;
        }

        public boolean matches(SecretData secretData) {

            if (all) return secretData.getSecretCount() == Secrets.getTotalCount();
            if (any) return secretData.getSecretCount() >= 1;

            Secret secret = Secrets.byKey(key);
            return secret != null && secretData.isUnlocked(secret);
        }

    }

}
