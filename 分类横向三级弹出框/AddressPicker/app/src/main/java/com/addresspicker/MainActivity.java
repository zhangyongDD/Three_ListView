package com.addresspicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn_show_address_picker);
        textView = (TextView) findViewById(R.id.tv_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        final XsAddressDialog dialog = new XsAddressDialog(this,R.style.mycustom_dialog);
        dialog.show();
        dialog.setCancelable(true);
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        // 设置弹出框的宽高
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = 500;
        // 设置弹出框的位置
        win.setGravity(Gravity.BOTTOM);
        win.setAttributes(lp);
        dialog.setTextBackListener(new XsAddressDialog.TextBack() {
            @Override
            public void textback(String province, String city, String area) {
                textView.setText("选中地址是:     "+province+"---"+city+"---"+area);
                dialog.dismiss();
            }
        });

    }


}
