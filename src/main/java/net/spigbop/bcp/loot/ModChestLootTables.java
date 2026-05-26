package net.spigbop.bcp.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.spigbop.bcp.BetterChestProgression;

public class ModChestLootTables {
    public static final ResourceKey<LootTable> END_CITY = create(
        "end_city_progression");

    public static ResourceKey<LootTable> create(String name) {
        return ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(
                BetterChestProgression.MODID,
                "chests/" + name
            )
        );
    }
}
