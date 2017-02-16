package com.ff.modealapplication.app.ui.item;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ff.modealapplication.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Created by bit-desktop on 2017-02-01.
 */

public class ItemListArrayAdapter extends ArrayAdapter<Map<String, Object>> {

    private LayoutInflater layoutInflater;
    DisplayImageOptions displayImageOption = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.apple)
            .showImageOnFail(R.drawable.apple)
            .delayBeforeLoading(0)
            .cacheOnDisc(true)
            .build();

    public ItemListArrayAdapter(Context context) {
        super(context, R.layout.item_list);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) { // 내부 많이 변경 (170207/상욱변경)

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_row, parent, false);
        }

        Map<String, Object> map = getItem(position);
//        ((TextView) convertView.findViewById(R.id.shop_name)).setText(map.get("shopName").toString());             // 해당 매장명
        ((TextView) convertView.findViewById(R.id.item_list_clock)).setText(map.get("expDate").toString());        // 유통기한
        ((TextView) convertView.findViewById(R.id.item_list_name)).setText(map.get("name").toString());            // 상품명
        ((TextView) convertView.findViewById(R.id.item_list_ori_price)).setText((map.get("oriPrice")).toString());   // 원가
        ((TextView) convertView.findViewById(R.id.item_list_price)).setText(map.get("price").toString());          // 판매가
        ((TextView) convertView.findViewById(R.id.item_list_shop_name)).setText(map.get("shopName").toString());   // 매장명
//        ((TextView) convertView.findViewById(R.id.item_list_distance)).setText(map.get(""));                          // 거리(반경)

        // 액티비티로 데이터 보내기 위해서...
        ((TextView) convertView.findViewById(R.id.send_no)).setText(String.valueOf(((Double) map.get("no")).longValue()));

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
        ImageLoader.getInstance().displayImage("http://192.168.1.90:8088/modeal/shop/images/" + map.get("picture"),
                (ImageView) convertView.findViewById(R.id.item_list_image), displayImageOption);                // 상품이미지

        convertView.findViewById(R.id.button_modify_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ItemModifyActivity.class);
                intent.putExtra("no", ((TextView)v.findViewById(R.id.send_no)).getText().toString());
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    // 목록에 상품이 추가됨
    public void add(List<Map<String, Object>> list) {
        if (list == null) {
            return;
        }
        for (Map<String, Object> map : list) {
            Log.d("test", "" + map);
            add(map);
        }
    }
}