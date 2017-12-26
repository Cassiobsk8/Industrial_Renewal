package cassiokf.industrialrenewal.item;

public class ItemMineCartCargoContainer extends ItemBase {

    public int minecartType;

    public ItemMineCartCargoContainer(String name) {
        super(name);

        this.maxStackSize = 16;
        this.minecartType = -1;

    }
/*
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = world.getBlockState(pos);

        if (!BlockRailBase.isRailBlock(iblockstate))
            return EnumActionResult.FAIL;

        else {
            if (!world.isRemote) {

                pos.offset(EnumFacing.EAST, (int) 0.5);

                ItemStack itemstack = player.getHeldItem(hand);

                TileEntityCartCargoContainer entityminecart = new TileEntityCartCargoContainer(world);
                if (itemstack.hasDisplayName()) {
                    entityminecart.setCustomNameTag(itemstack.getDisplayName());
                }
                world.spawnEntity(entityminecart);
                if (entityminecart != null) {
                    itemstack.shrink(1);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }*/

}
