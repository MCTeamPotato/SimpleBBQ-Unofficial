package com.sihenzhang.simplebbq.integration.rei.display;

import com.sihenzhang.simplebbq.integration.rei.SimpleBBQREIRecipes;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SkeweringDisplay extends BasicDisplay {

    public SkeweringDisplay(RecipeHolder<SkeweringRecipe> recipe) {
        this(
                Arrays.asList(
                        EntryIngredients.ofItemStacks(Arrays.asList(recipe.value().getIngredient().getItems())),
                        EntryIngredients.ofItemStacks(Arrays.asList(Ingredient.of(SimpleBBQItemTags.SKEWER).getItems()))
                ),
                Collections.singletonList(EntryIngredients.ofItemStacks(Collections.singleton(RecipeViewerHelper.getResultItem(recipe.value())))),
                Optional.ofNullable(recipe.id())
        );
    }

    public SkeweringDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SimpleBBQREIRecipes.SKEWERING;
    }
}
