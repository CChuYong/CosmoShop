package chuyong.cosmoshop.bill;

import chuyong.cosmoshop.internal.MessageConstants;
import chuyong.cosmoshop.objects.ShopItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public enum BillingMethod {
    CASH(CashBilling.getSolution()), GOLD(GoldBilling.getSolution());
    @Getter
    private BillingSolution solution;
    BillingMethod(BillingSolution solution){
        this.solution = solution;
    }
    public static BillingMethod getBySolution(BillingSolution sol){
        for(BillingMethod m : BillingMethod.values()){
            if(m.getSolution().equals(sol))
                return m;
        }
        return null;
    }
    public static void accept(Player player, ShopItem item, ClickType type){
        if(type == ClickType.LEFT || type == ClickType.SHIFT_LEFT){
            //구매
            if(item.getBuyPrice() < 0){
                player.sendMessage(MessageConstants.ERROR_CANT_BUY.getMessageWithPrefix());
                return;
            }
            int amount = type==ClickType.LEFT?1:64;
            if(!checkSlotAcceptable(player.getInventory(), item.getTarget(), amount)){
                player.sendMessage(MessageConstants.ERROR_NOT_ENOUGH_SPACE.getMessageWithPrefix());
                return;
            }
            if(item.getBillingSolution().hasBalance(player, item.getBuyPrice() * amount)){
                int left = item.getBillingSolution().takeBalance(player, item.getBuyPrice() * amount);
                ItemStack stack = item.getOnPurchase();
                stack.setAmount(amount);
                player.getInventory().addItem(stack);
                player.sendMessage(String.format(MessageConstants.PURCHSE_SUCCESS.getMessageWithPrefix(), item.getBillingSolution().getActualName(), left,item.getBillingSolution().getActualName()));
            }else{
                player.sendMessage(String.format(MessageConstants.ERROR_NOT_ENOUGH_PAYMENT.getMessageWithPrefix(), item.getBillingSolution().getActualName()));
            }
        }else if(type == ClickType.RIGHT || type == ClickType.SHIFT_RIGHT){
            //판매
            if(item.getSellPrice() < 0){
                player.sendMessage(MessageConstants.ERROR_CANT_SELL.getMessageWithPrefix());
                return;
            }
            int amount = type==ClickType.RIGHT?1:64;
            if(!checkEnoughItem(player.getInventory(), item.getTarget(), amount)){
                player.sendMessage(MessageConstants.ERROR_CANT_SELL_NOT_ENOUGH_ITEM.getMessageWithPrefix());
                return;
            }
            takeItem(player.getInventory(), item.getTarget(), amount);
            int left = item.getBillingSolution().addBalance(player, amount * item.getSellPrice());
            player.sendMessage(String.format(MessageConstants.SELL_SUCCESS.getMessageWithPrefix(), item.getBillingSolution().getActualName(), left,item.getBillingSolution().getActualName()));
        }
    }
    private static boolean checkSlotAcceptable(Inventory inv, ItemStack stack, int amount){
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack v = inv.getItem(i);
            if(v != null && v.getType() != Material.AIR){
                if(v.isSimilar(stack) && v.getAmount() + amount <= 64){
                    return true;
                }
            }else{
                return true;
            }
        }
        return false;
    }
    private static boolean checkEnoughItem(Inventory inv, ItemStack stack, int amount){
        int curr = 0;
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack v = inv.getItem(i);
            if(v != null && v.getType() != Material.AIR) {
                if (v.isSimilar(stack)) {
                    curr += v.getAmount();
                }
            }
        }
        return curr >= amount;
    }
    private static void takeItem(Inventory inv, ItemStack stack, int amount){
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack v = inv.getItem(i);
            if(v != null && v.getType() != Material.AIR) {
                if (v.isSimilar(stack)) {
                    if(v.getAmount() > amount){
                        v.setAmount(v.getAmount()-amount);
                        return;
                    }else{
                        amount -= v.getAmount();
                        v.setAmount(0);
                    }
                }
            }
        }
    }
}
