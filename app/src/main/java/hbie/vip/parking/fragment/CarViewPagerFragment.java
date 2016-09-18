package hbie.vip.parking.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import hbie.vip.parking.R;
import hbie.vip.parking.adapter.FragmentAdapter;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.main.NoScrollViewPager;

/**
 * 新的 车辆Fragment界面
 * Created by Administrator on 2016/6/15.
 */
public class CarViewPagerFragment extends Fragment implements View.OnClickListener{
    private TabLayout tabLayout;
    private NoScrollViewPager viewPager;
    private RadioButton radio_pay;
    private RadioButton radio_unpay;
    private Context mContext;
    private Pay_Fragment_Pay payfragmnet;
    private Pay_Fragment_UnPay unpayfragmnet;
    private UserInfo user;
    private SharedPreferences sp;
    private int currentTabIndex;
    List<Fragment> fragmenlist = new ArrayList<Fragment>();
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        user = new UserInfo(mContext);
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_main_activity,container,false);
        iniView(view);
        return view;
    }

    private void iniView(View view) {
//        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        radio_pay = (RadioButton) view.findViewById(R.id.radio_pay);
        radio_unpay = (RadioButton) view.findViewById(R.id.radio_unpay);
        viewPager = (NoScrollViewPager) view.findViewById(R.id.viewPager);
//        viewPager.setAdapter(new TabViewPagerAdapter(getActivity().getSupportFragmentManager()));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setNoScroll(false);
//        tabLayout.setupWithViewPager(viewPager);
        radio_pay.setOnClickListener(this);
        radio_unpay.setOnClickListener(this);
         payfragmnet = new Pay_Fragment_Pay();
         unpayfragmnet = new Pay_Fragment_UnPay();
        fragmenlist.add(unpayfragmnet);
        fragmenlist.add(payfragmnet);
        FragmentAdapter adapter = new FragmentAdapter(
                getActivity().getSupportFragmentManager(),fragmenlist);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case 1:
                        radio_pay.setChecked(true);
                        break;
                    case 2:
                        radio_unpay.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.viewPager, payfragmnet).add(R.id.viewPager, unpayfragmnet).hide(unpayfragmnet).show(payfragmnet)
//                .commit();
    }

    @Override
    public void onClick(View v) {
        int isShow = 0;
        switch (v.getId()){
            case R.id.radio_pay:
                viewPager.setCurrentItem(1);
                isShow=0;
                break;
            case R.id.radio_unpay:
                viewPager.setCurrentItem(2);
                isShow=1;
                break;
        }
        if(currentTabIndex!=isShow){
        FragmentTransaction trx = getActivity().getSupportFragmentManager().beginTransaction();
        trx.hide(fragmenlist.get(currentTabIndex));
        if (!fragmenlist.get(isShow).isAdded()) {
            trx.add(R.id.fragment_container, fragmenlist.get(isShow));
        }
        trx.show(fragmenlist.get(isShow)).commit();
        }
        currentTabIndex=isShow;

    }
}
