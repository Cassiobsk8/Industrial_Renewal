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
        }
        else
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
        }
        else
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
        if (mapPosSet == null || mapPosSet.isEmpty()) return list;
        int validOutputs = 0;
        int leftOutput = mapPosSet.size();
        int leftFluid = resource.amount;
        FluidStack realMaxOutput = new FluidStack(resource.getFluid(), Math.min(resource.amount, maxFluidCanTransport));
        int out = 0;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (te != null)
            {
                EnumFacing face = mapPosSet.get(te).getOpposite();
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
                if (tankStorage != null && tankStorage.getTankProperties() != null && tankStorage.getTankProperties().length > 0
                        && tankStorage.getTankProperties()[0].canFill())
                {
                    realMaxOutput.amount = machine.getLimitedValueForOutPut(leftFluid / leftOutput, maxFluidCanTransport, te, !doFill);
                    if (realMaxOutput.amount > 0)
                    {
                        int fluid = tankStorage.fill(realMaxOutput, doFill);
                        out += fluid;
                        if (doFill) leftFluid -= out;
                        validOutputs++;
                    }
                }
            }
            leftOutput--;
        }
        list.add(0, out);
        list.add(1, validOutputs);
        return list;
    }

    private static List<Integer> moveEnergy(TileEntityMultiBlocksTube machine, int amount, int maxEnergyCanTransport, boolean simulate)
    {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        final Map<TileEntity, EnumFacing> mapPosSet = machine.getMachineContainers();
        if (mapPosSet == null || mapPosSet.isEmpty()) return list;
        int validOutputs = 0;
        int realMaxOutput;
        int out = 0;
        int leftOutput = mapPosSet.size();
        int leftEnergy = amount;
        for (TileEntity te : mapPosSet.keySet())
        {
            if (te != null && mapPosSet.get(te) != null)
            {
                EnumFacing face = mapPosSet.get(te).getOpposite();
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    realMaxOutput = machine.getLimitedValueForOutPut(leftEnergy / leftOutput, maxEnergyCanTransport, te, simulate);
                    if (realMaxOutput > 0)
                    {
                        int energy = energyStorage.receiveEnergy(realMaxOutput, simulate);
                        if (!simulate) leftEnergy -= out;
                        out += energy;
                        validOutputs++;
                    }
                }
            }
            leftOutput--;
        }
        list.add(0, out);
        list.add(1, validOutputs);
        return list;
    }
}
