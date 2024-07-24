package github.poscard8.poscardsskills.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.module.BrilliantGearModule;
import github.poscard8.poscardsskills.module.PSModules;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class AnimatedArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public static final Material BRILLIANT_OUTER = new Material(TextureAtlas.LOCATION_BLOCKS, PoscardsSkills.asResource("models/armor/animated/brilliant_layer_1"));
    public static final Material BRILLIANT_INNER = new Material(TextureAtlas.LOCATION_BLOCKS, PoscardsSkills.asResource("models/armor/animated/brilliant_layer_2"));

    public static final Map<Item, Material> MATERIAL_MAP = Util.make(new HashMap<>(), map -> {

        if (PSModules.BRILLIANT_GEAR.isPresent()) {

            map.put(BrilliantGearModule.Items.BRILLIANT_HELMET.get(), BRILLIANT_OUTER);
            map.put(BrilliantGearModule.Items.BRILLIANT_CHESTPLATE.get(), BRILLIANT_OUTER);
            map.put(BrilliantGearModule.Items.BRILLIANT_LEGGINGS.get(), BRILLIANT_INNER);
            map.put(BrilliantGearModule.Items.BRILLIANT_BOOTS.get(), BRILLIANT_OUTER);

        }
    });

    protected final A outerModel;
    protected final A innerModel;

    public AnimatedArmorLayer(RenderLayerParent<T, M> parent, A outerModel, A innerModel) {

        super(parent);
        this.outerModel = outerModel;
        this.innerModel = innerModel;
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float p_117100_, float p_117101_, float p_117102_, float p_117103_, float p_117104_, float p_117105_) {

        renderArmorPiece(poseStack, bufferSource, entity, EquipmentSlot.CHEST, packedLight);
        renderArmorPiece(poseStack, bufferSource, entity, EquipmentSlot.LEGS, packedLight);
        renderArmorPiece(poseStack, bufferSource, entity, EquipmentSlot.FEET, packedLight);
        renderArmorPiece(poseStack, bufferSource, entity, EquipmentSlot.HEAD, packedLight);
    }

    protected void renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T entity, EquipmentSlot slot, int packedLight) {

        ItemStack itemstack = entity.getItemBySlot(slot);
        A model = getModel(slot);

        if (itemstack.getItem() instanceof ArmorItem armorItem) {

            if (armorItem.getSlot() == slot) {

                getParentModel().copyPropertiesTo(model);
                setPartVisibility(model, slot);

                Material material = MATERIAL_MAP.getOrDefault(armorItem, null);
                boolean hasFoil = itemstack.hasFoil();

                if (material != null) renderModel(poseStack, bufferSource, material, packedLight, hasFoil, model);
            }
        }
    }

    protected void setPartVisibility(A model, EquipmentSlot slot) {

        model.setAllVisible(false);

        switch (slot) {

            case HEAD -> {

                model.head.visible = true;
                model.hat.visible = true;
            }
            case CHEST -> {

                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
            }
            case LEGS -> {

                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {

                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
        }
    }

    protected void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, Material material, int packedLight, boolean hasFoil, A model) {

        VertexConsumer buffer = hasFoil ? VertexMultiConsumer.create(bufferSource.getBuffer(RenderType.glint()), material.buffer(bufferSource, RenderType::armorCutoutNoCull)) : material.buffer(bufferSource, RenderType::armorCutoutNoCull);
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected A getModel(EquipmentSlot slot) { return slot == EquipmentSlot.LEGS ? innerModel : outerModel; }

}
