package net.spigbop.bcp.block.entity;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.block.ModBlocks;

public class ModBlockEntityTypes {
    protected static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE,
                                                                                                             BetterChestProgression.MODID
    );

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }

    public static final Supplier<BlockEntityType<TreasureChestBlockEntity>> TREASURE_CHEST = BLOCK_ENTITY_TYPES.register(
        "treasure_chest", () -> BlockEntityType.Builder.of(
            TreasureChestBlockEntity::new,
            ModBlocks.BLACKSMITH_TREASURE_CHEST.get(),
            ModBlocks.END_CITY_TREASURE_CHEST.get()
        ).build(null)
    );
}
