package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.ShipUtility;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;

public class ShipDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("艦娘詳細情報");

        Intent intent = getIntent();
        final MyShip myShip = MyShipManager.INSTANCE.getMyShip(intent.getIntExtra("shipId", 0));
        final Deck deck = DeckManager.INSTANCE.getDeck(intent.getIntExtra("deckId", 0));

        //画面の向き
        boolean usesLandscape = intent.getBooleanExtra("usesLandscape", false);
        if (usesLandscape) {
            intent.putExtra("usesLandscape", false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        TextView text = (TextView) findViewById(R.id.shipName);
        text.setText(myShip.getName());

        text = (TextView) findViewById(R.id.lv);
        text.setText("Lv:" + String.valueOf(myShip.getLv()));

        text = (TextView) findViewById(R.id.state);
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

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setMax(myShip.getMaxhp());
        bar.setProgress(myShip.getNowhp());

        text = (TextView) findViewById(R.id.hp);
        text.setText(Integer.toString(myShip.getNowhp()) + "/" + Integer.toString(myShip.getMaxhp()));

        text = (TextView) findViewById(R.id.fuel);
        int fuelMax = MstShipManager.INSTANCE.getMstShip(myShip.getShipId()).getFuelMax();
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

        text = (TextView) findViewById(R.id.bull);
        int bullMax = MstShipManager.INSTANCE.getMstShip(myShip.getShipId()).getBullMax();
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

        text = (TextView) findViewById(R.id.cond);
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

        List<MySlotItem> slotItemList = new ArrayList<>();
        for (int slotItemId : myShip.getSlot()) {
            if (slotItemId != -1) {
                slotItemList.add(MySlotItemManager.INSTANCE.getMySlotItem(slotItemId));
            }
        }

        for (int i = 1; i <= 4; i++) {
            int slotItemId = myShip.getSlot().get(i - 1);

            if (slotItemId == -1) {
                String name = "space" + i;
                int strId = getResources().getIdentifier(name, "id", getPackageName());
                text = (TextView) findViewById(strId);
                text.setVisibility(View.INVISIBLE);


                name = "equipIcon" + i;
                strId = getResources().getIdentifier(name, "id", getPackageName());
                ImageView image = (ImageView) findViewById(strId);
                if (i <= myShip.getSlotnum()) {
                    image.setImageResource(EquipIconId.EMPTY.getImageId());
                } else {
                    image.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
                }

                name = "equipment" + i;
                strId = getResources().getIdentifier(name, "id", getPackageName());
                text = (TextView) findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "alv" + i;
                strId = getResources().getIdentifier(name, "id", getPackageName());
                text = (TextView) findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                name = "improvement" + i;
                strId = getResources().getIdentifier(name, "id", getPackageName());
                text = (TextView) findViewById(strId);
                text.setVisibility(View.INVISIBLE);

                continue;
            }

            MySlotItem mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(slotItemId);

            String name = "space" + i;
            int strId = getResources().getIdentifier(name, "id", getPackageName());
            text = (TextView) findViewById(strId);
            text.setVisibility(View.VISIBLE);
            if (myShip.getOnslot().get(i - 1) == 0) {
                text.setText("");
            } else {
                text.setText(String.valueOf(myShip.getOnslot().get(i - 1)));
            }

            name = "equipIcon" + i;
            strId = getResources().getIdentifier(name, "id", getPackageName());
            ImageView image = (ImageView) findViewById(strId);
            image.setImageResource(EquipIconId.toEquipIconId(MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId()).getType().get(3)).getImageId());

            name = "equipment" + i;
            strId = getResources().getIdentifier(name, "id", getPackageName());
            text = (TextView) findViewById(strId);
            text.setVisibility(View.VISIBLE);
            text.setText(MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId()).getName());

            name = "alv" + i;
            strId = getResources().getIdentifier(name, "id", getPackageName());
            text = (TextView) findViewById(strId);
            text.setVisibility(View.VISIBLE);
            switch (mySlotItem.getAlv()) {
                case 0:
                    text.setText("");
                    break;
                case 1:
                    text.setText("|");
                    text.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 2:
                    text.setText("||");
                    text.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 3:
                    text.setText("|||");
                    text.setTextColor(Color.rgb(67, 135, 233));
                    break;
                case 4:
                    text.setText("\\");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 5:
                    text.setText("\\\\");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 6:
                    text.setText("\\\\\\");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
                case 7:
                    text.setText(">>");
                    text.setTextColor(Color.rgb(243, 213, 26));
                    break;
            }

            name = "improvement" + i;
            strId = getResources().getIdentifier(name, "id", getPackageName());
            text = (TextView) findViewById(strId);
            text.setVisibility(View.VISIBLE);
            if (mySlotItem.getLevel() == 0) {
                text.setText("");
            } else {
                text.setText("★" + String.valueOf(mySlotItem.getLevel()));
            }
        }

        ImageView image = (ImageView) findViewById(R.id.extraSlotIcon);
        text = (TextView) findViewById(R.id.extraSlot);

        if (MySlotItemManager.INSTANCE.contains(myShip.getSlotEx())) {
            MstSlotitem mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlotEx()).getMstId());
            image.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType().get(3)).getImageId());
            text.setText(mstSlotitem.getName());
        } else {
            if (myShip.getSlotEx() == 0) {
                image.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
            } else if (myShip.getSlotEx() == -1) {
                image.setImageResource(EquipIconId.EMPTY.getImageId());
            }

            text.setText("");
        }

        text = (TextView) findViewById(R.id.shellingBasicAttackPower);
        int power = (int) ShipUtility.getShellingBasicAttackPower(myShip);
        text.setText(String.valueOf(power));

        text = (TextView) findViewById(R.id.nightBattleBasicAttackPower);
        power = (int) ShipUtility.getNightBattleBasicAttackPower(myShip);
        text.setText(String.valueOf(power));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean result = true;

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

}
