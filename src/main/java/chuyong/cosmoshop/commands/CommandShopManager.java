package chuyong.cosmoshop.commands;

import api.cosmoage.global.commandannotation.CosmoCMD;
import chuyong.cosmoshop.CosmoShop;
import chuyong.cosmoshop.bill.BillingSolution;
import chuyong.cosmoshop.bill.CashBilling;
import chuyong.cosmoshop.bill.GoldBilling;
import chuyong.cosmoshop.objects.Shop;
import chuyong.cosmoshop.objects.ShopItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandShopManager {
    @CosmoCMD(parent="상점관리", op=true)
    public void onNoArg(CommandSender sender, String[] args){
        sender.sendMessage("/상점관리 저장 - 모든 상점을 SQL에 올립니다.");
        sender.sendMessage("/상점관리 불러오기 - 모든 상점을 SQL에서 내립니다.");
        sender.sendMessage("*** 상점을 자동으로 저장해주지 않습니다. 설정을 마친후 반드시 /상점관리 저장 으로 저장해주세요.");
        sender.sendMessage("/상점관리 등록 [상점이름] [페이지] [슬롯번호(0~35)] [골드/캐시] [판매가(불가시 음수)] [구매가(불가시 음수)]");
    }
    @CosmoCMD(parent="상점관리", child = "등록", op=true)
    public void onReg(CommandSender sender, String[] args){
        if(args.length != 7){
            sender.sendMessage("/상점관리 등록 [상점이름] [페이지] [슬롯번호(0~35)] [골드/캐시] [판매가(불가시 음수)] [구매가(불가시 음수)]");
            return;
        }
        if(!(args[4].equals("골드") || args[4].equals("캐시"))){
            sender.sendMessage("4번쨰 Argument는 골드 혹은 캐시여야 합니다.");
            return;
        }
        Player player = (Player) sender;
        if(player.getItemInHand() == null){
            sender.sendMessage("손에 아이템을 들고 이용해주세요.");
            return;
        }
        Shop s = CosmoShop.push(args[1]);
        int page = Integer.parseInt(args[2]);
        int slot = Integer.parseInt(args[3]);

        BillingSolution sol;
        if(args[4].equals("골드")){
            sol = GoldBilling.getSolution();
        }else{
            sol = CashBilling.getSolution();
        }

        int sell = Integer.parseInt(args[5]);
        int buy = Integer.parseInt(args[6]);

        int actualSlot = (page-1) * 36 + slot;
        ItemStack act = player.getItemInHand().clone();
        act.setAmount(1);
        ShopItem it = new ShopItem(act, sell, buy, sol);
        s.pushItem(actualSlot, it);
        s.bake();
        sender.sendMessage("등록 성공.");
    }
    @CosmoCMD(parent="상점관리", child = "저장", op=true)
    public void onSave(CommandSender sender, String[] args){
        CosmoShop.getSqlHandler().saveAll();
        sender.sendMessage("상점 저장에 성공했습니다.");
    }
    @CosmoCMD(parent="상점관리", child = "불러오기", op=true)
    public void onLoad(CommandSender sender, String[] args){
        CosmoShop.getSqlHandler().readAll();
        sender.sendMessage("상점 불러오기에 성공했습니다.");
    }
}
