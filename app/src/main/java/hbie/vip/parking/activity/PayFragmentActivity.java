package hbie.vip.parking.activity;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.FindCarOrderActivity;
import hbie.vip.parking.activity.ReplaceCarFareActivity;
import hbie.vip.parking.activity.update.SetListview;
import hbie.vip.parking.adapter.OrderListAdapter;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by Administrator on 2016/9/4 0004.
 */
public class PayFragmentActivity extends Fragment implements View.OnClickListener {
    private static final int GET_NOPAY_LIST = 1;
    private WaveView pay, search, car_pare_replace;
    private View view,viewhead;
    private ProgressDialog dialog;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView lv_comprehensive;
    private UserInfo user;
    private Context mContext;
    private SharedPreferences sp;
    private OrderListAdapter orderListAdapter;
    private ArrayList<order.DataBean> orderList = new ArrayList<order.DataBean>();
    private order.DataBean orderData;
    private ListView rporderlist;
    private ImageView nullimage;
    private TextView  currentcarhead;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_NOPAY_LIST:
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
                                        if ("1".equals(itemObject.getString("status")) && "0".equals(itemObject.getString("pay_status"))) {
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
                                            orderList.add(orderData);
                                        }
                                    }
                                    orderListAdapter = new OrderListAdapter(mContext, orderList, 1);
                                    rporderlist.setAdapter(orderListAdapter);
                                    orderListAdapter.notifyDataSetChanged();
//                                    SetListview.setListViewHeightBasedOnChildren(rporderlist);
                                    nullimage.setVisibility(View.GONE);
                                    dialog.dismiss();
                                } else {
                                    nullimage.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
            }
        }
    };


    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        user = new UserInfo(mContext);
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_activity, container, false);
        inipayview();
        return view;
    }

    private void inipayview() {
        nullimage = (ImageView) view.findViewById(R.id.lv_comprehensive_nullimage);
        search = (WaveView) view.findViewById(R.id.relativeLayout_pay_data_search);
        search.setOnClickListener(this);
        car_pare_replace = (WaveView) view.findViewById(R.id.relativeLayout_car_fare_replace);
        car_pare_replace.setOnClickListener(this);
        lv_comprehensive = (PullToRefreshListView) view.findViewById(R.id.lv_comprehensive);
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
        rporderlist = lv_comprehensive.getRefreshableView();
        viewhead = LayoutInflater.from(getActivity()).inflate(R.layout.pay_listview_head, null);
        currentcarhead = (TextView) viewhead.findViewById(R.id.tv_current_Type);
        rporderlist.addHeaderView(viewhead);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout_pay_data_search:
                Intent intent = new Intent(mContext, FindCarOrderActivity.class);
//                intent.putExtras(bundle);
                startActivity(intent);
//                IntentUtils.getIntent(mContext, UpdatePersonalPhoneActivity.class);
                break;
            case R.id.relativeLayout_car_fare_replace:
                Intent carfare = new Intent(mContext, ReplaceCarFareActivity.class);
                startActivity(carfare);
                break;
        }
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
        return mdata.format(new Date(time));
    }

    @Override
    public void onResume() {
        super.onResume();
        user.readData(mContext);
        currentcarhead.setText(user.getCurrentcar());
        getData();
    }

    private void getData() {
        if (NetBaseUtils.isConnnected(mContext)) {
            dialog.setMessage("正在刷新...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
//                    new UserRequest(mContext,handler).getExpenseInfo(rpcar_Num.getText().toString(),GET_CURRENTCAR);
            new UserRequest(mContext, handler).getallcarunpay(user.getCurrentcar(), GET_NOPAY_LIST);
        } else {
            ToastUtils.ToastShort(mContext, "网络问题,请稍后重试!");
        }
    }


}
