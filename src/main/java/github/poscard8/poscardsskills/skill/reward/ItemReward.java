package github.poscard8.poscardsskills.skill.reward;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class ItemReward extends Reward {

    public final ItemStack stack;

    protected ItemReward(ResourceLocation skillKey, Set<Integer> at, ItemStack stack) {

        super(skillKey, at);
        this.stack = stack;
    }

}
