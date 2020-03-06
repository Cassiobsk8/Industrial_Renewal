package cassiokf.industrialrenewal.tileentity.abstracts;

import cassiokf.industrialrenewal.blocks.BlockElectricBigFenceColumn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;

public abstract class TEBigFenceBase extends TileEntity
{
    public static final ModelProperty<Boolean> CORE = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_LEFT = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_RIGHT = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_LEFT_DOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_RIGHT_DOWN = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_LEFT_TOP = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTIVE_RIGHT_TOP = new ModelProperty<>();

    public TEBigFenceBase(TileEntityType<?> p_i48289_1_)
    {
        super(p_i48289_1_);
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(CORE, getBlockState().get(BlockElectricBigFenceColumn.INDEX) == 1)
                .withInitial(ACTIVE_LEFT, activeSide(true, false, false))
                .withInitial(ACTIVE_RIGHT, activeSide(false, false, false))
                .withInitial(ACTIVE_LEFT_TOP, activeSide(true, true, false))
                .withInitial(ACTIVE_RIGHT_TOP, activeSide(false, true, false))
                .withInitial(ACTIVE_LEFT_DOWN, activeSide(true, false, true))
                .withInitial(ACTIVE_RIGHT_DOWN, activeSide(false, false, true))
                .build();
    }

    public abstract boolean activeSide(boolean left, boolean top, boolean down);

    public void requestAllModelsUpdate()
    {
        requestModelDataUpdate();
        int index = getBlockState().get(BlockElectricBigFenceColumn.INDEX);
        switch (index)
        {
            default:
            case 0:
                TileEntity te1 = world.getTileEntity(pos.up());
                if (te1 != null) te1.requestModelDataUpdate();
                te1 = world.getTileEntity(pos.up(2));
                if (te1 != null) te1.requestModelDataUpdate();
                break;
            case 1:
                te1 = world.getTileEntity(pos.up());
                if (te1 != null) te1.requestModelDataUpdate();
                te1 = world.getTileEntity(pos.down());
                if (te1 != null) te1.requestModelDataUpdate();
                break;
            case 2:
                te1 = world.getTileEntity(pos.down());
                if (te1 != null) te1.requestModelDataUpdate();
                te1 = world.getTileEntity(pos.down(2));
                if (te1 != null) te1.requestModelDataUpdate();
                break;
        }
    }
}
