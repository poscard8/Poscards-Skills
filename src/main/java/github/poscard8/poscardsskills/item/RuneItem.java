package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.util.PSTags;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RuneItem extends ItemWithDescription {

    protected static final String KEY = "customRarity";
    protected final Rarity rarity;

    public RuneItem(Properties property, Rarity rarity) {

        super(property.rarity(rarity));
        this.rarity = rarity;
    }

    public static void addParticlesAround(Player player, ItemStack stack) {

        ParticleOptions particleOptions = new ItemParticleOption(ParticleTypes.ITEM, stack);
        Random random = new Random();

        for(int i = 0; i < 7; ++i) {
            double d0 = random.nextGaussian() * 0.03D;
            double d1 = random.nextGaussian() * 0.03D;
            double d2 = random.nextGaussian() * 0.03D;
            player.level.addParticle(particleOptions, player.getRandomX(1.0D), player.getRandomY() + 0.25D, player.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        List<Component> components = new ArrayList<>();

        Component rarityComponent = PSComponents.rarity(rarity);
        Component description0 = Component.translatable("tooltip.poscardsskills.rune_desc")
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        Component description1 = Component.translatable("tooltip.poscardsskills.rune_desc_2")
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        Component description2 = Component.translatable("tooltip.poscardsskills.rune_desc_3", PSComponents.rarity(rarity).getString())
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));

        components.add(description0);
        components.add(rarityComponent.copy().append(description1));
        components.add(Component.empty());
        components.addAll(PSComponents.split(description2));

        return components;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (hand == InteractionHand.MAIN_HAND || player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) return super.use(level, player, hand);

        ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (mainHandItem.is(PSTags.Items.RUNE_NOT_APPLICABLE)) return InteractionResultHolder.fail(offHandItem);

        if (offHandItem.is(this)) {

            offHandItem.shrink(1);
            ItemStack applied = apply(mainHandItem);

            addParticlesAround(player, getDefaultInstance());
            player.playSound(BaseModule.SoundEvents.RUNE_APPLY.get());
            player.getCooldowns().addCooldown(this, 20);
            player.setItemInHand(InteractionHand.MAIN_HAND, applied);

            return InteractionResultHolder.consume(offHandItem);

        } else return super.use(level, player, hand);
    }

    protected ItemStack apply(ItemStack stack) {

        ItemStack newStack = stack.copy();
        CompoundTag tag = newStack.getOrCreateTag();

        if (tag.contains(KEY)) {

            if (tag.getString(KEY).equals(rarity.name())) { tag.remove(KEY); } else tag.putString(KEY, rarity.name());

        } else tag.putString(KEY, rarity.name());

        assert newStack.getTag() != null;
        if (newStack.getTag().isEmpty()) newStack.setTag(null);

        return newStack;
    }


}
