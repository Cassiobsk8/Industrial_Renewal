package net.cassiokf.industrialrenewal.data.client;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public static final List<ItemLike> SPONGE_IRON_LIST = List.of(ModItems.SPONGE_IRON.get());
    TagKey<Item> STEEL_BLOCKS = ItemTags.create(new ResourceLocation("forge:storage_blocks/steel"));
    TagKey<Item> STEEL_INGOT = ItemTags.create(new ResourceLocation("forge:ingots/steel"));
    TagKey<Item> STEEL_ROD = ItemTags.create(new ResourceLocation("forge:rods/steel"));
    TagKey<Item> IRON_ROD = ItemTags.create(new ResourceLocation("forge:rods/iron"));
    TagKey<Item> COPPER_ROD = ItemTags.create(new ResourceLocation("forge:rods/copper"));
    TagKey<Item> GOLD_ROD = ItemTags.create(new ResourceLocation("forge:rods/gold"));
    
    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }
    
    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }
    
    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }
    
    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, IndustrialRenewal.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
        
    }
    
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLOCKHAZARD.get(), 16).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.YELLOW_DYE).define('B', Items.BLACK_DYE).pattern("###").pattern("Y B").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.AISLEHAZARD.get(), 16).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.BLACK_DYE).define('B', Items.WHITE_DYE).pattern("###").pattern("Y B").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CAUTIONHAZARD.get(), 16).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.ORANGE_DYE).define('B', Items.WHITE_DYE).pattern("###").pattern("Y B").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DEFECTIVEHAZARD.get(), 16).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.BLUE_DYE).define('B', Items.WHITE_DYE).pattern("###").pattern("Y B").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.FIREHAZARD.get(), 16).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.RED_DYE).define('B', Items.WHITE_DYE).pattern("###").pattern("Y B").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RADIATIONHAZARD.get(), 16).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.MAGENTA_DYE).define('B', Items.YELLOW_DYE).pattern("###").pattern("Y B").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SAFETYHAZARD.get(), 16).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.LIME_DYE).define('B', Items.WHITE_DYE).pattern("###").pattern("Y B").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STEELBLOCK.get(), 1).define('A', STEEL_INGOT).pattern("AAA").pattern("AAA").pattern("AAA").unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CONCRETE.get(), 8).define('A', Tags.Items.GRAVEL).define('B', Tags.Items.SAND).pattern("BAB").pattern("A A").pattern("BAB").unlockedBy("has_item", has(Tags.Items.SAND)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.FRAME.get(), 6).define('A', IRON_ROD).define('B', STEEL_INGOT).pattern("ABA").pattern(" B ").pattern("ABA").unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMALL_MOTOR.get(), 2).define('#', Tags.Items.INGOTS_IRON).define('Y', Tags.Items.DUSTS_REDSTONE).define('B', Items.GOLD_INGOT).define('X', Items.PISTON).pattern("###").pattern("YXB").pattern("###").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INGOT_STEEL.get(), 9).requires(STEEL_BLOCKS).unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY.get(), 1).define('#', Tags.Items.DUSTS_REDSTONE).define('A', Tags.Items.NUGGETS_IRON).define('B', Items.GOLD_NUGGET).define('C', Items.LAPIS_LAZULI).define('D', Tags.Items.INGOTS_IRON).define('E', Items.GOLD_INGOT).pattern("A#B").pattern("A#B").pattern("DCE").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY_LITHIUM.get(), 1).define('#', Tags.Items.STORAGE_BLOCKS_REDSTONE).define('A', COPPER_ROD).define('B', GOLD_ROD).define('C', Items.LAPIS_BLOCK).define('D', Tags.Items.INGOTS_COPPER).define('E', Items.GOLD_INGOT).pattern("A#B").pattern("D#E").pattern("DCE").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SCREW_DRIVER.get(), 1).define('#', Tags.Items.INGOTS_IRON).define('Y', ModItems.SMALL_MOTOR.get()).define('A', ModItems.BATTERY.get()).define('B', Items.STONE_BUTTON).pattern("##Y").pattern(" BA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOLAR_PANEL.get(), 1).define('#', Items.GLASS_PANE).define('A', Items.LAPIS_LAZULI).define('B', Tags.Items.INGOTS_IRON).define('C', Tags.Items.DUSTS_REDSTONE).pattern("###").pattern("AAA").pattern("BCB").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGYCABLE_LV.get(), 6).define('#', Tags.Items.DUSTS_REDSTONE).define('A', Tags.Items.INGOTS_COPPER).define('X', Items.GLASS_PANE).pattern("AXA").pattern("###").pattern("AXA").unlockedBy("has_item", has(Items.COPPER_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGYCABLE_MV.get(), 6).define('#', Items.GOLD_INGOT).define('A', Tags.Items.DUSTS_REDSTONE).define('X', Items.GLASS_PANE).pattern("#X#").pattern("AAA").pattern("#X#").unlockedBy("has_item", has(Items.GOLD_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGYCABLE_MV.get(), 6).define('#', ModBlocks.ENERGYCABLE_LV.get()).define('A', Items.GOLD_INGOT).pattern("###").pattern("AAA").pattern("###").unlockedBy("has_item", has(Items.GOLD_INGOT)).save(consumer, new ResourceLocation("energy_cable_mv_2"));
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGYCABLE_HV.get(), 6).define('#', Items.GLASS_PANE).define('A', Tags.Items.DUSTS_REDSTONE).define('X', Items.DIAMOND).pattern("#X#").pattern("AAA").pattern("#X#").unlockedBy("has_item", has(Items.DIAMOND)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGYCABLE_HV.get(), 6).define('#', ModBlocks.ENERGYCABLE_MV.get()).define('A', Items.REDSTONE).define('X', Items.DIAMOND).pattern("###").pattern("AXA").pattern("###").unlockedBy("has_item", has(Items.DIAMOND)).save(consumer, new ResourceLocation("energy_cable_hv_2"));
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BARREL.get(), 1).define('#', Items.IRON_TRAPDOOR).define('A', Tags.Items.STORAGE_BLOCKS_IRON).pattern("#").pattern("A").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PORTABLE_GENERATOR.get(), 1).define('#', Tags.Items.INGOTS_IRON).define('Y', Items.FURNACE).define('A', ModItems.SMALL_MOTOR.get()).define('B', ModBlocks.BARREL.get()).pattern("#B#").pattern("A#Y").pattern("# #").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FLUID_PIPE.get(), 8).define('#', Tags.Items.INGOTS_COPPER).define('A', Tags.Items.NUGGETS_IRON).pattern("A#A").pattern("# #").pattern("A#A").unlockedBy("has_item", has(Tags.Items.INGOTS_COPPER)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FLUID_TANK.get(), 1).define('#', STEEL_ROD).define('A', ModBlocks.BARREL.get()).define('B', ModBlocks.FLUID_PIPE.get()).pattern("AAA").pattern("AAA").pattern("#B#").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIREBOX_SOLID.get(), 1).define('A', Tags.Items.DUSTS_REDSTONE).define('B', Items.HOPPER).define('C', Items.DROPPER).define('D', Items.FURNACE).pattern(" A ").pattern("BCD").pattern(" A ").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIREBOX_FLUID.get(), 1).define('A', Tags.Items.DUSTS_REDSTONE).define('B', ModBlocks.FLUID_PIPE.get()).define('C', ModBlocks.BARREL.get()).define('D', Items.FURNACE).pattern(" A ").pattern("CBD").pattern(" A ").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INDUSTRIAL_BATTERY_BANK.get(), 1).define('A', ModBlocks.ENERGYCABLE_HV.get()).define('B', ModBlocks.FRAME.get()).define('C', ModBlocks.SCAFFOLD.get()).pattern("ACA").pattern("B B").pattern("BCB").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STEAM_BOILER.get(), 1).define('A', ModBlocks.FLUID_PIPE.get()).define('B', ModBlocks.BARREL.get()).define('C', Tags.Items.DUSTS_REDSTONE).define('D', ModBlocks.FRAME.get()).pattern("ABA").pattern("CBC").pattern("DDD").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STEAM_TURBINE.get(), 1).define('A', ModBlocks.FLUID_PIPE.get()).define('B', ModBlocks.BARREL.get()).define('C', Tags.Items.DUSTS_REDSTONE).define('D', ModBlocks.FRAME.get()).define('E', ModItems.SMALL_MOTOR.get()).define('F', ModBlocks.BATTERY_BANK.get()).pattern("CBA").pattern("DED").pattern("DFD").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MINER.get(), 1).define('A', ModBlocks.FRAME.get()).define('B', ModItems.SMALL_MOTOR.get()).define('C', ModItems.BATTERY_LITHIUM.get()).define('D', ModBlocks.ELECTRIC_PUMP.get()).define('E', Items.HOPPER).define('F', ModBlocks.BARREL.get()).pattern("ABA").pattern("CDE").pattern("AFA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STORAGE_CHEST.get(), 1).define('#', ModBlocks.SCAFFOLD.get()).define('A', Tags.Items.CHESTS).pattern("AAA").pattern("AAA").pattern("# #").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TURBINE_PILLAR.get(), 6).define('#', Tags.Items.INGOTS_IRON).define('A', ModBlocks.ENERGYCABLE_LV.get()).pattern("#A#").pattern("#A#").pattern("#A#").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WIND_TURBINE.get(), 1).define('#', Tags.Items.INGOTS_IRON).define('A', ModItems.SMALL_MOTOR.get()).define('B', ModBlocks.ENERGYCABLE_LV.get()).define('C', STEEL_ROD).pattern(" # ").pattern("#AC").pattern(" B ").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WIND_BLADE.get(), 1).define('A', Tags.Items.INGOTS_IRON).define('B', STEEL_ROD).define('C', STEEL_INGOT).pattern("ABA").pattern("BCB").pattern("ABA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRILL_STEEL.get(), 1).define('A', STEEL_ROD).define('B', STEEL_INGOT).pattern(" A ").pattern(" A ").pattern("BBB").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRILL_DIAMOND.get(), 1).define('A', STEEL_ROD).define('B', STEEL_INGOT).define('C', Items.DIAMOND).pattern(" A ").pattern(" B ").pattern("CBC").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRILL_DEEP.get(), 1).define('A', STEEL_INGOT).define('B', STEEL_BLOCKS).define('C', Items.DIAMOND_BLOCK).pattern(" B ").pattern("ABA").pattern("C C").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPONGE_IRON.get(), 2).define('A', Items.IRON_INGOT).define('B', Items.COAL).pattern("AB ").pattern("BA ").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BATTERY_BANK.get(), 1).define('A', Tags.Items.INGOTS_IRON).define('B', ModItems.BATTERY.get()).define('D', Tags.Items.DUSTS_REDSTONE).pattern("ABA").pattern("BBB").pattern("ADA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TRASH.get(), 1).define('A', Tags.Items.INGOTS_IRON).define('B', Items.HOPPER).define('C', ModBlocks.FLUID_PIPE.get()).define('D', Items.ENDER_PEARL).define('E', ModBlocks.ENERGYCABLE_LV.get()).pattern("ABA").pattern("CDE").pattern("AAA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ELECTRIC_PUMP.get(), 1).define('A', Tags.Items.INGOTS_IRON).define('B', ModBlocks.FLUID_PIPE.get()).define('C', ModItems.SMALL_MOTOR.get()).define('D', IRON_ROD).pattern(" BA").pattern("CDA").pattern(" BA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        //        ShapedRecipeBuilder.shaped(ModBlocks.LOCKER.get(), 1)
        //                .define('A', IRON_ROD)
        //                .define('B', Tags.Items.CHESTS)
        //                .pattern(" A ")
        //                .pattern("ABA")
        //                .pattern(" A ")
        //                .unlockedBy("has_item", has(Items.IRON_INGOT))
        //                .save(consumer);
        
        
        //        ShapedRecipeBuilder.shaped(ModBlocks.CONCRETEWALL.get(), 8)
        //                .define('A', ModBlocks.CONCRETE.get())
        //                .pattern("AAA")
        //                .pattern("AAA")
        //                .unlockedBy("has_item", has(ModBlocks.CONCRETE.get()))
        //                .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LATHE.get(), 1).define('A', Tags.Items.INGOTS_IRON).define('B', ModItems.SMALL_MOTOR.get()).define('C', STEEL_INGOT).define('D', ModBlocks.BATTERY_BANK.get()).pattern("AAA").pattern("BCC").pattern("ADA").unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        //super.buildShapelessRecipes(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PILLAR.get(), 16).define('A', Tags.Items.INGOTS_IRON).define('B', IRON_ROD).pattern("BAB").pattern("BAB").pattern("BAB").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PILLAR_STEEL.get(), 16).define('A', STEEL_INGOT).define('B', STEEL_ROD).pattern("BAB").pattern("BAB").pattern("BAB").unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BRACE.get(), 16).define('A', Tags.Items.INGOTS_IRON).define('B', IRON_ROD).pattern("AB ").pattern("BAB").pattern(" BA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BRACE_STEEL.get(), 16).define('A', STEEL_INGOT).define('B', STEEL_ROD).pattern("AB ").pattern("BAB").pattern(" BA").unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COLUMN.get(), 16).define('A', IRON_ROD).define('B', Tags.Items.INGOTS_IRON).pattern("AAA").pattern("BBB").pattern("AAA").unlockedBy("has_item", has(Items.IRON_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COLUMN_STEEL.get(), 16).define('A', STEEL_ROD).define('B', STEEL_INGOT).pattern("AAA").pattern("BBB").pattern("AAA").unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATWALK.get(), 6).define('A', Items.IRON_BARS).define('B', IRON_ROD).pattern("B B").pattern("AAA").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATWALK_STEEL.get(), 6).define('A', Items.IRON_BARS).define('B', STEEL_ROD).pattern("B B").pattern("AAA").unlockedBy("has_item", has(STEEL_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATWALK_STAIR.get(), 6).define('A', Items.IRON_BARS).define('B', IRON_ROD).pattern("ABB").pattern(" AB").pattern("  A").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATWALK_STAIR_STEEL.get(), 6).define('A', Items.IRON_BARS).define('B', STEEL_ROD).pattern("ABB").pattern(" AB").pattern("  A").unlockedBy("has_item", has(STEEL_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HANDRAIL.get(), 4).define('A', IRON_ROD).pattern("AA").pattern("AA").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HANDRAIL_STEEL.get(), 4).define('A', STEEL_ROD).pattern("AA").pattern("AA").unlockedBy("has_item", has(STEEL_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATWALK_LADDER.get(), 6).define('A', IRON_ROD).define('B', Items.LADDER).pattern("A A").pattern("ABA").pattern("A A").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATWALK_LADDER_STEEL.get(), 6).define('A', STEEL_ROD).define('B', Items.LADDER).pattern("A A").pattern("ABA").pattern("A A").unlockedBy("has_item", has(STEEL_ROD)).save(consumer);
        
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PLATFORM.get(), 4).define('A', IRON_ROD).define('B', ItemTags.PLANKS).define('C', ModBlocks.PILLAR.get()).pattern("A A").pattern("ABA").pattern(" C ").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SCAFFOLD.get(), 12).define('A', IRON_ROD).define('B', ItemTags.PLANKS).pattern("ABA").pattern("A A").pattern("A A").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATWALK_GATE.get(), 2).define('A', IRON_ROD).define('B', Tags.Items.INGOTS_IRON).pattern("ABA").pattern("ABA").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.CATWALK_HATCH.get(), 1).requires(Items.IRON_TRAPDOOR).requires(Items.LADDER).unlockedBy("has_item", has(Tags.Items.INGOTS_IRON)).save(consumer);
        
        //        ShapedRecipeBuilder.shaped(ModBlocks.BIG_FENCE_COLUMN.get(), 6)
        //                .define('A', STEEL_INGOT)
        //                .define('B', IRON_ROD)
        //                .pattern("ABA")
        //                .pattern("ABA")
        //                .pattern("ABA")
        //                .unlockedBy("has_item", has(IRON_ROD))
        //                .save(consumer);
        //
        //        ShapedRecipeBuilder.shaped(ModBlocks.BIG_FENCE_WIRE.get(), 6)
        //                .define('A', STEEL_INGOT)
        //                .define('B', IRON_ROD)
        //                .pattern(" B ")
        //                .pattern(" B ")
        //                .pattern("BAB")
        //                .unlockedBy("has_item", has(IRON_ROD))
        //                .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ELECTRIC_FENCE.get(), 6).define('A', IRON_ROD).define('B', STEEL_ROD).pattern("ABA").pattern("ABA").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ELECTRIC_GATE.get(), 4).define('A', STEEL_ROD).define('B', Tags.Items.INGOTS_IRON).pattern("ABA").pattern("ABA").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAZOR_WIRE.get(), 6).define('A', Tags.Items.RODS_WOODEN).define('B', Tags.Items.NUGGETS_IRON).define('C', IRON_ROD).pattern("B B").pattern("CCC").pattern("ABA").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.LIGHT.get(), 1).requires(Tags.Items.DUSTS_GLOWSTONE).requires(Tags.Items.INGOTS_IRON).requires(Tags.Items.GLASS_PANES).unlockedBy("has_item", has(Tags.Items.DUSTS_GLOWSTONE)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FLUORESCENT.get(), 4).define('A', IRON_ROD).define('B', Blocks.GLOWSTONE).define('C', Tags.Items.GLASS_PANES).pattern(" A ").pattern("CBC").unlockedBy("has_item", has(Tags.Items.DUSTS_GLOWSTONE)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DAM_INTAKE.get(), 1).define('A', ModBlocks.CONCRETE.get()).define('B', Items.IRON_BARS).define('C', ModBlocks.HIGH_PRESSURE_PIPE.get()).pattern("AAA").pattern("C B").pattern("AAA").unlockedBy("has_item", has(ModBlocks.HIGH_PRESSURE_PIPE.get())).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DAM_OUTLET.get(), 1).define('A', ModBlocks.CONCRETE.get()).define('B', Items.IRON_BARS).pattern("AAA").pattern("B B").pattern("AAA").unlockedBy("has_item", has(ModBlocks.HIGH_PRESSURE_PIPE.get())).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HIGH_PRESSURE_PIPE.get(), 3).define('A', STEEL_INGOT).define('B', ModBlocks.FLUID_PIPE.get()).pattern("ABA").pattern("B B").pattern("ABA").unlockedBy("has_item", has(STEEL_INGOT)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DAM_TURBINE.get(), 1).define('A', STEEL_BLOCKS).define('B', STEEL_ROD).define('C', ModItems.SMALL_MOTOR.get()).define('D', ModBlocks.HIGH_PRESSURE_PIPE.get()).pattern("ABA").pattern("DBA").pattern("ACD").unlockedBy("has_item", has(ModBlocks.HIGH_PRESSURE_PIPE.get())).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DAM_GENERATOR.get(), 1).define('A', STEEL_INGOT).define('C', STEEL_ROD).define('B', ModItems.SMALL_MOTOR.get()).define('D', STEEL_BLOCKS).pattern("ACA").pattern("ABA").pattern("DBD").unlockedBy("has_item", has(ModBlocks.HIGH_PRESSURE_PIPE.get())).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ROTATIONAL_SHAFT.get(), 9).define('A', STEEL_BLOCKS).pattern("A").pattern("A").pattern("A").unlockedBy("has_item", has(STEEL_BLOCKS)).save(consumer);
        
        //        ShapedRecipeBuilder.shaped(ModBlocks.CARGO_LOADER.get(), 1)
        //                .define('A', Items.HOPPER)
        //                .define('B', ModBlocks.FRAME.get())
        //                .define('C', IRON_ROD)
        //                .define('D', ModItems.SMALL_MOTOR.get())
        //                .define('E', Tags.Items.DUSTS_REDSTONE)
        //                .pattern("ABC")
        //                .pattern("DBE")
        //                .pattern("ABC")
        //                .unlockedBy("has_item", has(IRON_ROD))
        //                .save(consumer);
        //
        //        ShapedRecipeBuilder.shaped(ModBlocks.FLUID_LOADER.get(), 1)
        //                .define('A', Items.HOPPER)
        //                .define('B', ModBlocks.FRAME.get())
        //                .define('C', IRON_ROD)
        //                .define('D', ModItems.SMALL_MOTOR.get())
        //                .define('E', Tags.Items.DUSTS_REDSTONE)
        //                .define('F', ModBlocks.FLUID_TANK.get())
        //                .pattern("ABC")
        //                .pattern("DBE")
        //                .pattern("FBC")
        //                .unlockedBy("has_item", has(IRON_ROD))
        //                .save(consumer);
        
        //        ShapedRecipeBuilder.shaped(ModBlocks.BOOSTER_RAIL.get(), 2)
        //                .define('A', Tags.Items.INGOTS_GOLD)
        //                .define('B', Tags.Items.RODS_WOODEN)
        //                .define('C', Tags.Items.DUSTS_REDSTONE)
        //                .pattern("ABA")
        //                .pattern("ACA")
        //                .pattern("ACA")
        //                .unlockedBy("has_item", has(Tags.Items.INGOTS_GOLD))
        //                .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CONVEYOR_BASIC.get(), 8).define('A', Tags.Items.LEATHER).define('B', IRON_ROD).define('C', ModItems.SMALL_MOTOR.get()).define('D', Tags.Items.INGOTS_IRON).pattern("AAA").pattern("BBC").pattern("DDD").unlockedBy("has_item", has(Tags.Items.INGOTS_IRON)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CONVEYOR_FAST.get(), 8).define('A', Tags.Items.LEATHER).define('B', STEEL_ROD).define('C', ModItems.SMALL_MOTOR.get()).define('D', Tags.Items.INGOTS_IRON).pattern("AAA").pattern("BCC").pattern("DDD").unlockedBy("has_item", has(Tags.Items.INGOTS_IRON)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CONVEYOR_EXPRESS.get(), 8).define('A', Tags.Items.LEATHER).define('C', ModItems.SMALL_MOTOR.get()).define('D', STEEL_INGOT).pattern("AAA").pattern("CCC").pattern("DDD").unlockedBy("has_item", has(Tags.Items.INGOTS_IRON)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INDUSTRIAL_FLOOR.get(), 6).define('A', Items.IRON_BARS).define('B', Tags.Items.INGOTS_IRON).pattern("AAA").pattern("   ").pattern("BBB").unlockedBy("has_item", has(Tags.Items.INGOTS_IRON)).save(consumer);
        
        //        ShapedRecipeBuilder.shaped(ModItems.CART_LINKABLE.get(), 1)
        //                .define('A', Tags.Items.INGOTS_IRON)
        //                .define('B', STEEL_INGOT)
        //                .define('C', Tags.Items.NUGGETS_IRON)
        //                .pattern("AB")
        //                .pattern("AC")
        //                .pattern("A ")
        //                .unlockedBy("has_item", has(STEEL_INGOT))
        //                .save(consumer);
        
        //        ShapelessRecipeBuilder.shapeless(ModItems.FLAT_CART.get(), 1)
        //                .requires(Items.SMOOTH_STONE_SLAB)
        //                .requires(Items.MINECART)
        //                .unlockedBy("has_item", has(Items.MINECART))
        //                .save(consumer);
        //
        //        ShapedRecipeBuilder.shaped(ModItems.CARGO_CART.get(), 1)
        //                .define('A', ModItems.FLAT_CART.get())
        //                .define('B', Tags.Items.CHESTS)
        //                .pattern("B")
        //                .pattern("A")
        //                .unlockedBy("has_item", has(ModItems.FLAT_CART.get()))
        //                .save(consumer);
        //
        //        ShapedRecipeBuilder.shaped(ModItems.PASSENGER_CART_MK1.get(), 1)
        //                .define('A', ModItems.FLAT_CART.get())
        //                .define('B', ItemTags.CARPETS)
        //                .define('C', ItemTags.PLANKS)
        //                .define('D', Tags.Items.RODS_WOODEN)
        //                .define('E', ItemTags.WOODEN_SLABS)
        //                .pattern("EEE")
        //                .pattern("DBD")
        //                .pattern("CAC")
        //                .unlockedBy("has_item", has(ModItems.FLAT_CART.get()))
        //                .save(consumer);
        //
        //        ShapedRecipeBuilder.shaped(ModItems.PASSENGER_CART_MK2.get(), 1)
        //                .define('A', ModItems.FLAT_CART.get())
        //                .define('B', ItemTags.CARPETS)
        //                .define('C', Tags.Items.INGOTS_IRON)
        //                .pattern("CCC")
        //                .pattern("CBC")
        //                .pattern("CAC")
        //                .unlockedBy("has_item", has(ModItems.FLAT_CART.get()))
        //                .save(consumer);
        //
        //        ShapedRecipeBuilder.shaped(ModItems.FLUID_CART.get(), 1)
        //                .define('A', ModItems.FLAT_CART.get())
        //                .define('B', ModBlocks.BARREL.get())
        //                .define('C', Tags.Items.INGOTS_IRON)
        //                .pattern("BBB")
        //                .pattern("CAC")
        //                .unlockedBy("has_item", has(ModItems.FLAT_CART.get()))
        //                .save(consumer);
        //
        //        ShapedRecipeBuilder.shaped(ModItems.STEAM_LOCOMOTIVE.get(), 1)
        //                .define('A', ModItems.FLAT_CART.get())
        //                .define('B', Blocks.BLAST_FURNACE)
        //                .define('C', ModBlocks.BARREL.get())
        //                .define('D', STEEL_INGOT)
        //                .pattern("DDD")
        //                .pattern("CBB")
        //                .pattern("AAA")
        //                .unlockedBy("has_item", has(ModItems.FLAT_CART.get()))
        //                .save(consumer);
        
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SPANEL_FRAME.get(), 1).define('A', IRON_ROD).define('B', ModItems.BATTERY.get()).define('C', ModBlocks.ENERGYCABLE_LV.get()).pattern("A  ").pattern("AA ").pattern("CBA").unlockedBy("has_item", has(IRON_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HV_ISOLATOR.get(), 1).define('A', GOLD_ROD).define('B', STEEL_INGOT).pattern("A").pattern("B").unlockedBy("has_item", has(STEEL_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TRANSFORMER.get(), 1).define('A', STEEL_INGOT).define('B', ModItems.BATTERY_LITHIUM.get()).define('C', GOLD_ROD).pattern("ACA").pattern("BBB").pattern("BBB").unlockedBy("has_item", has(STEEL_ROD)).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FLUID_VALVE.get(), 1).define('A', Items.LEVER).define('B', ModBlocks.HIGH_PRESSURE_PIPE.get()).define('C', Blocks.IRON_TRAPDOOR).pattern(" A ").pattern("BCB").pattern(" A ").unlockedBy("has_item", has(ModBlocks.HIGH_PRESSURE_PIPE.get())).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGY_SWITCH.get(), 1).define('A', Items.LEVER).define('B', ModBlocks.ENERGYCABLE_HV.get()).define('C', Tags.Items.STORAGE_BLOCKS_REDSTONE).pattern(" A ").pattern("BCB").pattern(" A ").unlockedBy("has_item", has(ModBlocks.ENERGYCABLE_HV.get())).save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FLUID_GAUGE.get(), 1)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.DUSTS_REDSTONE)
                .define('C', IRON_ROD)
                .define('D', ModBlocks.FLUID_PIPE.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ADA")
                .unlockedBy("has_item", has(ModBlocks.ENERGYCABLE_HV.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGY_LEVEL.get(), 1)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.DUSTS_REDSTONE)
                .define('C', IRON_ROD)
                .define('D', ModBlocks.ENERGYCABLE_LV.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ADA")
                .unlockedBy("has_item", has(ModBlocks.ENERGYCABLE_HV.get())).save(consumer);
        
        oreBlasting(consumer, SPONGE_IRON_LIST, RecipeCategory.MISC, ModItems.INGOT_STEEL.get(), 0.25f, 100, "spongeiron");
        oreSmelting(consumer, SPONGE_IRON_LIST, RecipeCategory.MISC, ModItems.INGOT_STEEL.get(), 0.25f, 200, "spongeiron");
    }
}
