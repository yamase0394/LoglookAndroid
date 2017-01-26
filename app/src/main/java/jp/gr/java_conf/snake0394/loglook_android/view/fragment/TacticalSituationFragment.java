package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.gr.java_conf.snake0394.loglook_android.BattleType;
import jp.gr.java_conf.snake0394.loglook_android.DeckUtility;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.TacticalSituation;

public class TacticalSituationFragment extends Fragment {

    public TacticalSituationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TacticalSituationFragment newInstance() {
        TacticalSituationFragment fragment = new TacticalSituationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tactical_situation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        FragmentManager manager = getChildFragmentManager();
        ErrorFragment fragment = (ErrorFragment) manager.findFragmentById(R.id.fragment);
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();

        try {
            TacticalSituation ts = TacticalSituation.INSTANCE;

            TextView text = (TextView) getActivity().findViewById(R.id.tactic);
            text.setText(ts.getBattle().getTactic());

            text = (TextView) getActivity().findViewById(R.id.formation);
            text.setText(ts.getBattle().getFormation());

            text = (TextView) getActivity().findViewById(R.id.eFormation);
            text.setText(ts.getBattle().geteFormation());

            text = (TextView) getActivity().findViewById(R.id.seiku);
            text.setText(DeckUtility.getSeiku(DeckManager.INSTANCE.getDeck(ts.getBattle().getDeckId())) + "(" + ts.getBattle().getSeiku() + ")");

            text = (TextView) getActivity().findViewById(R.id.touchPlane);
            if (ts.getBattle().getTouchPlane().equals("")) {
                text.setText("なし");
            } else {
                text.setText(ts.getBattle().getTouchPlane());
            }

            text = (TextView) getActivity().findViewById(R.id.enemyTouchPlane);
            if (ts.getBattle().getEtTouchPlane().equals("")) {
                text.setText("なし");
            } else {
                text.setText(ts.getBattle().getEtTouchPlane());
            }

            int friendHpSum = 0;
            int enemyHpSum = 0;

            int friendDamageSum = 0;
            int enemyDamageSum = 0;

            int friendSink = 0;
            int enemySink = 0;

            //本隊
            for (int i = 1; i <= 6; i++) {
                if (i > ts.getFriendShipId().size()) {
                    String name = "name" + i;
                    int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "lv" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "state" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "hp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "beforeHp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "damage" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    continue;
                }

                int id = ts.getFriendShipId().get(i - 1);
                MyShip myShip = MyShipManager.INSTANCE.getMyShip(id);

                String name = "name" + i;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(myShip.getName());

                name = "lv" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText("(Lv" + myShip.getLv() + ")");

                name = "state" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                    text.setText("退避");
                    text.setTextColor(Color.LTGRAY);
                } else if (ts.getFriendNowhps().get(i - 1) <= 0) {
                    text.setText("轟沈");
                    //オーシャンブルー
                    friendSink++;
                    text.setTextColor(Color.rgb(0, 102, 204));
                } else if (ts.getFriendNowhps().get(i - 1) <= ts.getFriendMaxhps().get(i - 1) / 4) {
                    text.setText("大破");
                    text.setTextColor(Color.RED);
                } else if (ts.getFriendNowhps().get(i - 1) <= ts.getFriendMaxhps().get(i - 1) / 2) {
                    text.setText("中破");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (ts.getFriendNowhps().get(i - 1) <= ts.getFriendMaxhps().get(i - 1) * 3 / 4) {
                    text.setText("小破");
                    //黄色
                    text.setTextColor(Color.rgb(255, 230, 30));
                } else if (ts.getFriendNowhps().get(i - 1) < ts.getFriendMaxhps().get(i - 1)) {
                    text.setText("健在");
                    //ライム
                    text.setTextColor((Color.rgb(153, 204, 0)));
                } else {
                    text.setText("無傷");
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                }

                name = "beforeHp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(ts.getFriendHpBeforeBattle().get(i - 1) + "/" + myShip.getMaxhp() + "→");

                friendHpSum += ts.getFriendHpBeforeBattle().get(i - 1);

                name = "hp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(ts.getFriendNowhps().get(i - 1) + "/" + ts.getFriendMaxhps().get(i - 1));

                name = "damage" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                int damage = ts.getFriendNowhps().get(i - 1) - ts.getFriendHpBeforeBattle().get(i - 1);
                text.setText(String.valueOf(damage));
                if (damage == 0) {
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                } else {
                    //オーシャンブルー
                    text.setTextColor(Color.rgb(0, 102, 204));
                    if (damage * -1 > ts.getFriendHpBeforeBattle().get(i - 1)) {
                        friendDamageSum += ts.getFriendHpBeforeBattle().get(i - 1);
                    } else {
                        friendDamageSum += damage * -1;
                    }
                }
            }

            //敵艦隊
            for (int i = 1; i <= 6; i++) {
                if (i > ts.getEnemyShipId().size()) {
                    String name = "eName" + i;
                    int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "eLv" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "eState" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "eBeforeHp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "eHp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    name = "eDamage" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setText("");

                    continue;
                }

                int id = ts.getEnemyShipId().get(i - 1);
                MstShip eShip = MstShipManager.INSTANCE.getMstShip(id);

                String name = "eName" + i;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(eShip.getName());

                name = "eLv" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                if (ts.getBattle().getBattleType() == BattleType.PRACTICE) {
                    text.setText("(Lv" + ts.getBattle().geteShipLv().get(i) + ")");
                } else {
                    text.setText(eShip.getYomi() + "(Lv" + ts.getBattle().geteShipLv().get(i) + ")");
                }

                name = "eState" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);

                if (ts.getEnemyNowhps().get(i - 1) <= 0) {
                    text.setText("撃沈");
                    //オーシャンブルー
                    text.setTextColor(Color.rgb(0, 102, 204));
                    enemySink++;
                } else if (ts.getEnemyNowhps().get(i - 1) <= ts.getEnemyMaxhps().get(i - 1) / 4) {
                    text.setText("大破");
                    text.setTextColor(Color.RED);
                } else if (ts.getEnemyNowhps().get(i - 1) <= ts.getEnemyMaxhps().get(i - 1) / 2) {
                    text.setText("中破");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (ts.getEnemyNowhps().get(i - 1) <= ts.getEnemyMaxhps().get(i - 1) * 3 / 4) {
                    text.setText("小破");
                    //黄色
                    text.setTextColor(Color.rgb(255, 230, 30));
                } else if (ts.getEnemyNowhps().get(i - 1) < ts.getEnemyMaxhps().get(i - 1)) {
                    text.setText("健在");
                    //ライム
                    text.setTextColor((Color.rgb(153, 204, 0)));
                } else {
                    text.setText("無傷");
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                }

                name = "eBeforeHp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(ts.getBattle().getNowhps().get(i + 6) + "/" + ts.getEnemyMaxhps().get(i - 1) + "→");

                enemyHpSum += ts.getBattle().getNowhps().get(i + 6);

                name = "eHp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(ts.getEnemyNowhps().get(i - 1) + "/" + ts.getEnemyMaxhps().get(i - 1));

                name = "eDamage" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                int damage = ts.getEnemyNowhps().get(i - 1) - ts.getBattle().getNowhps().get(i + 6);
                text.setText(String.valueOf(damage));
                if (damage == 0) {
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                } else {
                    //オーシャンブルー
                    text.setTextColor(Color.rgb(0, 102, 204));

                    if (damage * -1 > ts.getEnemyHpBeforeBattle().get(i - 1)) {
                        enemyDamageSum += ts.getEnemyHpBeforeBattle().get(i - 1);
                    } else {
                        enemyDamageSum += damage * -1;
                    }
                }
            }

            //第2艦隊
            for (int i = 1; i <= 6; i++) {
                if (i > ts.getFriendShipIdCombined().size()) {
                    String name = "cName" + i;
                    int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "cLv" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "cState" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "cBeforeHp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "cHp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "cDamage" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    continue;
                }

                int id = ts.getFriendShipIdCombined().get(i - 1);
                MyShip myShip = MyShipManager.INSTANCE.getMyShip(id);

                String name = "cName" + i;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText(myShip.getName());

                name = "cLv" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText("(Lv" + myShip.getLv() + ")");

                name = "cState" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);
                if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                    text.setText("退避");
                    text.setTextColor(Color.LTGRAY);
                } else if (ts.getFriendNowhpsCombined().get(i - 1) <= 0) {
                    text.setText("轟沈");
                    //オーシャンブルー
                    text.setTextColor(Color.rgb(0, 102, 204));
                    friendSink++;
                } else if (ts.getFriendNowhpsCombined().get(i - 1) <= ts.getFriendMaxhpsCombined().get(i - 1) / 4) {
                    text.setText("大破");
                    text.setTextColor(Color.RED);
                } else if (ts.getFriendNowhpsCombined().get(i - 1) <= ts.getFriendMaxhpsCombined().get(i - 1) / 2) {
                    text.setText("中破");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (ts.getFriendNowhpsCombined().get(i - 1) <= ts.getFriendMaxhpsCombined().get(i - 1) * 3 / 4) {
                    text.setText("小破");
                    //黄色
                    text.setTextColor(Color.rgb(255, 230, 30));
                } else if (ts.getFriendNowhpsCombined().get(i - 1) < ts.getFriendMaxhpsCombined().get(i - 1)) {
                    text.setText("健在");
                    //ライム
                    text.setTextColor((Color.rgb(153, 204, 0)));
                } else {
                    text.setText("無傷");
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                }

                name = "cBeforeHp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText(ts.getFriendHpBeforeBattleCombined().get(i - 1) + "/" + myShip.getMaxhp() + "→");

                friendHpSum += ts.getFriendHpBeforeBattleCombined().get(i - 1);

                name = "cHp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText(ts.getFriendNowhpsCombined().get(i - 1) + "/" + ts.getFriendMaxhpsCombined().get(i - 1));

                name = "cDamage" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                int damage = ts.getFriendNowhpsCombined().get(i - 1) - ts.getFriendHpBeforeBattleCombined().get(i - 1);
                text.setVisibility(View.VISIBLE);
                text.setText(String.valueOf(damage));
                if (damage == 0) {
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                } else {
                    //オーシャンブルー
                    text.setTextColor(Color.rgb(0, 102, 204));

                    if (damage * -1 > ts.getFriendHpBeforeBattleCombined().get(i - 1)) {
                        friendDamageSum += ts.getFriendHpBeforeBattleCombined().get(i - 1);
                    } else {
                        friendDamageSum += damage * -1;
                    }
                }
            }

            //敵第2艦隊
            for (int i = 1; i <= 6; i++) {
                if (i > ts.getEnemyShipIdCombined().size()) {
                    String name = "ecName" + i;
                    int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "ecLv" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "ecState" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "ecBeforeHp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "ecHp" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    name = "ecDamage" + i;
                    strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                    text = (TextView) getActivity().findViewById(strId);
                    text.setVisibility(View.GONE);

                    continue;
                }

                int id = ts.getEnemyShipIdCombined().get(i - 1);
                MstShip eShip = MstShipManager.INSTANCE.getMstShip(id);

                String name = "ecName" + i;
                int strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText(eShip.getName());

                name = "ecLv" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText(eShip.getYomi() + "(Lv" + ts.getBattle().geteShipLvCombined().get(i) + ")");

                name = "ecState" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setVisibility(View.VISIBLE);

                if (ts.getEnemyNowhpsCombined().get(i - 1) <= 0) {
                    text.setText("撃沈");
                    //オーシャンブルー
                    text.setTextColor(Color.rgb(0, 102, 204));
                    enemySink++;
                } else if (ts.getEnemyNowhpsCombined().get(i - 1) <= ts.getEnemyMaxhpsCombined().get(i - 1) / 4) {
                    text.setText("大破");
                    text.setTextColor(Color.RED);
                } else if (ts.getEnemyNowhpsCombined().get(i - 1) <= ts.getEnemyMaxhpsCombined().get(i - 1) / 2) {
                    text.setText("中破");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (ts.getEnemyNowhpsCombined().get(i - 1) <= ts.getEnemyMaxhpsCombined().get(i - 1) * 3 / 4) {
                    text.setText("小破");
                    //黄色
                    text.setTextColor(Color.rgb(255, 255, 0));
                } else if (ts.getEnemyNowhpsCombined().get(i - 1) < ts.getEnemyMaxhpsCombined().get(i - 1)) {
                    text.setText("健在");
                    //ライム
                    text.setTextColor((Color.rgb(153, 204, 0)));
                } else {
                    text.setText("無傷");
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                }

                name = "ecBeforeHp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(ts.getBattle().getNowhpsCombined().get(i + 6) + "/" + ts.getEnemyMaxhpsCombined().get(i - 1) + "→");

                enemyHpSum += ts.getBattle().getNowhpsCombined().get(i + 6);

                name = "ecHp" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                text.setText(ts.getEnemyNowhpsCombined().get(i - 1) + "/" + ts.getEnemyMaxhpsCombined().get(i - 1));

                name = "ecDamage" + i;
                strId = getResources().getIdentifier(name, "id", getActivity().getPackageName());
                text = (TextView) getActivity().findViewById(strId);
                int damage = ts.getEnemyNowhpsCombined().get(i - 1) - ts.getBattle().getNowhpsCombined().get(i + 6);
                text.setText(String.valueOf(damage));
                if (damage == 0) {
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                } else {
                    //オーシャンブルー
                    text.setTextColor(Color.rgb(0, 102, 204));

                    if (damage * -1 > ts.getEnemyHpBeforeBattleCombined().get(i - 1)) {
                        enemyDamageSum += ts.getEnemyHpBeforeBattleCombined().get(i - 1);
                    } else {
                        enemyDamageSum += damage * -1;
                    }
                }
            }

            text = (TextView) getActivity().findViewById(R.id.rank);
            int friendNum = ts.getFriendShipId().size() + ts.getFriendShipIdCombined().size();
            int enemyNum = ts.getEnemyShipId().size() + ts.getEnemyShipIdCombined().size();

            if (enemyNum == enemySink && friendSink == 0 && friendDamageSum == 0) {
                text.setText("完全勝利S");
            } else if (enemyNum == enemySink && friendSink == 0) {
                text.setText("S勝利");
            } else if (enemySink >= Math.floor(enemyNum * 2 / 3) && friendSink == 0) {
                text.setText("A勝利");
            } else if (ts.getEnemyNowhps().get(0) <= 0 && enemySink > friendSink) {
                text.setText("B勝利");
            } else if (Math.floor(100 * enemyDamageSum / enemyHpSum) * 10 > Math.floor(100 * friendDamageSum / friendHpSum) * 25) {
                text.setText("B勝利");
            } else if (Math.floor(100 * enemyDamageSum / enemyHpSum) * 10 > Math.floor(100 * friendDamageSum / friendHpSum) * 9) {
                text.setText("C敗北");
            } else if (ts.getEnemyNowhps().get(0) <= 0 || friendSink == 0 || friendNum - friendSink >= 2) {
                text.setText("D敗北");
            } else {
                text.setText("E敗北");
            }
        } catch (Exception e) {
            e.printStackTrace();
            manager = getChildFragmentManager();
            fragment = (ErrorFragment) manager.findFragmentById(R.id.fragment);
            transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }

    }
}
