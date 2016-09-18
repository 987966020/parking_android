package hbie.vip.parking.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import hbie.vip.parking.R;
import hbie.vip.parking.WXpay.Constants;
import hbie.vip.parking.WXpay.MD5;
import hbie.vip.parking.WXpay.Util;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.bean.WXpay_StaticBean;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.button.RevealButton;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.net.UserRequest;
import hbie.vip.parking.pay_zhifubao.PayResult;
import hbie.vip.parking.pay_zhifubao.SignUtils;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * Created by Administrator on 2016/6/11.
 */
public class PayActivity extends Activity implements View.OnClickListener {

    // 商户PID
//    public static final String PARTNER = "2088002084967422";
    public static final String PARTNER = "2088221951489991";
    // 商户收款账号
    public static final String SELLER = "wongzin@126.com";
//    public static final String SELLER = "aqian2001@163.com";
    // 商户私钥，pkcs8格式
//    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK4a3fO2l5Jn82ywtsBmWCKUDz/J0KKqmEXgu2VjO98dMjN7C+eO48kmhEe7JFpwVYZ9+tuO3TsSDonJ1DOsxVY/341/zYr8tV3SPjhChPTObAswUXznQ8qChIP8sCLdakw/YnlxnOvJneztmg++bIlMIGZUZy17rMKCgHTloJ7LAgMBAAECgYBv/RIlUJ7AaqL2l9iFe49Xdps0cbEE4OyfjgWcGq+JPTNsT8qBgLTeTyspJKQmlDk/EEvK7GM7OsslMDCRqKEpYGqgMJDZGYwanUc/gP4PNarsYY9J7yckRNMoUL2X8ROatiHLv2gHhaQ8zqQf0xG9/9uz+RG9KBiOhQzhEb7DmQJBAN40brXMEJdqqAxQ/de80M1vhgSu5nG+d/ztF2JHG/00DlUGu4AWypPL/6Xys5/GaWWCX3/XawZSHeias9NHdS0CQQDIlalKdsuFKSmrtH24hc/fYOp2VsWFUmMSDBzcizytT+zVKs1CBUbk5R7Pg/PS5iyeqYR6fbvs0HuArkD/f87XAkAQPLqeVEQeHHAdPkneWvDTIkQj0XgLdcSk2dpslw+niAdIFU7cRE4XUL/kq4COu1v2S/mYiPBMLPH8jll3pfAdAkAwhGLKbCmWL/qwWZv/Qf6h3WNY9Gwab28fMmbYwaUPlsGGXi//xB79xp3JO/WCEcLBLeepaThHc7YrzfpS0qtJAkEArxp4t06xxjWRKHpZdDFdtzpEdYg0sIDRhfepVCKxI496HRlrlo+7WipncI4Pm5fJIvm0IXbTlmlIVJx8EYKPPA==";
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANGJE3ZFmGUzOQFO" +
        "N4rlLxPAR5Kp8E4lgOUjJe71mS8QJdGsJFdGBnBCUROL+iMJFM2NLCi97e7nALev" +
        "PQd4QP5Ic1oBvgLlLWFwjLVEmzVBJuxboZLO7WXuYZA2NuGmOMZO//FJukAgyfHe" +
        "5vrbxtUjf5JBv5gtgJs+Xudn0CTbAgMBAAECgYBleEU1fERtlZ2gdTlOiOgAX5gJ" +
        "fURDA8Rksl23V7Yj5WT7IarDnMSXbnYGyj2K4+XwGNJutHNZwwJE8ZbTXDfUS5Wl" +
        "442mwSCM0aGzjiqSXbJLUTVBu0Cq2kfESl0j07yyeHGPu+dzFCjbZjwq4bxWgiVB" +
        "3+xK6vSsZEqcegplQQJBAPYA2ibtPERR/imvkvQflN4skKfq2agV2DZhRMzMQ0DC" +
        "0/lKaJ0jWUGZlKyxD/6rV3kq9D/5RqPdk1aXJYg/DXsCQQDaDNor4rBLaIQhHg7i" +
        "PWYY8K/F39qQ949O6JEm4+j6cOMrRacnJ8O66lHk/tb/SFJF2cBRyn105MS0k0/o" +
        "TbghAkA2NN/dHf9eqpaPxvFhu6fJARbq+VP2tsGK0gof+o6DMasVznCY15YuX1Ik" +
        "b2uv2T+QIofppNsM9qElvm51xDcLAkEAjCzk+H9znBalknCzWsfj4bahGRDufnFX" +
        "hH/ICHtNo+p8b64IZgiPMJNAcHlPl69TjKoOk8Yb3tDOj9N/9DQ6YQJAddZBixDa" +
        "wvXPvr64n3k1pbFqzBxSskidrpJ+KmaOwWEmDmrM74sqIjviurUSoFsP+5Q/9+ZY" +
        "z+BS6tfjCcpN6Q==";

    public static final String RSA_PUBLIC = "";
    private static final int SDK_PAY_FLAG = 1;


    private TextView pay_money, pay_car, pay_parking, pay_time;
    private Button choose_pay_weixin, choose_pay_zhifubao, choose_pay_yinhangka;
    private RevealButton pay_sure;
     String paymain, paypark, paymoney, paytime, paycar, payparknew, paymoneynew, paytimenew, paycarnew;
    private int chooseint = 1;
    ;
    private RelativeLayout  all_pay_means;
    private WaveView pm_back;
    private UserInfo user;
    private Context mContext;
    private Intent intent;
    private order.DataBean orderdataInfo;
    public static final int ADD_CURRENTKEY = 2;
    public static final int AlipayNotify = 3;


    private static final String TAG = "MicroMsg.SDKSample.PayActivity";


    IWXAPI msgApi;

    Map<String, String> resultunifiedorder;
    StringBuffer sb;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        if (NetBaseUtils.isConnnected(mContext)){
                            new UserRequest(mContext, mHandler).PAYFREE(user.getUserId(), orderdataInfo.getOrder_no(), "1", paymoney, "1", ADD_CURRENTKEY);
//                            new UserRequest(mContext, mHandler).AlipayNotify(AlipayNotify);
                        }else{
                            ToastUtils.ToastShort(mContext,"网络问题，请稍后重试！");
                        }
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
                case ADD_CURRENTKEY:
                    if (msg.obj != null){
                       String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.getString("status"))){
                                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case AlipayNotify:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.getString("status"))){
                                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_activity);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo(mContext);
        intent = getIntent();
        paymain = intent.getStringExtra("paymain");
        paypark = intent.getStringExtra("currentpark");
        paymoney = intent.getStringExtra("currentmoney");
        paytime = intent.getStringExtra("currenttime");
        paycar = intent.getStringExtra("currentcar");
        orderdataInfo = (order.DataBean) intent.getSerializableExtra("orderinfo");

        sb = new StringBuffer();
        msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        msgApi.registerApp(Constants.APP_ID);
        getCurrentPay();
    }

    private void getCurrentPay() {
        pay_money = (TextView) findViewById(R.id.tv_current_pay_money_text);
        pay_car = (TextView) findViewById(R.id.tv_pay_current_car_text);
        pay_parking = (TextView) findViewById(R.id.tv_pay_current_carparking_text);
        pay_time = (TextView) findViewById(R.id.tv_pay_current_carparking_time);
        all_pay_means = (RelativeLayout) findViewById(R.id.all_pay_means);
        pay_sure = (RevealButton) findViewById(R.id.btn_sure_pay);
        pay_sure.setOnClickListener(this);
        if (paymain.equals("1")) {
//            pay_money.setText(paymoney+"");
//            pay_car.setText(user.getCurrentcar()+"");
//            pay_parking.setText(paypark+"");
//            pay_time.setText(paytime+"");
            pay_money.setText(orderdataInfo.getMoney()+"");
            pay_car.setText(user.getCurrentcar()+"");
            pay_parking.setText(orderdataInfo.getPark()+"");
            pay_time.setText(orderdataInfo.getTime()+"");
        } else if (paymain.equals("2")) {
            pay_money.setText(orderdataInfo.getMoney()+"");
            pay_car.setText(orderdataInfo.getCar_number()+"");
            pay_parking.setText(orderdataInfo.getPark()+"");
            pay_time.setText(orderdataInfo.getTime()+"");
        } else if (paymain.equals("3")) {
            pay_money.setText(orderdataInfo.getMoney()+"");
            pay_car.setText(orderdataInfo.getCar_number()+"");
            pay_parking.setText(orderdataInfo.getPark()+"");
            pay_time.setText(orderdataInfo.getTime()+"");
            all_pay_means.setVisibility(View.GONE);
            pay_sure.setVisibility(View.GONE);
        } else if (paymain.equals("4")) {
            pay_money.setText(paymoney+"");
            pay_car.setText(paycar+"");
            pay_parking.setText(paypark+"");
            pay_time.setText(paytime+"");
        }
        choose_pay_weixin = (Button) findViewById(R.id.rb_choose_weixin);
        choose_pay_weixin.setOnClickListener(this);
        choose_pay_zhifubao = (Button) findViewById(R.id.rb_choose_zhifubao);
        choose_pay_zhifubao.setOnClickListener(this);
        choose_pay_yinhangka = (Button) findViewById(R.id.rb_choose_yinhangka);
//        choose_pay_yinhangka.setOnClickListener(this);

        pm_back = (WaveView) findViewById(R.id.pay_activity_back);
        pm_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_choose_weixin:
                choose_pay_weixin.setSelected(true);
                choose_pay_zhifubao.setSelected(false);
                choose_pay_yinhangka.setSelected(false);
                chooseint = 0;
                LogUtils.i("微信", "--------->" + chooseint);
                break;
            case R.id.rb_choose_zhifubao:
                choose_pay_weixin.setSelected(false);
                choose_pay_zhifubao.setSelected(true);
                choose_pay_yinhangka.setSelected(false);
                chooseint = 1;
                LogUtils.i("支付宝", "--------->" + chooseint);
                break;
//            case R.id.rb_choose_yinhangka:
//                choose_pay_yinhangka.setSelected(true);
//                choose_pay_weixin.setSelected(false);
//                choose_pay_zhifubao.setSelected(false);
//                chooseint = 2;
//                LogUtils.i("银行卡", "--------->" + chooseint);
//                break;
            case R.id.btn_sure_pay:
                if (chooseint == 1) {
                    paymoneynew = pay_money.getText().toString();
                    paycarnew = pay_car.getText().toString();
                    payparknew = pay_parking.getText().toString();
                    paytimenew = pay_time.getText().toString();
                    paymoney();
                } else if (chooseint == 0) {
                    paymoneynew = pay_money.getText().toString();
                    paycarnew = pay_car.getText().toString();
                    payparknew = pay_parking.getText().toString();
                    paytimenew = pay_time.getText().toString();
                    wxpay();
                }
//                else if (chooseint == 2) {
//                    ToastUtils.ToastShort(mContext, "银行卡尚未开通");
//                }
                else {
                    ToastUtils.ToastShort(mContext, "请选择支付方式");
                }


                break;
            case R.id.pay_activity_back:
                PayActivity.this.finish();
                break;
        }
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    private void paymoney() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        if (paycarnew == null || "".equals(paycarnew)){
            paycarnew = "无";
        }else if (payparknew == null || "".equals(payparknew)){
            payparknew = "无";
        }else if (paytimenew == null || "".equals(paytimenew)){
            paytimenew = "无";
        }else if (paymoneynew == null || "".equals(paymoneynew)){
            paymoneynew = "无";
        }
        String orderInfo = getOrderInfo(paycarnew, payparknew, paytimenew,paymoneynew );
//        String orderInfo = getOrderInfo(paycarnew, payparknew, paytimenew,"0.1" );

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
//     *
//     * @param v
//     */
//    public void h5Pay(View v) {
//        Intent intent = new Intent(this, H5PayDemoActivity.class);
//        Bundle extras = new Bundle();
//        /**
//         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//         * 商户可以根据自己的需求来实现
//         */
//        String url = "http://m.taobao.com";
//        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        startActivity(intent);
//    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String parking,String time, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称（当前车辆）
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情(所在停车场)
        orderInfo += "&parking=" + "\"" + parking + "\"";

        //商品详情(停车时间)
        orderInfo += "&time=" + "\"" + time + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
//        http://parking.xiaocool.net/index.php?g=apps&m=index&a=AlipayNotify
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
//        orderInfo += "&notify_url=" + "\"" + "http://parking.xiaocool.net/index.php?g=apps&m=index&a=AlipayNotify" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    //微信支付流程
    private void wxpay() {
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();

    }
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(PayActivity.this, "提示", "正在获取支付订单");
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
//            show.setText(sb.toString());
            Log.e("prepay_id", "-----------------" + sb.toString());

            resultunifiedorder = result;
            sendPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("orion1", "----" + entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion2", "----" + content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", paycarnew));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "www.baidu.com"));
            packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));//商户订单号
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
//            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("total_fee",String.valueOf(Integer.valueOf(paymoneynew)*100)));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            Log.e("paycarnew", "================" + paycarnew);
            Log.e("genOutTradNo","================"+genOutTradNo());
            Log.e("genOutTradNo","================"+String.valueOf(Integer.valueOf(paymoneynew)*100));

            String sign = genPackageSign(packageParams);//生成签名
            packageParams.add(new BasicNameValuePair("sign", sign));

            WXpay_StaticBean.wxpaylist.clear();
            WXpay_StaticBean.wxpaylist.add(paymoneynew);
            WXpay_StaticBean.wxpaylist.add(orderdataInfo.getOrder_no());

            String xmlstring = toXml(packageParams);

            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

        } catch (Exception e) {
            return null;
        }
    }
    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion3", "----" + e.toString());
        }
        return null;

    }
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
    private String genOutTradNo() {//获取商户 订单号
        Random random = new Random();
//		return "COATBAE810"; //订单号写死的话只能支付一次，第二次不能生成订单
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);


        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", "----" + packageSign);
        return packageSign;
    }
    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", "----" + sb.toString());
        return sb.toString();
    }
    private void sendPayReq() {

        genPayReq();


    }
    private void genPayReq() {
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

//        show.setText(sb.toString());
        msgApi.registerApp(Constants.APP_ID);
        Log.e("orion4", "----" + signParams.toString());
        msgApi.sendReq(req);
        finish();
    }
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("orion", "----" + appSign);
        return appSign;
    }

}
