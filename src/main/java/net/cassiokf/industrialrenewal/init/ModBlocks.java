package net.cassiokf.industrialrenewal.init;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.block.*;
import net.cassiokf.industrialrenewal.block.abstracts.IRBaseBlock;
import net.cassiokf.industrialrenewal.block.dam.*;
import net.cassiokf.industrialrenewal.block.decor.*;
import net.cassiokf.industrialrenewal.block.transport.*;
import net.cassiokf.industrialrenewal.util.enums.EnumConveyorTier;
import net.cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, IndustrialRenewal.MODID);
    
    public static final RegistryObject<Block> BLOCKHAZARD = registerBlock("block_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> CAUTIONHAZARD = registerBlock("caution_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> DEFECTIVEHAZARD = registerBlock("defective_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> SAFETYHAZARD = registerBlock("safety_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> RADIATIONHAZARD = registerBlock("radiation_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> AISLEHAZARD = registerBlock("aisle_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> FIREHAZARD = registerBlock("fire_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> STEELBLOCK = registerBlock("block_steel", IRBaseBlock::new);
    public static final RegistryObject<Block> CONCRETE = registerBlock("concrete", IRBaseBlock::new);
    public static final RegistryObject<Block> FRAME = registerBlock("frame", BlockFrame::new);
    
    public static final RegistryObject<BlockPillar> PILLAR = registerBlock("catwalk_pillar",
            BlockPillar::new, false);
    
    public static final RegistryObject<BlockPillar> PILLAR_STEEL = registerBlock("catwalk_steel_pillar",
            BlockPillar::new, false);
    
    public static final RegistryObject<BlockBrace> BRACE = registerBlock("brace",
            BlockBrace::new);
    
    public static final RegistryObject<BlockBrace> BRACE_STEEL = registerBlock("brace_steel",
            BlockBrace::new);
    
    public static final RegistryObject<BlockColumn> COLUMN = registerBlock("catwalk_column",
            BlockColumn::new);
    
    public static final RegistryObject<BlockColumn> COLUMN_STEEL = registerBlock("catwalk_column_steel",
            BlockColumn::new);
    
    public static final RegistryObject<BlockCatwalk> CATWALK = registerBlock("catwalk",
            BlockCatwalk::new, false);
    
    public static final RegistryObject<BlockCatwalk> CATWALK_STEEL = registerBlock("catwalk_steel",
            BlockCatwalk::new, false);
    
    public static final RegistryObject<BlockCatwalkStair> CATWALK_STAIR = registerBlock("catwalk_stair",
            BlockCatwalkStair::new, false);
    
    public static final RegistryObject<BlockCatwalkStair> CATWALK_STAIR_STEEL = registerBlock("catwalk_stair_steel",
            BlockCatwalkStair::new, false);
    
    public static final RegistryObject<BlockHandRail> HANDRAIL = registerBlock("handrail",
            BlockHandRail::new);
    
    public static final RegistryObject<BlockHandRail> HANDRAIL_STEEL = registerBlock("handrail_steel",
            BlockHandRail::new);
    
    public static final RegistryObject<BlockCatwalkLadder> CATWALK_LADDER = registerBlock("catwalk_ladder",
            BlockCatwalkLadder::new, false);
    
    public static final RegistryObject<BlockCatwalkLadder> CATWALK_LADDER_STEEL = registerBlock("catwalk_ladder_steel",
            BlockCatwalkLadder::new, false);
    
    public static final RegistryObject<BlockIndustrialFloor> INDUSTRIAL_FLOOR = registerBlock("industrial_floor",
            BlockIndustrialFloor::new);
    
    public static final RegistryObject<BlockPlatform> PLATFORM = registerBlock("platform",
            BlockPlatform::new, false);
    
    public static final RegistryObject<BlockScaffold> SCAFFOLD = registerBlock("scaffold",
            BlockScaffold::new, false);
    
    public static final RegistryObject<BlockCatwalkGate> CATWALK_GATE = registerBlock("catwalk_gate",
            BlockCatwalkGate::new);
    
    public static final RegistryObject<BlockCatwalkHatch> CATWALK_HATCH = registerBlock("catwalk_hatch",
            BlockCatwalkHatch::new);
    
    
    //    public static final RegistryObject<BlockElectricBigFenceColumn> BIG_FENCE_COLUMN = registerBlock("fence_big_column",
    //            BlockElectricBigFenceColumn::new);
    //
    //    public static final RegistryObject<BlockElectricBigFenceWire> BIG_FENCE_WIRE = registerBlock("fence_big_wire",
    //            BlockElectricBigFenceWire::new);
    
    public static final RegistryObject<BlockElectricFence> ELECTRIC_FENCE = registerBlock("electric_fence",
            BlockElectricFence::new);
    
    public static final RegistryObject<BlockElectricGate> ELECTRIC_GATE = registerBlock("electric_gate",
            BlockElectricGate::new);
    
    public static final RegistryObject<BlockRazorWire> RAZOR_WIRE = registerBlock("razor_wire",
            BlockRazorWire::new);
    
    public static final RegistryObject<BlockLight> LIGHT = registerBlock("light",
            BlockLight::new);
    
    public static final RegistryObject<BlockFluorescent> FLUORESCENT = registerBlock("fluorescent",
            BlockFluorescent::new);
    
    public static final RegistryObject<Block> SOLAR_PANEL = registerBlock("solar_panel", BlockSolarPanel::new);
    
    public static final RegistryObject<BlockSolarPanelFrame> SPANEL_FRAME = registerBlock("solar_panel_frame",
            () -> new BlockSolarPanelFrame(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).noOcclusion()));
    
    public static final RegistryObject<Block> BATTERY_BANK = registerBlock("battery_bank", BlockBatteryBank::new);
    
    
    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_LV = registerBlock("energy_cable_lv",
            () -> new BlockEnergyCable(EnumEnergyCableType.LV));
    
    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_MV = registerBlock("energy_cable_mv",
            () -> new BlockEnergyCable(EnumEnergyCableType.MV));
    
    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_HV = registerBlock("energy_cable_hv",
            () -> new BlockEnergyCable(EnumEnergyCableType.HV));
    
    public static final RegistryObject<BlockEnergyCableMeter> ENERGYCABLE_LV_METER = registerBlock("energy_cable_gauge_lv",
            () -> new BlockEnergyCableMeter(EnumEnergyCableType.LV), false);
    
    public static final RegistryObject<BlockEnergyCableMeter> ENERGYCABLE_MV_METER = registerBlock("energy_cable_gauge_mv",
            () -> new BlockEnergyCableMeter(EnumEnergyCableType.MV), false);

    public static final RegistryObject<BlockEnergyCableMeter> ENERGYCABLE_HV_METER = registerBlock("energy_cable_gauge_hv",
            () -> new BlockEnergyCableMeter(EnumEnergyCableType.HV), false);
    
    
    public static final RegistryObject<BlockFluidPipe> FLUID_PIPE = registerBlock("fluid_pipe", BlockFluidPipe::new);
    
    public static final RegistryObject<BlockFluidPipeLarge> FLUID_PIPE_LARGE = registerBlock("fluid_pipe_large", BlockFluidPipeLarge::new);
    
    public static final RegistryObject<BlockHighPressureFluidPipe> HIGH_PRESSURE_PIPE = registerBlock("high_pressure_pipe", BlockHighPressureFluidPipe::new);
    
    public static final RegistryObject<BlockBarrel> BARREL = registerBlock("barrel", BlockBarrel::new);
    
    public static final RegistryObject<BlockEnergySwitch> ENERGY_SWITCH = registerBlock("energy_switch",
            ()-> new BlockEnergySwitch(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f)
                    .noOcclusion()));
    public static final RegistryObject<BlockFluidValve> FLUID_VALVE = registerBlock("valve_pipe_large",
            ()-> new BlockFluidValve(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f)
                    .noOcclusion()));
    
    public static final RegistryObject<BlockElectricPump> ELECTRIC_PUMP = registerBlock("electric_pump",
            ()-> new BlockElectricPump(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)));
    
    public static final RegistryObject<BlockTrash> TRASH = registerBlock("trash",
            () -> new BlockTrash(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    public static final RegistryObject<BlockWindTurbinePillar> TURBINE_PILLAR = registerBlock("small_wind_turbine_pillar",
            () -> new BlockWindTurbinePillar(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f)
                    .noOcclusion()));
    
    public static final RegistryObject<BlockWindTurbineHead> WIND_TURBINE = registerBlock("small_wind_turbine",
            () -> new BlockWindTurbineHead(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f)
                    .noOcclusion()));
    
    public static final RegistryObject<BlockPortableGenerator> PORTABLE_GENERATOR = registerBlock("portable_generator",
            () -> new BlockPortableGenerator(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    
    public static final RegistryObject<BlockConveyor> CONVEYOR_BASIC = registerBlock("conveyor_bulk_basic",
            ()-> new BlockConveyor(EnumConveyorTier.BASIC));
    
    public static final RegistryObject<BlockConveyor> CONVEYOR_FAST = registerBlock("conveyor_bulk_fast",
            ()-> new BlockConveyor(EnumConveyorTier.FAST));
    
    public static final RegistryObject<BlockConveyor> CONVEYOR_EXPRESS = registerBlock("conveyor_bulk_express",
            ()-> new BlockConveyor(EnumConveyorTier.EXPRESS));
    
    
    public static final RegistryObject<BlockSteamBoiler> STEAM_BOILER = registerBlock("steam_boiler",
            ()-> new BlockSteamBoiler(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    public static final RegistryObject<BlockSteamTurbine> STEAM_TURBINE = registerBlock("steam_turbine",
            ()-> new BlockSteamTurbine(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    
    public static final RegistryObject<BlockMiner> MINER = registerBlock("mining",
            ()-> new BlockMiner(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    public static final RegistryObject<BlockIndustrialBatteryBank> INDUSTRIAL_BATTERY_BANK = registerBlock("ind_battery_bank",
            ()-> new BlockIndustrialBatteryBank(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    
    public static final RegistryObject<BlockFluidTank> FLUID_TANK = registerBlock("fluid_tank",
            ()-> new BlockFluidTank(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    
    public static final RegistryObject<BlockStorageChest> STORAGE_CHEST = registerBlock("storage_chest",
            ()-> new BlockStorageChest(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f)
                    .noOcclusion()));
    
    
    public static final RegistryObject<BlockLathe> LATHE = registerBlock("lathe",
            ()-> new BlockLathe(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f)
                    .noOcclusion()));
    
    public static final RegistryObject<BlockDamIntake> DAM_INTAKE = registerBlock("dam_intake",
            BlockDamIntake::new);
    
    public static final RegistryObject<BlockDamOutlet> DAM_OUTLET = registerBlock("dam_outflow",
            BlockDamOutlet::new);
    
    public static final RegistryObject<BlockDamTurbine> DAM_TURBINE = registerBlock("dam_turbine",
            ()-> new BlockDamTurbine(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).noOcclusion()));
    
    public static final RegistryObject<BlockRotationalShaft> ROTATIONAL_SHAFT = registerBlock("dam_axis",
            BlockRotationalShaft::new);
    
    public static final RegistryObject<BlockDamGenerator> DAM_GENERATOR = registerBlock("dam_generator",
            ()-> new BlockDamGenerator(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).noOcclusion()));
    
    
    
    public static final RegistryObject<BlockTransformer> TRANSFORMER = registerBlock("transformer_hv",
            ()-> new BlockTransformer(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).noOcclusion()));
    
    public static final RegistryObject<BlockHVIsolator> HV_ISOLATOR = registerBlock("isolator_hv",
            ()-> new BlockHVIsolator(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.8f).noOcclusion()));
    
    public static final RegistryObject<BlockInfinityGenerator> INFINITY_GENERATOR =
            registerBlock("infinity_generator", BlockInfinityGenerator::new);
    
    public static final RegistryObject<BlockEnergyLevel> ENERGY_LEVEL =
            registerBlock("energy_level", BlockEnergyLevel::new);
    
    public static final RegistryObject<BlockFluidGauge> FLUID_GAUGE =
            registerBlock("fluid_gauge", BlockFluidGauge::new);
    
    //================================================================================================
    
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, true);
    }
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean createItem) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        if(createItem)
            registryBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registryBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    public static void register (IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
