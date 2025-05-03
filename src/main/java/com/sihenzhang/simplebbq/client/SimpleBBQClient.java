package com.sihenzhang.simplebbq.client;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.client.particle.CampfireSmokeUnderGrillParticle;
import com.sihenzhang.simplebbq.client.renderer.blockentity.GrillRenderer;
import com.sihenzhang.simplebbq.client.renderer.blockentity.SkeweringTableRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class SimpleBBQClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(SimpleBBQRegistry.GRILL_BLOCK, RenderType.cutout());
        BlockEntityRenderers.register(SimpleBBQRegistry.GRILL_BLOCK_ENTITY, GrillRenderer::new);
        BlockEntityRenderers.register(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ENTITY, SkeweringTableRenderer::new);
        ParticleFactoryRegistry.getInstance().register(SimpleBBQRegistry.CAMPFIRE_SMOKE_UNDER_GRILL, CampfireSmokeUnderGrillParticle.Provider::new);

        SeasoningTooltip.onTooltip();
    }
}
