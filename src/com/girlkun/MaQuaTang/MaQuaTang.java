/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.MaQuaTang;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item.ItemOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class MaQuaTang {

    String code;
    int countLeft;
    public HashMap<Integer, Integer> detail = new HashMap<>();
    public ArrayList<Integer> listIdPlayer = new ArrayList<>();
    public ArrayList<ItemOption> option = new ArrayList<>();
    Timestamp datecreate;
    Timestamp dateexpired;

    public boolean isUsedGiftCode(int idPlayer) {
        boolean check = false;

        try {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement("SELECT `giftcodeuse` FROM `player` WHERE id = ?");
            ps.setInt(1, idPlayer);
            rs = ps.executeQuery();

            if (rs != null && rs.first()) {
                String cons = rs.getString("giftcodeuse");

                if (cons.contains(code + ",")) {
                    check = true;
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    public void addPlayerUsed(int idPlayer) throws Exception {
        String query = "UPDATE `player` SET giftcodeuse=(SELECT CONCAT (giftcodeuse, ?)) WHERE id=? LIMIT 1";
         GirlkunDB.executeUpdate(query,code+",",idPlayer);
    }

    public boolean timeCode() {
        return this.datecreate.getTime() > this.dateexpired.getTime() ? true : false;
    }
}
