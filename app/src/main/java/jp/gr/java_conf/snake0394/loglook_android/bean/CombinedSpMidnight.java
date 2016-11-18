package jp.gr.java_conf.snake0394.loglook_android.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;

/**
 * 連合艦隊 開幕夜戦
 */
public class CombinedSpMidnight extends AbstractBattle {
    private BattleType battleType = BattleType.COMBINED_SP_MIDNIGHT;

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
    private String seiku = "";

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

    public CombinedSpMidnight() {
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

            setSeiku("");
            JSONArray touchPlane = battledata.getJSONArray("api_touch_plane");
            setTouchPlane((MstSlotitemManager.INSTANCE.getMstSlotitem(touchPlane.getInt(0))).getName());
            setEtTouchPlane((MstSlotitemManager.INSTANCE.getMstSlotitem(touchPlane.getInt(1))).getName());

            JSONObject hougeki1 = battledata.getJSONObject("api_hougeki");
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
                    if (t.getInt(j) == -1) {
                        continue;
                    }
                    damageSum += t.getInt(j);
                }
                tIntList.add(damageSum);
            }
            setHougekiDamage1(tIntList);

        } catch (Exception e) {

        }
    }
}