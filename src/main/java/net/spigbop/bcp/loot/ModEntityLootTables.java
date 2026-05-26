package net.spigbop.bcp.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.spigbop.bcp.BetterChestProgression;

public class ModEntityLootTables {
    public static final ResourceKey<LootTable> ENDER_DRAGON = create(
        "ender_dragon");

    public static ResourceKey<LootTable> create(String name) {
        return ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(
                BetterChestProgression.MODID,
                "entities/" + name
            )
        );
    }
}
