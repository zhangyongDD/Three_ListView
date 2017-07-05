package com.lai.threemenudemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lai.threemenudemo.bean.MenuData;
import com.lai.threemenudemo.dialog.ThreeMenuDialog;

/**
 * 主页面，提供两种选择进入菜单列表
 * Created by LaiYingtang on 2016/5/25.
 */
public class HomeActivity extends Activity {
    private static final int REQUEST_CODE = 100;//请求码
    private TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //第一个选择
        tv1 = (TextView) findViewById(R.id.textview1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(HomeActivity.this, MainActivity.class),REQUEST_CODE);
            }
        });


        //第二个
        tv2 = (TextView) findViewById(R.id.textview2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreeMenuDialog dialog=new ThreeMenuDialog(HomeActivity.this);
                dialog.setonItemClickListener(new ThreeMenuDialog.MenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(MenuData menuData) {
                        if (menuData!=null)
                            tv2.setText(menuData.name); //选中第三个菜单后，主页面的name设置为选中的name
                    }
                });
                dialog.show();
            }
        });
    }

    //跳转回掉
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if (data !=null){
                    MenuData menuData= (MenuData) data.getSerializableExtra("menu");
                    if (menuData!=null)
                        tv1.setText(menuData.name);
                }
                break;
            default:

        }
    }
}
