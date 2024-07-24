package github.poscard8.poscardsskills.skill;

import github.poscard8.poscardsskills.skill.misc.Additional;
import github.poscard8.poscardsskills.skill.reward.ItemReward;
import github.poscard8.poscardsskills.skill.reward.Reward;
import github.poscard8.poscardsskills.skill.reward.XpReward;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SkillMilestone {

    public final Skill skill;
    public final SkillInstance instance;
    public final int level;
    public final List<ItemStack> itemRewards;
    public final int xpReward;
    public final List<SkillRecipe> recipes;
    public final List<Additional> additional;
    public State state;
    public boolean rewardsClaimed;
    public boolean canClaimRewards;

    SkillMilestone(SkillInstance instance, int level) {

        this.skill = instance.skill;
        this.instance = instance;
        this.level = level;
        this.itemRewards = initItemRewards();
        this.xpReward = initXPReward();
        this.recipes = initRecipes();
        this.additional = initAdditional();
        this.state = initState();
        this.rewardsClaimed = instance.claimedRewards[level];
        this.canClaimRewards = !rewardsClaimed && isUnlocked();
    }

    private List<ItemStack> initItemRewards() {

        List<Reward> rewardList = skill.rewards;
        List<ItemStack> itemRewards = new ArrayList<>();

        rewardList.stream().filter(reward -> reward.isAvailableFor(level)).filter(reward -> reward instanceof ItemReward).forEach(reward -> itemRewards.add(((ItemReward) reward).stack));
        return itemRewards;
    }

    private int initXPReward() {

        List<Reward> rewardList = skill.rewards;
        AtomicInteger total = new AtomicInteger();

        rewardList.stream().filter(reward -> reward.isAvailableFor(level)).filter(reward -> reward instanceof XpReward).forEach(reward -> total.addAndGet(((XpReward) reward).xp));
        return total.get();
    }

    private List<SkillRecipe> initRecipes() {

        List<SkillRecipe> recipeList = skill.recipes;
        return recipeList.stream().filter(recipe -> recipe.at == level).collect(Collectors.toList());
    }

    private List<Additional> initAdditional() {

        List<Additional> additionalList = skill.additional;
        return additionalList.stream().filter(additional1 -> additional1.at == level).collect(Collectors.toList());
    }

    private State initState() {

        return instance.level + 1 == level ? State.UNLOCKING : instance.level >= level ? State.UNLOCKED : State.LOCKED;
    }

    public void claimRewards(Player player) {

        if (!canClaimRewards) return;

        Inventory inventory = player.getInventory();
        for (ItemStack stack : itemRewards) inventory.placeItemBackInInventory(stack.copy());
        if (hasXPReward()) player.giveExperiencePoints(xpReward);

        instance.claimRewards(player, level);
        rewardsClaimed = true;
        canClaimRewards = false;
    }

    public boolean isUnlocked() { return state == State.UNLOCKED; }

    public boolean hasXPReward() { return xpReward > 0; }

    public boolean hasRecipesOrAdditional() { return recipes.size() + additional.size() > 0; }

    public enum State {

        LOCKED(instance -> PSComponents.locked()),
        UNLOCKING(PSComponents::progressBar),
        UNLOCKED(instance -> PSComponents.unlocked());

        private final Function<SkillInstance, Component> componentSupplier;
        State(Function<SkillInstance, Component> componentSupplier) { this.componentSupplier = componentSupplier; }

        public Component getComponent(SkillInstance instance) { return componentSupplier.apply(instance); }
    }

}
