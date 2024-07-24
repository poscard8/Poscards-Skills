package github.poscard8.poscardsskills.event;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.client.layer.AnimatedArmorLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PoscardsSkills.ID)
@OnlyIn(Dist.CLIENT)
public final class ClientEvents {

    private ClientEvents() {}

    @SubscribeEvent
    static void onPlayerRender(RenderPlayerEvent.Pre event) {

        Player player = event.getEntity();
        PlayerRenderer renderer = event.getRenderer();

        boolean slim = false;
        Field[] fields = PlayerModel.class.getDeclaredFields();

        for (Field field : fields) {

            if (field.getName().equals("slim")) {

                try {

                    field.setAccessible(true);
                    slim = (boolean) field.get(renderer.getModel());
                    field.setAccessible(false);
                    break;

                } catch (IllegalAccessException e) { throw new RuntimeException(e); }
            }
        }

        ModelPart outer = Minecraft.getInstance().getEntityModels().bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR);
        ModelPart inner = Minecraft.getInstance().getEntityModels().bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR);

        renderer.addLayer(new AnimatedArmorLayer<>(renderer, new HumanoidModel<>(outer), new HumanoidModel<>(inner)));
    }

}
