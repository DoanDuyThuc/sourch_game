package com.girlkun.models.boss.list_boss;

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

public class Baibien extends Boss {

    public Baibien() throws Exception {
        super(Util.randomBossId(), BossesData.BAIBIEN,BossesData.BAIBIEN);
    }

    @Override
    public void reward(Player plKill) {
            if (Util.isTrue(50, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 861, 10, this.location.x+20, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +30, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x+40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1164, 1, this.location.x +50, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +60, this.location.y, plKill.id));
               // Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +70, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1164, 1, this.location.x -30, this.location.y, plKill.id));
             //   Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1164, 1, this.location.x -50, this.location.y, plKill.id));
              //  Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -60 , this.location.y, plKill.id));
                //Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -80, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.baibien(zone, 1164, 1, this.location.x-20, this.location.y, plKill.id));
                
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