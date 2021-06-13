package chuyong.cosmoshop.objects;

import api.cosmoage.bukkit.item.ItemStackBuilder;
import chuyong.cosmoshop.bill.BillingSolution;
import chuyong.cosmoshop.gui.GUIConstants;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ShopItem {
    private ItemStack target;
    private int sellPrice;
    private int buyPrice;
    private ItemStack sample;
    private BillingSolution billingSolution;
    public ShopItem(ItemStack original, int sell, int buy, BillingSolution solution){
        this.target = original;
        this.sellPrice = sell;
        this.buyPrice = buy;
        this.billingSolution = solution;
        ItemStackBuilder builder = new ItemStackBuilder(original);
        if(original.getItemMeta().hasDisplayName())
            builder.displayname(original.getItemMeta().getDisplayName());
        builder.addLore("&f")
                .addLore("§6구매 §f가격 : " + (buyPrice > 0 ? buyPrice+" " + billingSolution.getActualName(): "구매 불가"))
                .addLore("§a판매 §f가격 : " + (sellPrice > 0 ? sellPrice+" " + billingSolution.getActualName(): "판매 불가"));
        GUIConstants.FOOTER_BUILDER.accept(builder);
        this.sample = builder.build();
    }
    public ItemStack getSampleItem(){
        return sample;
    }
    public ItemStack getOnPurchase(){
        return target.clone();
    }
}
