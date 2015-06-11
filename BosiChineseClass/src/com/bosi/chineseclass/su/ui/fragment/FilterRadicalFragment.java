
package com.bosi.chineseclass.su.ui.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Entity;

import java.util.ArrayList;
import java.util.List;

public class FilterRadicalFragment extends AbsFilterFragment {
    private class FilterAdapter extends BaseAdapter {

        private class ViewHolder {
            TextView text;
        }

        @Override
        public int getCount() {
            if (mFilterList != null) {
                return mFilterList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mFilterList != null && mFilterList.size() >= position) {
                mFilterList.get(position);
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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) mInflater.inflate(R.layout.py_grid_item, null, false);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(mFilterList.get(position));
            convertView = viewHolder.text;
            convertView.setTag(viewHolder);
            return convertView;
        }

    }

    private class FilterOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position <= mFilterList.size() && mFilterList != null) {
                List<String> lists = DbUtils.getInstance(getActivity()).getFilterBu(
                        mFilterList.get(position));
                if (lists != null && lists.size() > 0) {
                    String bu = lists.get(0);
                    mResultList = DbUtils.getInstance(getActivity()).getFilterRadicalsBy(bu);
                    mWordsList = mResultList;
                }
            }
            mResultGridView.invalidate();
            ((BaseAdapter) (mResultGridView.getAdapter())).notifyDataSetChanged();
        }

    }

    private class ResultAdapter extends BaseAdapter {

        class ViewHolder {
            TextView text;
        }

        @Override
        public int getCount() {
            if (mWordsList != null) {
                return mWordsList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mWordsList != null && mWordsList.size() >= position) {
                return mWordsList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) mInflater.inflate(R.layout.py_grid_item, null, false);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(mWordsList.get(position).word);
            convertView = viewHolder.text;
            convertView.setTag(viewHolder);
            return convertView;
        }

    }

    private class SpinnerSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e("SpinnerSelectedListener -----------", "" + position);
            mFilterList = DbUtils.getInstance(getActivity()).getFilterListByRadical(
                    String.valueOf(position));
            ((BaseAdapter) mFilterListView.getAdapter()).notifyDataSetChanged();
            mFilterListView.invalidate();
            Log.e("print", "" + position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private View mRootView;
    private Spinner mFilterSpinner;
    private Spinner mResultSpinner;

    private ListView mFilterListView;
    private GridView mResultGridView;

    private ArrayList<String> mFilterList = new ArrayList<String>();

    private ArrayList<Entity> mResultList = new ArrayList<Entity>();
    private ArrayList<Entity> mWordsList = new ArrayList<Entity>();

    @Override
    public void afterViewInject() {
    }

    @Override
    protected View getBasedView() {
        View view = mInflater.inflate(R.layout.dictionary_filter_radical_layout, null, false);
        mRootView = view;
        return view;
    }

    @Override
    public String getSelectedRstWord(int postion) {
        return mResultList.get(postion).word;
    }

    @Override
    void init() {
        mFilterSpinner = (Spinner) (mRootView.findViewById(R.id.filter_spinner_rad));
        mResultSpinner = (Spinner) (mRootView.findViewById(R.id.result_spinner_rad));
        mFilterListView = (ListView) (mRootView.findViewById(R.id.filter_list));
        mResultGridView = (GridView) (mRootView.findViewById(R.id.reslut_grid_rad));
        setFilterAdapter();
        setResultAdapter();
        setFilterListAdapter();
        setResultGridAdapter();

        setResultOnItemClick(mResultGridView);
        mFilterSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        mFilterListView.setOnItemClickListener(new FilterOnItemClickListener());
        mResultSpinner.setOnItemSelectedListener(new ResultSelectedListener());

    }

    private class ResultSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }

    }

    private void setFilterAdapter() {
        Log.e("print", "is calling");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= 17; i++) {
            if (i == 0) {
                list.add("全部");
            } else {
                list.add(String.valueOf(i));
            }
        }
        ArrayAdapter pyAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, list);
        pyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 设置默认值
        mFilterSpinner.setVisibility(View.VISIBLE);
        mFilterSpinner.setAdapter(pyAdapter);
    }

    private void setFilterListAdapter() {
        mFilterListView.setAdapter(new FilterAdapter());
    }

    private void setResultAdapter() {
        Log.e("print", "is calling");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= 26; i++) {
            if (i == 0) {
                list.add("全部");
            } else {
                list.add(String.valueOf(i));
            }
        }
        ArrayAdapter pyAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, list);
        pyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 设置默认值
        mResultSpinner.setVisibility(View.VISIBLE);
        mResultSpinner.setAdapter(pyAdapter);

    }

    private void setResultGridAdapter() {
        mResultGridView.setAdapter(new ResultAdapter());
    }

}
