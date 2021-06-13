package chuyong.cosmoshop;

import api.cosmoage.bukkit.commands.$BukkitCommandHandler;
import auctions.db.AuctionDB;
import chuyong.cosmoshop.commands.CommandShop;
import chuyong.cosmoshop.commands.CommandShopManager;
import chuyong.cosmoshop.gui.GUIListener;
import chuyong.cosmoshop.internal.SQLHandler;
import chuyong.cosmoshop.objects.Shop;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;

public class CosmoShop extends JavaPlugin {
    private static HashMap<String, Shop> shopMap = new HashMap<>();
    private static Economy econ = null;
    @Getter
    private static CosmoShop instance;
    @Getter
    private static GUIListener listener = new GUIListener();
    @Getter
    private static SQLHandler sqlHandler;
    @Override
    public void onEnable(){
        instance = this;
        getServer().getPluginManager().registerEvents(listener, this);
        setupEconomy();
        sqlHandler = new SQLHandler(AuctionDB.getPoolManager());
        try{
            $BukkitCommandHandler.registerCommands(this, new CommandShopManager());
            $BukkitCommandHandler.registerCommands(this, new CommandShop());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public static Shop getByName(String shopName){
        return shopMap.get(shopName);
    }
    public static void flush(){
        shopMap.clear();
    }

    public static Shop push(String sh){
        return shopMap.computeIfAbsent(sh, x->new Shop(sh));
    }
    public static Economy getEconomy(){
        return econ;
    }
    public static void bakeAll(){
        shopMap.values().forEach(x->x.bake());
    }
    public static Collection<Shop> getShops(){
        return shopMap.values();
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
