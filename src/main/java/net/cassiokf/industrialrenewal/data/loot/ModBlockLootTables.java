package net.cassiokf.industrialrenewal.data.loot;

import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.init.ModFluids;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }
    
    @Override
    protected void generate() {
        dropSelf(ModBlocks.AISLEHAZARD.get());
        dropSelf(ModBlocks.BLOCKHAZARD.get());
        dropSelf(ModBlocks.CAUTIONHAZARD.get());
        dropSelf(ModBlocks.DEFECTIVEHAZARD.get());
        dropSelf(ModBlocks.SAFETYHAZARD.get());
        dropSelf(ModBlocks.FIREHAZARD.get());
        dropSelf(ModBlocks.RADIATIONHAZARD.get());
        dropSelf(ModBlocks.CONCRETE.get());
        dropSelf(ModBlocks.STEELBLOCK.get());
        dropSelf(ModBlocks.FRAME.get());
        //            dropSelf(ModBlocks.CONCRETEWALL.get());
//        //            dropSelf(ModBlocks.LOCKER.get());
        dropSelf(ModBlocks.SOLAR_PANEL.get());
        dropSelf(ModBlocks.ENERGYCABLE_HV.get());
        dropSelf(ModBlocks.ENERGYCABLE_MV.get());
        dropSelf(ModBlocks.ENERGYCABLE_LV.get());
        dropSelf(ModBlocks.ENERGYCABLE_LV_METER.get());
        dropSelf(ModBlocks.TRASH.get());
        dropSelf(ModBlocks.TURBINE_PILLAR.get());
        dropSelf(ModBlocks.WIND_TURBINE.get());
        dropSelf(ModBlocks.PORTABLE_GENERATOR.get());
        dropSelf(ModBlocks.FLUID_PIPE.get());
        dropSelf(ModBlocks.FLUID_PIPE_LARGE.get());

        dropSelf(ModBlocks.BRACE.get());
        dropSelf(ModBlocks.BRACE_STEEL.get());
        dropSelf(ModBlocks.RAZOR_WIRE.get());
        //            dropSelf(ModBlocks.BIG_FENCE_WIRE.get());
        //            dropSelf(ModBlocks.BIG_FENCE_COLUMN.get());
        dropSelf(ModBlocks.ELECTRIC_GATE.get());
        dropSelf(ModBlocks.ELECTRIC_FENCE.get());
        dropSelf(ModBlocks.CATWALK_GATE.get());
        dropSelf(ModBlocks.CATWALK.get());
        dropSelf(ModBlocks.CATWALK_STEEL.get());
        dropSelf(ModBlocks.CATWALK_LADDER.get());
        dropSelf(ModBlocks.CATWALK_LADDER_STEEL.get());
        dropSelf(ModBlocks.CATWALK_STAIR.get());
        dropSelf(ModBlocks.CATWALK_STAIR_STEEL.get());
        dropSelf(ModBlocks.CATWALK_HATCH.get());
        dropSelf(ModBlocks.SCAFFOLD.get());
        dropSelf(ModBlocks.PLATFORM.get());
        dropSelf(ModBlocks.HANDRAIL.get());
        dropSelf(ModBlocks.HANDRAIL_STEEL.get());
        dropSelf(ModBlocks.COLUMN.get());
        dropSelf(ModBlocks.COLUMN_STEEL.get());
        dropSelf(ModBlocks.PILLAR.get());
        dropSelf(ModBlocks.PILLAR_STEEL.get());
//        //
        dropSelf(ModBlocks.LIGHT.get());
        dropSelf(ModBlocks.FLUORESCENT.get());
        dropSelf(ModBlocks.DAM_OUTLET.get());
        dropSelf(ModBlocks.DAM_INTAKE.get());
        dropSelf(ModBlocks.HIGH_PRESSURE_PIPE.get());
        dropSelf(ModBlocks.ROTATIONAL_SHAFT.get());
        dropSelf(ModBlocks.CONVEYOR_BASIC.get());
        dropSelf(ModBlocks.CONVEYOR_FAST.get());
        dropSelf(ModBlocks.CONVEYOR_EXPRESS.get());
        //            dropSelf(ModBlocks.CONVEYOR_INSERTER.get());
        //            dropSelf(ModBlocks.CONVEYOR_HOPPER.get());
//        dropSelf(ModBlocks.BOOSTER_RAIL.get());
        dropSelf(ModBlocks.INDUSTRIAL_FLOOR.get());
        dropSelf(ModBlocks.SPANEL_FRAME.get());

        dropSelf(ModBlocks.HV_ISOLATOR.get());
        dropSelf(ModBlocks.FLUID_VALVE.get());
        dropSelf(ModBlocks.ENERGY_SWITCH.get());
        
        //TODO: CANNOT DROP ITSELF
        //dropSelf(ModBlocks.BATTERY_BANK.get());
        //dropSelf(ModBlocks.BARREL.get());
        dropSelf(ModBlocks.ELECTRIC_PUMP.get());
        dropSelf(ModBlocks.STEAM_BOILER.get());
        dropSelf(ModBlocks.STEAM_TURBINE.get());
        dropSelf(ModBlocks.MINER.get());
        dropSelf(ModBlocks.INDUSTRIAL_BATTERY_BANK.get());
        dropSelf(ModBlocks.FLUID_TANK.get());
        dropSelf(ModBlocks.STORAGE_CHEST.get());
        dropSelf(ModBlocks.LATHE.get());
        dropSelf(ModBlocks.DAM_GENERATOR.get());
        dropSelf(ModBlocks.DAM_TURBINE.get());
        dropSelf(ModBlocks.TRANSFORMER.get());
        dropSelf(ModBlocks.INFINITY_GENERATOR.get());
        
        dropSelf(ModBlocks.ENERGY_LEVEL.get());
        dropSelf(ModBlocks.FLUID_GAUGE.get());
        dropSelf(ModFluids.STEAM_BLOCK.get());

        //super.addTables();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
