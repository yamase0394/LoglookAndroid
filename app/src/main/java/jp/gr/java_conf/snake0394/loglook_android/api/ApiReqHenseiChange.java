package jp.gr.java_conf.snake0394.loglook_android.api;

import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String requestBody;
        try {
            requestBody = IOUtils.toString(req.getRequestBody()
                                              .get(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();

            return;
        }

        String regex = "api%5Fid=(\\d+)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(requestBody);
        m.find();
        int deckId = Integer.parseInt(m.group(1));
        Deck deck = DeckManager.INSTANCE.getDeck(deckId);
        List<Integer> shipIdList = deck.getShipId();

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

            return;
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

                return;
            }
            for (int i = changeIndex; i < shipIdList.size() - 1; i++) {
                shipIdList.set(i, shipIdList.get(i + 1));
            }
            shipIdList.set(shipIdList.size() - 1, -1);
        }

        deck.setLevelSum(DeckUtility.getLevelSum(deck));
        deck.setCondRecoveryTime(DeckUtility.getCondRecoveryTime(deck));
    }
}
