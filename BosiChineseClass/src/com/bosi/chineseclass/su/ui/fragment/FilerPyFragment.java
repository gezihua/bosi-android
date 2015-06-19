
package com.bosi.chineseclass.su.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.utils.WordUtils;

import java.util.ArrayList;

public class FilerPyFragment extends AbsFilterFragment {
    private Spinner mPySpinner;
    private GridView mPyGridView;
    private GridView mWordsGridView;

    @Override
    protected View getBasedView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dictionary_filter_py_layout, null, false);
        return view;
    }

    void init() {
        mPyGridView = (GridView) getActivity().findViewById(R.id.result_show);
        mWordsGridView = (GridView) getActivity().findViewById(R.id.result_show_words);
        mPySpinner = (Spinner) getActivity().findViewById(R.id.result_py_filter);

        ArrayAdapter pyAdapter = new ArrayAdapter(getActivity(),
                R.layout.spiner_item, WordUtils.WORDLIST);
        pyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 设置默认值
        mPySpinner.setVisibility(View.VISIBLE);
        mPySpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        mPySpinner.setAdapter(pyAdapter);
        mPySpinner.setSelection(0);

        mPyGridView.setAdapter(new PyAdapter());
        mPyGridView.setOnItemClickListener(new PyOnItemClickListener());
        
        mWordsGridView.setAdapter(new WordsAdapter());
        setResultOnItemClick(mWordsGridView);
    }

    private class PyOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSecondFilterList = DbUtils.getInstance(getActivity()).getFilterWordsByPy(
                    mFilteredList.get(position));
            ((BaseAdapter) (mWordsGridView.getAdapter())).notifyDataSetChanged();
            mWordsGridView.invalidate();
        }
    }

    private class WordsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mSecondFilterList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mSecondFilterList.get(position);
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
                holder.textView = (TextView) mInflater.inflate(R.layout.py_grid_item, null, false);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView = holder.textView;
            convertView.setTag(holder);
            holder.textView.setText(mSecondFilterList.get(position));
            return convertView;
        }

        private class ViewHolder {
            TextView textView;
        }

    }

    private class PyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mFilteredList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mFilteredList.get(position);
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
                holder.textView = (TextView) mInflater.inflate(R.layout.py_grid_item, null, false);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView = holder.textView;
            convertView.setTag(holder);
            holder.textView.setText(mFilteredList.get(position));
            return convertView;
        }

        private class ViewHolder {
            TextView textView;
        }

    }

    private class SpinnerSelectedListener implements Spinner.OnItemSelectedListener {
        private int before = -1;

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((Spinner) parent).setSelection(position, true);
            if (before == -1 && position == 0 || before == position) {
                before = position;
                return;
            }
            if (before == -1) {
                before = position;
            }
            mFilteredList = DbUtils.getInstance(getActivity()).getFilterListByPy(
                    WordUtils.WORDLIST[position]);
            ((BaseAdapter) (mPyGridView.getAdapter())).notifyDataSetChanged();
            mPyGridView.invalidate();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFilteredList = new ArrayList<String>();

    }

    private ArrayList<String> mFilteredList = null;
    private ArrayList<String> mSecondFilterList = new ArrayList<String>();

    @Override
    public void afterViewInject() {
        mFilteredList = DbUtils.getInstance(getActivity()).getFilterListByPy("A");
    }

    @Override
    public String getSelectedRstWord(int postion) {
        return mSecondFilterList.get(postion);
    }

}
