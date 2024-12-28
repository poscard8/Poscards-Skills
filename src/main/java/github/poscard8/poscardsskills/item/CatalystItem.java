package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.registry.PSSoundEvents;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Catalyst are used for ascension.
 * <p>{@code lootTableKeyA} is used if extra progression is enabled,
 * {@code lootTableKeyB} is used if it is disabled.</p>
 * <p>Amount of xp needed for rewards can be configured.</p>
 */
public class CatalystItem extends ItemWithDescription {

    protected final ResourceLocation lootTableKeyA;
    protected final ResourceLocation lootTableKeyB;

    @Nullable
    protected final Supplier<? extends ParticleOptions> particleSupplier;

    public CatalystItem(Properties property, ResourceLocation lootTableKey, @Nullable Supplier<? extends ParticleOptions> particleSupplier) { this(property, lootTableKey, lootTableKey, particleSupplier); }

    public CatalystItem(Properties property, ResourceLocation lootTableKeyA, ResourceLocation lootTableKeyB, @Nullable Supplier<? extends ParticleOptions> particleSupplier) {

        super(property);
        this.lootTableKeyA = lootTableKeyA;
        this.lootTableKeyB = lootTableKeyB;
        this.particleSupplier = particleSupplier;
    }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        ColorPalette colorPalette = PoscardsSkills.getComponentHandler().getColorPalette();
        ChatFormatting descriptionColor = colorPalette.colorOf(ColorPalette.Key.DESCRIPTION);
        ChatFormatting xpColor = colorPalette.colorOf(ColorPalette.Key.SKILL_AND_XP);
        ChatFormatting denyColor = colorPalette.colorOf(ColorPalette.Key.DENY);

        List<Component> components = new ArrayList<>();

        components.add(Component.translatable("tooltip.poscardsskills.catalyst_desc_1").withStyle(descriptionColor));

        @Nullable ServerPlayer serverPlayer = PSUtils.getServerPlayer();
        if (serverPlayer != null) {

            SkillData skillData = SkillData.of(serverPlayer);
            components.add(Component.empty());

            MutableComponent totalXPComponent = Component.translatable("label.poscardsskills.total_xp").withStyle(descriptionColor);
            MutableComponent rewardComponent = Component.translatable("tooltip.poscardsskills.catalyst_rewards").withStyle(descriptionColor);

            ChatFormatting color = canUse(serverPlayer) ? xpColor : denyColor;

            String totalXPString = NumberFormat.getInstance().format(skillData.getTotalXP());
            String rewardString = NumberFormat.getInstance().format(getRewards(serverPlayer));

            totalXPComponent.append(Component.literal(totalXPString).withStyle(color));
            rewardComponent.append(Component.literal(rewardString).withStyle(color));

            components.add(totalXPComponent);
            components.add(rewardComponent);

            if (!canUse(serverPlayer)) {

                String remainingXPString = NumberFormat.getInstance().format(xpPerReward(serverPlayer) - skillData.getTotalXP());

                components.add(Component.empty());
                components.add(Component.translatable("tooltip.poscardsskills.catalyst_cannot_use", remainingXPString).withStyle(denyColor));
            }
        }

        return components;
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (hand == InteractionHand.MAIN_HAND) return super.use(level, player, hand);

        ItemStack offHandItem = player.getOffhandItem();

        if (offHandItem.is(this)) {

            if (canUse(PSUtils.getServerPlayer(player))) {

                if (isServerPlayer(player)) {

                    ServerPlayer serverPlayer = (ServerPlayer) player;

                    addRewards(serverPlayer);
                    SkillData.of(serverPlayer).ascend();
                }

                offHandItem.shrink(1);

                player.getCooldowns().addCooldown(this, 20);

                if (player.isLocalPlayer() && PoscardsSkillsClientConfig.ASCENSION_PARTICLES.get() && particleSupplier != null) PSUtils.addParticlesAroundPlayer(player, particleSupplier, 12);
                if (player.isLocalPlayer() && PoscardsSkillsClientConfig.ASCENSION_SOUND.get()) player.playSound(PSSoundEvents.CATALYST_USE.get());

                return InteractionResultHolder.consume(offHandItem);

            } else {

                if (isServerPlayer(player)) {

                    ServerPlayer serverPlayer = (ServerPlayer) player;

                    String remainingXPString = NumberFormat.getInstance().format(xpPerReward(serverPlayer) - SkillData.of(serverPlayer).getTotalXP());
                    player.displayClientMessage(Component.translatable("tooltip.poscardsskills.catalyst_cannot_use", remainingXPString).withStyle(ChatFormatting.RED), false);
                }

                return InteractionResultHolder.fail(offHandItem);
            }
        }
        return super.use(level, player, hand);
    }

    /**
     * Handling items and text displays.
     */
    protected void addRewards(ServerPlayer player) {

        SimpleContainer container = new SimpleContainer(256);
        LootTable lootTable = getLootTable(player);
        LootParams lootParams = new LootParams.Builder((ServerLevel) player.level())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.GIFT);

        for (int i = 0; i < getRewards(player); i++) {

            List<ItemStack> items = lootTable.getRandomItems(lootParams);
            items.forEach(container::addItem);
        }

        List<Component> components = PSComponents.ascensionComponents(SkillData.of(player), container);

        for (int i = 0; i < container.getContainerSize(); i++) {

            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) player.getInventory().placeItemBackInInventory(stack);
        }

        components.forEach(component -> player.displayClientMessage(component, false));
    }

    protected LootTable getLootTable(ServerPlayer player) {

        LootTable lootTableA = Objects.requireNonNull(player.getServer()).getLootData().getLootTable(lootTableKeyA);
        LootTable lootTableB = Objects.requireNonNull(player.getServer()).getLootData().getLootTable(lootTableKeyB);

        return PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get() ? lootTableA : lootTableB;
    }

    protected int xpPerReward(ServerPlayer player) {

        int xpForAscension = PoscardsSkillsCommonConfig.XP_FOR_ASCENSION.get();
        int xpIncreaseForAscension = PoscardsSkillsCommonConfig.XP_INCREASE_FOR_ASCENSION.get();

        return xpForAscension + SkillData.of(player).ascensions * xpIncreaseForAscension;
    }

    protected int getRewards(ServerPlayer player) { return SkillData.of(player).getTotalXP() / xpPerReward(player); }

    protected boolean canUse(ServerPlayer player) { return player != null && getRewards(player) > 0; }

    protected boolean isServerPlayer(Player player) { return player instanceof ServerPlayer; }

}
