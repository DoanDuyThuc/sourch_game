/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.models.boss.list_boss.thosk;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;


/**
 *
 * @author Administrator
 */
public class tho3 extends Boss {

    public tho3() throws Exception {
        super(Util.randomBossId(), BossesData.THO3,BossesData.THO3);
    }

    @Override
     public void reward(Player plKill) {
         int[] itemDos = new int[]{1996,1991};
         int randomDo = new Random().nextInt(itemDos.length);
            if (Util.isTrue(30, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1991, 3, this.location.x+20, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1991, 1, this.location.x +30, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x+40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1991, 1, this.location.x +50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +60, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +70, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1991, 10, this.location.x -130, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1991, 1, this.location.x -50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -60 , this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -80, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1991, 1, this.location.x-120, this.location.y, plKill.id));
                 } else  if (Util.isTrue(30, 100)){
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x+140, this.location.y, plKill.id));

        }
    }




    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 300000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
  
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
}