package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.item.RuneItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Enabling custom rarities. See {@link github.poscard8.poscardsskills.util.item.PSRarities}.
 */
@SuppressWarnings("ALL")
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    ItemStack self = (ItemStack) (Object) this;

    @Inject(method = "getRarity", at = @At("TAIL"), cancellable = true)
    void poscardsskills$getRarity(CallbackInfoReturnable<Rarity> ci) {

        if (!self.hasTag()) return;
        if (!self.getOrCreateTag().contains(RuneItem.NBT_KEY)) return;

        String name = self.getOrCreateTag().getString(RuneItem.NBT_KEY);
        Optional<Rarity> optional = rarityByName(name);
        optional.ifPresent(rarity -> ci.setReturnValue(rarity));
    }

    Optional<Rarity> rarityByName(String name) {

        for (Rarity rarity : Rarity.values()) {

            if (name.equals(rarity.name())) return Optional.of(rarity);
        }
        return Optional.empty();
    }

}
