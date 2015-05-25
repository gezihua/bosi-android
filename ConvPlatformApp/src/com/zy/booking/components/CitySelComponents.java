package com.zy.booking.components;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.tempdata.WeatherAreaModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CitySelComponents extends BaseComponents {

	public interface OnCitySelectedListener {
		public void onCitySelected(String provice, String city, String area);
	}
	
	public void setOnCitySelected(OnCitySelectedListener mListener){
		this.mOnCitySelectedListener = mListener;
	}

	private String selcityid = "";

	private ArrayList<WeatherAreaModel> weatherAreaModels;
	private WeatherAreaModel nowWeatherAreaModel;

	@ViewInject(R.id.sp_provice)
	private Spinner add_province_box;
	
	
	private ArrayList<WeatherAreaModel> provinceList = new ArrayList<WeatherAreaModel>();

	private ArrayAdapter<String> provinceAdapter;

	@ViewInject(R.id.sp_city)
	private Spinner add_city_box;
	private ArrayList<WeatherAreaModel> cityList = new ArrayList<WeatherAreaModel>();
	private ArrayAdapter<String> cityAdapter;

	@ViewInject(R.id.sp_area)
	private Spinner add_street_box;
	private ArrayList<WeatherAreaModel> streetList = new ArrayList<WeatherAreaModel>();
	private ArrayAdapter<String> streetAdapter;

	@ViewInject(R.id.tv_cancle)
	private View mCancle;

	@ViewInject(R.id.tv_sure)
	private View mSure;

	OnCitySelectedListener mOnCitySelectedListener;

	public CitySelComponents(LayoutInflater mLayoutInflater, Context mContext) {
		super(mLayoutInflater, mContext);
	}

	@Override
	public void initFatherView() {
		weatherAreaModels = com.zy.booking.components.tempdata.WeatherAreaList
				.GetList();
		
	}

	public void showDital(View mViewBase) {
		mFatherView = LayoutInflater.from(mContext).inflate(
				R.layout.popu_sel_citys, null);
		ViewUtils.inject(this, mFatherView);
		initPopuWindow(mFatherView);
	
		
	
		CreateAddress();
	
		mPopuWindow.showAsDropDown(mViewBase);
	}

	PopupWindow mPopuWindow;

	private void initPopuWindow(View mView) {
		mPopuWindow = new PopupWindow(mView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopuWindow.setOutsideTouchable(true);

	}

	/**
	 * 创建天气城市弹窗
	 * 
	 */
	private void CreateAddress() {

		GetProvinceList();

		if (nowWeatherAreaModel != null) {
			BindProvince(nowWeatherAreaModel.areaname1());
		} else {
			BindProvince(null);
		}

		add_province_box
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						String name = (String) add_province_box
								.getSelectedItem();
						WeatherAreaModel model = GetProvinceByName(name);
						GetCityList(model.areaname1());
						if (nowWeatherAreaModel != null) {
							BindCity(nowWeatherAreaModel.areaname2());
						} else {
							BindCity(null);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		add_city_box.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String name = (String) add_city_box.getSelectedItem();
				WeatherAreaModel model = GetCityByName(name);
				GetStreetList(model.areaname2());
				if (nowWeatherAreaModel != null) {
					BindStreet(nowWeatherAreaModel.areaname3());
				} else {
					BindStreet(null);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		add_street_box.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String name = (String) add_street_box.getSelectedItem();
				WeatherAreaModel model = GetStreetByName(name);
				selcityid = model.areaid();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		mCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopuWindow.dismiss();
			}
		});

		mSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GetNowWeatherArea(selcityid);
				mPopuWindow.dismiss();
			}
		});
	}

	/**
	 * 当前天气城市
	 * 
	 * @param ctiyid
	 */
	private void GetNowWeatherArea(String areaid) {
		for (WeatherAreaModel item : weatherAreaModels) {
			if (item.areaid().equals(areaid)) {
				nowWeatherAreaModel = item;
				break;
			}
		}
		if (nowWeatherAreaModel == null) {
			nowWeatherAreaModel = weatherAreaModels.get(0);
		}

		mOnCitySelectedListener.onCitySelected(nowWeatherAreaModel.areaname1(),
				nowWeatherAreaModel.areaname2(),
				nowWeatherAreaModel.areaname3());
		/*
		 * "当前选择城市：id:" + nowWeatherAreaModel.areaid() + "，" +
		 * nowWeatherAreaModel.areaname1() + "-" +
		 * nowWeatherAreaModel.areaname2() + "-" +
		 * nowWeatherAreaModel.areaname3();
		 */
	}

	/**
	 * 根据名称获取一级城市
	 * 
	 * @param name
	 * @return
	 */
	private WeatherAreaModel GetProvinceByName(String name) {
		WeatherAreaModel model = null;
		for (WeatherAreaModel item : provinceList) {
			if (item.areaname1().equals(name)) {
				model = item;
				break;
			}
		}
		return model;
	}

	/**
	 * 根据名称获取二级城市
	 * 
	 * @param name
	 * @return
	 */
	private WeatherAreaModel GetCityByName(String name) {
		WeatherAreaModel model = null;
		for (WeatherAreaModel item : cityList) {
			if (item.areaname2().equals(name)) {
				model = item;
				break;
			}
		}
		return model;
	}

	/**
	 * 根据名称获取三级城市
	 * 
	 * @param name
	 * @return
	 */
	private WeatherAreaModel GetStreetByName(String name) {
		WeatherAreaModel model = null;
		for (WeatherAreaModel item : streetList) {
			if (item.areaname3().equals(name)) {
				model = item;
				break;
			}
		}
		return model;
	}

	/**
	 * 获取一级城市列表
	 */
	private void GetProvinceList() {
		provinceList = new ArrayList<WeatherAreaModel>();
		for (WeatherAreaModel item : weatherAreaModels) {
			if (!checkProvince(item.areaname1())) {
				provinceList.add(item);
			}
		}
	}

	/**
	 * 绑定省
	 * 
	 * @param provincename
	 */
	private void BindProvince(String provincename) {
		ArrayList<String> list = new ArrayList<String>();
		for (WeatherAreaModel item : provinceList) {
			list.add(item.areaname1());
		}
		provinceAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, list);
		provinceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		add_province_box.setAdapter(provinceAdapter);
		if (provincename == null) {
			add_province_box.setSelection(0);
		} else {
			for (int i = 0; i < provinceList.size(); i++) {
				if (provincename.equals(provinceList.get(i).areaname1())) {
					add_province_box.setSelection(i);
					break;
				}
			}
		}
	}

	/**
	 * 是否已存在该一级城市
	 * 
	 * @param name
	 * @return
	 */
	private boolean checkProvince(String name) {
		boolean t = false;
		for (WeatherAreaModel item : provinceList) {
			if (item.areaname1().equals(name)) {
				t = true;
				break;
			}
		}
		return t;
	}

	/**
	 * 获取二级城市
	 * 
	 * @param pname
	 */
	private void GetCityList(String pname) {
		cityList = new ArrayList<WeatherAreaModel>();
		for (WeatherAreaModel item : weatherAreaModels) {
			if (item.areaname1().equals(pname)) {
				if (!checkCity(item.areaname2())) {
					cityList.add(item);
				}
			}
		}
	}

	/**
	 * 绑定市
	 * 
	 * @param cityname
	 */
	private void BindCity(String cityname) {
		ArrayList<String> list = new ArrayList<String>();
		for (WeatherAreaModel item : cityList) {
			list.add(item.areaname2());
		}
		cityAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, list);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		add_city_box.setAdapter(cityAdapter);
		if (cityname == null) {
			add_city_box.setSelection(0);
		} else {
			for (int i = 0; i < cityList.size(); i++) {
				if (cityname.equals(cityList.get(i).areaname2())) {
					add_city_box.setSelection(i);
					break;
				}
			}
		}
	}

	/**
	 * 是否已存在该二级城市
	 * 
	 * @param name
	 * @return
	 */
	private boolean checkCity(String name) {
		boolean t = false;
		for (WeatherAreaModel item : cityList) {
			if (item.areaname2().equals(name)) {
				t = true;
				break;
			}
		}
		return t;
	}

	/**
	 * 获取三级城市
	 * 
	 * @param pname
	 */
	private void GetStreetList(String pname) {
		streetList = new ArrayList<WeatherAreaModel>();
		for (WeatherAreaModel item : weatherAreaModels) {
			if (item.areaname2().equals(pname)) {
				if (!checkStreet(item.areaname3())) {
					streetList.add(item);
				}
			}
		}
	}

	/**
	 * 绑定区
	 * 
	 * @param streetname
	 */
	private void BindStreet(String streetname) {
		ArrayList<String> list = new ArrayList<String>();
		for (WeatherAreaModel item : streetList) {
			list.add(item.areaname3());
		}
		streetAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, list);
		streetAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		add_street_box.setAdapter(streetAdapter);
		if (streetname == null) {
			add_street_box.setSelection(0);
		} else {
			for (int i = 0; i < streetList.size(); i++) {
				if (streetname.equals(streetList.get(i).areaname3())) {
					add_street_box.setSelection(i);
					break;
				}
			}
		}
	}

	/**
	 * 是否已存在该三级城市
	 * 
	 * @param name
	 * @return
	 */
	private boolean checkStreet(String name) {
		boolean t = false;
		for (WeatherAreaModel item : streetList) {
			if (item.areaname3().equals(name)) {
				t = true;
				break;
			}
		}
		return t;
	}
}
