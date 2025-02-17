package com.sihenzhang.simplebbq.integration.jei;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import com.sihenzhang.simplebbq.util.I18nUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class SkeweringCategory extends BaseCategory<SkeweringRecipe> {
    public static final RecipeType<SkeweringRecipe> RECIPE_TYPE = RecipeType.create(SimpleBBQ.MOD_ID, "skewering", SkeweringRecipe.class);


    public SkeweringCategory(IGuiHelper guiHelper) {
        super(guiHelper.drawableBuilder(ModIntegrationJei.RECIPE_GUI_VANILLA, 0, 168, 125, 18).build()
        ,guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM.get().getDefaultInstance()));
    }

    @Override
    public RecipeType<SkeweringRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "category.skewering");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SkeweringRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(Ingredient.of(SimpleBBQItemTags.SKEWER));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStack(RecipeUtil.getResultItem(recipe));
    }
}
