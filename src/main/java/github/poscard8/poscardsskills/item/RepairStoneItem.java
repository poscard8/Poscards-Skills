package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.registry.PSSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class RepairStoneItem extends ItemWithDescription
{
    protected final Supplier<Integer> valueGetter;

    public RepairStoneItem(Properties property, Supplier<Integer> valueGetter)
    {
        super(property);
        this.valueGetter = valueGetter;
    }

    public int getRepairValue() { return valueGetter.get(); }

    public boolean canRepair(ItemStack stack) { return stack.isDamageableItem() && stack.isDamaged(); }

    public boolean canUse(Player player)
    {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < 9; i++)
        {
            if (canRepair(inventory.getItem(i))) return true;
        }
        return false;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player0, InteractionHand hand)
    {
        ItemStack stack = player0.getItemInHand(hand);

        if (player0 instanceof ServerPlayer player && canUse(player)) return useServerSide(player, stack);
        return super.use(level, player0, hand);
    }

    public InteractionResultHolder<ItemStack> useServerSide(ServerPlayer player, ItemStack stack)
    {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < 9; i++)
        {
            ItemStack tool = inventory.getItem(i);

            if (canRepair(tool))
            {
                int damage = Math.max(0, tool.getDamageValue() - getRepairValue());
                tool.setDamageValue(damage);
            }
        }
        boolean shrink = !(player.isCreative() || player.isSpectator());
        if (shrink) stack.shrink(1);

        player.level().playSound(null, player.getOnPos(), PSSoundEvents.REPAIR_STONE_APPLY.get(), SoundSource.PLAYERS);
        player.inventoryMenu.broadcastChanges();
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public Collection<Component> getDescriptionTexts(ItemStack stack, @Nullable Level level, TooltipFlag flag)
    {
        return List.of(Component.translatable("item.poscardsskills.repair_stone.desc").withStyle(ChatFormatting.GRAY));
    }

}
