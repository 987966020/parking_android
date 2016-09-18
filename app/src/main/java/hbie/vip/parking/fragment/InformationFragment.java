package hbie.vip.parking.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.PayActivity;
import hbie.vip.parking.activity.update.SetListview;
import hbie.vip.parking.activity.wheel.Jiahaoyou;
import hbie.vip.parking.adapter.OrderListAdapter;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.button.RevealButton;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.PermissionUtil;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/9.
 */
public class InformationFragment extends Fragment implements View.OnClickListener {
    private final static int GET_NOPAY_LIST = 5;
    private static final int GETCURRENT = 6;
    private static final int SENDMESSAGE = 7;
    private static final String ShortMessage = "我在使用智能停车app，快来下载www.pgyer.com/fq12";
    private Activity activity;
    private PullToRefreshListView lv_comprehensive;
    private Context mContext;
    private UserInfo user;
    private SharedPreferences sp;
    private TextView tv_car_current_number, tv_car_park, tv_car_time, tv_car_timelong, tv_car_money;
    private TextView cl_car_park, cl_car_time, cl_car_timelong, cl_car_money;
    private ImageView bt_car_Logo, lv_nullimage;
    private ImageView btn_exit;
    private ListView lv_order;
    private OrderListAdapter orderListAdapter;
    private TextView detail_loading;
    private ArrayList<order.DataBean> orderList;

    private RevealButton promptly_pay;
    private String currentcar;
    private String yearnew, monthnew, daynew, HHnew, mmnew, ssnew;
    private int mmtwo;
    private int i = 0;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private WaveView up_jiahaoyou;
    String phoneNumber = "";
    private ProgressDialog dialog;
    private order.DataBean orderData;


    private Timer timer;
    private TimerTask timerTask;
    int s = 0;
    int m = 0;
    int h = 0;


    @SuppressWarnings("static-access")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
        user = new UserInfo(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_main, container, false);
//        lv_order = (ListView) view.findViewById(R.id.orderlist_listview);
        orderList = new ArrayList<order.DataBean>();
//        lv_nullimage = (ImageView) view.findViewById(R.id.lv_comprehensive_nullimage);
//        lv_nullimage.setVisibility(View.GONE);

        return view;
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
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        up_jiahaoyou = (WaveView) getView().findViewById(R.id.up_jiahaoyou);
        up_jiahaoyou.setOnClickListener(this);
        bt_car_Logo = (ImageView) getView().findViewById(R.id.iv_car_logo);
        tv_car_current_number = (TextView) getView().findViewById(R.id.tv_car_current_info);
        tv_car_money = (TextView) getView().findViewById(R.id.tv_car_money);
        tv_car_park = (TextView) getView().findViewById(R.id.tv_car_park);
        tv_car_time = (TextView) getView().findViewById(R.id.tv_car_time);
        tv_car_timelong = (TextView) getView().findViewById(R.id.tv_car_timelong);
        promptly_pay = (RevealButton) getView().findViewById(R.id.btn_login_submit);
        promptly_pay.setOnClickListener(this);
        cl_car_park = (TextView) getView().findViewById(R.id.tv_editoril_material_currentpark_text_text);
        cl_car_time = (TextView) getView().findViewById(R.id.tv_editoril_material_time_name_text);
        cl_car_timelong = (TextView) getView().findViewById(R.id.tv_editoril_material_timelong_name_text);
        cl_car_money = (TextView) getView().findViewById(R.id.tv_car_money_title);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取数据，网络获取数据并保存
        LogUtils.i("UserData", "------->onResume");
        getData();

//        StartTime();
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    /**
     * 获取数据，网络获取数据并保存
     */
    public void getData() {
        user.readData(mContext);
        LogUtils.i("UserData", "------->onResume-->" + user.getUserId() + user.getCurrentcar());
        if (NetBaseUtils.isConnnected(mContext)) {
            dialog.setMessage("正在刷新...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            new UserRequest(mContext, handler).getMemberCurrentCar(user.getUserId(), GETCURRENT);
        } else {
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
            dialog.dismiss();
        }
    }

    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
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
                                        if ("0".equals(itemObject.getString("status")) && "0".equals(itemObject.getString("pay_status"))) {
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
                                            tv_car_park.setText(itemObject.getString("park") + "    " + itemObject.getString("position"));
                                            tv_car_time.setText(itemObject.getString("start_time_str"));
                                            tv_car_timelong.setText(itemObject.getString("time"));
                                            tv_car_money.setText(itemObject.getString("money"));
                                            tv_car_park.setTextColor(getResources().getColor(R.color.black));
                                            tv_car_time.setTextColor(getResources().getColor(R.color.black));
                                            tv_car_timelong.setTextColor(getResources().getColor(R.color.black));
                                            tv_car_money.setTextColor(getResources().getColor(R.color.black));
                                            cl_car_park.setTextColor(getResources().getColor(R.color.black));
                                            cl_car_time.setTextColor(getResources().getColor(R.color.black));
                                            cl_car_timelong.setTextColor(getResources().getColor(R.color.black));
                                            cl_car_money.setTextColor(getResources().getColor(R.color.black));
                                        }else{
                                            tv_car_park.setText("暂无");
                                            tv_car_time.setText("暂无");
                                            tv_car_timelong.setText("暂无");
                                            tv_car_money.setText("暂无");
                                            cl_car_park.setTextColor(getResources().getColor(R.color.gray));
                                            cl_car_time.setTextColor(getResources().getColor(R.color.gray));
                                            cl_car_timelong.setTextColor(getResources().getColor(R.color.gray));
                                            cl_car_money.setTextColor(getResources().getColor(R.color.gray));
                                            tv_car_park.setTextColor(getResources().getColor(R.color.gray));
                                            tv_car_time.setTextColor(getResources().getColor(R.color.gray));
                                            tv_car_timelong.setTextColor(getResources().getColor(R.color.gray));
                                            tv_car_money.setTextColor(getResources().getColor(R.color.gray));

                                            dialog.dismiss();
                                        }

                                    }
                                    dialog.dismiss();
                                } else {
                                    tv_car_park.setText("暂无");
                                    tv_car_time.setText("暂无");
                                    tv_car_timelong.setText("暂无");
                                    tv_car_money.setText("暂无");
                                    cl_car_park.setTextColor(getResources().getColor(R.color.gray));
                                    cl_car_time.setTextColor(getResources().getColor(R.color.gray));
                                    cl_car_timelong.setTextColor(getResources().getColor(R.color.gray));
                                    cl_car_money.setTextColor(getResources().getColor(R.color.gray));
                                    tv_car_park.setTextColor(getResources().getColor(R.color.gray));
                                    tv_car_time.setTextColor(getResources().getColor(R.color.gray));
                                    tv_car_timelong.setTextColor(getResources().getColor(R.color.gray));
                                    tv_car_money.setTextColor(getResources().getColor(R.color.gray));

                                    dialog.dismiss();
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                        break;
                case GETCURRENT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                JSONObject json = new JSONObject(data);
                                bt_car_Logo.setSelected(true);
                                currentcar = json.getString("car_number");
                                user.setCurrentcar(currentcar);
                                tv_car_current_number.setText(json.getString("car_number") + "  " + json.getString("brand") + "   " + json.getString("car_type"));
                                if (json.getString("car_number") != null && json.getString("car_number").length() > 0) {
                                    if (NetBaseUtils.isConnnected(mContext)) {
//                                        new UserRequest(mContext, handler).getCarLocation(user.getUserId(), json.getString("car_number"), GET_CAR_LOCATION);
                                        new UserRequest(mContext, handler).getallcarunpay(user.getCurrentcar(), GET_NOPAY_LIST);
                                        LogUtils.i("user.getCurrentcar()", "-------->" + user.getCurrentcar());
                                    } else {
                                        orderList.clear();
                                    }
                                }
                                cl_car_park.setTextColor(getResources().getColor(R.color.black));
                                cl_car_time.setTextColor(getResources().getColor(R.color.black));
                                cl_car_timelong.setTextColor(getResources().getColor(R.color.black));
                                cl_car_money.setTextColor(getResources().getColor(R.color.black));

                            } else {
                                bt_car_Logo.setSelected(false);
                                tv_car_current_number.setText("暂无");
                                tv_car_park.setText("暂无");
                                tv_car_time.setText("暂无");
                                tv_car_time.setText("暂无");
                                tv_car_money.setText("暂无");
                                cl_car_park.setTextColor(getResources().getColor(R.color.gray));
                                cl_car_time.setTextColor(getResources().getColor(R.color.gray));
                                cl_car_timelong.setTextColor(getResources().getColor(R.color.gray));
                                cl_car_money.setTextColor(getResources().getColor(R.color.gray));
                                tv_car_park.setTextColor(getResources().getColor(R.color.gray));
                                tv_car_time.setTextColor(getResources().getColor(R.color.gray));
                                tv_car_timelong.setTextColor(getResources().getColor(R.color.gray));
                                tv_car_money.setTextColor(getResources().getColor(R.color.gray));
                                orderList.clear();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case SENDMESSAGE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        JSONObject obj;
                        try {
                            obj = new JSONObject(result);
                            if (CommunalInterfaces._STATE.equals(obj.optString("status"))) {
                                Toast.makeText(getActivity(), "发送短信成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "发送短信失败", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "服务器失败请重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_submit:
                if (orderList.size() >  0&&orderList != null){
                        order.DataBean orderdataInfo = orderList.get(0);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderinfo", orderdataInfo);
                        bundle.putString("paymain", "1");
                        Intent intent = new Intent(mContext, PayActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        ToastUtils.ToastShort(mContext, "暂无停车信息");
                }
                break;
            case R.id.up_jiahaoyou:
                getCheck();
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.Contacts.CONTENT_TYPE);// vnd.android.cursor.dir/contact
                startActivityForResult(i, 0);
                break;
        }
    }

    private void getCheck() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CONTACTS},
                    100);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CONTACTS)){
                    Toast.makeText(getActivity(),"没有权限,请去应用设置权限",Toast.LENGTH_SHORT).show();
                }
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)){
                    Toast.makeText(getActivity(),"没有权限,请去应用设置权限",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /**
     * 获取返回的联系人信息
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getActivity().managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String hasPhone = c
                                .getString(c
                                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        // 取得联系人ID
                        String contactId = c.getString(c
                                .getColumnIndex(ContactsContract.Contacts._ID));
                        //取得联系人的名称
                        String contactname = c.getString(c
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false";

                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getActivity().getContentResolver()
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                    + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones
                                        .getString(phones
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                phoneNumInput.setText(phoneNumber);
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage(ShortMessage);
                                builder.setTitle("发送到" + "\t\t\t\t" + contactname);
                                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (NetBaseUtils.isConnnected(mContext)) {
                                            new UserRequest(mContext, handler).SendMeaasge(phoneNumber, ShortMessage, SENDMESSAGE);
                                        } else {
                                            Toast.makeText(getActivity(), "网络不稳定，请稍后重试~", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                                if(Build.VERSION.SDK_INT < 14) {
                                    phones.close();
                                }
                            }
                        }
                    }
                    break;
                }
        }
    }

}