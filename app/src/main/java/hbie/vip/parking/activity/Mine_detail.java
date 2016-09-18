package hbie.vip.parking.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hbie.vip.parking.R;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public class Mine_detail extends Activity implements View.OnClickListener{
    private RelativeLayout btn_back;
    private TextView textone,texttwo,textthird,textfour,textViewfive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_text);
        iniview();
    }

    private void iniview() {
        btn_back = (RelativeLayout) findViewById(R.id.relativeLayout_editoril_material_back);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relativeLayout_editoril_material_back:
                finish();
                break;
        }
    }
}
