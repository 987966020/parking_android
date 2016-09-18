package hbie.vip.parking.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.utils.HeadPicture;
import hbie.vip.parking.utils.IntentUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/10.
 */

public class RegisterForNameActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_next, btn_man, btn_woman;
    private TextView tv_service;//, tv_city
    private EditText et_name;
    private ImageView iv_head;
    private RelativeLayout back;
    private String head = null;
    private String gender = null;
    private String cityId = null;
    private Context mContext;
    private UserInfo user;
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "newpic";
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_name);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.relativeLayout_register_name_back);
        back.setOnClickListener(this);
        iv_head = (ImageView) findViewById(R.id.iv_resister_name_head);
        iv_head.setOnClickListener(this);
        btn_next = (Button) findViewById(R.id.btn_register_name_next);
        btn_next.setOnClickListener(this);
        btn_man = (Button) findViewById(R.id.btn_register_name_man);
        btn_man.setOnClickListener(this);
        btn_woman = (Button) findViewById(R.id.btn_register_name_woman);
        btn_woman.setOnClickListener(this);
        tv_service = (TextView) findViewById(R.id.tv_register_name_service);
        tv_service.setOnClickListener(this);
//        tv_city = (TextView) findViewById(R.id.tv_register_name_city);
//        tv_city.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_register_name);
        btn_man.setSelected(true);
        gender = "1";
        user = new UserInfo();
        user.readData(mContext);
        if (user.getUserSex().equals("0")) {
            btn_man.setSelected(false);
            btn_woman.setSelected(true);
        } else if (user.getUserSex().equals("1")) {
            btn_man.setSelected(true);
            btn_woman.setSelected(false);
        }
        if (!user.getUserName().equals("")) {
            et_name.setText(user.getUserName());
        }
        // 切换后将EditText光标置于末尾
        CharSequence charSequence = et_name.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
//        if (!user.getUserCity().equals("")) {
//            tv_city.setText(user.getUserCity());
//        }
        if (!user.getUserImg().equals("")) {
            new HeadPicture().getHeadPicture(iv_head);
            head = user.getUserImg();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayout_register_name_back:
                finish();
                break;
            case R.id.iv_resister_name_head:
                ShowPickDialog();// 点击更换头像
                break;
            case R.id.btn_register_name_next:
                btn_next.setEnabled(false);
                if (isCan()) {
                    IntentUtils.getIntent(RegisterForNameActivity.this, RegisterForVerificationActivity.class);
                }
                btn_next.setEnabled(true);
                break;
//            case R.id.tv_register_name_city:
////                Intent intent = new Intent();
////                intent.setClass(mContext, SetPersonalCityIdActivity.class);
////                startActivityForResult(intent, 4);
//                break;
            case R.id.btn_register_name_man:
                btn_man.setSelected(true);
                btn_woman.setSelected(false);
                gender = "1";
                break;
            case R.id.btn_register_name_woman:
                btn_man.setSelected(false);
                btn_woman.setSelected(true);
                gender = "0";
                break;
            case R.id.tv_register_name_service:
//                IntentUtils.getIntent(RegisterForNameActivity.this, CloakingServiceActivity.class);
                break;
        }
    }

    private boolean isCan() {
        if (head == null) {
            ToastUtils.ToastShort(mContext, "请设置头像！");
            return false;
        }
        if (et_name.getText().length() <= 0) {
            ToastUtils.ToastShort(mContext, "请填写姓名！");
            return false;
        }
//        if (tv_city.getText().length() <= 0) {
//            ToastUtils.ToastShort(mContext, "请设置城市！");
//            return false;
//        }
        return true;
    }

    private void saveData() {
//        user.setUserCity(tv_city.getText().toString());
//        user.setUserCityId(cityId);
        user.setUserSex(gender);
        user.setUserName(et_name.getText().toString());
        user.setUserImg(head);
        user.writeData(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
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
//                case 4:
//                    if (data != null) {
//                        cityId = data.getStringExtra("cityId");
//                        tv_city.setText(data.getStringExtra("city"));
//                        Toast.makeText(getApplicationContext(), data.getStringExtra("city"), Toast.LENGTH_SHORT).show();
//                    }
//                    break;
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
