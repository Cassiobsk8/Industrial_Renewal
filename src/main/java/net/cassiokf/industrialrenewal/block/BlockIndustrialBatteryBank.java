package net.cassiokf.industrialrenewal.block;

import net.cassiokf.industrialrenewal.block.abstracts.BlockTowerBase;
import net.cassiokf.industrialrenewal.blockentity.BlockEntityIndustrialBatteryBank;
import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityTowerBase;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockIndustrialBatteryBank extends BlockTowerBase<BlockEntityIndustrialBatteryBank> implements EntityBlock {
    public BlockIndustrialBatteryBank(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!world.isClientSide()){
            super.setPlacedBy(world, pos, state, livingEntity, itemStack);
            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);
            for(BlockPos blockPos : blocks){
                BlockEntity te = world.getBlockEntity(blockPos);
                if(te instanceof BlockEntityIndustrialBatteryBank bankTE && ((BlockEntityTowerBase<?>)te).isMaster()){

                    bankTE.setSelfBooleanProperty();
                    bankTE.setOtherBooleanProperty(TOP, false, false);
                    bankTE.setOtherBooleanProperty(BASE, false, true);
                    //bankTE.getBase().addToTower(bankTE, bankTE.getAbove()!=null? bankTE.getAbove().getMaster().tower : null);
                    bankTE.getBase().loadTower();
                }
            }
        }
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        if(!world.isClientSide()){
            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);
            for(BlockPos blockPos : blocks){
                BlockEntity te = world.getBlockEntity(blockPos);
                if(te instanceof BlockEntityIndustrialBatteryBank bankTE && ((BlockEntityTowerBase)te).isMaster()){
                    popResource((Level)world, te.getBlockPos(), new ItemStack(ModItems.BATTERY_LITHIUM.get(), ((BlockEntityIndustrialBatteryBank)te).getBatteries()));
                    bankTE.setOtherBooleanProperty(TOP, true, false);
                    bankTE.setOtherBooleanProperty(BASE, true, true);
                    if(!bankTE.isBase()){
                        bankTE.getBase().removeTower(bankTE);
                    }
                    if(bankTE.getAbove() != null){
                        //bankTE.getAbove().tower = new ArrayList<>();
                        bankTE.getAbove().loadTower();
                    }
                }
            }
        }
        super.destroy(world, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide){
            //Utils.debug("USE ON BATTERYBANK BLOCK");
            ItemStack stack = player.getItemInHand(handIn);
            if (stack.getItem().equals(ModItems.BATTERY_LITHIUM.get())) {
                BlockEntity te = worldIn.getBlockEntity(pos);
                //Utils.debug("PLACING",te instanceof TileEntityIndustrialBatteryBank);
                if (te instanceof BlockEntityIndustrialBatteryBank) {
                    if (((BlockEntityIndustrialBatteryBank) te).getMaster().placeBattery(player, stack))
                        return InteractionResult.PASS;
                }
            } else if (stack.getItem().equals(ModItems.SCREW_DRIVER.get())) {
                BlockEntity te = worldIn.getBlockEntity(pos);
                //Utils.debug("REMOVING",te instanceof TileEntityIndustrialBatteryBank);
                if (te instanceof BlockEntityIndustrialBatteryBank bb) {
                    if (bb.getMaster().removeBattery(player))
                        return InteractionResult.PASS;
                }
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.INDUSTRIAL_BATTERY_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityIndustrialBatteryBank)blockEntity).tick();
    }
}
