package net.spigbop.bcp.event;

import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.spigbop.bcp.BetterChestProgression;

@OnlyIn(Dist.DEDICATED_SERVER)
@EventBusSubscriber(
    modid = BetterChestProgression.MODID,
    value = Dist.DEDICATED_SERVER)
public class KeyDropperEvent {
    private static final List<EntityType<?>> bannedTypes = List.of();

    public static void onKeyDropperDeath(Monster monster) {
        if (monster.isUnderWater()) {
            return;
        }
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Monster monster
                            && !bannedTypes.contains(monster.getType())
        ) {
            onKeyDropperDeath(monster);
        }
    }
}
