package net.spigbop.bcp.block.entity;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.spigbop.bcp.block.TreasureChestBlock;
import net.spigbop.bcp.progression.KeyMaterial;
import org.jetbrains.annotations.NotNull;

public class TreasureChestBlockEntity extends ChestBlockEntity {
    private boolean unlocked = false;

    public TreasureChestBlockEntity(
        BlockPos pos,
        BlockState blockState
    ) {
        super(ModBlockEntityTypes.TREASURE_CHEST.get(), pos, blockState);
    }

    public TreasureChestBlock getBlock() {
        return (TreasureChestBlock) this.getBlockState().getBlock();
    }

    public KeyMaterial getMaterial() {
        return getBlock().getUnlockMaterial();
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.bcp.treasure_chest");
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public boolean isLocked() {
        return !this.unlocked;
    }

    public void unlock() {
        this.unlocked = true;
        setChanged();
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void saveAdditional(
        CompoundTag tag,
        HolderLookup.Provider registries
    ) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("treasure_unlocked", unlocked);
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void loadAdditional(
        CompoundTag tag,
        HolderLookup.Provider registries
    ) {
        super.loadAdditional(tag, registries);
        this.unlocked = tag.getBoolean("treasure_unlocked");
    }
}
