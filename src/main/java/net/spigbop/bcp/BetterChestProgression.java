package net.spigbop.bcp;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.spigbop.bcp.block.ModBlocks;
import net.spigbop.bcp.block.entity.ModBlockEntityTypes;
import net.spigbop.bcp.item.ModCreativeModTabs;
import net.spigbop.bcp.item.ModItems;
import org.slf4j.Logger;

@Mod(BetterChestProgression.MODID)
public class BetterChestProgression {
    public static final String MODID = "bcp";
    public static final String MODNAME = "better_chest_progression";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterChestProgression(IEventBus bus, ModContainer _c) {
        ModItems.register(bus);
        ModBlocks.register(bus);
        ModBlockEntityTypes.register(bus);
        ModCreativeModTabs.register(bus);
    }
}
