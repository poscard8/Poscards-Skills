package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.module.PSModules;
import github.poscard8.poscardsskills.util.PSTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ALL")
@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin {

    @Shadow
    private float speed;

    @Inject(method = "isCorrectToolForDrops(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("HEAD"), remap = false, cancellable = true)
    private void poscardsskills$isCorrectToolForDrops(ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> ci) {

        if (stack.getItem() instanceof PickaxeItem && PSModules.DECORATIVE_BLOCKS.isPresent()) {

            if (state.is(PSTags.Blocks.JADE_BLOCKS) || state.is(PSTags.Blocks.JASPER_BLOCKS) || state.is(PSTags.Blocks.MARBLE_BLOCKS)) {

                ci.setReturnValue(true);
            }
        }
    }

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void poscardsskills$getDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> ci) {

        if (stack.getItem() instanceof PickaxeItem && PSModules.DECORATIVE_BLOCKS.isPresent()) {

            if (state.is(PSTags.Blocks.JADE_BLOCKS) || state.is(PSTags.Blocks.JASPER_BLOCKS) || state.is(PSTags.Blocks.MARBLE_BLOCKS)) {

                ci.setReturnValue(speed);
            }
        }
    }

}
