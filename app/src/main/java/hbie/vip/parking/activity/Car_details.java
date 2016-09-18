package hbie.vip.parking.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hbie.vip.parking.R;
import hbie.vip.parking.bean.order.order;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class Car_details extends Activity implements View.OnClickListener{
    private RelativeLayout car_back;
    private TextView detail_brand,detail_number,detail_park,detail_start,detail_end,detail_price,detail_alltime,detail_allprice;
    private Intent intent;
    private order.DataBean orderdataInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_detail);
        intent =  getIntent();
        orderdataInfo = (order.DataBean) intent.getSerializableExtra("orderinfo");
        cariniview();
        setView();
    }

    private void setView() {
        if (orderdataInfo != null){
            if (orderdataInfo.getBrand()!= null&&orderdataInfo.getBrand().length() > 0){
                detail_brand.setText(orderdataInfo.getBrand()+"");
            }else{
                detail_brand.setVisibility(View.GONE);
            }
            if (orderdataInfo.getCar_number()!= null&&orderdataInfo.getCar_number().length() > 0){
                detail_number.setText(orderdataInfo.getCar_number()+"");
            }else{
                detail_number.setText("无");
            }
            if (orderdataInfo.getOrder_no()!= null&&orderdataInfo.getOrder_no().length() > 0){//订单编号
                detail_park.setText(orderdataInfo.getOrder_no()+"");
            }else{
                detail_park.setText("无");
            }
            if (orderdataInfo.getStart_time_str()!= null&&orderdataInfo.getStart_time_str().length() > 0){
                detail_start.setText(orderdataInfo.getStart_time_str()+"");
            }else{
                detail_start.setText("无");
            }
            if (orderdataInfo.getEnd_time_str()!= null&&orderdataInfo.getEnd_time_str().length() > 0){
                detail_end.setText(orderdataInfo.getEnd_time_str() + "");
            }else{
                detail_end.setText("无");
            }
            if (orderdataInfo.getPrice()!= null&&orderdataInfo.getPrice().length() > 0){
                detail_price.setText(orderdataInfo.getPrice()+"元/15分钟");
                Log.i("price","------------------->"+orderdataInfo.getPrice());
            }else{
                detail_price.setText("0元/15分钟");
                Log.i("price", "------------------->" + orderdataInfo.getPrice());
            }
            if (orderdataInfo.getTime()!= null&&orderdataInfo.getTime().length() > 0){
                detail_alltime.setText(orderdataInfo.getTime()+"");
            }else{
                detail_alltime.setText("无");
            }
            if (orderdataInfo.getMoney()!= null&&orderdataInfo.getMoney().length() > 0){
                detail_allprice.setText(orderdataInfo.getMoney() + "元");
                Log.i("price", "------------------->" + orderdataInfo.getMoney());
            }else{
                detail_allprice.setText("0元");
                Log.i("price", "------------------->" + orderdataInfo.getMoney());
            }


        }
    }

    private void cariniview() {
        car_back = (RelativeLayout) findViewById(R.id.pay_activity_back);//返回
        car_back.setOnClickListener(this);
        detail_brand = (TextView) findViewById(R.id.car_detail_brand);//车品牌
        detail_number = (TextView) findViewById(R.id.car_detail_number);//车牌号
        detail_park  = (TextView) findViewById(R.id.car_detail_park);//停车信息
        detail_start = (TextView) findViewById(R.id.car_detail_start);//入库时间
        detail_end = (TextView) findViewById(R.id.car_detail_end);//出库时间
        detail_price = (TextView) findViewById(R.id.car_detail_price);//单价
        detail_alltime = (TextView) findViewById(R.id.car_detail_alltime);//时间累积
        detail_allprice = (TextView) findViewById(R.id.car_detail_allprice);//费用累计
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_activity_back:
                finish();
                break;
        }
    }
}
