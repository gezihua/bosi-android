
package com.bosi.chineseclass.su.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.R.id;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Entity;

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
    private Spinner mSpinner;
    private View mEmptyView;

    @Override
    void init() {
        mSpinner = (Spinner) getActivity().findViewById(R.id.filter_spinner);
        mEmptyView = getActivity().findViewById(R.id.text_empty);
        getActivity().findViewById(R.id.hen_start).setOnClickListener(this);
        getActivity().findViewById(R.id.shu_start).setOnClickListener(this);
        getActivity().findViewById(R.id.pie_start).setOnClickListener(this);
        getActivity().findViewById(R.id.dian_start).setOnClickListener(this);
        getActivity().findViewById(R.id.zhe_start).setOnClickListener(this);
        for (int i = 0; i <=36; i++) {
        }
    }

    @Override
    protected View getBasedView() {
        View view = mInflater.inflate(R.layout.dictionary_filter_stoke_layout, null, false);
        return view;
    }

    @Override
    public void onClick(View v) {
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
           mFirstFilterList= DbUtils.getInstance(getActivity()).getFilterListByStoke(String.valueOf(begin));

        }

    }

}
