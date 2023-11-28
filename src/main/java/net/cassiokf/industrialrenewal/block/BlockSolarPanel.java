package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.IRBaseBlock;
import net.cassiokf.industrialrenewal.blockentity.BlockEntitySolarPanel;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class BlockSolarPanel extends IRBaseBlock implements EntityBlock {

    public BlockSolarPanel(Properties props) {
        super(props.noOcclusion());
    }

    public BlockSolarPanel() {
        super(metalBasicProperties.sound(SoundType.GLASS).strength(2f).noOcclusion());
    }

    protected static final AABB BLOCK_AABB = new AABB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);

    private static final Optional<VoxelShape> SHAPE = Stream.of(
            Block.box(0.5, 0, 0.5, 15.5, 1, 15.5),
            Block.box(0, 0, 0, 16, 1.2, 0.5),
            Block.box(0, 0, 15.5, 16, 1.2, 16),
            Block.box(0, 0, 0.5, 0.5, 1.2, 15.5),
            Block.box(15.5, 0, 0.5, 16, 1.2, 15.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    @Deprecated
    public boolean isOpaqueCube(final BlockState state) {
        return false;
    }

    @Deprecated
    public boolean isFullCube(final BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE.orElse(null);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.SOLAR_PANEL.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntitySolarPanel)blockEntity).tick();
    }
}
