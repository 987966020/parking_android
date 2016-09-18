package hbie.vip.parking.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.update.SetListview;
import hbie.vip.parking.adapter.OrderListAdapter;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ReplaceCarFareActivity extends Activity implements View.OnClickListener,View.OnFocusChangeListener,View.OnTouchListener{
    private static final int GET_CURRENTCAR = 1;
    private static final int GET_NOPAY_LIST = 3;
    private TextView un_car_number, un_pay_money, un_car_location, un_car_time, un_car_spend;
    private Button un_tv_car_pay;
    private RelativeLayout rp_car_Number,rp_car_Num_center,rp_car_Num_left,ll_unpay;
    private Button rp_car_search;
    private WaveView rp_back;
    private LinearLayout liner_focus;
    private Context mContext;
    private UserInfo user;
    private EditText rpcar_Num;
    private ProgressDialog dialog;
    private ListView rporderlist,lv_order;
    private ImageView image_search,lv_nullimage;
    private int focuse = 0;
    private static final int UNPAYOTHER = 2;
    private OrderListAdapter orderListAdapter;
    private ArrayList<order.DataBean> orderList = new ArrayList<order.DataBean>();
    private order.DataBean orderData;



    private Handler handler = new Handler(Looper.myLooper()){
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
                                        if ("1".equals(itemObject.getString("status"))&&"0".equals(itemObject.getString("pay_status"))) {
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
                                    SetListview.setListViewHeightBasedOnChildren(rporderlist);
                                    lv_nullimage.setVisibility(View.GONE);
                                    dialog.dismiss();
                                } else {
                                    lv_nullimage.setVisibility(View.VISIBLE);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.raplace_car_fare_activity);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo(mContext);
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        getreplaceView();
    }

    private void getreplaceView() {
        rp_car_Num_center = (RelativeLayout) findViewById(R.id.relativeLayout_rp_Num_center);
        rpcar_Num = (EditText) findViewById(R.id.et_write_rpcar_Num_left);
        rpcar_Num.setOnFocusChangeListener(this);
        lv_nullimage = (ImageView) findViewById(R.id.lv_comprehensive_nullimage);
        lv_nullimage.setVisibility(View.GONE);
        rp_car_search = (Button) findViewById(R.id.rp_search_car_fare_nymber);
        rp_car_search.setOnClickListener(this);
        rp_back = (WaveView) findViewById(R.id.relativeLayout_replace_car_fare_back);
        rp_back.setOnClickListener(this);
        rporderlist = (ListView) findViewById(R.id.lv_rpcar_pay);
        image_search = (ImageView) findViewById(R.id.image_search_image);
        liner_focus = (LinearLayout) findViewById(R.id.liner_focus);
        liner_focus.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rp_search_car_fare_nymber:
                if (NetBaseUtils.isConnnected(mContext)){
                    dialog.setMessage("正在登录...");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.show();
//                    new UserRequest(mContext,handler).getExpenseInfo(rpcar_Num.getText().toString(),GET_CURRENTCAR);
                    new UserRequest(mContext, handler).getallcarunpay(rpcar_Num.getText().toString(), GET_NOPAY_LIST);
                }else{
                    ToastUtils.ToastShort(mContext,"网络问题,请稍后重试!");
                }
                break;
            case R.id.relativeLayout_replace_car_fare_back:
                ReplaceCarFareActivity.this.finish();
                break;
            case R.id.po_btn_order_pay:
                Intent intent = new Intent(this,PayActivity.class);
                intent.putExtra("paymain","4");
                intent.putExtra("currentmoney",un_pay_money.getText().toString());
                intent.putExtra("currentpark",un_car_location.getText().toString());
                intent.putExtra("currenttime",un_car_time.getText().toString());
                intent.putExtra("currentcar",un_car_number.getText().toString());
                startActivity(intent);
        }
    }

    @Override
    public void onFocusChange(View v, boolean ishasFocus) {
                if (ishasFocus){
                    rp_car_Num_center.setVisibility(View.GONE);
                    rpcar_Num.setHint(getResources().getString(R.string.et_search_replace_car_Num));
                    image_search.setVisibility(View.VISIBLE);
                    LogUtils.i("获取焦点", "-------->" + 0);
                }else{
                    if ("".equals(rpcar_Num.getText().toString())) {
                        rp_car_Num_center.setVisibility(View.VISIBLE);
                        rpcar_Num.setHint("");
                        image_search.setVisibility(View.GONE);
                        LogUtils.i("失去焦点", "-------->" + 111);
                    }else{
                        rp_car_Num_center.setVisibility(View.GONE);
                        image_search.setVisibility(View.VISIBLE);
                    }
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //实现点击空白地方输入框失去焦点
        liner_focus.setFocusable(true);
        liner_focus.setFocusableInTouchMode(true);
        liner_focus.requestFocus();
        return false;
    }
}
