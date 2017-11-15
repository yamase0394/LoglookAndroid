package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.gr.java_conf.snake0394.loglook_android.R;

import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.オートジャイロ;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.ソナー;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.中口径主砲;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.副砲;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.噴式戦闘爆撃機_噴式景雲改;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.夜間戦闘機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.夜間攻撃機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.大口径主砲;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.大型飛行艇;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.対潜哨戒機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.対空機銃;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.小口径主砲;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.局地戦闘機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.水上機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.爆雷;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.艦上偵察機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.艦上戦闘機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.艦上攻撃機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.艦上爆撃機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.陸上攻撃機;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.電探;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.高射装置;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.高角砲;
import static jp.gr.java_conf.snake0394.loglook_android.view.EquipType3.魚雷;


/**
 * Created by snake0394 on 2016/12/08.
 */

public class EquipmentDrawerRecyclerAdapter extends RecyclerView.Adapter<EquipmentDrawerRecyclerAdapter.EquipmentViewHolder> implements OnDrawerItemSelectedListener {

    public interface OnItemClickListener {
        void onItemClick(String clickedItemName);
    }

    private int selectedPosition;
    private List<String> itemList;
    private OnItemClickListener parentListener;

    public EquipmentDrawerRecyclerAdapter(OnItemClickListener parentListener) {
        this.itemList = new ArrayList<>();
        this.parentListener = parentListener;
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.view_equip_fragment_drawer_item, viewGroup, false);
        return new EquipmentViewHolder(itemView, parentListener, this);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder sampleViewHolder, int i) {
        sampleViewHolder.bind(this.itemList.get(i), selectedPosition);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<String> newItemList) {
        this.itemList = newItemList;
    }

    public void setHighlight(int position) {
        if (this.selectedPosition == position) {
            return;
        }
        int prePosition = this.selectedPosition;
        this.selectedPosition = position;
        notifyItemChanged(prePosition);
        notifyItemChanged(this.selectedPosition);
    }

    @Override
    public void onDarwerItemSelected(int position) {
        setHighlight(position);
    }

    static class EquipmentViewHolder extends RecyclerView.ViewHolder {

        private OnItemClickListener parentListener;
        private OnDrawerItemSelectedListener adapterListener;

        @BindView(R.id.cardview)
        CardView cardView;
        @BindView(R.id.name)
        TextView nameTextView;
        @BindView(R.id.layout_icon)
        LinearLayout iconLayout;

        public EquipmentViewHolder(View rootView, OnItemClickListener parentListenr, OnDrawerItemSelectedListener adapterListener) {
            super(rootView);
            ButterKnife.bind(this, rootView);
            this.parentListener = parentListenr;
            this.adapterListener = adapterListener;
        }

        public void bind(String name, int selectedPosition) {
            if (getAdapterPosition() == selectedPosition) {
                cardView.setBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.colorAccent));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.drawer_slider_back));
            }

            nameTextView.setText(name);

            iconLayout.removeAllViews();
            switch (name) {
                case "小口径主砲":
                    ImageView icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(小口径主砲.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(高角砲.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "中口径主砲":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(中口径主砲.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "大口径主砲":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(大口径主砲.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "副砲":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(副砲.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(高角砲.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "魚雷":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(魚雷.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "艦戦/夜戦":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(艦上戦闘機.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(夜間戦闘機.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "艦爆/艦攻/噴式/夜攻":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(艦上爆撃機.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(艦上攻撃機.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(噴式戦闘爆撃機_噴式景雲改.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(夜間攻撃機.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "艦偵":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(艦上偵察機.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "水上機/飛行艇":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(水上機.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(大型飛行艇.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "電探":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(電探.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "機銃/高射装置":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(対空機銃.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(高射装置.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "高角砲":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(高角砲.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "対潜":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(ソナー.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(爆雷.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(対潜哨戒機.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(オートジャイロ.getImageId());
                    iconLayout.addView(icon);
                    break;
                case "陸上機":
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(局地戦闘機.getImageId());
                    iconLayout.addView(icon);
                    icon = new ImageView(iconLayout.getContext());
                    icon.setImageResource(陸上攻撃機.getImageId());
                    iconLayout.addView(icon);
                    break;
            }
        }

        @OnClick(R.id.cardview)
        void onClicked() {
            adapterListener.onDarwerItemSelected(getAdapterPosition());
            cardView.setBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.colorAccent));
            parentListener.onItemClick(nameTextView.getText().toString());
        }
    }

    public static class MyItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        public MyItemDecoration(final Context context) {
            final TypedArray array = context.obtainStyledAttributes(ATTRS);
            mDivider = array.getDrawable(0);
            array.recycle();
        }

        @Override
        public void getItemOffsets(final Rect outRect, final int itemPosition, final RecyclerView parent) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }

        @Override
        public void onDraw(final Canvas c, final RecyclerView parent) {
            drawVertical(c, parent);
        }

        public void drawVertical(final Canvas c, final RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}

