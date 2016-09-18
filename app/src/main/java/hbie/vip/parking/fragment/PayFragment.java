package hbie.vip.parking.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.activity.FindCarOrderActivity;
import hbie.vip.parking.activity.ReplaceCarFareActivity;
import hbie.vip.parking.adapter.FragmentAdapter;
import hbie.vip.parking.adapter.OrderListAdapter;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.fragment.Pay_Fragment_Pay;
import hbie.vip.parking.fragment.Pay_Fragment_UnPay;
import hbie.vip.parking.main.NoScrollViewPager;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by mac on 16/5/9.
 */
public class PayFragment extends BaseActivity implements View.OnClickListener {
    private WaveView pay, search, car_pare_replace;
    private NoScrollViewPager pay_viewPager;
    private Button btn_unpay,btn_pay;
    private Context mContext;

    private UserInfo user;
    private SharedPreferences sp;
    private List<Fragment> fragmenlist = new ArrayList<>();
    private TextView pay_wire, unpay_wire;
    private FragmentManager fragmentManager;
    private Pay_Fragment_Pay pf_pay;
    private Pay_Fragment_UnPay pf_unpay;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay);
        mContext = this;
        user = new UserInfo(mContext);
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
        payiniview();
    }

    private void payiniview() {
        pay_viewPager = (NoScrollViewPager) findViewById(R.id.pay_main_viewPager);
        btn_unpay = (Button) findViewById(R.id.showorderinfo_unpay);
        btn_pay = (Button) findViewById(R.id.showorderinfo_pay);
        pay_viewPager.setNoScroll(true);
        btn_pay.setOnClickListener(this);
        btn_unpay.setOnClickListener(this);
        car_pare_replace = (WaveView)findViewById(R.id.relativeLayout_car_fare_replace);//返回
        car_pare_replace.setOnClickListener(this);
        unpay_wire = (TextView) findViewById(R.id.pay_unpay_low_sire);
        pay_wire = (TextView) findViewById(R.id.pay_pay_low_sire);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        pf_pay = new Pay_Fragment_Pay();
        pf_unpay = new Pay_Fragment_UnPay();
//        fragments = new Fragment[]{employWorkFragment, employTalentFragment, employBookFragment, add_employWork_fragment, add_employTalent_fragment};
//        fragments = new Fragment[]{pf_unpay,pf_pay };
        unpay_wire.setVisibility(View.VISIBLE);
        pay_wire.setVisibility(View.INVISIBLE);
        fragmentTransaction.replace(R.id.pay_fragment_container_viewpager, pf_unpay);
        fragmentTransaction.commit();

    }
    /**
     * 初始化组件
     */
//    public void initView(View view) {
//        pay_viewPager = (NoScrollViewPager) view.findViewById(R.id.pay_main_viewPager);
//        btn_unpay = (Button) view.findViewById(R.id.showorderinfo_unpay);
//        btn_pay = (Button) view.findViewById(R.id.showorderinfo_pay);
//        pay_viewPager.setNoScroll(true);
//        btn_pay.setOnClickListener(this);
//        btn_unpay.setOnClickListener(this);
//
//    }

    @Override
    public void onClick(View v) {
        int isShow = 0;
        switch (v.getId()) {
            case R.id.btn_quit_submit:
                break;
            case R.id.relativeLayout_car_fare_replace:
                finish();
//                Intent carfare = new Intent(mContext, ReplaceCarFareActivity.class);
//                startActivity(carfare);
                break;
            case R.id.showorderinfo_unpay:
                unpay_wire.setVisibility(View.VISIBLE);
                pay_wire.setVisibility(View.INVISIBLE);
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                pf_unpay = new Pay_Fragment_UnPay();
                fragmentTransaction.replace(R.id.pay_fragment_container_viewpager, pf_unpay);
                fragmentTransaction.commit();
                Log.i("我是未缴费按钮", "啦啦啦啦哈哈哈");
                break;
            case R.id.showorderinfo_pay:
                pay_wire.setVisibility(View.VISIBLE);
                unpay_wire.setVisibility(View.INVISIBLE);
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                pf_pay = new Pay_Fragment_Pay();
                fragmentTransaction2.replace(R.id.pay_fragment_container_viewpager, pf_pay);
                fragmentTransaction2.commit();
                Log.i("我是缴费按钮","啦啦啦啦");
                break;

        }
    }

}
