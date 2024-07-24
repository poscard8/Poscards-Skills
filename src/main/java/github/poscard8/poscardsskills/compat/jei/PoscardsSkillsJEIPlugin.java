package github.poscard8.poscardsskills.compat.jei;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.item.BrilliantBookItem;
import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.module.BrilliantGearModule;
import github.poscard8.poscardsskills.module.BrilliantUtilitiesModule;
import github.poscard8.poscardsskills.module.PSModules;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@JeiPlugin
public class PoscardsSkillsJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = PoscardsSkills.asResource("jei_plugin");

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        IModPlugin.super.registerRecipes(registration);
        registerRareDrops(registration);
        registerBrilliantBookRecipes(registration);
        registerRepairRecipes(registration);
    }

    @Override
    public void registerRuntime(IRuntimeRegistration registration) {

        IModPlugin.super.registerRuntime(registration);
        hideBrilliantRuneRecipe(registration);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        IModPlugin.super.registerCategories(registration);

        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new SkillCraftingCategory(guiHelper), new RareDropCategory(guiHelper));
    }

    public void registerRareDrops(IRecipeRegistration registration) {

        registration.addRecipes(RareDrop.TYPE, RareDrop.getAll());
        registration.addRecipes(SkillRecipe.JEI_TYPE, SkillData.getAllRecipes());
    }

    public void registerBrilliantBookRecipes(IRecipeRegistration registration) {

        if (PSModules.BRILLIANT_UTILITIES.isPresent()) {

            List<ItemStack> inputs = BrilliantBookItem.getApplicableItems();
            List<ItemStack> outputs = inputs.stream().map(stack -> BrilliantBookItem.applyRandom(stack, true)).toList();

            IJeiAnvilRecipe brilliantBookRecipe = registration.getVanillaRecipeFactory().createAnvilRecipe(inputs, List.of(BrilliantUtilitiesModule.Items.BRILLIANT_BOOK.get().getDefaultInstance()), outputs);
            registration.addRecipes(RecipeTypes.ANVIL, List.of(brilliantBookRecipe));
        }
    }

    public void registerRepairRecipes(IRecipeRegistration registration) {

        if (PSModules.BRILLIANT_GEAR.isPresent()) {

            IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();
            Ingredient ingredient = Ingredient.of(BaseModule.Items.BRILLIANT_SHARD.get());

            List<ItemStack> repairItems = List.of(ingredient.getItems());
            List<ItemStack> repairableItems = List.of(

                    BrilliantGearModule.Items.BRILLIANT_SWORD.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_SHOVEL.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_PICKAXE.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_AXE.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_HOE.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_HELMET.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_CHESTPLATE.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_LEGGINGS.get().getDefaultInstance(),
                    BrilliantGearModule.Items.BRILLIANT_BOOTS.get().getDefaultInstance()
            );

            Stream<IJeiAnvilRecipe> repairRecipes = repairableItems.stream()

                    .mapMulti((stack, consumer) -> {

                        ItemStack damagedThreeQuarters = stack.copy();
                        damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
                        ItemStack damagedHalf = stack.copy();
                        damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);

                        IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
                        consumer.accept(repairWithSame);

                        ItemStack damagedFully = stack.copy();
                        damagedFully.setDamageValue(damagedFully.getMaxDamage());
                        IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairItems, List.of(damagedThreeQuarters));
                        consumer.accept(repairWithMaterial);

                    });

            registration.addRecipes(RecipeTypes.ANVIL, repairRecipes.toList());
        }
    }

    public void hideBrilliantRuneRecipe(IRuntimeRegistration registration) {

        Optional<CraftingRecipe> optional = registration.getRecipeManager().createRecipeLookup(RecipeTypes.CRAFTING).get().filter(craftingRecipe -> craftingRecipe.getId().equals(PoscardsSkills.asResource("brilliant_rune"))).findFirst();
        optional.ifPresent(craftingRecipe -> registration.getRecipeManager().hideRecipes(RecipeTypes.CRAFTING, List.of(craftingRecipe)));
    }

    @Override
    public ResourceLocation getPluginUid() { return ID; }

}
