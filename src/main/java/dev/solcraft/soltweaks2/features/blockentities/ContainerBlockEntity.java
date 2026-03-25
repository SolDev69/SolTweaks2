package dev.solcraft.soltweaks2.features.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class ContainerBlockEntity extends BlockEntity {

    @Nullable
    private EntityType<?> containedE = null;

    public void setContainedE(EntityType<?> entityType) {
        this.containedE = entityType;
    }
    public EntityType<?> getContainedE() {
        return containedE;
    }
    public ContainerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.CONTAINER_BLOCK_ENTITY, worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        if (containedE != null) {
            output.storeNullable("entity", BuiltInRegistries.ENTITY_TYPE.byNameCodec(), containedE);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        input.read("entity", BuiltInRegistries.ENTITY_TYPE.byNameCodec()).ifPresent(e -> containedE = e);
    }
}
