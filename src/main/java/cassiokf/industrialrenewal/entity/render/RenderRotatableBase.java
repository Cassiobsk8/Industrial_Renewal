package cassiokf.industrialrenewal.entity.render;

import cassiokf.industrialrenewal.entity.RotatableBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderRotatableBase<T extends RotatableBase> extends RenderBase<T>
{
    public RenderRotatableBase(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public abstract ModelBase getModel();

    @Override
    public void doRender(final T cart, double x, double y, double z, float yaw, final float partialTickTime)
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
        yaw += (flip ? 0.0f : 180.0f);
        getModel().render(cart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
