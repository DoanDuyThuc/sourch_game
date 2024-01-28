
package com.kygui;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.result.GirlkunResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import static org.slf4j.MDC.clear;

public class ShopKyGuiManager {
    private static ShopKyGuiManager instance;
    public static ShopKyGuiManager gI() {
        if (instance == null) {
            instance = new ShopKyGuiManager();
        }
        return instance;
    }
    private boolean isSave;    
    
    public long lastTimeUpdate;
    
    public String[] tabName = {"Trang bị","Phụ kiện","Hỗ trợ","Linh tinh",""};
    
    public List<ItemKyGui> listItem = new ArrayList<>();
    
    public void clear() throws SQLException 
    {
        try (Connection con = GirlkunDB.getConnection()) {
            String insertQuery = "TRUNCATE shop_ky_gui";
            try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                ps.executeUpdate();
            } catch (SQLException e) 
            {
                 System.err.print("\nError at 4\n");
                e.printStackTrace();
            }
        }
    }
   
    public void save() throws InterruptedException
    {
    
         if(isSave)
        {
            return;
        }
        isSave = true;
        try {
            clear();
        } catch (SQLException ex) 
        {
             System.err.print("\nError at 5\n");
            java.util.logging.Logger.getLogger(ShopKyGuiManager.class.getName()).log(Level.SEVERE, null, ex);
        }        
        try (Connection con = GirlkunDB.getConnection();) {
            Statement s = con.createStatement();
            s.execute("TRUNCATE shop_ky_gui");
            for(ItemKyGui it : this.listItem){
                if(it != null){
                    s.execute(String.format("INSERT INTO `shop_ky_gui`(`id`, `player_id`, `tab`, `item_id`, `gold`, `gem`, `quantity`, `itemOption`, `isUpTop`, `isBuy`) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')"
                            , it.id,it.player_sell,it.tab,it.itemId,it.goldSell,it.gemSell,it.quantity,JSONValue.toJSONString(it.options).equals("null") ? "[]" : JSONValue.toJSONString(it.options),it.isUpTop,it.isBuy ? 1 : 0));
                }
            }
        }
        catch (Exception e)
        {
            System.err.print("\nError at 6\n");
            e.printStackTrace();
        }
         isSave = false;
    }
}