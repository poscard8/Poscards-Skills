package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.experiencesource.types.EnchantItemExperienceSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ALL")
@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

    @Shadow
    private int[] costs;

    @Inject(method = "clickMenuButton", at = @At("RETURN"))
    private void poscardsskills$clickMenuButton(Player player, int index, CallbackInfoReturnable<Boolean> ci) {

        if (ci.getReturnValue()) EnchantItemExperienceSource.addWaitingXP(player, costs[index]);
    }

}
