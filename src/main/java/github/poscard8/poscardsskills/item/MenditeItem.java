package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.module.BaseModule;
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

import java.util.Collection;
import java.util.List;

public class MenditeItem extends ItemWithDescription {

    protected final int repairedDurability;


    public MenditeItem(Properties property) { this(property, 256); }

    public MenditeItem(Properties property, int repairedDurability) {

        super(property);
        this.repairedDurability = repairedDurability;
    }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(this);
        String name = getDescription().getString();
        if (key == null) return List.of();

        MutableComponent description0 = Component.translatable(String.format("tooltip.%s.%s_desc", key.getNamespace(), key.getPath()), repairedDurability)
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        Component description1 = Component.translatable(String.format("tooltip.%s.%s_desc_2", key.getNamespace(), key.getPath()), name)
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));

        Component newDescription = description0.append(description1);
        return PSComponents.split(newDescription);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (hand == InteractionHand.MAIN_HAND || player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) return super.use(level, player, hand);

        ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (!mainHandItem.isDamageableItem()) return InteractionResultHolder.pass(offHandItem);

        if (offHandItem.is(this)) {

            offHandItem.shrink(1);

            ItemStack applied = mainHandItem.copy();
            applied.setDamageValue(applied.getDamageValue() - repairedDurability);

            RuneItem.addParticlesAround(player, getDefaultInstance());
            player.playSound(BaseModule.SoundEvents.MENDITE_APPLY.get());
            player.getCooldowns().addCooldown(this, 20);
            player.setItemInHand(InteractionHand.MAIN_HAND, applied);

            return InteractionResultHolder.consume(offHandItem);

        } else return super.use(level, player, hand);
    }


}
