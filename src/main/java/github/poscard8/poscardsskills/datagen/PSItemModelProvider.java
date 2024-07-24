package github.poscard8.poscardsskills.datagen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PSItemModelProvider extends ItemModelProvider {

    public PSItemModelProvider(DataGenerator generator, ExistingFileHelper fileHelper) { super(generator, PoscardsSkills.ID, fileHelper); }

    @Override
    protected void registerModels() { BlockWrapper.forEach(wrapper -> wrapper.addItemModel(this)); }

}
