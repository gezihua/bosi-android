package com.bosi.chineseclass.han.modle;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.db.GameIconInfo;
import com.bosi.chineseclass.han.util.IconUtils;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<GameIconInfo> mIconList;

    public class GridHolder {
        ImageView icon;
        ImageView icon_above;
        public ImageView getIcon_above() {
            return icon_above;
        }
    }

    public ImageAdapter(Context c, List<GameIconInfo> iconList) {
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
            convertView = inflater.inflate(R.layout.layout_game_item, null);
            holder = new GridHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.game_item_image);
            holder.icon_above = (ImageView) convertView.findViewById(R.id.game_item_focuse);
            convertView.setTag(holder);
        } else {
            holder = (GridHolder) convertView.getTag();
        }
        Log.e("HNX", "" + position);
        int iconId = IconUtils.getIconDrawableId(mContext, mIconList, position);
        if (iconId == -1){
            holder.icon.setImageDrawable(null);
        } else {
            holder.icon.setImageResource(iconId);
        }
        return convertView;

    }

}