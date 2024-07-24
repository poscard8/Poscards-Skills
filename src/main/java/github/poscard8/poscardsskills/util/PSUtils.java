package github.poscard8.poscardsskills.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class PSUtils {

    private PSUtils() {}

    private static MinecraftServer CURRENT_SERVER = null;

    public static LocalPlayer getLocalPlayer() { return Objects.requireNonNull(Minecraft.getInstance().player); }

    @Nullable
    public static LocalPlayer getLocalPlayer(Player player) {

        return player instanceof LocalPlayer localPlayer ? localPlayer : getLocalPlayer().getUUID().equals(player.getUUID()) ? getLocalPlayer() : null;
    }

    @Nullable
    public static ServerPlayer getServerPlayer() { return getServerPlayer(getLocalPlayer()); }

    @Nullable
    public static ServerPlayer getServerPlayer(Player player) {

        return player instanceof ServerPlayer serverPlayer ? serverPlayer : getCurrentServer() != null ? getCurrentServer().getPlayerList().getPlayer(player.getUUID()) : null;
    }

    public static MinecraftServer getCurrentServer() { return CURRENT_SERVER; }

    public static void setServer(MinecraftServer server) { CURRENT_SERVER = server; }

    /**
     * Plays a local sound without using {@link RandomSource} in multiple threads.
     */
    @SuppressWarnings("deprecation")
    public static void playLocalSound(LocalPlayer localPlayer, SoundEvent soundEvent, float volume, float pitch) {

        Minecraft minecraft = Minecraft.getInstance();

        SimpleSoundInstance simplesoundinstance = new SimpleSoundInstance(soundEvent, SoundSource.MASTER, volume, pitch, RandomSource.createThreadSafe(), localPlayer.getX(), localPlayer.getY(), localPlayer.getZ());
        minecraft.getSoundManager().play(simplesoundinstance);
    }


}
