package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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

    public static List<Integer> outputFluid(TileEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, boolean doFill, World world)
    {
        List<Integer> list = new ArrayList<>();
        int quantity = machine.getMachineContainers().size();
        if (quantity > 0 && resource != null && resource.amount > 0)
        {
            list = moveFluid(machine, resource, maxFluidCanTransport, doFill);
        } else
        {
            list.add(0);
            list.add(0);
        }

        return list;
    }

    private static List<Integer> moveFluid(TileEntityMultiBlocksTube machine, FluidStack resource, int maxFluidCanTransport, boolean doFill)
    {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        final Map<TileEntity, EnumFacing> mapPosSet = machine.getMachineContainers();
        int validOutputs = getFluidMaxOutPutCount(resource, machine, maxFluidCanTransport, mapPosSet);
        if (validOutputs == 0) return list;
        list.add(1, validOutputs);
        FluidStack realMaxOutput = new FluidStack(resource.getFluid(), Math.min(resource.amount / validOutputs, maxFluidCanTransport));
        int out = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            EnumFacing face = mapPosSet.get(te).getOpposite();
            if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face))
            {
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
                if (tankStorage != null
                        && tankStorage.getTankProperties() != null
                        && tankStorage.getTankProperties().length > 0
                        && tankStorage.getTankProperties()[0].canFill())
                {
                    realMaxOutput.amount = machine.getLimitedValueForOutPut(realMaxOutput.amount, maxFluidCanTransport, te, !doFill);
                    if (realMaxOutput.amount > 0)
                    {
                        int fluid = tankStorage.fill(realMaxOutput, doFill);
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
        final Map<TileEntity, EnumFacing> mapPosSet = machine.getMachineContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return list;
        int validOutputs = getEnergyMaxOutPutCount(machine, maxEnergyCanTransport, mapPosSet);
        if (validOutputs == 0) return list;
        list.add(1, validOutputs);
        int realMaxOutput = Math.min(amount / validOutputs, maxEnergyCanTransport);
        int out = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (te == null || mapPosSet.get(te) == null) continue;
            EnumFacing face = mapPosSet.get(te).getOpposite();
            if (te.hasCapability(CapabilityEnergy.ENERGY, face))
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
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
        }
        list.add(0, out);
        return list;
    }

    public static int getFluidMaxOutPutCount(FluidStack resource, TileEntityMultiBlocksTube machine, int maxFluidCanTransport, Map<TileEntity, EnumFacing> mapPosSet)
    {
        int canAccept = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (!mapPosSet.containsKey(te)) continue;
            EnumFacing face = mapPosSet.get(te).getOpposite();
            if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face))
            {
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
                if (tankStorage != null
                        && tankStorage.getTankProperties() != null
                        && tankStorage.getTankProperties().length > 0
                        && tankStorage.getTankProperties()[0].canFill())
                {
                    FluidStack realMaxOutput = resource;
                    realMaxOutput.amount = machine.getLimitedValueForOutPut(realMaxOutput.amount, maxFluidCanTransport, te, true);
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

    private static int getEnergyMaxOutPutCount(TileEntityMultiBlocksTube machine, int maxEnergyCanTransport, Map<TileEntity, EnumFacing> mapPosSet)
    {
        int canAccept = 0;
        int realMaxOutput = maxEnergyCanTransport;
        for (TileEntity te : mapPosSet.keySet())
        {
            EnumFacing face = mapPosSet.get(te).getOpposite();
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, face))
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
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
