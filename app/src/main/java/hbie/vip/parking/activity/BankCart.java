package hbie.vip.parking.activity;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.dao.CommunalInterfaces;
import hbie.vip.parking.fragment.CarFragment;
import hbie.vip.parking.net.NetUtil;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.ui.list.PullToRefreshBase;
import hbie.vip.parking.ui.list.PullToRefreshListView;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BankCart extends Activity implements View.OnClickListener {
    private RelativeLayout ll_bankcard;
    private WaveView  bc_back;
//    private PopupMenu bc_delete;
    private TextView bc_bankName,bc_subranch,bc_cardNum,bc_visivle_addcard;
    private String bankName,subBranch,cardNum,cardNam;
//    private PullToRefreshListView iv_comprehensive;
    private Button iv_addbank;
    private Context mContext;
    private UserInfo user;
    public static final int KEY = 1;
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Handler handler = new Handler(Looper.myLooper()){
    @Override
    public void handleMessage(Message msg) {
        switch(msg.what){
            case KEY:
                if (msg.obj!=null){
                    String result = (String) msg.obj;
                    JSONObject obj;
                    try {
                        obj = new JSONObject(result);
                        String state = obj.getString("status");
                        String data = obj.getString("data");
                        LogUtils.i("state","-------->"+state);
                        LogUtils.i("data","--------->"+data);
                        LogUtils.i("userid","========>"+user.getUserId());
                        if (state.equals("success")){
                                JSONObject item = new JSONObject(data);
                            if (item.getString("bank_user_name")!=null&&item.getString("bank_user_name").length() > 0&&item.getString("bank_type")!=null&&item.getString("bank_type").length() > 0&&
                            item.getString("bank_no")!=null&&item.getString("bank_no").length() > 0&&item.getString("bank_branch")!=null&&item.getString("bank_branch").length() > 0){
                                bc_bankName.setText( item.getString("bank_user_name"));
                                bc_subranch.setText(item.getString("bank_type")+item.getString("bank_no"));
                                bc_cardNum.setText(item.getString("bank_branch"));
                                cardNam = item.getString("bank_type");
                                subBranch = item.getString("bank_no");
                                ll_bankcard.setVisibility(View.VISIBLE);
                                ll_bankcard.setOnClickListener(BankCart.this);
                                bc_visivle_addcard.setVisibility(View.GONE);
//                                iv_addbank.setText("修改");
                            }else{
                                ll_bankcard.setVisibility(View.GONE);
                                bc_visivle_addcard.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



                break;
            default:
                break;
        }
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bankcart_main);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo(mContext);
        bankcart();
        getCard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取数据，网络保存并获取数据
        LogUtils.i("UserData","---------->onResume");
        getCard();
    }

    private void bankcart() {
        bc_back = (WaveView) findViewById(R.id.ll_bankcart_back);
        bc_back.setOnClickListener(this);
        iv_addbank = (Button) findViewById(R.id.add_data_bankcart);
        iv_addbank.setOnClickListener(this);
        bc_bankName = (TextView) findViewById(R.id.tv_bankcardname);
        bc_subranch = (TextView) findViewById(R.id.tv_sunbranchname);
        bc_cardNum = (TextView) findViewById(R.id.tv_card_number);
        ll_bankcard = (RelativeLayout)findViewById(R.id.ll_bankcard);
        bc_visivle_addcard = (TextView) findViewById(R.id.tv_visible_please_add_card);

//
//        iv_comprehensive = (PullToRefreshListView) findViewById(R.id.iv_bank_cart_comprehensive);
//        iv_comprehensive.setPullRefreshEnabled(false);
//        iv_comprehensive.setScrollLoadEnabled(true);
//        iv_comprehensive.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {


//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//               if (NetBaseUtils.isConnnected(mContext)){
//                    new UserRequest(mContext,handler).GetUserInfo(user.getUserId(),KEY);
//               }else{
//                   ToastUtils.ToastShort(mContext,"�������⣬���Ժ����ԣ�");
//               }
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//            }
//        });
    }
    private void getCard() {
        if (NetBaseUtils.isConnnected(mContext)){
            new UserRequest(mContext,handler).GetUserInfo(user.getUserId(),KEY);
        }else{
            ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_bankcart_back:
                finish();
                break;
            case R.id.add_data_bankcart:
                Intent intent = new Intent(BankCart.this,AddCartActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_bankcard:
                Intent intentupdate = new Intent(BankCart.this,AddCartActivity.class);
                intentupdate.putExtra("usdaptcard","1");
                intentupdate.putExtra("bankname",bc_bankName.getText().toString());
                intentupdate.putExtra("branchname",subBranch);
                intentupdate.putExtra("cardname",cardNam);
                intentupdate.putExtra("cartNum",bc_cardNum.getText().toString());
                startActivity(intentupdate);
                break;
            default:
                break;
        }
    }



}
