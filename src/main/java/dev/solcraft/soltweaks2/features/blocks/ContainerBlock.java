package dev.solcraft.soltweaks2.features.blocks;

import com.mojang.serialization.MapCodec;
import dev.solcraft.soltweaks2.features.blockentities.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class ContainerBlock extends BaseEntityBlock
{
    public static final BooleanProperty FILLED = BooleanProperty.create("full");

    public ContainerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FILLED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ContainerBlock::new);
    }

    private void change(BlockState state, Level level, BlockPos pos, Player player) {
        // Get the current value of the "filled" property
        boolean filled = state.getValue(FILLED);

        // Flip the value of activated and save the new blockstate.
        level.setBlockAndUpdate(pos, state.setValue(FILLED, !filled));

        // Play a click sound to emphasise the interaction.
        level.playSound(player, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected InteractionResult useItemOn(
            final ItemStack itemStack,
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final Player player,
            final InteractionHand hand,
            final BlockHitResult hitResult
    ) {
        super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
        if (itemStack.getItem() instanceof SpawnEggItem spawnEggItem && level.getBlockEntity(pos) instanceof ContainerBlockEntity containerBlockEntity) {
            System.out.println("used item");
            containerBlockEntity.setContainedE(spawnEggItem.getType(itemStack));
            itemStack.consume(1, player);
            change(state, level, pos, player);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void playerDestroy(
            final Level level, final Player player, final BlockPos pos, final BlockState state, @Nullable final BlockEntity blockEntity, final ItemStack destroyedWith
    ) throws NullPointerException {
        ContainerBlockEntity containerBlockEntity = (ContainerBlockEntity) blockEntity;
        if (!destroyedWith.is(Items.SHEARS) && containerBlockEntity.getContainedE() != null) {
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.005F);
            // Explicity do not drop anything, unlike `super.playerDestroy`

            System.out.println("break1");
            System.out.println("break2");
            containerBlockEntity.getContainedE().spawn((ServerLevel) level, pos, EntitySpawnReason.SPAWN_ITEM_USE);
            containerBlockEntity.setContainedE(null);
        } else {
            super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }


    @Override
    protected void onPlace(final BlockState state, final Level level, final BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ContainerBlockEntity(worldPosition, blockState);
    }
}