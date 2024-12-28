package github.poscard8.poscardsskills.mixin;

import github.poscard8.poscardsskills.experiencesource.types.ChestExperienceSource;
import github.poscard8.poscardsskills.registry.PSAttributes;
import github.poscard8.poscardsskills.registry.PSSoundEvents;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
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

/**
 * Trigger for {@link ChestExperienceSource} and enables chest luck.
 * <p>+7 Chest Luck: %7 chance to get double the loot.</p>
 * <p>+111 Chest Luck: %11 chance to get triple the loot, %89 chance to get double the loot.</p>
 */
@SuppressWarnings("ALL")
@Mixin(RandomizableContainerBlockEntity.class)
public abstract class RandomizableContainerBlockEntityMixin {

    private static final String TARGET = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V";
    private static final Random RANDOM = new Random();

    RandomizableContainerBlockEntity self = (RandomizableContainerBlockEntity) (Object) this;

    @Inject(method = "unpackLootTable", at = @At(value = "INVOKE", target = TARGET, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    void poscardsskills$unpackLootTable(@Nullable Player player, CallbackInfo ci, LootTable lootTable, LootParams.Builder lootParams$builder) {

        @Nullable ServerPlayer serverPlayer = PSUtils.getServerPlayer(player);

        if (serverPlayer != null) {

            ChestExperienceSource.setWaitingLootTable(serverPlayer, lootTable.getLootTableId());

            ItemStack mainHand = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            LootParams lootParams = lootParams$builder.withLuck(serverPlayer.getLuck()).withParameter(LootContextParams.THIS_ENTITY, serverPlayer).create(LootContextParamSets.CHEST);

            int chestLuck = (int) serverPlayer.getAttribute(PSAttributes.CHEST_LUCK.get()).getValue();
            int setRolls = chestLuck / 100;
            int extraRollChance = chestLuck % 100;
            int rolls = setRolls + 1;

            for (int i = 0; i < setRolls; i++) { lootTable.fill(self, lootParams, RANDOM.nextLong()); }

            int randomInt = RANDOM.nextInt(100);

            if (randomInt < extraRollChance) {

                lootTable.fill(self, lootParams, RANDOM.nextLong());
                rolls++;
            }

            if (rolls > 1) {

                serverPlayer.displayClientMessage(PSComponents.chestLuck(rolls), false);
                PSUtils.playLocalSound(serverPlayer, PSSoundEvents.UNLOCK_SECRET);
            }
        }
    }

}
