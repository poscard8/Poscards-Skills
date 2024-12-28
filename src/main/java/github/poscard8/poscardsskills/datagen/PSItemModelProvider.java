package github.poscard8.poscardsskills.datagen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.wrapper.BlockType;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Item model file generator. See {@link BlockWrapper} and {@link BlockType} for more info.
 */
public class PSItemModelProvider extends ItemModelProvider {

    public PSItemModelProvider(PackOutput output, ExistingFileHelper fileHelper) { super(output, PoscardsSkills.ID, fileHelper); }

    @Override
    protected void registerModels() { BlockWrapper.forEach(wrapper -> wrapper.addItemModel(this)); }

}
