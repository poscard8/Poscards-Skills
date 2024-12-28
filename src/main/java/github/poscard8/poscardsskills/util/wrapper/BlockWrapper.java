package github.poscard8.poscardsskills.util.wrapper;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Makes block registration and JSON file generation simpler.
 * Registers blocks and block items at once.
 * Still uses deferred register and registry objects, but they are hidden.
 * Most methods utilize {@link BlockType} class, which is customized for my needs.
 */
public class BlockWrapper implements Supplier<Block>, ItemLike {

    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, PoscardsSkills.ID);
    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, PoscardsSkills.ID);
    public static final List<BlockWrapper> VALUES = new ArrayList<>();

    public final BlockSet blockSet;
    public final BlockType blockType;

    final RegistryObject<Block> blockHolder;
    final RegistryObject<BlockItem> itemHolder;

    public BlockWrapper(BlockSet blockSet, BlockType blockType) {

        String name = blockType.getName(blockSet);
        this.blockSet = blockSet;
        this.blockType = blockType;

        this.blockHolder = BLOCK_REGISTRY.register(name, blockType.getBlock(blockSet));
        this.itemHolder = ITEM_REGISTRY.register(name, () -> new BlockItem(get(), new Item.Properties()));

        VALUES.add(this);
    }

    public static void forEach(Consumer<BlockWrapper> consumer) { VALUES.forEach(consumer); }

    public void addBlockModel(BlockStateProvider provider) { blockType.blockModelFunction.accept(provider, this); }

    public void addItemModel(ItemModelProvider provider) { blockType.itemModelFunction.accept(provider, this); }

    public void addLootTable(BlockLootSubProvider provider) { blockType.lootTableFunction.accept(provider, this); }

    public Block get() { return blockHolder.get(); }

    public BlockItem getItem() { return itemHolder.get(); }

    @NotNull
    public Item asItem() { return getItem(); }

    public BlockState defaultBlockState() { return get().defaultBlockState(); }

    public String path() { return resourceLocation().getPath(); }

    public ResourceLocation resourceLocation() { return blockHolder.getId(); }

    public ResourceLocation textureLocation() { return textureLocation(""); }

    public ResourceLocation textureLocation(String suffix) { return new ResourceLocation(PoscardsSkills.ID, String.format("block/%s%s", blockType.getTexture(blockSet), suffix)); }

    public String toString() { return String.format("BlockWrapper{%s}", resourceLocation()); }

}