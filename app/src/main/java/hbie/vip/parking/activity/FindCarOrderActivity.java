package hbie.vip.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.adapter.CarinfoListAdapter;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.car.Car;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.fragment.PayFragment;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.picture.Util;
import hbie.vip.parking.ui.ReboundScrollView;
import hbie.vip.parking.ui.RoundImageView;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/22.
 */
public class FindCarOrderActivity extends BaseActivity implements View.OnClickListener {
    private WaveView back, edit;
    private TextView tv_name, tv_company, tv_poistion, tv_city,nullmessage;
    private RoundImageView iv_head;
    private ImageView iv_gender;
    private ImageView iv_daKa;
    private ReboundScrollView scrollview;
    private UserInfo user;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private PullToRefreshListView lv_comprehensive;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /* 车辆列表 */
    private ListView lv_car;
    private WaveView allcar;
    private CarinfoListAdapter carListAdapter;
    private ArrayList<Car.CarData> carList;
    private static final int SELECT_COMMENT_KEY = 1;
    private static final int GET_CAR_LIST=2;
    private Context mContext;
    private TextView detail_loading;
    private ProgressDialog dialog;
    private View mViewH;
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
                            if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                                JSONArray hwArray = obj.getJSONArray("data");
                                if(hwArray != null && hwArray.length()>0) {
                                    JSONObject itemObject;
                                    for (int i = 0; i < hwArray.length(); i++) {
                                        itemObject = hwArray.optJSONObject(i);
                                            Car.CarData carData = new Car.CarData();
                                            carData.setId(itemObject.getString("id"));
                                            carData.setCarnumber(itemObject.getString("car_number"));
                                            carData.setBrand(itemObject.getString("brand"));
                                            carData.setCartype(itemObject.getString("car_type"));
                                            carData.setEnginenumber(itemObject.getString("engine_number"));
                                            carList.add(carData);
                                        }
                                    carListAdapter = new CarinfoListAdapter(mContext, carList,2);
                                    lv_car.setAdapter(carListAdapter);
                                    dialog.dismiss();
                                }else{
                                    ToastUtils.ToastShort(mContext, "没有数据！");
                                    dialog.dismiss();
                                }
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
//                            ToastUtils.ToastShort(mContext, "没有数据！");
                        }
                    }
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_order);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        initView();
    }
    private void initView() {
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        back = (WaveView) findViewById(R.id.relativeLayout_find_order_back);
        back.setOnClickListener(this);
//        nullmessage = (TextView) findViewById(R.id.tv_null_message_text);
        user = new UserInfo();
        carList = new ArrayList<Car.CarData>();
//        View view = inflater.inflate(R.layout.fragment_car, container, false);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        lv_comprehensive = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
        lv_comprehensive.setPullLoadEnabled(true);
        lv_comprehensive.setScrollLoadEnabled(false);
        lv_comprehensive.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                user.readData(mContext);
                if (NetBaseUtils.isConnnected(mContext)) {
                    new UserRequest(mContext, handler).GetCarList(user.getUserId(), GET_CAR_LIST);
                } else {
                    ToastUtils.ToastShort(mContext, "网络错误，请稍后重试！");
                }
                stopRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                user.readData(mContext);

                stopRefresh();
            }
        });
        setLastUpdateTime();
        lv_comprehensive.doPullRefreshing(true, 500);
        lv_car = lv_comprehensive.getRefreshableView();
        mViewH = LayoutInflater.from(mContext).inflate(R.layout.carallbutton, null);
        allcar = (WaveView) mViewH.findViewById(R.id.ril_all_car_unpay);
        allcar.setOnClickListener(this);
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square).cacheInMemory(true).cacheOnDisc(true).build();
        lv_car.addHeaderView(mViewH);
    }
    /**
     * 获取数据，网络获取数据并保存
     */
    private void getData() {
        user = new UserInfo(mContext);
        user.readData(mContext);
        LogUtils.i("UserData", "------->onResume-->" + user.getUserId());
        if (NetBaseUtils.isConnnected(mContext)) {
            dialog.setMessage("正在刷新...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            new UserRequest(mContext, handler).GetCarList(user.getUserId(), GET_CAR_LIST);
        }else{
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        user.readData(mContext);
        getData();
    }
    private void stopRefresh() {
        lv_comprehensive.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_comprehensive.onPullDownRefreshComplete();
                lv_comprehensive.onPullUpRefreshComplete();
                setLastUpdateTime();
            }
        }, 1000);
    }
    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        lv_comprehensive.setLastUpdatedLabel(text);
        LogUtils.i("time", "-------->" + text);
    }
    private String formatDateTime(long time) {
        if (0 == time){
            return "";
        }
        return mdata.format(new Date(time));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout_find_order_back:
                finish();
                break;
            case R.id.ril_all_car_unpay:
                Intent intent = new Intent(mContext,PayFragment.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
        }

    }
}
