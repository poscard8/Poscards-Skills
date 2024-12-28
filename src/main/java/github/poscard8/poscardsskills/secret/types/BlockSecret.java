package github.poscard8.poscardsskills.secret.types;

import github.poscard8.poscardsskills.secret.Secret;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

/**
 * Secret unlocked via breaking blocks.
 */
public class BlockSecret extends Secret {

    public final Predicate<BlockState> statePredicate;
    public final float chance;

    public BlockSecret(Predicate<BlockState> statePredicate, float chance, int weight) {

        super(weight);
        this.statePredicate = statePredicate;
        this.chance = chance;
    }

    public void tryUnlock(ServerPlayer player, BlockState state) {

        if (statePredicate.test(state) && !player.isCreative() && !player.isSpectator()) {

            if (randomFloat() < chance) unlock(player);
        }
    }

}
