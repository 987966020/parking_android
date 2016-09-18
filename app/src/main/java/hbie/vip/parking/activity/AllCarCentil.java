package hbie.vip.parking.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

import hbie.vip.parking.R;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class AllCarCentil extends Activity{

    private PullToRefreshListView lv_comprehensive;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_order;
    private Context mContext;
    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_detail);
        mContext = this;
        user = new UserInfo(mContext);
        cariniview();
    }

    private void cariniview() {
        lv_comprehensive = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
        lv_comprehensive.setPullLoadEnabled(true);
        lv_comprehensive.setScrollLoadEnabled(false);
        lv_comprehensive.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                stopRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                stopRefresh();
            }
        });
        setLastUpdateTime();
        lv_comprehensive.doPullRefreshing(true, 500);
        lv_order = lv_comprehensive.getRefreshableView();
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
    protected void onResume() {
        super.onResume();
        allcarorder();
    }

    private void allcarorder() {
        if (NetBaseUtils.isConnnected(mContext)) {
            LogUtils.i("UserId","---------->"+user.getUserId());
//            new UserRequest(mContext, handler).getUnpayOrderListByOwner(user.getUserId(), GET_NOPAY_LIST);

        } else {
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
        }
    }
}
