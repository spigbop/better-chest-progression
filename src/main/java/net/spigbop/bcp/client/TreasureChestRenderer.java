package net.spigbop.bcp.client;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.spigbop.bcp.BetterChestProgression;
import net.spigbop.bcp.block.entity.TreasureChestBlockEntity;
import org.jetbrains.annotations.NotNull;

public class TreasureChestRenderer
    extends ChestRenderer<TreasureChestBlockEntity>
{
    public TreasureChestRenderer(
        BlockEntityRendererProvider.Context context
    ) {
        super(context);
    }

    @ParametersAreNonnullByDefault
    @NotNull
    @Override
    protected Material getMaterial(
        TreasureChestBlockEntity blockEntity,
        ChestType chestType
    ) {
        return new Material(
            Sheets.CHEST_SHEET, ResourceLocation.fromNamespaceAndPath(
            BetterChestProgression.MODID,
            "entity/chest/" + blockEntity.getMaterial().getChestTextureName()
        )
        );
    }
}
