package dev.solcraft.soltweaks2.modblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class ContainerBlock extends Block
{
    private @Nullable EntityType<?> containedE;

    public ContainerBlock(Properties properties) {
        super(properties);
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
        System.out.println("used item");
        if (itemStack.getItem() instanceof SpawnEggItem spawnEggItem && this.containedE == null) {
            System.out.println("spawn egg");
            this.containedE = spawnEggItem.getType(itemStack);
        }
        System.out.println("consume");
        return InteractionResult.CONSUME;
    }

    @Override
    public void playerDestroy(
            final Level level, final Player player, final BlockPos pos, final BlockState state, @Nullable final BlockEntity blockEntity, final ItemStack destroyedWith
    ) throws NullPointerException {
        super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
        if (containedE != null) {
            containedE.spawn((ServerLevel) level, pos, EntitySpawnReason.SPAWN_ITEM_USE);
            containedE = null;
        }
    }


}