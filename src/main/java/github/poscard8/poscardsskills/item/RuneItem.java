package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSSoundEvents;
import github.poscard8.poscardsskills.util.PSTags;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Runes give fancy names to items.
 * Applying a rune to item twice will reset the effect.
 */
public class RuneItem extends ItemWithDescription {

    public static final String NBT_KEY = "customRarity";
    protected final Rarity rarity;

    public RuneItem(Properties property, Rarity rarity) {

        super(property.rarity(rarity));
        this.rarity = rarity;
    }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        List<Component> components = new ArrayList<>();

        Component rarityComponent = PSComponents.rarity(rarity);
        Component description0 = Component.translatable("tooltip.poscardsskills.rune_desc_1")
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        Component description1 = Component.translatable("tooltip.poscardsskills.rune_desc_2")
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        Component description2 = Component.translatable("tooltip.poscardsskills.rune_desc_3", PSComponents.rarity(rarity).getString())
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));

        components.add(description0);
        components.add(rarityComponent.copy().append(description1));
        components.add(PSComponents.space());
        components.addAll(PSComponents.split(description2));

        return components;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (hand == InteractionHand.MAIN_HAND || player.getMainHandItem().isEmpty()) return super.use(level, player, hand);

        ItemStack offHandItem = player.getOffhandItem();
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.is(PSTags.Items.RUNE_NOT_APPLICABLE)) return InteractionResultHolder.fail(offHandItem);

        if (offHandItem.is(this)) {

            offHandItem.shrink(1);
            ItemStack applied = apply(mainHandItem);

            if (player.isLocalPlayer()) PSUtils.addParticlesAroundPlayer(player, getDefaultInstance());
            player.playSound(PSSoundEvents.RUNE_APPLY.get());
            player.getCooldowns().addCooldown(this, 20);
            player.setItemInHand(InteractionHand.MAIN_HAND, applied);

            return InteractionResultHolder.consume(offHandItem);

        } else return super.use(level, player, hand);
    }

    protected ItemStack apply(ItemStack stack) {

        ItemStack newStack = stack.copy();
        CompoundTag tag = newStack.getOrCreateTag();

        if (tag.contains(NBT_KEY)) {

            if (tag.getString(NBT_KEY).equals(rarity.name())) { tag.remove(NBT_KEY); } else tag.putString(NBT_KEY, rarity.name());

        } else tag.putString(NBT_KEY, rarity.name());

        assert newStack.getTag() != null;
        if (newStack.getTag().isEmpty()) newStack.setTag(null);

        return newStack;
    }


}
