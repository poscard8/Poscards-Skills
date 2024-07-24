package github.poscard8.poscardsskills.datagen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PSBlockStateProvider extends BlockStateProvider {

    public PSBlockStateProvider(DataGenerator generator, ExistingFileHelper fileHelper) { super(generator, PoscardsSkills.ID, fileHelper); }

    @Override
    protected void registerStatesAndModels() { BlockWrapper.forEach(wrapper -> wrapper.addBlockModel(this)); }

}
