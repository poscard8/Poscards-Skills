package github.poscard8.poscardsskills.event;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.command.PoscardsSkillsCommand;
import github.poscard8.poscardsskills.enchantment.DominanceEnchantment;
import github.poscard8.poscardsskills.enchantment.TrueEfficiencyEnchantment;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourceData;
import github.poscard8.poscardsskills.experiencesource.types.*;
import github.poscard8.poscardsskills.registry.PSAttributes;
import github.poscard8.poscardsskills.secret.Secrets;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.misc.ItemLock;
import github.poscard8.poscardsskills.ui.menu.PoscardsSkillsMenu;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import github.poscard8.poscardsskills.util.event.EventOptimizer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static github.poscard8.poscardsskills.util.PSUtils.getServerPlayer;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class CommonEvents {

    /**
     * Loaders for skills, experience sources, and extensions.
     */
    @SubscribeEvent
    static void addReloadListeners(AddReloadListenerEvent event) {

        event.addListener(PoscardsSkills.getSkillHandler());
        event.addListener(PoscardsSkills.getXPSourceHandler());
        event.addListener(PoscardsSkills.getExtensionHandler());
    }

    @SubscribeEvent
    static void registerCommands(RegisterCommandsEvent event) {

        PoscardsSkillsCommand.register(event.getDispatcher());
    }

    /**
     * Opens the main menu.
     */
    @SubscribeEvent
    static void handleKeyPress(TickEvent.PlayerTickEvent event) {

        if (!(event.player instanceof ServerPlayer serverPlayer)) return;
        while (PoscardsSkills.KEY_POSCARDS_SKILLS_MENU.consumeClick()) serverPlayer.openMenu(PoscardsSkillsMenu.PROVIDER);
    }

    /**
     * Captures the server. This is used in many different ways.
     */
    @SubscribeEvent
    static void onServerStart(ServerAboutToStartEvent event) {

        MinecraftServer server = event.getServer();

        PSUtils.setServer(server);
        SkillData.getOrCreateFile(server);
        ExperienceSourceData.getOrCreateFile(server);
    }

    /**
     * Loads the skill data of the player.
     */
    @SubscribeEvent
    static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.getEntity() instanceof ServerPlayer player) {

            if (PSUtils.getServer() == null) PSUtils.setServer(player.getServer());
            SkillData.of(player).update();
        }
    }

    /**
     * Prevents the player from interacting if the item the player is holding is locked.
     */
    @SubscribeEvent
    static void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.isCancelable()) return;

        ServerPlayer player = getServerPlayer(event.getEntity());
        ItemStack stack = event.getItemStack();

        if (player == null || event.getSide().isClient()) return;

        if (ItemLock.isItemLockedFor(player, stack)) {

            ItemLock requisite = ItemLock.getRequisitesFor(stack).get(0);

            player.displayClientMessage(PSComponents.itemRequisite(requisite), true);
            event.setCanceled(true);
        }
    }

    /**
     * Prevents the player from attacking if the item the player is holding is locked.
     */
    @SubscribeEvent
    static void onPlayerAttack(AttackEntityEvent event) {

        ServerPlayer player = getServerPlayer(event.getEntity());
        ItemStack stack = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);

        if (ItemLock.isItemLockedFor(player, stack)) {

            ItemLock requisite = ItemLock.getRequisitesFor(stack).get(0);

            player.displayClientMessage(PSComponents.itemRequisite(requisite), true);
            event.setCanceled(true);
        }
    }

    /**
     * Trigger for Critical Edge.
     */
    @SubscribeEvent
    static void onCriticalHit(CriticalHitEvent event) {

        Player player = event.getEntity();

        double critDamage = player.getAttributeValue(PSAttributes.CRIT_DAMAGE.get());
        double modifier = 1 + critDamage / 100;

        event.setDamageModifier((float) modifier);

        if (!event.isVanillaCritical()) {

            double exploitation = PSUtils.getExploitationValue(player);
            if (PSUtils.randomFloat() < exploitation) event.setResult(Event.Result.ALLOW);
        }
    }

    /**
     * Trigger for {@link EntityExperienceSource}.
     */
    @SubscribeEvent
    static void onEntityDeath(LivingDeathEvent event) {

        Entity killer = event.getSource().getEntity();
        Entity entity = event.getEntity();
        if (killer instanceof ServerPlayer player) EntityExperienceSource.handlePlayer(player, entity);
    }

    /**
     * Trigger for Vitality.
     */
    @SubscribeEvent
    static void onEntityHeal(LivingHealEvent event) {

        LivingEntity entity = event.getEntity();
        float originalAmount = event.getAmount();
        float boost = (float) (PSUtils.getVitalityValue(entity) * originalAmount);
        float boosted = originalAmount + boost;

        event.setAmount(boosted);
    }

    /**
     * Trigger for Dominance.
     */
    @SubscribeEvent
    static void onEntityTargetChange(LivingChangeTargetEvent event) {

        LivingEntity entity = event.getEntity();
        LivingEntity target = event.getOriginalTarget();

        if (entity instanceof Mob mob && target instanceof Player player) {

            if (DominanceEnchantment.shouldCancelTargeting(mob, player)) event.setNewTarget(null);
        }
    }

    /**
     * Trigger for {@link BlockExperienceSource}.
     */
    @SubscribeEvent
    static void onBlockBreak(BlockEvent.BreakEvent event) {

        Player player = event.getPlayer();
        BlockPos position = event.getPos();
        BlockState state = event.getState();

        if (player instanceof ServerPlayer serverPlayer) {

            int count = TrueEfficiencyEnchantment.breakAdjacentBlocks(player, position, state);
            BlockExperienceSource.handleBreak(serverPlayer, state, count);
            Secrets.handleBlock(serverPlayer, state);
        }
    }

    /**
     * Trigger for {@link BlockExperienceSource}. Debt is added to the experience
     * source when the player places blocks. If the player removes those blocks, debt is removed.
     * <p>Players cannot gain experience from a source if they have debt.
     */
    @SubscribeEvent
    static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {

        Entity placer = event.getEntity();
        BlockState state = event.getState();
        if (placer instanceof ServerPlayer player) BlockExperienceSource.handlePlace(player, state);
    }

    /**
     * Tick handler.<p>
     * {@link StructureExperienceSource} is triggered once every 20 ticks (once per second)
     * to save performance.
     */
    @SubscribeEvent
    static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.side == LogicalSide.CLIENT) return;
        ServerPlayer serverPlayer = getServerPlayer(event.player);

        if (serverPlayer != null) {

            ItemStack offHand = event.player.getOffhandItem();
            PSCriteriaTriggers.CARRY_BRILLIANT_SHARD.trigger(serverPlayer, offHand);
            PSCriteriaTriggers.ASCENSION.trigger(serverPlayer);
            PSCriteriaTriggers.SECRET.trigger(serverPlayer);
        }

        if (EventOptimizer.handle("onPlayerTick", 20)) return; // to save performance

        StructureExperienceSource.handlePlayer(serverPlayer);
    }

    /**
     * Trigger for {@link AdvancementExperienceSource}.
     */
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    static void onAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ResourceLocation advancement = event.getAdvancement().getId();

        AdvancementExperienceSource.handlePlayer(player, advancement);
        Secrets.handleAdvancement(player, advancement);
    }

    /**
     * On experience sources related to container blocks, xp is given after
     * the player closes the container (rather than instantly) to avoid confusion.
     */
    @SubscribeEvent
    static void onContainerClose(PlayerContainerEvent.Close event) {

        @Nullable ServerPlayer player = getServerPlayer(event.getEntity()) ;

        CraftExperienceSource.handlePlayer(player);
        EnchantingTableExperienceSource.handlePlayer(player);
        ChestExperienceSource.handlePlayer(player);
        SmeltExperienceSource.handlePlayer(player);
        AnvilEnchantExperienceSource.handlePlayer(player);
    }

    /**
     * Trigger for {@link AnvilEnchantExperienceSource}.
     */
    @SubscribeEvent
    static void onAnvilRepair(AnvilRepairEvent event) {

        ItemStack output = event.getOutput();

        if (EventOptimizer.handle("onAnvilRepair", 2)) return; // for some reason the AnvilRepairEvent is fired twice

        ServerPlayer player = getServerPlayer(event.getEntity());
        ItemStack right = event.getRight();

        if (ExperienceSource.canGainXP(player)) AnvilEnchantExperienceSource.addWaitingXP(player, right);
    }

    /**
     * Trigger for {@link CraftExperienceSource}.
     */
    @SubscribeEvent
    static void onItemCraft(PlayerEvent.ItemCraftedEvent event) {

        ServerPlayer player = getServerPlayer(event.getEntity());
        if (ExperienceSource.canGainXP(player)) CraftExperienceSource.addWaitingItem(player, event.getCrafting());
    }

    /**
     * Trigger for {@link SmeltExperienceSource}.
     */
    @SubscribeEvent
    static void onItemSmelt(PlayerEvent.ItemSmeltedEvent event) {

        ServerPlayer player = getServerPlayer(event.getEntity());
        if (ExperienceSource.canGainXP(player)) SmeltExperienceSource.addWaitingItem(player, event.getSmelting());
    }

    /**
     * Trigger for {@link FishExperienceSource}.
     */
    @SubscribeEvent
    static void onItemFish(ItemFishedEvent event) {

        ServerPlayer player = getServerPlayer(event.getEntity());
        for (ItemStack stack : event.getDrops()) FishExperienceSource.handlePlayer(player, stack);
    }

    /**
     * Trigger for {@link ConsumeExperienceSource}.
     */
    @SubscribeEvent
    static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {

        if (event.getEntity() instanceof ServerPlayer player) ConsumeExperienceSource.handlePlayer(player, event.getItem());
    }

    /**
     * Method to add requisite tooltips to locked items.
     */
    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {

        ServerPlayer player = getServerPlayer();
        if (player == null) return;

        ItemStack stack = event.getItemStack();
        List<Component> components = event.getToolTip();

        List<ItemLock> requisites = ItemLock.getRequisitesFor(stack);
        if (requisites.isEmpty()) return;

        List<Component> requisiteComponents = new ArrayList<>();

        for (ItemLock requisite : requisites) {

            if (!requisite.test(player, stack)) requisiteComponents.add(PSComponents.requisite(requisite));
        }

        if (!requisiteComponents.isEmpty()) requisiteComponents.add(0, Component.empty());

        components.addAll(requisiteComponents);
    }

}
