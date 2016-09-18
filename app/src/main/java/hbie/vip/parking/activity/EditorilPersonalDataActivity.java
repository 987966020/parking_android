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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.update.UpdatePersonalCityIdActivity;
import hbie.vip.parking.activity.update.UpdatePersonalPhoneActivity;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.net.request.constant.NetBaseConstant;
import hbie.vip.parking.ui.RoundImageView;
import hbie.vip.parking.utils.HeadPicture;
import hbie.vip.parking.utils.IntentUtils;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

public class EditorilPersonalDataActivity extends Activity implements View.OnClickListener {
    private RelativeLayout back, name, city, company, position, phone, work;
    private RadioGroup rg_gender;
    private RadioButton rb_male, rb_female;
    private RoundImageView iv_head;
    private String headdrawable,sv_name;
    private EditText tv_name;
    private String head;
    private TextView  tv_phone, tv_position;

    private EditText editPositon;
    private Button btn_submit;
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "newpic";
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    private static final int KEY = 4;
    private static final int UPDATE_KEY=9;
    private static final int UPDATE_AVATAR_KEY=6;
    private static final int UPDATE_GENDER_KEY = 5;
    private UserInfo user;
    private Context mContext;
    private String updateGender,updateAddr,updateName,updateCardid;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editoril_personal_data);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo(mContext);
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        initView();
    }
    private void initView() {
        tv_name = (EditText) findViewById(R.id.tv_editoril_material_name);
//        tvCity = (TextView) findViewById(R.id.tv_editoril_material_city);
//        tvCompany = (TextView) findViewById(R.id.tv_editoril_material_company);
//        tv_position = (TextView) findViewById(R.id.tv_editoril_material_city);
        tv_phone = (TextView) findViewById(R.id.tv_editoril_material_phone);
        iv_head = (RoundImageView) findViewById(R.id.iv_editoril_material_head);
        iv_head.setOnClickListener(this);
        headdrawable = user.getUserImg();
        back = (RelativeLayout) findViewById(R.id.relativeLayout_editoril_material_back);
        back.setOnClickListener(this);
        phone = (RelativeLayout) findViewById(R.id.editoril_material_phone);
        phone.setOnClickListener(this);
        editPositon =(EditText)findViewById(R.id.et_update_personal_addr);
        city = (RelativeLayout) findViewById(R.id.editoril_material_city);
        city.setOnClickListener(this);
        rg_gender = (RadioGroup) findViewById(R.id.radioGroup_editoril_material_gender);
        rb_male = (RadioButton) findViewById(R.id.radio_editoril_material_gender_Male);
        rb_female = (RadioButton) findViewById(R.id.radio_editoril_material_gender_Female);
        //绑定一个匿名监听器
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup view, int arg1) {
                user.readData(mContext);
                // 获取变更后的选中项的ID
                switch (view.getCheckedRadioButtonId()) {
                    case R.id.radio_editoril_material_gender_Male:
                        LogUtils.i("Gender", "选-->" + "男");
                        updateGender="1";
                        break;
                    case R.id.radio_editoril_material_gender_Female:
                        LogUtils.i("Gender", "选-->" + "女");
                        updateGender="0";
                        break;
                    default:
                        break;
                }
            }
        });
        btn_submit = (Button) findViewById(R.id.btn_save_submit);
        btn_submit.setOnClickListener(this);
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square).cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user == null) {
            user = new UserInfo();
        }
        user.readData(mContext);
        if (!user.getUserImg().equals("")) {
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" +user.getUserImg(), iv_head, options);
        }else{
            new HeadPicture().getHeadPicture(iv_head);
        }
        if (user.getUserName().equals("")) {
            tv_name.setHint(R.string.editoril_personal_data_other_hint);
        } else {
            tv_name.setText(user.getUserName());
        }

        if (user.getUserPhone().equals("")) {
            tv_phone.setText(R.string.editoril_personal_data_other_hint);
        } else {
            tv_phone.setText(user.getUserPhone());
        }
        if (user.getUserSex().equals("0")) {
            updateGender="0";
            rb_female.setChecked(true);
        } else if (user.getUserSex().equals("1")) {
            updateGender="1";
            rb_male.setChecked(true);
        }
        if(user.getUserPhone().equals("")){
            editPositon.setText(R.string.editoril_personal_data_other_hint);
        }else{
            editPositon.setText(user.getUserAddr());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout_editoril_material_back:
                finish();
                break;
            case R.id.iv_editoril_material_head:
                ShowPickDialog();// 点击更换头像
                break;
            case R.id.editoril_material_name:
//                ToastUtils.ToastShort(mContext, "姓名不可修改！");
                break;
            case R.id.btn_save_submit:
                getSubmitPhone();
                break;
            case R.id.editoril_material_phone:
                IntentUtils.getIntent(EditorilPersonalDataActivity.this, UpdatePersonalPhoneActivity.class);
                break;
            case R.id.editoril_material_city:
                IntentUtils.getIntent(EditorilPersonalDataActivity.this, UpdatePersonalCityIdActivity.class);
                break;
            default:
                break;
        }

    }
    /**
     * 判断数据 向服务器请求
     */
    private void getSubmitPhone() {
        sv_name = tv_name.getText().toString();
        updateAddr = editPositon.getText().toString();
//        updateName = user.getUserName();
//        updateCardid = us
        if (updateAddr == null || updateAddr.equals("")) {
            editPositon.setError("亲！请填地址");
        }else if (sv_name == null || sv_name.equals("")) {
            editPositon.setError("亲！请填名称");
        }
        else {
            dialog = new ProgressDialog(EditorilPersonalDataActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("正在提交....");
            dialog.show();
            new UserRequest(mContext, handler).updateUserInfo(user.getUserId(),sv_name,updateAddr,updateGender,updateCardid, UPDATE_KEY);
        }
    }
    /**
     * 数据的显示
     */
    private void displayData() {
        user.readData(mContext);
        if (!user.getUserImg().equals("")) {
//            LogUtils.i(TAG,NetBaseConstant.NET_HOST + "/" + user.getUserImg());
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + user.getUserImg(), iv_head, options);
        } else {
            new HeadPicture().getHeadPicture(iv_head);
        }
        tv_name.setText(user.getUserName());
        tv_phone.setText(user.getUserPhone());
        editPositon.setText(user.getUserAddr());
    }
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_KEY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            dialog.dismiss();
                            String state = json.getString("status");
                            LogUtils.i("UserRequestddddddddddddddd", "--->" + json.getString("data"));

                            if (state.equals("success")) {
                                user.setUserSex(updateGender);
                                user.setuserAddr(updateAddr);
                                user.setUserName(updateName);
                                user.writeData(mContext);
                                finish();
                                ToastUtils.ToastShort(mContext, "提交成功！");
                            } else {
                                ToastUtils.ToastShort(mContext, "提交失败！");
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    break;
                case KEY:
                    String key = (String) msg.obj;
                    LogUtils.i("KEY","-------->"+key);
                    try {
                        JSONObject json = new JSONObject(key);
                        String state=json.getString("status");
                        if (state.equals("success")) {
//                            JSONObject js=json.getJSONObject("data");
                            String path=json.getString("data");
                            LogUtils.i("UserRequestddddddddddddddd", "修改用户的头像--->" + user.getUserImg());
                            LogUtils.i("UserRequestddddddddddddddd", "修改用户的头像--->" + path);
                            user.setUserImg(path);
                            user.writeData(mContext);
                            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/"+path, iv_head, options);
                            if (NetBaseUtils.isConnnected(mContext)) {
                                new UserRequest(mContext, handler).updateavatar(user.getUserId(), path, UPDATE_AVATAR_KEY);
                            }else{
                                ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                            }
                            ToastUtils.ToastShort(mContext, "提交成功！");
                        }else{
                            ToastUtils.ToastShort(mContext, json.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATE_AVATAR_KEY:
                    String data = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(data);
                        String state=json.getString("status");
                        if (state.equals("success")) {
                            ToastUtils.ToastShort(mContext, "提交成功！");
                        }else{
                            ToastUtils.ToastShort(mContext, json.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATE_GENDER_KEY:
                    String result = (String) msg.obj;
                    JSONObject json;
                    try {
                        json = new JSONObject(result);
                        String state = json.getString("status");
                        if (state == "success") {
                            user.setUserSex(updateGender);
                            user.writeData(mContext);
                            ToastUtils.ToastShort(mContext, "提交成功！");
                        } else {
                            ToastUtils.ToastShort(mContext, "提交失败！");
                        }
                        if (user.getUserSex().equals("0")) {
                            rb_female.setChecked(true);
                        } else if (user.getUserSex().equals("1")) {
                            rb_male.setChecked(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        };
    };

    protected void ShowPickDialog() {
        new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
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
            user.readData(mContext);
            LogUtils.i("head", "---------->" + head);
            picname = "avatar"+user.getUserId()+String.valueOf(new Date().getTime());
            storeImageToSDCARD(photo, picname, filepath);
            if (NetBaseUtils.isConnnected(mContext)) {
                new UserRequest(mContext, handler).updateUserImg(head,KEY);
            }else{
                ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
            }
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
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}