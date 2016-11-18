package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.DeckManager;

/**
 * Deck1~4Fragmentを管理するFragmentです
 */
public class DeckManagerFragment extends Fragment {
    private View view;
    private Adapter adapter;
    private Unbinder unbinder;

    public DeckManagerFragment() {
        // Required empty public constructor
    }


    public static DeckManagerFragment newInstance() {
        DeckManagerFragment fragment = new DeckManagerFragment();
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        for (int i = 1; i <= DeckManager.INSTANCE.getDeckNum(); i++) {
            Fragment fragment = DeckFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("deckId", i);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, String.valueOf(i));
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deck_manager, container, false);
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        final ViewPager viewPager = ButterKnife.findById(view, R.id.viewpager);
        setupViewPager(viewPager);
        final TabLayout tabLayout = ButterKnife.findById(view, R.id.tabs);
        //別スレッドで行わないと画面の向きが変更されない
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}