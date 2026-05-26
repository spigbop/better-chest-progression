package net.spigbop.bcp.event;

import java.util.Collection;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.loot.ModEntityLootTables;

@EventBusSubscriber(modid = BetterChestProgression.MODID)
public class KeyDropperEvent {
    private static final List<EntityType<?>> bannedTypes = List.of();

    public static void onKeyDropperDeath(
        LivingEntity entity,
        DamageSource source,
        Collection<ItemEntity> drops
    ) {
        ServerLevel level = (ServerLevel) entity.level();

        LootParams params = new LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(LootContextParams.ORIGIN, entity.position())
            .withParameter(LootContextParams.DAMAGE_SOURCE, source)
            .withOptionalParameter(
                LootContextParams.ATTACKING_ENTITY,
                source.getEntity()
            )
            .withOptionalParameter(
                LootContextParams.DIRECT_ATTACKING_ENTITY,
                source.getDirectEntity()
            )
            .create(LootContextParamSets.ENTITY);

        LootTable table = level
            .getServer()
            .reloadableRegistries()
            .getLootTable(ModEntityLootTables.BLACKSMITH_KEY);

        BetterChestProgression.LOGGER.info("mixing blacksmith key loot table");

        table.getRandomItems(params).forEach(itemStack -> {
            drops.add(new ItemEntity(
                level,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                itemStack
            ));
        });
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        BetterChestProgression.LOGGER.info("some guy is dropping drops");
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide) {
            return;
        }

        EntityType<?> type = entity.getType();
        if (type.getCategory() == MobCategory.MONSTER &&
            !bannedTypes.contains(type)) {
            onKeyDropperDeath(entity, event.getSource(), event.getDrops());
        }
    }
}
