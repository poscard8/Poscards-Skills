package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.experiencesource.types.OpenChestExperienceSource;
import github.poscard8.poscardsskills.item.BrilliantKeyItem;
import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.module.BrilliantUtilitiesModule;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@SuppressWarnings("ALL")
@Mixin(RandomizableContainerBlockEntity.class)
public abstract class RandomizableContainerBlockEntityMixin {

    private static final String TARGET = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootContext;)V";

    private RandomizableContainerBlockEntity self = (RandomizableContainerBlockEntity) (Object) this;

    @Inject(method = "unpackLootTable", at = @At(value = "INVOKE", target = TARGET, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void poscardsskills$unpackLootTable(@Nullable Player player, CallbackInfo ci, LootTable lootTable, LootContext.Builder lootcontext$builder) {

        if (player != null) {

            OpenChestExperienceSource.setWaitingLootTable(player, lootTable.getLootTableId());

            ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (mainHand.getItem() instanceof BrilliantKeyItem) {

                if (!player.isCreative() && !player.isSpectator()) mainHand.shrink(1);

                LocalPlayer localPlayer = PSUtils.getLocalPlayer(player);
                if (localPlayer != null) localPlayer.playSound(BrilliantUtilitiesModule.SoundEvents.KEY_USE.get());

                PSCriteriaTriggers.USE_KEY.trigger((ServerPlayer) player);
            }

            LootContext lootContext = lootcontext$builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.CHEST);

            int chestLuck = (int) player.getAttribute(BaseModule.Attributes.CHEST_LUCK.get()).getValue();
            int setRolls = chestLuck / 100;
            int extraRollChance = chestLuck % 100;

            for (int i = 0; i < setRolls; i++) lootTable.fill(self, lootContext);

            int randomInt = new Random().nextInt(100);
            if (randomInt < extraRollChance) lootTable.fill(self, lootContext);
        }
    }

}
