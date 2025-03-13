package net.cassiokf.industrialrenewal.data.client;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    
    
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, IndustrialRenewal.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        withExistingParent("block_hazard", modLoc("block/block_hazard"));
        withExistingParent("caution_hazard", modLoc("block/caution_hazard"));
        withExistingParent("defective_hazard", modLoc("block/defective_hazard"));
        withExistingParent("safety_hazard", modLoc("block/safety_hazard"));
        withExistingParent("radiation_hazard", modLoc("block/radiation_hazard"));
        withExistingParent("aisle_hazard", modLoc("block/aisle_hazard"));
        withExistingParent("fire_hazard", modLoc("block/fire_hazard"));
        withExistingParent("block_steel", modLoc("block/block_steel"));
        withExistingParent("concrete", modLoc("block/concrete"));
        withExistingParent("frame", modLoc("block/frame_base"));
        
        
        withExistingParent("concrete_wall", modLoc("block/concrete_wall_inventory"));
        
        withExistingParent("solar_panel", modLoc("block/solar_panel"));
        withExistingParent("battery_bank", modLoc("block/battery_bank"));
        withExistingParent("barrel", modLoc("block/fluid_barrel"));
        withExistingParent("portable_generator", modLoc("block/portable_generator"));
        withExistingParent("trash", modLoc("block/trash"));
        
        withExistingParent("small_wind_turbine_pillar", modLoc("block/small_wind_turbine_pillar"));
        withExistingParent("small_wind_turbine", modLoc("block/small_wind_turbine"));
        
        withExistingParent("steam_boiler", modLoc("block/steam_boiler"));
        withExistingParent("steam_turbine", modLoc("block/steam_turbine"));
        withExistingParent("mining", modLoc("block/mining_drill"));
        
        withExistingParent("locker", modLoc("block/locker"));
        withExistingParent("storage_chest", modLoc("block/storage/master_chest"));
        
        
        withExistingParent("ind_battery_bank", modLoc("block/battery/ind_battery_item"));
        withExistingParent("fluid_tank", modLoc("block/tank/tank_item"));
        
        withExistingParent("lathe", modLoc("block/lathe"));
        
        withExistingParent("catwalk_pillar", modLoc("block/pillar/catwalk_pillar_base"));
        withExistingParent("catwalk_steel_pillar", modLoc("block/pillar/catwalk_steel_pillar_base"));
        
        withExistingParent("catwalk_column", modLoc("block/catwalk_column_base"));
        withExistingParent("catwalk_column_steel", modLoc("block/catwalk_column_steel_base"));
        
        withExistingParent("catwalk", modLoc("block/catwalk_inventory"));
        withExistingParent("catwalk_steel", modLoc("block/catwalk_steel_inventory"));
        
        withExistingParent("catwalk_stair", modLoc("block/catwalk_stair_inventory"));
        withExistingParent("catwalk_stair_steel", modLoc("block/catwalk_stair_steel_inventory"));
        
        withExistingParent("handrail", modLoc("block/catwalk_side"));
        withExistingParent("handrail_steel", modLoc("block/catwalk_steel_side"));
        
        withExistingParent("catwalk_ladder", modLoc("block/catwalk_ladder_inventory"));
        withExistingParent("catwalk_ladder_steel", modLoc("block/catwalk_ladder_steel_inventory"));
        
        withExistingParent("platform", modLoc("block/platform_inventory"));
        withExistingParent("scaffold", modLoc("block/scaffold_inventory"));
        
        withExistingParent("catwalk_gate", modLoc("block/catwalk_gate_0"));
        withExistingParent("catwalk_hatch", modLoc("block/catwalk_hatch_0"));
        
        withExistingParent("brace", modLoc("block/brace"));
        withExistingParent("brace_steel", modLoc("block/brace_steel"));
        
        withExistingParent("fence_big_column", modLoc("block/fence_big/fence_big_core_165"));
        withExistingParent("fence_big_wire", modLoc("block/fence_big_wire_item"));
        
        withExistingParent("electric_fence", modLoc("block/electric_fence_inventory"));
        withExistingParent("electric_gate", modLoc("block/electric_gate_base"));
        
        withExistingParent("razor_wire", modLoc("block/razor_wire"));
        //
        //        withExistingParent("cargo_loader", modLoc("block/cargo_loader"));
        //        withExistingParent("fluid_loader", modLoc("block/fluid_loader"));
        
        withExistingParent("dam_intake", modLoc("block/dam_intake"));
        withExistingParent("high_pressure_pipe", modLoc("block/press_pipe/pressurized_tube_item"));
        withExistingParent("dam_turbine", modLoc("block/dam_turbine"));
        withExistingParent("dam_axis", modLoc("block/dam_axis"));
        withExistingParent("dam_generator", modLoc("block/dam_generator"));
        withExistingParent("dam_outflow", modLoc("block/dam_outflow"));
        
        withExistingParent("conveyor_bulk_basic", modLoc("block/conveyor/basic/conveyor"));
        withExistingParent("conveyor_bulk_fast", modLoc("block/conveyor/fast/conveyor"));
        withExistingParent("conveyor_bulk_express", modLoc("block/conveyor/express/conveyor"));
        
        withExistingParent("light", modLoc("block/light"));
        withExistingParent("fluorescent", modLoc("block/fluorescent_inventory"));
        withExistingParent("industrial_floor", modLoc("block/ind_floor/floor_inventory"));
        
        withExistingParent("solar_panel_frame", modLoc("block/solar_panel_frame"));
        
        withExistingParent("valve_pipe_large", modLoc("block/valves/valve_large"));
        withExistingParent("energy_switch", modLoc("block/switch/energy_switch_off"));
        withExistingParent("transformer_hv", modLoc("block/hv/transformer"));
        withExistingParent("isolator_hv", modLoc("block/hv/hv_isolator"));
        
        withExistingParent("fluid_pipe_large", modLoc("block/large_fluid_pipe/large_fluid_pipe_inventory"));
        withExistingParent("infinity_generator", modLoc("block/infinity_generator"));
        withExistingParent("energy_level", modLoc("block/energy_level"));
        withExistingParent("fluid_gauge", modLoc("block/fluid_gauge"));
        
    }
    
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(IndustrialRenewal.MODID, "item/" + item.getId().getPath()));
    }
}
