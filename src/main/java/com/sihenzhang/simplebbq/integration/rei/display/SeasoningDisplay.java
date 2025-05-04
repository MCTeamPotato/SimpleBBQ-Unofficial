package com.sihenzhang.simplebbq.integration.rei.display;

import com.sihenzhang.simplebbq.integration.rei.SimpleBBQREIRecipes;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.*;

public class SeasoningDisplay extends BasicDisplay {

    public SeasoningDisplay(RecipeHolder<SeasoningRecipe> recipe) {
        this(
                Arrays.asList(
                        EntryIngredients.ofItemStacks(Arrays.asList(recipe.value().getIngredient().getItems())),
                        EntryIngredients.ofItemStacks(Arrays.asList(recipe.value().getSeasoning().getItems()))
                ),
                Collections.singletonList(EntryIngredients.ofItemStacks(recipe.value().getCachedResultItems().getUnchecked(recipe.value()))),
                Optional.ofNullable(recipe.id())
        );
    }

    public SeasoningDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SimpleBBQREIRecipes.SEASONING;
    }
}
