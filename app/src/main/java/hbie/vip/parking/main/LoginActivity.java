package hbie.vip.parking.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.MainActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.activity.RegisterForVerificationActivity;
import hbie.vip.parking.activity.ResetPasswordForVerificationActivity;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.button.RevealButton;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.IntentUtils;
import hbie.vip.parking.utils.KeyBoardUtils;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private RevealButton bt_login;
    private RevealButton tv_register, tv_forget_pwd;
    private EditText et_phone, et_password;
    private String phone = null;
    private String password = null;
    private Context mContext;
    private UserInfo user;
    private ProgressDialog dialog;
    private static final int KEY = 1;
    private static final int GET_KEY = 2;
    private final static int GET_CURRENT_CAR=3;
    private String TAG="LoginActivity.class";
    private long firstTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
//        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        UserInfo user = new UserInfo(mContext);
        user.readData(mContext);
        if (user.isLogined()) {// 已登录
            IntentUtils.getIntent(LoginActivity.this, MainActivity.class);
            finish();
        }else {
            LogUtils.i(TAG, "is no login");
            LogUtils.i(TAG,user.getUserId());
            initView();
        }
    }
    private void initView() {
        bt_login = (RevealButton) findViewById(R.id.btn_login_submit);
        tv_register = (RevealButton) findViewById(R.id.tv_login_register);
        tv_forget_pwd = (RevealButton) findViewById(R.id.tv_login_forget_pwd);
        tv_forget_pwd.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_login_phone);
        et_password = (EditText) findViewById(R.id.et_login_password);
        user = new UserInfo();
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
//        // 监听多个输入框
//        et_phone.addTextChangedListener(new TextChange());
//        et_password.addTextChangedListener(new TextChange());
        bt_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        user.readData(mContext);
        if (!user.getUserPhone().equals("")) {
            et_phone.setText(user.getUserPhone());
        }
//        // 切换后将EditText光标置于末尾
        CharSequence charSequence = et_phone.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        KeyBoardUtils.showKeyBoardByTime(et_phone, 300);
    }
    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {

            boolean Sign2 = et_phone.getText().length() > 0;
            boolean Sign3 = et_password.getText().length() > 0;

            if (Sign2 & Sign3) {
                bt_login.setTextColor(0xFFFFFFFF);
                bt_login.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                bt_login.setTextColor(0xFFD0EFC6);
                bt_login.setEnabled(false);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_submit:
                getLogin();
                break;
            case R.id.tv_login_register:// 注册
                IntentUtils.getIntent(LoginActivity.this, RegisterForVerificationActivity.class);
                break;
            case R.id.tv_login_forget_pwd:// 忘记密码
                IntentUtils.getIntent(LoginActivity.this, ResetPasswordForVerificationActivity.class);
                break;
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            LogUtils.i("UserData", "---->" + json.toString());
                            String state = json.getString("status");
                            String data = json.getString("data");
                            if (state.equals("success")) {
                                JSONObject item = new JSONObject(data);
                                String userId = item.getString("id");
                                LogUtils.i("UserId", "---->" + userId.toString());
                                user.setUserId(userId);
                                user.setUserName(item.getString("user_name"));
                                user.setUserPhone(item.getString("user_phone"));
                                user.setUserSex(item.getString("sex"));
                                user.setUserImg(item.getString("avatar"));
                                user.setuserAddr(item.getString("addr"));
//
                                user.setUserBranch(item.getString("bank_branch"));
                                LogUtils.i("UserData", "---->setUserPosition" + item.getString("addr"));
                                user.writeData(mContext);
                                user.readData(mContext);
                                LogUtils.i("UserData", "---->getUserPosition" + user.getUserAddr());
                                new UserRequest(mContext, handler).getMemberCurrentCar(user.getUserId(), GET_CURRENT_CAR);


                            } else {
                                ToastUtils.ToastShort(mContext, "账号密码错误！");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();
                                    }
                                }).start();

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    break;
                case GET_KEY:
                    if (msg.obj != null) {
                        int state = (Integer) msg.obj;
                        if (state == 1) {
//                            new GetAllFoucsKaListTask().executeOnExecutor(ThreadPoolUtils_Net.threadPool);
                        } else {
                            dialog.dismiss();
                        }
                    }
                    break;
                case GET_CURRENT_CAR:
                    String result = (String) msg.obj;
                    JSONObject json;
                    try {
                        json = new JSONObject(result);
                        LogUtils.i("UserData", "---->" + json.toString());
                        String state = json.getString("status");
                        String data = json.getString("data");
                        if (state.equals("success")) {
                            JSONObject item = new JSONObject(data);
                            user.setCurrentcar(item.getString("car_number"));
                            user.setUserCurrentCarBrand(item.getString("brand"));
                            user.setUserCurrentCarEnginenumber(item.getString("engine_number"));
                            user.setUserCurrentCarId(item.getString("id"));
                            user.setUserCurrentCarType(item.getString("car_type"));
                            user.writeData(mContext);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    break;
            }
        };
    };
    private void getLogin() {
        phone = et_phone.getText().toString();
        password = et_password.getText().toString();
        user.setUserPhone(phone);
        user.setUserPassword(password);
        user.writeData(mContext);
        dialog.setMessage("正在登录...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        if (NetBaseUtils.isConnnected(mContext)) {
            new UserRequest(mContext, handler).Login(phone, password, KEY);
        }else{
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_LONG).show();  //提示消息
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
