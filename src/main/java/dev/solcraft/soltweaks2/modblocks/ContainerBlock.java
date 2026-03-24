package dev.solcraft.soltweaks2.modblocks;

import net.minecraft.world.level.block.Block;

public class ContainerBlock extends Block
{
    private net.minecraft.world.entity.Entity containedE;

    public ContainerBlock(Properties properties) {
        super(properties);
    }

}