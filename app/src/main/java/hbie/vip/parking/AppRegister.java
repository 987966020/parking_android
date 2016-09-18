package hbie.vip.parking;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hbie.vip.parking.WXpay.Constants;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);

		// 将该app注册到微信
		msgApi.registerApp(Constants.APP_ID);
	}
}
