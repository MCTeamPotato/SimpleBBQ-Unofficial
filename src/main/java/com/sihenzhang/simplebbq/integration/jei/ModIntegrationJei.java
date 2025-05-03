package com.sihenzhang.simplebbq.integration.jei;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import com.sihenzhang.simplebbq.util.I18nUtils;
import com.sihenzhang.simplebbq.util.RLUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    public static final String MOD_ID = "jei";
    public static final ResourceLocation RECIPE_GUI_VANILLA = RLUtils.createRL(SimpleBBQ.MOD_ID, "textures/gui/gui_vanilla.png");

    @Override
    public ResourceLocation getPluginUid() {
        return RLUtils.createRL("simple_bbq");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new GrillCookingCategory(guiHelper));
        registration.addRecipeCategories(new CampfireCookingOnGrillCategory(guiHelper));
        registration.addRecipeCategories(new SeasoningCategory(guiHelper));
        registration.addRecipeCategories(new SkeweringCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<GrillCookingRecipe> boilingPotRecipes = recipeManager.getAllRecipesFor(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE).stream().map(RecipeHolder::value).collect(Collectors.toList());
        registration.addRecipes(GrillCookingCategory.RECIPE_TYPE, boilingPotRecipes);
        List<CampfireCookingRecipe> campfireCookingRecipes = recipeManager.getAllRecipesFor(RecipeType.CAMPFIRE_COOKING).stream().map(RecipeHolder::value).collect(Collectors.toList());
        registration.addRecipes(CampfireCookingOnGrillCategory.RECIPE_TYPE, campfireCookingRecipes);
        List<SeasoningRecipe> seasoningRecipes = recipeManager.getAllRecipesFor(SimpleBBQRegistry.SEASONING_RECIPE_TYPE).stream().map(RecipeHolder::value).collect(Collectors.toList());
        registration.addRecipes(SeasoningCategory.RECIPE_TYPE, seasoningRecipes);
        List<SkeweringRecipe> skeweringRecipes = recipeManager.getAllRecipesFor(SimpleBBQRegistry.SKEWERING_RECIPE_TYPE).stream().map(RecipeHolder::value).collect(Collectors.toList());
        registration.addRecipes(SkeweringCategory.RECIPE_TYPE, skeweringRecipes);

        registration.addIngredientInfo(
                List.of(
                        SimpleBBQRegistry.CHILI_POWDER.getDefaultInstance(),
                        SimpleBBQRegistry.CUMIN.getDefaultInstance(),
                        SimpleBBQRegistry.SALT_AND_PEPPER.getDefaultInstance()
                ),
                VanillaTypes.ITEM_STACK,
                I18nUtils.createIntegrationComponent(MOD_ID, "description.seasoning")
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(SimpleBBQRegistry.GRILL_BLOCK_ITEM.getDefaultInstance(), GrillCookingCategory.RECIPE_TYPE, CampfireCookingOnGrillCategory.RECIPE_TYPE, SeasoningCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM.getDefaultInstance(), SkeweringCategory.RECIPE_TYPE);
    }
}
