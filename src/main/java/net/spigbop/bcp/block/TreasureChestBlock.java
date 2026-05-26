package net.spigbop.bcp.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.spigbop.bcp.block.entity.ModBlockEntityTypes;
import net.spigbop.bcp.block.entity.TreasureChestBlockEntity;
import net.spigbop.bcp.item.KeyItem;
import net.spigbop.bcp.progression.KeyMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreasureChestBlock
    extends AbstractChestBlock<TreasureChestBlockEntity>
    implements SimpleWaterloggedBlock
{
    //region copy from ChestBlock
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    @SuppressWarnings("unused")
    protected static final int AABB_OFFSET = 1;
    @SuppressWarnings("unused")
    protected static final int AABB_HEIGHT = 14;
    protected static final VoxelShape NORTH_AABB = Block.box(
        1.0F,
        0.0F,
        0.0F,
        15.0F,
        14.0F,
        15.0F
    );
    protected static final VoxelShape SOUTH_AABB = Block.box(
        1.0F,
        0.0F,
        1.0F,
        15.0F,
        14.0F,
        16.0F
    );
    protected static final VoxelShape WEST_AABB = Block.box(
        0.0F,
        0.0F,
        1.0F,
        15.0F,
        14.0F,
        15.0F
    );
    protected static final VoxelShape EAST_AABB = Block.box(
        1.0F,
        0.0F,
        1.0F,
        16.0F,
        14.0F,
        15.0F
    );
    protected static final VoxelShape AABB = Block.box(
        1.0F,
        0.0F,
        1.0F,
        15.0F,
        14.0F,
        15.0F
    );

    public final MapCodec<TreasureChestBlock> mapCodec;

    @ParametersAreNonnullByDefault
    public static DoubleBlockCombiner.Combiner<TreasureChestBlockEntity, Float2FloatFunction> opennessCombiner(
        final LidBlockEntity lid
    ) {
        return new DoubleBlockCombiner.Combiner<>() {
            public @NotNull Float2FloatFunction acceptDouble(
                TreasureChestBlockEntity p_51633_,
                TreasureChestBlockEntity p_51634_
            ) {
                return (p_51638_) -> Math.max(
                    p_51633_.getOpenNess(p_51638_),
                    p_51634_.getOpenNess(p_51638_)
                );
            }

            public @NotNull Float2FloatFunction acceptSingle(
                TreasureChestBlockEntity p_51631_
            ) {
                Objects.requireNonNull(p_51631_);
                return p_51631_::getOpenNess;
            }

            public @NotNull Float2FloatFunction acceptNone() {
                Objects.requireNonNull(lid);
                return lid::getOpenNess;
            }
        };
    }

    public static boolean isChestBlockedAt(LevelAccessor level, BlockPos pos) {
        return isBlockedChestByBlock(level, pos) ||
               isCatSittingOnChest(level, pos);
    }

    private static boolean isBlockedChestByBlock(
        BlockGetter level,
        BlockPos pos
    ) {
        BlockPos blockpos = pos.above();
        return level
            .getBlockState(blockpos)
            .isRedstoneConductor(level, blockpos);
    }

    private static boolean isCatSittingOnChest(
        LevelAccessor level,
        BlockPos pos
    ) {
        List<Cat> list = level.getEntitiesOfClass(
            Cat.class, new AABB(
                pos.getX(),
                pos.getY() + 1,
                pos.getZ(),
                pos.getX() + 1,
                pos.getY() + 2,
                pos.getZ() + 1
            )
        );
        if (!list.isEmpty()) {
            for (Cat cat : list) {
                if (cat.isInSittingPose()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void createBlockStateDefinition(
        StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(FACING);
        builder.add(TYPE);
        builder.add(WATERLOGGED);
    }

    @ParametersAreNonnullByDefault
    protected @NotNull BlockState updateShape(
        BlockState state,
        Direction facing,
        BlockState facingState,
        LevelAccessor level,
        BlockPos currentPos,
        BlockPos facingPos
    ) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(
                currentPos,
                Fluids.WATER,
                Fluids.WATER.getTickDelay(level)
            );
        }

        if (facingState.is(this) && facing.getAxis().isHorizontal()) {
            ChestType chesttype = facingState.getValue(TYPE);
            if (state.getValue(TYPE) == ChestType.SINGLE &&
                chesttype != ChestType.SINGLE &&
                state.getValue(FACING) == facingState.getValue(FACING) &&
                ChestBlock.getConnectedDirection(facingState) ==
                facing.getOpposite()) {
                return state.setValue(TYPE, chesttype.getOpposite());
            }
        } else if (ChestBlock.getConnectedDirection(state) == facing) {
            return state.setValue(TYPE, ChestType.SINGLE);
        }

        return super.updateShape(
            state,
            facing,
            facingState,
            level,
            currentPos,
            facingPos
        );
    }

    @ParametersAreNonnullByDefault
    protected @NotNull VoxelShape getShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context
    ) {
        if (state.getValue(TYPE) == ChestType.SINGLE) {
            return AABB;
        } else {
            return switch (ChestBlock.getConnectedDirection(state)) {
                case SOUTH -> SOUTH_AABB;
                case WEST -> WEST_AABB;
                case EAST -> EAST_AABB;
                default -> NORTH_AABB;
            };
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ChestType chesttype = ChestType.SINGLE;
        Direction direction = context.getHorizontalDirection().getOpposite();
        FluidState fluidstate = context
            .getLevel()
            .getFluidState(context.getClickedPos());
        boolean flag = context.isSecondaryUseActive();
        Direction direction1 = context.getClickedFace();
        if (direction1.getAxis().isHorizontal() && flag) {
            Direction direction2 = this.candidatePartnerFacing(
                context,
                direction1.getOpposite()
            );
            if (direction2 != null &&
                direction2.getAxis() != direction1.getAxis()) {
                direction = direction2;
                chesttype = direction2.getCounterClockWise() ==
                            direction1.getOpposite()
                    ? ChestType.RIGHT
                    : ChestType.LEFT;
            }
        }

        if (chesttype == ChestType.SINGLE && !flag) {
            if (direction ==
                this.candidatePartnerFacing(
                    context,
                    direction.getClockWise()
                )) {
                chesttype = ChestType.LEFT;
            } else if (direction ==
                       this.candidatePartnerFacing(
                           context,
                           direction.getCounterClockWise()
                       )) {
                chesttype = ChestType.RIGHT;
            }
        }

        return this
            .defaultBlockState()
            .setValue(FACING, direction)
            .setValue(TYPE, chesttype)
            .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Nullable
    private Direction candidatePartnerFacing(
        BlockPlaceContext context,
        Direction direction
    ) {
        BlockState blockstate = context
            .getLevel()
            .getBlockState(context.getClickedPos().relative(direction));
        return blockstate.is(this) &&
               blockstate.getValue(TYPE) == ChestType.SINGLE
            ? blockstate.getValue(FACING)
            : null;
    }

    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
            ? Fluids.WATER.getSource(false)
            : super.getFluidState(state);
    }

    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    public KeyMaterial getUnlockMaterial() {
        return unlockMaterial;
    }

    @Override
    protected @NotNull MapCodec<? extends AbstractChestBlock<TreasureChestBlockEntity>> codec() {
        return mapCodec;
    }

    @ParametersAreNonnullByDefault
    @NotNull
    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends TreasureChestBlockEntity> combine(
        BlockState blockState,
        Level level,
        BlockPos blockPos,
        boolean b
    ) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    public BlockEntityType<? extends TreasureChestBlockEntity> blockEntityType() {
        return this.blockEntityType.get();
    }

    @ParametersAreNonnullByDefault
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState state,
        BlockEntityType<T> blockEntityType
    ) {
        return level.isClientSide ? createTickerHelper(
            blockEntityType,
            this.blockEntityType(),
            ChestBlockEntity::lidAnimateTick
        ) : null;
    }

    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        BlockState rotated = state.rotate(mirror.getRotation(state.getValue(
            FACING)));
        return mirror == Mirror.NONE
            ? rotated
            : rotated.setValue(TYPE, rotated.getValue(TYPE).getOpposite());
    }

    @ParametersAreNonnullByDefault
    protected boolean isPathfindable(
        BlockState state,
        PathComputationType pathComputationType
    ) {
        return false;
    }

    @ParametersAreNonnullByDefault
    protected void tick(
        BlockState state,
        ServerLevel level,
        BlockPos pos,
        RandomSource random
    ) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof ChestBlockEntity) {
            ((ChestBlockEntity) blockentity).recheckOpen();
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable BlockEntity newBlockEntity(
        BlockPos blockPos,
        BlockState blockState
    ) {
        return new TreasureChestBlockEntity(blockPos, blockState);
    }
    //endregion

    private final KeyMaterial unlockMaterial;

    public TreasureChestBlock(
        Properties properties,
        Supplier<BlockEntityType<? extends TreasureChestBlockEntity>> blockEntityType,
        KeyMaterial unlockMaterial
    ) {
        super(properties, blockEntityType);
        this.unlockMaterial = unlockMaterial;
        this.mapCodec = simpleCodec((p) -> new TreasureChestBlock(
            p,
            ModBlockEntityTypes.TREASURE_CHEST::get,
            unlockMaterial
        ));

        registerDefaultState(this
            .getStateDefinition()
            .any()
            .setValue(FACING, Direction.NORTH)
            .setValue(TYPE, ChestType.SINGLE)
            .setValue(WATERLOGGED, false));
    }

    public boolean canBeUnlockedBy(KeyItem item) {
        return item.getMaterial().canSubsideFor(this.getUnlockMaterial());
    }

    private static void failUse(Player player, Level level, BlockPos pos) {
        if (player != null) {
            level.playSound(
                null,
                pos,
                SoundEvents.CHEST_LOCKED,
                SoundSource.BLOCKS
            );
            player.displayClientMessage(
                Component.translatable("block.bcp.chest_needs_key"), true);
        }
    }

    private static void unlock(
        Player player,
        Level level,
        BlockPos pos,
        ItemStack item,
        InteractionHand hand,
        TreasureChestBlockEntity entity
    ) {
        if (player != null) {
            level.playSound(
                null,
                pos,
                SoundEvents.CHEST_LOCKED,
                SoundSource.BLOCKS
            );
            item.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        entity.unlock();
    }

    private void open(
        Player player,
        BlockState state,
        Level level,
        BlockPos pos
    ) {
        if (isChestBlockedAt(level, pos)) {
            return;
        }

        MenuProvider menuProvider = getMenuProvider(state, level, pos);
        if (menuProvider != null) {
            player.openMenu(menuProvider);
            player.awardStat(this.getOpenChestStat());
            PiglinAi.angerNearbyPiglins(player, true);
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    protected @NotNull ItemInteractionResult useItemOn(
        ItemStack stack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult _h
    ) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        TreasureChestBlockEntity entity = level
            .getBlockEntity(pos, ModBlockEntityTypes.TREASURE_CHEST.get())
            .orElse(null);

        if (entity != null) {
            if (entity.isLocked()) {
                if (stack.getItem() instanceof KeyItem key &&
                    this.canBeUnlockedBy(key)) {
                    unlock(player, level, pos, stack, hand, entity);
                } else {
                    failUse(player, level, pos);
                }
                return ItemInteractionResult.SUCCESS;
            }

            open(player, state, level, pos);
        }

        return ItemInteractionResult.SUCCESS;
    }

    @ParametersAreNonnullByDefault
    @Override
    protected @NotNull InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult _h
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        TreasureChestBlockEntity entity = level
            .getBlockEntity(pos, ModBlockEntityTypes.TREASURE_CHEST.get())
            .orElse(null);
        if (entity != null) {
            if (entity.isLocked()) {
                failUse(player, level, pos);
                return InteractionResult.FAIL;
            }

            open(player, state, level, pos);
        }

        return InteractionResult.SUCCESS;
    }

    @ParametersAreNonnullByDefault
    protected void onRemove(
        BlockState state,
        Level level,
        BlockPos pos,
        BlockState newState,
        boolean isMoving
    ) {
        TreasureChestBlockEntity entity = level
            .getBlockEntity(pos, ModBlockEntityTypes.TREASURE_CHEST.get())
            .orElse(null);
        if (entity == null || entity.isUnlocked()) {
            Containers.dropContentsOnDestroy(state, newState, level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
