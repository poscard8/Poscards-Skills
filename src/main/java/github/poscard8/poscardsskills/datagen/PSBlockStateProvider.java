package github.poscard8.poscardsskills.datagen;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.wrapper.BlockType;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Block state file generator. See {@link BlockWrapper} and {@link BlockType} for more info.
 */
public class PSBlockStateProvider extends BlockStateProvider {

    public PSBlockStateProvider(PackOutput output, ExistingFileHelper fileHelper) { super(output, PoscardsSkills.ID, fileHelper); }

    @Override
    protected void registerStatesAndModels() { BlockWrapper.forEach(wrapper -> wrapper.addBlockModel(this)); }

}
