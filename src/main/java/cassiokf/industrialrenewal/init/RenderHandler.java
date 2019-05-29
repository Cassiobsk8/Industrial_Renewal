package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.entity.*;
import cassiokf.industrialrenewal.entity.render.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {

    public static void registerEntitiesRender() {
        RenderingRegistry.registerEntityRenderingHandler(EntityCargoContainer.class, new IRenderFactory<EntityCargoContainer>() {
            @Override
            public Render<? super EntityCargoContainer> createRenderFor(RenderManager manager) {
                return new RenderCargoContainer<>(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityFluidContainer.class, new IRenderFactory<EntityFluidContainer>() {
            @Override
            public Render<? super EntityFluidContainer> createRenderFor(RenderManager manager) {
                return new RenderFluidContainer<>(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntitySteamLocomotive.class, new IRenderFactory<EntitySteamLocomotive>() {
            @Override
            public Render<? super EntitySteamLocomotive> createRenderFor(RenderManager manager) {
                return new RenderSteamLocomotive<>(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityLogCart.class, new IRenderFactory<EntityLogCart>() {
            @Override
            public Render<? super EntityLogCart> createRenderFor(RenderManager manager) {
                return new RenderLogCart<>(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityPassengerCar.class, new IRenderFactory<EntityPassengerCar>() {
            @Override
            public Render<? super EntityPassengerCar> createRenderFor(RenderManager manager) {
                return new RenderPassengerCar<>(manager);
            }
        });
    }
}
