package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.App;
import jp.gr.java_conf.snake0394.loglook_android.DeckUtility;
import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.activity.ShipDetailActivity;

/**
 * 第一艦隊のFragment
 */
public class DeckFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public DeckFragment() {
        // Required empty public constructor
    }


    public static DeckFragment newInstance() {
        DeckFragment fragment = new DeckFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_deck, container, false);

        try {
            final Deck deck = DeckManager.INSTANCE.getDeck(getArguments().getInt("deckId"));
            final Resources resources = getResources();
            TextView text;
            final List<Integer> shipId = deck.getShipId();

            for (int i = 1; i <= shipId.size(); i++) {

                final String packageName = App.getInstance().getPackageName();
                String name;
                int strId;

                //空きの場合
                if (shipId.get(i - 1) == -1) {
                    name = "name" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "cond" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "lv" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "state" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "progressBar" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    ProgressBar progressBar = (ProgressBar) rootView.findViewById(strId);
                    progressBar.setVisibility(View.INVISIBLE);

                    name = "hp" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "fuelText" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "fuel" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "bullText" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "bull" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    for (int j = 1; j <= 4; j++) {
                        name = "slot" + i + j;
                        strId = resources.getIdentifier(name, "id", packageName);
                        ImageView image = (ImageView) rootView.findViewById(strId);
                        image.setVisibility(View.INVISIBLE);
                    }

                    name = "slotEx" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    text = (TextView) rootView.findViewById(strId);
                    text.setVisibility(View.INVISIBLE);

                    name = "slotExIcon" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    ImageView image = (ImageView) rootView.findViewById(strId);
                    image.setVisibility(View.INVISIBLE);

                    name = "detail" + i;
                    strId = resources.getIdentifier(name, "id", packageName);
                    Button button = (Button) rootView.findViewById(strId);
                    button.setVisibility(View.GONE);
                    continue;
                }

                final MyShip myShip = MyShipManager.INSTANCE.getMyShip(shipId.get(i - 1));

                name = "name" + i;
                strId = resources.getIdentifier(name, "id", App.getInstance().getPackageName());
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText(myShip.getName());

                name = "lv" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText("Lv:" + String.valueOf(myShip.getLv()));
                //levelSum += myShip.getLv();

                name = "state" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);
                if (deck.getMission().get(0) == 1 || deck.getMission().get(0) == 3) {
                    text.setText("遠征");
                    //インディゴ
                    text.setBackgroundColor(Color.parseColor("#4e5a92"));
                } else if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                    text.setText("退避");
                    text.setBackgroundColor(Color.LTGRAY);
                } else if (DockTimer.INSTANCE.getShipId(1) == myShip.getId() || DockTimer.INSTANCE.getShipId(2) == myShip.getId() || DockTimer.INSTANCE.getShipId(3) == myShip.getId() || DockTimer.INSTANCE.getShipId(4) == myShip.getId()) {
                    text.setText("入渠");
                    //アクア
                    text.setBackgroundColor(Color.rgb(51, 204, 204));
                } else if (myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                    text.setText("大破");
                    text.setBackgroundColor(Color.RED);
                } else if (myShip.getNowhp() <= myShip.getMaxhp() / 2) {
                    text.setText("中破");
                    //オレンジ
                    text.setBackgroundColor(Color.rgb(255, 140, 0));
                } else if (myShip.getNowhp() <= myShip.getMaxhp() * 3 / 4) {
                    text.setText("小破");
                    //黄色
                    text.setBackgroundColor(Color.rgb(255, 230, 30));
                } else if (myShip.getNowhp() < myShip.getMaxhp()) {
                    text.setText("健在");
                    //ライム
                    text.setBackgroundColor((Color.rgb(153, 204, 0)));
                } else {
                    text.setText("無傷");
                    //緑
                    text.setBackgroundColor(Color.rgb(59, 175, 117));
                }

                MySlotItem mySlotItem;
                MstSlotitem mstSlotitem;
                for (int j = 1; j <= 4; j++) {
                    name = "slot" + i + j;
                    strId = resources.getIdentifier(name, "id", packageName);
                    ImageView image = (ImageView) rootView.findViewById(strId);
                    image.setVisibility(View.VISIBLE);

                    if (j > myShip.getSlotnum()) {
                        image.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
                        continue;
                    }

                    if (myShip.getSlot().get(j - 1) == -1) {
                        image.setImageResource(EquipIconId.EMPTY.getImageId());
                        continue;
                    }

                    mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlot().get(j - 1));
                    mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());

                    image.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType().get(3)).getImageId());
                }

                name = "slotEx" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);

                name = "slotExIcon" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                ImageView image = (ImageView) rootView.findViewById(strId);
                image.setVisibility(View.VISIBLE);

                if (MySlotItemManager.INSTANCE.contains(myShip.getSlotEx())) {
                    mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlotEx());
                    mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                    image.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType().get(3)).getImageId());
                } else {
                    if (myShip.getSlotEx() == 0) {
                        image.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
                    } else if (myShip.getSlotEx() == -1) {
                        image.setImageResource(EquipIconId.EMPTY.getImageId());
                    }
                }

                name = "progressBar" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                ProgressBar bar = (ProgressBar) rootView.findViewById(strId);
                bar.setVisibility(View.VISIBLE);
                bar.setMax(myShip.getMaxhp());
                bar.setProgress(myShip.getNowhp());

                name = "hp" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText(Integer.toString(myShip.getNowhp()) + "/" + Integer.toString(myShip.getMaxhp()));

                name = "fuelText" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);

                name = "fuel" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);
                MstShip mstShip = MstShipManager.INSTANCE.getMstShip(myShip.getShipId());
                int fuelMax = mstShip.getFuelMax();
                int fuelNow = myShip.getFuel();
                if (fuelNow == fuelMax) {
                    text.setText("||||||||||");
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                } else if (fuelNow >= fuelMax * 8 / 9) {
                    text.setText("|||||||||");
                    //黄色
                    text.setTextColor(Color.rgb(237, 185, 24));
                } else if (fuelNow >= fuelMax * 7 / 9) {
                    text.setText("||||||||");
                    //黄色
                    text.setTextColor(Color.rgb(237, 185, 24));
                } else if (fuelNow >= fuelMax * 6 / 9) {
                    text.setText("|||||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (fuelNow >= fuelMax * 5 / 9) {
                    text.setText("||||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (fuelNow >= fuelMax * 4 / 9) {
                    text.setText("|||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (fuelNow >= fuelMax * 3 / 9) {
                    text.setText("||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (fuelNow >= fuelMax * 2 / 9) {
                    text.setText("|||");
                    text.setTextColor(Color.RED);
                } else if (fuelNow >= fuelMax * 1 / 9) {
                    text.setText("||");
                    text.setTextColor(Color.RED);
                } else if (fuelNow > 0) {
                    text.setText("|");
                    text.setTextColor(Color.RED);
                } else {
                    text.setText("-");
                    //グレー
                    text.setTextColor(Color.rgb(118, 118, 118));
                }

                name = "bullText" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);

                name = "bull" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);
                int bullMax = mstShip.getBullMax();
                int bullNow = myShip.getBull();
                if (bullNow == bullMax) {
                    text.setText("||||||||||");
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                } else if (bullNow >= bullMax * 8 / 9) {
                    text.setText("|||||||||");
                    //黄色
                    text.setTextColor(Color.rgb(237, 185, 24));
                } else if (bullNow >= bullMax * 7 / 9) {
                    text.setText("||||||||");
                    //黄色
                    text.setTextColor(Color.rgb(237, 185, 24));
                } else if (bullNow >= bullMax * 6 / 9) {
                    text.setText("|||||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (bullNow >= bullMax * 5 / 9) {
                    text.setText("||||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (bullNow >= bullMax * 4 / 9) {
                    text.setText("|||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (bullNow >= bullMax * 3 / 9) {
                    text.setText("||||");
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else if (bullNow >= bullMax * 2 / 9) {
                    text.setText("|||");
                    text.setTextColor(Color.RED);
                } else if (bullNow >= bullMax * 1 / 9) {
                    text.setText("||");
                    text.setTextColor(Color.RED);
                } else if (bullNow > 0) {
                    text.setText("|");
                    text.setTextColor(Color.RED);
                } else {
                    text.setText("-");
                    //グレー
                    text.setTextColor(Color.rgb(118, 118, 118));
                }

                name = "cond" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                text = (TextView) rootView.findViewById(strId);
                text.setVisibility(View.VISIBLE);
                text.setText("cond:" + Integer.toString(myShip.getCond()));
                //condの値で色分けする
                if (myShip.getCond() >= 50) {
                    //緑
                    text.setTextColor(Color.rgb(59, 175, 117));
                } else if (myShip.getCond() >= 40) {
                    //グレー
                    text.setTextColor(Color.rgb(118, 118, 118));
                } else if (myShip.getCond() >= 30) {
                    //黄色
                    text.setTextColor(Color.rgb(237, 185, 24));
                } else if (myShip.getCond() >= 20) {
                    //オレンジ
                    text.setTextColor(Color.rgb(255, 140, 0));
                } else {
                    text.setTextColor(Color.RED);
                }

                name = "detail" + i;
                strId = resources.getIdentifier(name, "id", packageName);
                Button button = (Button) rootView.findViewById(strId);
                button.setVisibility(View.VISIBLE);
                final int id = myShip.getId();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ShipDetailActivity.class);
                        intent.putExtra("shipId", id);
                        intent.putExtra("deckId", getArguments().getInt("deckId"));

                        Configuration config = resources.getConfiguration();
                        switch (config.orientation) {
                            case Configuration.ORIENTATION_PORTRAIT:
                                intent.putExtra("usesLandscape", false);
                                break;
                            case Configuration.ORIENTATION_LANDSCAPE:
                                intent.putExtra("usesLandscape", true);
                                break;
                        }

                        startActivity(intent);
                    }
                });
            }

            text = (TextView) rootView.findViewById(R.id.seiku);
            text.setText(String.valueOf(DeckUtility.getSeiku(deck)));

            text = (TextView) rootView.findViewById(R.id.touchStartRate);
            text.setText(String.valueOf(DeckUtility.getTouchStartRate(deck)) + "%");

            text = (TextView) rootView.findViewById(R.id.sakuteki25);
            text.setText(String.valueOf(DeckUtility.getSakuteki25(deck)));

            text = (TextView) rootView.findViewById(R.id.sakuteki33);
            text.setText(String.valueOf(DeckUtility.getSakuteki33(deck)));

            text = (TextView) rootView.findViewById(R.id.levelSum);
            text.setText(String.valueOf(deck.getLevelSum()));

            text = (TextView) rootView.findViewById(R.id.condRecoveryTime);
            text.setText(deck.getCondRecoveryTime());

        } catch (Exception e) {
            ErrorLogger.writeLog(e);
            e.printStackTrace();
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
