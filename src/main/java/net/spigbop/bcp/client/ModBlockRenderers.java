package net.spigbop.bcp.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.block.entity.ModBlockEntityTypes;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = BetterChestProgression.MODID, value = Dist.CLIENT)
public class ModBlockRenderers {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
            ModBlockEntityTypes.TREASURE_CHEST.get(),
            TreasureChestRenderer::new
        );
    }
}
