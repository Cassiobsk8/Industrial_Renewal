package net.cassiokf.industrialrenewal.util;

import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import net.cassiokf.industrialrenewal.obj.CapPercentage;
import net.cassiokf.industrialrenewal.obj.CapResult;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        
        int out = 0;
        int validOutputs = 0;
        
        Set<CapPercentage> percentageMap = getEnergyDistributionPercentage(machine, amount, maxEnergyCanTransport);
    
        for (CapPercentage entry : percentageMap) {
            float percentage = entry.getPercentage();
            IEnergyStorage energyStorage = entry.getEnergyStorage();
            int individualAmount = entry.getAmount();
            int energyCalculated = (int) (individualAmount * percentage);
            if (energyCalculated > 0) {
                machine.getLimitedValueForOutPut(energyCalculated, maxEnergyCanTransport, entry.getBlockEntity(), simulate);
                int energy = energyStorage.receiveEnergy(energyCalculated, simulate);
                out += energy;
                validOutputs++;
            }
        }
    
        return new CapResult(out, validOutputs);
    }
    
    private static Set<CapPercentage> getEnergyDistributionPercentage(BlockEntityMultiBlocksTube machine, int amount, int maxEnergyCanTransport) {
        int validOutputs = 0;
        int totalEnergyUsed = 0;
        Map<BlockEntity, Direction> mapPosSet = machine.getReceiversContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return new HashSet<>();
        
        Set<CapPercentage> capPercentages = new HashSet<>();
        for (Map.Entry<BlockEntity, Direction> entry : mapPosSet.entrySet()) {
            BlockEntity te = entry.getKey();
            if (te != null && !te.isRemoved()) {
                Direction face = entry.getValue().getOpposite();
                IEnergyStorage energyStorage = te.getCapability(ForgeCapabilities.ENERGY, face).orElse(null);
                if (energyStorage != null && energyStorage.canReceive()) {
                    int realMaxOutput = machine.getLimitedValueForOutPut(amount, maxEnergyCanTransport, te, true);
                    if (realMaxOutput > 0) {
                        int energy = energyStorage.receiveEnergy(realMaxOutput, true);
                        totalEnergyUsed += energy;
                        validOutputs++;
                        capPercentages.add(new CapPercentage(energyStorage, te, Utils.normalize(realMaxOutput, 0, amount), energy));
                    }
                }
            }
        }
        if (totalEnergyUsed <= amount || validOutputs == 0 || totalEnergyUsed == 0) return capPercentages;
        
        float bestRatio = Utils.normalize(amount, 0, totalEnergyUsed);
        for (CapPercentage entry : capPercentages) {
            float actualPercentage = entry.getPercentage();
            if (actualPercentage > 0f) {
                entry.setPercentage(actualPercentage * bestRatio);
            }
        }
        return capPercentages;
    }
    
    private static CapResult moveFluid(BlockEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, IFluidHandler.FluidAction action) {
        if (machine == null) return new CapResult(0, 0);
        
        int out = 0;
        int validOutputs = 0;
        
        Set<CapPercentage> percentageMap = getFluidDistributionPercentage(machine, resource, maxFluidCanTransport);
        
        for (CapPercentage entry : percentageMap) {
            float percentage = entry.getPercentage();
            IFluidHandler energyStorage = entry.getFluidHandler();
            int individualAmount = entry.getAmount();
            int energyCalculated = (int) (individualAmount * percentage);
            if (energyCalculated > 0) {
                machine.getLimitedValueForOutPut(energyCalculated, maxFluidCanTransport, entry.getBlockEntity(), action.simulate());
                FluidStack realStack = new FluidStack(resource.getFluid(), energyCalculated);
                int energy = energyStorage.fill(realStack, action);
                out += energy;
                validOutputs++;
            }
        }
        
        return new CapResult(out, validOutputs);
    }
    
    private static Set<CapPercentage> getFluidDistributionPercentage(final BlockEntityMultiBlocksTube machine,  final FluidStack resource, final int maxFluidCanTransport) {
        int validOutputs = 0;
        int totalFluidUsed = 0;
        Map<BlockEntity, Direction> mapPosSet = machine.getReceiversContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return new HashSet<>();
        
        Set<CapPercentage> capPercentages = new HashSet<>();
        for (Map.Entry<BlockEntity, Direction> entry : mapPosSet.entrySet()) {
            BlockEntity te = entry.getKey();
            if (te != null && !te.isRemoved()) {
                Direction face = entry.getValue().getOpposite();
                IFluidHandler fluidHandler = te.getCapability(ForgeCapabilities.FLUID_HANDLER, face).orElse(null);
                if (fluidHandler != null) {
                    int realMaxOutput = machine.getLimitedValueForOutPut(resource.getAmount(), maxFluidCanTransport, te, true);
                    FluidStack realStack = new FluidStack(resource.getFluid(), realMaxOutput);
                    if (realMaxOutput > 0) {
                        int quantity = fluidHandler.fill(realStack, IFluidHandler.FluidAction.SIMULATE);
                        totalFluidUsed += quantity;
                        validOutputs++;
                        capPercentages.add(new CapPercentage(fluidHandler, te, Utils.normalize(realMaxOutput, 0, resource.getAmount()), quantity));
                    }
                }
            }
        }
        if (totalFluidUsed <= resource.getAmount() || validOutputs == 0 || totalFluidUsed == 0) return capPercentages;
        
        float bestRatio = Utils.normalize(resource.getAmount(), 0, totalFluidUsed);
        for (CapPercentage entry : capPercentages) {
            float actualPercentage = entry.getPercentage();
            if (actualPercentage > 0f) {
                entry.setPercentage(actualPercentage * bestRatio);
            }
        }
        return capPercentages;
    }
}
