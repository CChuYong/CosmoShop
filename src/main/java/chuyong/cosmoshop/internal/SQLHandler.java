package chuyong.cosmoshop.internal;

import api.cosmoage.bukkit.item.ItemStackSerializer;
import auctions.db.sql.ConnectionPoolManager;
import chuyong.cosmoshop.CosmoShop;
import chuyong.cosmoshop.bill.BillingMethod;
import chuyong.cosmoshop.objects.Shop;
import chuyong.cosmoshop.objects.ShopItem;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLHandler {
    private ConnectionPoolManager pool;
    private String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS cosmo_shop(shopname VARCHAR(64) PRIMARY KEY, billtype VARCHAR(36), slot INT, item BLOB, sell INT, buy INT)";
    private String SELECT_ALL = "SELECT * FROM cosmo_shop";
    private String DELETE_SHOP = "DELETE FROM cosmo_shop WHERE shopname=?";
    private String TRUNCATE_TABLE = "TRUNCATE TABLE cosmo_shop";
    private String REPLACE_ITEM = "REPLACE cosmo_shop(shopname, billtype, slot, item, sell, buy) VALUES(?, ?, ?, ?, ?, ?)";
    public SQLHandler(ConnectionPoolManager manager){
        this.pool = manager;
        try(Connection s = getConnection(); PreparedStatement stmt = s.prepareStatement(CREATE_TABLE_QUERY)){
            stmt.executeUpdate();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        readAll();
    }
    private Connection getConnection() throws Exception{
        return pool.getConnection();
    }
    public void deleteShop(String shopName){
        try(Connection s = getConnection(); PreparedStatement stmt = s.prepareStatement(DELETE_SHOP)){
            stmt.setString(1, shopName);
            stmt.executeUpdate();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void truncateTable(){
        try(Connection s = getConnection(); PreparedStatement stmt = s.prepareStatement(TRUNCATE_TABLE)){
            stmt.executeUpdate();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void readAll(){
        System.out.println("[CosmoShop] Shop을 SQL로부터 불러오는 중입니다..");
        CosmoShop.flush();
        try(Connection s = getConnection(); PreparedStatement stmt = s.prepareStatement(SELECT_ALL)){
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    String shopName = rs.getString("shopname");
                    String billtype = rs.getString("billtype");
                    int slotid = rs.getInt("slot");
                    byte[] arr = rs.getBytes("item");
                    ItemStack item = ItemStackSerializer.fromByteArray(arr);
                    int sellPrice = rs.getInt("sell");
                    int buyPrice = rs.getInt("buy");
                    Shop sh = CosmoShop.push(shopName);
                    ShopItem it = new ShopItem(item, sellPrice, buyPrice, BillingMethod.valueOf(billtype).getSolution());
                    sh.pushItem(slotid, it);
                }
            }
            CosmoShop.bakeAll();
            System.out.println("[CosmoShop] Shop 데이터 로드 완료");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void saveAll(){
        System.out.println("[CosmoShop] Shop을 SQL로 저장하는 중입니다..");
        truncateTable();
        for(Shop sh : CosmoShop.getShops()){
            sh.getItemMap().forEach((slot, item)->{
                try(Connection s = getConnection(); PreparedStatement stmt = s.prepareStatement(REPLACE_ITEM)){
                    stmt.setString(1, sh.getName());
                    stmt.setString(2, BillingMethod.getBySolution(item.getBillingSolution()).toString());
                    stmt.setInt(3, slot);
                    stmt.setBytes(4, ItemStackSerializer.toByteArray(item.getTarget()));
                    stmt.setInt(5, item.getSellPrice());
                    stmt.setInt(6, item.getBuyPrice());
                    stmt.executeUpdate();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            });
        }
        System.out.println("[CosmoShop] Shop 데이터 저장 완료");
    }

}
