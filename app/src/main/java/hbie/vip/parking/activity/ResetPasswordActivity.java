package hbie.vip.parking.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.main.LoginActivity;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * 修改密码
 *
 * @author Administrator
 *
 */
public class ResetPasswordActivity extends Activity implements View.OnClickListener {
    private static final int KEYONE = 2;
    private Button btn_submit;
    private WaveView back;
    private EditText et_pwd, et_pwd_affirm,et_new_pwd;
    private static final int KEY = 1;
    private Context mContext;
    private UserInfo user;
    private ProgressDialog dialog;
    private String updatetype,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password);
        ExitApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        updatetype = intent.getStringExtra("updatepwd");
        code = intent.getStringExtra("code");
        mContext = this;
        user = new UserInfo(mContext);
        initView();
    }

    // 初始化
    private void initView() {

        back = (WaveView) findViewById(R.id.relativeLayout_reset_password_back);
        back.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_reset_password_submit);
        btn_submit.setOnClickListener(this);
        et_pwd = (EditText) findViewById(R.id.et_reset_password);//新密码
        et_pwd_affirm = (EditText) findViewById(R.id.et_reset_password_affirm);//重复新密码
        et_new_pwd = (EditText) findViewById(R.id.et_new_password_affirm);//原始密码
        // 监听多个输入框
        user = new UserInfo();
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        if ("1".equals(updatetype)){
            et_new_pwd.setVisibility(View.GONE);
        }else if ("2".equals(updatetype)){
            et_new_pwd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_password_submit:
                if ("1".equals(updatetype)&&!"".equals(code)){
                    if (isCanDoone()) {
                        String pwd = et_pwd_affirm.getText().toString();
                        dialog.setMessage("正在修改...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();
                        if (NetBaseUtils.isConnnected(mContext)) {
                            new UserRequest(mContext, handler).forgetpwd(code, pwd, KEYONE);
                        } else {
                            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                        }
                    }
                }else if ("2".equals(updatetype)){
                    if (isCanDo()) {
                        String pwd = et_pwd_affirm.getText().toString();
                        dialog.setMessage("正在修改...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();
                        String newpwd = et_new_pwd.getText().toString();
                        if (NetBaseUtils.isConnnected(mContext)) {
                            new UserRequest(mContext, handler).updateUserPassWordtwo(newpwd, pwd, KEY);
                        }else{
                            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                        }
                    }

                }
                break;
            case R.id.relativeLayout_reset_password_back:
                finish();
                break;
        }
    }

    private Boolean isCanDo() {
        String newpwd = et_new_pwd.getText().toString();
        String pwd=et_pwd.getText().toString();
        String pwd_affirm=et_pwd_affirm.getText().toString();
        if (newpwd.isEmpty()) {
            ToastUtils.ToastLong(mContext, "请填写原始密码");
            return false;
        }
        if (pwd.isEmpty()) {
            ToastUtils.ToastLong(mContext, "请填写新密码");
            return false;
        }
        if (pwd_affirm.isEmpty()) {
            ToastUtils.ToastLong(mContext, "请再次填写新密码！");
            return false;
        }
        if (!pwd_affirm.equals(pwd)) {
            ToastUtils.ToastLong(mContext, "两处密码不相同！");
            return false;
        }
        return true;
    }
    private Boolean isCanDoone() {
        String pwd=et_pwd.getText().toString();
        String pwd_affirm=et_pwd_affirm.getText().toString();

        if (pwd.isEmpty()) {
            ToastUtils.ToastLong(mContext, "请填写新密码");
            return false;
        }
        if (pwd_affirm.isEmpty()) {
            ToastUtils.ToastLong(mContext, "请再次填写新密码！");
            return false;
        }
        if (!pwd_affirm.equals(pwd)) {
            ToastUtils.ToastLong(mContext, "两处密码不相同！");
            return false;
        }
        return true;
    }
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY:
                    if (msg.obj != null) {
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("status");
                            LogUtils.i("UserData", "---->" + json.toString());
                            if (state.equals("success")) {
                                user.setUserPassword(et_pwd_affirm.getText().toString());
                                user.writeData(mContext);
                                dialog.setMessage("修改成功,请重新登录！");
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
                                user.clearDataExceptPhone(mContext);
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);
                                ResetPasswordActivity.this.finish();
                            }else{
                                ToastUtils.ToastLong(mContext, "您输入的原始密码不正确");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case KEYONE:
                    if (msg.obj != null) {
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("status");
                            LogUtils.i("UserData", "---->" + json.toString());
                            if (state.equals("success")) {
                                user.setUserPassword(et_pwd_affirm.getText().toString());
                                user.writeData(mContext);
                                dialog.setMessage("修改成功！");
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
                                finish();
                            }else{
                                dialog.setMessage("修改失败！请与服务商联络");
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        };
    };
}