package github.poscard8.poscardsskills.mixin.accessor;

import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@SuppressWarnings("ALL")
@Mixin(ModelBakery.class)
@OnlyIn(Dist.CLIENT)
public interface ModelBakeryAccessor {

    @Accessor("UNREFERENCED_TEXTURES")
    static Set<Material> getUnreferencedTextures() { throw new AssertionError(); }

}
