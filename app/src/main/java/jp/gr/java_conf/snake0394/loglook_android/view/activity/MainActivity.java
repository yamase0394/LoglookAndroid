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
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefs;
import jp.gr.java_conf.snake0394.loglook_android.storage.GeneralPrefsSpotRepository;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.ConfigFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DamagedShipFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DeckCaptureListFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DeckFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.DockFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.EquipmentFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.HomeFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MissionFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.MyShipListFragment;
import jp.gr.java_conf.snake0394.loglook_android.view.fragment.TacticalSituationFragment;

import static jp.gr.java_conf.snake0394.loglook_android.R.id.toolbar;
import static jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity.Screen.HOME;
import static jp.gr.java_conf.snake0394.loglook_android.view.activity.MainActivity.Screen.drawerItemTitles;

public class MainActivity extends AppCompatActivity {
    
    
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ListView leftDrawerListView;
    //画面回転時のfragmentの更新に使用
    private Screen present;
    private boolean forcesLandscape;
    
    /**
     * このアクティビティが持つfragment
     */
    public enum Screen {
        HOME(0, "ホーム"), DECK(1, "艦隊"), MISSION(2, "遠征"), DOCK(3, "入渠"), DAMAGED_SHIP(4, "損傷艦"), MY_SHIP_LIST(5, "艦娘一覧"), EQUIPMENT(6, "装備一覧"), TACTICAL_SITUATION(7, "戦況"), DECK_CAPTURE_LIST(8, "編成一覧"), CONFIG(9, "設定");
        
        private int position;
        private String name;
        
        Screen(int id, String name) {
            this.position = id;
            this.name = name;
        }
        
        static final String[] drawerItemTitles = new String[Screen.values().length];
        
        static {
            for (Screen screen : values()) {
                drawerItemTitles[screen.getPosition()] = screen.name;
            }
        }
        
        private static final SparseArray<Screen> positionToScreenSparseArray = new SparseArray<>();
        
        static {
            for (Screen entry : values()) {
                positionToScreenSparseArray.put(entry.position, entry);
            }
        }
        
        private static final Map<String, Screen> nameToScreenMap = new HashMap<>();
        
        static {
            for (Screen entry : values()) {
                nameToScreenMap.put(entry.name, entry);
            }
        }
        
        public static Screen toScreen(int id) {
            return positionToScreenSparseArray.get(id);
        }
        
        public static Screen toScreen(String name) {
            return nameToScreenMap.get(name);
        }
        
        public int getPosition() {
            return position;
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        setContentView(R.layout.activity_main);
        
        if (!canGetUsageStats(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        
        //Android6以降の端末でランチャーのオーバーレイ用の権限を取得する
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaProjectionManager mMediaProjectionManager;
            mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            
            // MediaProjectionの利用にはパーミッションが必要。
            // ユーザーへの問い合わせのため、Intentを発行
            Intent permissionIntent = mMediaProjectionManager.createScreenCaptureIntent();
            startActivity(permissionIntent);
        }
        
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Set DrawerToggle.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_open);
        
        // リスナー登録
        drawerLayout.addDrawerListener(drawerToggle);
        
        // Drawerの矢印表示有り
        drawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        leftDrawerListView = (ListView) findViewById(R.id.slide_menu);
        
        // Set the adapter for list view.
        leftDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerItemTitles));
        // Set the list's click listener
        leftDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
        
        Intent intent = getIntent();
        
        Logger.d("MainActivity", "onCreate");
        
        GeneralPrefs prefs = GeneralPrefsSpotRepository.getEntity(getApplicationContext());
        this.forcesLandscape = prefs.forcesLandscape;
        
        if (this.forcesLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            //画面の向き
            boolean usesLandscape = intent.getBooleanExtra("usesLandscape", false);
            if (usesLandscape) {
                Logger.d("MainActivity", "横");
                intent.putExtra("usesLandscape", false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        
        //表示するfragment
        int position = intent.getIntExtra("position", -1);
        if (position != -1) {
            Screen mf = Screen.toScreen(position);
            Logger.d("MainActivity:onCreate", mf.name + " was selected");
            present = mf;
            selectItem(mf);
            return;
        }
        
        if (savedInstanceState == null) {
            Logger.d("MainActivity:onCreate", "savedInstanceState == null");
            
            if (this.forcesLandscape) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                //画面回転を自動に設定
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            // デフォルトはHomeFragment
            if (present == null) {
                Logger.d("MainActivity:onCreate", "present = null");
                selectItem(HOME);
            } else {
                Logger.d("MainActivity:onCreate", present.name + " was selected");
                selectItem(present);
            }
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        if (sp.getBoolean("isConfigExported", false)) {
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
        try {
            prefs.port = Integer.parseInt(sp.getString("port", "8000"));
        } catch (NumberFormatException e) {
            prefs.port = 8000;
        }
        prefs.showsView = sp.getBoolean("showView", true);
        prefs.viewX = sp.getInt("viewX", point.x / -2);
        prefs.viewY = sp.getInt("viewY", point.y / -2);
        prefs.viewWidth = sp.getInt("viewWidth", 20);
        prefs.viewHeight = sp.getInt("viewHeight", 50);
        prefs.vibratesWhenViewTouched = sp.getBoolean("touchVibration", true);
        prefs.usesProxy = sp.getBoolean("useProxy", false);
        prefs.proxyHost = sp.getString("proxyHost", "localhost");
        try {
            prefs.proxyPort = Integer.parseInt(sp.getString("proxyPort", "8080"));
        } catch (NumberFormatException e) {
            prefs.proxyPort = 8080;
        }
        prefs.logsJson = sp.getBoolean("saveJson", false);
        prefs.logsRequest = sp.getBoolean("saveRequest", false);
        GeneralPrefsSpotRepository.putEntity(getApplicationContext(), prefs);
        
        File dataFile = new File(Environment.getExternalStorageDirectory()
                                            .getPath() + "/泥提督支援アプリ/data");
        if (dataFile.exists()) {
            File[] files = dataFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            dataFile.delete();
        }
        
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isConfigExported", true);
        editor.apply();
        
        
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("MainActivity", "onStop");
        /*
        if (present != Fragment.TACTICAL_SITUATION) {
            //画面回転を自動に設定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        */
        //現在の画面の向きに応じて画面の向きを設定
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (this.forcesLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            switch (config.orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    Logger.d("MainActivity", "縦");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    Logger.d("MainActivity", "横");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
            }
        }
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    
    @Override
    //画面回転時に呼ばれる
    //ここでfragmentを画面に合ったものに更新する
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
        if (item.getItemId() == R.id.open_kancolle) {
            String packageName = "com.dmm.dmmlabo.kancolle";
            PackageManager pm = getPackageManager();
            Intent sendIntent = pm.getLaunchIntentForPackage(packageName);
            startActivity(sendIntent);
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
            finish();
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(Screen.toScreen(position));
        }
    }
    
    //DrawerListのpositionに該当するfragmentを表示
    private void selectItem(Screen mf) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment;
        
        if (mf == null) {
            mf = HOME;
        }
        
        Logger.d("MainActivity:selectItem", mf.name + " was selected");
        
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(true, true);
        
        ((AppBarLayout.LayoutParams) findViewById(toolbar).getLayoutParams()).setScrollFlags(0);
        
        switch (mf) {
            case HOME:
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
            case DECK_CAPTURE_LIST:
                fragment = DeckCaptureListFragment.newInstance();
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
        leftDrawerListView.setItemChecked(mf.getPosition(), true);
        
        //ツールバーのタイトルを更新
        getSupportActionBar().setTitle(mf.name);
        
        //Drawerを閉じる
        drawerLayout.closeDrawer(leftDrawerListView);
        
        //現在のDrawerListの位置を記録
        present = mf;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        Logger.d("MainActivity", "onNewIntent");
        //画面の向き
        if (this.forcesLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            boolean usesLandscape = intent.getBooleanExtra("usesLandscape", false);
            if (usesLandscape) {
                Logger.d("onNewIntent", "横");
                intent.putExtra("usesLandscape", false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        
        //表示するfragment
        int position = intent.getIntExtra("position", -1);
        if (position != -1) {
            Screen mf = Screen.toScreen(position);
            Logger.d("MainActivity:onCreate", mf.name + " was selected");
            present = mf;
        }
    }
    
    public static boolean canGetUsageStats(Context context) {
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
}
