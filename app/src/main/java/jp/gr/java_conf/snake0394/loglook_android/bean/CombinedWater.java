package jp.gr.java_conf.snake0394.loglook_android.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;

/**
 * 連合艦隊 水上部隊
 */
public class CombinedWater extends AbstractBattle {
    private BattleType battleType = BattleType.COMBINED_WATER;

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
     * 支援攻撃による敵へのダメージ。
     * [0]=-1,[1]～[6]=敵が受けたダメージ。
     */
    private List<Integer> supportEnemyDamage = null;

    private List<Integer> openingTaisenAtList = null;

    private List<Integer> openingTaisenDfList = null;

    private List<Integer> openingTaisenDamage = null;

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
    private List<Integer> HougekiAtList1 = null;

    /**
     * 砲撃戦の攻撃を受ける艦の位置。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は攻撃を受ける順に1～6=味方位置、7～12=敵位置が入る。
     */
    private List<Integer> HougekiDfList1 = null;

    /**
     * 砲撃戦によって与えたダメージ。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は与えたダメージ。
     */
    private List<Integer> HougekiDamage1 = null;

    /**
     * 砲撃戦の行動順
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は行動順に1～6=味方位置、7～12=敵位置が入る。
     */
    private List<Integer> HougekiAtList2 = null;

    /**
     * 砲撃戦の攻撃を受ける艦の位置。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は攻撃を受ける順に1～6=味方位置、7～12=敵位置が入る。
     */
    private List<Integer> HougekiDfList2 = null;

    /**
     * 砲撃戦によって与えたダメージ。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は与えたダメージ。
     */
    private List<Integer> HougekiDamage2 = null;

    /**
     * 砲撃戦の行動順
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は行動順に1～6=味方位置、7～12=敵位置が入る。
     */
    private List<Integer> HougekiAtList3 = null;

    /**
     * 砲撃戦の攻撃を受ける艦の位置。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は攻撃を受ける順に1～6=味方位置、7～12=敵位置が入る。
     */
    private List<Integer> HougekiDfList3 = null;

    /**
     * 砲撃戦によって与えたダメージ。
     * HougekiAtListに対応している。
     * サイズは砲撃戦の回数。
     * 格納されているリスト[0]=-1,それ以降は与えたダメージ。
     */
    private List<Integer> HougekiDamage3 = null;

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

    public CombinedWater() {
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

    public List<Integer> getSupportEnemyDamage() {
        return supportEnemyDamage;
    }

    public void setSupportEnemyDamage(List<Integer> supportEnemyDamage) {
        this.supportEnemyDamage = supportEnemyDamage;
    }

    @Override
    public List<Integer> getOpeningTaisenAtList() {
        return openingTaisenAtList;
    }

    public void setOpeningTaisenAtList(List<Integer> openingTaisenAtList) {
        this.openingTaisenAtList = openingTaisenAtList;
    }

    @Override
    public List<Integer> getOpeningTaisenDfList() {
        return openingTaisenDfList;
    }

    public void setOpeningTaisenDfList(List<Integer> openingTaisenDfList) {
        this.openingTaisenDfList = openingTaisenDfList;
    }

    @Override
    public List<Integer> getOpeningTaisenDamage() {
        return openingTaisenDamage;
    }

    public void setOpeningTaisenDamage(List<Integer> openingTaisenDamage) {
        this.openingTaisenDamage = openingTaisenDamage;
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

    public List<Integer> getHougekiAtList1() {
        return HougekiAtList1;
    }

    public void setHougekiAtList1(List<Integer> hougekiAtList1) {
        HougekiAtList1 = hougekiAtList1;
    }

    public List<Integer> getHougekiDfList1() {
        return HougekiDfList1;
    }

    public void setHougekiDfList1(List<Integer> hougekiDfList1) {
        HougekiDfList1 = hougekiDfList1;
    }

    public List<Integer> getHougekiDamage1() {
        return HougekiDamage1;
    }

    public void setHougekiDamage1(List<Integer> hougekiDamage1) {
        HougekiDamage1 = hougekiDamage1;
    }

    public List<Integer> getHougekiAtList2() {
        return HougekiAtList2;
    }

    public void setHougekiAtList2(List<Integer> hougekiAtList2) {
        HougekiAtList2 = hougekiAtList2;
    }

    public List<Integer> getHougekiDfList2() {
        return HougekiDfList2;
    }

    public void setHougekiDfList2(List<Integer> hougekiDfList2) {
        HougekiDfList2 = hougekiDfList2;
    }

    public List<Integer> getHougekiDamage2() {
        return HougekiDamage2;
    }

    public void setHougekiDamage2(List<Integer> hougekiDamage2) {
        HougekiDamage2 = hougekiDamage2;
    }

    public List<Integer> getHougekiAtList3() {
        return HougekiAtList3;
    }

    public void setHougekiAtList3(List<Integer> hougekiAtList3) {
        HougekiAtList3 = hougekiAtList3;
    }

    public List<Integer> getHougekiDfList3() {
        return HougekiDfList3;
    }

    public void setHougekiDfList3(List<Integer> hougekiDfList3) {
        HougekiDfList3 = hougekiDfList3;
    }

    public List<Integer> getHougekiDamage3() {
        return HougekiDamage3;
    }

    public void setHougekiDamage3(List<Integer> hougekiDamage3) {
        HougekiDamage3 = hougekiDamage3;
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

            if (battledata.getInt("api_opening_taisen_flag") == 1) {
                JSONObject taisen = battledata.getJSONObject("api_opening_taisen");

                JSONArray atList = taisen.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                setOpeningTaisenAtList(tIntList);

                JSONArray dfList = taisen.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                setOpeningTaisenDfList(tIntList);

                JSONArray damage = taisen.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 1; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                setOpeningTaisenDamage(tIntList);
            }

            if (battledata.getInt("api_opening_flag") == 1) {
                JSONObject raigeki = battledata.getJSONObject("api_opening_atack");
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

            if (houraiFlag.getInt(0) == 1) {
                JSONObject hougeki1 = battledata.getJSONObject("api_hougeki1");
                JSONArray atList = hougeki1.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                setHougekiAtList1(tIntList);

                JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                setHougekiDfList1(tIntList);

                JSONArray damage = hougeki1.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 1; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                setHougekiDamage1(tIntList);
            }

            if (houraiFlag.getInt(1) == 1) {
                JSONObject hougeki1 = battledata.getJSONObject("api_hougeki2");
                JSONArray atList = hougeki1.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                setHougekiAtList2(tIntList);

                JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                setHougekiDfList2(tIntList);

                JSONArray damage = hougeki1.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 1; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                setHougekiDamage2(tIntList);
            }

            if (houraiFlag.getInt(2) == 1) {
                JSONObject hougeki1 = battledata.getJSONObject("api_hougeki3");
                JSONArray atList = hougeki1.getJSONArray("api_at_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < atList.length(); i++) {
                    tIntList.add(atList.getInt(i));
                }
                setHougekiAtList3(tIntList);
                JSONArray dfList = hougeki1.getJSONArray("api_df_list");
                tIntList = new ArrayList<>();
                for (int i = 1; i < dfList.length(); i++) {
                    tIntList.add(dfList.getJSONArray(i).getInt(0));
                }
                setHougekiDfList3(tIntList);

                JSONArray damage = hougeki1.getJSONArray("api_damage");
                tIntList = new ArrayList<>();
                for (int i = 1; i < damage.length(); i++) {
                    JSONArray t = damage.getJSONArray(i);
                    int damageSum = 0;
                    for (int j = 0; j < t.length(); j++) {
                        damageSum += t.getInt(j);
                    }
                    tIntList.add(damageSum);
                }
                setHougekiDamage3(tIntList);
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
