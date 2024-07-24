package github.poscard8.poscardsskills.util.item;

import github.poscard8.poscardsskills.module.BaseModule;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public enum PSTiers implements Tier {

    BRILLIANT(Tiers.DIAMOND, Ingredient.of(BaseModule.Items.BRILLIANT_SHARD.get()));

    private final Tier template;
    private final Ingredient ingredient;

    PSTiers(Tier template, Ingredient ingredient) {

        this.template = template;
        this.ingredient = ingredient;
    }

    @Override
    public int getUses() { return template.getUses(); }

    @Override
    public float getSpeed() { return template.getSpeed(); }

    @Override
    public float getAttackDamageBonus() { return template.getAttackDamageBonus(); }

    @SuppressWarnings("deprecation")
    @Override
    public int getLevel() { return template.getLevel(); }

    @Override
    public int getEnchantmentValue() { return template.getEnchantmentValue(); }

    @Override
    public Ingredient getRepairIngredient() { return ingredient; }

}
