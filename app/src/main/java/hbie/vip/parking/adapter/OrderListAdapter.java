package hbie.vip.parking.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.Car_details;
import hbie.vip.parking.activity.PayActivity;
import hbie.vip.parking.bean.order.order;
import hbie.vip.parking.button.RevealButton;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.utils.LogUtils;

/**
 * Created by mac on 16/5/17.
 */
public class OrderListAdapter extends BaseAdapter {
    private List<order.DataBean> orderDataList;
    private LayoutInflater inflater;
    private Context mContext;
    private int typelayout;

    public OrderListAdapter(Context mContext, List<order.DataBean> orderDataList, int typelayout) {
        this.mContext = mContext;
        this.orderDataList = orderDataList;
        this.typelayout = typelayout;
        this.inflater = LayoutInflater.from(mContext);
    }


//    @Override
//    public int getItemViewType(int viewposition) {
//        int p = viewposition;
//        if(p==0){
//            return 0;
//        }else {
//            return 1;
//        }
//
//    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public int getCount() {
        return orderDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderPay holderPay = null;
        ViewHolderUnPay holderUnPay = null;
        final order.DataBean orderData = orderDataList.get(position);
        int type = getItemViewType(position);

        switch (typelayout) {
            case 0:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.order_pay_list, null);
                    holderPay = new ViewHolderPay(convertView);
                    convertView.setTag(holderPay);
                } else {
                    holderPay = (ViewHolderPay) convertView.getTag();
                }
                if (orderDataList.get(position).getCar_number() != null && orderDataList.get(position).getCar_number().length() > 0) {
                    holderPay.car_number.setText(orderDataList.get(position).getCar_number() + "");
                } else {
                    holderPay.car_number.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getPark() != null && orderDataList.get(position).getPosition() != null && orderDataList.get(position).getPosition().length() > 0&& orderDataList.get(position).getPark().length() > 0) {
                    holderPay.car_location.setText(orderDataList.get(position).getPark() + orderDataList.get(position).getPosition() + "");
                } else {
                    holderPay.car_location.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getEnd_time_str() != null && orderDataList.get(position).getEnd_time_str().length() > 0) {
                    holderPay.car_time.setText(orderDataList.get(position).getEnd_time_str() + "");
                } else {
                    holderPay.car_time.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getTime() != null && orderDataList.get(position).getTime().length() > 0) {
                    holderPay.car_spend.setText(orderDataList.get(position).getTime() + "");
                } else {
                    holderPay.car_spend.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getMoney() != null && orderDataList.get(position).getMoney().length() > 0) {
                    holderPay.pay_money.setText("￥" + orderDataList.get(position).getMoney() + "");
                } else {
                    holderPay.pay_money.setVisibility(View.GONE);
                }
                holderPay.tv_car_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundlepay(position, "orderinfo", Car_details.class, "缴费信息");
                    }
                });
                holderPay.editoril_material_gender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundlepay(position, "orderinfo", Car_details.class, "缴费信息");
                    }
                });
                break;
            case 1:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.order_unpay_list, null);
                    holderUnPay = new ViewHolderUnPay(convertView);
                    convertView.setTag(holderUnPay);
                } else {
                    holderUnPay = (ViewHolderUnPay) convertView.getTag();
                }
                if (orderDataList.get(position).getCar_number() != null && orderDataList.get(position).getCar_number().length() > 0) {
                    holderUnPay.un_car_number.setText(orderDataList.get(position).getCar_number() + "");
                } else {
                    holderUnPay.un_car_number.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getPark() != null && orderDataList.get(position).getPark().length() > 0 && orderDataList.get(position).getPosition() != null && orderDataList.get(position).getPosition().length() > 0) {
                    holderUnPay.un_car_location.setText(orderDataList.get(position).getPrice() + orderDataList.get(position).getPosition() + "");
                } else {
                    holderUnPay.un_car_location.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getEnd_time_str() != null && orderDataList.get(position).getEnd_time_str().length() > 0) {
                    holderUnPay.un_car_time.setText(orderDataList.get(position).getEnd_time_str() + "");
                } else {
//                    holderUnPay.un_car_time.setVisibility(View.GONE);
                    holderUnPay.un_car_time.setText("");
                }
                if (orderDataList.get(position).getTime() != null && orderDataList.get(position).getTime().length() > 0) {
                    holderUnPay.un_car_spend.setText(orderDataList.get(position).getTime() + "");
                } else {
                    holderUnPay.un_car_spend.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getMoney() != null && orderDataList.get(position).getMoney().length() > 0) {
                    holderUnPay.un_pay_money.setText("￥" + orderDataList.get(position).getMoney() + "");
                } else {
                    holderUnPay.un_pay_money.setVisibility(View.GONE);
                }
                LogUtils.i("数据", "------------->" + holderUnPay.un_car_number);
                holderUnPay.un_tv_car_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundleunpay(position, "orderinfo", PayActivity.class, "缴费信息");
                    }
                });
                holderUnPay.editoril_material_gender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundleunpay(position, "orderinfo", Car_details.class, "缴费信息");
                    }
                });
                break;
            case 2:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.order_unpay_list, null);
                    holderUnPay = new ViewHolderUnPay(convertView);
                    convertView.setTag(holderUnPay);
                } else {
                    holderUnPay = (ViewHolderUnPay) convertView.getTag();
                }
                if (orderDataList.get(position).getCar_number() != null && orderDataList.get(position).getCar_number().length() > 0) {
                    holderUnPay.un_car_number.setText(orderDataList.get(position).getCar_number() + "");
                } else {
                    holderUnPay.un_car_number.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getPark() != null && orderDataList.get(position).getPark().length() > 0 && orderDataList.get(position).getPosition() != null && orderDataList.get(position).getPosition().length() > 0) {
                    holderUnPay.un_car_location.setText(orderDataList.get(position).getPrice() + orderDataList.get(position).getPosition() + "");
                } else {
                    holderUnPay.un_car_location.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getEnd_time_str() != null && orderDataList.get(position).getEnd_time_str().length() > 0) {
                    holderUnPay.un_car_time.setText(orderDataList.get(position).getEnd_time_str() + "");
                } else {
                    holderUnPay.un_car_time.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getTime() != null && orderDataList.get(position).getTime().length() > 0) {
                    holderUnPay.un_car_spend.setText(orderDataList.get(position).getTime() + "");
                } else {
                    holderUnPay.un_car_spend.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getMoney() != null && orderDataList.get(position).getMoney().length() > 0) {
                    holderUnPay.un_pay_money.setText("￥" + orderDataList.get(position).getMoney() + "");
                } else {
                    holderUnPay.un_pay_money.setVisibility(View.GONE);
                }
                if (orderDataList.get(position).getPay_status() != null && orderDataList.get(position).getPay_status().length() > 0){
                    if ("0".equals(orderDataList.get(position).getPay_status())){
                        holderUnPay.un_tv_car_pay.setText("支付");
                        LogUtils.i("数据", "------------->" + holderUnPay.un_car_number);
                        holderUnPay.un_tv_car_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getBundleunpay(position, "orderinfo", PayActivity.class, "缴费信息");
                            }
                        });
                    }else if ("1".equals(orderDataList.get(position).getPay_status())){
                        holderUnPay.un_tv_car_pay.setText("已支付");
                        LogUtils.i("数据", "------------->" + holderUnPay.un_car_number);
                        holderUnPay.un_tv_car_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getBundleunpay(position, "orderinfo", Car_details.class, "缴费信息");
                            }
                        });
                    }
                }

                holderUnPay.editoril_material_gender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundleunpay(position, "orderinfo", Car_details.class, "缴费信息");
                    }
                });
                break;
        }
        return convertView;
    }

    @SuppressWarnings("rawtypes")
    public void getBundleunpay(final int position, String key, Class clazz, String str) {
        order.DataBean orderdataInfo = orderDataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, orderdataInfo);
        bundle.putString("paymain", "2");
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @SuppressWarnings("rawtypes")
    public void getBundlepay(final int position, String key, Class clazz, String str) {
        order.DataBean orderdataInfo = orderDataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, orderdataInfo);
        bundle.putString("paymain", "3");
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    class ViewHolderUnPay {
        RevealButton un_tv_car_pay;
        TextView un_car_number, un_pay_money, un_car_location, un_car_time, un_car_spend;
        WaveView editoril_material_gender;
        public ViewHolderUnPay(View convertView) {
            un_car_number = (TextView) convertView.findViewById(R.id.tv_wait_car_number);
            un_car_location = (TextView) convertView.findViewById(R.id.tv_car_wait_location);
            un_car_time = (TextView) convertView.findViewById(R.id.tv_car_wait_time);
            un_car_spend = (TextView) convertView.findViewById(R.id.tv_car_wait_spent);
            un_tv_car_pay = (RevealButton) convertView.findViewById(R.id.btn_order_pay);
            un_pay_money = (TextView) convertView.findViewById(R.id.tv_car_all_pay);
            editoril_material_gender = (WaveView) convertView.findViewById(R.id.editoril_material_gender);


        }
    }

    class ViewHolderPay {
        TextView car_number, pay_money, car_location, car_time, car_spend, tv_car_pay;

        WaveView editoril_material_gender;
        public ViewHolderPay(View convertView) {
            car_number = (TextView) convertView.findViewById(R.id.tv_wait_car_number_payfor);
            car_location = (TextView) convertView.findViewById(R.id.tv_car_wait_location_payfor);
            car_time = (TextView) convertView.findViewById(R.id.tv_car_wait_time_payfor);
            car_spend = (TextView) convertView.findViewById(R.id.tv_car_wait_spent_payfor);
            tv_car_pay = (TextView) convertView.findViewById(R.id.btn_order_pay_payfor);
            pay_money = (TextView) convertView.findViewById(R.id.tv_car_all_pay_payfor);
            editoril_material_gender = (WaveView) convertView.findViewById(R.id.editoril_material_gender);

        }

    }

    public static long getTodayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }
}
