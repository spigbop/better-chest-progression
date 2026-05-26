package net.spigbop.bcp.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.block.entity.ModBlockEntityTypes;
import net.spigbop.bcp.progression.KeyMaterial;

public class ModBlocks {
    protected static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(
        BetterChestProgression.MODID);

    public static final DeferredBlock<TreasureChestBlock> BLACKSMITH_TREASURE_CHEST =
        BLOCKS.register(
            "blacksmith_treasure_chest", () -> new TreasureChestBlock(
                BlockBehaviour.Properties.of()
                                         .mapColor(MapColor.COLOR_GRAY)
                                         .instrument(NoteBlockInstrument.BASEDRUM)
                                         .requiresCorrectToolForDrops()
                                         .strength(1.5F, 6.0F),
                ModBlockEntityTypes.TREASURE_CHEST::get,
                KeyMaterial.BLACKSMITH
            )
        );
    public static final DeferredBlock<TreasureChestBlock> END_CITY_TREASURE_CHEST = BLOCKS.register(
        "end_city_treasure_chest", () -> new TreasureChestBlock(
            BlockBehaviour.Properties.of()
                                     .mapColor(MapColor.COLOR_PURPLE)
                                     .instrument(NoteBlockInstrument.BASEDRUM)
                                     .requiresCorrectToolForDrops()
                                     .strength(1.5F, 6.0F),
            ModBlockEntityTypes.TREASURE_CHEST::get,
            KeyMaterial.ENDER
        )
    );

    protected static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(
        BetterChestProgression.MODID);

    public static final DeferredItem<BlockItem> BLACKSMITH_TREASURE_CHEST_ITEM =
        ITEMS.registerSimpleBlockItem(
            BLACKSMITH_TREASURE_CHEST
        );
    public static final DeferredItem<BlockItem> END_CITY_TREASURE_CHEST_ITEM =
        ITEMS.registerSimpleBlockItem(
            END_CITY_TREASURE_CHEST
        );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
