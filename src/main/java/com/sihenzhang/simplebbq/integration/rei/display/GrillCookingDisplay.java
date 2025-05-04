package com.sihenzhang.simplebbq.integration.rei.display;

import com.sihenzhang.simplebbq.integration.rei.SimpleBBQREIRecipes;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GrillCookingDisplay extends BasicDisplay {
    private double cookTime;

    public GrillCookingDisplay(RecipeHolder<GrillCookingRecipe> recipe) {
        this(EntryIngredients.ofIngredients(recipe.value().getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(BasicDisplay.registryAccess()))),
                Optional.ofNullable(recipe.id()), recipe.value().getCookingTime());
    }

    public GrillCookingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, CompoundTag tag) {
        this(inputs, outputs, location, tag.getDouble("cookTime"));
    }

    public GrillCookingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, double cookTime) {
        super(inputs, outputs, location);
        this.cookTime = cookTime;
    }

    public double getCookTime() {
        return cookTime;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SimpleBBQREIRecipes.GRILL_COOKING;
    }

    public static BasicDisplay.Serializer<GrillCookingDisplay> serializer() {
        return BasicDisplay.Serializer.of(GrillCookingDisplay::new, (display, tag) -> {
            tag.putDouble("cookTime", display.cookTime);
        });
    }
}
