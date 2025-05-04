package com.sihenzhang.simplebbq.integration.rei;

import com.sihenzhang.simplebbq.integration.rei.display.GrillCookingDisplay;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

public class SimpleBBQREICommonPlugin implements REIServerPlugin {

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(SimpleBBQREIRecipes.GRILL_COOKING, GrillCookingDisplay.serializer());
    }
}
