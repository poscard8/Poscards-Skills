package github.poscard8.poscardsskills.event;

import com.google.common.collect.Multimap;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.command.PoscardsSkillsCommand;
import github.poscard8.poscardsskills.command.XPSourceCommand;
import github.poscard8.poscardsskills.experiencesource.types.*;
import github.poscard8.poscardsskills.item.BrilliantBookItem;
import github.poscard8.poscardsskills.module.PSModules;
import github.poscard8.poscardsskills.skill.misc.ItemRequisite;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.ui.menu.PoscardsSkillsMenu;
import github.poscard8.poscardsskills.util.PSTags;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import github.poscard8.poscardsskills.util.event.EventOptimizer;
import github.poscard8.poscardsskills.util.item.BrilliantGearUtils;
import github.poscard8.poscardsskills.util.item.PSItemUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.*;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;


@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PoscardsSkills.ID)
public final class CommonEvents {

    private CommonEvents() {}

    @SubscribeEvent
    static void addReloadListeners(AddReloadListenerEvent event) {

        event.addListener(PoscardsSkills.getSkillHandler());
        event.addListener(PoscardsSkills.getXPSourceHandler());
    }

    @SubscribeEvent
    static void registerCommands(RegisterCommandsEvent event) {

        PoscardsSkillsCommand.register(event.getDispatcher());
        XPSourceCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    static void handleKeyPress(TickEvent.PlayerTickEvent event) {

        if (!(event.player instanceof ServerPlayer serverPlayer)) return;
        while (PoscardsSkills.KEY_SKILL_MENU.consumeClick()) serverPlayer.openMenu(PoscardsSkillsMenu.PROVIDER);
    }

    @SubscribeEvent
    static void onServerStart(ServerAboutToStartEvent event) {

        PSUtils.setServer(event.getServer());
    }

    @SubscribeEvent
    static void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (ItemRequisite.isLockedFor(player, stack)) {

            ItemRequisite requisite = ItemRequisite.getRequisitesFor(stack).get(0);

            player.displayClientMessage(PSComponents.itemRequisite(requisite), true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void onPlayerAttack(AttackEntityEvent event) {

        Player player = event.getEntity();
        ItemStack stack = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);

        if (ItemRequisite.isLockedFor(player, stack)) {

            ItemRequisite requisite = ItemRequisite.getRequisitesFor(stack).get(0);

            player.displayClientMessage(PSComponents.itemRequisite(requisite), true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void onEntityDeath(LivingDeathEvent event) {

        Entity killer = event.getSource().getEntity();
        Entity entity = event.getEntity();
        if (killer instanceof Player player) KillEntityExperienceSource.handlePlayer(player, entity);
    }

    @SubscribeEvent
    static void onBlockBreak(BlockEvent.BreakEvent event) {

        Player player = event.getPlayer();
        BlockState state = event.getState();
        BreakBlockExperienceSource.handleBreak(player, state);
    }

    @SubscribeEvent
    static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {

        Entity placer = event.getEntity();
        BlockState state = event.getState();
        if (placer instanceof Player player) BreakBlockExperienceSource.handlePlace(player, state);
    }

    @SubscribeEvent
    static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        ServerPlayer serverPlayer = PSUtils.getServerPlayer(event.player);

        if (serverPlayer != null) {

            if (serverPlayer.tickCount == 1) SkillData.of(serverPlayer).update();

            ItemStack offHand = event.player.getOffhandItem();
            PSCriteriaTriggers.CARRY_BRILLIANT_SHARD.trigger(serverPlayer, offHand);
        }

        if (EventOptimizer.handle("onPlayerTick", 20)) return; // to save performance

        VisitStructureExperienceSource.handlePlayer(serverPlayer);
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    static void onAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {

        Player player = event.getEntity();
        ResourceLocation advancement = event.getAdvancement().getId();
        UnlockAdvancementExperienceSource.handlePlayer(player, advancement);
    }

    @SubscribeEvent
    static void onContainerClose(PlayerContainerEvent.Close event) {

        Player player = event.getEntity();

        BrewPotionExperienceSource.handlePlayer(player);
        CraftItemExperienceSource.handlePlayer(player);
        EnchantItemExperienceSource.handlePlayer(player);
        OpenChestExperienceSource.handlePlayer(player);
        SmeltItemExperienceSource.handlePlayer(player);
        UseAnvilExperienceSource.handlePlayer(player);
    }

    @SubscribeEvent
    static void onPotionBrew(PlayerBrewedPotionEvent event) {

        ItemStack potion = event.getStack();
        if (potion.is(Items.AIR)) return;

        BrewPotionExperienceSource.addWaitingXP(event.getEntity(), potion);
        BrewPotionExperienceSource.setIngredientCount(potion, 0);
    }

    @SubscribeEvent
    static void onAnvilRepair(AnvilRepairEvent event) {

        ItemStack output = event.getOutput();
        if (output.hasTag()) {

            CompoundTag tag = output.getOrCreateTag();
            tag.putInt("HideFlags", (tag.getInt("HideFlags") / 2) * 2);
            tag.remove("pendingEnchantments");
            PSItemUtils.removeText(output);
            output.setTag(tag.copy());
        }

        if (EventOptimizer.handle("onAnvilRepair", 2)) return; // for some reason the AnvilRepairEvent is fired twice

        Player player = event.getEntity();
        ItemStack right = event.getRight();
        UseAnvilExperienceSource.addWaitingXP(player, right);
    }

    @SubscribeEvent
    static void onAnvilUpdate(AnvilUpdateEvent event) {

        if (event.getRight().getItem() instanceof BrilliantBookItem && BrilliantBookItem.canApplyTo(event.getLeft())) {

            ItemStack output = BrilliantBookItem.applyRandom(event.getLeft());

            event.setCost(5);
            event.setOutput(output);
        }
    }

    @SubscribeEvent
    static void onItemCraft(PlayerEvent.ItemCraftedEvent event) {

        Player player = event.getEntity();
        CraftItemExperienceSource.addWaitingItem(player, event.getCrafting());
    }

    @SubscribeEvent
    static void onItemSmelt(PlayerEvent.ItemSmeltedEvent event) {

        Player player = event.getEntity();
        SmeltItemExperienceSource.addWaitingItem(player, event.getSmelting());
    }

    @SubscribeEvent
    static void onItemFish(ItemFishedEvent event) {

        Player player = event.getEntity();
        for (ItemStack stack : event.getDrops()) FishExperienceSource.handlePlayer(player, stack);
    }


    @SubscribeEvent
    static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {

        if (event.getEntity() instanceof Player player) ConsumeItemExperienceSource.handlePlayer(player, event.getItem());
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        List<Component> components = event.getToolTip();

        // add components to brilliant gear so that I don't need 9 new Item classes
        if (stack.is(PSTags.Items.BRILLIANT_GEAR)) components.addAll(1, BrilliantGearUtils.getComponents(stack));

        List<ItemRequisite> requisites = ItemRequisite.getRequisitesFor(stack);
        if (requisites.size() == 0) return;

        List<Component> requisiteComponents = new ArrayList<>();

        for (ItemRequisite requisite : requisites) {

            if (!requisite.test(player, stack)) requisiteComponents.add(PSComponents.requisite(requisite));
        }

        if (requisiteComponents.size() > 0) requisiteComponents.add(0, Component.empty());

        components.addAll(requisiteComponents);
    }

    @SubscribeEvent
    static void onAttributeModify(ItemAttributeModifierEvent event) {

        ItemStack stack = event.getItemStack();

        if (stack.is(PSTags.Items.BRILLIANT_GEAR)) {

            Optional<AttributeModifier> optional = event.getModifiers().values().stream().findFirst();

            if (optional.isPresent()) {

                UUID uuid = optional.get().getId();
                Multimap<Attribute, AttributeModifier> brilliantModifiers = BrilliantGearUtils.getAttributeModifiers(stack, uuid);

                for (Attribute attribute : brilliantModifiers.keys()) {

                    Collection<AttributeModifier> modifiers = brilliantModifiers.get(attribute);
                    for (AttributeModifier modifier : modifiers) event.addModifier(attribute, modifier);
                }
            }
        }
    }


}
