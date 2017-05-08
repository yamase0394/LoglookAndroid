package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import jp.gr.java_conf.snake0394.loglook_android.R;

public class MaterialChartFragment extends Fragment implements MaterialChartFilterDialogFragment.OnCheckFinishedListener {
    
    private List<String> dateList;
    private List<Integer> fuel;
    private List<Integer> bullet;
    private List<Integer> steel;
    private List<Integer> bauxite;
    private List<Integer> bucket;
    private List<Integer> instantConstruction;
    private List<Integer> developmentMaterial;
    private List<Integer> improvementMaterial;
    
    private LineChart chart;
    
    public MaterialChartFragment() {
        // Required empty public constructor
    }
    
    public static MaterialChartFragment newInstance() {
        MaterialChartFragment fragment = new MaterialChartFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dateList = new ArrayList<>();
        fuel = new ArrayList<>();
        bullet = new ArrayList<>();
        steel = new ArrayList<>();
        bauxite = new ArrayList<>();
        bucket = new ArrayList<>();
        instantConstruction = new ArrayList<>();
        developmentMaterial = new ArrayList<>();
        improvementMaterial = new ArrayList<>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        File logFile = new File(Environment.getExternalStorageDirectory()
                                           .getPath() + "/泥提督支援アプリ/資材ログ.csv");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            //列名を読み飛ばす
            String line = reader.readLine();
            String endLine = line;
            Date preDate = null;
            while ((line = reader.readLine()) != null) {
                endLine = line;
                String[] values = line.split(",");
                Date date = sdf.parse(values[0]);
                //その日の最初のデータのみを使用する
                try {
                    if (DateUtils.isSameDay(preDate, date)) {
                        continue;
                    }
                } catch (IllegalArgumentException e) {
                    //preDateがnull
                }
                preDate = date;
                
                dateList.add(sdf.format(date));
                fuel.add(Integer.parseInt(values[1]));
                bullet.add(Integer.parseInt(values[2]));
                steel.add(Integer.parseInt(values[3]));
                bauxite.add(Integer.parseInt(values[4]));
                bucket.add(Integer.parseInt(values[5]));
                instantConstruction.add(Integer.parseInt(values[6]));
                developmentMaterial.add(Integer.parseInt(values[7]));
                try {
                    improvementMaterial.add(Integer.parseInt(values[8]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    improvementMaterial.add(0);
                }
            }
            
            //最後のデータは最新のものにする
            String[] values = endLine.split(",");
            Date date = sdf.parse(values[0]);
            try {
                if (DateUtils.isSameDay(preDate, date)) {
                    dateList.set(dateList.size() - 1, sdf.format(date));
                    fuel.set(dateList.size() - 1, Integer.parseInt(values[1]));
                    bullet.set(dateList.size() - 1, Integer.parseInt(values[2]));
                    steel.set(dateList.size() - 1, Integer.parseInt(values[3]));
                    bauxite.set(dateList.size() - 1, Integer.parseInt(values[4]));
                    bucket.set(dateList.size() - 1, Integer.parseInt(values[5]));
                    instantConstruction.set(dateList.size() - 1, Integer.parseInt(values[6]));
                    developmentMaterial.set(dateList.size() - 1, Integer.parseInt(values[7]));
                    try {
                        improvementMaterial.set(dateList.size() - 1, Integer.parseInt(values[8]));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        improvementMaterial.set(dateList.size() - 1, 0);
                    }
                } else {
                    dateList.add(sdf.format(date));
                    fuel.add(Integer.parseInt(values[1]));
                    bullet.add(Integer.parseInt(values[2]));
                    steel.add(Integer.parseInt(values[3]));
                    bauxite.add(Integer.parseInt(values[4]));
                    bucket.add(Integer.parseInt(values[5]));
                    instantConstruction.add(Integer.parseInt(values[6]));
                    developmentMaterial.add(Integer.parseInt(values[7]));
                    try {
                        improvementMaterial.add(Integer.parseInt(values[8]));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        improvementMaterial.add(0);
                    }
                }
            } catch (IllegalArgumentException e) {
                //preDateがnull
                dateList.add(sdf.format(date));
                fuel.add(Integer.parseInt(values[1]));
                bullet.add(Integer.parseInt(values[2]));
                steel.add(Integer.parseInt(values[3]));
                bauxite.add(Integer.parseInt(values[4]));
                bucket.add(Integer.parseInt(values[5]));
                instantConstruction.add(Integer.parseInt(values[6]));
                developmentMaterial.add(Integer.parseInt(values[7]));
                try {
                    improvementMaterial.add(Integer.parseInt(values[8]));
                } catch (ArrayIndexOutOfBoundsException ae) {
                    improvementMaterial.add(0);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material_chart, container, false);
        
        Toolbar toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_material_chart_fragment);
        toolbar.getMenu()
               .findItem(R.id.menu_filter)
               .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem menuItem) {
                       DialogFragment dialogFragment = MaterialChartFilterDialogFragment.newInstance();
                       dialogFragment.setTargetFragment(MaterialChartFragment.this, 0);
                       dialogFragment.show(getFragmentManager(), "filtering dialog");
                       return false;
                   }
               });
        
        chart = ButterKnife.findById(rootView, R.id.line_chart);
        
        List<LineDataSet> dataSets = new ArrayList<>();
        
        //チャートにデータを追加
        //燃料
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(fuel.get(i), i));
        }
        LineDataSet dataSet = new LineDataSet(entries, "燃料");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.fuel));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        //弾薬
        entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(bullet.get(i), i));
        }
        dataSet = new LineDataSet(entries, "弾薬");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.bullet));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        //鋼材
        entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(steel.get(i), i));
        }
        dataSet = new LineDataSet(entries, "鋼材");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.steel));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        //ボーキサイト
        entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(bauxite.get(i), i));
        }
        dataSet = new LineDataSet(entries, "ボーキサイト");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.bauxite));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        //高速修復材
        entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(bucket.get(i), i));
        }
        dataSet = new LineDataSet(entries, "高速修復材");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.bucket));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        //高速建造剤
        entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(instantConstruction.get(i), i));
        }
        dataSet = new LineDataSet(entries, "高速建造剤");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.instant_construction));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        //開発資材
        entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(developmentMaterial.get(i), i));
        }
        dataSet = new LineDataSet(entries, "開発資材");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.development_material));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        //改修資材
        entries = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            entries.add(new Entry(improvementMaterial.get(i), i));
        }
        dataSet = new LineDataSet(entries, "改修資材");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.improvement_material));
        dataSet.setDrawCircles(false);
        dataSets.add(dataSet);
        
        LineData data = new LineData(dateList, dataSets);
        data.setHighlightEnabled(false);
        
        chart.setData(data);
        chart.setDrawGridBackground(false);
        chart.getLegend()
             .setWordWrapEnabled(true);
        chart.setDescription("");
        
        return rootView;
    }
    
    @Override
    public void onDestroyView() {
        //検索ボックスを削除
        Toolbar toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);
        toolbar.getMenu()
               .removeItem(R.id.menu_filter);
        
        super.onDestroyView();
    }
    
    @Override
    public void onCheckFinished(List<String> checkedList) {
        List<LineDataSet> dataSets = new ArrayList<>();
        
        if (checkedList.contains("燃料")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(fuel.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "燃料");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.fuel));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        if (checkedList.contains("弾薬")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(bullet.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "弾薬");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.bullet));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        if (checkedList.contains("鋼材")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(steel.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "鋼材");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.steel));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        if (checkedList.contains("ボーキサイト")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(bauxite.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "ボーキサイト");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.bauxite));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        if (checkedList.contains("高速修復材")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(bucket.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "高速修復材");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.bucket));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        if (checkedList.contains("高速建造剤")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(instantConstruction.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "高速建造剤");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.instant_construction));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        if (checkedList.contains("開発資材")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(developmentMaterial.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "開発資材");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.development_material));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        if (checkedList.contains("改修資材")) {
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                entries.add(new Entry(improvementMaterial.get(i), i));
            }
            LineDataSet dataSet = new LineDataSet(entries, "改修資材");
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.improvement_material));
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }
        
        LineData data = new LineData(dateList, dataSets);
        data.setHighlightEnabled(false);
        
        chart.setData(data);
        chart.invalidate();
    }
    
    
}

