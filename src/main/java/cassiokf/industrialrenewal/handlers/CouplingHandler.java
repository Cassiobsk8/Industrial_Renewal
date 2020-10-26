package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.entity.EntityTenderBase;
import cassiokf.industrialrenewal.entity.LocomotiveBase;
import cassiokf.industrialrenewal.util.enums.EnumCouplingType;
import cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import javax.vecmath.Vector2d;
import java.util.UUID;

/*
       High Inspired on Railcraft's CovertJaguar Linkage system
       https://github.com/Railcraft/Railcraft
 */
public class CouplingHandler
{
    public static @Nullable
    EntityMinecart getCartFromUUID(@Nullable World world, @Nullable UUID id)
    {
        if (world == null || id == null)
            return null;
        if (world instanceof WorldServer)
        {
            Entity entity = ((WorldServer) world).getEntityFromUuid(id);
            if (entity instanceof EntityMinecart && entity.isEntityAlive())
            {
                return (EntityMinecart) entity;
            }
        } else
        {
            for (Entity entity : world.loadedEntityList)
            {
                if (entity instanceof EntityMinecart && entity.isEntityAlive() && entity.getPersistentID().equals(id))
                    return (EntityMinecart) entity;
            }
        }
        return null;
    }

    public static void onMinecartTick(EntityMinecart cart)
    {
        boolean connectedA = updateCartVelocity(cart, EnumCouplingType.COUPLING_1);
        boolean connectedB = updateCartVelocity(cart, EnumCouplingType.COUPLING_2);

        if (connectedA || connectedB)
        {
            applyDrag(cart);
        }
    }

    private static void applyDrag(EntityMinecart cart)
    {
        cart.motionX *= 0.96;
        cart.motionZ *= 0.96;
    }

    private static boolean updateCartVelocity(EntityMinecart cart1, EnumCouplingType ConnectionType)
    {
        EntityMinecart cart2 = getCartFromUUID(cart1.world, getConnection(cart1, ConnectionType));
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

            Vector2d cart1Pos = new Vector2d(cart1.posX, cart1.posZ);
            Vector2d cart2Pos = new Vector2d(cart2.posX, cart2.posZ);
            Vector2d normalized = new Vector2d(cart2Pos.x - cart1Pos.x, cart2Pos.y - cart1Pos.y);
            normalized = normalize(normalized);

            // Spring force
            applySpringForce(cart1, cart2, dist, normalized);

            // Damping force
            applyDampingForce(cart1, cart2, normalized);

            return true;
        }
        return false;
    }

    public static Vector2d normalize(Vector2d toNorm)
    {
        double x = toNorm.x;
        double y = toNorm.y;
        double sqrt = Math.sqrt(x * x + y * y);
        if (sqrt != 0)
        {
            return new Vector2d(x / sqrt, y / sqrt);
        }
        return toNorm;
    }

    private static void applySpringForce(EntityMinecart cart1, EntityMinecart cart2, double distance, Vector2d normalized)
    {
        double stretch = 0.8F * (distance - getDistanceBetween(cart1, cart2));

        double springX = stretch * normalized.getX();
        double springZ = stretch * normalized.getY();

        springX = limitForce(springX);
        springZ = limitForce(springZ);

        cart1.motionX += springX;
        cart1.motionZ += springZ;
        cart2.motionX -= springX;
        cart2.motionZ -= springZ;
    }

    private static void applyDampingForce(EntityMinecart cart1, EntityMinecart cart2, Vector2d norm)
    {
        Vector2d cart1Vel = new Vector2d(cart1.motionX, cart1.motionZ);
        Vector2d cart2Vel = new Vector2d(cart2.motionX, cart2.motionZ);

        double normal = 0.3F * (((cart2Vel.getX() - cart1Vel.getX()) * norm.getX()) + ((cart2Vel.getY() - cart1Vel.getY()) * norm.getY()));

        double dampX = normal * norm.getX();
        double dampZ = normal * norm.getY();

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

    private static float getDistanceBetween(EntityMinecart cart1, EntityMinecart cart2)
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

    public static UUID getConnection(EntityMinecart cart, EnumCouplingType ConnectionType)
    {
        long high = cart.getEntityData().getLong(ConnectionType.tagMostSigBits);
        long low = cart.getEntityData().getLong(ConnectionType.tagLeastSigBits);
        return new UUID(high, low);
    }

    public static boolean isConnected(EntityMinecart cart1, EntityMinecart cart2)
    {
        if (cart1 == cart2) return false;

        UUID id1 = cart1.getPersistentID();
        UUID id2 = cart2.getPersistentID();
        boolean cart1Connected = id2.equals(getConnection(cart1, EnumCouplingType.COUPLING_1))
                || id2.equals(getConnection(cart1, EnumCouplingType.COUPLING_2));
        boolean cart2Connected = id1.equals(getConnection(cart2, EnumCouplingType.COUPLING_1))
                || id1.equals(getConnection(cart2, EnumCouplingType.COUPLING_2));

        return cart1Connected || cart2Connected;
    }

    public static void removeConnection(EntityMinecart one, EntityMinecart two)
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

    private static EnumCouplingType getConnectionType(EntityMinecart from, EntityMinecart to)
    {
        UUID id = to.getPersistentID();
        if (id.equals(getConnection(from, EnumCouplingType.COUPLING_1)))
            return EnumCouplingType.COUPLING_1;
        else if (id.equals(getConnection(from, EnumCouplingType.COUPLING_2)))
            return EnumCouplingType.COUPLING_2;
        return null;
    }

    private static void removeConnection(EntityMinecart cart, EnumCouplingType ConnectionType)
    {
        cart.getEntityData().removeTag(ConnectionType.tagMostSigBits);
        cart.getEntityData().removeTag(ConnectionType.tagLeastSigBits);
    }
}
