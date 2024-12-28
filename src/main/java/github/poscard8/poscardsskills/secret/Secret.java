package github.poscard8.poscardsskills.secret;

import github.poscard8.poscardsskills.registry.PSItems;
import github.poscard8.poscardsskills.registry.PSSoundEvents;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

/**
 * Secrets are intangible things unlocked by players.
 * They are one of 2 ways to obtain brilliant shards.
 * <p>{@code weight} determines the amount of brilliant shards.</p>
 * <p>{@link #getDisplayItem()} is used for JEI compat.</p>
 */
public class Secret {

    public final int weight;

    public Secret(int weight) { this.weight = weight; }

    protected static float randomFloat() { return new Random().nextFloat(); }

    public ItemStack getRewardItem() { return new ItemStack(PSItems.BRILLIANT_SHARD.get(), weight); }

    public ItemStack getDisplayItem() {

        ItemStack stack = PSItems.SECRET.get().getDefaultInstance();
        stack.setHoverName(PSComponents.secretName(this));
        return stack;
    }

    public ResourceLocation getKey() { return Secrets.keyOf(this); }

    public int getIndex() { return Secrets.indexOf(this); }

    public boolean isRegistered() { return Secrets.getValues().contains(this); }

    public boolean isUnlockedFor(ServerPlayer player) { return SecretData.of(player).isUnlocked(this); }

    public boolean isLockedFor(ServerPlayer player) { return !isUnlockedFor(player); }

    public final void unlock(ServerPlayer player) { unlock(player, true); }

    public final void unlock(ServerPlayer player, boolean manually) {

        if (isLockedFor(player)) {

            SecretData.of(player).setUnlocked(player, this, true);
            onUnlock(player, manually);
        }
    }

    public final void lock(ServerPlayer player) {

        if (isUnlockedFor(player)) {

            SecretData.of(player).setUnlocked(player, this, false);
            onLock(player);
        }
    }

    public void onUnlock(ServerPlayer player, boolean manually) {

        List<Component> components = PSComponents.secretComponents(SecretData.of(player), this, manually);
        components.forEach(component -> player.displayClientMessage(component, false));

        if (manually) {

            addItemToPlayer(player);
            PSUtils.playLocalSound(player, PSSoundEvents.UNLOCK_SECRET);
        }
    }

    @SuppressWarnings("unused")
    public void onLock(ServerPlayer player) {
    }

    public void addItemToPlayer(ServerPlayer player) { player.getInventory().placeItemBackInInventory(getRewardItem()); }


}
