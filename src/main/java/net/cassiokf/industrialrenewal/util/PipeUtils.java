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
    
    public static CapResult moveEnergy(BlockEntityMultiBlocksTube machine, int maxReceive, int maxEnergyCanTransport, boolean simulate, Level level) {
        CapResult result = new CapResult(0, 0);
        
        int quantity = machine.getReceiversContainers().size();
        if (quantity > 0 && maxReceive > 0) {
            result = moveEnergy(machine, maxReceive, maxEnergyCanTransport, simulate);
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
    
    
    private static CapResult moveEnergy(BlockEntityMultiBlocksTube machine, int amount, int maxEnergyCanTransport, boolean simulate) {
        CapResult result = new CapResult(0, 0);
        
        final Map<BlockEntity, Direction> mapPosSet = machine.getReceiversContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return result;
        int validOutputs = 0;
        int realMaxOutput;
        int out = 0;
        int leftOutput = mapPosSet.size();
        int leftEnergy = amount;
        for (BlockEntity te : mapPosSet.keySet()) {
            if (te != null && !te.isRemoved()) {
                Direction face = mapPosSet.get(te).getOpposite();
                IEnergyStorage energyStorage = te.getCapability(ForgeCapabilities.ENERGY, face).orElse(null);
                if (energyStorage != null && energyStorage.canReceive()) {
                    realMaxOutput = machine.getLimitedValueForOutPut(leftEnergy / leftOutput, maxEnergyCanTransport, te, simulate);
                    if (realMaxOutput > 0) {
                        int energy = energyStorage.receiveEnergy(realMaxOutput, simulate);
                        if (!simulate) leftEnergy -= out;
                        out += energy;
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
    
    private static CapResult moveFluid(BlockEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, IFluidHandler.FluidAction action) {
        CapResult result = new CapResult(0, 0);
        
        final Map<BlockEntity, Direction> mapPosSet = machine.getReceiversContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return result;
        int validOutputs = 0;
        int leftOutput = mapPosSet.size();
        int leftFluid = resource.getAmount();
        int out = 0;
        for (BlockEntity te : mapPosSet.keySet()) {
            if (te != null && !te.isRemoved()) {
                Direction face = mapPosSet.get(te).getOpposite();
                IFluidHandler tankStorage = te.getCapability(ForgeCapabilities.FLUID_HANDLER, face).orElse(null);
                if (tankStorage != null) {
                    FluidStack realMaxOutput = new FluidStack(resource.getFluid(), Math.min(resource.getAmount(), maxFluidCanTransport));
                    realMaxOutput.setAmount(machine.getLimitedValueForOutPut(leftFluid / leftOutput, maxFluidCanTransport, te, action.equals(IFluidHandler.FluidAction.SIMULATE)));
                    if (realMaxOutput.getAmount() > 0) {
                        int fluid = tankStorage.fill(realMaxOutput, action);
                        out += fluid;
                        if (action.equals(IFluidHandler.FluidAction.EXECUTE)) leftFluid -= out;
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
