package github.poscard8.poscardsskills.util.wrapper;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.data.loot.BlockLoot;
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
 * Exists to make block registration and JSON file generation easier.
 * Registers blocks and block items at once.
 * Still uses deferred register and registry objects, but they are hidden/private.
 * Most methods utilize {@link BlockType} class, which is customized for my needs.
 */
public class BlockWrapper implements Supplier<Block>, ItemLike {

    public static final DeferredRegister<Block> BASE_BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, PoscardsSkills.ID);
    public static final DeferredRegister<Item> BASE_ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, PoscardsSkills.ID);
    public static final DeferredRegister<Block> DECORATIVE_BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, PoscardsSkills.ID);
    public static final DeferredRegister<Item> DECORATIVE_ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, PoscardsSkills.ID);
    public static final List<BlockWrapper> VALUES = new ArrayList<>();

    public final BlockSet blockSet;
    public final BlockType blockType;

    private final RegistryObject<Block> blockHolder;
    private final RegistryObject<BlockItem> itemHolder;

    public BlockWrapper(BlockSet blockSet, BlockType blockType) {

        String name = blockType.getName(blockSet);
        this.blockSet = blockSet;
        this.blockType = blockType;

        DeferredRegister<Block> blockDeferredRegister = this.blockType == BlockType.ROUGH ? BASE_BLOCK_REGISTRY : DECORATIVE_BLOCK_REGISTRY;
        DeferredRegister<Item> itemDeferredRegister = this.blockType == BlockType.ROUGH ? BASE_ITEM_REGISTRY : DECORATIVE_ITEM_REGISTRY;

        this.blockHolder = blockDeferredRegister.register(name, blockType.getBlock(blockSet));
        this.itemHolder = itemDeferredRegister.register(name, () -> new BlockItem(get(), new Item.Properties().tab(PoscardsSkills.CREATIVE_TAB)));

        VALUES.add(this);
    }

    public static void forEach(Consumer<BlockWrapper> consumer) { VALUES.forEach(consumer); }

    public void addBlockModel(BlockStateProvider provider) { blockType.blockModelFunction.accept(provider, this); }

    public void addItemModel(ItemModelProvider provider) { blockType.itemModelFunction.accept(provider, this); }

    public void addLootTable(BlockLoot provider) { blockType.lootTableFunction.accept(provider, this); }

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