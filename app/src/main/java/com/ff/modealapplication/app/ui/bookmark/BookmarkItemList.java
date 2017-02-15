package com.ff.modealapplication.app.ui.bookmark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ff.modealapplication.R;
import com.ff.modealapplication.andorid.network.SafeAsyncTask;
import com.ff.modealapplication.app.core.service.BookmarkService;
import com.ff.modealapplication.app.core.util.LoginPreference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by BIT on 2017-02-14.
 */

public class BookmarkItemList extends ArrayAdapter<Map<String, Object>> {

    private LayoutInflater layoutInflater;
    DisplayImageOptions displayImageOption = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.apple)
            .showImageOnFail(R.drawable.apple)
            .delayBeforeLoading(0)
            .cacheOnDisc(true)
            .build();

    // 체크된 아이템들을 저장할 List
    boolean[] isChecked;
    List<Map<String, Object>> list;

    public BookmarkItemList(Context context) {
        super(context, R.layout.activity_bookmark);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final int checkPosition = position;
        final BookHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.bookmark_item_row, null);
            holder = new BookHolder();
            holder.text = (TextView) convertView.findViewById(R.id.bookmark_item_text);
            holder.delete = (ToggleButton) convertView.findViewById(R.id.bookmark_item_delete);
            holder.imageView = (ImageView) convertView.findViewById(R.id.bookmark_item_image);
            holder.send_no = (TextView) convertView.findViewById(R.id.send_no);
            convertView.setTag(holder);
        } else {
            holder = (BookHolder)convertView.getTag();
        }
        holder.text.setText(getItem(position).get("iname").toString()); // 상품명
        holder.send_no.setText(getItem(position).get("itemNo").toString()); // 상품No
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
        ImageLoader.getInstance().displayImage("http://192.168.1.93:8088/modeal/shop/images/" + getItem(position).get("ipicture"), holder.imageView, displayImageOption); // 상품이미지

        holder.delete.setChecked(false);
        if (isChecked[position]) {
            new BookmarkDelete(((Double)getItem(position).get("itemNo")).intValue()).execute(); // DB에서 삭제
            BookmarkItemList.this.remove(getItem(position)); // view에서 삭제
            isChecked[position] = true;
            BookmarkItemList.this.notifyDataSetChanged(); // 갱신
            holder.delete.setChecked(true);
        } else {
            holder.delete.setChecked(false);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.delete.isChecked()) {
                    isChecked[position] = true;
                } else {
                    isChecked[position] = false;
                }
            }
        });
        return convertView;
    }

    public static class BookHolder {
        public ToggleButton delete;
        public TextView text;
        public ImageView imageView;
        public TextView send_no;
    }

    public void add(List<Map<String, Object>> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.list = list;
        isChecked = new boolean[list.size()];
        for (Map<String, Object> map : list) {
            Log.d("List items : ", "" + map);
            add(map);

        }
    }

    private class BookmarkDelete extends SafeAsyncTask<Void> {
        int buttonPosition;

        public BookmarkDelete(int buttonPosition) {
            this.buttonPosition = buttonPosition;
        }

        @Override
        public Void call() throws Exception {
            BookmarkService bookmarkService = new BookmarkService();
            bookmarkService.bookmarkDelete((long) buttonPosition, (Long) LoginPreference.getValue(getApplicationContext(), "no"));
            return null;
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            Log.d("*Main Exception error :", "" + e);
            throw new RuntimeException(e);
        }
    }
}
