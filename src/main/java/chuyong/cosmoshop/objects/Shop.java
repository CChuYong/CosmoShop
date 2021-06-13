package chuyong.cosmoshop.objects;

import chuyong.cosmoshop.CosmoShop;
import chuyong.cosmoshop.bill.BillingMethod;
import chuyong.cosmoshop.gui.GUIConstants;
import chuyong.cosmoshop.internal.MessageConstants;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.HashMap;


public class Shop {
    @Getter
    private String name;
    @Getter
    private HashMap<Integer, ShopItem> itemMap = new HashMap<>();
    private HashMap<Integer, Inventory> shopGUIMap = new HashMap<>();
    public Shop(String name){
        this.name = name;
    }
    public void pushItem(int slot, ShopItem item){
        this.itemMap.put(slot, item);
    }
    public void bake(){
        shopGUIMap.clear();
        int maxslot = Collections.max(itemMap.keySet());
        int pageCount = maxslot / 36 + 1;
        for(int i = 1; i<=pageCount; i++){
            int sep = i-1;
            Inventory inv = Bukkit.createInventory(null, 54, GUIConstants.SHOP_PREFIX + name);
            itemMap.forEach((slot, item)->{
                if(slot >= sep * 36 && slot < (sep+1) * 36){
                    int pos = slot % 36;
                    inv.setItem(pos, item.getSampleItem());
                }
            });
            GUIConstants.GUI_BUILDER.accept(inv, i);
            int finalI = i;
            CosmoShop.getListener().register(name, i, ev->{
                if(ev.getInventory()!= null && ev.getInventory().equals(inv)){
                    Player p = (Player) ev.getWhoClicked();
                    ev.setCancelled(true);
                    if(ev.getCurrentItem() == null)
                        return true;
                    if(ev.getCurrentItem().isSimilar(GUIConstants.RETURN_PAGE)){
                        openToPlayer(p, finalI - 1);
                        return true;
                    }
                    if(ev.getCurrentItem().isSimilar(GUIConstants.NEXT_PAGE)){
                        openToPlayer(p, finalI + 1);
                        return true;
                    }
                    for(ShopItem si : itemMap.values()){
                        if(si.getSampleItem().isSimilar(ev.getCurrentItem())){
                            BillingMethod.accept(p, si, ev.getClick());
                            return true;
                        }
                    }
                    return true;
                }
                return false;
            });
            shopGUIMap.put(i, inv);
        }
    }
    public void openToPlayer(Player player, int page){
        Inventory r = shopGUIMap.get(page);
        if(r == null){
            player.sendMessage(MessageConstants.PAGE_NOT_EXISTS.getMessageWithPrefix());
            return;
        }
        player.closeInventory();
        player.openInventory(r);
    }
}
