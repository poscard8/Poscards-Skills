package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.util.component.AnimatedColor;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("ALL")
@Mixin(Style.class)
public abstract class StyleMixin {

    private Style self = (Style) (Object) this;

    @Inject(method = "getColor", at = @At("TAIL"), cancellable = true)
    private void poscardsskills$getColor(CallbackInfoReturnable<TextColor> ci) {

        Optional<AnimatedColor> optional = AnimatedColor.ofStyle(self);
        if (optional.isPresent()) ci.setReturnValue(optional.get().getColor());
    }

}
