package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityChunkLoader;
import cassiokf.industrialrenewal.util.ChunkManagerCallback;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

public class BlockChunkLoader extends BlockBasicContainer<TileEntityChunkLoader>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool MASTER = PropertyBool.create("master");
    public static final PropertyBool WORKING = PropertyBool.create("working");

    public BlockChunkLoader(String name, CreativeTabs tab)
    {
        super(name, tab, Material.IRON);
    }

    private static void activateChunkLoader(World worldIn, BlockPos pos, EntityPlayer placer)
    {
        final ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IndustrialRenewal.instance, placer.getName(), worldIn, ForgeChunkManager.Type.NORMAL);

        if (ticket == null)
        {
            placer.sendStatusMessage(new TextComponentString("Could not request any more chunk loading tickets"), true);
            //Player has requested too many tickets. Forge will log an issue here.
            return;
        }

        //final IBlockState blockState = worldIn.getBlockState(pos);
        //if (blockState.getBlock() != BlockLibrary.weirding_gadget) return;

        final NBTTagCompound modData = ticket.getModData();
        modData.setTag("blockPosition", NBTUtil.createPosTag(pos));
        modData.setInteger("size", IRConfig.MainConfig.Main.chunkLoaderWidth);

        ChunkManagerCallback.activateTicket(worldIn, ticket);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(MASTER))
        {
            worldIn.setBlockState(pos.up(), state.withProperty(MASTER, false));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (!(placer instanceof EntityPlayer) || !state.getValue(MASTER))
        {
            return;
        }
        activateChunkLoader(worldIn, pos, (EntityPlayer) placer);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!state.getValue(MASTER)) return false;

        if (worldIn.isRemote)
        {
            return true;
        }

        final TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) worldIn.getTileEntity(pos);
        if (tileEntity == null) return false;

        boolean success = false;
        final Iterable<TileEntityChunkLoader> chainedGadgets = ChunkManagerCallback.getChainedGadgets(tileEntity);

        if ((tileEntity.isExpired() || !tileEntity.hasTicket(playerIn)))
        {
            for (final TileEntityChunkLoader chainedGadget : chainedGadgets)
            {
                activateChunkLoader(worldIn, chainedGadget.getPos(), playerIn);
            }

            success = true;
        }

        return success;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(MASTER))
        {
            if (IsLoader(worldIn, pos.up())) worldIn.setBlockToAir(pos.up());

            final TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) worldIn.getTileEntity(pos);
            if (tileEntity == null) return;
            tileEntity.expireAllTickets();
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
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntityChunkLoader te = (TileEntityChunkLoader) worldIn.getTileEntity(pos);
        return state.withProperty(WORKING, state.getValue(MASTER) && te != null && te.isActive());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER, WORKING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(MASTER, true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta)
    {
        int directionIndex = meta;
        if (meta > 3) directionIndex -= 4;
        boolean index = true;
        if (meta > 3) index = false;
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(directionIndex)).withProperty(MASTER, index);
    }

    @Override
    public int getMetaFromState(final IBlockState state)
    {
        int i = state.getValue(FACING).getHorizontalIndex();
        if (!state.getValue(MASTER)) i += 4;
        return i;
    }

    @Override
    @Deprecated
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        final TileEntityChunkLoader tileEntity = (TileEntityChunkLoader) worldIn.getTileEntity(pos);
        if (tileEntity == null) return false;

        tileEntity.receiveClientEvent(id, param);
        return true;
    }

    @Override
    public Class<TileEntityChunkLoader> getTileEntityClass()
    {
        return TileEntityChunkLoader.class;
    }

    @Nullable
    @Override
    public TileEntityChunkLoader createTileEntity(World world, IBlockState state)
    {
        return new TileEntityChunkLoader();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityChunkLoader();
    }
}
