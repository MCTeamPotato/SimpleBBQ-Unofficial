package com.sihenzhang.simplebbq.integration.rei;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.integration.rei.display.CampfireCookingOnGrillDisplay;
import com.sihenzhang.simplebbq.integration.rei.display.GrillCookingDisplay;
import com.sihenzhang.simplebbq.integration.rei.display.SeasoningDisplay;
import com.sihenzhang.simplebbq.integration.rei.display.SkeweringDisplay;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;

public class SimpleBBQREIRecipes {
    public static final CategoryIdentifier<SkeweringDisplay> SKEWERING = createCategoryIdentifier("skewering");
    public static final CategoryIdentifier<SeasoningDisplay> SEASONING = createCategoryIdentifier("seasoning");
    public static final CategoryIdentifier<GrillCookingDisplay> GRILL_COOKING = createCategoryIdentifier("grill_cooking");
    public static final CategoryIdentifier<CampfireCookingOnGrillDisplay> CAMPFIRE_COOKING_ON_GRILL = createCategoryIdentifier("campfire_cooking_on_grill");

    protected static <D extends Display> CategoryIdentifier<D> createCategoryIdentifier(String str) {
        return CategoryIdentifier.of(SimpleBBQ.MOD_ID, str);
    }
}
