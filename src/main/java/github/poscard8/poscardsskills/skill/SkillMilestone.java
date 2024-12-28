package github.poscard8.poscardsskills.skill;

import github.poscard8.poscardsskills.skill.reward.ItemReward;
import github.poscard8.poscardsskills.skill.reward.Reward;
import github.poscard8.poscardsskills.skill.reward.XpReward;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generated for every level of every skill.
 * <p>Has 3 States: {@code LOCKED, UNLOCKING, UNLOCKED}
 * The state is {@code UNLOCKING} when the skill instance
 * has a level of <i>n</i> and the milestone has a level of <i>n+1</i>.</p>
 */
public final class SkillMilestone {

    public final Skill skill;
    public final SkillInstance instance;
    public final int level;
    public final List<ItemStack> itemRewards;
    public final int xpReward;
    public final List<SkillRecipe> recipes;
    public State state;
    public boolean claimed;

    SkillMilestone(SkillInstance instance, int level) {

        this.skill = instance.skill;
        this.instance = instance;
        this.level = level;
        this.itemRewards = initItemRewards();
        this.xpReward = initXPReward();
        this.recipes = initRecipes();
        this.state = initState();
        this.claimed = instance.claimedRewards[level];
    }

    public boolean canClaimRewards() { return !claimed && isUnlocked(); }

    public void claimRewards(@Nullable ServerPlayer player) {

        if (player == null) return;
        if (!canClaimRewards()) return;

        Inventory inventory = player.getInventory();
        for (ItemStack stack : itemRewards) inventory.placeItemBackInInventory(stack.copy());
        if (hasXPReward()) player.giveExperiencePoints(xpReward);

        instance.claimRewards(player, level);
        claimed = true;
    }

    public boolean isUnlocked() { return state == State.UNLOCKED; }

    public boolean hasXPReward() { return xpReward > 0; }

    public boolean hasRecipes() { return !recipes.isEmpty(); }

    List<ItemStack> initItemRewards() {

        List<Reward> rewardList = skill.rewards;
        List<ItemStack> itemRewards = new ArrayList<>();

        rewardList.stream().filter(reward -> reward.isAvailableFor(level)).filter(reward -> reward instanceof ItemReward).forEach(reward -> itemRewards.add(((ItemReward) reward).stack));
        return itemRewards;
    }

    int initXPReward() {

        List<Reward> rewardList = skill.rewards;
        AtomicInteger total = new AtomicInteger();

        rewardList.stream().filter(reward -> reward.isAvailableFor(level)).filter(reward -> reward instanceof XpReward).forEach(reward -> total.addAndGet(((XpReward) reward).xp));
        return total.get();
    }

    List<SkillRecipe> initRecipes() {

        List<SkillRecipe> recipeList = skill.recipes;
        return recipeList.stream().filter(recipe -> recipe.at == level).collect(Collectors.toList());
    }

    State initState() {

        return instance.level + 1 == level ? State.UNLOCKING : instance.level >= level ? State.UNLOCKED : State.LOCKED;
    }

    /**
     * Different states display different UI texts.
     */
    public enum State {

        LOCKED(instance -> PSComponents.locked()),
        UNLOCKING(PSComponents::progressBar),
        UNLOCKED(instance -> PSComponents.unlocked());

        final Function<SkillInstance, Component> componentSupplier;

        State(Function<SkillInstance, Component> componentSupplier) { this.componentSupplier = componentSupplier; }

        public Component getComponent(SkillInstance instance) { return componentSupplier.apply(instance); }
    }

}
