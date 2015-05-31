
package com.bosi.chineseclass.su.ui.fragment;

import android.view.View;

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
public class FiterStokeFragment extends AbsFilterFragment {
    private ArrayList<Entity> mFirstFilterList = new ArrayList<Entity>();

    public static class Entity {
        

    }

    @Override
    void init() {

    }

    @Override
    protected View getBasedView() {
        // TODO Auto-generated method stub
        return null;
    }

}
