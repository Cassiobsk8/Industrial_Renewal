package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTransformerHV extends BlockMultiBlockBase<TileEntityTransformerHV>
{
    public static final PropertyInteger OUTPUT = PropertyInteger.create("output", 0, 2);

    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);

    public BlockTransformerHV(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x2x3CenteredPlus1OnTop(masterPos);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER, OUTPUT);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        /*
        tooltip.add(I18n.format("info.industrialrenewal.requires")
                + ": "
                + FluidInit.STEAM.getName()
                + " "
                + (IRConfig.MainConfig.Main.steamTurbineSteamPerTick)
                + " mB/t");
        tooltip.add(I18n.format("info.industrialrenewal.produces")
                + ": "
                + (IRConfig.MainConfig.Main.steamTurbineEnergyPerTick)
                + " FE/t");
         */
        super.addInformation(stack, player, tooltip, advanced);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityTransformerHV && pos.equals(((TileEntityTransformerHV) te).getConnectorPos()))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
        } else
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TileEntity te = source.getTileEntity(pos);
        if (te instanceof TileEntityTransformerHV && pos.equals(((TileEntityTransformerHV) te).getConnectorPos()))
        {
            return DOWN_AABB;
        } else
        {
            return FULL_BLOCK_AABB;
        }

    }

    @Override
    public  BlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        int type;
        if (!(te instanceof TileEntityTransformerHV) || !((TileEntityTransformerHV) te).isMaster()) type = 0;
        else type = (((TileEntityTransformerHV) te).isOutPut) ? 2 : 1;
        return state.withProperty(OUTPUT, type);
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public TileEntityTransformerHV createTileEntity(World world,  BlockState state)
    {
        return new TileEntityTransformerHV();
    }
}
