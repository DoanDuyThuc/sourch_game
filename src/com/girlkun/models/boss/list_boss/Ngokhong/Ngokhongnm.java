package com.girlkun.models.boss.list_boss.Ngokhong;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.Random;


public class Ngokhongnm extends Boss {

    public Ngokhongnm() throws Exception {
        super(BossID.NGOKHONGNM, BossesData.NGOKHONGNM);
    }
     @Override
     public void reward(Player plKill) {
            if (Util.isTrue(50, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1183, 3, this.location.x +10, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 14, this.location.x +30, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 13, this.location.x +40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1176, 3, this.location.x +50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1183, 3, this.location.x +60, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1176, 3, this.location.x-50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1183, 5, this.location.x-40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1176, 3, this.location.x-30, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1176, 5, this.location.x-20, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1183, 3, this.location.x-10, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1183, 5, this.location.x, this.location.y, plKill.id));
        
        }
    }
   @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if(Util.canDoWithTime(st,900000)){
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.parentBoss == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.NGOKHONGXD && boss.isDie()) {
                this.parentBoss.changeToTypePK();
                return;
            }
        }
        
    }
  
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
