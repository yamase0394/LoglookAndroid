package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.snake0394.loglook_android.DockTimer;
import jp.gr.java_conf.snake0394.loglook_android.Escape;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MstSlotitemManager;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItem;
import jp.gr.java_conf.snake0394.loglook_android.bean.MySlotItemManager;
import jp.gr.java_conf.snake0394.loglook_android.view.EquipIconId;

/**
 * Created by snake0394 on 2016/12/08.
 */

public class DamagedShipAdapter extends RecyclerView.Adapter<DamagedShipAdapter.DamagedShipViewHolder> {

    private SortedList<MyShip> sortedList;
    private final FragmentManager fragmentManager;

    public DamagedShipAdapter(FragmentManager fragmentManager, String sortType, String order) {
        sortedList = new SortedList<>(MyShip.class, new DamagedShipCallback(this, sortType, order));
        this.fragmentManager = fragmentManager;
    }

    @Override
    public DamagedShipViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_damaged_ship_cardview, viewGroup, false);
        return new DamagedShipViewHolder(itemView, fragmentManager);
    }

    @Override
    public void onBindViewHolder(DamagedShipViewHolder sampleViewHolder, int i) {
        MyShip data = sortedList.get(i);
        if (data != null) {
            sampleViewHolder.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void addDataOf(List<MyShip> dataList) {
        sortedList.addAll(dataList);
    }

    public void removeDataOf(List<MyShip> dataList) {
        sortedList.beginBatchedUpdates();
        for (MyShip data : dataList) {
            sortedList.remove(data);
        }
        sortedList.endBatchedUpdates();
    }

    public void clearData() {
        sortedList.clear();
    }

    public SortedList<MyShip> getList() {
        return sortedList;
    }

    class DamagedShipViewHolder extends RecyclerView.ViewHolder {

        private FragmentManager fragmentManager;
        private TextView name;
        private TextView lv;
        private TextView state;
        private ImageView slot1;
        private ImageView slot2;
        private ImageView slot3;
        private ImageView slot4;
        private ImageView slotEx;
        private LinearLayout equipments;
        private ProgressBar hpBar;
        private TextView hp;
        private TextView cond;
        private TextView repairTime;
        private TextView completeTime;

        public DamagedShipViewHolder(View rootView, FragmentManager fragmentManager) {
            super(rootView);
            this.fragmentManager = fragmentManager;
            name = (TextView) rootView.findViewById(R.id.name);
            lv = (TextView) rootView.findViewById(R.id.lv);
            state = (TextView) rootView.findViewById(R.id.state);
            slot1 = (ImageView) rootView.findViewById(R.id.slot1);
            slot2 = (ImageView) rootView.findViewById(R.id.slot2);
            slot3 = (ImageView) rootView.findViewById(R.id.slot3);
            slot4 = (ImageView) rootView.findViewById(R.id.slot4);
            slotEx = (ImageView) rootView.findViewById(R.id.imageview_slot_ex);
            equipments = (LinearLayout) rootView.findViewById(R.id.equipments);
            hpBar = (ProgressBar) rootView.findViewById(R.id.hpBar);
            hp = (TextView) rootView.findViewById(R.id.hp);
            cond = (TextView) rootView.findViewById(R.id.cond);
            repairTime = (TextView) rootView.findViewById(R.id.repairTime);
            completeTime = (TextView) rootView.findViewById(R.id.completeTime);
        }

        public void bind(@NonNull final MyShip myShip) {
            name.setText(myShip.getName());

            lv.setText("Lv:" + String.valueOf(myShip.getLv()));

            Deck deck = null;
            boolean isContained = false;
            for (int i = 2; i <= DeckManager.INSTANCE.getDeckNum(); i++) {
                deck = DeckManager.INSTANCE.getDeck(i);
                isContained = deck.getShipId().contains(myShip.getId());
                if (isContained) {
                    break;
                }
            }
            if (isContained && (deck.getMission().get(0) == 1 || deck.getMission().get(0) == 3)) {
                state.setText("遠征");
                //インディゴ
                state.setBackgroundColor(Color.parseColor("#4e5a92"));
            } else if (Escape.INSTANCE.isEscaped(myShip.getId())) {
                state.setText("退避");
                state.setBackgroundColor(Color.LTGRAY);
            } else if (DockTimer.INSTANCE.getShipId(1) == myShip.getId() || DockTimer.INSTANCE.getShipId(2) == myShip.getId() || DockTimer.INSTANCE.getShipId(3) == myShip.getId() || DockTimer.INSTANCE.getShipId(4) == myShip.getId()) {
                state.setText("入渠");
                //アクア
                state.setBackgroundColor(Color.rgb(51, 204, 204));
            } else if (myShip.getNowhp() <= myShip.getMaxhp() / 4) {
                state.setText("大破");
                state.setBackgroundColor(Color.RED);
            } else if (myShip.getNowhp() <= myShip.getMaxhp() / 2) {
                state.setText("中破");
                //オレンジ
                state.setBackgroundColor(Color.rgb(255, 140, 0));
            } else if (myShip.getNowhp() <= myShip.getMaxhp() * 3 / 4) {
                state.setText("小破");
                //黄色
                state.setBackgroundColor(Color.rgb(255, 230, 30));
            } else {
                state.setText("健在");
                //ライム
                state.setBackgroundColor((Color.rgb(153, 204, 0)));
            }

            MySlotItem mySlotItem;
            MstSlotitem mstSlotitem;
            if (1 > myShip.getSlotnum()) {
                slot1.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
            } else if (myShip.getSlot()
                             .get(0) == -1) {
                slot1.setImageResource(EquipIconId.EMPTY.getImageId());
            } else {
                //装備取得前に開くとmySlotItemがnullになる
                mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlot()
                                                                            .get(0));
                if (mySlotItem != null) {
                    mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                    slot1.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType()
                                                                                .get(3))
                                                      .getImageId());
                } else {
                    slot1.setImageResource(EquipIconId.UNKNOWN.getImageId());
                }
            }

            if (2 > myShip.getSlotnum()) {
                slot2.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
            } else if (myShip.getSlot()
                             .get(1) == -1) {
                slot2.setImageResource(EquipIconId.EMPTY.getImageId());
            } else {
                mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlot()
                                                                            .get(1));
                if (mySlotItem != null) {
                    mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                    slot2.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType()
                                                                                .get(3))
                                                      .getImageId());
                } else {
                    slot2.setImageResource(EquipIconId.UNKNOWN.getImageId());
                }
            }

            if (3 > myShip.getSlotnum()) {
                slot3.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
            } else if (myShip.getSlot()
                             .get(2) == -1) {
                slot3.setImageResource(EquipIconId.EMPTY.getImageId());
            } else {
                mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlot()
                                                                            .get(2));
                if (mySlotItem != null) {
                    mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                    slot3.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType()
                                                                                .get(3))
                                                      .getImageId());
                } else {
                    slot3.setImageResource(EquipIconId.UNKNOWN.getImageId());
                }
            }

            if (4 > myShip.getSlotnum()) {
                slot4.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
            } else if (myShip.getSlot()
                             .get(3) == -1) {
                slot4.setImageResource(EquipIconId.EMPTY.getImageId());
            } else {
                mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlot()
                                                                            .get(3));
                if (mySlotItem != null) {
                    mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                    slot4.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType()
                                                                                .get(3))
                                                      .getImageId());
                } else {
                    slot4.setImageResource(EquipIconId.UNKNOWN.getImageId());
                }
            }

            if (MySlotItemManager.INSTANCE.contains(myShip.getSlotEx())) {
                mySlotItem = MySlotItemManager.INSTANCE.getMySlotItem(myShip.getSlotEx());
                mstSlotitem = MstSlotitemManager.INSTANCE.getMstSlotitem(mySlotItem.getMstId());
                slotEx.setImageResource(EquipIconId.toEquipIconId(mstSlotitem.getType().get(3)).getImageId());
            } else {
                if (myShip.getSlotEx() == 0) {
                    slotEx.setImageResource(EquipIconId.NOT_AVAILABLE.getImageId());
                } else if (myShip.getSlotEx() == -1) {
                    slotEx.setImageResource(EquipIconId.EMPTY.getImageId());
                }
            }

            equipments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.DialogFragment dialogFragment = EquipmentDialogFragment.newInstance(myShip.getId());
                    dialogFragment.show(fragmentManager, "fragment_dialog");
                }
            });

            hpBar.setMax(myShip.getMaxhp());
            hpBar.setProgress(myShip.getNowhp());

            hp.setText(Integer.toString(myShip.getNowhp()) + "/" + Integer.toString(myShip.getMaxhp()));

            cond.setText(Integer.toString(myShip.getCond()));
            //condの値で色分けする
            if (myShip.getCond() >= 50) {
                //緑
                cond.setTextColor(Color.rgb(59, 175, 117));
            } else if (myShip.getCond() >= 40) {
                //グレー
                cond.setTextColor(Color.rgb(118, 118, 118));
            } else if (myShip.getCond() >= 30) {
                //黄色
                cond.setTextColor(Color.rgb(237, 185, 24));
            } else if (myShip.getCond() >= 20) {
                //オレンジ
                cond.setTextColor(Color.rgb(255, 140, 0));
            } else {
                cond.setTextColor(Color.RED);
            }

            long millisUntilFinished = myShip.getNdockTime();
            StringBuffer sb = new StringBuffer();
            long day = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
            if (day != 0) {
                sb.append(day + "日");
                millisUntilFinished -= TimeUnit.DAYS.toMillis(day);
            }
            long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
            if (hour != 0) {
                sb.append(hour + "時間");
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hour);
            }
            long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            if (minute != 0) {
                sb.append(minute + "分");
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minute);
            }
            repairTime.setText(sb.toString());

            SimpleDateFormat sdf = new SimpleDateFormat("dd日 HH:mm");
            completeTime.setText(sdf.format(Calendar.getInstance().getTimeInMillis() + myShip.getNdockTime()));
        }
    }

    private static class DamagedShipCallback extends SortedList.Callback<MyShip> {

        private RecyclerView.Adapter adapter;
        private String sortType;
        private String order;

        DamagedShipCallback(@NonNull RecyclerView.Adapter adapter, String sortType, String order) {
            this.adapter = adapter;
            this.sortType = sortType;
            this.order = order;
        }

        @Override
        public int compare(MyShip data1, MyShip data2) {
            int result = (int) (data1.getNdockTime() - data2.getNdockTime());

            switch (sortType) {
                case "修復時間":
                    result = (int) (data1.getNdockTime() - data2.getNdockTime());

                    if (result == 0) {
                        int damageData1;
                        if (data1.getNowhp() <= data1.getMaxhp() / 4) {
                            //大破
                            damageData1 = 4;
                        } else if (data1.getNowhp() <= data1.getMaxhp() / 2) {
                            //中破
                            damageData1 = 3;
                        } else if (data1.getNowhp() <= data1.getMaxhp() * 3 / 4) {
                            //小破
                            damageData1 = 2;
                        } else if (data1.getNowhp() < data1.getMaxhp()) {
                            //微ダメージ
                            damageData1 = 1;
                        } else {
                            //ノーダメージ
                            damageData1 = 0;
                        }

                        int damageData2;
                        if (data2.getNowhp() <= data2.getMaxhp() / 4) {
                            //大破
                            damageData2 = 4;
                        } else if (data2.getNowhp() <= data2.getMaxhp() / 2) {
                            //中破
                            damageData2 = 3;
                        } else if (data2.getNowhp() <= data2.getMaxhp() * 3 / 4) {
                            //小破
                            damageData2 = 2;
                        } else if (data2.getNowhp() < data2.getMaxhp()) {
                            //微ダメージ
                            damageData2 = 1;
                        } else {
                            //ノーダメージ
                            damageData2 = 0;
                        }

                        result = damageData1 - damageData2;
                    }
                    break;
                case "損傷度":
                    int damageData1;
                    if (data1.getNowhp() <= data1.getMaxhp() / 4) {
                        //大破
                        damageData1 = 4;
                    } else if (data1.getNowhp() <= data1.getMaxhp() / 2) {
                        //中破
                        damageData1 = 3;
                    } else if (data1.getNowhp() <= data1.getMaxhp() * 3 / 4) {
                        //小破
                        damageData1 = 2;
                    } else if (data1.getNowhp() < data1.getMaxhp()) {
                        //微ダメージ
                        damageData1 = 1;
                    } else {
                        //ノーダメージ
                        damageData1 = 0;
                    }

                    int damageData2;
                    if (data2.getNowhp() <= data2.getMaxhp() / 4) {
                        //大破
                        damageData2 = 4;
                    } else if (data2.getNowhp() <= data2.getMaxhp() / 2) {
                        //中破
                        damageData2 = 3;
                    } else if (data2.getNowhp() <= data2.getMaxhp() * 3 / 4) {
                        //小破
                        damageData2 = 2;
                    } else if (data2.getNowhp() < data2.getMaxhp()) {
                        //微ダメージ
                        damageData2 = 1;
                    } else {
                        //ノーダメージ
                        damageData2 = 0;
                    }

                    result = damageData1 - damageData2;

                    if (result == 0) {
                        result = (int) (data1.getNdockTime() - data2.getNdockTime());
                    }
                    break;
            }

            switch (order) {
                case "降順":
                    result *= -1;
                    break;
            }
            return result;
        }

        @Override
        public void onInserted(int position, int count) {
            adapter.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            adapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            adapter.notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(MyShip oldData, MyShip newData) {
            return oldData.getShipId() == newData.getShipId();
        }

        @Override
        public boolean areItemsTheSame(MyShip data1, MyShip data2) {
            return data1.getShipId() == data2.getShipId();
        }
    }
}