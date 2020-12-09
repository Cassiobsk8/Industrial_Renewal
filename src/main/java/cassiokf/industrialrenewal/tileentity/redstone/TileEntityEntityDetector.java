package cassiokf.industrialrenewal.tileentity.redstone;

import cassiokf.industrialrenewal.blocks.redstone.BlockEntityDetector;
import cassiokf.industrialrenewal.tileentity.abstracts.TileEntitySync;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityEntityDetector extends TileEntitySync implements ITickableTileEntity
{

    private Direction blockFacing = Direction.DOWN;
    private int distanceD = 6;
    private int tick = 0;
    private entityEnum eEnum = entityEnum.ALL;

    public TileEntityEntityDetector(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public enum entityEnum
    {
        ALL(0),
        PLAYERS(1),
        MOBHOSTIL(2),
        MOBPASSIVE(3),
        ITEMS(4),
        CARTS(5);

        public int intValue;

        entityEnum(int value) {
            intValue = value;
        }

        public static entityEnum valueOf(int no) {
            if (no > entityEnum.values().length - 1) {
                no = 0;
            }
            for (entityEnum l : entityEnum.values()) {
                if (l.intValue == no) return l;
            }
            throw new IllegalArgumentException("entityEnum not found");
        }
    }

    private Class<? extends Entity> getEntityToFilter() {
        switch (eEnum) {
            default:
            case ALL:
                return Entity.class;
            case PLAYERS:
                return PlayerEntity.class;
            case MOBHOSTIL:
                return MobEntity.class;
            case MOBPASSIVE:
                return CreatureEntity.class;
            case ITEMS:
                return ItemEntity.class;
            case CARTS:
                return MinecartEntity.class;
        }
    }

    @Override
    public void tick() {
        if (!this.world.isRemote && ((tick % 10) == 0))
        {
            tick = 0;
            changeState(passRedstone());
        }
        tick++;
    }

    private void changeState(boolean value) {
        BlockState state = getBlockState();
        boolean actualValue = state.get(BlockEntityDetector.ACTIVE);
        if (actualValue != value) {
            this.world.setBlockState(this.pos, state.with(BlockEntityDetector.ACTIVE, value), 3);
            this.sync();
            world.notifyNeighborsOfStateChange(this.pos.offset(getBlockFacing()), state.getBlock());
        }
    }

    public boolean passRedstone() {
        BlockState state = getBlockState();
        Direction inFace = state.get(BlockEntityDetector.FACING);
        return checkEntity(getEntityToFilter(), inFace);
    }


    private boolean checkEntity(Class<? extends Entity> entity, Direction facing) {
        int distance = distanceD + 1;
        double posX = this.pos.getX() + 1;
        double posY = this.pos.getY() + 1;
        double posZ = this.pos.getZ() + 1;
        switch (facing) {
            case DOWN:
                posY = posY + distance;
                break;
            case UP:
                posY = posY - distance;
                break;
            case NORTH:
                posZ = posZ - distance;
                break;
            case SOUTH:
                posZ = posZ + distance;
                break;
            case WEST:
                posX = posX - distance;
                break;
            case EAST:
                posX = posX + distance;
                break;
        }
        List<? extends Entity> entities = this.world.getEntitiesWithinAABB(entity,
                new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), posX, posY, posZ));
        return !entities.isEmpty();
    }

    public void setBlockFacing(Direction facing) {
        blockFacing = facing;
        markDirty();
    }

    public Direction getBlockFacing() {
        return blockFacing;
    }

    public entityEnum getEntityEnum() {
        return eEnum;
    }

    public void setNextEntityEnum(boolean value) {
        int old = getEntityEnum().intValue;
        if (value) {
            eEnum = TileEntityEntityDetector.entityEnum.valueOf(old + 1);
        }
        this.sync();
    }

    public void setNextDistance() {
        distanceD = distanceD + 1;
        if (distanceD > 8) {
            distanceD = 1;
        }
        this.sync();
    }

    public int getDistance() {
        return distanceD;
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag) {
        tag.putInt("baseFacing", blockFacing.getIndex());
        tag.putInt("distance", distanceD);
        tag.putInt("EnumConfig", this.eEnum.intValue);
        return super.write(tag);
    }

    @Override
    public void read(final CompoundNBT tag) {
        super.read(tag);
        blockFacing = Direction.byIndex(tag.getInt("baseFacing"));
        distanceD = tag.getInt("distance");
        eEnum = entityEnum.valueOf(tag.getInt("EnumConfig"));
    }
}
