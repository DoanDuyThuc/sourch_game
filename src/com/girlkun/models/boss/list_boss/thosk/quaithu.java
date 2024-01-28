
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


/**
 *
 * @author Administrator
 */
public class quaithu extends Boss {

    public quaithu() throws Exception {
        super(Util.randomBossId(), BossesData.QUAITHU,BossesData.QUAITHU);
    }

    @Override
    public void reward(Player plKill) {
            if (Util.isTrue(50, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1994, 1, this.location.x+120, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1994, 1, this.location.x +30, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1995, 3, this.location.x+140, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1995, 1, this.location.x +50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +60, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +70, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -30, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 861, 1, this.location.x -50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -60 , this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -80, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1994, 1, this.location.x-120, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 17, 1, this.location.x -140, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 18, 1, this.location.x -150, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 19, 1, this.location.x -160 , this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 18, 1, this.location.x -180, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 17, 1, this.location.x-120, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1992, 1, this.location.x +160 , this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1992, 1, this.location.x +180, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1992, 1, this.location.x+130, this.location.y, plKill.id));
                
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