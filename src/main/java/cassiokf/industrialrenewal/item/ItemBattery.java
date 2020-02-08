package cassiokf.industrialrenewal.item;

import net.minecraft.item.Item;

public class ItemBattery extends ItemBase
{
    public ItemBattery(Item.Properties properties)
    {
        super(properties);
        //this.container = new VoltsEnergyContainer(IRConfig.MainConfig.Main.batteryBankCapacity / 5, IRConfig.MainConfig.Main.batteryBankMaxInput / 5, IRConfig.MainConfig.Main.batteryBankMaxOutput / 5);
    }
/*
    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        list.add(new StringTextComponent("Energy: " + this.container.getEnergyStored() + " / " + this.container.getMaxEnergyStored()));
    }*/
}
