package chuyong.cosmoshop.gui;

import api.cosmoage.global.util.DataPair;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import java.util.HashMap;
import java.util.function.Predicate;

public class GUIListener implements Listener {
    private static HashMap<DataPair<String, Integer>, Predicate<InventoryClickEvent>> cons = new HashMap<>();
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getInventory()!=null && event.getInventory().getName().startsWith(GUIConstants.SHOP_PREFIX)){
            cons.forEach((x,y)->{
                if(y.test(event))
                    return;
            });
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryMoveItemEvent event){
        if(event.getSource()!=null && event.getSource().getName().startsWith(GUIConstants.SHOP_PREFIX)){
            event.setCancelled(true);
        }
        else if(event.getDestination()!=null && event.getDestination().getName().startsWith(GUIConstants.SHOP_PREFIX)){
            event.setCancelled(true);
        }
    }
    public void register(String shopName, int page, Predicate<InventoryClickEvent> ev){
        DataPair<String, Integer> dp = new DataPair<>(shopName, page);
        cons.put(dp, ev);
    }
}
