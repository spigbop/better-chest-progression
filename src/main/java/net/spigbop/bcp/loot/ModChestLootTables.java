package net.spigbop.bcp.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.spigbop.bcp.BetterChestProgression;

public class ModChestLootTables {
    public static final ResourceKey<LootTable> END_CITY = create(
        "end_city_progression"
    );
    public static final ResourceKey<LootTable> MENDING_BOOK = create(
        "mending_book"
    );
    public static final ResourceKey<LootTable> MENDING_BOOK_COMMON = create(
        "mending_book_common"
    );

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
