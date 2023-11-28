package net.cassiokf.industrialrenewal.data.client;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, IndustrialRenewal.MODID, exFileHelper);
    }
    
    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.BLOCKHAZARD);
        blockWithItem(ModBlocks.CAUTIONHAZARD);
        blockWithItem(ModBlocks.DEFECTIVEHAZARD);
        blockWithItem(ModBlocks.SAFETYHAZARD);
        blockWithItem(ModBlocks.RADIATIONHAZARD);
        blockWithItem(ModBlocks.FIREHAZARD);
        blockWithItem(ModBlocks.AISLEHAZARD);
        blockWithItem(ModBlocks.STEELBLOCK);
        blockWithItem(ModBlocks.CONCRETE);
    }
    
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
