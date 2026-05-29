package net.spigbop.bcp.progression;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum KeyMaterial {
    BLACKSMITH(
        "blacksmith",
        () -> Component
            .translatable("block.bcp.blacksmith_treasure_chest")
            .withStyle(ChatFormatting.WHITE)
    ),
    ANCIENT(
        "ancient_city",
        () -> Component
            .translatable("block.bcp.ancient_city_treasure_chest")
            .withStyle(ChatFormatting.DARK_BLUE)
    ),
    ENDER(
        "end_city",
        () -> Component
            .translatable("block.bcp.end_city_treasure_chest")
            .withStyle(ChatFormatting.LIGHT_PURPLE)
    ),
    ANY(
        null,
        () -> Component
            .translatable("block.bcp.any_treasure_chest")
            .withStyle(ChatFormatting.LIGHT_PURPLE),
        ENDER,
        BLACKSMITH
    );

    private final String chestTextureName;
    private final Supplier<Component> chestName;
    private final List<KeyMaterial> includes;

    KeyMaterial(
        String chestTextureName,
        Supplier<Component> chestName,
        List<KeyMaterial> includes
    ) {
        this.chestTextureName = chestTextureName;
        this.chestName = chestName;
        this.includes = includes;
    }

    KeyMaterial(
        String chestTextureName,
        Supplier<Component> chestName,
        KeyMaterial... includes
    ) {
        this(chestTextureName, chestName, List.of(includes));
    }

    KeyMaterial(String chestTextureName, Supplier<Component> chestName) {
        this(chestTextureName, chestName, List.of());
    }

    public String getChestTextureName() {
        return this.chestTextureName;
    }

    public Component translateChestName() {
        return this.chestName.get();
    }

    public boolean canSubsideFor(KeyMaterial other) {
        return (this == other || this.includes.contains(other));
    }
}
