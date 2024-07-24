package github.poscard8.poscardsskills.util.wrapper;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

public enum BlockSet {

    JADE(MaterialColor.EMERALD),
    JASPER(MaterialColor.TERRACOTTA_RED),
    MARBLE(MaterialColor.QUARTZ);

    private final MaterialColor materialColor;
    BlockSet(MaterialColor materialColor) { this.materialColor = materialColor; }

    @Override
    public String toString() { return super.toString().toLowerCase(); }

    public BlockBehaviour.Properties getProperties() { return BlockBehaviour.Properties.copy(Blocks.STONE).color(materialColor); }
}
