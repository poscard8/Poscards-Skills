package github.poscard8.poscardsskills.item;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BrilliantPearlItem extends ItemWithDescription {

    public BrilliantPearlItem(Properties property) { super(property); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.SLOW_FALLING, 100);
        if (level.isClientSide) return InteractionResultHolder.success(stack);

        player.teleportTo(player.getX(), level.getMaxBuildHeight(), player.getZ());
        player.addEffect(effectInstance);
        player.getCooldowns().addCooldown(this, 400);

        stack.shrink(1);
        return InteractionResultHolder.success(stack);
    }

}
