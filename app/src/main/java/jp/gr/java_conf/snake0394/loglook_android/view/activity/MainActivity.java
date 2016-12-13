package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.ConfigFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DamagedShipFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DeckFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DeckManagerFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DockFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.EquipmentFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.ErrorFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.HomeFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MissionFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.TacticalSituationFragment;

import static jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity.Fragment.HOME;

public class MainActivity extends AppCompatActivity implements DockFragment.OnFragmentInteractionListener, ConfigFragment.OnFragmentInteractionListener, ErrorFragment.OnFragmentInteractionListener, MissionFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, DeckManagerFragment.OnFragmentInteractionListener, TacticalSituationFragment.OnFragmentInteractionListener, DeckFragment.OnFragmentInteractionListener {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private String[] mDrawerItemTitles;
    private ListView mDrawerList;
    //画面回転時のfragmentの更新に使用
    private Fragment present;

    /**
     * このアクティビティが持つfragment
     */
    public enum Fragment {
        NULL(0),
        HOME(1),
        DECK(2),
        MISSION(3),
        DOCK(4),
        DAMAGED_SHIP(5),
        EQUIPMENT(6),
        TACTICAL_SITUATION(7),
        CONFIG(8);

        private int position;

        Fragment(int id) {
            this.position = id;
        }

        private static final Map<Integer, Fragment> toMainFragmentMap = new HashMap<>();

        static {
            for (Fragment entry : values()) {
                toMainFragmentMap.put(entry.position, entry);
            }
        }

        public static Fragment toMainFragment(int id) {
            return toMainFragmentMap.get(id);
        }

        public int getPosition() {
            return position;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set DrawerToggle.
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_open);

        // リスナー登録
        mDrawer.setDrawerListener(mDrawerToggle);

        // Drawerの矢印表示有り
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // initialize drawer list.
        mDrawerItemTitles = getResources().getStringArray(R.array.title);
        mDrawerList = (ListView) findViewById(R.id.slide_menu);

        //ヘッダー
        mDrawerList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.drawer_header, null));

        // Set the adapter for list view.
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerItemTitles));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        Intent intent = getIntent();

        Log.d("MainActivity", "onCreate");

        //画面の向き
        boolean usesLandscape = intent.getBooleanExtra("usesLandscape", false);
        if (usesLandscape) {
            Log.d("MainActivity", "横");
            intent.putExtra("usesLandscape", false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        //表示するfragment
        Fragment mf = Fragment.toMainFragment(intent.getIntExtra("position", Fragment.NULL.getPosition()));
        if (mf != Fragment.NULL) {
            intent.putExtra("position", Fragment.NULL.getPosition());
            selectItem(mf);
            return;
        }
        if (savedInstanceState == null) {
            //画面回転を自動に設定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            // デフォルトはHomeFragment
            selectItem(HOME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onStop");
        /*
        if (present != Fragment.TACTICAL_SITUATION) {
            //画面回転を自動に設定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        */
        //現在の画面の向きに応じて画面の向きを設定
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        switch (config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d("MainActivity", "縦");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                Log.d("MainActivity", "横");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
        /*
        if (present != Fragment.TACTICAL_SITUATION) {
            //画面回転を自動に設定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        */
        //現在の画面の向きに応じて画面の向きを設定
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        switch (config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d("MainActivity", "縦");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                Log.d("MainActivity", "横");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    //画面回転時に呼ばれる
    //ここでfragmentを画面に合ったものに更新する
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
        selectItem(present);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Toolbarを使わない時と同じ
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(Fragment.toMainFragment(position));
        }
    }

    //DrawerListのpositionに該当するfragmentを表示
    private void selectItem(Fragment mf) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment;

        if (mf == null) {
            mf = HOME;
        }

        switch (mf) {
            case HOME:
                fragment = HomeFragment.newInstance();
                break;
            case DECK:
                fragment = DeckManagerFragment.newInstance();
                break;
            case MISSION:
                fragment = MissionFragment.newInstance();
                break;
            case DOCK:
                fragment = DockFragment.newInstance();
                break;
            case CONFIG:
                fragment = ConfigFragment.newInstance();
                break;
            case TACTICAL_SITUATION:
                fragment = TacticalSituationFragment.newInstance();
                /*
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.body, fragment);
                transaction.commitAllowingStateLoss();
                //ツールバーのタイトルを更新
                getSupportActionBar().setTitle("戦況");
                //現在のDrawerListの位置を記録
                present = mf;
                return;
                */
                break;
            case DAMAGED_SHIP:
                fragment = DamagedShipFragment.newInstance();
                break;
            case EQUIPMENT:
                fragment = EquipmentFragment.newInstance();
                break;
            default:
                return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.body, fragment);
        //transaction.commit();
        transaction.commitAllowingStateLoss();

        //選択されたDrawerListの位置ををハイライト
        mDrawerList.setItemChecked(mf.getPosition(), true);

        //ツールバーのタイトルを更新
        getSupportActionBar().setTitle(mDrawerItemTitles[mf.getPosition() - 1]);

        //Drawerを閉じる
        mDrawer.closeDrawer(mDrawerList);

        //現在のDrawerListの位置を記録
        present = mf;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("MainActivity", "onNewIntent");
        //画面の向き
        boolean usesLandscape = intent.getBooleanExtra("usesLandscape", false);
        if (usesLandscape) {
            Log.d("MainActivity", "横");
            intent.putExtra("usesLandscape", false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        //表示するfragment
        Fragment mf = Fragment.toMainFragment(intent.getIntExtra("position", Fragment.NULL.getPosition()));
        if (mf != Fragment.NULL) {
            Log.d("MainActivity", "fragment");
            intent.putExtra("position", Fragment.NULL.getPosition());
            present = mf;
        }
    }
}
