package chuyong.cosmoshop.bill;

import org.bukkit.entity.Player;

public interface BillingSolution {
    boolean hasBalance(Player player, int balance);
    int takeBalance(Player player, int balance);
    int addBalance(Player player, int balance);
    String getActualName();
}
