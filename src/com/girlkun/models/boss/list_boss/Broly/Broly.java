package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;


public class Broly extends Boss {

    public Broly() throws Exception {
        super(BossID.BROLY , BossesData.BROLY_1,BossesData.BROLY_2, BossesData.BROLY_3);
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
                Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 561, 1, this.location.x -80, this.location.y, plKill.id));
                Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 562, 1, this.location.x-20, this.location.y, plKill.id));
                
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
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}





















