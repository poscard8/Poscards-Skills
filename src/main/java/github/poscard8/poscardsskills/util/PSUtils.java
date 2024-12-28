package github.poscard8.poscardsskills.util;

import github.poscard8.poscardsskills.enchantment.PSEnchantment;
import github.poscard8.poscardsskills.registry.PSEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class PSUtils {

    static MinecraftServer CURRENT_SERVER = null;

    public static LocalPlayer getLocalPlayer() { return Objects.requireNonNull(Minecraft.getInstance().player); }

    @Nullable
    public static LocalPlayer getLocalPlayer(Player player) {

        return player instanceof LocalPlayer localPlayer ? localPlayer : getLocalPlayer().getUUID().equals(player.getUUID()) ? getLocalPlayer() : null;
    }

    @Nullable
    public static ServerPlayer getServerPlayer() { return getServerPlayer(getLocalPlayer()); }

    @Nullable
    public static ServerPlayer getServerPlayer(Player player) {

        return player instanceof ServerPlayer serverPlayer ? serverPlayer : getServer() != null ? getServer().getPlayerList().getPlayer(player.getUUID()) : null;
    }

    public static MinecraftServer getServer() { return CURRENT_SERVER; }

    public static void setServer(MinecraftServer server) { CURRENT_SERVER = server; }

    @OnlyIn(Dist.CLIENT)
    public static void playLocalSound(Player player, Supplier<SoundEvent> soundSupplier) { playLocalSound(player, soundSupplier, true); }

    @OnlyIn(Dist.CLIENT)
    public static void playLocalSound(@NotNull Player player, Supplier<SoundEvent> soundSupplier, boolean fluctuate) {

        Random random = new Random();
        SoundEvent soundEvent = soundSupplier.get();

        float volume = fluctuate ? random.nextFloat(0.8F, 1.25F) : 1;
        float pitch = fluctuate ? random.nextFloat(0.8F, 1.25F) : 1;

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, player.getSoundSource(), volume, pitch);
    }

    public static void addParticlesAroundPlayer(Player player, ItemStack stack) { addParticlesAroundPlayer(player, stack, 8); }

    public static void addParticlesAroundPlayer(Player player, ItemStack stack, int count) {

        LocalPlayer localPlayer = getLocalPlayer(player);
        if (localPlayer == null) return;

        ParticleOptions particleOptions = new ItemParticleOption(ParticleTypes.ITEM, stack);
        Random random = new Random();

        for(int i = 0; i < count; ++i) {

            double xd = random.nextGaussian() * 0.04D;
            double yd = random.nextGaussian() * 0.04D;
            double zd = random.nextGaussian() * 0.04D;

            localPlayer.level().addParticle(particleOptions, player.getRandomX(1.0D), player.getRandomY() + 0.25D, player.getRandomZ(1.0D), xd, yd, zd);
        }
    }

    public static void addParticlesAroundPlayer(Player player, Supplier<? extends ParticleOptions> particleSupplier) { addParticlesAroundPlayer(player, particleSupplier, 16); }

    public static void addParticlesAroundPlayer(Player player, Supplier<? extends ParticleOptions> particleSupplier, int count) {

        LocalPlayer localPlayer = getLocalPlayer(player);
        if (localPlayer == null) return;

        ParticleOptions particleOptions = particleSupplier.get();
        Random random = new Random();

        for(int i = 0; i < count; ++i) {

            double xd = random.nextGaussian() * 0.04D;
            double yd = random.nextGaussian() * 0.04D;
            double zd = random.nextGaussian() * 0.04D;

            localPlayer.level().addParticle(particleOptions, player.getRandomX(1.0D), player.getRandomY() + 0.25D, player.getRandomZ(1.0D), xd, yd, zd);
        }
    }

    public static void addComponentsToItem(ItemStack stack, Component... components) {

        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag display = new CompoundTag();
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf(""));

        for (Component component : components) {

            StringTag stringTag = StringTag.valueOf(Component.Serializer.toJson(component));
            lore.add(stringTag);
        }
        display.put("Lore", lore);
        tag.put("display", display);
        stack.setTag(tag);
    }

    public static void removeComponentsOfItem(ItemStack stack) {

        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag display = new CompoundTag();

        display.put("Lore", new ListTag());
        tag.put("display", display);
        stack.setTag(tag);
    }

    public static Map<Enchantment, Integer> getEnchantmentLevel(ItemStack stack) {

        Map<Enchantment, Integer> map = new HashMap<>();

        if (stack.isEmpty()) return map;
        if (!(stack.getItem() instanceof EnchantedBookItem)) return stack.getAllEnchantments();

        ListTag listTag = EnchantedBookItem.getEnchantments(stack);
        int size = listTag.size();

        for (int i = 0; i < size; i++) {

            try {

                CompoundTag compoundTag = listTag.getCompound(i);

                String id = compoundTag.getString("id");
                ResourceLocation key = ResourceLocation.tryParse(id);
                short level = compoundTag.getShort("lvl");

                if (ForgeRegistries.ENCHANTMENTS.containsKey(key)) map.put(ForgeRegistries.ENCHANTMENTS.getValue(key), (int) level);

            } catch (Exception ignored) {}
        }
        return map;
    }

    public static int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {

        if (stack.isEmpty()) return 0;
        if (!(stack.getItem() instanceof EnchantedBookItem)) return stack.getEnchantmentLevel(enchantment);

        ListTag listTag = EnchantedBookItem.getEnchantments(stack);
        int size = listTag.size();
        String enchantmentId = Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)).toString();

        for (int i = 0; i < size; i++) {

            try {

                CompoundTag compoundTag = listTag.getCompound(i);
                String id = compoundTag.getString("id");

                if (id.equals(enchantmentId))  return compoundTag.getShort("lvl");

            } catch (Exception ignored) {}
        }
        return 0;
    }

    public static double getTrueEfficiencyValue(LivingEntity entity) {

        PSEnchantment trueEfficiency = PSEnchantments.TRUE_EFFICIENCY.get();
        int level = EnchantmentHelper.getEnchantmentLevel(trueEfficiency, entity);
        return trueEfficiency.getValue(level);
    }

    public static double getVitalityValue(LivingEntity entity) {

        PSEnchantment vitality = PSEnchantments.VITALITY.get();
        int level = EnchantmentHelper.getEnchantmentLevel(vitality, entity);
        return vitality.getValue(level) / 6.0F;
    }

    public static double getExploitationValue(LivingEntity entity) {

        PSEnchantment exploitation = PSEnchantments.EXPLOITATION.get();
        int level = EnchantmentHelper.getEnchantmentLevel(exploitation, entity);
        return exploitation.getValue(level);
    }

    public static int randomInt() { return new Random().nextInt(); }

    public static float randomFloat() { return new Random().nextFloat(); }

}
