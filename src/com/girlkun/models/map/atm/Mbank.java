package com.girlkun.models.map.atm;
//import com.girlkun.models.boss.bdkb.TrungUyXanhLo;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.TrapMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
  @author huy 

 *
 */
public class Mbank { 

    public static final long POWER_CAN_GO_TO_MBANK = 2000000000;

    public static final List<Mbank> MBANK;
    public static final int MAX_AVAILABLE = 50;
    public static final int TIME_MBANK = 1800000;

    private Player player;

    static {
        MBANK = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            MBANK.add(new Mbank(i));
        }
    }

    public int id;
    public int level;
    public final List<Zone> zones;

    public Clan clan;
    public boolean isOpened;
    private long lastTimeOpen;

    public Mbank(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    public void update() {
        if (this.isOpened) {
            if (Util.canDoWithTime(lastTimeOpen, TIME_MBANK)) {
                this.finish();
            }
        }
    }

    public void openMbank(Player plOpen, Clan clan, int level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
 //       this.clan.timeOpenMbank = this.lastTimeOpen;
  //      this.clan.playerOpenMbank = plOpen;
  //      this.clan.Mbank = this;

        resetMbank();
        ChangeMapService.gI().goToGas(plOpen);
        sendTextGas();
    }

    private void resetMbank() {
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                Mob.initMopbMbank(m, this.level);
                Mob.hoiSinhMob(m);
            }
        }
    }


    public void finish() {
        List<Player> plOutDT = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            for (Player pl : players) {
                plOutDT.add(pl);
            }

        }
        for (Player pl : plOutDT) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 0, -1, 384);
        }
  //      this.clan.Mbank = null;
        this.clan = null;
        this.isOpened = false;
    }


    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public static void addZone(int idGas, Zone zone) {
        MBANK.get(idGas).zones.add(zone);
    }

    private void sendTextGas() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextGas(pl);
        }
    }
}
