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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.update.SetListview;
import hbie.vip.parking.adapter.OrderListAdapter;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.car.Car;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class Pay_Fragment_Pay extends Fragment {
    private static final int GET_PAY_LIST = 4;
    private Context mContext;
    private UserInfo user;
    private SharedPreferences sp;
    private PullToRefreshListView lv_comprehensive;
    private Activity activity;
    private TextView detail_loading, currentcarhead,tv_content_all;
    private ImageView nullimage;
    private ListView lv_order;
    private ProgressDialog dialog;
    private OrderListAdapter orderListAdapter;
    private ArrayList<order.DataBean> orderList = new ArrayList<>();
    private final static int GET_CAR_LOCATION = 1;
    private final static int GET_ORDER_LIST = 2;
    private final static int GET_NOPAY_LIST = 3;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View listhead;
    private LayoutInflater minflater;
    private String currentcar;
    private View viewhead;
    private Intent intent;
    private String type;
    private order.DataBean orderData;
    private Car.CarData carorser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        user = new UserInfo(mContext);
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
        intent = getActivity().getIntent();
        type = intent.getStringExtra("type");
        carorser = ( Car.CarData) intent.getSerializableExtra("carinfo");
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_mian_fragment, container, false);
        initpay();
        detail_loading = (TextView) view.findViewById(R.id.detail_loading);
        nullimage = (ImageView) view.findViewById(R.id.lv_comprehensive_nullimage);
        lv_comprehensive = (PullToRefreshListView) view
                .findViewById(R.id.lv_comprehensive);
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
        lv_order = lv_comprehensive.getRefreshableView();
//        orderListAdapter = new OrderListAdapter(activity, orderList,0);
//        lv_comprehensive.doPullRefreshing(true, 500);
        viewhead = LayoutInflater.from(getActivity()).inflate(R.layout.pay_listview_head, null);
        currentcarhead = (TextView) viewhead.findViewById(R.id.tv_current_Type);
        tv_content_all = (TextView) viewhead.findViewById(R.id.tv_content_all);//所有车辆
        lv_order.addHeaderView(viewhead);
// lv_order.setAdapter(orderListAdapter);
//        orderList.clear();
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                order.OrderData orderData = new order.OrderData();
//                for(int i=0;i<5;i++) {
//                    orderData.setHead(i+"");
//                    orderList.add(orderData);
//                }
//                orderListAdapter.notifyDataSetChanged();
//            }
//        });
        return view;
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
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取数据，网络获取数据并保存
        LogUtils.i("UserData", "------->onResume");
        getData();
    }

    /**
     * 获取数据，网络获取数据并保存
     */
    private void getData() {
        user.readData(mContext);
        LogUtils.i("UserData", "------->onResume-->" + user.getUserId() + user.getCurrentcar());
        currentcarhead.setText(user.getCurrentcar());
        if ("1".equals(type)){
            tv_content_all.setText("所有车辆");
            currentcarhead.setVisibility(View.GONE);
            if (NetBaseUtils.isConnnected(mContext)) {
                dialog.setMessage("正在刷新..");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                new UserRequest(mContext, handler).getCarHistoryOrder(user.getUserId(), GET_ORDER_LIST);
                if (orderList.size() > 0) {
                    nullimage.setVisibility(View.GONE);
                } else {
                    nullimage.setVisibility(View.VISIBLE);
                }
            } else {
                ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
            }
//        orderListAdapter.notifyDataSetChanged();
        }else if ("2".equals(type)){
            currentcarhead.setText(carorser.getCarnumber());
            if (NetBaseUtils.isConnnected(mContext)) {
                dialog.setMessage("正在刷新..");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                new UserRequest(mContext, handler).getallcarunpay(carorser.getCarnumber(), GET_PAY_LIST);
                if (orderList.size() > 0) {
                    nullimage.setVisibility(View.GONE);
                } else {
                    nullimage.setVisibility(View.VISIBLE);
                }
            } else {
                ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
            }
        }

    }

    /**
     * 初始化
     */
    private void initpay() {

    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
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
                                user.readData(mContext);
                                JSONArray hwArray = obj.getJSONArray("data");
                                if (hwArray != null && hwArray.length() > 0) {
                                    for (int i = 0; i < hwArray.length(); i++) {
                                        JSONObject itemObject = hwArray.getJSONObject(i);
                                        orderData = new order.DataBean();
//                                            orderData.setId(itemObject.getString("id"));
//                                        if ("1".equals(itemObject.getString("status")) && "1".equals(itemObject.getString("pay_status"))) {
                                        if ("1".equals(itemObject.getString("pay_status"))) {
//                                        LogUtils.i("paytype", "-------->" + itemObject.getString("pay_type"));
                                        orderData.setCar_number(itemObject.getString("car_number"));
                                        orderData.setBrand(itemObject.getString("brand"));
                                        orderData.setCar_type(itemObject.getString("car_type"));
                                        orderData.setPark(itemObject.getString("park"));
                                        orderData.setPosition(itemObject.getString("position"));
                                        orderData.setOrder_no(itemObject.getString("order_no"));
                                        orderData.setOwner_id(itemObject.getString("owner_id"));
                                        orderData.setPark_date(itemObject.getString("date"));
                                        orderData.setStart_time_str(itemObject.getString("start_time"));
                                        orderData.setEnd_time_str(itemObject.getString("end_time"));
                                        orderData.setPay_user_id(itemObject.getString("pay_user_id"));
                                        orderData.setTime(itemObject.getString("time"));
                                        orderData.setPay_type(itemObject.getString("pay_type"));
                                        orderData.setPrice(itemObject.getString("price"));
                                        orderData.setStatus(itemObject.getString("status"));
                                        orderData.setPay_status(itemObject.getString("pay_status"));
                                        orderData.setMoney(itemObject.getString("money"));
                                        orderData.setPark_time(itemObject.getString("pay_time"));
                                        if (user.getUserId() != null&&user.getUserId().length() > 0){
                                            if (user.getUserId().equals(itemObject.getString("pay_user_id"))){
                                                orderList.add(orderData);
                                             }
                                        }
                                        nullimage.setVisibility(View.GONE);
                                        }
                                    }

                                    orderListAdapter = new OrderListAdapter(mContext, orderList, 0);
                                    lv_order.setAdapter(orderListAdapter);
                                    orderListAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                } else {
                                    nullimage.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }
                            } else {
                                nullimage.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
//                            ToastUtils.ToastShort(mContext, "没有数据！");
                        }
                    }
                    break;
                case GET_PAY_LIST:
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
                                        if ("1".equals(itemObject.getString("pay_status"))) {
                                            orderData = new order.DataBean();
                                            orderData.setOrder_no(itemObject.getString("order_no"));
                                            orderData.setCar_number(itemObject.getString("car_number"));
                                            orderData.setBrand(itemObject.getString("brand"));
                                            orderData.setCar_type(itemObject.getString("car_type"));
                                            orderData.setPark(itemObject.getString("park"));
                                            orderData.setPosition(itemObject.getString("position"));
                                            orderData.setOwner_name(itemObject.getString("owner_name"));
                                            orderData.setPay_user_name(itemObject.getString("pay_user_name"));
                                            orderData.setPark_code(itemObject.getString("park_code"));
                                            orderData.setOwner_id(itemObject.getString("owner_id"));
                                            orderData.setStart_time_str(itemObject.getString("start_time_str"));
                                            orderData.setEnd_time_str(itemObject.getString("end_time_str"));
                                            orderData.setPrice(itemObject.getString("price"));
                                            orderData.setTime(itemObject.getString("time"));
                                            orderData.setPay_user_id(itemObject.getString("pay_user_id"));
                                            orderData.setPark_time(itemObject.getString("park_time"));
                                            orderData.setPay_type(itemObject.getString("pay_type"));
                                            orderData.setStatus(itemObject.getString("status"));
                                            orderData.setPay_status(itemObject.getString("pay_status"));
                                            orderData.setMoney(itemObject.getString("money"));
                                            if (user.getUserId() != null&&user.getUserId().length() > 0){
                                                if (user.getUserId().equals(itemObject.getString("pay_user_id"))){
                                                    orderList.add(orderData);

                                                }
                                            }
                                        }
                                    }
                                    orderListAdapter = new OrderListAdapter(mContext, orderList, 0);
                                    lv_order.setAdapter(orderListAdapter);
                                    orderListAdapter.notifyDataSetChanged();
//                                    SetListview.setListViewHeightBasedOnChildren(rporderlist);
                                    nullimage.setVisibility(View.GONE);
                                    dialog.dismiss();
                                } else {
                                    nullimage.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }
                            }else{
                                dialog.dismiss();
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
            }
        }
    };
}
