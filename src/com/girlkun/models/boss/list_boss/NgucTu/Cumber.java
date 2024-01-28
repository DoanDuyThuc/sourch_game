/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss.NgucTu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 *
 * @Stole By Arriety
 */
public class Cumber extends Boss {

    public Cumber() throws Exception {
        super(BossID.CUMBER, BossesData.CUMBER);
    }

    @Override
   
       public void reward(Player plKill) {
    int id = Util.nextInt(0, 100);
    int[] rdfl = new int[]{555,557,559}; 
    int randomfl = new Random().nextInt(rdfl.length);
    //rdfl.itemOptions.add(new ItemOption(147, 3));
             if (Util.isTrue(35, 50)) {
                 
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 861, 10, this.location.x+20, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +30, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x+40, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +50, this.location.y, plKill.id));
              ///  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +60, this.location.y, plKill.id));
              ///  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +70, this.location.y, plKill.id));
             //   Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -30, this.location.y, plKill.id));
             ///   Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -40, this.location.y, plKill.id));
             //   Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -50, this.location.y, plKill.id));
             //   Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -60 , this.location.y, plKill.id));
              // Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 557, 1, this.location.x -80, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 558, 1, this.location.x-20, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.minion(zone, 1199, 1, this.location.x -130, this.location.y, plKill.id));
               // Service.gI().dropItemMap(this.zone, Util.minion(zone, 1199, 1, this.location.x -140, this.location.y, plKill.id));
             //   Service.gI().dropItemMap(this.zone, Util.minion(zone, 1199, 1, this.location.x -90, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.minion(zone, 1199, 1, this.location.x -160 , this.location.y, plKill.id));
              
                Service.gI().dropItemMap(this.zone, Util.aothantl(zone, rdfl[randomfl], 1, this.location.x -100, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.minion(zone, 1199, 1, this.location.x-120, this.location.y, plKill.id));
             
                
        } 
           
       
    }


@Override
public void leaveMap(){
    super.leaveMap();
    super.dispose();
    BossManager.gI().removeBoss(this);
}
 

}
