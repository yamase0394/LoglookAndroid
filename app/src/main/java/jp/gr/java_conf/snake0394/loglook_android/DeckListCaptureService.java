package jp.gr.java_conf.snake0394.loglook_android;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.logger.ErrorLogger;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;


/**
 * アプリを起動するランチャー
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DeckListCaptureService extends Service {

    private static final String TAG = "DeckListCaptureService";

    private final Handler handler = new Handler();

    private WindowManager.LayoutParams params;

    private int displayWidth;
    private int displayHeight;

    private Bitmap listBitmap;
    private List<Bitmap> bitmapList;
    private List<String> listNameList;
    private int shipNum;
    private int shipDataWidth;

    private LinearLayout linearLayout;
    private ImageView preview;
    private Spinner fleetNameSpinner;
    private CheckBox usesFleetDividerCheck;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.d("DeckListCaptureService", "onCreate");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            stopSelf();
            return;
        }

        setTheme(R.style.AppTheme);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        OverlayService.getDefaultDisplay()
                .getMetrics(metrics);
        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;

        bitmapList = new ArrayList<>();
        listNameList = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        if (startId > 1) {
            Logger.d(TAG, "flags=" + flags + ", startId=" + startId);
            Logger.d(TAG, "multi running is not permitted");
            return START_STICKY;
        }

        //タッチイベントを取得するためのviewを作る
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.layout_capture_screen, null);

        params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, -(displayWidth / 2), displayHeight / 2, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        ImageButton captureButton = ButterKnife.findById(linearLayout, R.id.button_cap);
        captureButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                try {
                    OverlayService.hideAllOverlayView();

                    Bitmap bitmap = ScreenCaptureService.getScreenshot();

                    OverlayService.showAllOverlayView();

                    if (bitmap == null) {
                        return true;
                    }

                    Bitmap screenShot = removeBlank(bitmap);

                    Bitmap shipData = trimShipDataArea(screenShot);
                    shipDataWidth = shipData.getWidth();

                    arrangeShipData(shipData);

                    preview.setImageBitmap(listBitmap);

                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "スクリーンショット失敗。エラーログを記録しました。", Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                    ErrorLogger.writeLog(e);
                    e.printStackTrace();
                    stopSelf();
                }

                return true;
            }
        });

        ImageButton saveButton = ButterKnife.findById(linearLayout, R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listBitmap == null) {
                    if (bitmapList.isEmpty()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "画像が空です", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        return;
                    }
                } else {
                    bitmapList.add(listBitmap);
                    listNameList.add((String) fleetNameSpinner.getSelectedItem());
                }

                for (int i = 0; i < bitmapList.size(); i++) {
                    String txt = listNameList.get(i);
                    int color = Color.BLACK;
                    switch (txt) {
                        case "なし":
                            continue;
                        case "第一艦隊":
                            color = Color.rgb(255, 0, 0);
                            break;
                        case "第二艦隊":
                            color = Color.rgb(0, 0, 255);
                            break;
                        case "ボス支援":
                            color = Color.rgb(32, 172, 76);
                            break;
                        case "道中支援":
                            color = Color.rgb(224, 96, 32);
                            break;
                    }

                    Bitmap base = bitmapList.get(i);

                    //文字の縁
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    // 描画の幅
                    paint.setStrokeWidth(6);
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(45);
                    paint.setStyle(Paint.Style.STROKE);
                    //文字本隊
                    Paint w_paint = new Paint();
                    w_paint.setAntiAlias(true);
                    w_paint.setColor(color);
                    w_paint.setTextSize(45);
                    w_paint.setStrokeWidth(0);
                    w_paint.setStyle(Paint.Style.FILL);
                    w_paint.getTextBounds(txt, 0, txt.length(), new Rect());
                    Paint.FontMetrics fm = w_paint.getFontMetrics();//フォントマトリックス
                    int mtw = (int) w_paint.measureText(txt);//幅
                    int fmHeight = (int) (Math.abs(fm.top) + fm.bottom);//高さ

                    Bitmap txtBitmap = Bitmap.createBitmap(mtw + 1 * 2, fmHeight + 1 * 2, Bitmap.Config.ARGB_8888);
                    Canvas cv = new Canvas(txtBitmap);
                    cv.drawText(txt, 1, Math.abs(fm.ascent) + 1, paint);
                    cv.drawText(txt, 1, Math.abs(fm.ascent) + 1, w_paint);

                    Bitmap containTxt = Bitmap.createBitmap(base.getWidth(), base.getHeight(), Bitmap.Config.ARGB_8888);
                    cv = new Canvas(containTxt);
                    cv.drawBitmap(base, 0, 0, null);
                    cv.drawBitmap(txtBitmap, shipDataWidth / 2, 0, null);

                    bitmapList.set(i, containTxt);
                }

                listBitmap = bitmapList.get(0);

                for (int i = 1; i < bitmapList.size(); i++) {
                    Bitmap bitmap = bitmapList.get(i);
                    int newwidth = shipDataWidth * 2 * (i + 1);
                    int newheight = Math.max(listBitmap.getHeight(), bitmap.getHeight());
                    Bitmap newList = Bitmap.createBitmap(newwidth, newheight, Bitmap.Config.ARGB_8888);

                    Canvas offScreen = new Canvas(newList);
                    offScreen.drawBitmap(listBitmap, 0, 0, null);
                    offScreen.drawBitmap(bitmap, newwidth - 2 * shipDataWidth, 0, null);

                    if (usesFleetDividerCheck.isChecked()) {
                        Paint paint = new Paint();
                        paint.setColor(Color.rgb(32, 32, 192));
                        paint.setStyle(Paint.Style.FILL);
                        paint.setAntiAlias(true);
                        offScreen.drawRect((newwidth - shipDataWidth * 2) - (shipDataWidth / 200), 0, (newwidth - shipDataWidth * 2) + (shipDataWidth / 200), newheight, paint);
                    }

                    listBitmap = newList;
                }

                try {
                    File sdcard_path = new File(Environment.getExternalStorageDirectory()
                            .getPath() + "/泥提督支援アプリ/capture/list");

                    if (!sdcard_path.exists()) {
                        sdcard_path.mkdirs();
                    }

                    Date mDate = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    File file = new File(sdcard_path, "攻略編成_" + simpleDateFormat.format(mDate) + ".jpg");
                    FileOutputStream fos = new FileOutputStream(file);

                    listBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    fos.close();

                    stopSelf();

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(file), "image/jpg");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "保存失敗。エラーログを記録しました。", Toast.LENGTH_SHORT)
                            .show();
                    ErrorLogger.writeLog(e);
                }
            }
        });

        ImageButton closeButton = ButterKnife.findById(linearLayout, R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });

        ImageButton nextDeckButton = ButterKnife.findById(linearLayout, R.id.button_next);
        nextDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listBitmap == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "画像が空です", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    return;
                }
                shipNum = 0;
                bitmapList.add(listBitmap);
                listNameList.add((String) fleetNameSpinner.getSelectedItem());
                listBitmap = null;
                preview.setImageResource(android.R.color.transparent);
            }
        });

        preview = ButterKnife.findById(linearLayout, R.id.imageView);

        usesFleetDividerCheck = ButterKnife.findById(linearLayout, R.id.check_uses_fleet_divider);

        fleetNameSpinner = ButterKnife.findById(linearLayout, R.id.spinner_fleet_name);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item);
        arrayAdapter.addAll(new String[]{"なし", "第一艦隊", "第二艦隊", "ボス支援", "道中支援"});
        arrayAdapter.setDropDownViewResource(R.layout.spinner_drop_item);
        fleetNameSpinner.setAdapter(arrayAdapter);
        fleetNameSpinner.setSelection(0);

        ImageView dragHandle = ButterKnife.findById(linearLayout, R.id.view_drag_handle);
        dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getRawX();
                int y = (int) motionEvent.getRawY();
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    int centerX = x - (displayWidth / 2);
                    int centerY = y - (displayHeight / 2);

                    params.x = centerX + linearLayout.getWidth() / 2 - 10;
                    params.y = centerY + linearLayout.getHeight() / 2 - 10;

                    OverlayService.updateOverlayViewLayout(linearLayout, params);
                }
                return false;
            }
        });

        OverlayService.addOverlayView(linearLayout, params);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OverlayService.removeOverlayView(linearLayout);
        Logger.d("ScrennCaptureService", "onDestroy");
    }

    private Bitmap removeBlank(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int kcsWidth = height * 5 / 3;
        if (kcsWidth < displayWidth) {
            int blackWidth = (displayWidth - kcsWidth) / 2;
            int[] newPixcels = new int[kcsWidth * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[x + y * width];
                    if (x < blackWidth || x > kcsWidth + blackWidth - 1) {
                        continue;
                    }
                    newPixcels[(x - blackWidth) + y * kcsWidth] = pixel;
                }
            }
            return Bitmap.createBitmap(newPixcels, kcsWidth, displayHeight, Bitmap.Config.ARGB_8888);
        } else if (kcsWidth == displayWidth) {
            return bitmap;
        } else {
            //Logger.d("kcsWidth", String.valueOf(kcsWidth));
            int kcsHeight = displayWidth * 3 / 5;
            //Logger.d("kcsHeight", String.valueOf(kcsHeight));
            int blackHeight = (displayHeight - kcsHeight) / 2;
            //Logger.d("blankHeight", String.valueOf(blackHeight));
            int[] newPixcels = new int[displayWidth * kcsHeight];
            for (int y = blackHeight; y < kcsHeight + blackHeight; y++) {
                for (int x = 0; x < displayWidth; x++) {
                    int pixel = pixels[x + y * width];
                    newPixcels[x + (y - blackHeight) * displayWidth] = pixel;
                }
            }

            return Bitmap.createBitmap(newPixcels, displayWidth, kcsHeight, Bitmap.Config.ARGB_8888);
        }
    }

    private Bitmap trimShipDataArea(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        // Bitmap から Pixel を取得
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int unnecessaryWidth = width - 470 * height / 480;
        int unnecessaryHeight = height - 378 * height / 480;
        int[] newPixcels = new int[(width - unnecessaryWidth - 20) * (height - unnecessaryHeight - 20)];
        for (int y = 0; y < height; y++) {
            if (y < unnecessaryHeight || y > height - 20 - 1) {
                continue;
            }
            for (int x = 0; x < width; x++) {
                if (x < unnecessaryWidth || x > width - 20 - 1) {
                    continue;
                }
                int pixel = pixels[x + y * width];
                newPixcels[(x - unnecessaryWidth) + (y - unnecessaryHeight) * (width - unnecessaryWidth - 20)] = pixel;
            }
        }

        return Bitmap.createBitmap(newPixcels, width - unnecessaryWidth - 20, height - unnecessaryHeight - 20, Bitmap.Config.ARGB_8888);
    }

    private void arrangeShipData(Bitmap bitmap) {
        shipNum++;
        if (listBitmap == null) {
            listBitmap = bitmap;
            return;
        }

        Bitmap newDeck;
        if (shipNum == 2) {
            int newwidth = bitmap.getWidth() + listBitmap.getWidth();
            int newheight = listBitmap.getHeight();
            newDeck = Bitmap.createBitmap(newwidth, newheight, Bitmap.Config.ARGB_8888);

            Canvas offScreen = new Canvas(newDeck);
            offScreen.drawBitmap(listBitmap, 0, 0, null);
            offScreen.drawBitmap(bitmap, listBitmap.getWidth(), listBitmap.getHeight() - bitmap.getHeight(), null);

        } else if (shipNum % 2 == 0) {
            int newwidth = listBitmap.getWidth();
            int newheight = listBitmap.getHeight();

            newDeck = Bitmap.createBitmap(newwidth, newheight, Bitmap.Config.ARGB_8888);

            Canvas offScreen = new Canvas(newDeck);
            offScreen.drawBitmap(listBitmap, 0, 0, null);
            offScreen.drawBitmap(bitmap, listBitmap.getWidth() - bitmap.getWidth(), listBitmap.getHeight() - bitmap.getHeight(), null);

        } else {
            int newwidth = listBitmap.getWidth();
            int newheight = listBitmap.getHeight() + bitmap.getHeight();

            newDeck = Bitmap.createBitmap(newwidth, newheight, Bitmap.Config.ARGB_8888);

            Canvas offScreen = new Canvas(newDeck);
            offScreen.drawBitmap(listBitmap, 0, 0, null);
            offScreen.drawBitmap(bitmap, listBitmap.getWidth() - 2 * bitmap.getWidth(), listBitmap.getHeight(), null);
        }
        listBitmap = newDeck;
    }
}
