package com.sihenzhang.simplebbq.integration.rei;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.rei.category.CampfireCookingOnGrillCategory;
import com.sihenzhang.simplebbq.integration.rei.category.GrillCookingCategory;
import com.sihenzhang.simplebbq.integration.rei.category.SeasoningCategory;
import com.sihenzhang.simplebbq.integration.rei.category.SkeweringCategory;
import com.sihenzhang.simplebbq.integration.rei.display.CampfireCookingOnGrillDisplay;
import com.sihenzhang.simplebbq.integration.rei.display.GrillCookingDisplay;
import com.sihenzhang.simplebbq.integration.rei.display.SeasoningDisplay;
import com.sihenzhang.simplebbq.integration.rei.display.SkeweringDisplay;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class SimpleBBQREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        var skewering = new SkeweringCategory();
        var seasoning = new SeasoningCategory();
        var grillCooking = new GrillCookingCategory();
        var campfireCookingOnGrill = new CampfireCookingOnGrillCategory();

        registry.add(skewering);
        registry.add(seasoning);
        registry.add(grillCooking);
        registry.add(campfireCookingOnGrill);

        registry.addWorkstations(skewering.getCategoryIdentifier(), EntryStacks.of(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM));
        registry.addWorkstations(seasoning.getCategoryIdentifier(), EntryStacks.of(SimpleBBQRegistry.GRILL_BLOCK_ITEM));
        registry.addWorkstations(grillCooking.getCategoryIdentifier(), EntryStacks.of(SimpleBBQRegistry.GRILL_BLOCK_ITEM));
        registry.addWorkstations(campfireCookingOnGrill.getCategoryIdentifier(), EntryStacks.of(SimpleBBQRegistry.GRILL_BLOCK_ITEM));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(SkeweringRecipe.class, SimpleBBQRegistry.SKEWERING_RECIPE_TYPE, SkeweringDisplay::new);
        registry.registerRecipeFiller(SeasoningRecipe.class, SimpleBBQRegistry.SEASONING_RECIPE_TYPE, SeasoningDisplay::new);
        registry.registerRecipeFiller(GrillCookingRecipe.class, SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE, GrillCookingDisplay::new);
        registry.registerRecipeFiller(CampfireCookingRecipe.class, RecipeType.CAMPFIRE_COOKING, CampfireCookingOnGrillDisplay::new);
    }
}
