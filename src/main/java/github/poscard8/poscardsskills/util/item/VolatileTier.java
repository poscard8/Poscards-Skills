package github.poscard8.poscardsskills.util.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public enum VolatileTier implements Tier
{
    INSTANCE;

    @Override
    public int getUses() { return 3200; }

    @Override
    public float getSpeed() { return 28.0F; }

    @Override
    public float getAttackDamageBonus() { return 6.5F; }

    @Override
    public int getLevel() { return 4; }

    @Override
    public int getEnchantmentValue() { return 0; }

    @Override
    @NotNull
    public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }

}
