package jp.gr.java_conf.snake0394.loglook_android.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;

/**
 * 長距離空襲
 */
public class LdAirbattle extends AbstractBattle {
    private BattleType battleType = BattleType.LD_AIRBATTLE;

    /**
     * 出撃艦隊id
     */
    private int deckId;

    /**
     * 敵艦船。[0]=-1、[1]～[6]=敵艦船id
     */
    List<Integer> eShip;

    /**
     * 敵艦船のレベル。[0]=-1、[1]～[6]=レベル
     */
    private List<Integer> eShipLv;

    /**
     * 敵味方の現在hp。[0]=-1、[1]～[6]=味方hp、[7]～[12]=敵hp
     */
    List<Integer> nowhps;

    /**
     * 敵味方の最大hp。[0]=-1、[1]～[6]=味方hp、[7]～[12]=敵hp
     */
    List<Integer> maxhps;

    /**
     * 味方陣形
     */
    private String formation;

    /**
     * 敵陣形
     */
    private String eFormation;

    /**
     * 味方触接機
     */
    private String touchPlane;

    /**
     * 敵触接機
     */
    private String etTouchPlane;

    /**
     * 交戦形態
     */
    private String tactic;

    /**
     * 制空状態
     */
    private String seiku;

    /**
     * 基地航空隊による敵へのダメージ。
     * サイズは航空隊による攻撃の回数。
     * 格納されているリスト[0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<List<Integer>> baseEnemyDamage = null;

    /**
     * 艦載機による味方へのダメージ。
     * [0]=-1,[1]～[6]=味方が受けたダメージ。
     */
    private List<Integer> koukuFriendDamage = null;

    /**
     * 艦載機による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> koukuEnemyDamage = null;

    public LdAirbattle() {
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public List<Integer> geteShip() {
        return eShip;
    }

    public void seteShip(List<Integer> eShip) {
        this.eShip = eShip;
    }

    public List<Integer> geteShipLv() {
        return eShipLv;
    }

    public void seteShipLv(List<Integer> eShipLv) {
        this.eShipLv = eShipLv;
    }

    public List<Integer> getNowhps() {
        return nowhps;
    }

    public void setNowhps(List<Integer> nowhps) {
        this.nowhps = nowhps;
    }

    public List<Integer> getMaxhps() {
        return maxhps;
    }

    public void setMaxhps(List<Integer> maxhps) {
        this.maxhps = maxhps;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String geteFormation() {
        return eFormation;
    }

    public void seteFormation(String eFormation) {
        this.eFormation = eFormation;
    }

    public String getTouchPlane() {
        return touchPlane;
    }

    public void setTouchPlane(String touchPlane) {
        this.touchPlane = touchPlane;
    }

    public String getEtTouchPlane() {
        return etTouchPlane;
    }

    public void setEtTouchPlane(String etTouchPlane) {
        this.etTouchPlane = etTouchPlane;
    }

    public String getTactic() {
        return tactic;
    }

    public void setTactic(String tactic) {
        this.tactic = tactic;
    }

    public String getSeiku() {
        return seiku;
    }

    public void setSeiku(String seiku) {
        this.seiku = seiku;
    }

    public List<List<Integer>> getBaseEnemyDamage() {
        return baseEnemyDamage;
    }

    public void setBaseEnemyDamage(List<List<Integer>> baseEnemyDamage) {
        this.baseEnemyDamage = baseEnemyDamage;
    }

    public List<Integer> getKoukuFriendDamage() {
        return koukuFriendDamage;
    }

    public void setKoukuFriendDamage(List<Integer> koukuFriendDamage) {
        this.koukuFriendDamage = koukuFriendDamage;
    }

    public List<Integer> getKoukuEnemyDamage() {
        return koukuEnemyDamage;
    }

    public void setKoukuEnemyDamage(List<Integer> koukuEnemyDamage) {
        this.koukuEnemyDamage = koukuEnemyDamage;
    }

    public void set(String jsonStr) {
        try {
            JSONObject battleroot = new JSONObject(jsonStr);
            JSONObject battledata = battleroot.getJSONObject("api_data");
            setDeckId(battledata.getInt("api_dock_id"));

            JSONArray shipKe = battledata.getJSONArray("api_ship_ke");
            List<Integer> tIntList = new ArrayList<>();
            for (int i = 0; i < shipKe.length(); i++) {
                tIntList.add(shipKe.getInt(i));
            }
            seteShip(tIntList);

            JSONArray shipLv = battledata.getJSONArray("api_ship_lv");
            tIntList = new ArrayList<>();
            for (int i = 0; i < shipLv.length(); i++) {
                tIntList.add(shipLv.getInt(i));
            }
            seteShipLv(tIntList);

            JSONArray nowhps = battledata.getJSONArray("api_nowhps");
            tIntList = new ArrayList<>();
            for (int i = 0; i < nowhps.length(); i++) {
                tIntList.add(nowhps.getInt(i));
            }
            setNowhps(tIntList);

            JSONArray maxhps = battledata.getJSONArray("api_maxhps");
            tIntList = new ArrayList<>();
            for (int i = 0; i < maxhps.length(); i++) {
                tIntList.add(maxhps.getInt(i));
            }
            setMaxhps(tIntList);

            JSONArray formation = battledata.getJSONArray("api_formation");
            switch (formation.getInt(0)) {
                case 1:
                    setFormation("単縦陣");
                    break;
                case 2:
                    setFormation("複縦陣");
                    break;
                case 3:
                    setFormation("輪形陣");
                    break;
                case 4:
                    setFormation("梯形陣");
                    break;
                case 5:
                    setFormation("単横陣");
                    break;
            }

            switch (formation.getInt(1)) {
                case 1:
                    seteFormation("単縦陣");
                    break;
                case 2:
                    seteFormation("複縦陣");
                    break;
                case 3:
                    seteFormation("輪形陣");
                    break;
                case 4:
                    seteFormation("梯形陣");
                    break;
                case 5:
                    seteFormation("単横陣");
                    break;
            }

            switch (formation.getInt(2)) {
                case 1:
                    setTactic("同航戦");
                    break;
                case 2:
                    setTactic("反航戦");
                    break;
                case 3:
                    setTactic("T字戦有利");
                    break;
                case 4:
                    setTactic("T字戦不利");
                    break;
            }

            if (battledata.has("api_air_base_attack")) {
                JSONArray airBaseAttack = battledata.getJSONArray("api_air_base_attack");
                List<List<Integer>> tIntListList = new ArrayList<>();
                for (int i = 0; i < airBaseAttack.length(); i++) {
                    JSONObject obj = airBaseAttack.getJSONObject(i);
                    JSONObject stage3 = obj.getJSONObject("api_stage3");
                    JSONArray edam = stage3.getJSONArray("api_edam");
                    tIntList = new ArrayList<>();
                    for (int j = 0; j < edam.length(); j++) {
                        tIntList.add(edam.getInt(j));
                    }
                    tIntListList.add(tIntList);
                }
                setBaseEnemyDamage(tIntListList);
            }

            JSONObject kouku = battledata.getJSONObject("api_kouku");
            JSONObject stage1 = kouku.getJSONObject("api_stage1");
            HashMap<Integer, String> seikuMap = new HashMap<>();
            seikuMap.put(0, "制空均衡");
            seikuMap.put(1, "制空権確保");
            seikuMap.put(2, "航空優勢");
            seikuMap.put(3, "航空劣勢");
            seikuMap.put(4, "制空権喪失");
            setSeiku(seikuMap.get(stage1.getInt("api_disp_seiku")));
            JSONArray touchPlane = stage1.getJSONArray("api_touch_plane");
            setTouchPlane((MstSlotitemManager.INSTANCE.getMstSlotitem(touchPlane.getInt(0))).getName());
            setEtTouchPlane((MstSlotitemManager.INSTANCE.getMstSlotitem(touchPlane.getInt(1))).getName());
            if (battledata.getJSONArray("api_stage_flag").getInt(2) == 1) {
                JSONObject stage3 = kouku.getJSONObject("api_stage3");
                JSONArray fdam = stage3.getJSONArray("api_fdam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < fdam.length(); i++) {
                    tIntList.add(fdam.getInt(i));
                }
                setKoukuFriendDamage(tIntList);
                JSONArray edam = stage3.getJSONArray("api_edam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < edam.length(); i++) {
                    tIntList.add(edam.getInt(i));
                }
                setKoukuEnemyDamage(tIntList);
            }
        } catch (Exception e) {

        }
    }
}
