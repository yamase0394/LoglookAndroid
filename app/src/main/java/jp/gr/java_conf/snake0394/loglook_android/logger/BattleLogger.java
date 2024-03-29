package jp.gr.java_conf.snake0394.loglook_android.logger;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;
import jp.gr.java_conf.snake0394.loglook_android.bean.AbstractBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.SortieBattleresult;

/**
 * 戦闘をCSVファイルに記録します。
 */
public enum BattleLogger {
    INSTANCE;

    private boolean isFirstBattle = false;
    private int eventId;
    private int cell;
    private BattleType battleType = BattleType.NULL;
    private AbstractBattle battle;

    /**
     * api_req_map/start
     */
    public void start(int cell, int eventId) {
        isFirstBattle = true;
        this.cell = cell;
        this.eventId = eventId;
    }

    /**
     * api_req_map/next
     */
    public void next(int cell, int eventId) {
        this.cell = cell;
        this.eventId = eventId;
    }

    public void setBattle(AbstractBattle battle) {
        this.battle = battle;
    }

    public void writeLog(SortieBattleresult battleresult) {
        //SDカードのディレクトリパス
        File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/");

        //パス区切り用セパレータ
        String Fs = File.separator;

        //テキストファイル保存先のファイルパス
        String filePath = sdcard_path + Fs + "海戦・ドロップ報告書.csv";

        //フォルダがなければ作成
        if (!sdcard_path.exists()) {
            sdcard_path.mkdir();
        }

        try {
            StringBuffer sb = new StringBuffer();
            File file = new File(filePath);
            if (!file.exists()) {
                sb.append("日付,海域,マス,ボス,ランク,交戦形態,味方陣形,敵陣形,制空状態,味方触接機,敵触接機,敵艦隊名,ドロップ艦種,ドロップ艦娘,味方艦1,味方艦1HP,味方艦2,味方艦2HP,味方艦3,味方艦3HP,味方艦4,味方艦4HP,味方艦5,味方艦5HP,味方艦6,味方艦6HP,敵艦1,敵艦1HP,敵艦2,敵艦2HP,敵艦3,敵艦3HP,敵艦4,敵艦4HP,敵艦5,敵艦5HP,敵艦6,敵艦6HP\r\n");
            }

            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "SJIS"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append(sdf.format(Calendar.getInstance()
                                         .getTime()) + ",");
            sb.append(battleresult.getQuestName() + ",");
            sb.append(cell + ",");
            if (isFirstBattle && eventId == 5) {
                sb.append("出撃&ボス");
            } else if (isFirstBattle) {
                sb.append("出撃");
            } else if (eventId == 5) {
                sb.append("ボス");
            } else {
                sb.append("");
            }
            sb.append(",");
            sb.append(battleresult.getRank());
            sb.append(",");
            sb.append(battle.getTactic());
            sb.append(",");
            sb.append(battle.getFormation());
            sb.append(",");
            sb.append(battle.geteFormation());
            sb.append(",");
            sb.append(battle.getSeiku());
            sb.append(",");
            sb.append(battle.getTouchPlane());
            sb.append(",");
            sb.append(battle.getEtTouchPlane());
            sb.append(",");
            sb.append(battleresult.geteFleetName());
            sb.append(",");
            sb.append(battleresult.getGetShipType());
            sb.append(",");
            sb.append(battleresult.getGetShipName());
            sb.append(",");

            Deck deck = DeckManager.INSTANCE.getDeck(battle.getDeckId());
            List<Integer> shipId = deck.getShipId();
            List<Integer> eship = battle.geteShip();
            List<Integer> nowhps = battle.getNowhps();
            List<Integer> maxhps = battle.getMaxhps();
            for (int i = 1; i < 7; i++) {
                if (shipId.get(i - 1) == -1) {
                    sb.append(",,");
                    continue;
                }
                MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId.get(i - 1));
                MstShip mstShip = MstShipManager.INSTANCE.getMstShip(myShip.getShipId());
                sb.append(mstShip.getName());
                sb.append("(Lv");
                sb.append(myShip.getLv());
                sb.append("),");
                sb.append(nowhps.get(i));
                sb.append("/");
                sb.append(maxhps.get(i));
                sb.append(",");
            }

            for (int i = 7, j = 1; i < 13; i++, j++) {
                if (eship.get(j) == -1) {
                    sb.append(",");
                } else {
                    sb.append(MstShipManager.INSTANCE.getMstShip(eship.get(j)).getName());
                    if (!MstShipManager.INSTANCE.getMstShip(eship.get(j)).getYomi().equals("") && !MstShipManager.INSTANCE.getMstShip(eship.get(j)).getYomi().equals("-")) {
                        sb.append("(");
                        sb.append(MstShipManager.INSTANCE.getMstShip(eship.get(j)).getYomi());
                        sb.append(")");
                    }
                    sb.append(",");
                    sb.append(nowhps.get(i));
                    sb.append("/");
                    sb.append((maxhps.get(i)));
                }
                if (i == 12) {
                    break;
                }
                sb.append(",");
            }
            Log.d("battlelog", sb.toString());
            isFirstBattle = false;
            sb.append("\r\n");
            pw.write(sb.toString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

    }
}
