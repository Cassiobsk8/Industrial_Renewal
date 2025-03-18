package net.cassiokf.industrialrenewal.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class IRRenderType extends RenderType {
    
    private final static ResourceLocation wireResourceLocation = new ResourceLocation(IndustrialRenewal.MODID + ":textures/misc/laser.png");
    public static final RenderType WIRE_RENDER_TYPE = create("WireRenderType", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setTextureState(new TextureStateShard(wireResourceLocation, false, false)).setShaderState(ShaderStateShard.POSITION_COLOR_TEX_SHADER).setLayeringState(NO_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).setCullState(CULL).setLightmapState(NO_LIGHTMAP).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(false));
    
    public IRRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
    
}
