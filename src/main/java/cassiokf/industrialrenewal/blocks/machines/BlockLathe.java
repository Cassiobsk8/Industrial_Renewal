package cassiokf.industrialrenewal.blocks.machines;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.tileentity.machines.TELathe;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockLathe extends BlockMultiBlockBase<TELathe>
{
    public BlockLathe(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,  BlockState state, PlayerEntity player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) OpenGUI(world, pos, player);
        return true;
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TELathe)
        {
            TELathe masterTE = ((TELathe) te).getMaster();
            player.openGui(IndustrialRenewal.instance, GUIHandler.LATHE, world, masterTE.getPos().getX(), masterTE.getPos().getY(), masterTE.getPos().getZ());
        }
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, EnumFacing facing)
    {
        return MachinesUtils.getBlocksIn3x2x2Centered(masterPos, facing);
    }

    @Override
    public  BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(MASTER, true);
    }

    @Override
    protected BlockPos getMasterPosBasedOnPlace(BlockPos pos, EnumFacing facing) {
        return pos;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos,  BlockState state, EntityLivingBase placer, ItemStack stack)
    {
    }

    @Nullable
    @Override
    public TELathe createTileEntity(World world,  BlockState state)
    {
        return new TELathe();
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
