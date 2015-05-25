package com.zy.booking.modle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.R;

public class SearchCateGoryAdapter extends SampleListAdapter{

	public SearchCateGoryAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	@Override
	public View getView(int posi, View viewTemp, ViewGroup arg2) {
		if(viewTemp == null){
			viewTemp = LayoutInflater.from(context).inflate(R.layout.item_search_concadition, null);
		}
		
		JsonObject mJsonObj =  (JsonObject) mListData.get(posi);
		
		//viewTemp.set
		return viewTemp;
	}
	
	

}
