package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.experiencesource.types.BrewPotionExperienceSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ALL")
@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {

    @Inject(method = "getOutput", at = @At("RETURN"), cancellable = true, remap = false)
    private static void poscardsskills$getOutput(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> ci) {

        if (ci.getReturnValue().isEmpty()) return;

        int ingredientCount = BrewPotionExperienceSource.getIngredientCount(input);
        ItemStack newPotion = BrewPotionExperienceSource.setIngredientCount(ci.getReturnValue(), ingredientCount + 1);
        ci.setReturnValue(newPotion);
    }


}
