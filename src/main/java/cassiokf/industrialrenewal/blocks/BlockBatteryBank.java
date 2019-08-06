package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;

public class BlockBatteryBank extends BlockTileEntityConnected<TileEntityBatteryBank>
{



    public BlockBatteryBank(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setDefaultState(getDefaultState());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (hand.equals(EnumHand.MAIN_HAND) && playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(ModItems.screwDrive) && worldIn.getTileEntity(pos) instanceof TileEntityBatteryBank)
        {
            TileEntityBatteryBank te = (TileEntityBatteryBank) worldIn.getTileEntity(pos);
            if (te != null) te.toggleFacing(facing);
            if (!worldIn.isRemote) ItemPowerScrewDrive.playDrillSound(worldIn, pos);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntityBatteryBank te = (TileEntityBatteryBank) worldIn.getTileEntity(pos);
        if (te != null) te.forceFaceCheck();
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }


    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState)
        {
            EnumFacing facing = state.getValue(FACING);
            IExtendedBlockState eState = (IExtendedBlockState) state;
            TileEntityBatteryBank te = (TileEntityBatteryBank) world.getTileEntity(pos);
            if (te != null) return eState.withProperty(SOUTH, te.isFacingOutput(facing.getOpposite()))
                    .withProperty(NORTH, te.isFacingOutput(facing))
                    .withProperty(EAST, te.isFacingOutput(facing.rotateY()))
                    .withProperty(WEST, te.isFacingOutput(facing.rotateYCCW()))
                    .withProperty(UP, te.isFacingOutput(EnumFacing.UP))
                    .withProperty(DOWN, te.isFacingOutput(EnumFacing.DOWN));
        }
        return state;
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public Class<TileEntityBatteryBank> getTileEntityClass() {
        return TileEntityBatteryBank.class;
    }

    @Nullable
    @Override
    public TileEntityBatteryBank createTileEntity(World world, IBlockState state) {
        return new TileEntityBatteryBank();
    }
}
