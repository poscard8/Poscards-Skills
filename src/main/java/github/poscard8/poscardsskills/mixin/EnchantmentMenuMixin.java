package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.types.EnchantingTableExperienceSource;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Trigger for {@link EnchantingTableExperienceSource}.
 */
@SuppressWarnings("ALL")
@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

    EnchantmentMenu self = (EnchantmentMenu) (Object) this;

    @Inject(method = "clickMenuButton", at = @At("RETURN"))
    void poscardsskills$clickMenuButton(Player player, int index, CallbackInfoReturnable<Boolean> ci) {

        @Nullable ServerPlayer serverPlayer = PSUtils.getServerPlayer(player);
        if (ci.getReturnValue() && ExperienceSource.canGainXP(player)) EnchantingTableExperienceSource.addWaitingXP(serverPlayer, self.costs[index]);
    }

}
