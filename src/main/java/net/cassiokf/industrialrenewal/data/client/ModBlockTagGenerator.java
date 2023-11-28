package net.cassiokf.industrialrenewal.data.client;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, IndustrialRenewal.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
//        this.tag(ModTags.Blocks.TESTETAG)
//                .add(ModBlocks.STEELBLOCK.get()).addTag(Tags.Blocks.STORAGE_BLOCKS_IRON);
//        this.tag(BlockTags.NEEDS_IRON_TOOL)
//                .add(ModBlocks.STEELBLOCK.get());
    }
}
