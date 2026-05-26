package net.spigbop.bcp.item;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.block.ModBlocks;

public class ModCreativeModTabs {
    protected static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB,
        BetterChestProgression.MODID
    );

    public static final Supplier<CreativeModeTab> CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.register(
        BetterChestProgression.MODNAME,
        () -> CreativeModeTab
            .builder()
            .title(Component.translatable("itemGroup.bcp"))
            .icon(() -> new ItemStack(
                ModBlocks.END_CITY_TREASURE_CHEST_ITEM.get(),
                1
            ))
            .displayItems(List.of(ModItems.BLACKSMITH_KEY, ModItems.ENDER_KEY))
            .build()
    );

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
