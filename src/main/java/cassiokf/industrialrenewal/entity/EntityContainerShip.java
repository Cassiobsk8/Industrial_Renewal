package cassiokf.industrialrenewal.entity;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityContainerShip extends EntityBoat
{
    public EntityContainerShip(World worldIn)
    {
        super(worldIn);
    }

    public EntityContainerShip(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    protected void initEntityAI()
    {

    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        return false;
    }

    @Override
    public Item getItemBoat()
    {
        return super.getItemBoat();
    }
}
