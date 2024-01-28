package com.girlkun.models.npc;

import com.girlkun.MaQuaTang.MaQuaTangManager;
import com.girlkun.consts.ConstMap;
import com.girlkun.services.*;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.data.DataGame;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.models.boss.list_boss.DuongTank;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;

import java.util.HashMap;
import java.util.List;

import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.SummonDragon;

import static com.girlkun.services.func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static com.girlkun.services.func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static com.girlkun.services.func.SummonDragon.SHENRON_SAY;

import com.girlkun.models.player.Player;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.challenge.MartialCongressService;
import com.girlkun.models.map.doanhtrai.DoanhTrai;
import com.girlkun.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.matches.PVPService;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.server.Manager;
import com.girlkun.services.func.CombineServiceNew;
import com.girlkun.services.func.Input;
import com.girlkun.services.func.LuckyRound;
import com.girlkun.services.func.TopService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import com.girlkun.services.func.ChonAiDay;
import com.kygui.ItemKyGui;
import com.kygui.ShopKyGuiService;
import java.util.Timer;
import java.util.TimerTask;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.girlkun.models.map.BDKB.BanDoKhoBauService;
import com.girlkun.models.mob.Mob;
import com.girlkun.services.func.UseItem;

import java.util.logging.Level;
//import static sun.audio.AudioPlayer.player;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    private static Npc sukien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào Mừng Bạn đến Ngọc Rồng wibu", "Shop Đá \nGerry", "Shop", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "shop_sukien", false);
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", false);
                                    break;
                                case 5: {
                                    Item honLinhThu = null;
                                    Item voc = null;
                                    Item concua = null;
                                    Item tv = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 695);
                                        voc = InventoryServiceNew.gI().findItemBag(player, 696);
                                        concua = InventoryServiceNew.gI().findItemBag(player, 697);
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 vỏ ốc");
                                    } else if (voc == null || voc.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 vỏ sò");
                                    } else if (concua == null || concua.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 con cua");
                                    } else if (tv == null || tv.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, voc, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, concua, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 99);
                                        Service.gI().sendMoney(player);

                                        Item dns = ItemService.gI().createNewItem((short) 674);
                                        dns.quantity = 10;
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        player.inventory.event += 10;
                                        Service.gI().sendThongBao(player, "|2|Bạn Nhận được 10 đá ngũ sắc,10 điểm sự kiện");
                                    }
                                    break;

                                }
                            }
                        }
                    }
                }
            }
        };
    }
     private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
                        } else {
                             this.createOtherMenu(player, ConstNpc.BASE_MENU, "Garden SSJ hắn chộm đá quý ngàn năm của ta \n"
                                     + "Hắn đang ẩn nấp nơi Vùng Đất thám hiểm ,Hãy giúp ta tiêu diệt hắn \nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?\n\n|5| Tiêu diệt boss có cơ hội nhận được danh hiệu VIP 10k tấn công ...","Thông Tin","Vâng","Từ Chối");
                       }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 1:
                                if (player.clan != null) {
                                    if (player.clan.khiGas != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
                                                "Bang hội của con đang đi Vùng đất thám hiểm cấp độ "
                                                        + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
                                                "Vùng Đất thám hiểm nhiều vàng bạc châu báu\n"
                                                        + "các con hãy giúp chúng ta tiêu diệt quái vật \n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    ChangeMapService.gI().goToGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    Input.gI().createFormChooseLevelGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
                        switch (select) {
                            case 0:
                                GasService.gI().openBanDoKhoBau(player, Integer.parseInt(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }
     private static Npc chihang(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Sự Kiện trung thu đang diễn ra hãy thu thập vật Phẩm sự Kiện Mang đến đây Thắp đèn để nhận những phần quà hấp dẫn"
                                + "\n Tiêu diệt boss thỏ trắng , quái thú mang đánh lửa, diêm ,lồng đèn \n "
                                + "x99 đánh lửa + x10 diêm + x10 Lồng đèn + x 999 Tv = 10 Hộp quà", "Thắp Đèn", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                
                                case 0: {
                                    Item diem = null;
                                    Item danhlua = null;
                                    Item longden = null;
                                    Item tv = null;
                                    try {
                                        diem = InventoryServiceNew.gI().findItemBag(player, 1994);
                                        danhlua = InventoryServiceNew.gI().findItemBag(player, 1991);
                                        longden = InventoryServiceNew.gI().findItemBag(player, 1995);
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (diem == null || diem.quantity < 10) {
                                        this.npcChat(player, "Bạn không đủ diêm");
                                    } else if (danhlua == null || danhlua.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ đánh lửa");
                                    } else if (longden == null || longden.quantity < 10) {
                                        this.npcChat(player, "Bạn không đủ lồng đèn");
                                    } else if (tv == null || tv.quantity < 999) {
                                        this.npcChat(player, "Bạn không đủ 999 thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, diem, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, danhlua, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, longden, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 999);
                                        Service.gI().sendMoney(player);
                                        Item hopqua = ItemService.gI().createNewItem((short) 1990);
                                        hopqua.quantity  = 10 ;
                                        InventoryServiceNew.gI().addItemBag(player, hopqua);
                                       Service.getInstance().sendThongBaoOK(player, "Bạn Nhận được 10 hộp quà sử dụng item bất kì để update đồ!!");
                                         Service.gI().sendThongBaoAllPlayer(player.name + "  Đã Thắp lồng đèn Tại đảo kame mau mau  tới xem ");
                                      }
                                    break;

                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú +1 tỷ vàng", "Đổi Trứng\nLinh thú", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {

                        case 0:
                            Service.getInstance().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:

                            ShopKyGuiService.gI().openShopKyGui(pl);
                            break;

                    }
                }
            }
        };
    }
  private static Npc nangde(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "|1|Chào bạn , hãy mang đệ Tử đến đây ta sẽ Giúp ngươi nâng cấp\n Đương nhiên lệ phí cũng không hề thấp\nSau khi nâng cấp đệ tử ngươi tăng chỉ số khá mạnh đó " ," Nâng cấp","Shop \n Nhiên Liệu");
                }
            }

            @Override
           public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                             Service.getInstance().sendThongBaoOK(player, "HUYHAU AIR SẮP CODE XONG RỒI");
                                break;
                          case 1:
                             Service.getInstance().sendThongBaoOK(player, "HUYHAU AIR SẮP CODE XONG RỒI");
                                break;
                            }
                }
            }
        };
    }
    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 1000 ngọc không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.ruby < 100000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 100K ngọc HỒNG ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.idAura, player.getEffFront()}, (int) player.nPoint.dame,
                                                new long[]{player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.ruby -= 100000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

//            public void Npcchat(Player player) {
//                String[] chat = {
//                    "|5|Chào Mừng đến với WIBU",
//                    "|2|sever Hoàn toàn miễn phí ",
//                    "|7|CODE BY KAMI"
//                };
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int index = 0;
//
//                    @Override
//                    public void run() {
//                        npcChat(player, chat[index]);
//                        index = (index + 1) % chat.length;
//                    }
//                }, 10000, 10000);
//            }
            @Override
            public void openBaseMenu(Player player) {
              //  Npcchat(player);
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Lãnh địa Bang Hội", "Kho báu dưới biển",(player.titleitem == false ? "Bật" : "Tắt") + " Danh Hiệu Túi",
                                    (player.titlett == false ? "Bật" : "Tắt") + " Danh Hiệu Tu Tiên",
                                    "Danh Hiệu");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
//                                        
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Yes you do!", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 800L) {

                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else {
                                    this.npcChat(player, "");
                                }
                                break; // qua lanh dia
                            case 2:
                                if (player.clan != null) {
                                    if (player.clan.BanDoKhoBau != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "|7|Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                + player.clan.BanDoKhoBau.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                "|1|Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                                case 3:
                                player.titleitem = !player.titleitem;
                                Service.gI().removeTitle(player);
                                Service.gI().sendThongBao(player, "Đã " + (player.titleitem == true ? "Bật" : "Tắt") + " Danh Hiệu Túi!");
                                break;
                            case 4:
                                player.titlett = !player.titlett;
                                Service.gI().sendThongBao(player, "Đã " + (player.titleitem == true ? "Bật" : "Tắt") + " Danh Hiệu Tu Tiên!");
                                break;
                            case 5:
                                    this.createOtherMenu(player, 888,
                                        "Đây là danh hiệu mà ngươi có"
                                        + (player.lastTimeTitle1 > 0 ? "\n DH Tân thủ.SD,HP,KI +10 %" : "")+ (player.lastTimeTitle2 > 0 ? "\n Tuổi ther. hp,sd,ki + 10%" : "")+ (player.lastTimeTitle3 > 0 ? "\n Thiên Tử. hp,sd,ki +20%" : ""),
                                        (player.lastTimeTitle1 > 0 ? ("Danh hiệu \n Tân Thủ: " + (player.isTitleUse == true ? "On" : "Off") + "\n" + Util.msToTime(player.lastTimeTitle1)) : "Hết hạn"),
                                        (player.lastTimeTitle2 > 0 ? ("Danh hiệu \n 2: " + (player.isTitleUse2 == true ? "On" : "Off") + "\n" + Util.msToTime(player.lastTimeTitle2)) : "Hết hạn"),
                                        (player.lastTimeTitle3 > 0 ? ("Danh hiệu \n 3: " + (player.isTitleUse3 == true ? "On" : "Off") + "\n" + Util.msToTime(player.lastTimeTitle3)) : "Hết hạn"),
                                        "Từ chối");

                                break;
                                 
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().goToDBKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    }else if (player.iDMark.getIndexMenu() == 888) {
                        switch (select) {
                            case 0:
                                if (player.lastTimeTitle1 > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse = !player.isTitleUse;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.isTitleUse == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                }
                                break;
                            case 1:
                                if (player.lastTimeTitle2 > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse2 = !player.isTitleUse2;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.isTitleUse2 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                }
                                break;
                           case 2:
                                if (player.lastTimeTitle3 > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse3 = !player.isTitleUse3;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.isTitleUse3 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));

                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "WIBU Xin chào anh em \n|5|Số Dư VND :"
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta") + player.getSession().vnd,
                                "Đổi mật khẩu", "Nhận ngọc xanh", "Nhận đệ tử", "Kích hoạt\n Tài khoản 20k", "GiftCode","List Code \n Game");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Input.gI().createFormChangePassword(player);
                                break;
                            case 1:
                                if (player.inventory.gem == 200000) {
                                    this.npcChat(player, "không thể thực hiện");
                                    break;
                                }
                                player.inventory.gem = 200000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 200K ngọc xanh");
                                break;

                            case 2:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                } else {
                                    this.npcChat(player, "Bạn đã có rồi");
                                }
                                break;

                            case 3:
                                if (!player.getSession().actived) {
                                    if (player.getSession().vnd >= 20000) {
                                        if (PlayerDAO.subvnd(player, 20000)) {
                                            player.getSession().actived = true;
                                            PlayerDAO.activePlayer(player);
                                            this.npcChat(player, "Kích hoạt thành viên thành công!");
                                            player.inventory.ruby += 20000;
                                            Service.gI().sendMoney(player);

                                        }
                                    } else {
                                        this.npcChat(player, "Kích hoạt thành viên không thành công");
                                    }
                                } else {
                                    this.npcChat(player, "Bạn đã kích hoạt thành viên rồi!");

                                }
                                break;
                            case 4:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 5:
                          MaQuaTangManager.gI().checkInfomationGiftCode(player);
                        break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.QUA_TAN_THU) {
                        switch (select) {
                            case 2:
                                if (nhanDeTu) {
                                    if (player.pet == null) {
                                        PetService.gI().createNormalPet(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                    } else {
                                        this.npcChat("Con đã nhận đệ tử rồi");
                                    }
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_THUONG) {
                        switch (select) {

                        }
                    }
                }

            }

        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc badoc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Làm Nhiệm Vụ Tiếp theo đi", "Về đảo\nRùa", "No");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                } else if (this.mapId == 160) {
                    if (player.iDMark.getIndexMenu() == 0) { // 
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 1156);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        }
                    }
                if (this.mapId == 19) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.playerTask.taskMain.id >= 24) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Hãy hoàn thành những nhiệm vụ trước đó");
                                }
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS && boss.zone != null && boss.zone.map != null) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        if (player != null && boss != null && boss.zone != null) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;

                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                if (boss != null && !boss.isDie()) {
                                    if (player != null && boss.zone != null && player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    }
                }
                if (this.mapId == 68) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                break;
                        }
                    }
                }
                
                }  
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", "Cửa Hàng \n Đá quý","Cửa hàng \n Hồng ngọc","Đổi Thỏi Vàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;

                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "DAQUY", false);

                                    break;
                                case 2://shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA_RUBY", false);
                                    break;
                               case 3:
                                    this.createOtherMenu(player, 997,
                                            "|5|Bạn đang có :" + player.getSession().vnd + " vnd\n Bạn có chắc chắn muốn đổi  thỏi vàng?", "|7|QUY ĐỔI \n LUÔN",  "Từ chối");
                                    break;
                                       }
                    
               
                               } else if (player.iDMark.getIndexMenu() == 997) {
                            switch (select) {
                                case 0:
                       //             int vang = 5;
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI_HN, "Hãy chọn mệnh giá muốn thay đổi. tỉ lệ nạp hiện giời là 1", "9999d", "10k",
                                            "20k", "30k", "50K", "100k", "200k", "300k", "500k", "1tr");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_HN) {
                            boolean flag = false;
                            int vang = 1; // ti le map
                            int sotienquydoi = 0;
                            int thoivangnhanduoc = 0;
                            int dabaove = 0;
                            int quang = 0;

                            switch (select) {
                                case 0:
                                  //  byte vang = 5;
                                    sotienquydoi = 9999;
                                    thoivangnhanduoc = 20 * vang;
                                    dabaove = 2 * vang;
                                    quang = 5;
                                    flag = true;
                                    break;
                                case 1:
                                    sotienquydoi = 10000;
                                    thoivangnhanduoc = 30 * vang;
                                    dabaove = 3 * vang;
                                    quang =7;
                                    flag = true;
                                    break;
                                case 2:
                                    sotienquydoi = 20000;
                                    thoivangnhanduoc = 60 * vang;
                                    dabaove = 6 * vang;
                                    quang =14;
                                    flag = true;
                                    break;
                                case 3:
                                    sotienquydoi = 30000;
                                    thoivangnhanduoc = 90 * vang;
                                    dabaove = 9 * vang;
                                    quang = 20;
                                    flag = true;
                                    break;
                                case 4:
                                    sotienquydoi = 50000;
                                    thoivangnhanduoc = 160 * vang;
                                    dabaove = 16 * vang;
                                    quang = 60;
                                    flag = true;
                                    break;
                                case 5:
                                    sotienquydoi = 100000;
                                    thoivangnhanduoc = 330 * vang;
                                    dabaove = 33 * vang;
                                    quang = 120;
                                    flag = true;
                                    break;
                                case 6:
                                    sotienquydoi = 200000;
                                    thoivangnhanduoc = 670 * vang;
                                    dabaove = 67 * vang;
                                    quang = 240;
                                    flag = true;
                                    break;
                                case 7:
                                    sotienquydoi = 300000;
                                    thoivangnhanduoc = 1050 * vang;
                                    dabaove = 105 * vang;
                                    quang = 320;
                                    flag = true;
                                    break;
                                case 8:
                                    sotienquydoi = 500000;
                                    thoivangnhanduoc = 1800 * vang;
                                    dabaove = 180 * vang;
                                    quang = 500;
                                    flag = true;
                                    break;
                                case 9:
                                    sotienquydoi = 1000000;
                                    thoivangnhanduoc = 3700 * vang;
                                    quang = 1200; //chinrh phu hopw goa tri max 127 load game // vang la ti le nap
                                    dabaove = 370 * vang;
                                    flag = true;
                                    break;
                            }
                            if (flag) {
                                if (player.getSession().vnd >= sotienquydoi) {
                                    player.NapNgay += sotienquydoi;          
                                    PlayerDAO.subvndBar(player, sotienquydoi);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, thoivangnhanduoc);
                                    Item dns = ItemService.gI().createNewItem((short) 987, dabaove);
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    player.quang += quang;
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + thoivangnhanduoc
                                            + " " + thoiVang.template.name + " và " + dabaove
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy đổi ");
                                }

                            }
                        }
                    }
                }
            }
        };
    }
    
    
    public static Npc halowin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|CHÀO CẬU TÔI CÓ THỂ GIÚP GÌ CHO CẬU NHỈ \n ",
                            "THA GIA \n SỰ KIỆN ", "ĐỔI DANH HIỆ");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 29) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    this.createOtherMenu(player, 998,
                                            "|5|CHÀO CẬU ĐIẾN VỚI \n SỰ KIỆN HA LO WIN ", "|7|THAM GIA",  "Từ chối");
                                    break;

                                case 1: //shop
                                    this.createOtherMenu(player, 999,
                                            "|5|CHÀO CẬU CHẬU MUỐN ĐỔI GÌ NÈ SỐ  " , "|7|ĐỔI LUN ",  "Từ chối");
                                    break;

                               case 3:
                                    this.createOtherMenu(player, 997,
                                            "|5|Bạn đang có :" + player.getSession().vnd + " vnd\n Bạn có chắc chắn muốn đổi  thỏi vàng?", "|7|QUY ĐỔI \n LUÔN",  "Từ chối");
                                    break;
                                       
                            case 4: 
                            this.createOtherMenu(player, 999,
                                    "quang", "quang"
                            );
                            
                    break;
                            }
                               } else if (player.iDMark.getIndexMenu() == 998) {
                            switch (select) {
                                case 0:
                                    //
                                       Service.gI().sendThongBao(player, "|2|CHỨC NĂNG ĐANG VIẾT LƯỜI QUÁ \n sorry nhé sẽ hoàn thành sớm nhất có thể cho anh em \n chơi nho thank ");                                    
                       //             int vang = 5;
//                                    this.createOtherMenu(player, ConstNpc.SUKIENHALO, "Hãy chọn mệnh giá muốn thay đổi. tỉ lệ nạp hiện giời là 1", "9999d", "10k",
//                                            "20k", "30k", "50K", "100k", "200k", "300k", "500k", "1tr");
                                    break;
                            }
                        }else if (player.iDMark.getIndexMenu() == 999) {
                            switch (select) {
                                case 0:

                                   this.createOtherMenu(player, ConstNpc.đoianhhieu, "CHÀO BẠN HIỆN H SỐ ĐIỂM CỦA BẠN LÀ " + player.quang + " \n bạn có muốn đổi gì nhỉ ", "DANH HIỆU\n 3 NGÀY", "DANH HIỆU \n 7 ngày","DANH HIỆU\n 30 NGÀY");
                                    break;
                            }
                        }
                               else if (player.iDMark.getIndexMenu() == ConstNpc.đoianhhieu) {


                            switch (select) {
                                case 0:
                                     if (player.quang < 23) { 
                                        Service.gI().sendThongBao(player, "|2|BẠN KHÔNG ĐỦ 230 ĐIỂM  !");
                                                                                                                   
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {


                                        Service.gI().sendMoney(player);
                                        player.quang -= 10;
                                        Item danhieu3say = ItemService.gI().createNewItem((short) 1177);
                                        InventoryServiceNew.gI().addItemBag(player, danhieu3say);
                                        InventoryServiceNew.gI().sendItemBags(player);                                       
                                        Service.gI().sendThongBao(player, "|2|BẠN VỪA nhận dc danh hiệu 3 ngày");                                                                                                                                           
                                    }  
                                case 1:
                                     if (player.quang < 60) { 
                                        Service.gI().sendThongBao(player, "|2|BẠN KHÔNG ĐỦ 60 ĐIỂM  !");
                                                                                                                   
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {


                                        Service.gI().sendMoney(player);
                                        player.quang -= 60;
                                        Item danhieu3say = ItemService.gI().createNewItem((short) 1988);
                                        InventoryServiceNew.gI().addItemBag(player, danhieu3say);
                                        InventoryServiceNew.gI().sendItemBags(player);                                       
                                        Service.gI().sendThongBao(player, "|2|BẠN VỪA nhận dc danh hiệu 7 ngày");
                                    }
                                  //  break;
                                case 2:
                                     if (player.quang < 120) { 
                                        Service.gI().sendThongBao(player, "|2|BẠN KHÔNG ĐỦ 120 ĐIỂM  !");
                                                                                                                   
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {


                                        Service.gI().sendMoney(player);
                                        player.quang -= 120;
                                        Item danhieu3say = ItemService.gI().createNewItem((short) 1196);
                                        InventoryServiceNew.gI().addItemBag(player, danhieu3say);
                                        InventoryServiceNew.gI().sendItemBags(player);                                       
                                        Service.gI().sendThongBao(player, "|2|BẠN VỪA nhận dc danh hiệu 30 ngày");
                                    }
                                    break;
                                    }

                        }
                    }
                }
            }
        };
    }   

    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            public void chatWithNpc(Player player) {
//                String[] chat = {
//                    "ối BẠN ơi",
//                    "sever Hoàn toàn miễn phí ",
//                    "CODE BY KAMI"
//                };
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int index = 0;
//
//                    @Override
//                    public void run() {
//                        npcChat(player, chat[index]);
//                        index = (index + 1) % chat.length;
//                    }
//                }, 10000, 10000);
//            }

            @Override

            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Tài XỈU Nơi thăng hoa cảm xúc,\n|5| Mang sổ đỏ đến đây",
                            "Xỉu", "Tài");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().TAI(player);
                                    }
                                    break;

                                case 1:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().XIU(player);
                                    }
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị", "Nâng Cấp\nSarigan", "Nâng Cấp\nBtc3", "Mở Chỉ\nSố Btc3","Tiến Hóa \n Cải Trang", " Nâng Cấp \nLinh Thú","NÂNG CẤP\n SKH","SKH VIP");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata", "Mở chỉ số\nBông tai\nPorata",
                                "Nhập\nNgọc Rồng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_MAT_THAN);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP3);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3);
                                    break;
                                 case 5:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TIEN_HOA_CT);
                                   break;
                                    case 6:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.THANG_CAP_TRANG_BI);
                                   break;
                                case 7:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_KICH_HOAT);
                                    break;
                                case 8:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                                    break;                                   


                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_MAT_THAN:
                                case CombineServiceNew.NANG_CAP_DO_KICH_HOAT:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:                                    
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP3:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3:
                                case CombineServiceNew.TIEN_HOA_CT:
                                      case CombineServiceNew.THANG_CAP_TRANG_BI:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player,0);
                                    }
                                    break;
                            }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                                                    
                        
                        
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: //làm phép nhập đá
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 4:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player,0);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

      public static Npc ngokhong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 175) {
                        this.createOtherMenu(player, 0,  "Ngộ không , là  đệ tử tuyệt vời của ta \n muốn thu phục hắn ,phải cống nạp Tam Hảo cho ta \n Đệ ngộ 200 %  Chỉ số = 999 Tam Hỏa ,\n Người hẫy tiêu diệt Boss Thạch Hầu map rừng chết Air, rừng chết 1, đồi cát lún , ngẫu nhiên 3 map ",
                                 " Đệ Ngộ Không","Đóng");
                    }
                }
            }
             @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 175) {
                                
                                if (select == 0) {
                                Item hongdao = null;
                                try {
                                    hongdao = InventoryServiceNew.gI().findItemBag(player, 1183);
                                } catch (Exception e) {
                                }
                                if (hongdao == null || hongdao.quantity < 999) {
                                    Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu  Tam Hỏa , Hẫy Tiêu Diệt boss Thạch Hầu!!!");
                                } else {                                    
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, hongdao, 999);
                                    PetService.gI().changePicPet(player, player.gender);
                                    this.npcChat(player, "Bạn đã nhận được đệ Ngộ không");
                                }
                                break;
                                }
                            }
                    }
                }
            }
        };
    }
        public static Npc chidai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            
//             void Npcchat(Player player) {
//                String[] chat = {
//                    "Vì tổn Thương Nhiều quá",
//                    "Nên chẳng yêu thêm một ai ",
//                    "Vì nõi đau ngày ấy cứ theo anh từng ngày ,"
//                };
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int index = 0;
//
//                    @Override
//                    public void run() {
//                        npcChat(player, chat[index]);
//                        index = (index + 1) % chat.length;
//                    }
//                }, 6000, 6000);
//            }
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,  "Vùng đát Thám Hiểm Không dành cho những người yếu bóng vía ",
                                 " GO","Đóng");
                }
                         if (this.mapId == 180) {
                        this.createOtherMenu(player, 0,  "Nào Nào , Muốn Ta giúp chi  ",
                                 " Về đảo \nKame","Nâng Cấp \n Item Vip" , "Tiến Hóa\n Chân Mệnh","Đến\n Đền Thượng","Đống");
                    }
                }
            }
           
              @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                 //    Npcchat(player);
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 180, -1, 400);
                                    break; // 
                                    case 5:
                                BossManager.gI().showListBoss(player);
                                }
                        }
                    } else if (this.mapId == 180) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 456);
                                    break;
                                case 1:
                                     CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_VIP);
                                    break;
                                case 2:
                                     CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHAN_MENH);
                                    break;   
                                 case 3:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 181, -1, 966);
                                    break;
                                        }
                            
                                     } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                     switch (player.combineNew.typeCombine) {
                                      case CombineServiceNew.NANG_VIP:
                                       case CombineServiceNew.CHAN_MENH:    
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player ,0);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    
        public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//        public void Npcchat(Player player) {
//                String[] chat = {
//                    "Giúp Ta đẫn Mị Nương Về Nha",
//                    "Em buông tay anh vì lí do gì ",
//                    "Người hãy nói đi , đừng Bắt Anh phải nghĩ suy"
//                };
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int index = 0;
//
//                    @Override
//                    public void run() {
//                        npcChat(player, chat[index]);
//                        index = (index + 1) % chat.length;
//                    }
//                }, 6000, 6000);
//            }
            @Override
              public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                               "Mị nương đang đi lạc ngươi hãy giúp ta đưa nàng đến đảo kame \n Ta trao thưởng quà Hậu hĩnh,"
                                        
                                        , "Hướng dẫn\n Hộ Tống mị", "Hộ Tống" , " Shop Mị" ,"Đóng");
                        
                
                }

            }                                   
                                    
                                    
            @Override
         
              public void confirmMenu(Player player, int select) {
            //       Npcchat(player);
                if (canOpenNpc(player)) {
                    if (this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) { 
                           switch (select) {
                                case 1:
                                Boss oldDuongTank = BossManager.gI().getBossById(Util.createIdDuongTank((int) player.id));
                                if (oldDuongTank != null) {
                                    this.npcChat(player, " Mị Nương đang được hộ tống" + oldDuongTank.zone.zoneId);
                                } else if (player.inventory.ruby < 20000) {
                                    this.npcChat(player, "Nhà ngươi không đủ 20K Hồng Ngọc ");
                                } else {
//                                    Item honLinhThu = null;
//                                    try {
//                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 695);
//                                    } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                    }
//                                    if (honLinhThu == null || honLinhThu.quantity < 1) {
//                                        this.npcChat(player, "Bạn không đủ vật phẩm");                                                                                                        
//                                    } else {
//                                        
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 1);               
//                                        Service.getInstance().sendMoney(player);
//                                        InventoryServiceNew.gI().sendItemBags(player);
                                    
                                    BossData bossDataClone = new BossData(
                                            "Mị nương do" +" "+ player.name + " hộ tống",
                                            (byte) 2,
                                            new short[]{841, 842, 843, -1, -1, -1},
                                            100000,
                                            new long[]{player.nPoint.hpMax * 2},
                                            new int[]{103},
                                            new int[][]{
                                            {Skill.TAI_TAO_NANG_LUONG, 7, 15000}},
                                            new String[]{}, //text chat 1
                                            new String[]{}, //text chat 2
                                            new String[]{}, //text chat 3
                                            60
                                    );

                                    try {
                                        DuongTank dt = new DuongTank(Util.createIdDuongTank((int) player.id), bossDataClone, player.zone, player.location.x - 20, player.location.y);
                                        dt.playerTarger = player;
                                        int[] map = {6,29,30,4,5,27,28};
                                        dt.mapCongDuc = map[Util.nextInt(map.length)];
                                        player.haveDuongTang = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //trừ vàng khi gọi boss
                                    player.inventory.ruby -= 20000;
                                    Service.getInstance().sendMoney(player);
                                break;
                                }
                               case 0: 
                             Service.getInstance().sendThongBaoFromAdmin(player, " Gặp Npc VUA HÙNG , Chọn Hộ Tống Rồi Dắt Mị đếnVị Trí được chỉ định \n "
                                     + "Phần quà 100k Ngọc Hồng , Ran Dom 5 -10 Đồng Bạc , Phí dắt 20k Ngọc Hồng ..");
                                break;
                            case 2:
                                    ShopServiceNew.gI().opendShop(player, "MI", true);
                                    break;
                    
                 
                            case 7:
                                    this.createOtherMenu(player, 997,
                                            "|5|Bạn đang có :" + player.getSession().vnd + " vnd\n Bạn có Muốn Mua  Đồng Bạc?\n Luu ý phải có đồng bạc trong hành trang mới có thể mua ", "500k Vnd\n9990 đồng Bạc",  "1000k\n18888 Đồng Bạc", "Từ chối");
                                    break;
                                        }   
                              } else if (player.iDMark.getIndexMenu() == 997) {
                            switch (select) {
                                  case 0:
                                    if (player.getSession().vnd < 500000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        Item honthu = null;
                               try {
                                    honthu = InventoryServiceNew.gI().findItemBag(player, 1187);
                                     honthu.itemOptions.add(new Item.ItemOption(30, 1));
                                } catch (Exception e) {
                                }
                                       PlayerDAO.subvnd(player, 500000);
                                      Service.gI().sendMoney(player);
                                        honthu.quantity += 9990;
                                  this.npcChat(player, "Bạn Nhận được 9990 đồng bạc");     
                                  break;
                                    }
                                     case 1:
                                    if (player.getSession().vnd < 1000000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        Item thoivang = null;
                                try {
                                    thoivang = InventoryServiceNew.gI().findItemBag(player, 1187);
                                     thoivang.itemOptions.add(new Item.ItemOption(30, 1));
                                } catch (Exception e) {
                                }
                                        PlayerDAO.subvnd(player, 1000000);
                                        Service.gI().sendMoney(player);
                                         thoivang.quantity += 18888;
                                     this.npcChat(player, "Bạn Nhận được 18888 đồng bạc"); 
                                      break;
                                    }
                    
                            } 
                    }
                }
            }
        }
     };
     }   

    public static Npc duongtang(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "|1|Rừng chết , đang bị lũ yêu ma quỷ quái bao vây , ngươi hãy giúp ta đến đó tiêu diệt nó nhé \n Nơi đây có rất nhiều vật phẩm quý hiếm \n Đào tiên , thỏi vàng , hồng ngọc ...,Quái Khá là mạnh nhé \n chúc ngươi đi may mắn", "OK", "Từ chối");
                    }
                    if (mapId == 123) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Aru?", "OK", "Từ chối");

                    }
                    if (mapId == 177) {
                        this.createOtherMenu(player, 0,  "Ngộ không , Bắt Giới là những đệ tử tuyệt vời của ta \n muốn thu phục hắn ,phải cống nạp Đào tiên cho ta \n Đệ Bắt giới 100 %  Chỉ số = 999 đào tiên ",
                                 " Đệ Bắt Giới","Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 0) {
                                if (player.capCS < 10) {
                                    Service.gI().sendThongBao(player, "cấp chuyển sinh 10 mới có thể thông hành!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapInYard(player, 175, -1, 174);
                            }
                            if (mapId == 177) {
                                
                                if (select == 0) {
                                Item hongdao = null;
                                try {
                                    hongdao = InventoryServiceNew.gI().findItemBag(player, 1176);
                                } catch (Exception e) {
                                }
                                if (hongdao == null || hongdao.quantity < 999) {
                                    Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu  đào tiên!!!");
                                } else {//codebypain                                        
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, hongdao, 999);
                                    PetService.gI().changeBerusPet(player, player.gender);
                                    this.npcChat(player, "Bạn đã nhận được đệ BẮt Giới");
                                }
                                break;
                                }
                            }
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
//                                    changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.nPoint.power >= 800000000L) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 800tr sức mạnh để vào!");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

//public static Npc Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 149) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "tét", "Gọi nhân bản");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                   if (select == 0){
//                        BossManager.gI().createBoss(-214);
//                   }
//                }
//            }
//        };
//    }
    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nThỏi vàng");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
                        } else if (select == 1) {
                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    break;
                                case 1: {
                                    Item item = InventoryServiceNew.gI().findItemBag(pl, 457);
                                    if (item.isNotNullItem() && item.quantity >= 20) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 20);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        pl.goldNormar += 20;
                                        ChonAiDay.gI().goldNormar += 20;
                                        ChonAiDay.gI().addPlayerNormar(pl);
                                        createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    } else {
                                        Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");

                                    }
                                }
                                break;

                                case 2: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 200);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.goldVIP += 200;
                                            ChonAiDay.gI().goldVip += 200;
                                            ChonAiDay.gI().addPlayerVIP(pl);
                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                        }
                                    } catch (Exception ex) {
//                                            java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                            }
                        }
                    }
                }
            }
        };
    }
    
    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    //con đường rắn độc
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                    if (this.mapId == 181) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Shop","Đến \n Đền Băng"," Từ Chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }  else   if (this.mapId == 181) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                  ShopServiceNew.gI().opendShop(player, "TAPITON", false);
                                    break;
                                  case 1:
                                    ChangeMapService.gI().changeMap(player, 159, -1, -1, -1);
                                    break;
                               
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    
    public static Npc taption(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "HIẾN TẾ CẮT CU BẠN ĐÃ SẴN SÀNG CHƯA ",
                        "hướng dẫn tao \n cắt cu mày", "bắt đầu cắt cu", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (player.iDMark.isBaseMenu()) {
//                        switch (select) {
//                            case 0:
//                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
//                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
//                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
//                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
//                                            "Nâng\ngiới hạn\nsức mạnh",
//                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
//                                } else {
//                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                            "Sức mạnh của con đã đạt tới giới hạn",
//                                            "Đóng");
//                                }
//                                break;
//                            case 1:
//                                if (player.pet != null) {
//                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
//                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
//                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
//                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
//                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
//                                    } else {
//                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
//                                                "Đóng");
//                                    }
//                                } else {
//                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
//                                }
//                                //giới hạn đệ tử
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
//                        switch (select) {
//                           
//                            case 0:
//                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
//                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
//                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
//                                        Service.gI().sendMoney(player);
//                                    }
//                                } else {
//                                    Service.gI().sendThongBao(player,
//                                            "Bạn không đủ vàng để mở, còn thiếu "
//                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
//                                }
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
//                        if (select == 0) {
//                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
//                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
//                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
//                                    Service.gI().sendMoney(player);
//                                }
//                            } else {
//                                Service.gI().sendThongBao(player,
//                                        "Bạn không đủ vàng để mở, còn thiếu "
//                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
//                            }
                       // }
                   // }
               // }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        if (player.mabuEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                    if (this.mapId == 154) {
                        player.billEgg.sendBillEgg();
                        if (player.billEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.mabuEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.mabuEgg.sendMabuEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_EGG:
                                if (select == 0) {
                                    player.mabuEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                    if (this.mapId == 154) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg();
                                }
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                           
                            case 0:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
//                                if (!player.getSession().actived) {
//                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                                } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }
    

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7| Bạn Có muốn vào khu kiếm thẻ chiến lực\nTa sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 120 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  500 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi SKH VIP","NÂNG SKH ","Từ Chối");
                }
            }

            //if (player.inventory.gold < 500000000) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                return;
//            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 120000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 120000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        } // kamiv3
                        if (select == 2) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_KICH_HOAT);
                        }                         

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player,0);
                        }
                    }
                }
            }

        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đói bụng quá.. ngươi mang cho ta x99 phần đồ ăn 5 loại,\nta sẽ mở shop Hủy Diệt.\n Nếu tâm trạng ta vui ngươi có thể nhận được trang bị\ntăng đến 15%!",
                            "OK", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        if (player.setClothes.godClothes) {
                                                ShopServiceNew.gI().opendShop(player, "BILL", true);
                                                break;
                                            }
                                          Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu  đồ thần linh");
                                    }
                                            break;
                                        }
                                    }
                }
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.!",
                            "Nâng Đồ \nThien sứ", "Học\nTuyệt Kỹ", "Hướng dẫn");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Cửa hàng", "Chế tạo", "Đóng");
                                break;
                            case 1:
                                Item BiKiepTuyetKy = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1180);
                                if (BiKiepTuyetKy != null) {
                                    this.createOtherMenu(player, 6, "|5|Ta sẽ giúp ngươi học tuyệt kỹ: %skill \nBí kiếp tuyệt kỹ: "
                                            + BiKiepTuyetKy.quantity
                                            + "/9999\nGiá vàng: 10.000.000\nGiá ngọc: 99.999" + "Yêu Cầu 80 tỷ sức mạnh",
                                            "Đồng ý\nHọc", "Từ chối");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "THIEN_SU", false);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item biKiepTuyetKy = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1180);
                                if (biKiepTuyetKy.quantity >= 99) {
                                    if (player.inventory.gold >= 1000000000) {
                                        if (player.inventory.ruby >= 99999) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, biKiepTuyetKy, 99);
                                            player.inventory.gold -= 1000000000;
                                            player.inventory.ruby -= 99999;
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendMoney(player);
                                            if (player.getSession().player.nPoint.power >= 60000000000L) {
                                                switch (player.gender) {
                                                    case 0:
                                                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                                        Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã học tuyệt kỹ Super KameJoko");
                                                        break;
                                                    case 1:
                                                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                                        Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã học tuyệt kỹ Ma Phong Ba");
                                                        break;
                                                    case 2:
                                                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                                        Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã học tuyệt kỹ Cadic Liên Hoàn Chưởng");
                                                        break;
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Con không đủ Sức mạnh để học tuyệt kỹ này !");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Con không đủ Vàng để học tuyệt kỹ này !");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Con không đủ Hồng Ngọc để học tuyệt kỹ này !");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Con không đủ bí kíp tuyệt kỹ để học tuyệt kỹ này");
                                }
                                break;

                        }
                    }
                }
            }
        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Ngài cần  \b|7|gì ở tôi!", "Nhiệm vụ\nhàng ngày", "Từ chối");
                    }
//                    if (this.mapId == 47) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
//                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "|7|Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Hê Hê Hê ta là thần mèo Karin",
                                    "NÂNG CẤP \nSKH","NÂNG CẤP \nSKH VIP");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|2|Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Cập nhật top Server\b|7|Người Muốn Xem BXH GÌ?" ,
                            "BXH \n chuyển sinh", "BXH\n Nhiệm Vụ", "BXH\nTu Tiên", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Service.gI().showListTop(player, Manager.topSM);
                                        break;
                                    }
                                    if (select == 1) {
                                        Service.gI().showListTop(player, Manager.topNV);
                                        break;
                                    }
                                    if (select == 2) {
                                        Service.gI().showListTop(player, Manager.topSK);
                                        break;
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity + " bí kiếp.\n"
                                    + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");

                        }

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                InventoryServiceNew.gI().addItemBag(player, yardart);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                            }
                        }

                    }
                }
            }
        };
    }

    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn muốn nâng cấp khỉ ư?", "Nâng cấp\nkhỉ", "Shop của Khỉ", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Cần Khỉ Lv1 hoặc 2,4,6 để nâng cấp lên lv8\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm 10 đá ngũ sắc",
                                            "Khỉ\ncấp 2",
                                            "Khỉ\ncấp 4",
                                            "Khỉ\ncấp 6",
                                            "Khỉ\ncấp 8",
                                            "Từ chối");
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "KHI", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1137);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1137 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1137 + i) && soLuong >= 20) {
                                            CombineServiceNew.gI().khilv2(player, 1138 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 20);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 1 với 20 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 1: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1138);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1138 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1138 + i) && soLuong >= 30) {
                                            CombineServiceNew.gI().khilv3(player, 1139 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 2 với 30 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 2: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1139);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1139 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1139 + i) && soLuong >= 40) {
                                            CombineServiceNew.gI().khilv4(player, 1140 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 40);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 3 với 40 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 3: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1140);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1140 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1140 + i) && soLuong >= 50) {
                                            CombineServiceNew.gI().khilv5(player, 1136 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 50);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 3 với 50 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;

                                case 5: // canel
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 52) {
                        createOtherMenu(pl, 0, DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Giai(pl), "Thông tin\nChi tiết", DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(pl) ? "Đăng ký" : "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23");
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 52) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, DaiHoiVoThuat.gI().Info());
                                break;
                            case 1:
                                if (DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(player)) {
                                    DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Reg(player);
                                }
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                break;
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 2:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "|7|chỗ tôi có bán đệ vip + 60% hợp thể 300k và \n|5| Ngọc Hồng số lượng lớn ", " ĐỆ VIP ", " NGỌC HỒNG", "đóng");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 999,
                                            "|7|Bạn đang có :" + player.getSession().vnd + " vnd\n Bạn có chắc chắn muốn mua đệ WIBU?", "Đồng ý", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 997,
                                            "|5|Bạn đang có :" + player.getSession().vnd + " vnd\n Bạn có chắc chắn muốn đổi  thỏi vàng?", "20k Vnd\n2k thỏi vàng", "50k\n50kHồng ngọc", "100k\n110kHồng ngọc", "200k\n250kHồng Ngọc", "500k\n650kHồng Ngọc", "Từ chối");
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == 999) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd < 300000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        this.createOtherMenu(player, 998,
                                                "Chọn hành tinh", "Trái đất", "Namek", "XayDa", "Từ chối");
                                        break;
                                    }
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 998) {
                            switch (select) {
                                case 0:
                                    if (player.pet == null) {
                                        PetService.gI().createBerusPet(player, 0);
                                    } else {
                                        PetService.gI().changeBerusPet(player, 0);
                                    }
                                    ChangeMapService.gI().changeMapInYard(player, player.gender * 7, -1, Util.nextInt(300, 500));
                                    PlayerDAO.subvnd(player, 300000);
                                    break;
                                case 1:
                                    if (player.pet == null) {
                                        PetService.gI().createBerusPet(player, 1);
                                    } else {
                                        PetService.gI().changeBerusPet(player, 1);
                                    }
                                    ChangeMapService.gI().changeMapInYard(player, player.gender * 7, -1, Util.nextInt(300, 500));
                                    PlayerDAO.subvnd(player, 300000);
                                    break;
                                case 2:
                                    if (player.pet == null) {
                                        PetService.gI().createBerusPet(player, 2);
                                    } else {
                                        PetService.gI().changeBerusPet(player, 2);
                                    }
                                    ChangeMapService.gI().changeMapInYard(player, player.gender * 7, -1, Util.nextInt(300, 500));
                                    PlayerDAO.subvnd(player, 300000);
                                    break;
                                case 3:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 997) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd < 20000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                         Item thoivang = null;
                                         thoivang.quantity = 2000;
                                        PlayerDAO.subvnd(player, 20000);
                                        Service.gI().sendMoney(player);
                                        this.npcChat(player, "Bạn nhận được 2k thỏi vàng");
                                        break;
                                    }

                                case 1:
                                    if (player.getSession().vnd < 50000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        player.inventory.ruby += 50000;
                                        PlayerDAO.subvnd(player, 50000);
                                        Service.gI().sendMoney(player);
                                        this.npcChat(player, "Bạn nhận được 50k hồng ngọc");

                                        break;
                                    }

                                case 2:
                                    if (player.getSession().vnd < 100000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        player.inventory.ruby += 110000;
                                        PlayerDAO.subvnd(player, 100000);
                                        Service.gI().sendMoney(player);
                                        this.npcChat(player, "Bạn nhận được 110k hồng ngọc");

                                        break;
                                    }

                                case 3:
                                    if (player.getSession().vnd < 200000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        player.inventory.ruby += 250000;
                                        PlayerDAO.subvnd(player, 200000);
                                        Service.gI().sendMoney(player);
                                        this.npcChat(player, "Bạn nhận được 250k hồng ngọc");

                                        break;
                                    }

                                case 4:
                                    if (player.getSession().vnd < 500000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        player.inventory.ruby += 650000;
                                        PlayerDAO.subvnd(player, 500000);
                                        Service.gI().sendMoney(player);
                                    }
                                    this.npcChat(player, "Bạn nhận được 650k hồng ngọc");

                                    break;
                                case 5:

                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bery(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//      public void Npcchat(Player player) {
//                String[] chat = {
//                    "Không tiền vất vả gian nan",
//                    "Em buông tay anh vì lí do gì ",
//                    "Người hãy nói đi ,"
//                };
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int index = 0;
//
//                    @Override
//                    public void run() {
//                        npcChat(player, chat[index]);
//                        index = (index + 1) % chat.length;
//                    }
//                }, 6000, 6000);
//            }
            @Override
            public void openBaseMenu(Player player) {
                //  Npcchat(player);
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "|6|Chào bạn tôi sẽ đưa bạn đến hành tinh ZuZU Bóng tối,\n Nơi đây chiếm giữ Đá xanh LAm\n vật phẩm Nâng bt3,2\n và Đá Nâng Cấp Mắt th   ần cho đệ ?\n Và đặc biệt đang giam giữ boss bãi Biển \n tiềm năng sm đang nhân 4", "Ok",
                                "Đóng");
                    }
                    if (this.mapId == 173) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về đảo kame", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 173, -1, 200);
                                    break; // 
                                    case 5:
                                BossManager.gI().showListBoss(player);
                                }
                        }
                    } else if (this.mapId == 173) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 456);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "|7|Chào bạn tôi sẽ đưa bạn đến hành tinh Đảo Rùa,\n Nơi đây chiếm giữ Mảnh vỡ  btc3\n vật phẩm mở chỉ  số btc3 ?", "Đảo rùa", "Đồ chơi\nĐệ",
                                "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về đảo kame", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, -1);
                                    break; // 
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "DE", false);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP3:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3:
                                case CombineServiceNew.NANG_CAP_MAT_THAN:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player ,0);
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 806);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.lastTimeOpenDoanhTrai = (System.currentTimeMillis() - 300000);
                        //  player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                        }
                        //  player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
//                    if (player.clan != null && player.name.contains("trumticker")) {
//                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
//                                "NQMP",
//                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + (TimeUtil.getSecondLeft(player.clan.lastTimeOpenDoanhTrai, DoanhTrai.TIME_DOANH_TRAI / 1000)) / 60
                                + "p. Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.LastDoanhTrai <= 1 * 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi đã đi hôm nay rồi \n Thời gian chờ còn lại để có thể vào : " + (((player.LastDoanhTrai + (1 * 24 * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000) + " s", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.clan.createTimeLong <= 2 * 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi chỉ vừa tạo.Hãy chờ đủ 2 ngày để có thể vào doanh trại \n Thời gian chờ còn lại để có thể vào : " + (((player.clan.createTimeLong + (2 * 24 * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000) + " s", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.clan.lastTimeOpenDoanhTrai <= 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi doanh trại gần đây .Hãy chờ" + (((player.clan.lastTimeOpenDoanhTrai + (24 * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000) + "s để có thể tham gia doanh trại lần nữa. Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }

                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override

            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }
    
    public static Npc tutien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                                  Service.gI().sendThongBaoOK(player, "HỆ THỐNG ĐÃ BẢO TRÌ CHỨC NĂNG NÀY XIN CẢM ƠN ");                    
//                    if (this.mapId == 5) {
//                        this.createOtherMenu(player, 0,
//                                "|2|Bạn đạt 200 tỷ sức mạnh , chuyển sinh cấp 34 , đá tu tiên có thể tu tiên \n"
//                                + "cấp 1-10 199 đá \n cấp 10-20 599 đá \n cấp 20-30 999 đá \n "
//                                + "\n" + "\n\nSức Mạnh: " + Util.getFormatNumber(player.nPoint.power) + "/" + "200.000.000.000 sức mạnh"
//                                + "\n\n Cấp chuyển sinh : " + Util.getFormatNumber(player.capCS), "Hướng dẫn","Tu Tiên", "Xem thông tin", " từ chối");
//
//                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                     NpcService.gI().createTutorial(player, this.avartar, ConstNpc.TU_TIEN);
                                     break;
                                case 1:
                                    int percent = 100;
                                    int hongngoc = 0;
                                    int da =0;
                                    Item tutien = null;
                                    tutien = InventoryServiceNew.gI().findItemBag(player, 1998);
                                    if (player.capTT <= 10) {
                                        hongngoc = 199;
                                        da = 10000;
                                    }
                                    if (player.capTT >10 && player.capTT <= 20) {
                                        hongngoc = 599;
                                        da = 20000;
                                    }
                                    if (player.capTT > 20) {
                                        hongngoc = 999;
                                        da = 30000;
                                    }

                                    if (player.capTT >= 20) {
                                        percent = percent - (player.capTT) * 3;
                                    }
                                    this.createOtherMenu(player, 987,
                                            "|6|Bạn đang Tu Tiên  :" + player.capTT + " \n Cấp tiếp theo với tỉ lệ : " + (100 - player.capTT * 3) + "% \n Mức giá Tu Tiên : " + hongngoc + "Đá tu tiên, "+da+" hồng ngọc \n Bạn có muốn tu tiên ?", "Đồng ý", "Từ chối");
                                    break; // 
                                case 2:
                                    int hp = 0,
                                     dame = 0;
                                    if (player.capTT > 0) {
                                        if (player.capTT <= 10) {
                                            dame += (9000) * player.capTT;
                                            hp += (17000) * player.capTT;
                                        }
                                        if (player.capTT <= 20 && player.capTT > 10) {
                                            dame += (11000) * (player.capTT);
                                            hp += (25000) * (player.capTT);
                                        }
                                        if (player.capTT > 20) {
                                            dame += (15000) * (player.capTT);
                                            hp += (32000) * (player.capTT);
                                        }
                                    }
                                    Service.gI().sendThongBaoOK(player, "Bạn đang cấp Tu Tiên: " + player.capTT + "\n HP : +" + hp + "\n MP : +" + hp + "\n Dame : +" + dame);

                                    break;
                                case 3:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 987) {
                            if (player.capCS < 34) {
                                npcChat(player, "Bạn chưa đủ cấp chuyển sinh");
                                return;
                            }
                            if (player.nPoint.power < 200000000000L) {
                                npcChat(player, "Bạn chưa đủ sức mạnh tu tiẻn");
                                return;
                            } else {
                                int hongngoc = 0;
                                int da = 0;
                                Item tutien = null;
                                tutien = InventoryServiceNew.gI().findItemBag(player, 1998);
                                if (player.capTT <= 10) {
                                    hongngoc = 199;
                                    da = 10000;
                                }
                                if (player.capTT <= 20 && player.capTT > 10) {
                                    hongngoc = 599;
                                    da = 20000;
                                }
                                if (player.capTT > 20) {
                                    hongngoc = 999;
                                    da = 30000;
                                }
                                if (player.inventory.ruby < da) {
                                    npcChat(player, "Bạn chưa đủ hồng ngọc để Tu Tiên");
                                    return;
                                }
                                
                                if (tutien == null || tutien.quantity < hongngoc) {
                                    this.npcChat(player, "Bạn không đủ "+hongngoc+" đá tu tiên");
                                    return;
                                }
                                int percent = 100;
                                if (player.capTT >= 20) {
                                    percent = percent - (player.capTT) * 3;
                                }
                                player.inventory.ruby -= da;
                                InventoryServiceNew.gI().subQuantityItemsBag(player, tutien, hongngoc);
                                if (Util.nextInt(0, 100) < (percent)) {
                                    player.capTT++;
                                    npcChat(player, "Tu Tiên thành công \n cấp hiện tại :" + player.capTT);
                                    player.nPoint.power = 1500000;

                                } else {
                                    npcChat(player, "Tu tiên thất bại \n cấp hiện tại :" + player.capTT);
                                    player.nPoint.power = 180000000000L;
                                }
                                Service.gI().player(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendMoney(player);
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
// đây là chỗ chuyển sinh sư phụ // là hiết 
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "|3|Bạn đạt 200 tỷ sức mạnh có thể chuyển sinh \n KHI CHUYẺN SINH HỤT BẠN SẼ BỊ TRỪ ngẫu nghiên và chuyển sinh thành công \n cũng ngẫu nhiên ❤️ \nSỨC ĐÁNH VÀ 300MP VÀ HP  \n" + "\n\nSức Mạnh: " + Util.getFormatNumber(player.nPoint.power) + "/" + "200.000.000.000 sức mạnh", "Chuyển sinh", "Xem thông tin",
                                "từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    int percent = 100;
                                    int hongngoc = 0;
                                    if (player.capCS <= 10) {
                                        hongngoc = 10000;
                                    }
                                    if (player.capCS <= 20 && player.capCS > 10) {
                                        hongngoc = 20000;
                                    }
                                    if (player.capCS > 20) {
                                        hongngoc = 30000;
                                    }

                                    if (player.capCS >= 20) {
                                        percent = percent - (player.capCS) * 3;
                                    }
                                    this.createOtherMenu(player, 987,
                                            "|5|Bạn đang chuyển sinh :" + player.capCS + " \n Cấp tiếp theo với tỉ lệ : " + (50 - player.capCS * 3) + "% \n Mức giá chuyển sinh : " + hongngoc + "hồng ngọc \nBạn có muốn chuyển sinh ?", "Đồng ý", "Từ chối");
                                    break; // 
                                case 1:
                                    if (player.nPoint.hpg < 750000) {
                                     Service.gI().sendThongBao(player, "|2|vui lòng nâng MAX CHỈ SỐ ĐỂ XEM CHỈ SỐ CHUYỂN SINH !");                                     
                                
                                    } else if (player.nPoint.dame < 32000) { 
                                        Service.gI().sendThongBao(player, "|2|vui lòng nâng MAX CHỈ SỐ ĐỂ XEM CHỈ SỐ CHUYỂN SINH !");
                                    } else if (player.nPoint.mpg < 750000) { 
                                        Service.gI().sendThongBao(player, "|2|vui lòng nâng MAX CHỈ SỐ ĐỂ XEM CHỈ SỐ CHUYỂN SINH !");                                         
                                    } else {
                                   
        
                                    Service.gI().sendThongBaoOK(player, "Bạn đang cấp chuyển sinh: " + player.capCS + "\n HP : +" + (player.nPoint.hpg - 750000) + "\n MP : +" +  (player.nPoint.mpg - 750000)  + "\n Dame : +" + (player.nPoint.dameg - 32000));
                                    }
                                    break;
                                case 2:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 987) {
                            if (player.nPoint.power < 200000000000L) {
                                npcChat(player, "Bạn chưa đủ khỏe để chuyển sinh");
                                return;
                            } else {
                                int hongngoc = 0;
                                if (player.capCS <= 10) {
                                    hongngoc = 10000;
                                }
                                if (player.capCS <= 20 && player.capCS > 10) {
                                    hongngoc = 20000;
                                }
                                if (player.capCS > 20) {
                                    hongngoc = 30000;
                                }
                                if (player.inventory.ruby < hongngoc) {
                                    npcChat(player, "Bạn chưa đủ hồng ngọc để chuyển sinh");
                                    return;
                                }
                                int percent = 100;
                                if (player.capCS >= 20) {
                                    percent = percent - (player.capCS) * 3;
                                }
                                player.inventory.ruby -= hongngoc;
                                if (Util.nextInt(0, 100) < (percent)) {
                                    player.capCS++;
                                    player.quang +=3;
                                    npcChat(player, "Chuyển sinh thành công \n cấp hiện tại :" + player.capCS);
                                    player.nPoint.power = Util.nextInt(0, 1000000000);;
                                    player.nPoint.hpg += Util.nextInt(200, 1500);
                                    player.nPoint.dameg += Util.nextInt(50, 300);
                                    player.nPoint.mpg += Util.nextInt(200, 2000);                       
                                } else {
                                    npcChat(player, "Chuyển sinh thất bại check chỉ số bị trừ \n cấp hiện tại :" + (player.capCS -1));
                                     player.capCS -=1;
                                     player.nPoint.power = 100000000000L;
                                     player.nPoint.dameg -= Util.nextInt(50, 150);
                                     player.nPoint.hpg -= Util.nextInt(200, 1000);
                                     player.nPoint.mpg -= Util.nextInt(200, 1000);

                                }
                                Service.gI().player(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendMoney(player);

                            }

                        }
                    }
                }
            }
        };
    }
    public static Npc chuyensinhdt(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
// muôn tắt thì tắt chuyển sinh đệ ở đây 
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "|7| CHÀO CẬU ! \n  CẬU CÓ MUỐN HIẾN TẾ ĐỆ TỰ ĐÃ NUÔI ĐẬY ĐỂ GIA TĂNG \n SỨC MẠNH BẢN THÂN HAY KHÔNG? \n NGƯƠI SẼ MẤT 30 TỈ SỨC MẠNH VÀ ĐỆ TỬ CỦA NGƯƠI  \n SỨC MẠNH SẼ ĐƯỢC CỘNG NGẪU NHIÊN CHO NGƯỜI\n " + Util.getFormatNumber(player.pet.nPoint.power) + "/" + "200.000.000.000 sức mạnh", "HI SINH ĐỆ TỬ", "Xem thông tin",
                                "từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                        // if (player.pet.nPoint.power >= 200000000000L) {
 {
                                    Item dt = null;

                                    try {
                                        dt = InventoryServiceNew.gI().findItemBag(player, 1763);
 
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (dt == null || dt.quantity < 1) {
                                       Service.gI().sendThongBao(player, "|2|CHỨC NĂNG YÊU CẦU CÓ 3 THẺ CHUYỂN SINH ĐỆ TỬ <>!");
                                    } else if (player.pet.nPoint.power < 200000000000L) { 
                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU ĐỆ TỬ PHẢI 200 TỈ SM NHÉ !");
                                    } else if (player.nPoint.power <200000000000L) { 
                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU SƯ PHỤ TRIÊN 200 TỈ  TỈ SM NHÉ !");                                                                                                                      
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, dt, 1);
                                       // PetService.gI().createNormalPet(player);   tạo pett mới thì bật lên ko thì tắt đi 
                                        Service.gI().sendMoney(player);
                                        player.nPoint.power -= 30000000000L;
                                        player.pet.nPoint.power = 1200L;
                                        player.nPoint.hpg += Util.nextInt(200, 1500); // chỉ số cộng cho sư phụ 
                                        player.nPoint.dameg += Util.nextInt(50, 300);
                                        player.nPoint.mpg += Util.nextInt(200, 2000); 
                                        // chỉ số pet ki bị trừ 
                                        player.pet.nPoint.hpg = Util.nextInt(200, 2000);
                                        player.pet.nPoint.mpg = Util.nextInt(200, 2000);
                                        player.pet.nPoint.dameg = Util.nextInt(10, 200);
                                        player.pet.nPoint.critg = Util.nextInt(1, 5);
                                        player.pet.nPoint.defg  =  Util.nextInt(10, 309);
                                        player.pet.nPoint.critg = 1;
                                        player.pet.nPoint.defg = 100;
                                        player.pet.playerSkill.skills.get(1).skillId = -1;
                                        player.pet.playerSkill.skills.get(2).skillId = -1;
                                        player.pet.playerSkill.skills.get(4).skillId = -1;
                                        player.pet.playerSkill.skills.get(3).skillId = -1;    
                                        InventoryServiceNew.gI().sendItemBags(player);                                       
                                        Service.gI().sendThongBao(player, "|2| ĐỆ NHÀ NGƯƠI ĐÃ BỊ T RÚT MÁU HAHA 🤣 CHỈ \n chỉ số nhà người đã dc cộng ngẫu nghiên ");
                                      //  Client.gI().kickSession(player.getSession());

                                    
                        

                        }
                    }
                }
            
        };
    } 
//    public static Npc (int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        this.createOtherMenu(player, 0,
//                                "|7| CHÀO CẬU ! \n  CẬU CÓ MUỐN HIẾN TẾ ĐỆ TỰ ĐÃ NUÔI ĐẬY ĐỂ GIA TĂNG \n SỨC MẠNH BẢN THÂN HAY KHÔNG? \n NGƯƠI SẼ MẤT 30 TỈ SỨC MẠNH VÀ ĐỆ TỬ CỦA NGƯƠI  \n SỨC MẠNH SẼ ĐƯỢC CỘNG NGẪU NHIÊN CHO NGƯỜI\n " + Util.getFormatNumber(player.pet.nPoint.power) + "/" + "200.000.000.000 sức mạnh", "HI SINH ĐỆ TỬ", "Xem thông tin",
//                                "từ chối");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                        // if (player.pet.nPoint.power >= 200000000000L) {
// {
//                                    Item dt = null;
//
//                                    try {
//                                        dt = InventoryServiceNew.gI().findItemBag(player, 1763);
// 
//                                    } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                    }
//                                    if (dt == null || dt.quantity < 1) {
//                                       Service.gI().sendThongBao(player, "|2|CHỨC NĂNG YÊU CẦU CÓ 3 THẺ CHUYỂN SINH ĐỆ TỬ <>!");
//                                    } else if (player.pet.nPoint.power < 200000000000L) { 
//                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU ĐỆ TỬ PHẢI 200 TỈ SM NHÉ !");
//                                    } else if (player.nPoint.power <200000000000L) { 
//                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU SƯ PHỤ TRIÊN 200 TỈ  TỈ SM NHÉ !");                                                                                                                      
//                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                    } else {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, dt, 1);
//                                       // PetService.gI().createNormalPet(player);   tạo pett mới thì bật lên ko thì tắt đi 
//                                        Service.gI().sendMoney(player);
//                                        player.nPoint.power -= 30000000000L;
//                                        player.pet.nPoint.power = 1200L;
//                                        player.nPoint.hpg += Util.nextInt(200, 1500); // chỉ số cộng cho sư phụ 
//                                        player.nPoint.dameg += Util.nextInt(50, 300);
//                                        player.nPoint.mpg += Util.nextInt(200, 2000); 
//                                        // chỉ số pet ki bị trừ 
//                                        player.pet.nPoint.hpg = Util.nextInt(200, 2000);
//                                        player.pet.nPoint.mpg = Util.nextInt(200, 2000);
//                                        player.pet.nPoint.dameg = Util.nextInt(10, 200);
//                                        player.pet.nPoint.critg = Util.nextInt(1, 5);
//                                        player.pet.nPoint.defg  =  Util.nextInt(10, 309);
//                                        player.pet.nPoint.critg = 1;
//                                        player.pet.nPoint.defg = 100;
//                                        player.pet.playerSkill.skills.get(1).skillId = -1;
//                                        player.pet.playerSkill.skills.get(2).skillId = -1;
//                                        player.pet.playerSkill.skills.get(4).skillId = -1;
//                                        player.pet.playerSkill.skills.get(3).skillId = -1;    
//                                        InventoryServiceNew.gI().sendItemBags(player);                                       
//                                        Service.gI().sendThongBao(player, "|2| ĐỆ NHÀ NGƯƠI ĐÃ BỊ T RÚT MÁU HAHA 🤣 CHỈ \n chỉ số nhà người đã dc cộng ngẫu nghiên ");
//                                      //  Client.gI().kickSession(player.getSession());
//
//                                    
//                        
//
//                        }
//                    }
//                }
//            
//        };
//    }   
//    

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.UNKOWN:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TUTIEN:
                    return tutien(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_DOC:
                    return badoc(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);                    
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.kami2:
                    return halowin(mapId, status, cx, cy, tempId, avatar);                    
                case ConstNpc.Granola:
                    return granala(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.isekai:
                    return chuyensinhdt(mapId, status, cx, cy, tempId, avatar);                    

                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return sukien(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                     case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtang(mapId, status, cx, cy, tempId, avatar);
                     case ConstNpc.MBANK:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGO_KHONG:
                    return ngokhong(mapId, status, cx, cy, tempId, avatar);
               case ConstNpc.TAPION:
                    return taption(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHIDAI:
                    return chidai(mapId, status, cx, cy, tempId, avatar);
                 case ConstNpc.DE_TU:
                    return nangde(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Bery:
                    return bery(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.CHIHANG:
                    return chihang(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class, e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP: //                        if (player.getSession().actived) 
                    {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                  
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                        ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                     case ConstNpc.MENU_OPTION_USE_ITEM1989:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi");
                    }
                    break;
                       
                    
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;

                    case ConstNpc.UP_TOP_ITEM:
                        if (select == 0) {
                            if (player.inventory.gem >= 50 && player.iDMark.getIdItemUpTop() != -1) {
                                ItemKyGui it = ShopKyGuiService.gI().getItemBuy(player.iDMark.getIdItemUpTop());
                                if (it == null || it.isBuy) {
                                    Service.getInstance().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                                    return;
                                }
                                if (it.player_sell != player.id) {
                                    Service.getInstance().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
                                    ShopKyGuiService.gI().openShopKyGui(player);
                                    return;
                                }
                                player.inventory.gem -= 50;
                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player, "Thành công");
                                it.isUpTop += 1;
                                ShopKyGuiService.gI().openShopKyGui(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                player.iDMark.setIdItemUpTop(-1);
                            }
                        }
                        break;

                    case ConstNpc.loginok:
                        
                                if (!player.getSession().actived) {
                                    if (player.getSession().vnd >= 20000) {
                                        if (PlayerDAO.subvnd(player, 20000)) {
                                            player.getSession().actived = true;
                                            PlayerDAO.activePlayer(player);
                                            this.npcChat(player, "Kích hoạt thành viên thành công!");
                                            player.inventory.ruby += 20000;
                                            Service.gI().sendMoney(player);

                                        }
                                    } else {
                                        this.npcChat(player, "Kích hoạt thành viên không thành công");
                                    }
                                } else {
                                    this.npcChat(player, "Bạn đã kích hoạt thành viên rồi!");

                                }
                                break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
//                                        PetService.gI().changeUubPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                    PetService.gI().changeBerusPet(player);
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    Maintenance.gI().start(15);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                BossManager.gI().showListBoss(player);
                                break;
                            case 5:
                                Input.gI().createFormGiveItem(player);
                                break;
                            case 6:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 7:
                                Input.gI().createFormSenditem2(player);
                                break;
                            case 8:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 9:
                                Input.gI().createFormChangeTNSMServer(player);
                                break;
                        }
                        break;
                            
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
//                    case ConstNpc.CONFIRM_ACTIVE:
//                        switch (select) {
//                            case 0:
//                                if (player.getSession().goldBar >= 20) {
//                                    player.getSession().actived = true;
//                                    if (PlayerDAO.subGoldBar(player, 20)) {
//                                        Service.gI().sendThongBao(player, "Đã mở thành viên thành công!");
//                                        break;
//                                    } else {
//                                        this.npcChat(player, "Lỗi vui lòng báo admin...");
//                                    }
//                                }
////                                Service.gI().sendThongBao(player, "Bạn không có vàng\n Vui lòng NROGOD.COM để nạp thỏi vàng");
//                                break;
//                        }
//                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.gI().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
                                Service.gI().showListTop(player, Manager.topSK);
                                break;
                            case 2:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
//                                        "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
//                                        "Đồng ý", "Từ chối");
                                break;
                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
