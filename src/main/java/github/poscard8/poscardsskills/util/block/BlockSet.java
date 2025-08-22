package github.poscard8.poscardsskills.util.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public enum BlockSet {

    JADE(MapColor.COLOR_LIGHT_GREEN),
    JASPER(MapColor.TERRACOTTA_RED),
    MARBLE(MapColor.QUARTZ);

    final MapColor mapColor;

    BlockSet(MapColor mapColor) { this.mapColor = mapColor; }

    @Override
    public String toString() { return super.toString().toLowerCase(); }

    public BlockBehaviour.Properties getProperties() { return BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(mapColor); }

}
