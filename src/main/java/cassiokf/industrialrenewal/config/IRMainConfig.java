package cassiokf.industrialrenewal.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class IRMainConfig
{
    public static ForgeConfigSpec.IntValue tutorial_chance;
    public static ForgeConfigSpec.BooleanValue generate_overworld;

    public static void init(ForgeConfigSpec.Builder commom)
    {
        commom.comment("Main Config");
/*
        tutorial_chance = commom
                .comment("Maximum number of ore veins of the tutorial ore that can spawn in one chunk.")
                .defineInRange("oregen.tutorial_chance", 20, 1, 1000000);

        generate_overworld = commom
                .comment("Decide if you want Tutorial Mod ores to spawn in the overworld")
                .define("oregen.generate_overworld", true);*/
    }
}
