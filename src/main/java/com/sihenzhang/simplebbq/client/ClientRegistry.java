package com.sihenzhang.simplebbq.client;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.client.particle.CampfireSmokeUnderGrillParticle;
import com.sihenzhang.simplebbq.client.renderer.blockentity.GrillRenderer;
import com.sihenzhang.simplebbq.client.renderer.blockentity.SkeweringTableRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = SimpleBBQ.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemBlockRenderTypes.setRenderLayer(SimpleBBQRegistry.GRILL_BLOCK.get(), RenderType.cutout()));
    }

    @SubscribeEvent
    public static void onRendererRegister(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(SimpleBBQRegistry.GRILL_BLOCK_ENTITY.get(), GrillRenderer::new);
        event.registerBlockEntityRenderer(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ENTITY.get(), SkeweringTableRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onParticleRegister(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(SimpleBBQRegistry.CAMPFIRE_SMOKE_UNDER_GRILL.get(), CampfireSmokeUnderGrillParticle.Provider::new);
    }
}
