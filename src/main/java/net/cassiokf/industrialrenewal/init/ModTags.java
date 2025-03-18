package net.cassiokf.industrialrenewal.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static class Blocks {
        
        //        private static TagKey<Block> TEST = BlockTags.create(new ResourceLocation("forge:steam"));
    }
    
    public static class Items {
    
    }
    
    public static class Fluids {
        
        public static final TagKey<Fluid> STEAM_TAG = tag("forge:steam");
        
        public static TagKey<Fluid> tag(String name) {
            return FluidTags.create(new ResourceLocation(name));
        }
        
    }
}
