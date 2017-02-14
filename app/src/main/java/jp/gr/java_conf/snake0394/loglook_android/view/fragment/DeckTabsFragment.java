package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.TabLayoutSupport;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;

/**
 * Deck1~4Fragmentを管理するFragmentです
 */
public class DeckTabsFragment extends Fragment {
    private View view;
    private TabLayoutAdapter adapter;
    private Unbinder unbinder;
    private LoopRecyclerViewPager viewPager;

    public DeckTabsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        if (DeckManager.INSTANCE.getDeckNum() == 0) {
            return ErrorFragment.newInstance();
        }
        DeckTabsFragment fragment = new DeckTabsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_deck_manager_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.viewPager = ButterKnife.findById(view, R.id.viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewPager.setLayoutManager(layout);
        final TabLayout tabLayout = ButterKnife.findById(view, R.id.tabs);
        DeckTabsRecyclerViewAdapter recyclerAdapter = new DeckTabsRecyclerViewAdapter(getFragmentManager());
        recyclerAdapter.setItems(DeckManager.INSTANCE.getDeckList());
        viewPager.setAdapter(recyclerAdapter);
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
    public void onStart() {
        super.onStart();
    }

    private static class TabLayoutAdapter implements TabLayoutSupport.ViewPagerTabLayoutAdapter {

        public TabLayoutAdapter() {
            super();
        }

        @Override
        public String getPageTitle(int i) {
            return String.valueOf(i + 1);
        }

        @Override
        public int getItemCount() {
            return DeckManager.INSTANCE.getDeckNum();
        }
    }

    private static class TabListener extends TabLayoutSupport.TabLayoutOnPageChangeListener implements TabLayout.OnTabSelectedListener {
        private FragmentManager fragmentManager;
        private TabLayoutSupport.ViewPagerOnTabSelectedListener superListener;
        private boolean pageChanged;

        public TabListener(TabLayout tabLayout, RecyclerViewPager viewPager, FragmentManager fragmentManager) {
            super(tabLayout, viewPager);
            this.fragmentManager = fragmentManager;
            superListener = new TabLayoutSupport.ViewPagerOnTabSelectedListener(viewPager);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            superListener.onTabSelected(tab);
            Log.d("selected", String.valueOf(tab.getPosition()));
            this.pageChanged = false;
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            Log.d("reselected", String.valueOf(tab.getPosition()));
            if(pageChanged){
                android.support.v4.app.DialogFragment dialogFragment = DeckMenuDialogFragment.newInstance(tab.getPosition() + 1);
                dialogFragment.show(this.fragmentManager, "fragment_dialog");
            }
        }

        @Override
        public void OnPageChanged(int oldPosition, int newPosition) {
            super.OnPageChanged(oldPosition, newPosition);
            Log.d("pageChanged", oldPosition + "to" + newPosition);
            this.pageChanged = true;
        }
    }
}