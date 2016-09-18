package hbie.vip.parking.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.car.Car;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/13.
 */
public class AddCarActivity extends BaseActivity implements View.OnClickListener {
    private static final int ADD_CURRENTKEY =1 ;
    private static final int ADD_NOMALKEY = 2;
    private RelativeLayout back;
    private ProgressDialog dialog;
    private Context mContext;
    private String carId,addBrand,addNumber,addType,addEngine,editTypetext;
    private EditText editBrand,editNumber,editEngine;
    private Spinner editType;
    private Button btnSave;
    private static final int ADD_KEY=1;
    private static final int SET_CURRENT_CAR_KEY=3;
    private UserInfo user;
    private String baotao;
    private List<String> spinnerlist;
    private ArrayAdapter spinneradapter;
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_CURRENTKEY:
                    String data = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(data);
                        String state=json.getString("status");
                        if (state.equals("success")) {
                            carId = json.getString("data");
                            if (NetBaseUtils.isConnnected(mContext)) {
                                new UserRequest(mContext, handler).updateMemberCurrentCar(user.getUserId(),addNumber.toUpperCase(), SET_CURRENT_CAR_KEY);

                            }
                        }else{
                            ToastUtils.ToastShort(mContext,"已添加过此车辆");
                        }
//                        json.getString("data")
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ADD_NOMALKEY:
                    String datanoml = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(datanoml);
                        String state=json.getString("status");
                        if (state.equals("success")) {
                            carId = json.getString("data");
                            Car.CarData carData = new Car.CarData();
                            carData.setId(carId);
                            carData.setIscurrentcar("0");
                            carData.setCarnumber(addNumber.toUpperCase());
                            carData.setBrand(addBrand);
                            carData.setCartype(addType);
                            carData.setEnginenumber(addEngine);
                            ToastUtils.ToastShort(mContext, "提交成功！");
                            AddCarActivity.this.finish();
                        }else{
                            //可能有未交费订单，需要调用查询这辆车的未交费信息接口
                            ToastUtils.ToastShort(mContext, "已经添加过该车辆");
//                            ToastUtils.ToastShort(mContext, json.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SET_CURRENT_CAR_KEY:
                    String cardata = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(cardata);
                        String state=json.getString("status");
                        if (state.equals("success")) {
                            user.setCurrentcar(addNumber);
                            user.setUserCurrentCarId(carId);
                            user.setUserCurrentCarType(addType);
                            user.setUserCurrentCarEnginenumber(addEngine);
                            user.setUserCurrentCarBrand(addBrand);
                            user.writeData(mContext);
                            ToastUtils.ToastShort(mContext, "提交成功！");
                            AddCarActivity.this.finish();
                        }else{
                            ToastUtils.ToastShort(mContext, "当前驾驶车辆已经存在");
//                            ToastUtils.ToastShort(mContext, json.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_car_data);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo(mContext);
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        initView();
    }
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.relativeLayout_editoril_material_back);
        back.setOnClickListener(this);
        editBrand =(EditText)findViewById(R.id.ed_add_car_brand_text);
        editNumber =(EditText)findViewById(R.id.et_add_car_number_text);
        editType =(Spinner)findViewById(R.id.et_add_car_type_text);
        editEngine =(EditText)findViewById(R.id.et_add_car_engine_text);
        btnSave=(Button)findViewById(R.id.btn_save_submit);
        btnSave.setOnClickListener(this);
        spinnerlist = new ArrayList<>();
        spinnerlist.add("小型车");
        spinnerlist.add("大型车");
        spinnerlist.add("其他型车");
        spinneradapter = new ArrayAdapter<String>(mContext,R.layout.spinner_layout_ymd,spinnerlist);
        spinneradapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        editType.setAdapter(spinneradapter);
        editType.setSelection(0);
        editTypetext = spinnerlist.get(0);
        editType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editTypetext = spinnerlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout_editoril_material_back:
                finish();
                break;
            case R.id.btn_save_submit:
                getSubmitPhone();
//                finish();
                break;
        }
    }
    /**
     * 判断数据 向服务器请求
     */
    private void getSubmitPhone() {

        addBrand=editBrand.getText().toString().toUpperCase();
        addNumber=editNumber.getText().toString();
//        addType=editType.getText().toString();
        addEngine=editEngine.getText().toString();
        if (addNumber == null || addNumber.equals("")) {
//            editNumber.setError(Html.fromHtml("<font color=#E10979>请输入地址！</font>"));
            Toast.makeText(getApplicationContext(), "请填写车牌号！", Toast.LENGTH_SHORT).show();
        }else if(addBrand ==null || addBrand.equals("")){
            ToastUtils.ToastShort(mContext, "请填写品牌");
        }else if(addEngine ==null || addEngine.equals("")){
            ToastUtils.ToastShort(mContext, "请填写发动机号");
        }
        else {
            // 调用添加车辆信息接口


//            dialog = new ProgressDialog(AddCarActivity.this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.setMessage("正在提交....");
//            dialog.show();
//            dialog.dismiss();
//            if (user.getCurrentcar() == null||"".equals(user.getCurrentcar())){
//            }else{
//                new UserRequest(mContext, handler).addCar(user.getUserId(), addNumber.toUpperCase(), addBrand, addType, addEngine, ADD_KEY);
//            }
              new AlertDialog.Builder(AddCarActivity.this).setTitle("系统消息").setMessage("是否设置当前车辆为驾驶车辆")
                      .setPositiveButton("是",new DialogInterface.OnClickListener(){

                          @Override
                          public void onClick(DialogInterface dialogone, int which) {
                              if (NetBaseUtils.isConnnected(mContext)){
                                  dialog.setMessage("正在提交...");
                                  dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                  dialog.show();
                                  new UserRequest(mContext, handler).addCar(user.getUserId(), addNumber, addBrand, editTypetext, addEngine, ADD_CURRENTKEY);
                              }else{
                                  ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                              }
                                                        }
                      }).setNeutralButton("否", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogone, int which) {
                              if (NetBaseUtils.isConnnected(mContext)){
                                  dialog.setMessage("正在提交...");
                                  dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                  dialog.show();
                                  new UserRequest(mContext, handler).addCar(user.getUserId(), addNumber, addBrand, editTypetext, addEngine, ADD_NOMALKEY);
                              }else{
                                  ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                      }
                  }
              }).create().show();


        }
    }

}
