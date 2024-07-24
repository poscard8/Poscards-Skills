package github.poscard8.poscardsskills.util.item;

import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.module.BrilliantGearModule;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum PSArmorMaterials implements ArmorMaterial {

    BRILLIANT(ArmorMaterials.DIAMOND, Ingredient.of(BaseModule.Items.BRILLIANT_SHARD.get()), BrilliantGearModule.SoundEvents.ARMOR_EQUIP_BRILLIANT, "poscardsskills:brilliant");

    private final ArmorMaterial template;
    private final Ingredient ingredient;
    private final Supplier<SoundEvent> soundSupplier;
    private final String name;

    PSArmorMaterials(ArmorMaterial template, Ingredient ingredient, Supplier<SoundEvent> soundSupplier, String name) {

        this.template = template;
        this.ingredient = ingredient;
        this.soundSupplier = soundSupplier;
        this.name = name;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) { return template.getDurabilityForSlot(slot); }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) { return template.getDefenseForSlot(slot); }

    @Override
    public int getEnchantmentValue() { return template.getEnchantmentValue(); }

    @Override
    public SoundEvent getEquipSound() { return soundSupplier.get(); }

    @Override
    public Ingredient getRepairIngredient() { return ingredient; }

    @Override
    public String getName() { return name; }

    @Override
    public float getToughness() { return template.getToughness(); }

    @Override
    public float getKnockbackResistance() { return template.getKnockbackResistance(); }

}
