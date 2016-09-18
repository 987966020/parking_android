package hbie.vip.parking.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Adapter;
import java.util.ArrayList;
import java.util.List;

import hbie.vip.parking.fragment.Pay_Fragment_Pay;
import hbie.vip.parking.fragment.Pay_Fragment_UnPay;

/**
 *
 * Created by Administrator on 2016/6/15.
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public final int COUNT = 2;
//    private String[] titles = new String[]{"已缴费","未交费"};

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList.add(new Pay_Fragment_Pay());
        fragmentList.add(new Pay_Fragment_UnPay());
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titles[position];
//    }
}
