package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.Block3x3x3Base;
import net.cassiokf.industrialrenewal.blockentity.BlockEntitySteamTurbine;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockSteamTurbine extends Block3x3x3Base<BlockEntitySteamTurbine> implements EntityBlock {
    public BlockSteamTurbine(BlockBehaviour.Properties properties) {
        super(properties);
    }

//    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//        //TODO: add to config
//        tooltip.add(new StringTextComponent(
//                I18n.get("info.industrialrenewal.requires")
//                        + ": "
//                        + "Steam"
//                        + " "
//                        + 250//(IRConfig.Main.steamTurbineSteamPerTick.get().toString())
//                        + " mB/t"));
//        tooltip.add(new StringTextComponent(
//                I18n.get("info.industrialrenewal.produces")
//                        + ": "
//                        + 512//(IRConfig.Main.steamTurbineEnergyPerTick.get().toString())
//                        + " FE/t"));
//        super.appendHoverText(stack, worldIn, tooltip, flagIn);
//    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.STEAM_TURBINE_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntitySteamTurbine)blockEntity).tick();
    }
}
