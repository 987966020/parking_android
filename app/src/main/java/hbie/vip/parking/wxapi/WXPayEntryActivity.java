package hbie.vip.parking.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import hbie.vip.parking.R;
import hbie.vip.parking.WXpay.Constants;
import hbie.vip.parking.activity.PayActivity;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.WXpay_StaticBean;
import hbie.vip.parking.fragment.InformationFragment;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
	private UserInfo user;
	public static final int ADD_CURRENTKEY = 2;
	private Context contect;
	private PayActivity payactivity;
	private Handler mHandler = new Handler(Looper.myLooper()){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case ADD_CURRENTKEY:
					if (msg.obj != null){
						String result = (String) msg.obj;
						try {
							JSONObject obj = new JSONObject(result);
							if ("success".equals(obj.getString("status"))){
								Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
							}
//							payactivity = new PayActivity();
//							payactivity.finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
			}
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		contect = this;
		user = new UserInfo(contect);
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);

		Log.e("useridpay","===============>"+user.getUserId());
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		contect = this;
        api.handleIntent(intent, this);

		Log.e("useridpay2","===============>"+user.getUserId());
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +";code=" + String.valueOf(resp.errCode)));
//			builder.show();
			if ("0".equals(String.valueOf(resp.errCode))){
				user.readData(contect);
				if (NetBaseUtils.isConnnected(this)){
					Log.e("useridpay3","===============>"+user.getUserId());
					new UserRequest(this, mHandler).PAYFREE(user.getUserId(), WXpay_StaticBean.wxpaylist.get(1), "2", WXpay_StaticBean.wxpaylist.get(0), "1", ADD_CURRENTKEY);
				}else{
					ToastUtils.ToastShort(this,"网络问题，请稍后重试！");
				}
				finish();
			}else if ("-1".equals(String.valueOf(resp.errCode))){
				ToastUtils.ToastShort(this, "支付失败");
//				payactivity = new PayActivity();
//				payactivity.finish();
				finish();
			}else if ("-2".equals(String.valueOf(resp.errCode))){
				ToastUtils.ToastShort(this, "取消支付");
//				payactivity = new PayActivity();
//				payactivity.finish();
				finish();
			}
		}
	}
}