package chuyong.cosmoshop.bill;

import org.bukkit.entity.Player;

public class CashBilling implements BillingSolution{
    private static final String name = "다이아";
    @Override
    public boolean hasBalance(Player player, int balance) {
        return false;
    }

    @Override
    public int takeBalance(Player player, int balance) {
        return 0;
    }

    @Override
    public int addBalance(Player player, int balance) {
        return 0;
    }

    @Override
    public String getActualName() {
        return name;
    }

    private static final CashBilling inst = new CashBilling();
    private CashBilling(){}
    public static CashBilling getSolution(){
        return inst;
    }
}
