package net.cassiokf.industrialrenewal.tab;

import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndustrialRenewal.MODID);
    
    public static final RegistryObject<CreativeModeTab> INDR_TAB =
            CREATIVE_MODE_TABS.register("indr_tab",
                    () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SCREW_DRIVER.get()))
                            .title(Component.translatable("creativetab.indr_tab"))
                            .displayItems((pParameters, pOutput) -> {
                                for (RegistryObject<Item> item : ModItems.ITEMS.getEntries()) {
                                    pOutput.accept(item.get());
                                }
                            })
                            .build());
    
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
