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
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 *
 *@Stole By Arriety
 */
public class SongokuTaAc extends Boss {

    public SongokuTaAc() throws Exception {
        super(BossID.SONGOKU_TA_AC, BossesData.SONGOKU_TA_AC);
    }

    @Override
    public void reward(Player plKill) {
             if (Util.isTrue(35, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 861, 10, this.location.x+20, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +30, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x+40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +60, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x +70, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -30, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -40, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -50, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 457, 1, this.location.x -60 , this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 559, 1, this.location.x -80, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 560, 1, this.location.x-20, this.location.y, plKill.id));
                
        }
    }


@Override
public void leaveMap(){
    super.leaveMap();
    super.dispose();
    BossManager.gI().removeBoss(this);
}

}
