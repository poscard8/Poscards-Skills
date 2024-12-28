package github.poscard8.poscardsskills.secret.types;

import github.poscard8.poscardsskills.secret.Secret;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Secret unlocked via advancements.
 */
public class AdvancementSecret extends Secret {

    public final ResourceLocation advancementKey;

    public AdvancementSecret(ResourceLocation advancementKey, int weight) {

        super(weight);
        this.advancementKey = advancementKey;
    }

    public void tryUnlock(ServerPlayer player, ResourceLocation key) {

        if (advancementKey.equals(key)) unlock(player);
    }

}
