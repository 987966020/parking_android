package hbie.vip.parking.activity.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.main.LoginActivity;
import hbie.vip.parking.net.NetUtil;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.RegexUtil;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/13.
 */
public class UpdatePersonalPhoneActivity extends Activity implements View.OnClickListener {
    private RelativeLayout back;
    private EditText et_phone, et_code,old_pass;
    private TextView tv_currentPhone, tv_getCode;
    private Button btn_submit;
    private ProgressDialog dialog;
    private Context mContext;
    private static final int KEY = 1;
    private static final int UPDATE_KEY=3;
    private static final int IS_KAY = 2;
    private static final int CODE_ONE = -9;
    private static final int CODE_TWO = -8;
    private static String code = "-1";
    private SharedPreferences sp;
    private int i = 60;
    private UserInfo user;
    private String updatePhone;
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_KEY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            dialog.dismiss();
                            String state = json.getString("status");
                            if (state.equals("success")) {

                                if ("密码错误".equals(json.getString("data"))){
                                    ToastUtils.ToastShort(mContext, json.getString("data"));
                                }else{
                                    ToastUtils.ToastShort(mContext, "提交成功！");
                                    SharedPreferences.Editor e=sp.edit();
                                    e.clear();
                                    e.commit();
                                    user.writeData(mContext);
                                    user.clearPhone(mContext);
                                    user.clearDataExceptPhone(mContext);
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    startActivity(intent);
                                    ExitApplication.getInstance().exit();
                                }
                            } else {
                                ToastUtils.ToastShort(mContext, "提交失败！");
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    break;
                case IS_KAY:
                    if (msg.obj != null) {
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("status");
                            LogUtils.i("UserData", "---->" + json.toString());
                            if (state.equals("success")) {
                                ToastUtils.ToastShort(mContext, "该手机号已被注册！");
                            } else {
                                getVerificationCode();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case KEY:
                    if (msg.obj != null) {
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("status");
                            LogUtils.i("UserData", "---->" + json.toString());
                            if (state.equals("success")) {
                                code = json.getString("data");
                                Log.i("<=====code=====>",code);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CODE_ONE:
                    tv_getCode.setText("重发(" + i + ")");
                    break;
                case CODE_TWO:
                    tv_getCode.setText("重新发送");
                    tv_getCode.setClickable(true);
                    i = 60;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_personal_phone);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        initView();

    }
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.relativeLayout_update_personal_phone_back);
        back.setOnClickListener(this);
        old_pass = (EditText) findViewById(R.id.et_update_phone_old_pass);
        et_phone = (EditText) findViewById(R.id.et_update_personal_phone);
        et_code = (EditText) findViewById(R.id.et_update_personal_phone_code);
        tv_currentPhone = (TextView) findViewById(R.id.tv_update_personal_phone_current);
        tv_getCode = (TextView) findViewById(R.id.tv_update_personal_phone_verification_get);
        tv_getCode.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_update_personal_phone_submit);
        btn_submit.setOnClickListener(this);
        user = new UserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user.readData(mContext);
        tv_currentPhone.setText(user.getUserPhone());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayout_update_personal_phone_back:
                finish();// 返回上一层
                break;
            case R.id.tv_update_personal_phone_verification_get:
                if (isCanGetCode()) {
                    if (NetUtil.isConnnected(mContext) == true) {
                        new UserRequest(mContext, handler).isCanRegister(getPhoneNumber(), IS_KAY);
                    } else {
                        ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                    }
                }
                break;
            case R.id.btn_update_personal_phone_submit:
                if (isCanNext()) {
                    getSubmitPhone();
                }
                break;
        }
    }
    private void getVerificationCode() {
        tv_getCode.setClickable(false);
        tv_getCode.setText("重发(" + i + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(CODE_ONE);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(CODE_TWO);
            }
        }).start();
        new UserRequest(mContext, handler).getVerficationCode(getPhoneNumber(), KEY);
    }

    private boolean isCanNext() {
        if (!code.equals(getCoding())) {
            LogUtils.i("ddd",getCoding());
            LogUtils.i("<=====code======>",code);
            et_code.setError("验证码不对");
            return false;
        }
        return true;
    }

    private Boolean isCanGetCode() {
        if (!RegexUtil.checkMobile(getPhoneNumber())) {
            ToastUtils.ToastShort(mContext, "请正确填写手机号！");
            return false;
        }
        return true;
    }

    private String getPhoneNumber() {
        return et_phone.getText().toString();
    }

    private String getCoding() {
        return et_code.getText().toString();
    }
    /**
     * 判断数据 向服务器请求
     */
    private void getSubmitPhone() {

        updatePhone = et_phone.getText().toString();
        if (updatePhone == null || updatePhone.equals("")) {
            et_phone.setError("亲！请填正确的手机号");
        } else if (old_pass.getText().toString() == null || "".equals(old_pass.getText().toString())){
            ToastUtils.ToastShort(mContext, "请填写原始密码");
        }else {
            et_code.setText("");
            dialog = new ProgressDialog(UpdatePersonalPhoneActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("正在提交....");
            dialog.show();
            new UserRequest(mContext, handler).updateUserPhone(user.getUserId(),updatePhone,code,old_pass.getText().toString(), UPDATE_KEY);
        }
    }
}