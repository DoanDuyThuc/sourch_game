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

public class Gojo extends Boss {

    public Gojo() throws Exception {
        super(Util.randomBossId(), BossesData.GOJO,BossesData.GOJO);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{2027,2028};
        int[] itemtime = new int[]{381,382,383,384,385};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomitem = new Random().nextInt(itemtime.length);
        if (Util.isTrue(100, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, 2027, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemtime[randomitem], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
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
