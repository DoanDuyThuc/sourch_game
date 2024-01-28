package com.girlkun.server;

import com.girlkun.MaQuaTang.MaQuaTangManager;
import com.girlkun.database.GirlkunDB;

import java.net.ServerSocket;
import com.girlkun.models.map.challenge.MartialCongressManager;
import com.girlkun.jdbc.daos.HistoryTransactionDAO;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.player.Player;
import com.girlkun.network.session.ISession;
import com.girlkun.network.example.MessageSendCollect;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.network.server.IServerClose;
import com.girlkun.network.server.ISessionAcceptHandler;
import static com.girlkun.server.Maintenance.isBaoTri;
import com.girlkun.server.io.MyKeyHandler;
import com.girlkun.server.io.MySession;
import com.girlkun.services.ClanService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.NgocRongNamecService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChonAiDay;
import com.girlkun.services.func.TopService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import com.kygui.ShopKyGuiManager;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.logging.Level;

public class ServerManager {

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "Girlkun75";
    public static int PORT = 14445;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;
    private long start;

    public void init() {
        Manager.gI();
        try {
            if (Manager.LOCAL) {
                return;
            }
            GirlkunDB.executeUpdate("update account set last_time_login = '2000-01-01', "
                    + "last_time_logout = '2001-01-01'");
        } catch (Exception e) {
        }
        HistoryTransactionDAO.deleteHistory();
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
    }

    public void run() {
        activeCommandLine();
        activeGame();
        activeServerSocket();
        NgocRongNamecService.gI().initNgocRongNamec((byte) 0);
        isRunning = true;
        try {
            Thread.sleep(1000);
            BossManager.gI().loadBoss();
            Manager.MAPS.forEach(com.girlkun.models.map.Map::initBoss);
        } catch (InterruptedException ex) {
            System.err.print("\nError at 311\n");
            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void act() throws Exception {
        GirlkunServer.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
            @Override
            public void sessionInit(ISession is) {
                if (!canConnectWithIp(is.getIP())) {
                    is.disconnect();
                    return;
                }
                is = is.setMessageHandler(Controller.getInstance())
                        .setSendCollect(new MessageSendCollect())
                        .setKeyHandler(new MyKeyHandler())
                        .startCollect();
            }

            @Override
            public void sessionDisconnect(ISession session) {
                Client.gI().kickSession((MySession) session);
            }
        }).setTypeSessioClone(MySession.class)
                .setDoSomeThingWhenClose(new IServerClose() {
                    @Override
                    public void serverClose() {
                        System.out.println("server close");
                        System.exit(0);
                    }
                })
                .start(PORT);
    }

    private void activeServerSocket() {
        if (true) {
            try {
                this.act();
            } catch (Exception e) {
                System.err.print("\nError at 312\n");
                e.printStackTrace();
            }
            return;
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("savekigui")) {
//                    ShopKyGuiManager.gI().save();
                }
                if (line.equals("baotri")) {
                    Maintenance.gI().start(0);
                }
                if (line.equals("savedata")) {
                    ClanService.gI().saveclan();
                } else if (line.equals("online")) {
                    List<String> lines = new ArrayList<>();
                    ThreadMXBean threadMXBean;
                    threadMXBean = ManagementFactory.getThreadMXBean();
                    ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds());
                    System.out.println("Danh sách tên các luồng đang chạy:");
                    lines.add("Danh sách tên các luồng đang chạy:");
                    for (ThreadInfo threadInfo : threadInfos) {
                        lines.add(threadInfo.getThreadName());
                        System.out.println(threadInfo.getThreadName());
                    }
                    Path file = Paths.get("DataThread.txt");
                    try {
                        Files.write(file, lines, StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    int threadMap;
                    Logger.log(Logger.PURPLE, "Thread" + (Thread.activeCount()) + "\nOnline:" + Client.gI().getPlayers().size());

                } else if (line.equals("nplayer")) {
                    Logger.error("Player in game: " + Client.gI().getPlayers().size() + "\n");
                } else if (line.equals("admin")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }, "adminThread").start();
                } else if (line.startsWith("bang")) {
                    new Thread(() -> {
                        try {
                            ClanService.gI().close();
                            Logger.error("Save " + Manager.CLANS.size() + " bang");
                        } catch (Exception e) {

                            e.printStackTrace();
                            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");

                        }
                    }, "bangThread").start();
                } else if (line.startsWith("a")) {
                    String a = line.replace("a ", "");
                    Service.gI().sendThongBaoAllPlayer(a);
                } else if (line.startsWith("tb")) {
                    String a = line.replace("tb ", "");
                    Service.gI().sendBangThongBaoAllPlayervip(a);
                } else if (line.startsWith("qua")) {
                    try {
                        List<Item.ItemOption> ios = new ArrayList<>();
                        String[] pagram1 = line.split("=")[1].split("-");
                        String[] pagram2 = line.split("=")[2].split("-");
                        if (pagram1.length == 4 && pagram2.length % 2 == 0) {
                            Player p = Client.gI().getPlayer(Integer.parseInt(pagram1[0]));
                            if (p != null) {
                                for (int i = 0; i < pagram2.length; i += 2) {
                                    ios.add(new Item.ItemOption(Integer.parseInt(pagram2[i]), Integer.parseInt(pagram2[i + 1])));
                                }
                                Item i = Util.sendDo(Integer.parseInt(pagram1[2]), Integer.parseInt(pagram1[3]), ios);
                                i.quantity = Integer.parseInt(pagram1[1]);
                                InventoryServiceNew.gI().addItemBag(p, i);
                                InventoryServiceNew.gI().sendItemBags(p);
                                Service.gI().sendThongBao(p, "Admin trả đồ. anh em thông cảm nhé...");
                            } else {
                                System.out.println("Người chơi không online");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Lỗi quà");

                    }
                }
            }
        }, "activeCommandLineThread").start();
    }

    private void activeGame() {
        long delay = 60000; // Cập nhật thông tin game và player
        MartialCongressManager.gI().update();
        new Thread(()
                -> {
            while (!isBaoTri) {
                try {
                    NgocRongNamecService.gI().update();
                    for (BanDoKhoBau bando : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                        bando.update();
                    }
                    ClanService.gI().saveclan();
                    Service.gI().AutoSavePlayerData();
                    ShopKyGuiManager.gI().save();
                    Thread.sleep(delay);
                } catch (Exception e) {
                    System.err.print("\nError at 314\n");
                    e.printStackTrace();
                }
            }
        }, "activeGame").start();
    }

    public void close(long delay) {
        isRunning = false;
        try {
            GirlkunServer.gI().stopConnect();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: Lỗi Đóng kết nối tới server.\n");
        }
        Logger.log(Logger.BLACK, "\nĐóng kết nối tới server.\n");
        try {
            Client.gI().close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: Lỗi lưu dử liệu người chơi.\n");
        }
        try {
            ClanService.gI().close();
            Logger.log(Logger.BLACK, "Lưu dử liệu bang hội\n");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
        }
        try {
            ShopKyGuiManager.gI().save();
            Logger.log(Logger.BLACK, "Lưu dử liệu ký gửi\n");
        } catch (InterruptedException ex) {
            System.err.print("\nError at 315\n");
           ex.printStackTrace();
        }
//        try {
//            CheckDataDay.ResetDataDay();
//            Logger.log(Logger.BLACK,
//                    "Reset Dữ Liệu Hoạt Động Hằng Ngày - Quà Nạp Hằng Ngày.\n");
//        } catch (SQLException ex) {
//            System.err.print("\nError at 316\n");
//            ex.printStackTrace();
//        }
        Logger.log(Logger.BLACK, "Bảo trì đóng server thành công.\n");
        
      //  ServerManager.gI().run();
        System.exit(0);
    }
}
