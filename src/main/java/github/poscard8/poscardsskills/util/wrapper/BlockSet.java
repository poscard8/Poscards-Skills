package github.poscard8.poscardsskills.util.wrapper;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

public enum BlockSet {

    JADE(MaterialColor.COLOR_LIGHT_GREEN),
    JASPER(MaterialColor.TERRACOTTA_RED),
    MARBLE(MaterialColor.QUARTZ);

    final MaterialColor mapColor;

    BlockSet(MaterialColor mapColor) { this.mapColor = mapColor; }

    @Override
    public String toString() { return super.toString().toLowerCase(); }

    public BlockBehaviour.Properties getProperties() { return BlockBehaviour.Properties.copy(Blocks.STONE).color(mapColor); }

}
