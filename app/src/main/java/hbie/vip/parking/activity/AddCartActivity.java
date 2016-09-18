package hbie.vip.parking.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.constants.MessageConstants;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.ILoadingLayout;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by Administrator on 2016/6/8.
 */
public class AddCartActivity extends BaseActivity implements View.OnClickListener{
    private Intent intent;
    private static final int ADD_KEY = 1;
    private static final int SET_CURRENT_CART_KEY = 2;
    //    private RelativeLayout iv_bankname,iv_subbranch,iv_cartname,iv_cartnumber;
    private RelativeLayout bank_back,btn_bankcart_delect;

    private ProgressDialog bankdiolog;
    private Context mContext;
    private String cartId,banktype,subBranch,cartName,cartNumber,usdaptcard;
    private EditText iv_bankname,iv_subbranch,iv_cartname,iv_cartnumber;
    private Button bankfinish;
    private UserInfo user;
    private TextView tv_update_title;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case ADD_KEY:
                     String data = (String) msg.obj;
                    try {
                        JSONObject  obj = new JSONObject(data);
                        String state = obj.getString("status");
                        if (state.equals("success")){
                            ToastUtils.ToastShort(mContext,"提交成功");
                            finish();
                        }else{
                            ToastUtils.ToastShort(mContext,obj.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case SET_CURRENT_CART_KEY:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_cart_activity);
        intent = getIntent();
        usdaptcard = intent.getStringExtra("usdaptcard");
        banktype = intent.getStringExtra("bankname");
        subBranch = intent.getStringExtra("branchname");
        cartName = intent.getStringExtra("cardname");
        cartNumber = intent.getStringExtra("cartNum");
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo();
        bankdiolog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        bankiniview();
    }

    private void bankiniview() {
        bank_back = (RelativeLayout) findViewById(R.id.btn_bankcart_back);
        bank_back.setOnClickListener(this);
        btn_bankcart_delect = (RelativeLayout) findViewById(R.id.btn_bankcart_delect);
        tv_update_title = (TextView) findViewById(R.id.tv_add_card_title);
        iv_bankname = (EditText) findViewById(R.id.et_bank_name);
        iv_subbranch = (EditText) findViewById(R.id.et_subbranch_name);
        iv_cartname = (EditText) findViewById(R.id.et_cart_name);
        iv_cartnumber = (EditText) findViewById(R.id.et_cart_number);
        bankfinish = (Button) findViewById(R.id.btn_finish_submit);
        bankfinish.setOnClickListener(this);
        if ("1".equals(usdaptcard)){
            iv_bankname.setText(cartName);
            iv_subbranch.setText(subBranch);
            iv_cartname.setText(banktype);
            iv_cartnumber.setText(cartNumber);
            tv_update_title.setText("修改");
            btn_bankcart_delect.setVisibility(View.VISIBLE);
            btn_bankcart_delect.setOnClickListener(this);
        }else{
            iv_bankname.setText("");
            iv_subbranch.setText("");
            iv_cartname.setText("");
            iv_cartnumber.setText("");
            tv_update_title.setText("添加");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bankcart_back:
                finish();
                break;
            case R.id.btn_finish_submit:
                getFinishphone();
                break;
            case R.id.btn_bankcart_delect:
                if (NetBaseUtils.isConnnected(mContext)) {
                    user.readData(mContext);
                    new UserRequest(mContext,handler).AddBankCard(user.getUserId(),"","","","",ADD_KEY);

                }else{
                    ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                }
                break;
        }
    }

    private void getFinishphone() {
        banktype = iv_bankname.getText().toString();
        subBranch = iv_subbranch.getText().toString();
        cartName = iv_cartname.getText().toString();
        cartNumber = iv_cartnumber.getText().toString();
        if (banktype == null||banktype.equals("")){
            ToastUtils.ToastShort(mContext,"请填写银行名称");
        }else if (subBranch == null||subBranch.equals("")){
            ToastUtils.ToastShort(mContext,"请填写支行名称");
        }else if (cartName == null||cartName.equals("")){
            ToastUtils.ToastShort(mContext,"请填写账户名称");
        }else if (cartNumber == null||cartNumber.equals("")){
            ToastUtils.ToastShort(mContext,"请填写银行卡号");
        }else {
            if (NetBaseUtils.isConnnected(mContext)) {

                user.readData(mContext);
                new UserRequest(mContext,handler).AddBankCard(user.getUserId(),banktype,subBranch,cartName,cartNumber,ADD_KEY);
            }else{
                ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
            }

        }
    }


}
