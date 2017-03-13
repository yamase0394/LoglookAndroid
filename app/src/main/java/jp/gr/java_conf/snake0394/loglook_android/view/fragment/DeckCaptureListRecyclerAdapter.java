package jp.gr.java_conf.snake0394.loglook_android.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.gr.java_conf.snake0394.loglook_android.R;
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * Created by snake0394 on 2016/12/08.
 */
public class DeckCaptureListRecyclerAdapter extends RecyclerView.Adapter<DeckCaptureListRecyclerAdapter.RecyclerViewHolder> {
    
    private SortedList<File> sortedList;
    private OnRecyclerItemClickListener listener;
    
    public DeckCaptureListRecyclerAdapter(OnRecyclerItemClickListener listener) {
        sortedList = new SortedList<>(File.class, new SortedListCallcack(this));
        this.listener = listener;
    }
    
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.cardview_deck_capture, viewGroup, false);
        return new RecyclerViewHolder(itemView, listener);
    }
    
    @Override
    public void onBindViewHolder(RecyclerViewHolder sampleViewHolder, int i) {
        File data = sortedList.get(i);
        if (data != null) {
            sampleViewHolder.bind(data);
        }
    }
    
    @Override
    public int getItemCount() {
        return sortedList.size();
    }
    
    public void setItems(List<File> newItemList) {
        for (File file : newItemList) {
            if (isImage(file)) {
                sortedList.add(file);
            }
        }
    }
    
    public File getItemAt(int position) {
        return sortedList.get(position);
    }
    
    private static class SortedListCallcack extends SortedList.Callback<File> {
        
        private RecyclerView.Adapter adapter;
        
        SortedListCallcack(@NonNull RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }
        
        @Override
        public int compare(File item1, File item2) {
            return (int) (item1.lastModified() - item2.lastModified());
        }
        
        @Override
        public void onInserted(int position, int count) {
            adapter.notifyItemRangeInserted(position, count);
        }
        
        @Override
        public void onRemoved(int position, int count) {
            adapter.notifyItemRangeRemoved(position, count);
        }
        
        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }
        
        @Override
        public void onChanged(int position, int count) {
            adapter.notifyItemRangeChanged(position, count);
        }
        
        @Override
        public boolean areContentsTheSame(File oldData, File newData) {
            return Objects.equals(oldData.getPath(), newData.getPath());
        }
        
        @Override
        public boolean areItemsTheSame(File data1, File data2) {
            return Objects.equals(data1.getPath(), data2.getPath());
    
        }
    }
    
    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
    
        private final OnRecyclerItemClickListener listener;
    
        @BindView(R.id.preview)
        ImageView previewImage;
        @BindView(R.id.name)
        TextView nameText;
    
        public RecyclerViewHolder(View rootView, final OnRecyclerItemClickListener listener) {
            super(rootView);
            this.listener = listener;
            ButterKnife.bind(this, rootView);
        }
        
        public void bind(File file) {
            try {
                InputStream is = new FileInputStream(file);
                Bitmap bm = BitmapFactory.decodeStream(is);
                previewImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                previewImage.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            
           nameText.setText(file.getName());
        }
        
        @OnClick(R.id.cardview)
        void onItemClicked(){
            Logger.d("RecyclerViewHolder", "onClick");
            listener.onRecyclerItemClicked(getAdapterPosition());
        }
    }
    
    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClicked(int position);
    }
    
    public static String getFileHeader(File f) {
        if (f.length() == 0) {
            return null;
        } else {
            byte[] b = new byte[8];
            InputStream in = null;
            try {
                in = FileUtils.openInputStream(f);
                b = toByteArray(in, 8);
            } catch (IOException e) {
                return "";
            } finally {
                IOUtils.closeQuietly(in);
            }
            return new String(Hex.encodeHex(b));
        }
    }
    
    public static boolean isImage(File image) {
        if (image == null) {
            return false;
        }
        String fileHeader = getFileHeader(image);
        if (fileHeader == null || fileHeader.isEmpty()) {
            return false;
        }
        fileHeader = fileHeader.toUpperCase();
        if (fileHeader.equals("89504E470D0A1A0A")) { // PNG
            return true;
        } else if (fileHeader.matches("^FFD8.*")) { // JPG
            return true;
        } else if (fileHeader.matches("^474946383961.*") || fileHeader.matches("^474946383761.*")) { // GIF
            return true;
        } else if (fileHeader.matches("^424D.*")) { // BMP
            return true;
        }
        return false;
    }
}