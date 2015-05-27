
package com.bosi.chineseclass.activitys;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.su.db.DicOpenHelper;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.main);

        // try {
        // getAssets().open("dict.db",0);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        init();
    }
    private void init(){
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DicOpenHelper helper = new DicOpenHelper(getBaseContext());
                SQLiteDatabase database = helper.getReadableDatabase();
            }
        });
    }
}
