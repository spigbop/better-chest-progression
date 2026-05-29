package net.spigbop.bcp.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.progression.KeyMaterial;

public class ModItems {
    protected static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(
        BetterChestProgression.MODID);

    public static final DeferredItem<KeyItem> BLACKSMITH_KEY = ITEMS.register(
        "blacksmith_key", () -> new KeyItem(
            new Item.Properties().durability(4).setNoRepair().stacksTo(1),
            KeyMaterial.BLACKSMITH
        )
    );
    public static final DeferredItem<KeyItem> STAR_KEY = ITEMS.register(
        "star_key", () -> new KeyItem(
            new Item.Properties()
                .durability(16)
                .setNoRepair()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant(), KeyMaterial.ANCIENT
        )
    );
    public static final DeferredItem<KeyItem> ENDER_KEY = ITEMS.register(
        "ender_key", () -> new KeyItem(
            new Item.Properties()
                .durability(128)
                .setNoRepair()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant(), KeyMaterial.ENDER
        )
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
