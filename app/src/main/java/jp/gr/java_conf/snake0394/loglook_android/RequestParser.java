package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.logger.CreateItemLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.CreateShipLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.HeavilyDamagedWarningActivity;

/**
 * Created by snake0394 on 2016/10/26.
 */
public class RequestParser {
    public synchronized static void parse(String uri, String requestBody) {
        //Log.d("requestURI", uri);
        try {
            Context context = App.getInstance();
            switch (uri) {
                case "api_req_mission/start":
                    String regex = "Fdeck%5Fid=(\\d+)";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(requestBody);
                    m.find();
                    int deckId = Integer.parseInt(m.group(1));

                    regex = "Fmission%5Fid=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    int missionId = Integer.parseInt(m.group(1));
                    MissionTimer.INSTANCE.ready(deckId, missionId);

                    break;

                case "api_req_map/start":
                    regex = "Fdeck%5Fid=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    deckId = Integer.parseInt(m.group(1));
                    Deck deck = DeckManager.INSTANCE.getDeck(deckId);
                    List<Integer> shipIdList = deck.getShipId();
                    ArrayList<Integer> heavyDamaged = new ArrayList<>();
                    for (int i = 0; i < shipIdList.size(); i++) {
                        MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipIdList.get(i));
                        if (shipIdList.get(i) != -1 && myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                            heavyDamaged.add(myShip.getId());
                        }
                    }
                    if (!heavyDamaged.isEmpty()) {
                        //大破進撃警告画面を表示
                        Intent intent = new Intent(context, HeavilyDamagedWarningActivity.class);
                        intent.putIntegerArrayListExtra("shipId", heavyDamaged);
                        intent.putExtra("first", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(intent);
                    }

                    break;

                case "api_req_hensei/change":
                    //艦隊ID
                    regex = "api%5Fid=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    deckId = Integer.parseInt(m.group(1));
                    deck = DeckManager.INSTANCE.getDeck(deckId);
                    shipIdList = deck.getShipId();

                    //変更位置
                    regex = "api%5Fship%5Fidx=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);

                    if (!m.find()) {
                        //一斉解除
                        shipIdList.set(1, -1);
                        shipIdList.set(2, -1);
                        shipIdList.set(3, -1);
                        shipIdList.set(4, -1);
                        shipIdList.set(5, -1);
                        break;
                    }

                    int changeIndex = Integer.parseInt(m.group(1));

                    //配備される艦の所有艦娘ID
                    regex = "api%5Fship%5Fid=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    if (m.find()) {
                        int deployedShipId = Integer.parseInt(m.group(1));

                        for (int i = 1; i <= DeckManager.INSTANCE.getDeckNum(); i++) {
                            if (i == deckId) {
                                continue;
                            }
                            Deck tDeck = DeckManager.INSTANCE.getDeck(i);
                            int num = 0;
                            List<Integer> tShipIdList = tDeck.getShipId();
                            for (int id : tShipIdList) {
                                if (id == deployedShipId) {
                                    tShipIdList.set(num, deck.getShipId().get(changeIndex));
                                    for (int j = 0; j < tShipIdList.size() - 1; j++) {
                                        if (tShipIdList.get(j) == -1) {
                                            try {
                                                int k = j;
                                                while (tShipIdList.get(k + 1) == -1) {
                                                    k++;
                                                }
                                                tShipIdList.set(j, tShipIdList.get(k + 1));
                                                tShipIdList.set(k + 1, -1);
                                                j = k;
                                            } catch (IndexOutOfBoundsException e) {

                                            }
                                        }
                                    }
                                    break;
                                }
                                num++;
                            }
                        }

                        for (int i = 0; i < shipIdList.size(); i++) {
                            if (shipIdList.get(i) == deployedShipId) {
                                shipIdList.set(i, shipIdList.get(changeIndex));
                            }
                        }

                        shipIdList.set(changeIndex, deployedShipId);

                        for (int i = 0; i < shipIdList.size() - 1; i++) {
                            if (shipIdList.get(i) == -1) {
                                try {
                                    int j = i;
                                    while (shipIdList.get(j + 1) == -1) {
                                        j++;
                                    }
                                    shipIdList.set(i, shipIdList.get(j + 1));
                                    shipIdList.set(j + 1, -1);
                                    i = j;
                                } catch (IndexOutOfBoundsException e) {

                                }
                            }
                        }
                    } else {
                        if (shipIdList.size() - 1 == changeIndex) {
                            shipIdList.set(changeIndex, -1);
                            break;
                        }
                        for (int i = changeIndex; i < shipIdList.size() - 1; i++) {
                            shipIdList.set(i, shipIdList.get(i + 1));
                        }
                        shipIdList.set(shipIdList.size() - 1, -1);
                    }
                    deck.setLevelSum(DeckUtility.getLevelSum(deck));
                    deck.setCondRecoveryTime(DeckUtility.getCondRecoveryTime(deck));
                    break;

                case "api_req_kousyou/createship":
                    regex = "Fkdock%5Fid=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    int kdockId = Integer.parseInt(m.group(1));

                    regex = "large%5Fflag=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    int largeFlag = Integer.parseInt(m.group(1));

                    CreateShipLogger.INSTANCE.ready(kdockId,largeFlag);
                    break;

                case "api_req_kousyou/createitem":
                    regex = "item1=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    int fuel = Integer.parseInt(m.group(1));

                    regex = "item2=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    int bullet = Integer.parseInt(m.group(1));

                    regex = "item3=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    int steel = Integer.parseInt(m.group(1));

                    regex = "item4=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    int bauxite = Integer.parseInt(m.group(1));

                    CreateItemLogger.INSTANCE.ready(fuel,bullet,steel,bauxite);
                    break;

                case "api_req_nyukyo/start":
                    regex = "api%5Fhighspeed=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    if (Integer.parseInt(m.group(1)) == 0) {
                        regex = "api%5Fndock%5Fid=(\\d+)";
                        p = Pattern.compile(regex);
                        m = p.matcher(requestBody);
                        m.find();
                        int dockId = Integer.parseInt(m.group(1));
                        regex = "api%5Fship%5Fid=(\\d+)";
                        p = Pattern.compile(regex);
                        m = p.matcher(requestBody);
                        m.find();
                        int shipId = Integer.parseInt(m.group(1));
                        DockTimer.INSTANCE.startTimer(context, dockId, shipId, Calendar.getInstance().getTimeInMillis() + MyShipManager.INSTANCE.getMyShip(shipId).getNdockTime());
                    }
                    break;

                case "api_req_nyukyo/speedchange":
                    regex = "api%5Fndock%5Fid=(\\d+)";
                    p = Pattern.compile(regex);
                    m = p.matcher(requestBody);
                    m.find();
                    DockTimer.INSTANCE.stop(Integer.parseInt(m.group(1)));
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        if (App.getInstance().getSharedPreferences().getBoolean("saveRequest", false)) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/request/");

            //パス区切り用セパレータ
            String Fs = File.separator;

            uri = uri.replaceAll("/", "=");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //テキストファイル保存先のファイルパス
            String filePath = sdcard_path + Fs + sdf.format(Calendar.getInstance().getTime()) + "-" + uri + ".txt";

            //フォルダがなければ作成
            if (!sdcard_path.exists()) {
                sdcard_path.mkdirs();
            }

            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "SJIS")));
                pw.write(requestBody);
                pw.flush();
                pw.close();
            } catch (Exception e) {

            }
        }
    }
}
