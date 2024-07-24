package github.poscard8.poscardsskills.util.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.module.BaseModule;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;

public final class PSCreativeModeTab extends CreativeModeTab {

    private static final String[] ORDER = new String[]{

            "jade_bricks", "jade_brick_stairs", "jade_brick_slab",
            "jade_brick_wall", "chiseled_jade", "jade_pillar",
            "layered_jade", "shiny_jade", "rough_jade",
            "jasper_bricks", "jasper_brick_stairs", "jasper_brick_slab",
            "jasper_brick_wall", "chiseled_jasper", "jasper_pillar",
            "layered_jasper", "shiny_jasper", "rough_jasper",
            "marble_bricks", "marble_brick_stairs", "marble_brick_slab",
            "marble_brick_wall", "chiseled_marble", "marble_pillar",
            "layered_marble", "shiny_marble", "rough_marble",
            "elegant_rune", "ethereal_rune", "classical_rune",
            "mendite", "brilliant_shard", "brilliant_fertilizer",
            "brilliant_pearl", "brilliant_book", "brilliant_key",
            "brilliant_sword", "brilliant_shovel", "brilliant_pickaxe",
            "brilliant_axe", "brilliant_hoe", "brilliant_helmet",
            "brilliant_chestplate", "brilliant_leggings", "brilliant_boots"
    };


    public PSCreativeModeTab() { super("poscardsskills.main"); }

    @Override
    public ItemStack makeIcon() { return BaseModule.Items.BRILLIANT_SHARD.get().getDefaultInstance(); }

    @Override
    public void fillItemList(NonNullList<ItemStack> stacks) {

        super.fillItemList(stacks);

        List<ItemStack> copy = List.copyOf(stacks.stream().toList());
        List<ItemStack> sorted = sorted(copy);

        for (int i = 0; i < sorted.size(); i++) stacks.set(i, sorted.get(i));
    }

    public List<ItemStack> sorted(List<ItemStack> list) { return list.stream().sorted(Comparator.comparingInt(PSCreativeModeTab::indexOf)).toList(); }

    private static int indexOf(ItemStack stack) {

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());

        if (key == null) return Integer.MAX_VALUE;
        if (!key.getNamespace().equals(PoscardsSkills.ID)) return Integer.MAX_VALUE;

        int index = -1;

        for (int i = 0; i < ORDER.length; i++) {

            String string = ORDER[i];

            if (string.equals(key.getPath())) {

                index = i;
                break;
            }
        }

        return index == -1 ? Integer.MAX_VALUE : index;
    }


}
