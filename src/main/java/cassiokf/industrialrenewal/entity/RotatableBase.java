package cassiokf.industrialrenewal.entity;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RotatableBase extends TrainBase
{
    public boolean cornerFlip;
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;

    public RotatableBase(World worldIn)
    {
        super(worldIn);
    }

    public RotatableBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public boolean getRenderFlippedYaw(float yaw)
    {
        yaw %= 360.0f;
        if (yaw < 0.0f)
        {
            yaw += 360.0f;
        }
        if (!oldRender || Math.abs(yaw - lastRenderYaw) < 90.0f || Math.abs(yaw - lastRenderYaw) > 270.0f || (motionX > 0.0 && lastMotionX < 0.0) || (motionZ > 0.0 && lastMotionZ < 0.0)
                || (motionX < 0.0 && lastMotionX > 0.0) || (motionZ < 0.0 && lastMotionZ > 0.0) || wrongRender >= 50)
        {
            lastMotionX = motionX;
            lastMotionZ = motionZ;
            lastRenderYaw = yaw;
            oldRender = true;
            wrongRender = 0;
            return false;
        }
        ++wrongRender;
        return true;
    }

    @Override
    public void moveMinecartOnRail(BlockPos pos) {
        super.moveMinecartOnRail(pos);

         BlockState blockState = world.getBlockState(pos);

        BlockRailBase.EnumRailDirection railDirection = ((BlockRailBase) blockState.getBlock()).getRailDirection(world, pos, blockState, this);
        cornerFlip = ((railDirection == BlockRailBase.EnumRailDirection.SOUTH_EAST || railDirection == BlockRailBase.EnumRailDirection.SOUTH_WEST) && motionX < 0.0)
                || ((railDirection == BlockRailBase.EnumRailDirection.NORTH_EAST || railDirection == BlockRailBase.EnumRailDirection.NORTH_WEST) && motionX > 0.0);
        //if (blockState.getBlock() != ModBlocks.ADVANCED_DETECTOR.getBlock() && isDisabled()) {
        //    releaseCart();
        //}

        //if (fixedRailPos != null && !fixedRailPos.equals(pos)) {
        //    fixedRailDirection = null;
        //    fixedRailPos = new BlockPos(fixedRailPos.getX(), -1, fixedRailPos.getZ());
        //}
    }
}
