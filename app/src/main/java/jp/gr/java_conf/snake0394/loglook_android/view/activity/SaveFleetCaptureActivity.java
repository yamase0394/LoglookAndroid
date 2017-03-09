package jp.gr.java_conf.snake0394.loglook_android.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;

public class SaveFleetCaptureActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private String filename;

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.edit_name)
    EditText nameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_fleet_capture);
        ButterKnife.bind(this);

        this.bitmap = (Bitmap) getIntent().getExtras()
                                          .get("bitmap");
        imageView.setImageBitmap(bitmap);

        Date mDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        this.filename = simpleDateFormat.format(mDate) + ".jpg";
        nameEdit.setText(filename);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.button_save)
    void save() {
        try {
            File sdcard_path = new File(Environment.getExternalStorageDirectory()
                                                   .getPath() + "/泥提督支援アプリ/capture/list");

            if (!sdcard_path.exists()) {
                sdcard_path.mkdirs();
            }


            FileOutputStream fos = new FileOutputStream(new File(sdcard_path, this.filename));

            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();

            Toast.makeText(getApplicationContext(), "保存完了", Toast.LENGTH_SHORT)
                 .show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "保存失敗。エラーログを記録しました。", Toast.LENGTH_SHORT)
                 .show();
            ErrorLogger.writeLog(e);
        }
        finish();
    }

    @OnClick(R.id.button_cancel)
    void cancel() {
        finish();
    }
}
