package github.poscard8.poscardsskills.secret;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import github.poscard8.poscardsskills.registry.PSAttributes;
import github.poscard8.poscardsskills.skill.SkillData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SecretData {

    public final List<Secret> unlocked;

    public SecretData() { this(new ArrayList<>()); }

    public SecretData(List<Secret> unlocked) { this.unlocked = unlocked; }

    public static SecretData of(ServerPlayer player) { return SkillData.of(player).secretData; }

    public static SecretData deserialize(JsonArray serialized) {

        List<Secret> unlocked = new ArrayList<>();

        for (JsonElement element : serialized) {

            if (element.isJsonPrimitive()) {

                String string = element.getAsString();
                ResourceLocation key = ResourceLocation.tryParse(string);

                if (key != null && Secrets.byKey(key) != null) unlocked.add(Secrets.byKey(key));
            }
        }

        return new SecretData(unlocked);
    }

    public JsonArray serialize() {

        JsonArray array = new JsonArray();

        for (Secret secret : unlocked) {

            ResourceLocation key = secret.getKey();
            if (key != null)  array.add(key.toString());
        }
        return array;
    }

    public SecretData copy() { return new SecretData(new ArrayList<>(unlocked)); }

    public SecretData reset(ServerPlayer player) {

        unlocked.clear();
        updateSkillData(player);
        return this;
    }

    public SecretData maxOut(ServerPlayer player) {

        unlocked.clear();
        unlocked.addAll(Secrets.getValues());
        updateSkillData(player);
        return this;
    }

    public Map.Entry<Attribute, AttributeModifier> getAttributeModifier() {

        return Map.entry(PSAttributes.LEGACY.get(), new AttributeModifier(UUID.fromString("CF2F55D3-347A-4F38-8497-9C13AFFDB531"), () -> "Poscard's Skills: Secret modifier", getSecretCount(), AttributeModifier.Operation.ADDITION));
    }

    public int getSecretCount() { return unlocked.size(); }

    public boolean isUnlocked(Secret secret) { return unlocked.contains(secret); }

    public void unlock(ServerPlayer player, Secret secret, boolean manually) { if (secret.isRegistered()) secret.unlock(player, manually); }

    public void lock(ServerPlayer player, Secret secret) { if (secret.isRegistered()) secret.lock(player); }

    /**
     * This method is for updating the data, not for unlocking a secret. To unlock a secret, see {@link #unlock(ServerPlayer, Secret, boolean)};
     */
    public void setUnlocked(ServerPlayer player, Secret secret, boolean unlock) {

        if (!secret.isRegistered()) return;

        if (unlock) { unlocked.add(secret); } else unlocked.remove(secret);
        updateSkillData(player);
    }

    public void updateSkillData(ServerPlayer player) { SkillData.of(player).updateSecrets(this); }

}
