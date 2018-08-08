package cassiokf.industrialrenewal.util;

import cassiokf.industrialrenewal.entity.EntityCargoContainer;
import cassiokf.industrialrenewal.entity.render.RenderCargoContainer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {
    public static void registerEntitiesRender() {
        RenderingRegistry.registerEntityRenderingHandler(EntityCargoContainer.class, new IRenderFactory<EntityCargoContainer>() {
            @Override
            public Render<? super EntityCargoContainer> createRenderFor(RenderManager manager) {
                return new RenderCargoContainer(manager);
            }
        });
    }
}
