package github.poscard8.poscardsskills.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("ALL")
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    private ItemStack self = (ItemStack) (Object) this;

    @Inject(method = "getRarity", at = @At("TAIL"), cancellable = true)
    private void poscardsskills$getRarity(CallbackInfoReturnable<Rarity> ci) {

        if (!self.hasTag()) return;
        if (!self.getOrCreateTag().contains("customRarity")) return;

        String name = self.getOrCreateTag().getString("customRarity");
        Optional<Rarity> optional = rarityByName(name);

        if (optional.isEmpty()) return;
        ci.setReturnValue(optional.get());
    }

    private Optional<Rarity> rarityByName(String name) {

        for (Rarity rarity : Rarity.values()) {

            if (name.equals(rarity.name())) return Optional.of(rarity);
        }
        return Optional.empty();
    }

}
