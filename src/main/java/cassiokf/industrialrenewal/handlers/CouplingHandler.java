package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.entity.EntityTenderBase;
import cassiokf.industrialrenewal.entity.LocomotiveBase;
import cassiokf.industrialrenewal.util.enums.EnumCouplingType;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

/*
       High Inspired on Railcraft's CovertJaguar Linkage system
       https://github.com/Railcraft/Railcraft
 */
public class CouplingHandler
{
    public static @Nullable
    MinecartEntity getCartFromUUID(@Nullable World world, @Nullable UUID id)
    {
        if (world == null || id == null)
            return null;
        if (world instanceof ServerWorld)
        {
            Entity entity = ((ServerWorld) world).getEntityByUuid(id);
            if (entity instanceof MinecartEntity && entity.isAlive())
            {
                return (MinecartEntity) entity;
            }
        } else
        {
            for (Entity entity : world.loadedEntityList)
            {
                if (entity instanceof MinecartEntity && entity.isAlive() && entity.getUniqueID().equals(id))
                    return (MinecartEntity) entity;
            }
        }
        return null;
    }

    public static void onMinecartTick(MinecartEntity cart)
    {
        boolean connectedA = updateCartVelocity(cart, EnumCouplingType.COUPLING_1);
        boolean connectedB = updateCartVelocity(cart, EnumCouplingType.COUPLING_2);

        if (connectedA || connectedB)
        {
            applyDrag(cart);
        }
    }

    private static void applyDrag(MinecartEntity cart)
    {
        cart.motionX *= 0.99;
        cart.motionZ *= 0.99;
    }

    private static boolean updateCartVelocity(MinecartEntity cart1, EnumCouplingType ConnectionType)
    {
        MinecartEntity cart2 = getCartFromUUID(cart1.world, getConnection(cart1, ConnectionType));
        if (cart2 != null && cart1 != cart2)
        {
            double dist = cart1.getDistance(cart2);
            if (dist > 10F)
            {
                removeConnection(cart1, cart2);
                return false;
            }

            if (cart1 instanceof EntityTenderBase && cart2 instanceof LocomotiveBase && ((LocomotiveBase) cart2).tender != cart1)
            {
                ((LocomotiveBase) cart2).setTender((EntityTenderBase) cart1);
            }
            else if (cart2 instanceof EntityTenderBase && cart1 instanceof LocomotiveBase && ((LocomotiveBase) cart1).tender != cart2)
            {
                ((LocomotiveBase) cart1).setTender((EntityTenderBase) cart2);
            }

            Vec3d normalized = new Vec3d(cart2.getPosition().getX() - cart1.getPosition().getX(), cart2.getPosition().getY() - cart1.getPosition().getY(), cart2.getPosition().getZ() - cart1.getPosition().getZ());
            normalized = normalize(normalized);

            // Spring force
            applySpringForce(cart1, cart2, dist, normalized);

            // Damping force
            applyDampingForce(cart1, cart2, normalized);

            return true;
        }
        return false;
    }

    public static Vec3d normalize(Vec3d toNorm)
    {
        Double x = toNorm.getX();
        Double z = toNorm.getZ();
        Double sqrt = Math.sqrt(x * x + z * z);
        if (sqrt != 0)
        {
            return new Vec3d(x / sqrt, toNorm.y, z / sqrt);
        }
        return toNorm;
    }

    private static void applySpringForce(MinecartEntity cart1, MinecartEntity cart2, double distance, Vec3d normalized)
    {
        double stretch = 1.5F * (distance - getDistanceBetween(cart1, cart2));

        double springX = stretch * normalized.getX();
        double springZ = stretch * normalized.getZ();

        springX = limitForce(springX);
        springZ = limitForce(springZ);

        cart1.motionX += springX;
        cart1.motionZ += springZ;
        cart2.motionX -= springX;
        cart2.motionZ -= springZ;
    }

    private static void applyDampingForce(MinecartEntity cart1, MinecartEntity cart2, Vec3d norm)
    {
        double normal = 0.3F * (((cart2.getMotion().getX() - cart1.getMotion().getX()) * norm.getX()) + ((cart2.getMotion().getZ() - cart1.getMotion().getZ()) * norm.getZ()));

        double dampX = normal * norm.getX();
        double dampZ = normal * norm.getZ();

        dampX = limitForce(dampX);
        dampZ = limitForce(dampZ);

        cart1.motionX += dampX;
        cart1.motionZ += dampZ;
        cart2.motionX -= dampX;
        cart2.motionZ -= dampZ;
    }

    private static double limitForce(double force)
    {
        return Math.copySign(Math.min(Math.abs(force), 6F), force);
    }

    private static float getDistanceBetween(MinecartEntity cart1, MinecartEntity cart2)
    {
        float dist = 0;
        if (cart1 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart1).getFixedDistance(cart2);
        else
            dist += 0.78f;
        if (cart2 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart2).getFixedDistance(cart1);
        else
            dist += 0.78f;
        return dist;
    }

    public static UUID getConnection(MinecartEntity cart, EnumCouplingType ConnectionType)
    {
        long high = cart.getPersistentData().getLong(ConnectionType.tagMostSigBits);
        long low = cart.getPersistentData().getLong(ConnectionType.tagLeastSigBits);
        return new UUID(high, low);
    }

    public static boolean isConnected(MinecartEntity cart1, MinecartEntity cart2)
    {
        if (cart1 == cart2) return false;

        UUID id1 = cart1.getUniqueID();
        UUID id2 = cart2.getUniqueID();
        boolean cart1Connected = id2.equals(getConnection(cart1, EnumCouplingType.COUPLING_1))
                || id2.equals(getConnection(cart1, EnumCouplingType.COUPLING_2));
        boolean cart2Connected = id1.equals(getConnection(cart2, EnumCouplingType.COUPLING_1))
                || id1.equals(getConnection(cart2, EnumCouplingType.COUPLING_2));

        return cart1Connected || cart2Connected;
    }

    public static void removeConnection(MinecartEntity one, MinecartEntity two)
    {
        EnumCouplingType firstConnection = getConnectionType(one, two);
        EnumCouplingType secondConnection = getConnectionType(two, one);

        if (firstConnection != null)
        {
            removeConnection(one, firstConnection);
        }
        if (secondConnection != null)
        {
            removeConnection(two, secondConnection);
        }
    }

    private static EnumCouplingType getConnectionType(MinecartEntity from, MinecartEntity to)
    {
        UUID id = to.getUniqueID();
        if (id.equals(getConnection(from, EnumCouplingType.COUPLING_1)))
            return EnumCouplingType.COUPLING_1;
        else if (id.equals(getConnection(from, EnumCouplingType.COUPLING_2)))
            return EnumCouplingType.COUPLING_2;
        return null;
    }

    private static void removeConnection(MinecartEntity cart, EnumCouplingType ConnectionType)
    {
        cart.getPersistentData().remove(ConnectionType.tagMostSigBits);
        cart.getPersistentData().remove(ConnectionType.tagLeastSigBits);
    }
}
