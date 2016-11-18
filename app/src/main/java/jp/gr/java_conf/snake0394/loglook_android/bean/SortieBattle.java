package jp.gr.java_conf.snake0394.loglook_android.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;

/**
 * Created by snake0394 on 2016/08/05.
 */
public enum SortieBattle {
    INSTANCE;

    BattleType battleType;

    /**
     * 出撃艦隊id
     */
    private int deckId;

    /**
     * 敵艦船。[0]=-1、[1]～[6]=敵艦船id
     */
    List<Integer> eShip;

    /**
     * 敵味方の現在hp。[0]=-1、[1]～[6]=味方hp、[7]～[12]=敵hp
     */
    List<Integer> nowhps;

    /**
     * 敵味方の最大hp。[0]=-1、[1]～[6]=味方hp、[7]～[12]=敵hp
     */
    List<Integer> maxhps;

    /**
     * 第2艦隊の現在hp。[0]=-1、[1]～[6]=味方hp
     */
    List<Integer> nowhpsCombined;

    /**
     * 第2艦隊の最大hp。[0]=-1、[1]～[6]=味方hp
     */
    List<Integer> maxhpsCombined;

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

    /**
     * 先制対潜による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> openingTaisenEnemyDamage = null;

    /**
     * 先制雷撃による味方へのダメージ。
     * [0]=-1,[1]～[6]=味方が受けたダメージ。
     */
    private List<Integer> openingAttackFriendDamage = null;

    /**
     * 先制雷撃による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> openingAttackEnemyDamage = null;

    /**
     * 砲撃戦の行動順
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は行動順に1～6=味方位置、7～12=敵位置が入る。
     */
    private List<List<Integer>> HougekiAtList = null;

    /**
     * 砲撃戦の攻撃を受ける艦の位置。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は攻撃を受ける順に1～6=味方位置、7～12=敵位置が入る。
     */
    private List<List<Integer>> HougekiDfList = null;

    /**
     * 砲撃戦によって与えたダメージ。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は与えたダメージ。
     */
    private List<List<Integer>> HougekiDamage = null;

    /**
     * 雷撃による味方へのダメージ。
     * [0]=-1,[1]～[6]=味方が受けたダメージ。
     */
    private List<Integer> raigekiFriendDamage = null;

    /**
     * 雷撃による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> raigekiEnemyDamage = null;

    private SortieBattle() {
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

    public List<Integer> getOpeningTaisenEnemyDamage() {
        return openingTaisenEnemyDamage;
    }

    public void setOpeningTaisenEnemyDamage(List<Integer> openingTaisenEnemyDamage) {
        this.openingTaisenEnemyDamage = openingTaisenEnemyDamage;
    }

    public List<Integer> getOpeningAttackFriendDamage() {
        return openingAttackFriendDamage;
    }

    public void setOpeningAttackFriendDamage(List<Integer> openingAttackFriendDamage) {
        this.openingAttackFriendDamage = openingAttackFriendDamage;
    }

    public List<Integer> getOpeningAttackEnemyDamage() {
        return openingAttackEnemyDamage;
    }

    public void setOpeningAttackEnemyDamage(List<Integer> openingAttackEnemyDamage) {
        this.openingAttackEnemyDamage = openingAttackEnemyDamage;
    }

    public List<List<Integer>> getHougekiAtList() {
        return HougekiAtList;
    }

    public void setHougekiAtList(List<List<Integer>> hougekiAtList) {
        HougekiAtList = hougekiAtList;
    }

    public List<List<Integer>> getHougekiDfList() {
        return HougekiDfList;
    }

    public void setHougekiDfList(List<List<Integer>> hougekiDfList) {
        HougekiDfList = hougekiDfList;
    }

    public List<List<Integer>> getHougekiDamage() {
        return HougekiDamage;
    }

    public void setHougekiDamage(List<List<Integer>> hougekiDamage) {
        HougekiDamage = hougekiDamage;
    }

    public List<Integer> getRaigekiFriendDamage() {
        return raigekiFriendDamage;
    }

    public void setRaigekiFriendDamage(List<Integer> raigekiFriendDamage) {
        this.raigekiFriendDamage = raigekiFriendDamage;
    }

    public List<Integer> getRaigekiEnemyDamage() {
        return raigekiEnemyDamage;
    }

    public void setRaigekiEnemyDamage(List<Integer> raigekiEnemyDamage) {
        this.raigekiEnemyDamage = raigekiEnemyDamage;
    }

    public void set(String jsonStr, BattleType battleType) {
        this.battleType = battleType;
        try {
            JSONObject battleroot = new JSONObject(jsonStr);
            JSONObject battledata = battleroot.getJSONObject("api_data");
            switch (battleType) {
                case BATTLE:
                case AIRBATTLE:
                case LD_AIRBATTLE:
                    setDeckId(battledata.getInt("api_dock_id"));
                    break;
                case SP_MIDNIGTH:
                case COMBINED_AIRBATTLE:
                case COMBINED_LD_AIRBATTLE:
                case COMBINED_BATTLE:
                case COMBINED_WATER:
                    setDeckId(battledata.getInt("api_deck_id"));
            }

            JSONArray shipKe = battledata.getJSONArray("api_ship_ke");
            List<Integer> tIntList = new ArrayList<>();
            for (int i = 0; i < shipKe.length(); i++) {
                tIntList.add(shipKe.getInt(i));
            }
            seteShip(tIntList);

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

            if (battleType == BattleType.COMBINED_AIRBATTLE || battleType == BattleType.COMBINED_WATER || battleType == BattleType.COMBINED_BATTLE || battleType == BattleType.COMBINED_LD_AIRBATTLE || battleType == BattleType.SP_MIDNIGTH) {
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
            }

            JSONArray formation = battledata.getJSONArray("api_formation");
            HashMap<Integer, String> formationMap = new HashMap<>();
            formationMap.put(1, "単縦陣");
            formationMap.put(2, "複縦陣");
            formationMap.put(3, "輪形陣");
            formationMap.put(4, "梯形陣");
            formationMap.put(5, "単横陣");
            formationMap.put(11, "第一警戒航行序列");
            formationMap.put(12, "第二警戒航行序列");
            formationMap.put(13, "第三警戒航行序列");
            formationMap.put(14, "第四警戒航行序列");


            HashMap<Integer, String> tacticMap = new HashMap<>();
            tacticMap.put(1, "同航戦");
            tacticMap.put(2, "反航戦");
            tacticMap.put(3, "T字戦有利");
            tacticMap.put(4, "T字戦不利");
            setFormation(formationMap.get(formation.getInt(0)));
            seteFormation(formationMap.get(formation.getInt(1)));
            setTactic(tacticMap.get(formation.getInt(2)));

            if (battleType == BattleType.SP_MIDNIGTH || battleType == BattleType.COMBINED_SP_MIDNIGHT) {
                setSeiku("");
                JSONArray touchPlane = battledata.getJSONArray("api_touch_plane");
                setTouchPlane((MstSlotitemManager.INSTANCE.getMstSlotitem(touchPlane.getInt(0))).getName());
                setEtTouchPlane((MstSlotitemManager.INSTANCE.getMstSlotitem(touchPlane.getInt(1))).getName());

                List<List<Integer>> tAtListList = new ArrayList<>();
                List<List<Integer>> tDfListList = new ArrayList<>();
                List<List<Integer>> tDamageListList = new ArrayList<>();
                JSONObject hougeki1 = battledata.getJSONObject("api_hougeki");
                JSONArray atList = hougeki1.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                tAtListList.add(tIntList);

                JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                tDfListList.add(tIntList);

                JSONArray damage = hougeki1.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 0; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        if (t.getInt(j) == -1) {
                            continue;
                        }
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                tDamageListList.add(tIntList);

                setHougekiAtList(tAtListList);
                setHougekiDfList(tDfListList);
                setHougekiDamage(tDamageListList);
                return;
            }

            if (battledata.has("api_air_base_attack")) {
                JSONArray airBaseAttack = battledata.getJSONArray("api_air_base_attack");
                List<List<Integer>> tIntListList = new ArrayList<>();
                for (int i = 0; i < airBaseAttack.length(); i++) {
                    JSONObject obj = airBaseAttack.getJSONObject(i);
                    JSONArray stage3 = obj.getJSONArray("api_stage3");
                    tIntList = new ArrayList<>();
                    for (int j = 0; j < stage3.length(); j++) {
                        tIntList.add(stage3.getInt(i));
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

            if (battleType == BattleType.AIRBATTLE || battleType == BattleType.COMBINED_AIRBATTLE && battledata.getJSONArray("api_stage_flag2").getInt(2) == 1) {
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

                if (battleType == BattleType.COMBINED_AIRBATTLE) {
                    JSONObject stage3Combined = kouku.getJSONObject("api_stage3_combined");
                    fdam = stage3Combined.getJSONArray("api_fdam");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < fdam.length(); i++) {
                        tIntList.add(fdam.getInt(i));
                    }
                    setKouku2FriendDamageCombined(tIntList);
                }
            }

            if (battleType == BattleType.AIRBATTLE || battleType == BattleType.LD_AIRBATTLE || battleType == BattleType.COMBINED_LD_AIRBATTLE) {
                return;
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

            if (battleType == BattleType.COMBINED_AIRBATTLE) {
                return;
            }

            if (battledata.getInt("api_opening_taisen_flag") == 1) {
                JSONObject taisen = battledata.getJSONObject("api_opening_taisen");
                JSONArray damage = taisen.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 0; i < damage.length(); i++) {
                    tIntList.add(damage.getInt(i));
                }
                setOpeningTaisenEnemyDamage(tIntList);
            }

            if (battledata.getInt("api_opening_flag") == 1) {
                JSONObject raigeki = battledata.getJSONObject(" api_opening_atack");
                JSONArray fdam = raigeki.getJSONArray("api_fdam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < fdam.length(); i++) {
                    tIntList.add(fdam.getInt(i));
                }
                setOpeningAttackFriendDamage(tIntList);
                JSONArray edam = raigeki.getJSONArray("api_edam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < edam.length(); i++) {
                    tIntList.add(edam.getInt(i));
                }
                setOpeningAttackEnemyDamage(tIntList);
            }

            JSONArray houraiFlag = battledata.getJSONArray("api_hourai_flag");
            List<List<Integer>> tAtListList = new ArrayList<>();
            List<List<Integer>> tDfListList = new ArrayList<>();
            List<List<Integer>> tDamageListList = new ArrayList<>();

            if (battleType == BattleType.COMBINED_BATTLE) {
                if (houraiFlag.getInt(0) == 1) {
                    JSONObject hougeki1 = battledata.getJSONObject("api_hougeki1");
                    JSONArray atList = hougeki1.getJSONArray("api_at_list");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < atList.length(); i++) {
                        tIntList.add(atList.getInt(i));
                    }
                    tAtListList.add(tIntList);

                    JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < dfList.length(); i++) {
                        tIntList.add(dfList.getJSONArray(i).getInt(0));
                    }
                    tDfListList.add(tIntList);

                    JSONArray damage = hougeki1.getJSONArray("api_damage");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < damage.length(); i++) {
                        JSONArray t = damage.getJSONArray(i);
                        int damageSum = 0;
                        for (int j = 0; j < t.length(); j++) {
                            damageSum += t.getInt(j);
                        }
                        tIntList.add(damageSum);
                    }
                    tDamageListList.add(tIntList);

                    setHougekiAtList(tAtListList);
                    setHougekiDfList(tDfListList);
                    setHougekiDamage(tDamageListList);
                }

                if (houraiFlag.getInt(1) == 1) {
                    JSONObject raigeki = battledata.getJSONObject("api_raigeki");
                    JSONArray fdam = raigeki.getJSONArray("api_fdam");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < fdam.length(); i++) {
                        tIntList.add(fdam.getInt(i));
                    }
                    setRaigekiFriendDamage(tIntList);

                    JSONArray edam = raigeki.getJSONArray("api_edam");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < edam.length(); i++) {
                        tIntList.add(edam.getInt(i));
                    }
                    setRaigekiEnemyDamage(tIntList);
                }

                if (houraiFlag.getInt(2) == 1) {
                    JSONObject hougeki1 = battledata.getJSONObject("api_hougeki2");
                    JSONArray atList = hougeki1.getJSONArray("api_at_list");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < atList.length(); i++) {
                        tIntList.add(atList.getInt(i));
                    }
                    tAtListList.add(tIntList);

                    JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < dfList.length(); i++) {
                        tIntList.add(dfList.getJSONArray(i).getInt(0));
                    }
                    tDfListList.add(tIntList);

                    JSONArray damage = hougeki1.getJSONArray("api_damage");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < damage.length(); i++) {
                        JSONArray t = damage.getJSONArray(i);
                        int damageSum = 0;
                        for (int j = 0; j < t.length(); j++) {
                            damageSum += t.getInt(j);
                        }
                        tIntList.add(damageSum);
                    }
                    tDamageListList.add(tIntList);

                    setHougekiAtList(tAtListList);
                    setHougekiDfList(tDfListList);
                    setHougekiDamage(tDamageListList);
                }

                if (houraiFlag.getInt(3) == 1) {
                    JSONObject hougeki1 = battledata.getJSONObject("api_hougeki3");
                    JSONArray atList = hougeki1.getJSONArray("api_at_list");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < atList.length(); i++) {
                        tIntList.add(atList.getInt(i));
                    }
                    tAtListList.add(tIntList);

                    JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < dfList.length(); i++) {
                        tIntList.add(dfList.getJSONArray(i).getInt(0));
                    }
                    tDfListList.add(tIntList);

                    JSONArray damage = hougeki1.getJSONArray("api_damage");
                    tIntList = new ArrayList<>();
                    for (int i = 0; i < damage.length(); i++) {
                        JSONArray t = damage.getJSONArray(i);
                        int damageSum = 0;
                        for (int j = 0; j < t.length(); j++) {
                            damageSum += t.getInt(j);
                        }
                        tIntList.add(damageSum);
                    }
                    tDamageListList.add(tIntList);

                    setHougekiAtList(tAtListList);
                    setHougekiDfList(tDfListList);
                    setHougekiDamage(tDamageListList);
                }

                return;
            }

            if (houraiFlag.getInt(0) == 1) {
                JSONObject hougeki1 = battledata.getJSONObject("api_hougeki1");
                JSONArray atList = hougeki1.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                tAtListList.add(tIntList);

                JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                tDfListList.add(tIntList);

                JSONArray damage = hougeki1.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 0; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                tDamageListList.add(tIntList);

                setHougekiAtList(tAtListList);
                setHougekiDfList(tDfListList);
                setHougekiDamage(tDamageListList);
            }

            if (houraiFlag.getInt(1) == 1) {
                JSONObject hougeki1 = battledata.getJSONObject("api_hougeki2");
                JSONArray atList = hougeki1.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                tAtListList.add(tIntList);

                JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                tDfListList.add(tIntList);

                JSONArray damage = hougeki1.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 0; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                tDamageListList.add(tIntList);

                setHougekiAtList(tAtListList);
                setHougekiDfList(tDfListList);
                setHougekiDamage(tDamageListList);
            }

            if (houraiFlag.getInt(2) == 1) {
                JSONObject hougeki1 = battledata.getJSONObject("api_hougeki3");
                JSONArray atList = hougeki1.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                tAtListList.add(tIntList);

                JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 0; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                tDfListList.add(tIntList);

                JSONArray damage = hougeki1.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 0; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                tDamageListList.add(tIntList);

                setHougekiAtList(tAtListList);
                setHougekiDfList(tDfListList);
                setHougekiDamage(tDamageListList);
            }

            if (houraiFlag.getInt(3) == 1) {
                JSONObject raigeki = battledata.getJSONObject("api_raigeki");
                JSONArray fdam = raigeki.getJSONArray("api_fdam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < fdam.length(); i++) {
                    tIntList.add(fdam.getInt(i));
                }
                setRaigekiFriendDamage(tIntList);

                JSONArray edam = raigeki.getJSONArray("api_edam");
                tIntList = new ArrayList<>();
                for (int i = 0; i < edam.length(); i++) {
                    tIntList.add(edam.getInt(i));
                }
                setRaigekiEnemyDamage(tIntList);
            }
        } catch (Exception e) {

        }
    }
}