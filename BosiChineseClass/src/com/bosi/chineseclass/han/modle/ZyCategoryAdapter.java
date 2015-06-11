package com.bosi.chineseclass.han.modle;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.db.ZyCategoryInfo;
import com.bosi.chineseclass.han.util.IconUtils;

public class ZyCategoryAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ZyCategoryInfo> mIconList;

    public class GridHolder {
        ImageView icon;
        private TextView title;
    }

    public ZyCategoryAdapter(Context c, List<ZyCategoryInfo> iconList) {
        mContext = c;
        mIconList = iconList;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mIconList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_zy_category_item, null);
            holder = new GridHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.zy_category_item_image);
            holder.title = (TextView) convertView.findViewById(R.id.zy_category_item_title);
            convertView.setTag(holder);
        } else {
            holder = (GridHolder) convertView.getTag();
        }
        
        ZyCategoryInfo info = mIconList.get(position);
        Log.e("HNX", "" + position + "    path : " + mIconList.get(position).getIconPath());
        Bitmap icon = IconUtils.getImageFromAssetsFile(mContext, info.getIconPath());
        holder.icon.setImageBitmap(icon);
        holder.title.setText(info.getTitle());
        return convertView;

    }

}