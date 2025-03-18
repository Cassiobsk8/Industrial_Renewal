package net.cassiokf.industrialrenewal.blockentity.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BETubeBase extends BE6WayConnection {
    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public static final BooleanProperty CUP = BooleanProperty.create("cup");
    public static final BooleanProperty CDOWN = BooleanProperty.create("cdown");
    public static final BooleanProperty CNORTH = BooleanProperty.create("cnorth");
    public static final BooleanProperty CSOUTH = BooleanProperty.create("csouth");
    public static final BooleanProperty CEAST = BooleanProperty.create("ceast");
    public static final BooleanProperty CWEST = BooleanProperty.create("cwest");
    //For PillarPipe
    public static final BooleanProperty WUP = BooleanProperty.create("wup");
    public static final BooleanProperty WDOWN = BooleanProperty.create("wdown");
    public static final BooleanProperty WNORTH = BooleanProperty.create("wnorth");
    public static final BooleanProperty WSOUTH = BooleanProperty.create("wsouth");
    public static final BooleanProperty WEAST = BooleanProperty.create("weast");
    public static final BooleanProperty WWEST = BooleanProperty.create("wwest");
    
    public BETubeBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }
}
