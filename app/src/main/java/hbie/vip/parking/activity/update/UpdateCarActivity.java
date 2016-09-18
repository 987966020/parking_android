package hbie.vip.parking.activity.update;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.adapter.CarinfoListAdapter;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.car.Car;
import hbie.vip.parking.button.RevealButton;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

import static hbie.vip.parking.bean.car.Car.CarData;

/**
 * Created by mac on 16/5/16.
 */
public class UpdateCarActivity extends BaseActivity implements View.OnClickListener {
    private static final int SET_NOMAL_CAR_KEY = 4;
    private WaveView back;
    private ProgressDialog dialog;
    private Context mContext;
    private String addBrand,addNumber,addType,addEngine,un_editBrand,un_editNumber,un_editType, un_editEngine,editTypetext;
    private EditText editBrand,editNumber,editEngine;
    private Spinner editType;
    private RevealButton btnSave;
    private static final int ADD_KEY=1;
    private static final int SET_CURRENT_CAR_KEY=2;
    private static final  int UN_BIND_CAR_KEY=3;
    private UserInfo user;
    private Bundle bundle;
    private CarData carinfo = null;
    PopupMenu popupMenu;
    Menu menu;
    private int changecar = 1;
    private ArrayList<CarData> carList;
    private CarinfoListAdapter carListAdapter;
    private List<String> spinnerlist;
    private ArrayAdapter<String> spinneradapter;
    private TextView textViewCustom;
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_KEY:
                    String data = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(data);
                        String state=json.getString("status");
                        if (state.equals("success")) {
                            ToastUtils.ToastShort(mContext, "提交成功！");
                            dialog.dismiss();
                            finish();
                        }else{
                            dialog.dismiss();
                            ToastUtils.ToastShort(mContext, json.getString("data"));
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
                            user.setCurrentcar(carinfo.getCarnumber());
                            user.setUserCurrentCarType(carinfo.getCartype());
                            user.setUserCurrentCarEnginenumber(carinfo.getEnginenumber());
                            user.setUserCurrentCarBrand(carinfo.getBrand());
                            user.writeData(mContext);
                            ToastUtils.ToastShort(mContext, "提交成功！"+user.getCurrentcar());
                            dialog.dismiss();
                            finish();
                        }else{
                            dialog.dismiss();
                            ToastUtils.ToastShort(mContext, json.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SET_NOMAL_CAR_KEY:
                    String cardnomalata = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(cardnomalata);
                        String state=json.getString("status");
                        if (state.equals("success")) {
                            Car.CarData carData = new Car.CarData();
                            carData.setId(user.getUserCurrentCarId());
                            carData.setBrand(user.getUserCurrentCarBrand());
                            carData.setCarnumber(user.getCurrentcar());
                            carData.setEnginenumber(user.getUserCurrentCarEnginenumber());
                            carData.setCartype(user.getUserCurrentCarType());
                            user.clearCar(mContext);
                            dialog.dismiss();
                            finish();
                        }else{
                            dialog.dismiss();
                            ToastUtils.ToastShort(mContext, json.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case UN_BIND_CAR_KEY:
                    String undata = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(undata);
                        String state=json.getString("status");
                        if (state.equals("success")) {
                            if (un_editNumber.equals(user.getCurrentcar())){
                               user.clearCar(mContext);
                            }else{
                                ToastUtils.ToastShort(mContext, "提交成功！");
                            }
                            dialog.dismiss();
                            finish();
                        }else{
                            dialog.dismiss();
                            ToastUtils.ToastShort(mContext, json.getString("data"));
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
        setContentView(R.layout.update_add_car_data);
        ExitApplication.getInstance().addActivity(this);
//        bundle = getIntent().getExtras();
        Intent intent = this.getIntent();
        carinfo = (CarData)intent.getSerializableExtra("carinfo");
        mContext = this;
        user = new UserInfo();
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        carList = new ArrayList<Car.CarData>();
        initView();
    }
    private void initView() {
        back = (WaveView) findViewById(R.id.relativeLayout_editoril_material_back);
        back.setOnClickListener(this);
        editBrand =(EditText)findViewById(R.id.ed_add_car_brand_text);
        editNumber =(EditText)findViewById(R.id.et_add_car_number_text);
        editType =(Spinner)findViewById(R.id.et_add_car_type_text);
        editEngine =(EditText)findViewById(R.id.et_add_car_engine_text);
        btnSave=(RevealButton)findViewById(R.id.btn_save_submit);
        btnSave.setOnClickListener(this);
        user.readData(mContext);

        if(carinfo!=null){
            if("1".equals(carinfo.getIscurrentcar())){
                btnSave.setText("设为常用车辆");
            }else if ("0".equals(carinfo.getIscurrentcar())){
                btnSave.setText("设为驾驶车辆");
            }
            editBrand.setText(carinfo.getBrand());
            editEngine.setText(carinfo.getEnginenumber());
//            editType.setText(carinfo.getCartype());
            editNumber.setText(carinfo.getCarnumber());
            editBrand.setEnabled(false);
            editEngine.setEnabled(false);
            editType.setEnabled(false);
            editNumber.setEnabled(false);
            spinnerlist = new ArrayList<>();
            spinnerlist.add("小型车");
            spinnerlist.add("大型车");
            spinnerlist.add("其他型车");
            spinneradapter = new ArrayAdapter<String>(mContext,R.layout.spinner_layout_black,spinnerlist);
            spinneradapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            editType.setAdapter(spinneradapter);
            for (int i = 0;i < spinnerlist.size();i++){
                if (spinnerlist.get(i).equals(carinfo.getCartype())){
                    editType.setSelection(i);
                    editTypetext = spinnerlist.get(i);
                }
            }
        }


        popupMenu = new PopupMenu(this, findViewById(R.id.popupmenu_btn));
        menu = popupMenu.getMenu();

        MenuInflater menuInflater = getMenuInflater();
        setMenueBackground();
//        menu.findItem(R.id.setcurrent).setVisible(true);
        LogUtils.i("menu", "-------->" + carinfo.getIscurrentcar());
        if ("1".equals(carinfo.getIscurrentcar())){
        menuInflater.inflate(R.menu.popupmenunomal, menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setcurrent:
                            editBrand.setEnabled(true);
                            editEngine.setEnabled(true);
                            editType.setEnabled(true);
                            editNumber.setEnabled(false);
                            spinneradapter = new ArrayAdapter<String>(mContext,R.layout.spinner_layout_ymd,spinnerlist);
                            spinneradapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            editType.setAdapter(spinneradapter);
                        for (int i = 0;i < spinnerlist.size();i++){
                            if (spinnerlist.get(i).equals(carinfo.getCartype())){
                                editType.setSelection(i);
                                editTypetext = spinnerlist.get(i);
                            }
                        }
                        editType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                View convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_layout_black,parent,false);
                                textViewCustom= (TextView)convertView.findViewById(R.id.textViewCustom);
                                textViewCustom.setTextColor(mContext.getResources().getColor(R.color.black));
                                editTypetext = spinnerlist.get(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                            btnSave.setText("确认修改");
                        break;
                    case R.id.unbind:
                        un_editNumber = editNumber.getText().toString();
                        if (NetBaseUtils.isConnnected(mContext)) {
                            new UserRequest(mContext, handler).unBindCar(user.getUserId(), carinfo.getCarnumber(), UN_BIND_CAR_KEY);
                        }
                        break;
                    case R.id.changelicationnomal:
                        un_editNumber = editNumber.getText().toString();
                        if (!un_editNumber.equals(user.getCurrentcar())){
                        if (NetBaseUtils.isConnnected(mContext)) {
                            dialog.setMessage("正在修改...");
                            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog.show();
                            new UserRequest(mContext, handler).updateMemberCurrentCar(user.getUserId(), carinfo.getCarnumber(), SET_CURRENT_CAR_KEY);
                        }else{
                            ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                        }
                        }else{
                            if (NetBaseUtils.isConnnected(mContext)) {
                                dialog.setMessage("正在修改...");
                                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                dialog.show();
                                new UserRequest(mContext, handler).updateMemberCurrentCar(user.getUserId(), "", SET_NOMAL_CAR_KEY);
                            }else{
                                ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                            }

                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        }else{
            menuInflater.inflate(R.menu.popupmenu, menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.setcurrent:
                            editBrand.setEnabled(true);
                            editEngine.setEnabled(true);
                            editType.setEnabled(true);
                            editNumber.setEnabled(false);
                            spinneradapter = new ArrayAdapter<String>(mContext,R.layout.spinner_layout_ymd,spinnerlist);
                            spinneradapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            editType.setAdapter(spinneradapter);
                            for (int i = 0;i < spinnerlist.size();i++){
                                if (spinnerlist.get(i).equals(carinfo.getCartype())){
                                    editType.setSelection(i);
                                    editTypetext = spinnerlist.get(i);
                                }
                            }
                            editType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    View convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_layout_black,parent,false);
                                    textViewCustom= (TextView)convertView.findViewById(R.id.textViewCustom);
                                    textViewCustom.setTextColor(mContext.getResources().getColor(R.color.black));
                                    editTypetext = spinnerlist.get(position);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            btnSave.setText("确认修改");
                            break;
                        case R.id.unbind:
                            un_editNumber = editNumber.getText().toString();
                            if (NetBaseUtils.isConnnected(mContext)) {
                                dialog.setMessage("正在解除绑定...");
                                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                dialog.show();
                                new UserRequest(mContext, handler).unBindCar(user.getUserId(), carinfo.getCarnumber(), UN_BIND_CAR_KEY);
                            }
                            break;
                        case R.id.changelication:
                            un_editNumber = editNumber.getText().toString();
                            if (!un_editNumber.equals(user.getCurrentcar())){
                                if (NetBaseUtils.isConnnected(mContext)) {
                                    dialog.setMessage("正在修改...");
                                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    dialog.show();
                                    new UserRequest(mContext, handler).updateMemberCurrentCar(user.getUserId(), carinfo.getCarnumber(), SET_CURRENT_CAR_KEY);
                                }else{
                                    ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                                }
                            }else{
                                if (NetBaseUtils.isConnnected(mContext)) {
                                    dialog.setMessage("正在修改...");
                                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    dialog.show();
                                    new UserRequest(mContext, handler).updateMemberCurrentCar(user.getUserId(), "", SET_NOMAL_CAR_KEY);
                                }else{
                                    ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                                }

                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }
    /**IconMenuItemView 用于创建并控制Options menu的类，继承自基本的View。所以，我们可以使用
     * 一个LayoutInflater创建这个View并给它应用指定的背景
     */
    protected void setMenueBackground() {
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                LogUtils.i("name的值", "------>" + name);
                if (name.equalsIgnoreCase("com.android.internal.view.menu.ListMenuItemView")
                        || name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView")) {

                    try {
                        LayoutInflater newpopumenu = getLayoutInflater();
                        final View newview = newpopumenu.createView(name, null, attrs);
                        new Handler().post(new Runnable() {
                            public void run() {
//                                newview.setBackgroundColor(mContext.getResources().getColor(R.color.black));
                                if (newview instanceof TextView) {
                                    newview.setBackgroundResource(R.color.black);
                                    ((TextView) newview).setTextColor(mContext.getResources().getColor(R.color.title_color));
                                }
                            }
                        });
                        return newview;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return null;
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
                if ("设为常用车辆".equals(btnSave.getText().toString())){
                    if (NetBaseUtils.isConnnected(mContext)) {
                        dialog.setMessage("正在修改...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();
                        new UserRequest(mContext, handler).updateMemberCurrentCar(user.getUserId(), "", SET_NOMAL_CAR_KEY);
                    }else{
                        ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                    }
                }else if("设为驾驶车辆".equals(btnSave.getText().toString())){
                    if (NetBaseUtils.isConnnected(mContext)) {
                        dialog.setMessage("正在修改...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();
                        new UserRequest(mContext, handler).updateMemberCurrentCar(user.getUserId(), carinfo.getCarnumber(), SET_CURRENT_CAR_KEY);
                    }else{
                        ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                    }
                }else if ("确认修改".equals(btnSave.getText().toString())){
                    getSubmitPhone();
                }else if ("保存".equals(btnSave.getText().toString())){
                    getSubmitPhone();
                }

//                finish();
                break;
        }
    }
    // 监听事件

        public void popupmenu(View v) {
        popupMenu.show();
    }
    /**
     * 判断数据 向服务器请求
     */
    private void getSubmitPhone() {
        addBrand=editBrand.getText().toString();
        addNumber=editNumber.getText().toString();
//        addType=editType.getText().toString();
        addEngine=editEngine.getText().toString();
        if (addNumber == null || addNumber.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写车牌号！", Toast.LENGTH_SHORT).show();
        }else if(addBrand ==null || addBrand.equals("")){
            ToastUtils.ToastShort(mContext, "请填写品牌！");
        }else if(addEngine ==null || addEngine.equals("")){
            ToastUtils.ToastShort(mContext, "请填写发动机号！");
        }
        else {

            dialog = new ProgressDialog(UpdateCarActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("正在提交....");
            dialog.show();
            dialog.dismiss();
            user.readData(mContext);
            new UserRequest(mContext, handler).updateCar(user.getUserId(),carinfo.getId(), addNumber,addBrand,editTypetext,addEngine, ADD_KEY);
        }
    }
}

