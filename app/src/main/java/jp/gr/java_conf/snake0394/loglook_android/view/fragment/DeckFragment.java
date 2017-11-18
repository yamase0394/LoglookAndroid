package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.TabLayoutSupport;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.Deck;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;

import static butterknife.ButterKnife.findById;

/**
 * Deck1~4Fragmentを管理するFragmentです
 */
public class DeckFragment extends Fragment implements DeckTabsRecyclerViewAdapter.OnRecyclerViewClickListener {
    private View view;
    private Unbinder unbinder;
    private List<Deck> deckList;

    public DeckFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        if (DeckManager.INSTANCE.getDeckNum() == 0) {
            return ErrorFragment.newInstance();
        }
        DeckFragment fragment = new DeckFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_deck_manager_new, container, false);
        unbinder = ButterKnife.bind(this, view);

        LoopRecyclerViewPager viewPager = findById(view, R.id.viewpager);
        viewPager.setHasFixedSize(true);

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewPager.setLayoutManager(layout);

        DeckTabsRecyclerViewAdapter recyclerAdapter = new DeckTabsRecyclerViewAdapter(this);
        deckList = DeckManager.INSTANCE.getDeckList();
        //二艦隊以上なら連合艦隊を編成できると判断
        if (deckList.size() >= 2) {
            deckList.add(null);
        }
        recyclerAdapter.setItems(deckList);
        viewPager.setAdapter(recyclerAdapter);

        viewPager.getRecycledViewPool().setMaxRecycledViews(DeckTabsRecyclerViewAdapter.VIEW_TYPE_NORMAL, 3);
        viewPager.getRecycledViewPool().setMaxRecycledViews(DeckTabsRecyclerViewAdapter.VIEW_TYPE_COMBINED, 1);
        viewPager.getRecycledViewPool().setMaxRecycledViews(DeckTabsRecyclerViewAdapter.VIEW_TYPE_7_SHIP_FLEET, 1);

        TabLayout tabLayout = findById(view, R.id.tabs);
        TabLayoutSupport.setupWithViewPager(tabLayout, viewPager, new TabLayoutAdapter());
        tabLayout.getTabAt(0).select();
        TabListener listener = new TabListener(tabLayout, viewPager, getFragmentManager());
        tabLayout.setOnTabSelectedListener(listener);
        viewPager.addOnPageChangedListener(listener);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRecyclerViewClicked(DialogFragment dialogFragment, Intent intent) {
        if (dialogFragment != null) {
            dialogFragment.show(getFragmentManager(), "dialog");
            return;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    private class TabLayoutAdapter implements TabLayoutSupport.ViewPagerTabLayoutAdapter {

        public TabLayoutAdapter() {
            super();
        }

        @Override
        public String getPageTitle(int i) {
            if (deckList.get(i) == null) {
                return "連合";
            } else if (i == 2 && deckList.get(i).getShipId().get(6) != -1) {
                //第三艦隊かつ7隻編成の場合は遊撃部隊として扱う
                return "遊撃部隊";
            }
            return String.valueOf(i + 1);
        }

        @Override
        public int getItemCount() {
            return deckList.size();
        }
    }

    private class TabListener extends TabLayoutSupport.TabLayoutOnPageChangeListener implements TabLayout.OnTabSelectedListener {
        private FragmentManager fragmentManager;
        private TabLayoutSupport.ViewPagerOnTabSelectedListener superListener;
        private boolean pageChanged;

        public TabListener(TabLayout tabLayout, RecyclerViewPager viewPager, FragmentManager fragmentManager) {
            super(tabLayout, viewPager);
            this.fragmentManager = fragmentManager;
            this.superListener = new TabLayoutSupport.ViewPagerOnTabSelectedListener(viewPager);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            superListener.onTabSelected(tab);
            this.pageChanged = false;
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            if (pageChanged) {
                int deckId = tab.getPosition() + 1;
                if (deckList.get(deckId - 1) == null) {
                    deckId = 1;
                }
                android.support.v4.app.DialogFragment dialogFragment = DeckMenuDialogFragment.newInstance(deckId);
                dialogFragment.show(this.fragmentManager, "fragment_dialog");
            }
        }

        @Override
        public void OnPageChanged(int oldPosition, int newPosition) {
            super.OnPageChanged(oldPosition, newPosition);
            this.pageChanged = true;
        }
    }
}