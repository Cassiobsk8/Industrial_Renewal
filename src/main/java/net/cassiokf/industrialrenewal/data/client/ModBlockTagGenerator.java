package net.cassiokf.industrialrenewal.data.client;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, IndustrialRenewal.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        
        addAllPickAxe(ModBlocks.BLOCKS);
        
//        this.tag(ModTags.Blocks.TESTETAG)
//                .add(ModBlocks.STEELBLOCK.get()).addTag(Tags.Blocks.STORAGE_BLOCKS_IRON);
//        this.tag(BlockTags.NEEDS_IRON_TOOL)
//                .add(ModBlocks.STEELBLOCK.get());
    }
    
    private void addAllPickAxe(DeferredRegister<Block> BLOCKS) {
        for (RegistryObject<Block> rb : BLOCKS.getEntries()) {
            Block b = rb.get();
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(b);
        }
    }
}
