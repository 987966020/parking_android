package hbie.vip.parking.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import hbie.vip.parking.MainActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.net.UserRequest;
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
public class RegisterForVerificationActivity extends Activity implements View.OnClickListener {
    private Button btn_next, btn_man, btn_woman;
    private TextView tv_service;//, tv_city
    private EditText et_name, et_pwd, et_pwd_affirm;
    private ImageView iv_head;
    private String head = null;
    private String gender = "0";
    private String cityId = null;
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "newpic";

    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    private TextView tv_get;
    private EditText et_phone, et_code,et_address,et_cardid;
    private RelativeLayout back;
    private static final int KEY = 1;
    private static final int IS_KAY = 2;
    private static final int UPKEY=3;
    private static final int REGIST_KEY=8;
    private static final  int UPDATE_AVATAR_KEY=4;
    private static final int UP_LOAD_AVATAR_KEY=5;
    private static final int CODE_ONE = -9;
    private static final int CODE_TWO = -8;
    private Context mContext;
    private static String avatar="",phone="",password="",cardid="",address="";
    private static String code = "";
    private int i = 60;
    private UserInfo user;
    private ProgressDialog dialog;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPKEY:
                    if (msg.obj != null) {
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("status");
                            LogUtils.d("Register---",result);
                            if (state.equals("success")) {
                                dialog.setMessage("正在保存个人信息...");
                                String avatar = json.getString("data");
                                user.setUserImg(avatar);
                                user.writeData(mContext);
                                String pwd = et_pwd_affirm.getText().toString();
                                if (NetBaseUtils.isConnnected(mContext)) {
                                    new UserRequest(mContext, handler).register(et_phone.getText().toString(),pwd,code,gender,avatar,et_name.getText().toString(),et_address.getText().toString(),et_cardid.getText().toString(),KEY);
                                }else{
                                    ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                                }
                            }
                            else{
                                ToastUtils.ToastShort(mContext,"网络问题"+state );
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
                                dialog.dismiss();
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
                case REGIST_KEY:
                    if (msg.obj != null) {
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("status");
                            LogUtils.i("userdata","-------->"+state);
                            if (state.equals("success")) {
                                dialog.setMessage("注册成功");
                                String userIdTemp = json.getString("data");
                                user.setUserId(userIdTemp);
                                user.writeData(mContext);
                                // // 进入主页面
                                Intent intent = new Intent(RegisterForVerificationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }else{
                                dialog.setMessage(json.getString("data"));
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
                                ToastUtils.ToastShort(mContext, "该手机号注册不成功！");
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
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
                                LogUtils.i("codecode","--------->"+code);
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
        setContentView(R.layout.activity_register_verification);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.relativeLayout_register_verification_back);
        back.setOnClickListener(this);
        et_pwd = (EditText) findViewById(R.id.et_register_set_password);
        et_pwd_affirm = (EditText) findViewById(R.id.et_register_set_password_affirm);
        btn_next = (Button) findViewById(R.id.btn_register_verification_next);
        btn_next.setOnClickListener(this);
        tv_get = (TextView) findViewById(R.id.tv_register_verification_get);
        tv_get.setOnClickListener(this);
        et_name=(EditText)findViewById(R.id.et_register_set_name);
        et_phone = (EditText) findViewById(R.id.et_register_verification_phone);
        et_code = (EditText) findViewById(R.id.et_register_verification_code);
        et_address=(EditText)findViewById(R.id.et_register_set_address);
        et_cardid=(EditText)findViewById(R.id.et_register_set_carid);
        user = new UserInfo();
        iv_head = (ImageView) findViewById(R.id.iv_resister_name_head);
        iv_head.setOnClickListener(this);
        btn_man = (Button) findViewById(R.id.btn_register_name_man);
        btn_man.setOnClickListener(this);
        btn_woman = (Button) findViewById(R.id.btn_register_name_woman);
        btn_woman.setOnClickListener(this);
        dialog=new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
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
            case R.id.iv_resister_name_head:
                ShowPickDialog();// 点击更换头像
                break;
            case R.id.btn_register_name_man:
                btn_man.setSelected(true);
                btn_woman.setSelected(false);
                if((head==null)||(head.isEmpty())) {
                    iv_head.setImageResource(R.mipmap.avatar_man);
                    avatar = "avatar_man.png";
                    user.setUserImg(avatar);
                }
                gender = "1";
                break;
            case R.id.btn_register_name_woman:
                btn_man.setSelected(false);
                btn_woman.setSelected(true);
                if((head==null)||(head.isEmpty())) {
                    iv_head.setImageResource(R.mipmap.avatar_woman);
                    avatar = "avatar_woman.png";
                    user.setUserImg(avatar);
                }
                gender = "0";
                break;
            case R.id.btn_register_verification_next:
                if (isCanNext()) {// TODO
//                    code="";
                    dialog.setMessage("正在注册...");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.show();
                    user.readData(mContext);
                    if (NetBaseUtils.isConnnected(mContext)) {
                        if((head==null)||(head.isEmpty())) {
//                            String avatar = "avatar_man.png";
                            user.setUserImg(avatar);
                            user.writeData(mContext);
                            String pwd = et_pwd_affirm.getText().toString();
                            LogUtils.i("pwd","-------->"+pwd);
                            new UserRequest(mContext, handler).register(et_phone.getText().toString(),pwd,code,gender,avatar,et_name.getText().toString(),et_address.getText().toString(),et_cardid.getText().toString(),REGIST_KEY);
                        }else {
                            new UserRequest(mContext, handler).updateUserImg(head, UPKEY);
                        }
                    }else{
                        ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                    }
//                    IntentUtils.getIntent(RegisterForVerificationActivity.this, RegisterForSetPasswordActivity.class);
                }
                break;
            case R.id.relativeLayout_register_verification_back:
                finish();
                break;
            case R.id.tv_register_verification_get:
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
//    @Override
//    protected void onPause() {
//        super.onPause();
//        saveData();
//    }
    private boolean isCanNext() {
        if (et_name.getText().length() <= 0) {
            ToastUtils.ToastShort(mContext, "请填写姓名！");
            return false;
        }
        if (et_cardid.getText().length() <= 0) {
            ToastUtils.ToastShort(mContext, "请填写身份证号！");
            return false;
        }
        if (et_address.getText().length() <= 0) {
            ToastUtils.ToastShort(mContext, "请填写地址！");
            return false;
        }
        if (code == null || code.length() <= 0) {
            LogUtils.i("code---","========>"+code);
            ToastUtils.ToastShort(mContext, "请获取验证码！");
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
        String pwd=et_pwd.getText().toString();
        String pwd_affirm=et_pwd_affirm.getText().toString();
        if (pwd.isEmpty()||pwd_affirm.isEmpty()) {
            ToastUtils.ToastLong(mContext, "请将两处密码填写完整！");
            return false;
        }
        if (!pwd_affirm.equals(pwd)) {
            ToastUtils.ToastLong(mContext, "两处密码不相同！");
            return false;
        }
        saveData();
        return true;
    }

    private void saveData() {
        LogUtils.d("11111",gender);
        LogUtils.d("------et_name",et_name.getText().toString());
        LogUtils.d("-----et_phone",et_phone.getText().toString());
        LogUtils.d("------et_address",et_address.getText().toString());
        LogUtils.d("------et_cardid",et_cardid.getText().toString());
        user.setUserSex(gender);
        user.setUserName(et_name.getText().toString());
        user.setUserPhone(et_phone.getText().toString());
        user.setUserCode(code);
        user.setuserAddr(et_address.getText().toString());
        user.setUserCardid(et_cardid.getText().toString());
        user.writeData(mContext);
        user.readData(mContext);
        LogUtils.d("222222",gender);
        LogUtils.d("------et_name",user.getUserName());
        LogUtils.d("-----et_phone",user.getUserPhone());
        LogUtils.d("------et_address",user.getUserAddr());
        LogUtils.d("------et_cardid",user.getUserCardid());
        LogUtils.d("------userdode",user.getUserCode());


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
        if (NetBaseUtils.isConnnected(mContext)) {
            new UserRequest(mContext, handler).getVerficationCode(getPhoneNumber(), KEY);
        }else{
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
        }

    }

    private String getPhoneNumber() {
        return et_phone.getText().toString();
    }

    protected void ShowPickDialog() {
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, PHOTO_REQUEST_ALBUM);

            }
        }).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file = new File(path, "newpic.jpg");
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }

                startActivityForResult(intentFromCapture, PHOTO_REQUEST_CAMERA);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:// 相册
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "newpic.jpg");
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PHOTO_REQUEST_ALBUM:// 图库
                    startPhotoZoom(data.getData());
                    break;

                case PHOTO_REQUEST_CUT: // 图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            iv_head.setImageDrawable(drawable);
            picname = "avatar"+user.getUserId()+String.valueOf(new Date().getTime());
            storeImageToSDCARD(photo, picname, filepath);
//            if (NetBaseUtils.isConnnected(mContext)) {
//                new UserRequest(mContext, handler).updateUserImg(head, KEY);
//            }else{
//                ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
//            }
        }
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     * */
    public void storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imagefile = new File(file, ImageName + ".jpg");
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            head = imagefile.getPath();
            user.setUserImg(head);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
