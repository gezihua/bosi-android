package com.bosi.chineseclass.han.modle;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.bosi.chineseclass.han.util.Utils;

public class ZyCategoryAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ZyCategoryInfo> mIconList;

    public class GridHolder {
        private ImageView icon;
        private TextView title;
        private View background;
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
            holder.background = convertView;
            convertView.setTag(holder);
        } else {
            holder = (GridHolder) convertView.getTag();
        }
        
        ZyCategoryInfo info = mIconList.get(position);
        Bitmap icon = IconUtils.getImageFromAssetsFile(mContext, info.getIconPath());
        String title = info.getTitle();
        holder.icon.setImageBitmap(icon);
        holder.title.setText(title);
        String backgroundPath = "background_" + (position % 8);
        int backgroundId = IconUtils.getDrawaleIdFromName(mContext, backgroundPath);
        holder.background.setBackgroundResource(backgroundId);

        if (Utils.isEmpty(title)) {
            int title_background_id = IconUtils.getDrawaleIdFromName(mContext, info.getTitle_background_path());
            if (title_background_id > 0) {
                holder.title.setBackgroundResource(title_background_id);
            }
        } else {
            holder.title.setBackground(null);
        }
        return convertView;

    }

}