package com.girlkun.services;

import com.girlkun.models.Template;
import com.girlkun.models.Template.ItemOptionTemplate;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.shop.ItemShop;
import com.girlkun.server.Manager;
import com.girlkun.services.func.CombineServiceNew;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import com.girlkun.models.item.Item.ItemOption;
import static com.girlkun.models.item.ItemTime.BAN_DO_KHO_BAU;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.girlkun.network.io.Message;

import java.util.*;
import java.util.stream.Collectors;

public class ItemService {

    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public short getItemIdByIcon(short IconID) {
        for (int i = 0; i < Manager.ITEM_TEMPLATES.size(); i++) {
            if (Manager.ITEM_TEMPLATES.get(i).iconID == IconID) {
                return Manager.ITEM_TEMPLATES.get(i).id;
            }
        }
        return -1;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

//    public Item createItemFromItemShop(ItemShop itemShop) {
//        Item item = new Item();
//        item.template = itemShop.temp;
//        item.quantity = 1;
//        item.content = item.getContent();
//        item.info = item.getInfo();
//        for (Item.ItemOption io : itemShop.options) {
//            item.itemOptions.add(new Item.ItemOption(io));
//        }
//        return item;
//       
//    }
    public Item createItemFromItemShop(ItemShop itemShop) {
        if ("BILL".equals(itemShop.tabShop.shop.tagName)) {
            Item item = new Item();
            item.template = itemShop.temp;
            item.quantity = 1;
            item.content = item.getContent();
            item.info = item.getInfo();

            for (Item.ItemOption io : itemShop.options) {
                item.itemOptions.add(new Item.ItemOption(io));
            }

            item.itemOptions.forEach(c -> {
                if (c.optionTemplate.id != 21 && c.optionTemplate.id != 30) {
                    if (Util.nextInt(0, 500) < 300) {
                        c.param = c.param + ((c.param * Util.nextInt(1, 5)) / 100);
                    } else if (Util.nextInt(0, 500) < 450) {
                        c.param = c.param + ((c.param * Util.nextInt(1, 10)) / 100);
                    } else {
                        c.param = c.param + ((c.param * Util.nextInt(1, 15)) / 100);
                    }
                }
            });

            return item;
        } else {
            Item item = new Item();
            item.template = itemShop.temp;
            item.quantity = 1;
            item.content = item.getContent();
            item.info = item.getInfo();

            for (Item.ItemOption io : itemShop.options) {
                item.itemOptions.add(new Item.ItemOption(io));

                removeAndAddOptionTemplate(item.itemOptions, new Item.ItemOption(io).optionTemplate.id);

            }

            return item;
        }
    }    
    public void removeAndAddOptionTemplate(List<Item.ItemOption> itemOptions, int removeId) {
        int id = 0;
        int param = 0;
        int[] rd203 = new int[]{50, 77, 103};
        int[] rd212 = new int[]{5, 14, 94, 108, 97};
        int random203 = new Random().nextInt(rd203.length);
        int random212 = new Random().nextInt(rd212.length);

        boolean shouldExecute = false;

        switch (removeId) {
            case 217:
                id = 50;
                param = Util.nextInt(1, 10);
                shouldExecute = true;
                break;
            case 218:
                id = 77;
                param = Util.nextInt(1, 10);
                shouldExecute = true;
                break;
            case 219:
                id = 103;
                param = Util.nextInt(1, 10);
                shouldExecute = true;
                break;
            case 220:
                id = 50;
                param = Util.nextInt(25, 30);
                shouldExecute = true;
                break;
            case 221:
                id = 77;
                param = Util.nextInt(25, 30);
                shouldExecute = true;
                break;
            case 222:
                id = 103;
                param = Util.nextInt(25, 30);
                shouldExecute = true;
                break;
            case 223:
                id = 50;
                param = Util.nextInt(1, 15);
                shouldExecute = true;
                break;
            case 224:
                id = 77;
                param = Util.nextInt(1, 15);
                shouldExecute = true;
                break;
            case 225:
                id = 103;
                param = Util.nextInt(1, 15);
                shouldExecute = true;
                break;
            case 226:
                id = rd203[random203];
                param = Util.nextInt(1, 6);
                shouldExecute = true;
                break;
            case 227:
                id = 50;
                param = Util.nextInt(1, 6);
                shouldExecute = true;
                break;
            case 228:
                id = 77;
                param = Util.nextInt(1, 6);
                shouldExecute = true;
                break;
            case 229:
                id = 103;
                param = Util.nextInt(1, 6);
                shouldExecute = true;
                break;
            case 230:
                id = rd212[random212];
                param = Util.nextInt(1, 15);
                shouldExecute = true;
                break;
            case 231:
                id = 50;
                param = Util.nextInt(25, 35);
                shouldExecute = true;
                break;
            case 232:
                id = 77;
                param = Util.nextInt(25, 35);
                shouldExecute = true;
                break;
            case 233:
                id = 103;
                param = Util.nextInt(25, 35);
                shouldExecute = true;
                break;
            case 234:
                id = 5;
                if (Util.isTrue(90, 100)) {
                    param = Util.nextInt(70, 89);
                } else {
                    param = Util.nextInt(85, 100);
                }
                shouldExecute = true;
                break;
            default:
                // Nếu không khớp với các trường hợp trên, không thực hiện thay đổi
                break;
        }

        if (shouldExecute && itemOptions.stream().anyMatch(io -> io.optionTemplate.id == removeId)) {
            itemOptions.removeIf(io -> io.optionTemplate.id == removeId); // Xóa optionTemplate có id cần xóa
            itemOptions.add(new ItemOption(new Item.ItemOption(id, param))); // Thêm optionTemplate mới có id vào danh sách
        }
    }
    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (Item.ItemOption io : item.itemOptions) {
            it.itemOptions.add(new Item.ItemOption(io));
        }
        return it;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item otpts(short tempId) {
        return otpts(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();

        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item otpts(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null
                && player.clan.timeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Bản đồ kho báu", secondsLeft);
        }
    }

    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemDoHuyDiet(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public Template.ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        if (item != null) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return false;
    }

    public void OpenSKH(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) {
            return;
        }
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
        int[][] items = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
        int skhv1 = 25;// ti le
        int skhv2 = 35;//ti le
        int skhc = 40;//ti le
        int skhId = -1;

        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        Item item = null;
        switch (itemUseId) {
            case 2000:
                item = itemSKH(items[0][select], options[0][skhId]);
                break;
            case 2001:
                item = itemSKH(items[1][select], options[1][skhId]);
                break;
            case 2002:
                item = itemSKH(items[2][select], options[2][skhId]);
                break;
        }
        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public int randomSKHId(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
        int skhv1 = 25;
        int skhv2 = 35;
        int skhc = 40;
        int skhId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }

    public void OpenDHD(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) {
            return;
        }
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
        int gender = -1;
        switch (itemUseId) {
            case 2003: //td
                gender = 0;
                break;
            case 2004: //xd
                gender = 2;
                break;
            case 2005: //nm
                gender = 1;
                break;
        }
        int[][] items = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}}; //td, namec,xd
        Item item = randomCS_DHD(items[gender][select], gender);

        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void OpenItem736(Player player, Item itemUse) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            int rd = Util.nextInt(1, 100);
            int rac = 50;
            int ruby = 20;
            int dbv = 10;
            int vb = 10;
            int ximuoi = 9;
            int vv = 1;
            Item item = randomRac();
            if (rd <= rac) {
                item = randomRac();
            } else if (rd <= rac + ruby) {
                item = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
            } else if (rd <= rac + ruby + dbv) {
                item = daBaoVe();
            } else if (rd <= rac + ruby + dbv + vb) {
                item = vanBay2011(true);
            } else if (rd <= rac + ruby + dbv + vb + ximuoi) {
                item = phuKien2011(true);
            } else if (rd <= rac + ruby + dbv + vb + ximuoi + vv) {
                Service.gI().sendThongBaoAllPlayer(player.name + "  Sử dụng hộp quà nhận được hộp thiên sứ bro max ");
            }
            if (item.template.id == 457) {
                item.quantity = Util.nextInt(10, 30);
            }
            icon[0] = itemUse.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            player.inventory.event++;
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settaiyoken(Player player) throws Exception {
       // for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1048);
            Item quan = ItemService.gI().otpts((short) 1051);
            Item gang = ItemService.gI().otpts((short) 1054);
            Item giay = ItemService.gI().otpts((short) 1057);
            Item nhan = ItemService.gI().otpts((short) 1060);
            ao.itemOptions.add(new Item.ItemOption(127, 0));
            quan.itemOptions.add(new Item.ItemOption(127, 0));
            gang.itemOptions.add(new Item.ItemOption(127, 0));
            giay.itemOptions.add(new Item.ItemOption(127, 0));
            nhan.itemOptions.add(new Item.ItemOption(127, 0));
            ao.itemOptions.add(new Item.ItemOption(139, 0));
            quan.itemOptions.add(new Item.ItemOption(139, 0));
            gang.itemOptions.add(new Item.ItemOption(139, 0));
            giay.itemOptions.add(new Item.ItemOption(139, 0));
            nhan.itemOptions.add(new Item.ItemOption(139, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
      //  }
    }
    public Item otphd(short tempId) {
        return otphd(tempId, 1);
    }    
    public Item otphd(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(95, 110)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9500, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(95, 110)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(18, 22)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }    

    public void setgenki(Player player) throws Exception {
     //   for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1048);
            Item quan = ItemService.gI().otpts((short) 1051);
            Item gang = ItemService.gI().otpts((short) 1054);
            Item giay = ItemService.gI().otpts((short) 1057);
            Item nhan = ItemService.gI().otpts((short) 1060);
            ao.itemOptions.add(new Item.ItemOption(128, 0));
            quan.itemOptions.add(new Item.ItemOption(128, 0));
            gang.itemOptions.add(new Item.ItemOption(128, 0));
            giay.itemOptions.add(new Item.ItemOption(128, 0));
            nhan.itemOptions.add(new Item.ItemOption(128, 0));
            ao.itemOptions.add(new Item.ItemOption(140, 0));
            quan.itemOptions.add(new Item.ItemOption(140, 0));
            gang.itemOptions.add(new Item.ItemOption(140, 0));
            giay.itemOptions.add(new Item.ItemOption(140, 0));
            nhan.itemOptions.add(new Item.ItemOption(140, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
      //  }
    }

    public void setkamejoko(Player player) throws Exception {
     //   for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1048);
            Item quan = ItemService.gI().otpts((short) 1051);
            Item gang = ItemService.gI().otpts((short) 1054);
            Item giay = ItemService.gI().otpts((short) 1057);
            Item nhan = ItemService.gI().otpts((short) 1060);
            ao.itemOptions.add(new Item.ItemOption(129, 0));
            quan.itemOptions.add(new Item.ItemOption(129, 0));
            gang.itemOptions.add(new Item.ItemOption(129, 0));
            giay.itemOptions.add(new Item.ItemOption(129, 0));
            nhan.itemOptions.add(new Item.ItemOption(129, 0));
            ao.itemOptions.add(new Item.ItemOption(141, 0));
            quan.itemOptions.add(new Item.ItemOption(141, 0));
            gang.itemOptions.add(new Item.ItemOption(141, 0));
            giay.itemOptions.add(new Item.ItemOption(141, 0));
            nhan.itemOptions.add(new Item.ItemOption(141, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
         //   }
        }
    }

    public void setgodki(Player player) throws Exception {
      //  for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1049);
            Item quan = ItemService.gI().otpts((short) 1052);
            Item gang = ItemService.gI().otpts((short) 1055);
            Item giay = ItemService.gI().otpts((short) 1058);
            Item nhan = ItemService.gI().otpts((short) 1061);
            ao.itemOptions.add(new Item.ItemOption(130, 0));
            quan.itemOptions.add(new Item.ItemOption(130, 0));
            gang.itemOptions.add(new Item.ItemOption(130, 0));
            giay.itemOptions.add(new Item.ItemOption(130, 0));
            nhan.itemOptions.add(new Item.ItemOption(130, 0));
            ao.itemOptions.add(new Item.ItemOption(142, 0));
            quan.itemOptions.add(new Item.ItemOption(142, 0));
            gang.itemOptions.add(new Item.ItemOption(142, 0));
            giay.itemOptions.add(new Item.ItemOption(142, 0));
            nhan.itemOptions.add(new Item.ItemOption(142, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
         //   }
        }
    }

    public void setgoddam(Player player) throws Exception {
     //   for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1049);
            Item quan = ItemService.gI().otpts((short) 1052);
            Item gang = ItemService.gI().otpts((short) 1055);
            Item giay = ItemService.gI().otpts((short) 1058);
            Item nhan = ItemService.gI().otpts((short) 1061);
            ao.itemOptions.add(new Item.ItemOption(131, 0));
            quan.itemOptions.add(new Item.ItemOption(131, 0));
            gang.itemOptions.add(new Item.ItemOption(131, 0));
            giay.itemOptions.add(new Item.ItemOption(131, 0));
            nhan.itemOptions.add(new Item.ItemOption(131, 0));
            ao.itemOptions.add(new Item.ItemOption(143, 0));
            quan.itemOptions.add(new Item.ItemOption(143, 0));
            gang.itemOptions.add(new Item.ItemOption(143, 0));
            giay.itemOptions.add(new Item.ItemOption(143, 0));
            nhan.itemOptions.add(new Item.ItemOption(143, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        //
    }

    public void setsummon(Player player) throws Exception {
      ///  for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1049);
            Item quan = ItemService.gI().otpts((short) 1052);
            Item gang = ItemService.gI().otpts((short) 1055);
            Item giay = ItemService.gI().otpts((short) 1058);
            Item nhan = ItemService.gI().otpts((short) 1061);
            ao.itemOptions.add(new Item.ItemOption(132, 0));
            quan.itemOptions.add(new Item.ItemOption(132, 0));
            gang.itemOptions.add(new Item.ItemOption(132, 0));
            giay.itemOptions.add(new Item.ItemOption(132, 0));
            nhan.itemOptions.add(new Item.ItemOption(132, 0));
            ao.itemOptions.add(new Item.ItemOption(144, 0));
            quan.itemOptions.add(new Item.ItemOption(144, 0));
            gang.itemOptions.add(new Item.ItemOption(144, 0));
            giay.itemOptions.add(new Item.ItemOption(144, 0));
            nhan.itemOptions.add(new Item.ItemOption(144, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
      //  }
    }

    public void setgodgalick(Player player) throws Exception {
        //for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1050);
            Item quan = ItemService.gI().otpts((short) 1053);
            Item gang = ItemService.gI().otpts((short) 1056);
            Item giay = ItemService.gI().otpts((short) 1059);
            Item nhan = ItemService.gI().otpts((short) 1062);
            ao.itemOptions.add(new Item.ItemOption(133, 0));
            quan.itemOptions.add(new Item.ItemOption(133, 0));
            gang.itemOptions.add(new Item.ItemOption(133, 0));
            giay.itemOptions.add(new Item.ItemOption(133, 0));
            nhan.itemOptions.add(new Item.ItemOption(133, 0));
            ao.itemOptions.add(new Item.ItemOption(136, 0));
            quan.itemOptions.add(new Item.ItemOption(136, 0));
            gang.itemOptions.add(new Item.ItemOption(136, 0));
            giay.itemOptions.add(new Item.ItemOption(136, 0));
            nhan.itemOptions.add(new Item.ItemOption(136, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            //}
        }
    }

    public void setmonkey(Player player) throws Exception {
   //     for (int i = 0; i < 12; i) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1050);
            Item quan = ItemService.gI().otpts((short) 1053);
            Item gang = ItemService.gI().otpts((short) 1056);
            Item giay = ItemService.gI().otpts((short) 1059);
            Item nhan = ItemService.gI().otpts((short) 1062);
            ao.itemOptions.add(new Item.ItemOption(134, 0));
            quan.itemOptions.add(new Item.ItemOption(134, 0));
            gang.itemOptions.add(new Item.ItemOption(134, 0));
            giay.itemOptions.add(new Item.ItemOption(134, 0));
            nhan.itemOptions.add(new Item.ItemOption(134, 0));
            ao.itemOptions.add(new Item.ItemOption(137, 0));
            quan.itemOptions.add(new Item.ItemOption(137, 0));
            gang.itemOptions.add(new Item.ItemOption(137, 0));
            giay.itemOptions.add(new Item.ItemOption(137, 0));
            nhan.itemOptions.add(new Item.ItemOption(137, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
       // }
    }

    public void setgodhp(Player player) throws Exception {
       // for (int i = 0; i < 12; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105);
            Item ao = ItemService.gI().otpts((short) 1050);
            Item quan = ItemService.gI().otpts((short) 1053);
            Item gang = ItemService.gI().otpts((short) 1056);
            Item giay = ItemService.gI().otpts((short) 1059);
            Item nhan = ItemService.gI().otpts((short) 1062);
            ao.itemOptions.add(new Item.ItemOption(135, 0));
            quan.itemOptions.add(new Item.ItemOption(135, 0));
            gang.itemOptions.add(new Item.ItemOption(135, 0));
            giay.itemOptions.add(new Item.ItemOption(135, 0));
            nhan.itemOptions.add(new Item.ItemOption(135, 0));
            ao.itemOptions.add(new Item.ItemOption(138, 0));
            quan.itemOptions.add(new Item.ItemOption(138, 0));
            gang.itemOptions.add(new Item.ItemOption(138, 0));
            giay.itemOptions.add(new Item.ItemOption(138, 0));
            nhan.itemOptions.add(new Item.ItemOption(138, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
          //  }
        }
    }

    public Item itemSKH(int itemId, int skhId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new Item.ItemOption(skhId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdSKH(skhId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public int optionItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
                return 47;
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 7;
            default:
                return 14;
        }
    }

    public int pagramItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
            case 2:
                return Util.nextInt(5);
            case 1:
            case 3:
                return Util.nextInt(20, 30);
            default:
                return Util.nextInt(3);
        }
    }

    public int optionIdSKH(int skhId) {
        switch (skhId) {
            case 127: //Set Arriety Taiyoken
                return 139;
            case 128: //Set Arriety Genki
                return 140;
            case 129: //Set Arriety Kamejoko
                return 141;
            case 130: //Set Arriety KI
                return 142;
            case 131: //Set Arriety Dame
                return 143;
            case 132: //Set Arriety Summon
                return 144;
            case 133: //Set Arriety Galick
                return 136;
            case 134: //Set Arriety Monkey
                return 137;
            case 135: //Set Arriety HP
                return 138;
        }
        return 0;
    }

    public Item itemDHD(int itemId, int dhdId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.add(new Item.ItemOption(dhdId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdDHD(dhdId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public int optionIdDHD(int skhId) {
        switch (skhId) {
            case 127: //Set Arriety Taiyoken
                return 139;
            case 128: //Set Arriety Genki
                return 140;
            case 129: //Set Arriety Kamejoko
                return 141;
            case 130: //Set Arriety KI
                return 142;
            case 131: //Set Arriety Dame
                return 143;
            case 132: //Set Arriety Summon
                return 144;
            case 133: //Set Arriety Galick
                return 136;
            case 134: //Set Arriety Monkey
                return 137;
            case 135: //Set Arriety HP
                return 138;
        }
        return 0;
    }

    public Item randomCS_DHD(int itemId, int gender) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 1800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(36) + 85))); // hp 85-100k
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(15000) + 18500))); // 8500-10000
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(36) + 80))); // ki 80-90k
        }
        if (nhd == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(13) + 17)); //chí mạng 17-19%
        }
        it.itemOptions.add(new Item.ItemOption(21, 80));// yêu cầu sm 80 tỉ
        it.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
        it.itemOptions.add(new Item.ItemOption(87, 1));// ko the gd
        return it;
    }

    //Cải trang sự kiện 20/11
    public Item caitrang2011(boolean rating) {
        Item item = createItemSetKichHoat(680, 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, 28));//hp 28%
        item.itemOptions.add(new Item.ItemOption(103, 25));//ki 25%
        item.itemOptions.add(new Item.ItemOption(147, 24));//sd 26%
        item.itemOptions.add(new Item.ItemOption(117, 18));//Đẹp + 18% sd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    //610 - bong hoa
    //Phụ kiện bó hoa 20/11
    public Item phuKien2011(boolean rating) {
        Item item = createItemSetKichHoat(954, 1);
        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(147, new Random().nextInt(5) + 5));
        if (Util.isTrue(1, 100)) {
            item.itemOptions.get(Util.nextInt(item.itemOptions.size() - 1)).param = 10;
        }
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item hiem(boolean rating) {
        Item item = createItemSetKichHoat(1989, 1);
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item vanBay2011(boolean rating) {
        Item item = createItemSetKichHoat(1997, 1);
        item.itemOptions.add(new Item.ItemOption(50, (new Random().nextInt(30) + 50)));
        item.itemOptions.add(new Item.ItemOption(77, (new Random().nextInt(30) + 50)));
        item.itemOptions.add(new Item.ItemOption(103, (new Random().nextInt(30) + 50)));
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(950, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item daBaoVe() {
        Item item = createItemSetKichHoat(987, 1);
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        return item;
    }

    public Item randomRac() {
        short[] racs = {20, 19, 18, 17,222,221,220,223,224,225, 1978, 381, 385, 382, 663, 664, 667, 666, 665};
        Item item = createItemSetKichHoat(racs[Util.nextInt(racs.length - 1)], 1);
        if (optionRac(item.template.id) != 0) {
            item.itemOptions.add(new Item.ItemOption(optionRac(item.template.id), 1));
        }
        return item;
    }

    public byte optionRac(short itemId) {
        switch (itemId) {
            case 220:
                return 71;
            case 221:
                return 70;
            case 222:
                return 69;
            case 224:
                return 67;
            case 223:
                return 68;
            default:
                return 0;
        }
    }

    public void openBoxVip(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 3000) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
            return;
        }
        Item item;
        if (Util.isTrue(45, 100)) {
            item = caitrang2011(false);
        } else {
            item = phuKien2011(false);
        }
        short[] icon = new short[2];
        icon[0] = 6983;
        icon[1] = item.template.iconID;
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        player.inventory.event -= 3000;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    }

    public void giaobong(Player player, int quantity) {
        if (quantity > 10000) {
            return;
        }
        try {
            Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 610);
            if (itemUse.quantity < quantity) {
                Service.gI().sendThongBao(player, "Bạn không đủ bông...");
                return;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, quantity);
            Item item = createItemSetKichHoat(736, (quantity / 100));
            item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được x" + (quantity / 100) + " " + item.template.name);
        } catch (Exception e) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
        }
    }

    public Item PK_WC(int itemId) {
        Item phukien = createItemSetKichHoat(itemId, 1);
        int co = 983;
        int cup = 982;
        int bong = 966;
        if (cup == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(6) + 5)); // hp 5-10%
        }
        if (co == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(6) + 5)); // ki 5-10%
        }
        if (bong == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(6) + 5)); // sd 5- 10%
        }
        phukien.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        phukien.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100)) {// tỉ lệ ra hsd
            phukien.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return phukien;
    }

    //Cải trang Gohan WC
    public Item CT_WC(boolean rating) {
        Item caitrang = createItemSetKichHoat(883, 1);
        caitrang.itemOptions.add(new Item.ItemOption(77, 30));// hp 30%
        caitrang.itemOptions.add(new Item.ItemOption(103, 15));// ki 15%
        caitrang.itemOptions.add(new Item.ItemOption(50, 20));// sd 20%
        caitrang.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        caitrang.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100) && rating) {// tỉ lệ ra hsd
            caitrang.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return caitrang;
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        if (player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).count() < 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thần linh");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).count() < 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ hủy diệt");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).findFirst().get();
            List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).collect(Collectors.toList());
            short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

            Item itemTS = DoThienSu(itemIds[player.gender][itemTL.template.type], player.gender);
            InventoryServiceNew.gI().addItemBag(player, itemTS);

            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));

            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openDTSVIP(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            int percent = 35;
            int percentLine = 30;
            percent = Util.nextInt(0, 50);
            if (percent == 49) {
                percent = 20;
            } else if (percent == 48 || percent == 47) {
                percent = 19;
            } else if (percent == 46 || percent == 45) {
                percent = 18;
            } else if (percent == 44 || percent == 43) {
                percent = 17;
            } else if (percent == 42 || percent == 41) {
                percent = 16;
            } else if (percent == 40 || percent == 39) {
                percent = 15;
            } else if (percent == 38 || percent == 37) {
                percent = 14;
            } else if (percent == 36 || percent == 35) {
                percent = 13;
            } else if (percent == 34 || percent == 33) {
                percent = 12;
            } else if (percent == 32 || percent == 31) {
                percent = 11;
            } else if (percent == 30 || percent == 29) {
                percent = 10;
            } else if (percent <= 28 || percent >= 26) {
                percent = 9;
            } else if (percent <= 25 || percent >= 23) {
                percent = 8;
            } else if (percent <= 22 || percent >= 20) {
                percent = 7;
            } else if (percent <= 19 || percent >= 17) {
                percent = 6;
            } else if (percent <= 16 || percent >= 14) {
                percent = 5;
            } else if (percent <= 13 || percent >= 11) {
                percent = 4;
            } else if (percent <= 10 || percent >= 8) {
                percent = 3;
            } else if (percent <= 7 || percent >= 5) {
                percent = 2;
            } else if (percent <= 4 || percent >= 2) {
                percent = 1;
            } else if (percent <= 1) {
                percent = 0;
            }
            short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2
            int gender = Util.nextInt(0, 2);
            int iditem = Util.nextInt(0, 4);
            if ((itemIds[gender][iditem] > 1050 && itemIds[gender][iditem] < 1057)) {
                {
                    int ran = Util.nextInt(100);
                    if (ran <97) {
                        iditem = Util.nextInt(0, 4);
                    }
                }
                Item itemTS = ItemService.gI().DoThienSu(itemIds[gender][iditem], gender);
                percent += 10;
                if (percent > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param * percent / 100);
                        }
                    }
                }
                percent = Util.nextInt(0, 100);
                if (percent <= percentLine) {
                    if (percent >= (percentLine - 3)) {
                        percentLine = 3;
                    } else if (percent <= (percentLine - 4) && percent >= (percentLine - 10)) {
                        percentLine = 2;
                    } else {
                        percentLine = 1;
                    }
                    itemTS.itemOptions.add(new Item.ItemOption(15, percentLine));
                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(98);
                    listOptionBonus.add(99);
                     listOptionBonus.add(87);
                    for (int i = 0; i < percentLine; i++) {
                        percent = Util.nextInt(0, listOptionBonus.size());
                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(percent), Util.nextInt(1, 5)));
                        listOptionBonus.remove(percent);
                    }
                }
                InventoryServiceNew.gI().addItemBag(player, itemTS);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
            } else {
                Service.getInstance().sendThongBao(player, "Rương Rỗng , Chúc May Mắn , Kiểm tra hành trang không đù ô trống");
            }
        }

    }

    public Item DoThienSu(int itemId, int gender) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(20, 100)) {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(11) + 220))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(21) + 230))); // hp 130-150k 15%
            }
        }
        //găng
        if (Util.isTrue(1, 100)) {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(29651) + 89350))); // 9350-10000
            }
        } else {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(20001) + 100000))); // gang 15% 10-11k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(50, 100)) {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 90))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 110))); // ki 110-130k
            }
        }

        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(14, Util.highlightsItem(gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
        }
        dots.itemOptions.add(new Item.ItemOption(21, 120));
        dots.itemOptions.add(new Item.ItemOption(30, 1));
        return dots;
    }

    public List<Item.ItemOption> getListOptionItemShop(short id) {
        List<Item.ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.id == id && list.size() == 0) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }

    private void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
