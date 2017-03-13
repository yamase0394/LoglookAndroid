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

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;

import static butterknife.ButterKnife.findById;

/**
 * Deck1~4Fragmentを管理するFragmentです
 */
public class DeckFragment extends Fragment implements DeckTabsRecyclerViewAdapter.OnRecyclerViewClickListener{
    private View view;
    private Unbinder unbinder;

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

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewPager.setLayoutManager(layout);

        DeckTabsRecyclerViewAdapter recyclerAdapter = new DeckTabsRecyclerViewAdapter(this);
        recyclerAdapter.setItems(DeckManager.INSTANCE.getDeckList());
        viewPager.setAdapter(recyclerAdapter);

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
            if(pageChanged){
                android.support.v4.app.DialogFragment dialogFragment = DeckMenuDialogFragment.newInstance(tab.getPosition() + 1);
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