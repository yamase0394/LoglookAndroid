package jp.gr.java_conf.snake0394.loglook_android.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;

/**
 * 連合艦隊 航空戦
 */
public class CombinedAirbattle extends AbstractBattle {
    private BattleType battleType = BattleType.COMBINED_AIRBATTLE;

    /**
     * 出撃艦隊id
     */
    private int deckId;

    /**
     * 敵艦船。[0]=-1、[1]～[6]=敵艦船id
     */
    private List<Integer> eShip;

    /**
     * 敵艦船のレベル。[0]=-1、[1]～[6]=レベル
     */
    private List<Integer> eShipLv;

    /**
     * 敵味方の現在hp。[0]=-1、[1]～[6]=味方hp、[7]～[12]=敵hp
     */
    private List<Integer> nowhps;

    /**
     * 敵味方の最大hp。[0]=-1、[1]～[6]=味方hp、[7]～[12]=敵hp
     */
    private List<Integer> maxhps;

    /**
     * 第2艦隊の現在hp。[0]=-1、[1]～[6]=味方hp
     */
    private List<Integer> nowhpsCombined;

    /**
     * 第2艦隊の最大hp。[0]=-1、[1]～[6]=味方hp
     */
    private List<Integer> maxhpsCombined;

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
    private List<Integer> baseInjectionEnemyDamage = null;

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
    private List<Integer> injectionKoukuFriendDamage = null;

    /**
     * 艦載機による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> injectionKoukuEnemyDamage = null;

    /**
     * 艦載機による第2艦隊へのダメージ。
     * [0]=-1,[1]～[6]=味方が受けたダメージ。
     */
    private List<Integer> injectionkoukuFriendDamageCombined = null;


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

    /**
     * 艦載機による第2艦隊へのダメージ。
     * [0]=-1,[1]～[6]=味方が受けたダメージ。
     */
    private List<Integer> koukuFriendDamageCombined = null;


    /**
     * 艦載機による味方へのダメージ。
     * [0]=-1,[1]～[6]=味方が受けたダメージ。
     */
    private List<Integer> kouku2FriendDamage = null;

    /**
     * 艦載機による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> kouku2EnemyDamage = null;

    /**
     * 艦載機による第2艦隊へのダメージ。
     * [0]=-1,[1]～[6]=味方が受けたダメージ。
     */
    private List<Integer> kouku2FriendDamageCombined = null;

    /**
     * 支援攻撃による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> supportEnemyDamage = null;

    public CombinedAirbattle() {
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

    public List<Integer> getNowhpsCombined() {
        return nowhpsCombined;
    }

    public void setNowhpsCombined(List<Integer> nowhpsCombined) {
        this.nowhpsCombined = nowhpsCombined;
    }

    public List<Integer> getMaxhpsCombined() {
        return maxhpsCombined;
    }

    public void setMaxhpsCombined(List<Integer> maxhpsCombined) {
        this.maxhpsCombined = maxhpsCombined;
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

    public List<Integer> getKoukuFriendDamageCombined() {
        return koukuFriendDamageCombined;
    }

    public void setKoukuFriendDamageCombined(List<Integer> koukuFriendDamageCombined) {
        this.koukuFriendDamageCombined = koukuFriendDamageCombined;
    }

    public List<Integer> getKouku2FriendDamage() {
        return kouku2FriendDamage;
    }

    public void setKouku2FriendDamage(List<Integer> kouku2FriendDamage) {
        this.kouku2FriendDamage = kouku2FriendDamage;
    }

    public List<Integer> getKouku2EnemyDamage() {
        return kouku2EnemyDamage;
    }

    public void setKouku2EnemyDamage(List<Integer> kouku2EnemyDamage) {
        this.kouku2EnemyDamage = kouku2EnemyDamage;
    }

    public List<Integer> getKouku2FriendDamageCombined() {
        return kouku2FriendDamageCombined;
    }

    public void setKouku2FriendDamageCombined(List<Integer> kouku2FriendDamageCombined) {
        this.kouku2FriendDamageCombined = kouku2FriendDamageCombined;
    }

    public List<Integer> getSupportEnemyDamage() {
        return supportEnemyDamage;
    }

    public void setSupportEnemyDamage(List<Integer> supportEnemyDamage) {
        this.supportEnemyDamage = supportEnemyDamage;
    }

    @Override
    public List<Integer> getBaseInjectionEnemyDamage() {
        return baseInjectionEnemyDamage;
    }

    public void setBaseInjectionEnemyDamage(List<Integer> baseInjectionEnemyDamage) {
        this.baseInjectionEnemyDamage = baseInjectionEnemyDamage;
    }

    @Override
    public List<Integer> getInjectionKoukuFriendDamage() {
        return injectionKoukuFriendDamage;
    }

    public void setInjectionKoukuFriendDamage(List<Integer> injectionKoukuFriendDamage) {
        this.injectionKoukuFriendDamage = injectionKoukuFriendDamage;
    }

    @Override
    public List<Integer> getInjectionKoukuEnemyDamage() {
        return injectionKoukuEnemyDamage;
    }

    public void setInjectionKoukuEnemyDamage(List<Integer> injectionKoukuEnemyDamage) {
        this.injectionKoukuEnemyDamage = injectionKoukuEnemyDamage;
    }

    public List<Integer> getInjectionkoukuFriendDamageCombined() {
        return injectionkoukuFriendDamageCombined;
    }

    public void setInjectionkoukuFriendDamageCombined(List<Integer> injectionkoukuFriendDamageCombined) {
        this.injectionkoukuFriendDamageCombined = injectionkoukuFriendDamageCombined;
    }

    public void set(String jsonStr) {
        try {
            JSONObject battleroot = new JSONObject(jsonStr);
            JSONObject battledata = battleroot.getJSONObject("api_data");
            setDeckId(battledata.getInt("api_deck_id"));

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

            JSONArray nowhpsCombined = battledata.getJSONArray("api_nowhps_combined");
            tIntList = new ArrayList<>();
            for (int i = 0; i < nowhpsCombined.length(); i++) {
                tIntList.add(nowhpsCombined.getInt(i));
            }
            setNowhpsCombined(tIntList);

            JSONArray maxhpsCombined = battledata.getJSONArray("api_maxhps_combined");
            tIntList = new ArrayList<>();
            for (int i = 0; i < maxhpsCombined.length(); i++) {
                tIntList.add(maxhpsCombined.getInt(i));
            }
            setMaxhpsCombined(tIntList);

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
                case 11:
                    setFormation("第一警戒航行序列");
                    break;
                case 12:
                    setFormation("第二警戒航行序列");
                    break;
                case 13:
                    setFormation("第三警戒航行序列");
                    break;
                case 14:
                    setFormation("第四警戒航行序列");
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
                case 11:
                    seteFormation("第一警戒航行序列");
                    break;
                case 12:
                    seteFormation("第二警戒航行序列");
                    break;
                case 13:
                    seteFormation("第三警戒航行序列");
                    break;
                case 14:
                    seteFormation("第四警戒航行序列");
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

            if (battledata.has("api_air_base_injection")) {
                JSONObject airBaseAttack = battledata.getJSONObject("api_air_base_injection");
                JSONObject stage3 = airBaseAttack.getJSONObject("api_stage3");
                JSONArray edam = stage3.getJSONArray("api_edam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < edam.length(); i++) {
                    tIntList.add(edam.getInt(i));
                }
                setBaseInjectionEnemyDamage(tIntList);
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

            if (battledata.has("api_injection_kouku")) {
                JSONObject injectionKouku = battledata.getJSONObject("api_injection_kouku");
                JSONObject stage3 = injectionKouku.getJSONObject("api_stage3");

                JSONArray fdam = stage3.getJSONArray("api_fdam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < fdam.length(); i++) {
                    tIntList.add(fdam.getInt(i));
                }
                setInjectionKoukuFriendDamage(tIntList);

                JSONArray edam = stage3.getJSONArray("api_edam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < edam.length(); i++) {
                    tIntList.add(edam.getInt(i));
                }
                setInjectionKoukuEnemyDamage(tIntList);

                if (injectionKouku.has("api_stage3_combined")) {
                    JSONObject stage3Combined = injectionKouku.getJSONObject("api_stage3_combined");
                    fdam = stage3Combined.getJSONArray("api_fdam");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < fdam.length(); i++) {
                        tIntList.add(fdam.getInt(i));
                    }
                    setInjectionkoukuFriendDamageCombined(tIntList);
                }
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

                if (battleType == BattleType.COMBINED_AIRBATTLE || battleType == BattleType.COMBINED_WATER || battleType == BattleType.COMBINED_LD_AIRBATTLE || battleType == BattleType.COMBINED_AIRBATTLE) {
                    JSONObject stage3Combined = kouku.getJSONObject("api_stage3_combined");
                    fdam = stage3Combined.getJSONArray("api_fdam");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < fdam.length(); i++) {
                        tIntList.add(fdam.getInt(i));
                    }
                    setKoukuFriendDamageCombined(tIntList);
                }
            }

            if (battledata.getJSONArray("api_stage_flag2").getInt(2) == 1) {
                JSONObject kouku2 = battledata.getJSONObject("api_kouku2");
                JSONObject stage3 = kouku2.getJSONObject("api_stage3");
                JSONArray fdam = stage3.getJSONArray("api_fdam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < fdam.length(); i++) {
                    tIntList.add(fdam.getInt(i));
                }
                setKouku2FriendDamage(tIntList);
                JSONArray edam = stage3.getJSONArray("api_edam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < edam.length(); i++) {
                    tIntList.add(edam.getInt(i));
                }
                setKouku2EnemyDamage(tIntList);

                JSONObject stage3Combined = kouku.getJSONObject("api_stage3_combined");
                fdam = stage3Combined.getJSONArray("api_fdam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < fdam.length(); i++) {
                    tIntList.add(fdam.getInt(i));
                }
                setKouku2FriendDamageCombined(tIntList);
            }

            switch (battledata.getInt("api_support_flag")) {
                case 1:
                    JSONObject support = battledata.getJSONObject("api_support_info");
                    JSONObject air = support.getJSONObject("api_support_airatack");
                    JSONObject stage3 = air.getJSONObject("api_stage3");
                    JSONArray edam = stage3.getJSONArray("api_edam");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < edam.length(); i++) {
                        tIntList.add(edam.getInt(i));
                    }
                    setSupportEnemyDamage(tIntList);
                    break;
                case 2:
                case 3:
                    support = battledata.getJSONObject("api_support_info");
                    JSONObject hourai = support.getJSONObject("api_support_hourai");
                    JSONArray damage = hourai.getJSONArray("api_damage");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < damage.length(); i++) {
                        tIntList.add(damage.getInt(i));
                    }
                    setSupportEnemyDamage(tIntList);
                    break;
            }
        } catch (Exception e) {

        }
    }
}
