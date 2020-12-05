package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiBlockHelper
{
    public static List<Integer> outputEnergy(TileEntityMultiBlocksTube machine, int maxReceive, int maxEnergyCanTransport, boolean simulate, World world)
    {
        List<Integer> list = new ArrayList<>();
        int quantity = machine.getMachineContainers().size();
        if (quantity > 0 && maxReceive > 0)
        {
            list = moveEnergy(machine, maxReceive, maxEnergyCanTransport, simulate);
        } else
        {
            list.add(0);
            list.add(0);
        }
        return list;
    }

    public static List<Integer> outputFluid(TileEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, IFluidHandler.FluidAction action, World world)
    {
        List<Integer> list = new ArrayList<>();
        int quantity = machine.getMachineContainers().size();
        if (quantity > 0 && resource != null && resource.getAmount() > 0)
        {
            list = moveFluid(machine, resource, maxFluidCanTransport, action);
        } else
        {
            list.add(0);
            list.add(0);
        }

        return list;
    }

    private static List<Integer> moveFluid(TileEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, IFluidHandler.FluidAction action)
    {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        final Map<TileEntity, Direction> mapPosSet = machine.getMachineContainers();
        int validOutputs = getFluidMaxOutPutCount(resource, machine, maxFluidCanTransport, mapPosSet);
        if (validOutputs == 0) return list;
        list.add(1, validOutputs);
        FluidStack realMaxOutput = new FluidStack(resource.getFluid(), Math.min(resource.getAmount() / validOutputs, maxFluidCanTransport));
        int out = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (te != null)
            {
                Direction face = mapPosSet.get(te).getOpposite();
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).orElse(null);
                if (tankStorage != null
                        && tankStorage.getTanks() > 0
                        && tankStorage.isFluidValid(0, resource))
                {
                    realMaxOutput.setAmount(machine.getLimitedValueForOutPut(realMaxOutput.getAmount(), maxFluidCanTransport, te, action.execute() ? action.simulate() : action.execute()));
                    if (realMaxOutput.getAmount() > 0)
                    {
                        int fluid = tankStorage.fill(realMaxOutput, action);
                        out += fluid;
                    }
                }
            }
        }
        list.add(0, out);
        return list;
    }

    private static List<Integer> moveEnergy(TileEntityMultiBlocksTube machine, int amount, int maxEnergyCanTransport, boolean simulate)
    {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        final Map<TileEntity, Direction> mapPosSet = machine.getMachineContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return list;
        int validOutputs = getEnergyMaxOutPutCount(machine, maxEnergyCanTransport, mapPosSet);
        if (validOutputs == 0) return list;
        list.add(1, validOutputs);
        int realMaxOutput = Math.min(amount / validOutputs, maxEnergyCanTransport);
        int out = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (te == null || mapPosSet.get(te) == null) continue;
            Direction face = mapPosSet.get(te).getOpposite();
            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face).orElse(null);
            if (energyStorage != null && energyStorage.canReceive())
            {
                realMaxOutput = machine.getLimitedValueForOutPut(realMaxOutput, maxEnergyCanTransport, te, simulate);
                if (realMaxOutput > 0)
                {
                    int energy = energyStorage.receiveEnergy(realMaxOutput, simulate);
                    out += energy;
                }
            }
        }
        list.add(0, out);
        return list;
    }

    public static int getFluidMaxOutPutCount(FluidStack resource, TileEntityMultiBlocksTube machine, int maxFluidCanTransport, Map<TileEntity, Direction> mapPosSet)
    {
        int canAccept = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (!mapPosSet.containsKey(te)) continue;
            if (te != null)
            {
                Direction face = mapPosSet.get(te).getOpposite();
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).orElse(null);
                if (tankStorage != null
                        && tankStorage.getTanks() > 0
                        && tankStorage.isFluidValid(0, resource))
                {
                    FluidStack realMaxOutput = resource.copy();
                    realMaxOutput.setAmount(machine.getLimitedValueForOutPut(realMaxOutput.getAmount(), maxFluidCanTransport, te, true));
                    if (realMaxOutput.amount > 0)
                    {
                        int fluid = tankStorage.fill(realMaxOutput, false);
                        if (fluid > 0) canAccept++;
                    }
                }
            }
        }
        return canAccept;
    }

    private static int getEnergyMaxOutPutCount(TileEntityMultiBlocksTube machine, int maxEnergyCanTransport, Map<TileEntity, Direction> mapPosSet)
    {
        int canAccept = 0;
        int realMaxOutput = maxEnergyCanTransport;
        for (TileEntity te : mapPosSet.keySet())
        {
            Direction face = mapPosSet.get(te).getOpposite();
            if (te != null)
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face).orElse(null);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    realMaxOutput = machine.getLimitedValueForOutPut(realMaxOutput, maxEnergyCanTransport, te, true);
                    if (realMaxOutput > 0)
                    {
                        int energy = energyStorage.receiveEnergy(realMaxOutput, true);
                        if (energy > 0) canAccept++;
                    }
                }
            }
        }
        return canAccept;
    }
}
