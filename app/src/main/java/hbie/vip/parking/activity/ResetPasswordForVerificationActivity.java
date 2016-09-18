package hbie.vip.parking.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
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
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.IntentUtils;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.RegexUtil;
import hbie.vip.parking.utils.ToastUtils;

/**
 * 验证
 *
 * @author Administrator
 *
 */
public class ResetPasswordForVerificationActivity extends Activity implements View.OnClickListener {
    private Button btn_next;
    private TextView tv_get;
    private EditText et_phone, et_code;
    private RelativeLayout back,rl_code;
    private static final int KEY = 1;
    private static final int IS_KAY = 2;
    private static final int CODE_ONE = -9;
    private static final int CODE_TWO = -8;
    private Context mContext;
    private static String code = "";
    private int i = 60;
    private UserInfo user;
    private ProgressDialog dialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IS_KAY:
                    if (msg.obj != null) {
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("status");
                            if (state.equals("success")) {
                                getVerificationCode();
                            } else if (state.equals("error")) {
                                ToastUtils.ToastShort(mContext, "该手机号尚未注册！");
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
                                dialog.setMessage("发送成功");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                dialog.dismiss();
                            }
                            LogUtils.i("code","-------->"+code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CODE_ONE:
                    tv_get.setText("重发(" + i + ")");
                    break;
                case CODE_TWO:
                    tv_get.setText("重新发送");
                    tv_get.setClickable(true);
                    i = 60;
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password_verification);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.relativeLayout_reset_password_verification_back);
        back.setOnClickListener(this);
        rl_code=(RelativeLayout) findViewById(R.id.relativeLayout_reset_password_verification_code);
        rl_code.setOnClickListener(this);
        btn_next = (Button) findViewById(R.id.btn_reset_password_verification_next);
        btn_next.setOnClickListener(this);
        tv_get = (TextView) findViewById(R.id.tv_reset_password_verification_get);
        tv_get.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_reset_password_verification_phone);
        et_code = (EditText) findViewById(R.id.et_reset_password_verification_code);
        dialog=new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        user = new UserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user.readData(mContext);
        if (!user.getUserPhone().equals("")) {
            et_phone.setText(user.getUserPhone());
        }
//        code=user.getUserCode();
        CharSequence charSequence = et_phone.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_password_verification_next:
                if (isCanNext()) {
//                    code="";
//                    et_code.setText("");
//                    IntentUtils.getIntent(ResetPasswordForVerificationActivity.this, ResetPasswordActivity.class);
                    Intent intent = new Intent(ResetPasswordForVerificationActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("updatepwd","1");
                    intent.putExtra("code",code);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.relativeLayout_reset_password_verification_code:
                ToastUtils.ToastShort(mContext, "目前只支持中国手机号用户！");
                break;
            case R.id.relativeLayout_reset_password_verification_back:
                finish();
                break;
            case R.id.tv_reset_password_verification_get:
                if (NetBaseUtils.isConnnected(mContext)) {
                    if (isCanGetCode()) {
                        et_code.setText("");
                        et_code.clearFocus();
                        new UserRequest(mContext, handler).isCanRegister(getPhoneNumber(), IS_KAY);
                    }
                }else{
                    ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private boolean isCanNext() {
        if (code == null || code.length() <= 0) {
            ToastUtils.ToastShort(mContext, "请先获取验证码！");
            return false;
        }
        if (getCoding().isEmpty()) {
            et_code.setError(Html.fromHtml("<font color=#E10979>请输入验证码！</font>"));
            return false;
        }
        if (!code.equals(getCoding())) {
            et_code.setError(Html.fromHtml("<font color=#E10979>验证码错误！</font>"));
            return false;
        }
        return true;
    }

    private void saveData() {
        user.setUserPhone(et_phone.getText().toString());
        user.setUserCode(code);
        user.writeData(mContext);
    }

    private Boolean isCanGetCode() {
        if (!RegexUtil.checkMobile(getPhoneNumber())) {
            ToastUtils.ToastShort(mContext, "请正确填写手机号！");
            return false;
        }
        return true;
    }

    private String getCoding() {
        return et_code.getText().toString();
    }

    private void getVerificationCode() {
        tv_get.setClickable(false);
        tv_get.setText("重发(" + i + ")");
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
        dialog.setMessage("正在发送验证码...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (NetBaseUtils.isConnnected(mContext)) {
            new UserRequest(mContext, handler).getVerficationCode(getPhoneNumber(), KEY);
        }else{
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
        }
    }
    private String getPhoneNumber() {
        return et_phone.getText().toString();
    }
}