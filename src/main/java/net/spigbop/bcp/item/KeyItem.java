package net.spigbop.bcp.item;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.spigbop.bcp.block.TreasureChestBlock;
import net.spigbop.bcp.progression.KeyMaterial;

public class KeyItem extends Item {
    private final KeyMaterial material;

    public KeyItem(Properties properties, KeyMaterial material) {
        super(properties);
        this.material = material;
    }

    public KeyMaterial getMaterial() {
        return material;
    }

    @SuppressWarnings("unused")
    public boolean canUnlock(TreasureChestBlock chest) {
        return getMaterial().canSubsideFor(chest.getUnlockMaterial());
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(
        ItemStack s,
        TooltipContext c,
        List<Component> tooltipComponents,
        TooltipFlag tf
    ) {
        tooltipComponents.add(Component.translatable(
                                           "item.bcp.tooltip.key",
                                           this.getMaterial()
                                               .translateChestName()
                                       )
                                       .withStyle(ChatFormatting.GRAY));

        super.appendHoverText(s, c, tooltipComponents, tf);
    }
}
