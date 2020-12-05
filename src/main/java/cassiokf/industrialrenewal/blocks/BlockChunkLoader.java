package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.abstracts.BlockHorizontalFacing;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.handlers.ChunkManagerCallback;
import cassiokf.industrialrenewal.tileentity.TileEntityChunkLoader;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import javax.annotation.Nullable;

public class BlockChunkLoader extends BlockHorizontalFacing
{
    public static final PropertyBool MASTER = PropertyBool.create("master");
    public static final PropertyBool WORKING = PropertyBool.create("working");

    public BlockChunkLoader(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    private static void activateChunkLoader(World worldIn, BlockPos pos, PlayerEntity placer)
    {
        final ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IndustrialRenewal.instance, placer.getName(), worldIn, ForgeChunkManager.Type.NORMAL);

        if (ticket == null)
        {
            placer.sendStatusMessage(new TextComponentString("Could not request any more chunk loading tickets"), true);
            return;
        }

        final CompoundNBT modData = ticket.getModData();
        modData.setTag("blockPosition", NBTUtil.createPosTag(pos));
        modData. putInt("size", IRConfig.MainConfig.Main.chunkLoaderWidth);

        ChunkManagerCallback.activateTicket(worldIn, ticket);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos,  BlockState state)
    {
        if (state.getValue(MASTER))
        {
            worldIn.setBlockState(pos.up(), state.withProperty(MASTER, false));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos,  BlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (!(placer instanceof PlayerEntity) || !state.getValue(MASTER) || worldIn.isRemote)
        {
            return;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityChunkLoader) ((TileEntityChunkLoader) te).setMaster(true);
        activateChunkLoader(worldIn, pos, (PlayerEntity) placer);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,  BlockState state, PlayerEntity playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!state.getValue(MASTER)) return false;

        if (worldIn.isRemote)
        {
            return true;
        }

        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityChunkLoader)) return false;

        boolean success = false;

        if ((((TileEntityChunkLoader) tileEntity).isExpired() || !((TileEntityChunkLoader) tileEntity).hasTicket(playerIn)))
        {
            activateChunkLoader(worldIn, tileEntity.getPos(), playerIn);
            success = true;
        }

        return success;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos,  BlockState state)
    {
        if (state.getValue(MASTER))
        {
            if (IsLoader(worldIn, pos.up())) worldIn.setBlockToAir(pos.up());

            final TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityChunkLoader)
                ((TileEntityChunkLoader) tileEntity).expireAllTickets();
        } else
        {
            if (IsLoader(worldIn, pos.down()))
                worldIn.setBlockToAir(pos.down());
        }
        super.breakBlock(worldIn, pos, state);
    }

    private boolean IsLoader(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockChunkLoader;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
                && worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up());
    }

    @Override
    public  BlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        return state.withProperty(WORKING, state.getValue(MASTER) && te instanceof TileEntityChunkLoader && ((TileEntityChunkLoader) te).isActive());
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER, WORKING);
    }

    @Override
    public  BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(MASTER, true);
    }

    @Override
    public  BlockState getStateFromMeta(final int meta)
    {
        int directionIndex = meta;
        if (meta > 3) directionIndex -= 4;
        boolean index = true;
        if (meta > 3) index = false;
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(directionIndex)).withProperty(MASTER, index);
    }

    @Override
    public int getMetaFromState(final  BlockState state)
    {
        int i = state.getValue(FACING).getHorizontalIndex();
        if (!state.getValue(MASTER)) i += 4;
        return i;
    }

    @Override
    @Deprecated
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityChunkLoader)) return false;

        tileEntity.receiveClientEvent(id, param);
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityChunkLoader createTileEntity(World world,  BlockState state)
    {
        return new TileEntityChunkLoader();
    }
}
