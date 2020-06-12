package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.model.carts.ModelSteamLocomotive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

//@SideOnly(Side.CLIENT)
public class RenderSteamLocomotive<T extends EntitySteamLocomotive> extends Render<EntitySteamLocomotive>
{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/steamlocomotive.png");

    protected ModelSteamLocomotive modelMinecart = new ModelSteamLocomotive();

    public RenderSteamLocomotive(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(final EntitySteamLocomotive cart, double x, double y, double z, float yaw, final float partialTickTime)
    {
        //based on TechReborn's StevesCarts rotation code
        GlStateManager.pushMatrix();
        this.bindEntityTexture(cart);
        final double partialPosX = cart.lastTickPosX + (cart.posX - cart.lastTickPosX) * partialTickTime;
        final double partialPosY = cart.lastTickPosY + (cart.posY - cart.lastTickPosY) * partialTickTime;
        final double partialPosZ = cart.lastTickPosZ + (cart.posZ - cart.lastTickPosZ) * partialTickTime;
        float partialRotPitch = cart.prevRotationPitch + (cart.rotationPitch - cart.prevRotationPitch) * partialTickTime;
        final Vec3d posFromRail = cart.getPos(partialPosX, partialPosY, partialPosZ);
        if (posFromRail != null && cart.canUseRail())
        {
            final double predictionLength = 0.30000001192092896;
            Vec3d lastPos = cart.getPosOffset(partialPosX, partialPosY, partialPosZ, predictionLength);
            Vec3d nextPos = cart.getPosOffset(partialPosX, partialPosY, partialPosZ, -predictionLength);
            if (lastPos == null)
            {
                lastPos = posFromRail;
            }
            if (nextPos == null)
            {
                nextPos = posFromRail;
            }
            x += posFromRail.x - partialPosX;
            y += (lastPos.y + nextPos.y) / 2.0 - partialPosY;
            z += posFromRail.z - partialPosZ;
            Vec3d difference = nextPos.add(-lastPos.x, -lastPos.y, -lastPos.z);
            if (difference.length() != 0.0)
            {
                difference = difference.normalize();
                yaw = (float) (Math.atan2(difference.z, difference.x) * 180.0 / 3.141592653589793);
                partialRotPitch = (float) (Math.atan(difference.y) * 73.0);
            }
        }
        yaw = 180.0f - yaw;
        partialRotPitch *= -1.0f;
        float damageRot = cart.getRollingAmplitude() - partialTickTime;
        float damageTime = cart.getDamage() - partialTickTime;
        final float damageDir = cart.getRollingDirection();
        if (damageTime < 0.0f)
        {
            damageTime = 0.0f;
        }
        boolean flip = cart.motionX > 0.0 != cart.motionZ > 0.0;
        if (cart.cornerFlip)
        {
            flip = !flip;
        }
        if (cart.getRenderFlippedYaw(yaw + (flip ? 0.0f : 180.0f)))
        {
            flip = !flip;
        }
        GlStateManager.translate((float) x, (float) y + 0.375F, (float) z);
        GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(partialRotPitch, 0.0f, 0.0f, 1.0f);
        if (damageRot > 0.0f)
        {
            damageRot = MathHelper.sin(damageRot) * damageRot * damageTime / 10.0f * damageDir;
            GlStateManager.rotate(damageRot, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.rotate(flip ? 0.0f : 180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        modelMinecart.render(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    private void renderText(String text, double x, double y, double z)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.1F, 0.1F, 0.1F);
        GlStateManager.scale(0.07F, 0.07F, 1F);
        //RenderHelper.disableStandardItemLighting();
        int xh = -Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2;
        GlStateManager.translate(x, y, z);
        Minecraft.getMinecraft().fontRenderer.drawString(text, xh, 0, 0xFFFFFFFF);
        //RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySteamLocomotive entity)
    {
        return TEXTURES;
    }
}
