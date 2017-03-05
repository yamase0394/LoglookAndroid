package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShip;
import jp.gr.java_conf.snake0394.loglook_android.bean.MyShipManager;

/**
 * 大破進撃を警告する画面です。
 */
public class HeavilyDamagedWarningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heavily_damaged_warning);

        Intent intent = getIntent();

        StringBuffer sb = new StringBuffer();
        List<Integer> shipId = intent.getIntegerArrayListExtra("shipId");
        for (int i = 0; i < shipId.size(); i++) {
            MyShip ms = MyShipManager.INSTANCE.getMyShip(shipId.get(i));
            sb.append(ms.getName());
            if (i == shipId.size() - 1) {
                break;
            }
            sb.append(", ");
        }

        TextView text = (TextView) findViewById(R.id.textView2);
        text.setText(sb.toString());


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                finish();
                String packageName = "com.dmm.dmmlabo.kancolle";
                PackageManager pm = HeavilyDamagedWarningActivity.this.getPackageManager();
                Intent sendIntent = pm.getLaunchIntentForPackage(packageName);
                HeavilyDamagedWarningActivity.this.startActivity(sendIntent);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                Intent sendIntent = new Intent(getBaseContext(), MainActivity.class);
                sendIntent.putExtra("usesLandscape", true);
                sendIntent.putExtra("position", MainActivity.Screens.DECK.getPosition());
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
