package chuyong.cosmoshop.bill;

import chuyong.cosmoshop.CosmoShop;
import org.bukkit.entity.Player;

public class GoldBilling implements BillingSolution{
    private static final String name = "골드";
    @Override
    public boolean hasBalance(Player player, int balance) {
        return CosmoShop.getEconomy().getBalance(player) >= balance;
    }

    @Override
    public int takeBalance(Player player, int balance) {
        CosmoShop.getEconomy().withdrawPlayer(player, balance);
        return (int) CosmoShop.getEconomy().getBalance(player);
    }

    @Override
    public int addBalance(Player player, int balance) {
        CosmoShop.getEconomy().depositPlayer(player, balance);
        return (int) CosmoShop.getEconomy().getBalance(player);
    }

    @Override
    public String getActualName() {
        return name;
    }

    private static final GoldBilling inst = new GoldBilling();
    private GoldBilling(){}
    public static GoldBilling getSolution(){
        return inst;
    }
}
