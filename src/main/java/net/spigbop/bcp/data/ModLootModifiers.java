package net.spigbop.bcp.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.loot.ModChestLootTables;
import net.spigbop.bcp.loot.ModEntityLootTables;

public class ModLootModifiers extends GlobalLootModifierProvider {
    public ModLootModifiers(
        PackOutput output,
        CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(output, registries, BetterChestProgression.MODID);
    }

    private AddTableLootModifier addModifier(
        ResourceKey<LootTable> original,
        ResourceKey<LootTable> patch
    ) {
        return new AddTableLootModifier(
            new LootItemCondition[]{
                LootTableIdCondition.builder(original.location()).build()
            }, patch
        );
    }

    private ResourceKey<LootTable> getVanillaKey(String path) {
        return ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.withDefaultNamespace(path)
        );
    }

    @Override
    protected void start() {
        this.add(
            "add_ancient_city_mending",
            this.addModifier(
                BuiltInLootTables.ANCIENT_CITY,
                ModChestLootTables.MENDING_BOOK
            )
        );
        this.add(
            "add_end_city_mending",
            this.addModifier(
                BuiltInLootTables.END_CITY_TREASURE,
                ModChestLootTables.MENDING_BOOK_COMMON
            )
        );
        this.add(
            "add_end_city_progression",
            this.addModifier(
                BuiltInLootTables.END_CITY_TREASURE,
                ModChestLootTables.END_CITY
            )
        );
        this.add(
            "add_ender_dragon",
            this.addModifier(
                getVanillaKey("entities/ender_dragon"),
                ModEntityLootTables.ENDER_DRAGON
            )
        );
    }
}
