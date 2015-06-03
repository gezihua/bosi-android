
package com.bosi.chineseclass.su.ui.fragment;

import android.R.integer;
import android.content.Entity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.bosi.chineseclass.R;

import java.util.ArrayList;
import java.util.List;

public class FilterRadicalFragment extends AbsFilterFragment {
    private Spinner mFilterSpinner;
    private Spinner mResultSpinner;
    private ListView mFilterListView;
    private GridView mResultGridView;
    private ArrayList<String> mFilterList = new ArrayList<String>();
    private ArrayList<Entity> mResultList = new ArrayList<Entity>();

    @Override
    void init() {
        mFilterSpinner = (Spinner) getActivity().findViewById(R.id.filter_spinner);
        mResultSpinner = (Spinner) getActivity().findViewById(R.id.result_spinner);
        mFilterListView = (ListView) getActivity().findViewById(R.id.filter_list);
        mResultGridView = (GridView) getActivity().findViewById(R.id.reslut_grid);
        setFilterAdapter();
        setResultAdapter();
        setFilterListAdapter();
        setResultGridAdapter();
    }

    private void setResultGridAdapter() {
        
    }

    private void setFilterListAdapter() {
        // TODO Auto-generated method stub
        
    }

    private void setResultAdapter() {
        
    }

    private void setFilterAdapter() {
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
        mFilterSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        mFilterSpinner.setAdapter(pyAdapter);
    }

    private class SpinnerSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterList = DbUtils.getInstance(getActivity()).getFilterListByRadical(String.valueOf(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    protected View getBasedView() {
        View view = mInflater.inflate(R.layout.dictionary_filter_radical_layout, null, false);
        return view;
    }

    @Override
    public void afterViewInject() {
    }

}
