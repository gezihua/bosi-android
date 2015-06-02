
package com.bosi.chineseclass.su.ui.fragment;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Entity;
import com.emsg.sdk.EmsgClient.MsgTargetType;

import u.aly.s;

import java.util.ArrayList;

/**
 * 这个Fragement 其实只会负责一个Filter list , 但会，进行多次Filter， 元数据也可能不同
 * 
 * @author Myjoy
 * @date 2015-5-31
 *       <p>
 *       1 . 代表 横起笔 2 . 代表竖起笔 3 . 代表撇起笔 4 . 代表点起笔 5 . 代表横折起笔
 *       </p>
 */
public class FiterStokeFragment extends AbsFilterFragment implements OnClickListener {
    private ArrayList<Entity> mFirstFilterList = new ArrayList<Entity>();
    private ArrayList<Entity> mStokesList = new ArrayList<Entity>();
    private Spinner mSpinner;
    private View mEmptyView;
    private GridView mResult;

    @Override
    void init() {
        mSpinner = (Spinner) getActivity().findViewById(R.id.filter_spinner);
        mEmptyView = getActivity().findViewById(R.id.text_empty);
        getActivity().findViewById(R.id.hen_start).setOnClickListener(this);
        getActivity().findViewById(R.id.shu_start).setOnClickListener(this);
        getActivity().findViewById(R.id.pie_start).setOnClickListener(this);
        getActivity().findViewById(R.id.dian_start).setOnClickListener(this);
        getActivity().findViewById(R.id.zhe_start).setOnClickListener(this);
        String[] strs = new String[37];
        for (int i = 0; i < strs.length; i++) {
            if (i == 0) {
                strs[0] = "全部";
            } else {
                strs[i] = String.valueOf(i);
            }
        }
        ArrayAdapter pyAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, strs);
        pyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(pyAdapter);
        mSpinner.setVisibility(View.VISIBLE);
        // mSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        mSpinner.setSelection(0);

        mResult = (GridView) getActivity().findViewById(R.id.filter_result);
        mResult.setAdapter(new ResultAdapter());
        mResult.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private class ResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mStokesList != null) {
                return mStokesList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mStokesList != null && mStokesList.size() > 0) {
                return mStokesList.size();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                holder.text = (TextView) mInflater.inflate(R.layout.py_grid_item, null, false);
            } else {
                holder = ((ViewHolder) convertView.getTag());
            }
            holder.text.setText(mStokesList.get(position).word);
            convertView = holder.text;
            convertView.setTag(holder);
            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            // if (condition) {
            //
            // }
        }

        private class ViewHolder {
            TextView text;
        }

    }

    public void setSpinnerAdapter(SpinnerAdapter adapter) {
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new StokesSelectedListener());
    }

    private class StokesSelectedListener implements OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                mStokesList = mFirstFilterList;
            } else {
                Log.e("print", "StokesSelectedListener");
                filterByStoke(String.valueOf(position));
            }
            ((BaseAdapter) (mResult.getAdapter())).notifyDataSetChanged();
            mResult.invalidate();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        private void filterByStoke(String stoke) {
            mStokesList = new ArrayList<Entity>();
            Log.e("print", "-------filterByStoke");
            for (int i = 0; i < mFirstFilterList.size(); i++) {
                Entity entity = mFirstFilterList.get(i);
                Log.e("print", "-------" + entity.stokes);
                if (entity.stokes.equals(stoke)) {
                    Log.e("print", "-------exe");
                    mStokesList.add(entity);
                }

            }
        }

    }

    @Override
    protected View getBasedView() {
        View view = mInflater.inflate(R.layout.dictionary_filter_stoke_layout, null, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        mEmptyView.setVisibility(View.GONE);
        int id = v.getId();
        int begin = 0;
        switch (id) {
            case R.id.hen_start: {
                begin = 1;
                break;
            }
            case R.id.shu_start: {
                begin = 2;
                break;
            }
            case R.id.pie_start: {
                begin = 3;
                break;
            }
            case R.id.dian_start: {
                begin = 4;
                break;
            }
            case R.id.zhe_start: {
                begin = 5;
                break;
            }

            default:
                begin = 0;
                break;
        }
        if (begin != 0) {
            mFirstFilterList = DbUtils.getInstance(getActivity()).getFilterListByStoke(
                    String.valueOf(begin));
            mStokesList = mFirstFilterList;
            mResult.invalidate();
            ((BaseAdapter) (mResult.getAdapter())).notifyDataSetChanged();
        }
        mResult.setVisibility(View.VISIBLE);

    }

}
