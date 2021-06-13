package chuyong.cosmoshop.gui;

import api.cosmoage.bukkit.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GUIConstants {
    public final static ItemStack EMPTY_PANE = new ItemStackBuilder(Material.STAINED_GLASS_PANE).displayname("&f").build();
    public final static ItemStack RETURN_PAGE = new ItemStackBuilder(Material.PAPER).displayname("§f[ §c◀ §f]").addLore("§f클릭 시 §c이전 §f페이지로 이동합니다.").build();
    public final static ItemStack NEXT_PAGE = new ItemStackBuilder(Material.PAPER).displayname("§f[ §a▶ §f]").addLore("§f클릭 시 §a다음 §f페이지로 이동합니다.").build();
    public final static String SHOP_PREFIX = "§f[ §6상점 §f] ";
    public final static BiConsumer<Inventory, Integer> GUI_BUILDER = (inv, page)->{
        for(int i = 36; i < 45; i++)
            inv.setItem(i, EMPTY_PANE);
        inv.setItem(45, RETURN_PAGE);
        inv.setItem(49, new ItemStackBuilder(Material.BOOK).displayname("§f[ §6현재 페이지 §f]").addLore(String.format("§f현재 페이지 : §6%d", page)).build());
        inv.setItem(53, NEXT_PAGE);
    };
    public final static Consumer<ItemStackBuilder> FOOTER_BUILDER = (builder)->{
        builder.addLore("&f");
        builder.addLore("           §f< §e이용방법 §f>")
                .addLore("&f")
                .addLore("§6좌클릭            §7>     §f1 개 §6구매")
                .addLore("§a우클릭            §7>     §f1 개 §a판매")
                .addLore("§f쉬프트 + §6좌클릭  §7>     §f64 개 §6구매")
                .addLore("§f쉬프트 + §a우클릭  §7>     §f64 개 §a판매");
    };
}
