package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefsSpotRepository;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.ConfigFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DamagedShipFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DeckFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DockFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.EquipmentFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.HomeFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MissionFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.TacticalSituationFragment;

import static jp.gr.java_conf.snake0394.loglook_android.R.id.toolbar;
import static jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity.Fragment.HOME;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private String[] mDrawerItemTitles;
    private ListView mDrawerList;
    //画面回転時のfragmentの更新に使用
    private Fragment present;

    private final int OVERLAY_REQ_CODE = 1234;
    private final int USAGE_ACCESS_REQ_CODE = 2222;


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
        MY_SHIP_LIST(6),
        EQUIPMENT(7),
        TACTICAL_SITUATION(8),
        CONFIG(9);

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

        if (!canGetUsageStats()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, USAGE_ACCESS_REQ_CODE);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("UsageAccessPermissionGranted", false);
            editor.apply();
        }

        //Android6以降の端末でランチャーのオーバーレイ用の権限を取得する
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_REQ_CODE);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("SystemAlertPermissionGranted", false);
            editor.apply();
        }

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
        mDrawerList.addHeaderView(LayoutInflater.from(this)
                                                .inflate(R.layout.drawer_header, null));

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
            present = mf;
            selectItem(mf);
            return;
        }

        if (savedInstanceState == null) {
            //画面回転を自動に設定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            // デフォルトはHomeFragment
            if (present == null) {
                selectItem(HOME);
            } else {
                selectItem(present);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(sp.getBoolean("isConfigExported", false)){
            return;
        }

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point(0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Android 4.2以上
            display.getRealSize(point);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // Android 3.2以上
            try {
                Method getRawWidth = Display.class.getMethod("getRawWidth");
                Method getRawHeight = Display.class.getMethod("getRawHeight");
                int width = (Integer) getRawWidth.invoke(display);
                int height = (Integer) getRawHeight.invoke(display);
                point.set(width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GeneralPrefs prefs = GeneralPrefsSpotRepository.getEntity(getApplicationContext());
        prefs.port = Integer.parseInt(sp.getString("port", "8000"));
        prefs.showsView = sp.getBoolean("showView", true);
        prefs.viewX = sp.getInt("viewX", point.x / -2);
        prefs.viewY = sp.getInt("viewY", point.y / -2);
        prefs.viewWidth = sp.getInt("viewWidth", 20);
        prefs.viewHeight = sp.getInt("viewHeight", 50);
        prefs.vibratesWhenViewTouched = sp.getBoolean("touchVibration", true);
        prefs.usesProxy = sp.getBoolean("useProxy", false);
        prefs.proxyHost = sp.getString("proxyHost", "localhost");
        prefs.proxyPort = Integer.parseInt(sp.getString("proxyPort", "8080"));
        prefs.logsJson = sp.getBoolean("saveJson", false);
        prefs.logsRequest = sp.getBoolean("saveRequest", false);
        GeneralPrefsSpotRepository.putEntity(getApplicationContext(), prefs);

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isConfigExported", true);
        editor.apply();
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

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(true, true);

        switch (mf) {
            case HOME:
                ((AppBarLayout.LayoutParams) findViewById(toolbar).getLayoutParams()).setScrollFlags(0);
                fragment = HomeFragment.newInstance();
                break;
            case DECK:
                fragment = DeckFragment.newInstance();
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
                break;
            case DAMAGED_SHIP:
                ((AppBarLayout.LayoutParams) findViewById(toolbar).getLayoutParams()).setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                fragment = DamagedShipFragment.newInstance();
                break;
            case EQUIPMENT:
                ((AppBarLayout.LayoutParams) findViewById(toolbar).getLayoutParams()).setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                fragment = EquipmentFragment.newInstance();
                break;
            case MY_SHIP_LIST:
                ((AppBarLayout.LayoutParams) findViewById(toolbar).getLayoutParams()).setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                fragment = MyShipListFragment.newInstance();
                break;
            default:
                return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.body, fragment);
        //transaction.commit();
        //Il
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
    protected void onNewIntent(Intent intent) {
        Log.d("MainActivity", "onNewIntent");
        //画面の向き
        boolean usesLandscape = intent.getBooleanExtra("usesLandscape", false);
        if (usesLandscape) {
            Log.d("onNewIntent", "横");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                //権限が得られなかった
            } else {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("SystemAlertPermissionGranted", true);
                editor.apply();
            }
        } else if (requestCode == USAGE_ACCESS_REQ_CODE) {
            if (checkPermission(getApplicationContext())) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("UsageAccessPermissionGranted", true);
                editor.apply();
            } else {

            }
        }
    }

    private static boolean checkPermission(Context context) {
        // Lollipop以前は使えないAPIが含まれています。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        // AppOpsManagerを取得
        AppOpsManager aom = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        // GET_USAGE_STATSのステータスを取得
        int mode = aom.checkOp(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            // AppOpsの状態がデフォルトなら通常のpermissionチェックを行う。
            // 普通のアプリならfalse
            return context.checkPermission("android.permission.PACKAGE_USAGE_STATS", android.os.Process.myPid(), android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED;
        }
        // AppOpsの状態がデフォルトでないならallowedのみtrue
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private boolean canGetUsageStats() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        AppOpsManager aom = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
        int uid = android.os.Process.myUid();
        int mode = aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, uid, getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
