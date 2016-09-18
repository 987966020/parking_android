package hbie.vip.parking.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.AddCarActivity;
import hbie.vip.parking.activity.update.UpdateCarActivity;
import hbie.vip.parking.adapter.CarinfoListAdapter;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.car.Car;
import hbie.vip.parking.button.RevealButton;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/9.
 */
public class CarFragment extends Fragment {
    private static final int CHANGE_CURRENT_CAR_KEY = 2;
    private static final int ADD_NOMALKEY_TOO =3 ;
    private Activity activity;
    private PullToRefreshListView lv_comprehensive;
    private WaveView iv_AddCar;
    private WaveView iv_editor;
    private Context mContext;
    private SharedPreferences sp;
    private View viewH = null;
//    private View views = null;
    private TextView tv_car_current_number, tv_car_current_brand;
//    private RelativeLayout ly_car_info;
    /* 车辆列表 */
    private ListView lv_car;
    private CarinfoListAdapter carListAdapter;
    private TextView detail_loading;
    private ArrayList<Car.CarData> carList;
    /* 用以发送消息到handler 告知fragment需要切换 */
    private final static int GET_CAR_LIST=1;
    private ProgressDialog dialog;
    private static final int KEY = 1;
    public final static int ISPLAY = 4;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private UserInfo user;
    @SuppressWarnings("static-access")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
//        Bundle args = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        detail_loading = (TextView) view.findViewById(R.id.detail_loading);
        lv_comprehensive = (PullToRefreshListView) view.findViewById(R.id.lv_comprehensive);
        lv_comprehensive.setPullLoadEnabled(true);
        lv_comprehensive.setScrollLoadEnabled(false);
        lv_comprehensive.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                carListAdapter.notifyDataSetChanged();
                user.readData(mContext);
                if (NetBaseUtils.isConnnected(mContext)) {
                    new UserRequest(mContext, handler).GetCarList(user.getUserId(), GET_CAR_LIST);
//                    carListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                }
//                carListAdapter.notifyDataSetChanged();
                stopRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                carList.clear();
//                carListAdapter.notifyDataSetChanged();
                stopRefresh();
            }
        });

        setLastData();
        lv_comprehensive.doPullRefreshing(true, 500);
        lv_car = lv_comprehensive.getRefreshableView();
        carList = new ArrayList<Car.CarData>();
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.drive_car_current,
                null);
        lv_car.addHeaderView(viewH);
        return view;
    }

    private void setLastData() {
        String text = formatdatatime(System.currentTimeMillis());
        lv_comprehensive.setLastUpdatedLabel(text);
        LogUtils.i("time","-------->"+text);
    }

    private void stopRefresh() {
        lv_comprehensive.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_comprehensive.onPullDownRefreshComplete();
                lv_comprehensive.onPullUpRefreshComplete();
                setLastData();
            }
        },2000);
    }
    private String formatdatatime(long time){
           if (0==time){
               return "";
           }
        return mdata.format(new Date(time));
    }
    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }
    //    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        tv_car_current_number =(TextView) getView().findViewById(R.id.tv_car_current_Number);
        tv_car_current_brand = (TextView)getView().findViewById(R.id.tv_car_current_brand);
        iv_AddCar = (WaveView) getView().findViewById(R.id.add_car);
        iv_AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddCarActivity.class));
            }
        });
        iv_editor = (WaveView) getView().findViewById(R.id.ly_car_info);
        iv_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car.CarData cardataInfo = new Car.CarData();
                cardataInfo.setId(user.getUserCurrentCarId());
                cardataInfo.setBrand(user.getUserCurrentCarBrand());
                cardataInfo.setCarnumber(user.getCurrentcar());
                cardataInfo.setEnginenumber(user.getUserCurrentCarEnginenumber());
                cardataInfo.setCartype(user.getUserCurrentCarType());
                cardataInfo.setIscurrentcar("1");
                Bundle bundle = new Bundle();
                bundle.putSerializable("carinfo", cardataInfo);
                Intent intent = new Intent(mContext, UpdateCarActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
//                startActivity(new Intent(mContext, UpdateCarActivity.class));
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // 获取数据，网络获取数据并保存
        LogUtils.i("UserData", "------->onResume" );
        getData();
        // 数据显示
        displayData();
    }

    /**
     * 数据的显示
     */
    private void displayData() {
        user.readData(mContext);
    }
    /**
     * 获取数据，网络获取数据并保存
     */
    private void getData() {
        user = new UserInfo(mContext);
        user.readData(mContext);
        tv_car_current_number.setText(user.getCurrentcar().toUpperCase());
        tv_car_current_brand.setText(user.getUserCurrentCarBrand());
        if (!"".equals(tv_car_current_number.getText().toString())&&!"".equals(tv_car_current_brand.getText().toString())){
            iv_editor.setVisibility(View.VISIBLE);
        }else{
            iv_editor.setVisibility(View.GONE);
        }
        if(NetBaseUtils.isConnnected(mContext)){
            dialog.setMessage("正在刷新...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            new UserRequest(mContext, handler).GetCarList(user.getUserId(), GET_CAR_LIST);
        }
        LogUtils.i("UserData", "------->onResume-->"+user.getUserId() );
    }
    private Handler handler = new Handler(Looper.myLooper()) {


        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GET_CAR_LIST:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        JSONObject obj;
                        carList.clear();
                        try {
                            obj = new JSONObject(result);
                            LogUtils.i("objwww","---------->"+obj);
                            LogUtils.i("objwww","---------->"+obj.optString("status"));
                            if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                                JSONArray hwArray = obj.getJSONArray("data");
                                if (hwArray != null && hwArray.length() > 0) {
                                    JSONObject itemObject;
                                    for (int i = 0; i < hwArray.length(); i++) {
                                        itemObject = hwArray.optJSONObject(i);
                                        if (itemObject.getString("is_current_car").equals("0")) {
                                            Car.CarData carData = new Car.CarData();
                                            carData.setId(itemObject.getString("id"));
                                            carData.setCarnumber(itemObject.getString("car_number"));
                                            carData.setBrand(itemObject.getString("brand"));
                                            carData.setCartype(itemObject.getString("car_type"));
                                            carData.setEnginenumber(itemObject.getString("engine_number"));
                                            carData.setIscurrentcar("0");
                                            carList.add(carData);
                                        } else {
                                            user.setCurrentcar(itemObject.getString("car_number"));
                                            user.setUserCurrentCarId(itemObject.getString("id"));
                                            user.setUserCurrentCarType(itemObject.getString("car_type"));
                                            user.setUserCurrentCarEnginenumber(itemObject.getString("engine_number"));
                                            user.setUserCurrentCarBrand(itemObject.getString("brand"));
                                            user.writeData(mContext);
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Message msg = Message.obtain();
                                                        msg.what = ISPLAY;
                                                        handler.sendMessage(msg);
                                                    }
                                                }).start();
                                        }
                                    }
                                }
                                carListAdapter = new CarinfoListAdapter(activity, carList, 1);
                                lv_car.setAdapter(carListAdapter);
                                carListAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }else{
                                dialog.dismiss();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            ToastUtils.ToastShort(mContext, "");
                        }
                    }else{
//                        ToastUtils.ToastShort(mContext, "没有数据！");
                    }
                    break;
                case ISPLAY:
                    user = new UserInfo(mContext);
                    user.readData(mContext);
                    tv_car_current_number.setText(user.getCurrentcar().toUpperCase());
                    tv_car_current_brand.setText(user.getUserCurrentCarBrand());
                    if (!"".equals(tv_car_current_number.getText().toString())&&!"".equals(tv_car_current_brand.getText().toString())){
                        iv_editor.setVisibility(View.VISIBLE);
                    }else{
                        iv_editor.setVisibility(View.GONE);
                    }
                    break;
            }
        };
    };
}
