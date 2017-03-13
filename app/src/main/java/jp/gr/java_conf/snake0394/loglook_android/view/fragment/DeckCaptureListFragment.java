package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.snake0394.loglook_android.DeckListOverlayService;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;

import static jp.gr.java_conf.snake0394.loglook_android.view.fragment.DeckCaptureListRecyclerAdapter.getFileHeader;

/**
 * 艦娘一覧画面
 */
public class DeckCaptureListFragment extends Fragment implements DeckCaptureListRecyclerAdapter.OnRecyclerItemClickListener,OptionsDialog.OnItemSelectedListener {
    
    private static final String[] options = new String[]{"開く", "オーバーレイ", "共有"};

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    
    private int selectedPositon = -1;
    private Unbinder unbinder;

    public DeckCaptureListFragment() {
        // Required empty public constructor
    }

    public static DeckCaptureListFragment newInstance() {
        DeckCaptureListFragment fragment = new DeckCaptureListFragment();
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_deck_capture_list, container, false);
        this.unbinder = ButterKnife.bind(this, rootView);
        

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (recyclerView.getAdapter() == null) {
            DeckCaptureListRecyclerAdapter adapter = new DeckCaptureListRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        
        initDataList();
        recyclerView.scrollToPosition(0);

        return rootView;
    }
    
    private void initDataList() {
        List<File> dataList = new ArrayList<>();
        File dataFile = new File(Environment.getExternalStorageDirectory()
                                            .getPath() + "/泥提督支援アプリ/capture/list");
        if (dataFile.exists()) {
            File[] files = dataFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    dataList.add(file);
                }
            }
            dataFile.delete();
        }
        
        ((DeckCaptureListRecyclerAdapter)recyclerView.getAdapter()).setItems(dataList);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
    
    @Override
    public void onRecyclerItemClicked(int position) {
    
        this.selectedPositon = position;
        
        DialogFragment dialogFragment = OptionsDialog.newInstance(options);
        dialogFragment.setTargetFragment(DeckCaptureListFragment.this, 0);
        dialogFragment.show(getFragmentManager(), "dialog");
    }
    
    @Override
    public void onItemSelected(int witch) {
        if (Objects.equals(selectedPositon, -1)) {
            return;
        }
    
        DeckCaptureListRecyclerAdapter recyclerAdapter = ((DeckCaptureListRecyclerAdapter)this.recyclerView.getAdapter());
        File selected = recyclerAdapter.getItemAt(selectedPositon);
        Logger.d("DeckCaptureListFragment", selected.getPath() + " was selected");
        
        switch (options[witch]){
            case "開く":
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(selected), getImageType(selected));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(selected));
                startActivity(intent);
                break;
            case "オーバーレイ":
                intent = new Intent(getActivity(), DeckListOverlayService.class);
                intent.putExtra("uri", selected.getPath());
                getActivity().startService(intent);
                break;
            case "共有":
                 intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(selected), getImageType(selected));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(selected));
                startActivity(intent);
                break;
        }
    }
    
    public static String  getImageType(File image) {
        if (image == null) {
            return "";
        }
        String fileHeader = getFileHeader(image);
        if (fileHeader == null || fileHeader.isEmpty()) {
            return "";
        }
        fileHeader = fileHeader.toUpperCase();
        if (fileHeader.equals("89504E470D0A1A0A")) { // PNG
            return "image/png";
        } else if (fileHeader.matches("^FFD8.*")) { // JPG
            return "image/jpg";
        } else if (fileHeader.matches("^474946383961.*") || fileHeader.matches("^474946383761.*")) { // GIF
            return "image/gif";
        } else if (fileHeader.matches("^424D.*")) { // BMP
            return "image/bmp";
        }
        return "";
    }
}
