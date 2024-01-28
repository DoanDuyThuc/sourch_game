package com.girlkun.services.func;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerNotify;
import com.girlkun.network.io.Message;
import com.girlkun.services.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.*;
import java.util.stream.Collectors;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;

    private static final int COST = 500000000;
    private static final int COSTV = 300000000;
    private static final int TIME_COMBINE = 1;
    private static final byte MAX_STAR_ITEM = 8;
    private static final byte MAX_LEVEL_ITEM = 8;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    
    public static final int CHE_TAO_TRANG_BI_TS = 520;
    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int NANG_CAP_LINH_THU = 512;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int PHAN_RA_DO_THAN_LINH = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int NANG_CAP_SKH_VIP = 516;
    public static final int NANG_CAP_MAT_THAN = 517;
    public static final int THANG_CAP_TRANG_BI = 10000;
    public static final int NANG_CAP_BONG_TAI_CAP3 = 518;
    public static final int MO_CHI_SO_BONG_TAI_CAP3 = 521;
    public static final int TIEN_HOA_CT = 555;
    public static final int NANG_VIP = 999;
    public static final int CHAN_MENH = 10001;

     
    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int RUBY_BONG_TAI = 50_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 45;
    public static final int NANG_CAP_DO_KICH_HOAT = 556;

    private final Npc baHatMit;
    private final Npc npsthiensu64;
    private final Npc whis;
    private final Npc chidai;
    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.NPC_64);
         this.whis = NpcManager.getNpc(ConstNpc.WHIS);
          this.chidai = NpcManager.getNpc(ConstNpc.CHIDAI);
    
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case NANG_CAP_DO_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 Món đồ Thần Linh bất kì", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    String npcSay = "|2|Con có muốn đổi món đồ Hủy Diệt để nhận lấy trang bị kích hoạt cùng hành tinh với trang bị đó ?\n|7|"
                            + "|1|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";

                    if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_DAP_DO_KICH_HOAT,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;                
                
                  case NANG_CAP_BONG_TAI_CAP3:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item mvbt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 1181) {
                            mvbt = item;
                        }
                    }
                    if (bongTai != null && mvbt != null && mvbt.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.rubyCombine = RUBY_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.rubyCombine + "Hồng ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2 và X9999 MVBT ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                           "Cần 1 Bông tai Porata cấp 2 và X9999 MVBT ", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI_CAP3:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item thachPhu = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1179) {
                            bongTai = item;
                        } else if (item.template.id == 1182) {
                            thachPhu = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && thachPhu != null && daXanhLam != null && thachPhu.quantity >= 999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.rubyCombine = RUBY_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.rubyCombine + "Hồng ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "1Cần 1 Bông tai Porata cấp 3, X999 Thạch Phù và 99 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "2Cần 1 Bông tai Porata cấp 3, X999 Thạch Phù và 99 Đá xanh lam", "Đóng");
                }
                break; 
                   case THANG_CAP_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item caitrang = null;
                    Item dangusac = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.type == 72
                                                   ) {
                            caitrang = item;
                        } else if (item.template.id == 1188) {
                            dangusac = item;
                        }
                    }
                    int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                    if (caitrang != null && dangusac != null &&  level < 8) {

                        player.combineNew.rubyCombine = getGemNangCaiTrang(level);;
                        player.combineNew.ratioCombine = getRatioNCCaiTrang(level);
                        
                        

                        String npcSay = "Nâng Cấp Linh Thú " + "\n|2|";
                        for (Item.ItemOption io : caitrang.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + getDaNangCap(level) + " Hồn Linh Thú";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.rubyCombine + "  hồng ngọc");
                        } else {
                            npcSay += "Cần " + getDaNangCap(level) + " Hồn Linh Thú ";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if ( level >= 8) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Đã Nâng Cấp Tối Đa  Linh Thú", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                             "Cần 1 Linh Thú , hồn Linh Thú ", "Đóng");
                }
                break;
                
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
            case NANG_CAP_LINH_THU:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item linhthu = null;
                    Item thangtinhthach = null;
                    Item thucan = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id >= 1666 && item.template.id <= 1667) {
                            linhthu = item;
                        } else if (item.template.id == 1200) {
                            thangtinhthach = item;
                        } else if (item.template.id >= 1201 && item.template.id <= 1202) {
                            thucan = item;
                        }
                    }
                    if (linhthu != null && thangtinhthach != null && thucan != null && thangtinhthach.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Linh Thú Siêu Cấp" + "\n|2|";
                        for (Item.ItemOption io : linhthu.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Linh Thú, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Linh Thú, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                }
                break;
                
             case CHE_TAO_TRANG_BI_TS:
                 if (player.combineNew.itemsCombine.size() == 0) {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "HuyHau", "Yes");
                    return;
                }
                  if (player.combineNew.itemsCombine.size() >= 2 &&  player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() &&  item.isCongThucVip()).count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 9999).count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
                        return;
                    }
                    Item mTS = null, daNC = null, daMM = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.isNotNullItem()) {
                                if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;    
                    int tilenew = tilemacdinh;
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;                     
                    }

                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ " 
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " +  mTS.quantity + "/9999\n";
                    if (daNC != null) {
                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap() 
                                  + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                  + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 50k Ngọc Hồng";
                    if (player.inventory.ruby < 50000) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không ngọc hồng", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n50 K Ngọc Hồng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;    
                
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getRUBYPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc ");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 16 && item.template.id <= 20) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 3 sao trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 3 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5 || item.template.type == 35).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
            case NANG_CAP_MAT_THAN:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1665 || item.template.id == 1666 || item.template.id == 1667).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1170 || item.template.id == 1171 || item.template.id == 1172).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1173).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 1173) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type == 35) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 77 || io.optionTemplate.id == 50
                                        || io.optionTemplate.id == 103) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s Khóa Thạch", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " khóa thạch");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "by huyhau đại đế", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
                case TIEN_HOA_CT:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item caitrang = null;
                    Item dangusac = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.type == 5
                                                   ) {
                            caitrang = item;
                        } else if (item.template.id == 1992) {
                            dangusac = item;
                        }
                    }
                    int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                    if (caitrang != null && dangusac != null &&  level < 8) {

                        player.combineNew.rubyCombine = getGemNangCaiTrang(level);;
                        player.combineNew.ratioCombine = getRatioNCCaiTrang(level);
                        
                        

                        String npcSay = "Tiến Hóa cải Trang " + "\n|2|";
                        for (Item.ItemOption io : caitrang.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + getDaNangCap(level) + " Đá Tiến Hóa";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.rubyCombine + "  hồng ngọc");
                        } else {
                            npcSay += "Cần " + getDaNangCap(level) + " Đá Tiến Hóa ";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if ( level >= 8) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cải trang tiến hóa MAX", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                             "Cần 1 Cải Trang, đá tiến hóa ", "Đóng");
                }
                break;
                case NANG_VIP:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item caitrang = null;
                    Item dangusac = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1555   || item.template.id == 1557   || item.template.id == 1556  || item.template.id == 1558  ) {
                            caitrang = item;
                        } else if (item.template.id == 1191) {
                            dangusac = item;
                        }
                    }
                    int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                    if (caitrang != null && dangusac != null &&  level < 8) {

                        player.combineNew.rubyCombine = getGemNangCaiTrang(level);;
                        player.combineNew.ratioCombine = getRatioNCCaiTrang(level);
                        
                        

                        String npcSay = "Nâng Cấp item vip " + "\n|2|";
                        for (Item.ItemOption io : caitrang.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + getDaNangCap(level) + " rượu";
                            chidai.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.rubyCombine + "  hồng ngọc");
                        } else {
                            npcSay += "Cần " + getDaNangCap(level) + " Rượu ";
                            chidai.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if ( level >= 8) {
                        this.chidai.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Vip Max", "Đóng");
                    }
                } else {
                    this.chidai.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                             "Cần 1  Item Vip , rượu ", "Đóng");
                }
                break;
            case PHAN_RA_DO_THAN_LINH:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rải vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : " + couponAdd + " Điểm\n"
                            + (1000000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(1000000000) + " vàng";

                    if (player.inventory.gold < 1000000000) {
                        this.baHatMit.npcChat(player, "Không Đủ Vàng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(1000000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã 1 lần 1 món đồ thần linh", "Đóng");
                }
                break;
            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món Hủy Diệt bất kì và 1 món Thần Linh cùng loại", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " thiên sứ tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không Đủ Vàng", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TS,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case CHAN_MENH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item  ngocboi1 = null, DaKhac = null; 
                    
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id == 1203) {
                                DaKhac = item;
                            } else if (item.template.id == 1204
                                    || item.template.id == 1205
                                    || item.template.id == 1206
                                    || item.template.id == 1207
                                    || item.template.id == 1208
                                    || item.template.id == 1209
                                     || item.template.id == 1210) {
                                ngocboi1 = item;                                
                            }  
                        }
                    }                    
                    int level1_1 = 0;
                    int level1_2 = 0;
                    int level1_3 = 0;
                    int level1_4 = 0;
                Item.ItemOption optionLevel_72 = null;
                
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 50 ) {
                        level1_1 = io.param;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 77) {
                        level1_2 = io.param;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id ==103) {
                        level1_3 = io.param;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id ==5) {
                        level1_4 = io.param;
                        break;
                    }
                }

                
                    if ( ngocboi1 == null || DaKhac == null  ){
                        this.chidai.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần Ngọc Bội và 1 Đá Tiến Hóa Ngọc Bội", "Đóng");
                    }
                    if (player.inventory.ruby < 500000 || player.inventory.gold < 50_000_000 ) {
                    Service.getInstance().sendThongBao(player, "Chuẩn bị đủ 500K Hồng Ngọc và 50 Tr Vàng hãy đến tìm ta");
                        return;
                    }
                    else if (ngocboi1 != null && DaKhac != null ) {
                        String npcSay = "|6|" + ngocboi1.template.name + "\n";
                        for (Item.ItemOption io : ngocboi1.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        npcSay += "Ngươi có muốn Tiến Hóa Ngọc Bội không?\n";
                        if (player.inventory.gold >= 50_000_000 && player.inventory.ruby >= 500000 ) {
                            this.chidai.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Tiến Hóa\n Ngọc Bội");
                        } else {
                            this.chidai.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Chuẩn bị đủ tiền rồi hãy gặp ta!!!");
                        }
                    } else {
                        this.chidai.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần Ngọc Bội + 10 và 1 Đá Tiến Hóa Ngọc Bội", "Đóng");
                    }
                }
                break;    
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thiên sứ và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 2) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ kích hoạt ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Khoong đủ vàng", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player, int type) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case CHUYEN_HOA_TRANG_BI:

                break;
            case NANG_CAP_DO_KICH_HOAT:
                dapDoKichHoat(player);
                break;                
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
             case THANG_CAP_TRANG_BI:
                ThangCapTrangBi(player);
                break;
            case PHAN_RA_DO_THAN_LINH:
                phanradothanlinh(player);
                break;
            case NANG_CAP_DO_TS:
                openDTS(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break;
              case TIEN_HOA_CT:
                TienHoaCt(player);
                break;
             case NANG_VIP:
                NangVip(player);
                break;
            case NANG_CAP_LINH_THU:
                moChiSolinhthu(player);
                break;
            case NANG_CAP_MAT_THAN:
                moChiSomatThan(player);
                break;
            case CHAN_MENH:
                ChanMenh(player);
                break;
            case NANG_CAP_BONG_TAI_CAP3:
                nangCapBongTaicap3(player);
                break;
            case MO_CHI_SO_BONG_TAI_CAP3:
                moChiSoBongTaicap3(player);
                break;
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 13000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(11000, 14000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv2(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 20));//sd
        item.itemOptions.add(new ItemOption(77, 20));//hp
        item.itemOptions.add(new ItemOption(103, 20));//ki
        item.itemOptions.add(new ItemOption(14, 20));//cm
        item.itemOptions.add(new ItemOption(5, 20));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(34, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv3(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 40));//sd
        item.itemOptions.add(new ItemOption(77, 40));//hp
        item.itemOptions.add(new ItemOption(103, 40));//ki
        item.itemOptions.add(new ItemOption(14, 40));//cm
        item.itemOptions.add(new ItemOption(5, 40));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(35, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv4(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 60));//sd
        item.itemOptions.add(new ItemOption(77, 60));//hp
        item.itemOptions.add(new ItemOption(103, 60));//ki
        item.itemOptions.add(new ItemOption(14, 60));//cm
        item.itemOptions.add(new ItemOption(5, 60));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(36, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv5(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 80));//sd
        item.itemOptions.add(new ItemOption(77, 80));//hp
        item.itemOptions.add(new ItemOption(103, 80));//ki
        item.itemOptions.add(new ItemOption(14, 80));//cm
        item.itemOptions.add(new ItemOption(5, 80));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(36, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void doiKiemThan(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiChuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhNhua = player.combineNew.itemsCombine.get(0);
            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2016);
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

     public void openCreateItemAngel(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 9999).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
            return;
        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isManhTS()) {
                        mTS = item;
                    } else if (item.isDaNangCap()) {
                        daNC = item;
                    } else if (item.isDaMayMan()) {
                        daMM = item;
                    } else if (item.isCongThucVip()) {
                        CtVip = item;
                    }
                }
            }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 ) {//check chỗ trống hành trang
            if (player.inventory.ruby < 50000) {
                Service.getInstance().sendThongBao(player, "Không đủ Ngọc Hồng");
                return;
            }
                    player.inventory.ruby -= 50000;
                    
                    int tilemacdinh = 35;
                    int tileLucky = 20;
                    if (daNC != null) {
                        tilemacdinh += (daNC.template.id - 1073)*10;
                    } else {
                        tilemacdinh = tilemacdinh;
                    }
                    if (daMM != null) {
                        tileLucky += tileLucky*(daMM.template.id - 1078)*10/100;
                    } else {
                        tileLucky = tileLucky;
                    }
                    if (Util.nextInt(0, 100) < tilemacdinh) {
                        Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                        if (daNC != null) {
                        Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                        }
                        if (daMM != null) {
                        Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                        }
                        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 9999).findFirst().get();
                        
                        tilemacdinh = Util.nextInt(0, 50);
                        if (tilemacdinh == 49) { tilemacdinh = 20;}
                        else if (tilemacdinh == 48 || tilemacdinh == 47) { tilemacdinh = 19;}
                        else if (tilemacdinh == 46 || tilemacdinh == 45) { tilemacdinh = 18;}
                        else if (tilemacdinh == 44 || tilemacdinh == 43) { tilemacdinh = 17;}
                        else if (tilemacdinh == 42 || tilemacdinh == 41) { tilemacdinh = 16;}
                        else if (tilemacdinh == 40 || tilemacdinh == 39) { tilemacdinh = 15;}
                        else if (tilemacdinh == 38 || tilemacdinh == 37) { tilemacdinh = 14;}
                        else if (tilemacdinh == 36 || tilemacdinh == 35) { tilemacdinh = 13;}
                        else if (tilemacdinh == 34 || tilemacdinh == 33) { tilemacdinh = 12;}
                        else if (tilemacdinh == 32 || tilemacdinh == 31) { tilemacdinh = 11;}
                        else if (tilemacdinh == 30 || tilemacdinh == 29) { tilemacdinh = 10;}
                        else if (tilemacdinh <= 28 || tilemacdinh >= 26) { tilemacdinh = 9;}
                        else if (tilemacdinh <= 25 || tilemacdinh >= 23) { tilemacdinh = 8;}
                        else if (tilemacdinh <= 22 || tilemacdinh >= 20) { tilemacdinh = 7;}
                        else if (tilemacdinh <= 19 || tilemacdinh >= 17) { tilemacdinh = 6;}
                        else if (tilemacdinh <= 16 || tilemacdinh >= 14) { tilemacdinh = 5;}
                        else if (tilemacdinh <= 13 || tilemacdinh >= 11) { tilemacdinh = 4;}
                        else if (tilemacdinh <= 10 || tilemacdinh >= 8) { tilemacdinh = 3;}
                        else if (tilemacdinh <= 7 || tilemacdinh >= 5) { tilemacdinh = 2;}
                        else if (tilemacdinh <= 4 || tilemacdinh >= 2) { tilemacdinh = 1;}
                        else if (tilemacdinh <= 1) { tilemacdinh = 0;}
                        short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                        Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh()], itemCtVip.template.gender);
                        
                        tilemacdinh += 10;
                        
                        if (tilemacdinh > 0) {
                            for(byte i = 0; i < itemTS.itemOptions.size(); i++) {
                            if(itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                                itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param*tilemacdinh/100);
                            }
                        }
                    }
                        tilemacdinh = Util.nextInt(0, 100);
                        
                        if (tilemacdinh <= tileLucky) {
                        if (tilemacdinh >= (tileLucky - 3)) {
                            tileLucky = 3;
                        } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                            tileLucky = 2;
                        } else { tileLucky = 1; }
                        itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                        ArrayList<Integer> listOptionBonus = new ArrayList<>();
                        listOptionBonus.add(50); 
                        listOptionBonus.add(77); 
                        listOptionBonus.add(103); 
                        listOptionBonus.add(98);
                        listOptionBonus.add(99);
                        for (int i = 0; i < tileLucky; i++) {
                            tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                            itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                            listOptionBonus.remove(tilemacdinh);
                        }
                    }
                        
                        InventoryServiceNew.gI().addItemBag(player, itemTS);
                        sendEffectSuccessCombine(player);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    if (mTS != null && daMM != null && daNC != null && CtVip != null ) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    } else if (CtVip != null && mTS != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                    } else if (CtVip != null && mTS != null && daNC != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                    } else if (CtVip != null && mTS != null && daMM != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    }
                    
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }


    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }

            if (nr1s != null && doThan != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new Item.ItemOption(30, 0));
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void phanradothanlinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.gold -= 500000000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
            sendEffectSuccessCombine(player);
            player.inventory.coupon += couponAdd;
            this.baHatMit.npcChat(player, "Con đã nhận được " + couponAdd + " điểm");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        // new update 2 mon huy diet + 1 mon than linh(skh theo style) +  5 manh bat ki
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.gold < COST) {
            Service.gI().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().get();
        List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).findFirst().get();

        player.inventory.gold -= COST;
        sendEffectSuccessCombine(player);
        short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

        Item itemTS = ItemService.gI().DoThienSu(itemIds[itemTL.template.gender > 2 ? player.gender : itemTL.template.gender][itemManh.typeIdManh()], itemTL.template.gender);
        InventoryServiceNew.gI().addItemBag(player, itemTS);

        InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 5);
        itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        player.combineNew.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    public void openSKHVIP(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHVip[Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVip[6];
                }
            } else {
                itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
    private void dapDoKichHoat2(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hãy chuẩn bị ít nhất 1 ô trống trong hành trang");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.gI().sendThongBao(player, "Cần 1 trang bị Hủy Diệt và 5 đá ngũ sắc và 500tr vàng");
            return;
        }
        if (player.inventory.gold < 500000000) {
            Service.gI().sendThongBao(player, "Cần 1 trang bị Hủy Diệt và 5 đá ngũ sắc và 500tr vàng");
            return;
        }
        Item item1 = player.combineNew.itemsCombine.get(0);
        Item item2 = player.combineNew.itemsCombine.get(1);
        boolean flag1 = false;
        boolean flag2 = false;
        if (UpdateItem.isIDHuyDiet(item1)) {
            flag1 = true;
        }
        if (item2.template.id == 674 && item2.quantity >= 2) {
            flag2 = true;
        }
        if (!flag1 || !flag2) {
            Service.gI().sendThongBao(player, "Cần 1 trang bị Hủy Diệt và 5 đá ngũ sắc và 500tr vàng");
            return;
        }
        player.inventory.gold -= 500000000;
        UpdateItem.CreateSKH(player, item1.template.gender, item1.template.type, item1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 5);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }   
    

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dtl != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dhd, 1);
                    if (dtl != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().addItemBag(player, ticket);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(921);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item thangtinhthach = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    linhthu = item;
                } else if (item.template.id == 934) {
                    thangtinhthach = item;
                } else if (item.template.id == 935) {
                    thucan = item;
                }
            }
            if (linhthu != null && thangtinhthach != null && thangtinhthach.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thucan, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.clear();
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
      

    private void moChiSolinhthu(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item manhHon = null;
            Item daXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 1665 && item.template.id <= 1667) {
                    linhthu = item;
                } else if (item.template.id == 1200) {
                    manhHon = item;
                } else if (item.template.id >= 1201 && item.template.id <= 1202) {
                    daXanhLam = item;
                }
            }
            if (linhthu != null && manhHon != null && manhHon.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhHon, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daXanhLam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 25)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 25)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 25)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(15, 25)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(15, 25)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(15, 25)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(15, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc hồng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 16 && item.template.id <= 20) && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
                }
            }
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
                        if (optionLevel != null && optionLevel.param >= 5) {
                            //        ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                            //                  + "thành công " +  trangbi.template.name + " lên +" + optionLevel.param );
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
      
    private void ThangCapTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.rubyCombine;
            if (player.inventory.ruby < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item caitrang = null;
            Item dangusac = null;
 
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.type == 72
                          ) {
                    caitrang = item;
                } else if (item.template.id == 1188) {
                    dangusac = item;
                }
            }
            int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
            int hp = 0;
                Item.ItemOption optionhp = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 77) {
                        hp = io.param;
                        optionhp = io;
                        break;
                    }
                }
            int ki = 0;
                Item.ItemOption optionki = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 103) {
                        ki = io.param;
                        optionki = io;
                        break;
                    }
                }
            int sd = 0;
                Item.ItemOption optionsd = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 50) {
                        sd = io.param;
                        optionsd = io;
                        break;
                    }
                }
             int crit = 0;
                Item.ItemOption optioncrit = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 14) {
                        crit = io.param;
                        optioncrit = io;
                        break;
                    }
                }
            int sdcrit = 0;
                Item.ItemOption optionsdcrit = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 5) {
                        sdcrit = io.param;
                        optionsdcrit = io;
                        break;
                    }
                }
            if (caitrang.template.type == 72 && dangusac != null && level < 8 && dangusac.quantity >= getDaNangCap(level) ) {
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, dangusac, getDaNangCap(level));
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
 
                    if (optionLevel == null) {
                       caitrang.itemOptions.add(new Item.ItemOption(72,1));
                   } else  {
                       optionLevel.param++;
                   }
                        optionhp.param += Util.nextInt(5,  6);
                        optionki.param += Util.nextInt(5, 6);
                        optionsd.param += Util.nextInt(5, 6);
                        optioncrit.param += 1;
                        optionsdcrit.param += Util.nextInt(2, 3);                  
                    sendEffectSuccessCombine(player);
                }                   
                else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
                 
            }
         
            else{
                Service.gI().sendThongBao(player, "Không đủ Hồn Linh Thú");
                return;
            }
        
    }}
         private void TienHoaCt(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.rubyCombine;
            if (player.inventory.ruby < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item caitrang = null;
            Item dangusac = null;
 
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.type == 5
                          ) {
                    caitrang = item;
                } else if (item.template.id == 1992) {
                    dangusac = item;
                }
            }
            int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
            int hp = 0;
                Item.ItemOption optionhp = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 77) {
                        hp = io.param;
                        optionhp = io;
                        break;
                    }
                }
            int ki = 0;
                Item.ItemOption optionki = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 103) {
                        ki = io.param;
                        optionki = io;
                        break;
                    }
                }
            int sd = 0;
                Item.ItemOption optionsd = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 50) {
                        sd = io.param;
                        optionsd = io;
                        break;
                    }
                }
             int crit = 0;
                Item.ItemOption optioncrit = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 14) {
                        crit = io.param;
                        optioncrit = io;
                        break;
                    }
                }
            int sdcrit = 0;
                Item.ItemOption optionsdcrit = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 5) {
                        sdcrit = io.param;
                        optionsdcrit = io;
                        break;
                    }
                }
            if (caitrang.template.type == 5 && dangusac != null && level < 8 && dangusac.quantity >= getDaNangCap(level) ) {
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, dangusac, getDaNangCap(level));
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
 
                    if (optionLevel == null) {
                       caitrang.itemOptions.add(new Item.ItemOption(72,1));
                   } else  {
                       optionLevel.param++;
                   }
                        optionhp.param += Util.nextInt(5,  6);
                        optionki.param += Util.nextInt(5, 6);
                        optionsd.param += Util.nextInt(5, 6);
//                        optioncrit.param += 1;
//                        optionsdcrit.param += Util.nextInt(2, 3);                  
                    sendEffectSuccessCombine(player);
                }                   
                else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
                 
            }
         
            else{
                Service.gI().sendThongBao(player, "Không đủ đá tiến Hóa");
                return;
            }
        
    }}
         
          private void NangVip(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.rubyCombine;
            if (player.inventory.ruby < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item caitrang = null;
            Item dangusac = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1555
                        || item.template.id == 1557
                          || item.template.id == 1558
                          || item.template.id == 1556
                          ) {
                    caitrang = item;
                } else if (item.template.id == 1191) {
                    dangusac = item;
                                 }
            }
            int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
            int hp = 0;
                Item.ItemOption optionhp = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 0) {
                        hp = io.param;
                        optionhp = io;
                        break;
                    }
                }
            int ki = 0;
                Item.ItemOption optionki = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 6) {
                        ki = io.param;
                        optionki = io;
                        break;
                    }
                }
            int sd = 0;
                Item.ItemOption optionsd = null;
                for (Item.ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 7) {
                        sd = io.param;
                        optionsd = io;
                        break;
                    }
                }
             
            if (caitrang.template.id == 1555 || caitrang.template.id == 1558   || caitrang.template.id == 1557   || caitrang.template.id == 1556 && dangusac != null  && level < 8 && dangusac.quantity >= getDaNangCap(level)) {
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, dangusac, getDaNangCap(level) );
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
 
                    if (optionLevel == null) {
                       caitrang.itemOptions.add(new Item.ItemOption(72,1));
                   } else  {
                       optionLevel.param++;
                   }
                        optionhp.param += Util.nextInt(150,  1266);
                        optionki.param += Util.nextInt(105, 8556);
                        optionsd.param += Util.nextInt(105, 8556);
                    sendEffectSuccessCombine(player);
                }                   
                else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
                 
            }
         
            else{
                Service.gI().sendThongBao(player, "Không đủ Rượu Quy");
                return;
            }
        
    }}

    private void moChiSomatThan(Player player) {

        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1665 || item.template.id == 1666 || item.template.id == 1667).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1170 || item.template.id == 1171 || item.template.id == 1172).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1173).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 1173) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type == 35) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }

            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 77
                                || io.optionTemplate.id == 50
                                || io.optionTemplate.id == 103) {
                            option = io;

                        }
                        if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                            option.param += (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param += (option2.param * 10 / 100);
                            }
                            if (optionLevel == null) {
                                itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                            } else {
                                optionLevel.param++;
                            }
                            if (optionLevel != null && optionLevel.param >= 5) {
                                //        ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                                //                  + "thành công " +  trangbi.template.name + " lên +" + optionLevel.param );
                            }
                            sendEffectSuccessCombine(player);
                        } else {
                            if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                                option.param -= (option.param * 10 / 100);
                                if (option2 != null) {
                                    option2.param -= (option2.param * 10 / 100);
                                }
                                optionLevel.param--;
                            }
                            sendEffectFailCombine(player);
                        }
                        if (player.combineNew.itemsCombine.size() == 3) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                        }
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                }
            }
        }
    }
    
      private void nangCapBongTaicap3(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.rubyCombine;
            if (player.inventory.ruby < gem) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongTai = item;
                } else if (item.template.id == 1181) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999);
                if (Util.isTrue(player.combineNew.ratioCombine, 50)) {
                    bongTai.template = ItemService.gI().getTemplate(1179);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 3));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
  

    private void moChiSoBongTaicap3(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.rubyCombine;
            if (player.inventory.ruby < gem) {
                Service.gI().sendThongBao(player, "Không đủ Hồng ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item thachPhu = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1179) {
                    bongtai = item;
                } else if (item.template.id == 1182) {
                    thachPhu = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && thachPhu != null && thachPhu.quantity >= 999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thachPhu, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 50)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
      private void ChanMenh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item ngocboi1 = null; 
            Item DaKhac = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isNotNullItem()) {
                            if (item.template.id == 1203) {
                                DaKhac = item;
                            } else if (item.template.id == 1204
                                    || item.template.id == 1205
                                    || item.template.id == 1206
                                    || item.template.id == 1207
                                    || item.template.id == 1208
                                    || item.template.id == 1209
                                     || item.template.id == 1210) {
                                ngocboi1 = item;                                
                            }  
                        }
                    }
                }

                    int level1_1 = 0;
                    int level1_2 = 0;
                    int level1_3 = 0;
                    int level1_4 = 0;
                    int level1_102 = 0;
                    int level1_219 = 0;
                    int level1_220 = 0;
                    int level1_221 = 0;
                    int level1_222 = 0;
                    int level1_223 = 0;
                    int level1_107 = 0;
                    

                Item.ItemOption optionLevel_5 = null;
                Item.ItemOption optionLevel_50 = null;
                Item.ItemOption optionLevel_77 = null;
                Item.ItemOption optionLevel_103 = null;
                Item.ItemOption optionLevel_102 = null;
                Item.ItemOption optionLevel_219 = null;
                Item.ItemOption optionLevel_220 = null;
                Item.ItemOption optionLevel_221 = null;
                Item.ItemOption optionLevel_222 = null;
                Item.ItemOption optionLevel_223 = null;
                Item.ItemOption optionLevel_107 = null;
                
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 102 ) {
                        level1_102 = io.param;
                        optionLevel_102 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 107 ) {
                        level1_107 = io.param;
                        optionLevel_107 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 213 ) {
                        level1_219 = io.param;
                        optionLevel_219 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 212 ) {
                        level1_220 = io.param;
                        optionLevel_220 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 214 ) {
                        level1_223 = io.param;
                        optionLevel_223 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 215 ) {
                        level1_221 = io.param;
                        optionLevel_221 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 216 ) {
                        level1_222 = io.param;
                        optionLevel_222 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 50 ) {
                        level1_1 = io.param;
                        optionLevel_50 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id == 77) {
                        level1_2 = io.param;
                        optionLevel_77 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id ==103) {
                        level1_3 = io.param;
                        optionLevel_103 = io;
                        break;
                    }
                }
                for (Item.ItemOption io : ngocboi1.itemOptions) {
                    if (io.optionTemplate.id ==5) {
                        level1_4 = io.param;
                        optionLevel_5 = io;
                        break;
                    }
                }

                    
                    if (ngocboi1 == null || DaKhac == null) {
                        Service.getInstance().sendThongBao(player, "Không đủ vật phẩm ");
                        return;                  
                    }
                    if (Util.isTrue(100, 100)) {
                        if (player.inventory.ruby > 500000 || player.inventory.gold > 50_000_000 ){
                     
                    if (ngocboi1.template.id == 1204 && DaKhac.quantity >=1000) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 1000);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1205);
                        item.itemOptions.add(new Item.ItemOption(50, level1_1 + Util.nextInt (10,20)));
                        item.itemOptions.add(new Item.ItemOption(77, level1_2 + Util.nextInt (10,20)));
                        item.itemOptions.add(new Item.ItemOption(103, level1_3 + Util.nextInt (10,20)));
                        item.itemOptions.add(new Item.ItemOption(5, level1_4 + Util.nextInt (1,10)));
                        item.itemOptions.add(new Item.ItemOption(107, level1_107));
                        item.itemOptions.add(new Item.ItemOption(102, level1_102));
                        
                        if (optionLevel_223 != null){
                        item.itemOptions.add(new Item.ItemOption(212, level1_223));
                        item.itemOptions.add(new Item.ItemOption(213, level1_219));
                        item.itemOptions.add(new Item.ItemOption(214, level1_220));
                        item.itemOptions.add(new Item.ItemOption(215, level1_221));
                        item.itemOptions.add(new Item.ItemOption(216, level1_222));
                        
                        }
                        
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");
                        player.inventory.ruby -= 500000;
                        player.inventory.gold -= 50_000_000;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                    }
                    
                    if (ngocboi1.template.id == 1205  && DaKhac.quantity >=2000) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 2000);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1206);
                        item.itemOptions.add(new Item.ItemOption(50, level1_1 + Util.nextInt (20,30)));
                        item.itemOptions.add(new Item.ItemOption(77, level1_2 + Util.nextInt (20,30)));
                        item.itemOptions.add(new Item.ItemOption(103, level1_3 + Util.nextInt (20,30)));
                        item.itemOptions.add(new Item.ItemOption(5, level1_4 + Util.nextInt (5,10)));
                        item.itemOptions.add(new Item.ItemOption(107, level1_107));
                        item.itemOptions.add(new Item.ItemOption(102, level1_102));
                        
                        if (optionLevel_223 != null){
                        item.itemOptions.add(new Item.ItemOption(212, level1_223));
                        item.itemOptions.add(new Item.ItemOption(213, level1_219));
                        item.itemOptions.add(new Item.ItemOption(214, level1_220));
                        item.itemOptions.add(new Item.ItemOption(215, level1_221));
                        item.itemOptions.add(new Item.ItemOption(216, level1_222));
                        }
                     
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");
                        player.inventory.ruby -= 500000;
                        player.inventory.gold -= 50_000_000;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                    }
                    
                    if (ngocboi1.template.id == 1206 &&  DaKhac.quantity >=3000) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 3000);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1207);
                        item.itemOptions.add(new Item.ItemOption(50, level1_1 + Util.nextInt (20,35)));
                        item.itemOptions.add(new Item.ItemOption(77, level1_2 + Util.nextInt (20,35)));
                        item.itemOptions.add(new Item.ItemOption(103, level1_3 + Util.nextInt (20,35)));
                        item.itemOptions.add(new Item.ItemOption(5, level1_4 + Util.nextInt (5,10)));
                        item.itemOptions.add(new Item.ItemOption(107, level1_107));
                        item.itemOptions.add(new Item.ItemOption(102, level1_102));
                        
                        if (optionLevel_223 != null){
                        item.itemOptions.add(new Item.ItemOption(212, level1_223));
                        item.itemOptions.add(new Item.ItemOption(213, level1_219));
                        item.itemOptions.add(new Item.ItemOption(214, level1_220));
                        item.itemOptions.add(new Item.ItemOption(215, level1_221));
                        item.itemOptions.add(new Item.ItemOption(216, level1_222));
                        }
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");
                        player.inventory.ruby -= 500000;
                        player.inventory.gold -= 50_000_000;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                    }
                    
                    if (ngocboi1.template.id == 1207  && DaKhac.quantity >=4000) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 4000);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1208);
                        item.itemOptions.add(new Item.ItemOption(50, level1_1 + Util.nextInt (35,45)));
                        item.itemOptions.add(new Item.ItemOption(77, level1_2 +  Util.nextInt (35,45)));
                        item.itemOptions.add(new Item.ItemOption(103, level1_3 +  Util.nextInt (35,45)));
                        item.itemOptions.add(new Item.ItemOption(5, level1_4 +  Util.nextInt (5,15)));
                        item.itemOptions.add(new Item.ItemOption(107, level1_107));
                        item.itemOptions.add(new Item.ItemOption(102, level1_102));
                        item.itemOptions.add(new Item.ItemOption(212, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(213, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(214, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(215, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(216, Util.nextInt (2,10)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa  chân Mệnh thành công");
                        player.inventory.ruby -= 500000;
                        player.inventory.gold -= 50_000_000_000L;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                    }
                    
                    if (ngocboi1.template.id == 1208  && DaKhac.quantity >=5000) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 5000);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1209);
                        item.itemOptions.add(new Item.ItemOption(50, level1_1 +  Util.nextInt (35,45)));
                        item.itemOptions.add(new Item.ItemOption(77, level1_2 +  Util.nextInt (35,45)));
                        item.itemOptions.add(new Item.ItemOption(103, level1_3 +  Util.nextInt (35,45)));
                        item.itemOptions.add(new Item.ItemOption(5, level1_4 +  Util.nextInt (15,25)));
                        item.itemOptions.add(new Item.ItemOption(107, level1_107));
                        item.itemOptions.add(new Item.ItemOption(102, level1_102));
                        item.itemOptions.add(new Item.ItemOption(212, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(213, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(214, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(215, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(216, Util.nextInt (2,10)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");
                        player.inventory.ruby -= 500000;
                        player.inventory.gold -= 50_000_000_000L;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                    } 
                   
                     if (ngocboi1.template.id == 1209  && DaKhac.quantity >=15000) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 15000);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1210);
                        item.itemOptions.add(new Item.ItemOption(50, level1_1 +  Util.nextInt (35,75)));
                        item.itemOptions.add(new Item.ItemOption(77, level1_2 +  Util.nextInt (35,75)));
                        item.itemOptions.add(new Item.ItemOption(103, level1_3 +  Util.nextInt (35,75)));
                        item.itemOptions.add(new Item.ItemOption(5, level1_4 +  Util.nextInt (35,75)));
                        item.itemOptions.add(new Item.ItemOption(107, level1_107));
                        item.itemOptions.add(new Item.ItemOption(102, level1_102));
                        item.itemOptions.add(new Item.ItemOption(212,  Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(213, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(214, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(215, Util.nextInt (2,10)));
                        item.itemOptions.add(new Item.ItemOption(216, Util.nextInt (2,10)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");
                        player.inventory.ruby -= 500000;
                        player.inventory.gold -= 50_000_000_000L;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                    } 
                     
                        }else {
                    Service.getInstance().sendThongBao(player, "Không đủ điều kiện để Tiến Hóa Ngọc Bội");
                    return;
                     }
                    }
        }
    }


    //--------------------------------------------------------------------------
    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 20000000;
            case 2:
                return 35000000;
            case 3:
                return 50000000;
            case 4:
                return 75000000;
            case 5:
                return 90000000;
            case 6:
                return 115000000;
            case 7:
                return 150000000;
            case 8:
                return 200000000;
//            case 9:
//                return 300000000;
//            case 10:
//                return 450000000;
//            case 11:
//                return 500000000;
//            case 12:
//                return 650000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) { //tile dap do chi hat mit
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 40f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 18f;
            case 5:
                return 10f;
            case 6:
                return 5f;
            case 7:
                return 2f;
            case 8:
                return 0.5f;
//            case 9:
//                return 100f;
//            case 10:
//                return 100f;
//            case 11:
//                return 90f;
//            case 12:
//                return 90f;
//                 case 13:
//                return 90f;
//            case 14:
//                return 90f;
//            case 15:
//                return 90f;
//            case 16:
//                return 10f;
//            case 17:
//                return 10f;
//            case 18:
//                return 10f;
//            case 19:
//                return 10f;
//            case 120:
//                return 10f;
        }

        return 0;
    }

    private int getRUBYPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 20;
            case 1:
                return 30;
            case 2:
                return 40;
            case 3:
                return 50;
            case 4:
                return 60;
            case 5:
                return 70;
            case 6:
                return 80;
            case 7:
                return 90;
            case 8:
                return 100;
//            case 9:
//                return 150;
//            case 10:
//                return 150;
//            case 11:
//                return 150;
//            case 12:
//                return 150;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 5;
            case 6:
                return 1;
            case 7: // 7 sao
                return 1;
            case 8:
                return 5;
            case 9:
                return 1;
            case 10: // 7 sao
                return 0.3;
            case 11: // 7 sao
                return 1;
            case 12: // 7 sao
                return 1;
        }
        return 0;
    }
     private double gettilelinhthu(int level) {
        switch (level) {
            case 0:
                return 20;
            case 1:
                return 10;
            case 2:
                return 5;
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return 2;
            case 6:
                return 1;
            case 7: // 7 sao
                return 1;
            case 8:
               return 1;
          }
        return 0;
    }
    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
        }
        return 0;
    }
      private float getRatioNCCaiTrang(int level) { //Tỉ lệ nâng cấp cải trang
        switch (level) {
            case 0:
                return 40f;
            case 1:
                return 15f;
            case 2:
                return 10f;
            case 3:
                return 10f;
            case 4:
                return 10f;
            case 5:
                return 5f;
            case 6:
                return 5f;
            case 7:
                return 2f;
            case 8:
                return 2f;    
            case 9:
                return 1f;    
            case 10:
                return 0.5f;
            case 11:
                return 0.6f;
             case 12:
                return 0.4f;
             case 13:
                return 0.3f;
             case 14:
                return 0.2f;
             case 15:
                return 0.1f;
        }               
        return 0;
    }
        private int getDaNangCap(int level) {
        switch (level) {
            case 0:
                return 50;
            case 1:
                return 100;
            case 2:
                return 110;
            case 3:
                return 120 ;
            case 4:
                return 130;
            case 5:
                return 140;
            case 6:
                return 150;
            case 7:
                return 160;
            case 8:
                return 170;
            case 9:
                return 180;
            case 10:
                return 190;
            case 11:
                return 200;
            case 12:
                return 210;
        }
        return 0;
    }
    private int getGemNangCaiTrang(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 20000;
            case 2:
                return 30000;
            case 3:
                return 40000;
            case 4:
                return 50000;
            case 5:
                return 60000;
            case 6:
                return 70000;
            case 7:
                return 80000;
            case 8:
                return 90000;    
            case 9:
                return 100000;    
            case 10:
                return 110000;
            case 11:
                return 120000;
             case 12:
                return 130000; 
            case 13:
                return 140000;    
            case 14:
                return 150000;    
            case 15:
                return 160000;
        }
        return 0;
    }


    private int getCountDaBaoVe(int level) {

        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
        }
        return 0;
    }
       private int getruby(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 20000;
            case 2:
                return 30000;
            case 3:
                return 150000;
            case 4:
                return 170000;
            case 5:
                return 230000;
            case 6:
                return 100000;
            case 7:
                return 250000;
        }
        return 0;
    }
    

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5  || item1.template.id == 72 || item1.template.id == 1665 || item1.template.id == 1666 || item1.template.id == 1667) {
                trangBi = item1;
            } else if (item1.template.type == 14 || item1.template.id == 1170 || item1.template.id == 1171 || item1.template.id == 1172 || item1.template.id == 1188) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5 || item2.template.id == 1665 || item2.template.type == 72 || item2.template.id == 1666 || item2.template.id == 1667) {
                trangBi = item2;
            } else if (item2.template.type == 14 || item2.template.id == 1170 || item2.template.id == 1171 || item2.template.id == 1172|| item2.template.id == 1188) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else if (trangBi.template.id == 1665 && daNangCap.template.id == 1170) {
                return true;
            } else if (trangBi.template.id == 1666 && daNangCap.template.id == 1171) {
                return true;
            } else if (trangBi.template.id == 1667 && daNangCap.template.id == 1172) {
                return true;
                 } else if (trangBi.template.type == 72 && daNangCap.template.id == 1188) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else if (trangBi.template.id == 1665 && daNangCap.template.id == 1170) {
                return true;
            } else if (trangBi.template.id == 1666 && daNangCap.template.id == 1171) {
                return true;
            } else if (trangBi.template.id == 1667 && daNangCap.template.id == 1172) {
                return true;
                 } else if (trangBi.template.type == 72 && daNangCap.template.id == 1188) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20 || (item.template.id >= 1975 && item.template.id <= 1977)));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 || item.template.type == 32) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 5; // +5%né đòn
            case 1975:
                return 4; // +3%sđ
            case 1976:
                return 6; // +2%giáp
            case 1977:
                return 6; // +5%né đòn
             default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            case 1975:
                return 50;
            case 1976:
                return 77;
            case 1977:
                return 103;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case NANG_CAP_DO_KICH_HOAT:
                return "Ta sẽ giúp ngươi\n làm điều đó";                
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case PHAN_RA_DO_THAN_LINH:
                return "Ta sẽ phân rã \n  trang bị của người thành điểm!";
           case THANG_CAP_TRANG_BI:
                return "Cần 1 Linh Thú,  Hồn Linh Thú và Thỏi Vàng"
                        + "\nSau đó chọn 'Nâng Cấp '";
            case CHE_TAO_TRANG_BI_TS:
                return "Chế tạo\ntrang bị thiên sứ";
            case NANG_CAP_SKH_VIP:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_BONG_TAI_CAP3:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\nthành cấp 3";
            case MO_CHI_SO_BONG_TAI_CAP3:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 3 của ngươi\ncó 1 chỉ số ngẫu nhiên";
                case TIEN_HOA_CT:
                return "Ta sẽ phù phép\ncho Cải trang  của ngươi\ncó 1 chỉ số Thật cao";
            case NANG_VIP:
                return "Ta sẽ phù phép\ncho iTEM VIP  của ngươi\ncó 1 chỉ số Thật cao";
            case CHAN_MENH:
                return "Mang chân Mệnh tới đây ta \nBan sức mạnh cho";
            case NANG_CAP_LINH_THU:
                return "Ta sẽ phù phép\ncho Linh Thú cùi bắp của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_MAT_THAN:
                return "MAng MẮt Thần Đến Đây TA sẽ PHÙ PHép CHo nó MẠnh HƠn";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_DO_KICH_HOAT:
                return "Vào hành trang\nchọn 1 trang bị hủy diệt,Đá Ngũ Sắc\n "
                        + " và 500tr vàng\n"
                        + "Chỉ cần chọn 'Nâng Cấp'";                
            case PHAN_RA_DO_THAN_LINH:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để phân rã\n"
                        + "Sau đó chọn 'Phân Rã'";
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\n"
                        + "Mảnh trang bị tương ứng\n"
                        + "1 đá nâng cấp (tùy chọn)"
                        + "1 đá may mắn (tùy chọn)"
                        + "Sau đó chọn 'Nâng cấp'";
            case THANG_CAP_TRANG_BI:
                return "Cần 1 Linh Thú,  Hồn Linh Thú và Thỏi Vàng"
                        + "\nSau đó chọn 'Nâng Cấp '";
            case NANG_CAP_SKH_VIP:
                return "vào hành trang\nChọn 1 trang bị thiên sứ bất kì\nChọn tiếp ngẫu nhiên 2 món SKH thường \n "
                        + " đồ SKH VIP sẽ cùng loại \n với đồ thiên sứ!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_LINH_THU:
                return "Vào hành trang\nChọn Linh Thú\nChọn Thăng Tinh Thạch số lượng 99 cái\nvà Thức Ăn để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_MAT_THAN:
                return "vào hành trang\nChọn trang bị\n(SHARIGAN RED or BLUE, GREEN)\nChọn đá red,blue,green để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI_CAP3:
                return "Vào hành trang\nchọn bông tai Porata cấp 2\nchọn x9999 MVBT C3\nSau đó chọn 'Nâng cấp'";
                  case TIEN_HOA_CT:
                return "Vào hành trang\nchọn cải trang\nchọn  , x99 đá tiến hóa\nSau đó chọn 'Nâng cấp'";
                 case NANG_VIP:
                return "Vào hành trang\nchọn  1 item vip \nchọn  ,  rượu quý \nSau đó chọn 'Nâng cấp'";
              case CHAN_MENH:
                return "Vào hành trang\nchọn Chân mệnh \nchọn  ,   Đá Hoàng Kim \nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI_CAP3:
                return "Vào hành trang\nChọn bông tai Porata cấp 3\nChọn thạch phù để nâng cấp, số lượng 99 viên\nđá xanh lam\nSau đó chọn 'Nâng cấp'";
            default:

                return "";
        }
    }

}
