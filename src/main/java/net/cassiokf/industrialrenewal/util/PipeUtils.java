package net.cassiokf.industrialrenewal.util;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import net.cassiokf.industrialrenewal.obj.CapResult;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Map;

public class PipeUtils {
    
    public static CapResult moveEnergy(BlockEntityMultiBlocksTube machine, int maxReceive, int maxEnergyCanTransport, boolean simulate) {
        CapResult result = new CapResult(0, 0);
        
        if (maxReceive > 0 && !machine.getReceiversContainers().isEmpty()) {
            result = doMoveEnergy(machine, maxReceive, maxEnergyCanTransport, simulate);
        }
        
        return result;
    }
    
    public static CapResult outputFluid(BlockEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, IFluidHandler.FluidAction action, Level level) {
        CapResult result = new CapResult(0, 0);
        int quantity = machine.getReceiversContainers().size();
        if (quantity > 0 && resource != null && resource.getAmount() > 0) {
            result = moveFluid(machine, resource, maxFluidCanTransport, action);
        }
        return result;
    }
    
    
    private static CapResult doMoveEnergy(BlockEntityMultiBlocksTube machine, int amount, int maxEnergyCanTransport, boolean simulate) {
        if (machine == null) return new CapResult(0, 0);
        
        Map<BlockEntity, Direction> mapPosSet = machine.getReceiversContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return new CapResult(0, 0);
    
        int leftEnergy = amount;
        int out = 0;
        int validOutputs = 0;
    
        for (Map.Entry<BlockEntity, Direction> entry : mapPosSet.entrySet()) {
            BlockEntity te = entry.getKey();
            if (te != null && !te.isRemoved()) {
                Direction face = entry.getValue().getOpposite();
                IEnergyStorage energyStorage = te.getCapability(ForgeCapabilities.ENERGY, face).orElse(null);
                if (energyStorage != null && energyStorage.canReceive()) {
                    int realMaxOutput = machine.getLimitedValueForOutPut(leftEnergy / mapPosSet.size(), maxEnergyCanTransport, te, simulate);
                    if (realMaxOutput > 0) {
                        int energy = energyStorage.receiveEnergy(realMaxOutput, simulate);
                        if (!simulate) leftEnergy -= energy;
                        out += energy;
                        validOutputs++;
                    }
                }
            }
        }
    
        return new CapResult(out, validOutputs);
    }
    
    private static CapResult moveFluid(BlockEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, IFluidHandler.FluidAction action) {
        if (machine == null || resource == null || resource.getAmount() <= 0) {
            return new CapResult(0, 0);
        }
    
        CapResult result = new CapResult(0, 0);
        final Map<BlockEntity, Direction> mapPosSet = machine.getReceiversContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) {
            return result;
        }
    
        int validOutputs = 0;
        int leftOutput = mapPosSet.size();
        int leftFluid = resource.getAmount();
        int out = 0;
    
        for (Map.Entry<BlockEntity, Direction> entry : mapPosSet.entrySet()) {
            BlockEntity te = entry.getKey();
            if (te != null && !te.isRemoved()) {
                Direction face = entry.getValue().getOpposite();
                IFluidHandler tankStorage = te.getCapability(ForgeCapabilities.FLUID_HANDLER, face).orElse(null);
                if (tankStorage != null) {
                    int realMaxOutputAmount = Math.min(resource.getAmount(), maxFluidCanTransport);
                    FluidStack realMaxOutput = new FluidStack(resource.getFluid(), realMaxOutputAmount);
                    realMaxOutput.setAmount(machine.getLimitedValueForOutPut(leftFluid / leftOutput, maxFluidCanTransport, te, action.equals(IFluidHandler.FluidAction.SIMULATE)));
                    if (realMaxOutput.getAmount() > 0) {
                        int fluid = tankStorage.fill(realMaxOutput, action);
                        out += fluid;
                        if (action.equals(IFluidHandler.FluidAction.EXECUTE)) {
                            leftFluid -= fluid;
                        }
                        validOutputs++;
                    }
                }
            }
            leftOutput--;
        }
    
        result.setOutPut(out);
        result.setValidReceivers(validOutputs);
        return result;
    }
}
