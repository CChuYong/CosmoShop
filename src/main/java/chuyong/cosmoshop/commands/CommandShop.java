package chuyong.cosmoshop.commands;

import api.cosmoage.global.commandannotation.CosmoCMD;
import chuyong.cosmoshop.CosmoShop;
import chuyong.cosmoshop.internal.MessageConstants;
import chuyong.cosmoshop.objects.Shop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShop {
    @CosmoCMD(parent="상점")
    public void onNoArg(CommandSender sender, String[] args){
        if(args.length == 0){
            sender.sendMessage("/상점 [상점명]");
        }else{
            Shop s = CosmoShop.getByName(args[0]);
            if(s == null){
                sender.sendMessage(MessageConstants.UNKNOWN_SHOP.getMessageWithPrefix());
                return;
            }
            Player player = (Player) sender;
            s.openToPlayer(player, 1);
        }
    }
}
