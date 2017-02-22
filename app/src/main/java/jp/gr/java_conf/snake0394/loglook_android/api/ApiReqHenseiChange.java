package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.DeckUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.proxy.RequestMetaData;
import jp.gr.java_conf.snake0394.loglook_android.proxy.ResponseMetaData;

/**
 * Created by snake0394 on 2017/02/17.
 */
@API("/kcsapi/api_req_hensei/change")
public class ApiReqHenseiChange implements APIListenerSpi {
    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        int deckId = Integer.parseInt(req.getParameterMap()
                                         .get("api_id")
                                         .get(0));
        Deck targetDeck = DeckManager.INSTANCE.getDeck(deckId);
        List<Integer> shipIdList = targetDeck.getShipId();

        //変更場所。-1=旗艦以外全解除
        int changeIndex = Integer.parseInt(req.getParameterMap()
                                              .get("api_ship_idx")
                                              .get(0));

        //配備艦の所有艦娘ID。-1=はずす, -2=旗艦以外全解除
        int deployedShipId = Integer.parseInt(req.getParameterMap()
                                                 .get("api_ship_id")
                                                 .get(0));

        if (deployedShipId == -1) {
            //はずす
            shipIdList.remove(changeIndex);
            shipIdList.add(-1);

        } else if (deployedShipId == -2) {
            //旗艦以外全解除
            shipIdList.set(1, -1);
            shipIdList.set(2, -1);
            shipIdList.set(3, -1);
            shipIdList.set(4, -1);
            shipIdList.set(5, -1);

        } else if (shipIdList.contains(deployedShipId)) {
            //配備艦娘が対象艦隊に所属している
            //変更位置にいる艦娘の所有艦娘ID
            int oldShipId = shipIdList.get(changeIndex);
            //配備艦の以前の位置
            int oldIndex = shipIdList.indexOf(deployedShipId);
            if (oldShipId == -1) {
                shipIdList.set(shipIdList.indexOf(-1), deployedShipId);
                shipIdList.remove(oldIndex);
                shipIdList.add(-1);
            } else {
                shipIdList.set(oldIndex, oldShipId);
                shipIdList.set(changeIndex, deployedShipId);
            }
        } else {
            //配備艦が艦隊に含まれているか
            boolean contains = false;

            for (Deck tDeck : DeckManager.INSTANCE.getDeckList()) {
                if (tDeck.getId() == deckId) {
                    continue;
                }

                if (tDeck.getShipId()
                         .contains(deployedShipId)) {
                    int tempId = shipIdList.get(changeIndex);
                    if (tempId == -1) {
                        shipIdList.set(changeIndex, deployedShipId);

                        tempId = tDeck.getShipId()
                                      .indexOf(deployedShipId);
                        tDeck.getShipId()
                             .remove(tempId);
                        tDeck.getShipId()
                             .add(-1);
                    } else {
                        int idx = tDeck.getShipId()
                                       .indexOf(deployedShipId);
                        tDeck.getShipId()
                             .set(idx, tempId);
                        shipIdList.set(changeIndex, deployedShipId);
                    }
                    DeckManager.INSTANCE.put(tDeck);
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                shipIdList.set(changeIndex, deployedShipId);
            }
        }

        targetDeck.setLevelSum(DeckUtility.getLevelSum(targetDeck));
        targetDeck.setCondRecoveryTime(DeckUtility.getCondRecoveryTime(targetDeck));
    }
}
