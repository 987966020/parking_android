package hbie.vip.parking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import hbie.vip.parking.activity.update.SetListview;
import hbie.vip.parking.adapter.OrderListAdapter;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.car.Car;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.ReboundScrollView;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/23.
 */
public class PayOrderActivity extends BaseActivity implements View.OnClickListener {
    private WaveView back, edit, ll_unpay;
    private ReboundScrollView scrollview;
    //jjjjjhhhh
    private UserInfo user;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private PullToRefreshListView lv_comprehensive;
    /* 车辆列表000 */
    private ListView lv_order, lv_carNum_OderList;
    private OrderListAdapter orderListAdapter;
    private ArrayList<order.DataBean> orderList;
    private static final int GET_ORDER_LIST = 2;
    private Context mContext;
    private TextView detail_loading;
    private Car.CarData carinfo = null;
    private ImageView nullimage;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private int GET_NOPAY_LIST = 1;
    private TextView un_car_number, un_pay_money, un_car_location, un_car_time, un_car_spend;
    private Button un_tv_car_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_car_orderlist);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo(mContext);
        Intent intent = this.getIntent();
        carinfo = (Car.CarData) intent.getSerializableExtra("carinfo");
        initView();
    }

    private void initView() {
        lv_comprehensive = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
        lv_comprehensive.setPullLoadEnabled(true);
        lv_comprehensive.setScrollLoadEnabled(false);
        lv_comprehensive.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                user.readData(mContext);
                getData();
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
        lv_order = lv_comprehensive.getRefreshableView();
//        un_car_number = (TextView) findViewById(R.id.ao_tv_wait_car_number);
//        un_car_location = (TextView) findViewById(R.id.ao_tv_car_wait_location);
//        un_car_time = (TextView) findViewById(R.id.ao_tv_car_wait_time);
//        un_car_spend = (TextView) findViewById(R.id.ao_tv_car_wait_spent);
//        un_tv_car_pay = (Button) findViewById(R.id.ao_btn_order_pay);
//        un_tv_car_pay.setOnClickListener(this);
        un_pay_money = (TextView) findViewById(R.id.tv_car_all_pay);
//        ll_unpay = (RelativeLayout) findViewById(R.id.ao_editoril_material_gender);
        nullimage = (ImageView) findViewById(R.id.lv_comprehensive_nullimage);
        back = (WaveView) findViewById(R.id.relativeLayout_find_order_back);
        back.setOnClickListener(this);
        user = new UserInfo();
//        View view = inflater.inflate(R.layout.fragment_car, container, false);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        orderList = new ArrayList<>();
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square).cacheInMemory(true).cacheOnDisc(true).build();

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

    /**
     * 获取数据，网络获取数据并保存
     */
    private void getData() {
        user = new UserInfo(mContext);
        LogUtils.i("UserData", "------->onResume-->" + user.getUserId());
        if (NetBaseUtils.isConnnected(mContext)) {
//            new UserRequest(mContext, handler).getExpenseInfo( carinfo.getCarnumber(), GET_ORDER_LIST);
            new UserRequest(mContext, handler).getallcarunpay(carinfo.getCarnumber(), GET_ORDER_LIST);
        } else {
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        user.readData(mContext);
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout_find_order_back:
                finish();
                break;
//            case R.id.ao_btn_order_pay:
//                Intent intent = new Intent(this,PayActivity.class);
//                intent.putExtra("paymain","4");
////                intent.putExtra("paymain","1");
//                intent.putExtra("currentmoney",un_pay_money.getText().toString());
//                intent.putExtra("currentpark",un_car_location.getText().toString());
//                intent.putExtra("currenttime",un_car_time.getText().toString());
//                intent.putExtra("currentcar",un_car_number.getText().toString());
//                startActivity(intent);
//                break;
        }

    }

    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case GET_ORDER_LIST:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        JSONObject obj;
                        orderList.clear();
                        try {
                            obj = new JSONObject(result);
                            LogUtils.i("result_pay", "------------->" + result);
                            if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                                JSONArray hwArray = obj.getJSONArray("data");
                                LogUtils.i("hwArray", "------------>" + hwArray);
                                if (hwArray != null && hwArray.length() > 0) {
                                    JSONObject itemObject;
                                    for (int i = 0; i < hwArray.length(); i++) {
                                        itemObject = hwArray.optJSONObject(i);
                                        if ("1".equals(itemObject.getString("status"))) {
                                            order.DataBean orderData = new order.DataBean();
                                            orderData.setCar_number(itemObject.getString("car_number"));
                                            orderData.setPark(itemObject.getString("park"));
                                            orderData.setStart_time_str(itemObject.getString("start_time_str"));
                                            orderData.setEnd_time_str(itemObject.getString("end_time_str"));
                                            orderData.setTime(itemObject.getString("time"));
                                            orderData.setPosition(itemObject.getString("position"));
                                            orderData.setMoney(itemObject.getString("money"));
                                            orderData.setPrice(itemObject.getString("price"));
                                            orderData.setBrand(itemObject.getString("brand"));
                                            orderData.setPay_status(itemObject.getString("pay_status"));
                                            orderList.add(orderData);
                                        }
                                    }
                                    orderListAdapter = new OrderListAdapter(mContext, orderList, 2);
                                    lv_order.setAdapter(orderListAdapter);
                                    orderListAdapter.notifyDataSetChanged();
                                    SetListview.setListViewHeightBasedOnChildren(lv_order);
                                    nullimage.setVisibility(View.GONE);
                                } else {
                                    nullimage.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
//                            ToastUtils.ToastShort(mContext, "没有数据！");
                        }
                        break;
                    }
            }
            ;
        }
    };
}

