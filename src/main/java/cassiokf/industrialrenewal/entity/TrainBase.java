package cassiokf.industrialrenewal.entity;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class TrainBase extends EntityMinecart
{
    public float length;

    public TrainBase(World worldIn)
    {
        super(worldIn);
        this.length = width;
    }

    public TrainBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    protected void setSize(float width, float height, float length)
    {
        if (width != this.width || height != this.height)
        {
            float f0 = this.width;
            float f1 = this.length;
            this.width = width;
            this.length = length;
            this.height = height;

            if (this.width < f0 || this.length < f1)
            {
                double d0 = (double) width / 2.0D;
                double d1 = length / 2.0D;
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d1, this.posX + d0, this.posY + (double) this.height, this.posZ + d1));
                return;
            }

            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) this.width, axisalignedbb.minY + (double) this.height, axisalignedbb.minZ + (double) this.length));

            if ((this.width > f0 || this.length > f1) && !this.firstUpdate && !this.world.isRemote)
            {
                this.move(MoverType.SELF, (double) (f0 - this.width), 0.0D, (double) (f1 - this.length));
            }
        }
    }

    //public void setPosition(double x, double y, double z)
    //{
    //    this.posX = x;
    //    this.posY = y;
    //    this.posZ = z;
    //    float f = this.width / 2.0F;
    //    float f1 = this.height;
    //    float f2 = this.length / 2.0F;
    //    this.setEntityBoundingBox(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f2));
    //}
}
