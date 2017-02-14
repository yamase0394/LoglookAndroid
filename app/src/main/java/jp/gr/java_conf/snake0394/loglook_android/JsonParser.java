package jp.gr.java_conf.snake0394.loglook_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.bean.Airbattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.Basic;
import jp.gr.java_conf.snake0394.loglook_android.bean.Battle;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedAirbattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedEC;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedEach;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedEachWater;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedLdAirbattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedSpMidnight;
import jp.gr.java_conf.snake0394.loglook_android.bean.CombinedWater;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.Kdock;
import jp.gr.java_conf.snake0394.loglook_android.bean.LdAirbattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.Material;
import jp.gr.java_conf.snake0394.loglook_android.bean.MissionResult;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMission;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstMissionManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstUseitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.PracticeBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.ShipMap;
import jp.gr.java_conf.snake0394.loglook_android.bean.SortieBattleresult;
import jp.gr.java_conf.snake0394.loglook_android.bean.SpMidnightBattle;
import jp.gr.java_conf.snake0394.loglook_android.bean.TacticalSituation;
import jp.gr.java_conf.snake0394.loglook_android.logger.BattleLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.CreateItemLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.CreateShipLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.MaterialLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.MissionLogger;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.HeavilyDamagedWarningActivity;

/**
 * Created by snake0394 on 2016/08/05.
 */
public class JsonParser {

    public synchronized static void parse(String uri, String jsonStr) {
        Context context = App.getInstance();
        try {
            //Log.d("JsonParser", uri);
            //uriでJSONの解析処理を切り替える
            switch (uri) {
                //起動時にキャッシュがない場合
                case "api_start2":
                    //root
                    JSONObject start2Root = new JSONObject(jsonStr);
                    //root/data
                    JSONObject start2Data = start2Root.getJSONObject("api_data");
                    //root/data/mst_ship
                    JSONArray mstShipArray = start2Data.getJSONArray("api_mst_ship");
                    JSONObject[] shipObject = new JSONObject[mstShipArray.length()];
                    for (int i = 0; i < mstShipArray.length(); i++) {
                        shipObject[i] = mstShipArray.getJSONObject(i);
                        MstShip mstShip = new MstShip();
                        mstShip.setId(shipObject[i].getInt("api_id"));
                        mstShip.setName(shipObject[i].getString("api_name"));
                        mstShip.setYomi(shipObject[i].getString("api_yomi"));
                        mstShip.setStype(shipObject[i].getInt("api_stype"));
                        try {
                            mstShip.setFuelMax(shipObject[i].getInt("api_fuel_max"));
                            mstShip.setBullMax(shipObject[i].getInt("api_bull_max"));
                        } catch (Exception e) {
                        }
                        MstShipManager.INSTANCE.put(mstShip);
                    }
                    //MstShipのnullオブジェクトをput
                    MstShip mstShip = new MstShip();
                    mstShip.setId(-1);
                    mstShip.setName("");
                    mstShip.setYomi("");
                    MstShipManager.INSTANCE.put(mstShip);
                    //MstShipManagerをシリアライズ
                    MstShipManager.INSTANCE.serialize();


                    //root/data/mst_slotitem
                    JSONArray slotitem = start2Data.getJSONArray("api_mst_slotitem");
                    int slotitemLength = slotitem.length();
                    JSONObject[] slotitemobjext = new JSONObject[slotitemLength];
                    for (int i = 0; i < slotitemLength; i++) {
                        slotitemobjext[i] = slotitem.getJSONObject(i);
                        MstSlotitem ms = new MstSlotitem();
                        ms.setId(slotitemobjext[i].getInt("api_id"));
                        ms.setSortno(slotitemobjext[i].getInt("api_sortno"));
                        ms.setName(slotitemobjext[i].getString("api_name"));
                        List<Integer> tList = new ArrayList<>();
                        JSONArray typeArry = slotitemobjext[i].getJSONArray("api_type");
                        tList.add(typeArry.getInt(0));
                        tList.add(typeArry.getInt(1));
                        tList.add(typeArry.getInt(2));
                        tList.add(typeArry.getInt(3));
                        ms.setType(tList);
                        ms.setTaik(slotitemobjext[i].getInt("api_taik"));
                        ms.setSouk(slotitemobjext[i].getInt("api_souk"));
                        ms.setHoug(slotitemobjext[i].getInt("api_houg"));
                        ms.setRaig(slotitemobjext[i].getInt("api_raig"));
                        ms.setSoku(slotitemobjext[i].getInt("api_soku"));
                        ms.setBaku(slotitemobjext[i].getInt("api_baku"));
                        ms.setTyku(slotitemobjext[i].getInt("api_tyku"));
                        ms.setTais(slotitemobjext[i].getInt("api_tais"));
                        ms.setAtap(slotitemobjext[i].getInt("api_atap"));
                        ms.setHoum(slotitemobjext[i].getInt("api_houm"));
                        ms.setRaim(slotitemobjext[i].getInt("api_raim"));
                        ms.setHouk(slotitemobjext[i].getInt("api_houk"));
                        ms.setRaik(slotitemobjext[i].getInt("api_raik"));
                        ms.setBakk(slotitemobjext[i].getInt("api_bakk"));
                        ms.setSaku(slotitemobjext[i].getInt("api_saku"));
                        ms.setSakb(slotitemobjext[i].getInt("api_sakb"));
                        ms.setLuck(slotitemobjext[i].getInt("api_luck"));
                        ms.setLeng(slotitemobjext[i].getInt("api_leng"));
                        ms.setRare(slotitemobjext[i].getInt("api_rare"));
                        tList = new ArrayList<>();
                        typeArry = slotitemobjext[i].getJSONArray("api_broken");
                        tList.add(typeArry.getInt(0));
                        tList.add(typeArry.getInt(1));
                        tList.add(typeArry.getInt(2));
                        tList.add(typeArry.getInt(3));
                        ms.setBroken(tList);
                        ms.setInfo(slotitemobjext[i].getString("api_info"));
                        ms.setUsebull(slotitemobjext[i].getString("api_usebull"));
                        try {
                            ms.setCost(slotitemobjext[i].getInt("api_cost"));
                            ms.setDistance(slotitemobjext[i].getInt("api_distance"));
                        } catch (Exception e) {
                        }
                        MstSlotitemManager.INSTANCE.put(ms);
                    }
                    MstSlotitem ms = new MstSlotitem();
                    ms.setId(-1);
                    ms.setName("");
                    MstSlotitemManager.INSTANCE.put(ms);
                    //MstSlotMapをシリアライズ
                    MstSlotitemManager.INSTANCE.serialize();


                    //root/data/mst_useitem
                    JSONArray useitemList = start2Data.getJSONArray("api_mst_useitem");
                    JSONObject[] useitemObj = new JSONObject[useitemList.length()];
                    for (int i = 0; i < useitemList.length(); i++) {
                        MstUseitem mstUseitem = new MstUseitem();
                        useitemObj[i] = useitemList.getJSONObject(i);
                        mstUseitem.setId(useitemObj[i].getInt("api_id"));
                        mstUseitem.setName(useitemObj[i].getString("api_name"));
                        MstUseitemManager.INSTANCE.put(mstUseitem);
                    }
                    //MstUseitemManagerをシリアライズ
                    MstUseitemManager.INSTANCE.serialize();

                    //root/data/mst_mission
                    JSONArray mission = start2Data.getJSONArray("api_mst_mission");
                    JSONObject[] missionObj = new JSONObject[mission.length()];
                    for (int i = 0; i < mission.length(); i++) {
                        missionObj[i] = mission.getJSONObject(i);
                        MstMission mstmission = new MstMission();
                        mstmission.setId(missionObj[i].getInt("api_id"));
                        mstmission.setMapareaId(missionObj[i].getInt("api_maparea_id"));
                        mstmission.setName(missionObj[i].getString("api_name"));
                        mstmission.setTime(missionObj[i].getInt("api_time"));
                        List<Integer> mstmissionlist = new ArrayList<>();
                        JSONArray winitem = missionObj[i].getJSONArray("api_win_item1");
                        mstmissionlist.add(winitem.getInt(0));
                        mstmissionlist.add(winitem.getInt(1));
                        mstmission.setWinItem1(mstmissionlist);
                        mstmissionlist = new ArrayList<>();
                        winitem = missionObj[i].getJSONArray("api_win_item2");
                        mstmissionlist.add(winitem.getInt(0));
                        mstmissionlist.add(winitem.getInt(1));
                        mstmission.setWinItem2(mstmissionlist);
                        MstMissionManager.INSTANCE.put(mstmission);
                    }
                    //MstMissionManagerをシリアライズ
                    MstMissionManager.INSTANCE.serialize();

                    break;

                case "api_get_member/require_info":
                    JSONObject root = new JSONObject(jsonStr);
                    JSONObject dataObj = root.getJSONObject("api_data");
                    JSONArray slotItemArray = dataObj.getJSONArray("api_slot_item");
                    List <Integer> list = new ArrayList<>();
                    for (int i = 0; i < slotItemArray.length(); i++) {
                        JSONObject jsonObj = slotItemArray.getJSONObject(i);
                        MySlotItem msi = new MySlotItem();
                        msi.setId(jsonObj.getInt("api_id"));
                        msi.setMstId(jsonObj.getInt("api_slotitem_id"));
                        msi.setLocked(jsonObj.getInt("api_locked"));
                        msi.setLevel(jsonObj.getInt("api_level"));
                        try {
                            msi.setAlv(jsonObj.getInt("api_alv"));
                        } catch (Exception e) {
                        }
                        MySlotItemManager.INSTANCE.put(msi);
                        list.add(msi.getId());
                    }
                    MySlotItemManager.INSTANCE.delete(list);
                    MySlotItemManager.INSTANCE.serialize();
                    break;


                //母港画面に移動時
                case "api_port/port":
                    //root
                    JSONObject portRoot = new JSONObject(jsonStr);
                    //root/data
                    JSONObject portData = portRoot.getJSONObject("api_data");

                    //資材
                    JSONArray materialDate = portData.getJSONArray("api_material");
                    List<Integer> materialList = new ArrayList<>();
                    for (int i = 0; i < materialDate.length(); i++) {
                        materialList.add(materialDate.getJSONObject(i).getInt("api_value"));
                    }
                    int a = materialList.get(4);
                    materialList.set(4, materialList.get(5));
                    materialList.set(5, a);
                    Material material = new Material();
                    material.setMaterialList(materialList);
                    MaterialLogger.INSTANCE.writeLog(material);

                    //root/data/ship
                    JSONArray shipArray = portData.getJSONArray("api_ship");
                    JSONObject[] shipObj = new JSONObject[shipArray.length()];
                    list = new ArrayList<>();
                    for (int i = 0; i < shipArray.length(); i++) {
                        shipObj[i] = shipArray.getJSONObject(i);
                        MyShip myShip = new MyShip();
                        myShip.setId(shipObj[i].getInt("api_id"));
                        myShip.setSortno(shipObj[i].getInt("api_sortno"));
                        myShip.setShipId(shipObj[i].getInt("api_ship_id"));
                        myShip.setLv(shipObj[i].getInt("api_lv"));
                        List<Integer> tList = new ArrayList<>();
                        JSONArray array = shipObj[i].getJSONArray("api_exp");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setExp(tList);
                        myShip.setNowhp(shipObj[i].getInt("api_nowhp"));
                        myShip.setMaxhp(shipObj[i].getInt("api_maxhp"));
                        myShip.setLeng(shipObj[i].getInt("api_leng"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_slot");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setSlot(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_onslot");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setOnslot(tList);
                        myShip.setSlotEx(shipObj[i].getInt("api_slot_ex"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_kyouka");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setKyouka(tList);
                        myShip.setBacks(shipObj[i].getInt("api_backs"));
                        myShip.setFuel(shipObj[i].getInt("api_fuel"));
                        myShip.setBull(shipObj[i].getInt("api_bull"));
                        myShip.setSlotnum(shipObj[i].getInt("api_slotnum"));
                        myShip.setNdockTime(shipObj[i].getLong("api_ndock_time"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_ndock_item");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setNdockItem(tList);
                        myShip.setSrate(shipObj[i].getInt("api_srate"));
                        myShip.setCond(shipObj[i].getInt("api_cond"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_karyoku");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setKaryoku(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_raisou");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setRaisou(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_taiku");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setTaiku(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_soukou");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setSoukou(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_kaihi");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setKaihi(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_taisen");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setTaisen(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_sakuteki");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setSakuteki(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_lucky");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setLucky(tList);
                        myShip.setLocked(shipObj[i].getInt("api_locked"));
                        myShip.setLockedEquip(shipObj[i].getInt("api_locked_equip"));
                        try {
                            myShip.setSallyArea(shipObj[i].getInt("api_sally_area"));
                        } catch (Exception e) {
                        }
                        list.add(myShip.getId());
                        ShipMap.INSTANCE.put(myShip.getId(), MstShipManager.INSTANCE.getMstShip(myShip.getShipId()));
                        MyShipManager.INSTANCE.put(myShip.getId(), myShip);
                    }
                    MyShipManager.INSTANCE.delete(list);
                    mstShip = new MstShip();
                    mstShip.setId(-1);
                    mstShip.setName("");
                    mstShip.setYomi("");
                    ShipMap.INSTANCE.put(-1, mstShip);

                    //root/data/deck_port
                    JSONArray deckPortArray = portData.getJSONArray("api_deck_port");
                    JSONObject[] deckPortObj = new JSONObject[deckPortArray.length()];
                    Deck deck;
                    for (int i = 0; i < deckPortArray.length(); i++) {
                        deck = new Deck();
                        deckPortObj[i] = deckPortArray.getJSONObject(i);
                        deck.setId(deckPortObj[i].getInt("api_id"));
                        deck.setName(deckPortObj[i].getString("api_name"));

                        JSONArray deckMissionArray = deckPortObj[i].getJSONArray("api_mission");
                        List<Long> tmilistl = new ArrayList<>();
                        for (int j = 0; j < deckMissionArray.length(); j++) {
                            tmilistl.add(deckMissionArray.getLong(j));
                        }
                        deck.setMission(tmilistl);

                        JSONArray deckShipArray = deckPortObj[i].getJSONArray("api_ship");
                        List<Integer> tmilist = new ArrayList<>();
                        for (int j = 0; j < deckShipArray.length(); j++) {
                            tmilist.add(deckShipArray.getInt(j));
                        }
                        deck.setShipId(tmilist);

                        deck.setLevelSum(DeckUtility.getLevelSum(deck));
                        deck.setCondRecoveryTime(DeckUtility.getCondRecoveryTime(deck));

                        DeckManager.INSTANCE.put(deck);

                        //遠征タイマーを制御
                        tmilistl = DeckManager.INSTANCE.getDeck(deck.getId()).getMission();
                        //艦隊が遠征中か強制帰投中
                        if (tmilistl.get(0) == 1 || tmilistl.get(0) == 3) {
                            //指定された艦隊に対するタイマーが作動中でない
                            if (!(MissionTimer.INSTANCE.isRunning(deck.getId()))) {
                                //遠征タイマーをセット
                                MissionTimer.INSTANCE.ready(deck.getId(), Integer.valueOf(tmilistl.get(1).toString()));
                                MissionTimer.INSTANCE.startTimer(context, tmilistl.get(2));
                            }
                            //未出撃
                        } else if (tmilistl.get(0) == 0) {
                            //指定された艦隊に対するタイマーが作動中
                            if (MissionTimer.INSTANCE.isRunning(deck.getId())) {
                                //遠征タイマーを中断
                                NotificationUtility.cancelNotification(context, deck.getId());
                            }
                        }
                    }

                    deckPortArray = portData.getJSONArray("api_ndock");
                    deckPortObj = new JSONObject[deckPortArray.length()];
                    for (int i = 0; i < deckPortArray.length(); i++) {
                        deckPortObj[i] = deckPortArray.getJSONObject(i);
                        if (deckPortObj[i].getInt("api_ship_id") != 0 && !DockTimer.INSTANCE.isRunning(deckPortObj[i].getInt("api_id"))) {
                            DockTimer.INSTANCE.startTimer(context, deckPortObj[i].getInt("api_id"), deckPortObj[i].getInt("api_ship_id"), deckPortObj[i].getLong("api_complete_time"));
                        }
                    }

                    JSONObject object = portData.getJSONObject("api_basic");
                    Basic.INSTANCE.setLevel(object.getInt("api_level"));

                    Escape.INSTANCE.close();


                    break;

                //プリセットによる編成変更
                case "api_req_hensei/preset_select":
                    root = new JSONObject(jsonStr);
                    JSONObject dataB = root.getJSONObject("api_data");
                    deck = DeckManager.INSTANCE.getDeck(dataB.getInt("api_id"));
                    List<Integer> tlist = new ArrayList<>();
                    JSONArray shipIdli = dataB.getJSONArray("api_ship");
                    for (int i = 0; i < shipIdli.length(); i++) {
                        tlist.add(shipIdli.getInt(i));
                    }
                    deck.setShipId(tlist);
                    deck.setLevelSum(DeckUtility.getLevelSum(deck));
                    deck.setCondRecoveryTime(DeckUtility.getCondRecoveryTime(deck));
                    break;

                case "api_get_member/kdock":
                    CreateShipLogger logger = CreateShipLogger.INSTANCE;
                    if (!logger.isReady()) {
                        return;
                    }

                    root = new JSONObject(jsonStr);
                    JSONArray data = root.getJSONArray("api_data");

                    //準備完了状態のドック位置の情報を取得
                    JSONObject obj = data.getJSONObject(logger.getKdockId() - 1);
                    Kdock kdock = new Kdock();
                    kdock.setMstShipId(obj.getInt("api_created_ship_id"));
                    kdock.setFuel(obj.getInt("api_item1"));
                    kdock.setBullet(obj.getInt("api_item2"));
                    kdock.setSteel(obj.getInt("api_item3"));
                    kdock.setBauxite(obj.getInt("api_item4"));
                    kdock.setDevelopmentMaterial(obj.getInt("api_item5"));

                    //空きドック数
                    int emptyDockNum = 0;
                    for (int i = 0; i < data.length(); i++) {
                        if (data.getJSONObject(i).getInt("api_state") == 0) {
                            emptyDockNum++;
                        }
                    }

                    logger.write(kdock, emptyDockNum);
                    break;

                case "api_req_kousyou/createitem":
                    root = new JSONObject(jsonStr);
                    obj = root.getJSONObject("api_data");
                    int createFlag = obj.getInt("api_create_flag");

                    if (createFlag == 0) {
                        CreateItemLogger.INSTANCE.write(createFlag, null, null);
                        break;
                    }

                    int equipTypeId = obj.getInt("api_type3");
                    EquipType2 equipType2 = EquipType2.toEquipType2(equipTypeId);

                    obj = obj.getJSONObject("api_slot_item");
                    int mstSlotItemId = obj.getInt("api_slotitem_id");
                    MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mstSlotItemId);

                    CreateItemLogger.INSTANCE.write(createFlag, mstSlotitem, equipType2);
                    break;

                case "api_get_member/slot_item":
                    root = new JSONObject(jsonStr);
                    data = root.getJSONArray("api_data");
                    list = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObj = data.getJSONObject(i);
                        MySlotItem msi = new MySlotItem();
                        msi.setId(jsonObj.getInt("api_id"));
                        msi.setMstId(jsonObj.getInt("api_slotitem_id"));
                        msi.setLocked(jsonObj.getInt("api_locked"));
                        msi.setLevel(jsonObj.getInt("api_level"));
                        try {
                            msi.setAlv(jsonObj.getInt("api_alv"));
                        } catch (Exception e) {
                        }
                        MySlotItemManager.INSTANCE.put(msi);
                        list.add(msi.getId());
                    }
                    MySlotItemManager.INSTANCE.delete(list);
                    MySlotItemManager.INSTANCE.serialize();
                    break;

                case "api_get_member/ship3":
                    //root
                    portRoot = new JSONObject(jsonStr);
                    //root/data
                    portData = portRoot.getJSONObject("api_data");

                    //root/data/ship
                    shipArray = portData.getJSONArray("api_ship_data");
                    shipObj = new JSONObject[shipArray.length()];
                    list = new ArrayList<>();
                    for (int i = 0; i < shipArray.length(); i++) {
                        shipObj[i] = shipArray.getJSONObject(i);
                        MyShip myShip = new MyShip();
                        myShip.setId(shipObj[i].getInt("api_id"));
                        myShip.setSortno(shipObj[i].getInt("api_sortno"));
                        myShip.setShipId(shipObj[i].getInt("api_ship_id"));
                        myShip.setLv(shipObj[i].getInt("api_lv"));
                        List<Integer> tList = new ArrayList<>();
                        JSONArray array = shipObj[i].getJSONArray("api_exp");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setExp(tList);
                        myShip.setNowhp(shipObj[i].getInt("api_nowhp"));
                        myShip.setMaxhp(shipObj[i].getInt("api_maxhp"));
                        myShip.setLeng(shipObj[i].getInt("api_leng"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_slot");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setSlot(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_onslot");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setOnslot(tList);
                        myShip.setSlotEx(shipObj[i].getInt("api_slot_ex"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_kyouka");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setKyouka(tList);
                        myShip.setBacks(shipObj[i].getInt("api_backs"));
                        myShip.setFuel(shipObj[i].getInt("api_fuel"));
                        myShip.setBull(shipObj[i].getInt("api_bull"));
                        myShip.setSlotnum(shipObj[i].getInt("api_slotnum"));
                        myShip.setNdockTime(shipObj[i].getLong("api_ndock_time"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_ndock_item");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setNdockItem(tList);
                        myShip.setSrate(shipObj[i].getInt("api_srate"));
                        myShip.setCond(shipObj[i].getInt("api_cond"));
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_karyoku");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setKaryoku(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_raisou");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setRaisou(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_taiku");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setTaiku(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_soukou");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setSoukou(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_kaihi");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setKaihi(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_taisen");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setTaisen(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_sakuteki");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setSakuteki(tList);
                        tList = new ArrayList<>();
                        array = shipObj[i].getJSONArray("api_lucky");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setLucky(tList);
                        myShip.setLocked(shipObj[i].getInt("api_locked"));
                        myShip.setLockedEquip(shipObj[i].getInt("api_locked_equip"));
                        try {
                            myShip.setSallyArea(shipObj[i].getInt("api_sally_area"));
                        } catch (Exception e) {
                        }
                        list.add(myShip.getId());
                        ShipMap.INSTANCE.put(myShip.getId(), MstShipManager.INSTANCE.getMstShip(myShip.getShipId()));
                        MyShipManager.INSTANCE.put(myShip.getId(), myShip);
                    }

                    break;

                //出撃時
                case "api_req_map/start":
                    JSONObject startRoot = new JSONObject(jsonStr);
                    JSONObject startData = startRoot.getJSONObject("api_data");
                    BattleLogger.INSTANCE.start(startData.getInt("api_no"), startData.getInt("api_event_id"));
                    break;


                //次のマスへの移動時
                case "api_req_map/next":
                    JSONObject nextRoot = new JSONObject(jsonStr);
                    JSONObject nextData = nextRoot.getJSONObject("api_data");
                    BattleLogger.INSTANCE.next(nextData.getInt("api_no"), nextData.getInt("api_event_id"));
                    break;


                //戦闘開始時
                case "api_req_sortie/battle":
                    Battle battle = new Battle();
                    battle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(battle);
                    TacticalSituation.INSTANCE.set(battle);
                    break;


                //航空戦
                case "api_req_sortie/airbattle":
                    Airbattle airbattle = new Airbattle();
                    airbattle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(airbattle);
                    TacticalSituation.INSTANCE.set(airbattle);
                    break;

                //航空戦
                case "api_req_sortie/ld_airbattle":
                    LdAirbattle ldAirbattle = new LdAirbattle();
                    ldAirbattle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(ldAirbattle);
                    TacticalSituation.INSTANCE.set(ldAirbattle);
                    break;


                //夜戦スタート
                case "api_req_battle_midnight/sp_midnight":
                    SpMidnightBattle spMidnightBattle = new SpMidnightBattle();
                    spMidnightBattle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(spMidnightBattle);
                    TacticalSituation.INSTANCE.set(spMidnightBattle);
                    break;


                //戦闘終了時
                case "api_req_sortie/battleresult":
                    SortieBattleresult contentB = SortieBattleresult.INSTANCE;
                    JSONObject battleresul = new JSONObject(jsonStr);
                    JSONObject battleresultdata = battleresul.getJSONObject("api_data");
                    contentB.setRank(battleresultdata.getString("api_win_rank"));
                    contentB.setQuestName(battleresultdata.getString("api_quest_name"));
                    JSONObject enemyInfo = battleresultdata.getJSONObject("api_enemy_info");
                    contentB.seteFleetName(enemyInfo.getString("api_deck_name"));
                    try {
                        JSONObject getShip = battleresultdata.getJSONObject("api_get_ship");
                        contentB.setGetShipType(getShip.getString("api_ship_type"));
                        contentB.setGetShipName(getShip.getString("api_ship_name"));
                    } catch (Exception e) {
                        contentB.setGetShipName("");
                        contentB.setGetShipType("");
                    }
                    BattleLogger.INSTANCE.writeLog();
                    break;


                //連合艦隊航空戦
                case "api_req_combined_battle/airbattle":
                    CombinedAirbattle combinedAirbattle = new CombinedAirbattle();
                    combinedAirbattle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedAirbattle);
                    TacticalSituation.INSTANCE.set(combinedAirbattle);
                    break;


                //連合艦隊 長距離空襲
                case "api_req_combined_battle/ld_airbattle":
                    CombinedLdAirbattle combinedLdAirbattle = new CombinedLdAirbattle();
                    combinedLdAirbattle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedLdAirbattle);
                    TacticalSituation.INSTANCE.set(combinedLdAirbattle);
                    break;


                //連合艦隊 機動部隊 戦闘
                case "api_req_combined_battle/battle":
                    CombinedBattle combinedBattle = new CombinedBattle();
                    combinedBattle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedBattle);
                    TacticalSituation.INSTANCE.set(combinedBattle);
                    break;


                //連合艦隊 水上部隊 戦闘
                case "api_req_combined_battle/battle_water":
                    CombinedWater combinedWater = new CombinedWater();
                    combinedWater.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedWater);
                    TacticalSituation.INSTANCE.set(combinedWater);
                    break;

                //連合艦隊 夜戦スタート
                case "api_req_combined_battle/sp_midnight":
                    CombinedSpMidnight combinedSpMidnight = new CombinedSpMidnight();
                    combinedSpMidnight.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedSpMidnight);
                    TacticalSituation.INSTANCE.set(combinedSpMidnight);
                    break;

                //敵連合艦隊
                case "api_req_combined_battle/ec_battle":
                    CombinedEC combinedEC = new CombinedEC();
                    combinedEC.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedEC);
                    TacticalSituation.INSTANCE.set(combinedEC);
                    break;

                //連合VS連合(機動部隊)
                case "api_req_combined_battle/each_battle":
                    CombinedEach combinedEach = new CombinedEach();
                    combinedEach.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedEach);
                    TacticalSituation.INSTANCE.set(combinedEach);
                    break;

                //連合VS連合(水上部隊)
                case "api_req_combined_battle/each_battle_water":
                    CombinedEachWater combinedEachWater = new CombinedEachWater();
                    combinedEachWater.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(combinedEachWater);
                    TacticalSituation.INSTANCE.set(combinedEachWater);
                    break;

                //連合艦隊戦闘終了時
                case "api_req_combined_battle/battleresult":
                    contentB = SortieBattleresult.INSTANCE;
                    battleresul = new JSONObject(jsonStr);
                    battleresultdata = battleresul.getJSONObject("api_data");
                    contentB.setRank(battleresultdata.getString("api_win_rank"));
                    contentB.setQuestName(battleresultdata.getString("api_quest_name"));
                    enemyInfo = battleresultdata.getJSONObject("api_enemy_info");
                    contentB.seteFleetName(enemyInfo.getString("api_deck_name"));
                    try {
                        JSONObject getShip = battleresultdata.getJSONObject("api_get_ship");
                        contentB.setGetShipType(getShip.getString("api_ship_type"));
                        contentB.setGetShipName(getShip.getString("api_ship_name"));
                    } catch (Exception e) {
                        contentB.setGetShipName("");
                        contentB.setGetShipType("");
                    }
                    try {
                        JSONObject getShip = battleresultdata.getJSONObject("api_escape");
                        int damaged = getShip.getJSONArray("api_escape_idx").getInt(0);
                        int towing = getShip.getJSONArray("api_tow_idx").getInt(0);
                        if (damaged > 6) {
                            damaged = DeckManager.INSTANCE.getDeck(2).getShipId().get(damaged - 7);
                        } else {
                            damaged = DeckManager.INSTANCE.getDeck(1).getShipId().get(damaged - 1);
                        }
                        if (towing > 6) {
                            towing = DeckManager.INSTANCE.getDeck(2).getShipId().get(towing - 7);
                        } else {
                            towing = DeckManager.INSTANCE.getDeck(1).getShipId().get(towing - 1);
                        }
                        Escape.INSTANCE.ready(damaged, towing);
                    } catch (Exception e) {
                    }
                    BattleLogger.INSTANCE.writeLog();
                    break;

                case "api_req_combined_battle/goback_port":
                    Escape.INSTANCE.escape();
                    break;

                //api_req_map/nextの直前
                case "api_get_member/ship_deck":
                    portRoot = new JSONObject(jsonStr);
                    portData = portRoot.getJSONObject("api_data");
                    shipArray = portData.getJSONArray("api_ship_data");
                    shipObj = new JSONObject[shipArray.length()];
                    ArrayList<Integer> heavyDamaged = new ArrayList<>();
                    for (int i = 0; i < shipArray.length(); i++) {
                        shipObj[i] = shipArray.getJSONObject(i);
                        MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipObj[i].getInt("api_id"));
                        myShip.setLv(shipObj[i].getInt("api_lv"));
                        myShip.setNowhp(shipObj[i].getInt("api_nowhp"));
                        myShip.setMaxhp(shipObj[i].getInt("api_maxhp"));
                        List<Integer> tList = new ArrayList<>();
                        JSONArray array = shipObj[i].getJSONArray("api_onslot");
                        for (int j = 0; j < array.length(); j++) {
                            tList.add(array.getInt(j));
                        }
                        myShip.setOnslot(tList);
                        myShip.setCond(shipObj[i].getInt("api_cond"));
                        myShip.setFuel(shipObj[i].getInt("api_fuel"));
                        myShip.setBull(shipObj[i].getInt("api_bull"));
                        if (myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                            /*退避している場合は警告を出さない
                            if (Escape.INSTANCE.isEscaped(shipObj[i].getInt("api_id"))) {
                                continue;
                            }
                            */
                            heavyDamaged.add(shipObj[i].getInt("api_id"));
                        }
                    }
                    if (!heavyDamaged.isEmpty()) {
                        //大破進撃警告画面を表示
                        Intent intent = new Intent(context, HeavilyDamagedWarningActivity.class);
                        intent.putIntegerArrayListExtra("shipId", heavyDamaged);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(intent);
                    }
                    break;

                //演習
                case "api_req_practice/battle":
                    PracticeBattle practiceBattle = new PracticeBattle();
                    practiceBattle.set(jsonStr);
                    BattleLogger.INSTANCE.setBattle(practiceBattle);
                    TacticalSituation.INSTANCE.set(practiceBattle);

                    break;

                //遠征開始時
                case "api_req_mission/start":
                    JSONObject msRoot = new JSONObject(jsonStr);
                    JSONObject msData = msRoot.getJSONObject("api_data");
                    MissionTimer.INSTANCE.startTimer(context, msData.getLong("api_complatetime"));
                    break;


                //遠征帰投時
                case "api_req_mission/result":
                    MissionResult mr = new MissionResult();
                    JSONObject miRoot = new JSONObject(jsonStr);
                    JSONObject miData = miRoot.getJSONObject("api_data");
                    JSONArray mishipid = miData.getJSONArray("api_ship_id");
                    List<Integer> mishipidList = new ArrayList<>();
                    for (int i = 0; i < mishipid.length(); i++) {
                        mishipidList.add(mishipid.getInt(i));
                    }
                    mr.setShipId(mishipidList);
                    mr.setClearResult(miData.getInt("api_clear_result"));
                    mr.setMapareaName(miData.getString("api_maparea_name"));
                    mr.setQuestName(miData.getString("api_quest_name"));
                    try {
                        mishipid = miData.getJSONArray("api_get_material");
                        mishipidList = new ArrayList<>();
                        for (int i = 0; i < mishipid.length(); i++) {
                            mishipidList.add(mishipid.getInt(i));
                        }
                        mr.setGainMaterial(mishipidList);
                    } catch (Exception e) {
                        mishipidList = new ArrayList<>();
                        mishipidList.add(0);
                        mishipidList.add(0);
                        mishipidList.add(0);
                        mishipidList.add(0);
                        mr.setGainMaterial(mishipidList);
                    }
                    try {
                        JSONObject useitem = miData.getJSONObject("api_get_item1");
                        mr.setUseitemId1(MstMissionManager.INSTANCE.getMstMission(mr.getQuestName()).getWinItem1().get(0));
                        mr.setUseitemName1(MstUseitemManager.INSTANCE.getMstUseitem(mr.getUseitemId1()).getName());
                        mr.setUseitemCount1(useitem.getInt("api_useitem_count"));
                    } catch (Exception e) {
                    }
                    try {
                        JSONObject useitem = miData.getJSONObject("api_get_item2");
                        mr.setUseitemId2(MstMissionManager.INSTANCE.getMstMission(mr.getQuestName()).getWinItem2().get(0));
                        mr.setUseitemName2(MstUseitemManager.INSTANCE.getMstUseitem(mr.getUseitemId2()).getName());
                        mr.setUseitemCount2(useitem.getInt("api_useitem_count"));
                    } catch (Exception e) {
                    }
                    JSONArray exp = miData.getJSONArray("api_get_ship_exp");
                    int expSum = 0;
                    for (int i = 0; i < exp.length(); i++) {
                        expSum += exp.getInt(i);
                    }
                    mr.setExpSum(expSum);
                    MissionLogger.INSTANCE.writeLog(mr);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.writeLog(e);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (sp.getBoolean("saveJson", false)) {
            //SDカードのディレクトリパス
            File sdcard_path = new File(Environment.getExternalStorageDirectory().getPath() + "/泥提督支援アプリ/json/");

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
                pw.write(jsonStr);
                pw.flush();
                pw.close();
            } catch (Exception e) {
            }
        }

        //Log.d("JSON", jsonStr);
    }

}