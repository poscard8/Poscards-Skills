package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSSoundEvents;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * On offhand, repairs items with durability.
 */
public class RepairStoneItem extends ItemWithDescription {

    protected final Supplier<Integer> durabilityGetter;

    public RepairStoneItem(Properties property, Supplier<Integer> durabilityGetter) {

        super(property);
        this.durabilityGetter = durabilityGetter;
    }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(this);
        String name = getDescription().getString();
        if (key == null) return List.of();

        MutableComponent description0 = Component.translatable(String.format("tooltip.%s.repair_stone_desc_1", key.getNamespace()), getRepairedDurability())
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        Component description1 = Component.translatable(String.format("tooltip.%s.repair_stone_desc_2", key.getNamespace()), name)
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));

        Component newDescription = description0.append(description1);
        return PSComponents.split(newDescription);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (hand == InteractionHand.MAIN_HAND || player.getMainHandItem().isEmpty()) return super.use(level, player, hand);

        ItemStack offHandItem = player.getOffhandItem();
        ItemStack mainHandItem = player.getMainHandItem();

        if (!mainHandItem.isDamageableItem()) return InteractionResultHolder.pass(offHandItem);

        if (offHandItem.is(this)) {

            offHandItem.shrink(1);

            ItemStack applied = mainHandItem.copy();
            applied.setDamageValue(applied.getDamageValue() - getRepairedDurability());

            if (player.isLocalPlayer()) PSUtils.addParticlesAroundPlayer(player, getDefaultInstance());
            player.playSound(PSSoundEvents.REPAIR_STONE_APPLY.get());
            player.getCooldowns().addCooldown(this, 20);
            player.setItemInHand(InteractionHand.MAIN_HAND, applied);

            return InteractionResultHolder.consume(offHandItem);

        } else return super.use(level, player, hand);
    }

    public int getRepairedDurability() { return durabilityGetter.get(); }


}
