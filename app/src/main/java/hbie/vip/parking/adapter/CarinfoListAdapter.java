package hbie.vip.parking.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.AddCarActivity;
import hbie.vip.parking.activity.PayFragmentActivity;
import hbie.vip.parking.activity.PayOrderActivity;
import hbie.vip.parking.activity.update.UpdateCarActivity;
import hbie.vip.parking.bean.car.Car;
import hbie.vip.parking.button.WaveView;
import hbie.vip.parking.fragment.PayFragment;

/**
 * Created by mac on 16/5/16.
 */
public class CarinfoListAdapter extends BaseAdapter {
    private List<Car.CarData> carDataList;
    private LayoutInflater inflater;
    private Context mContext;
    private int layouttype;
    public CarinfoListAdapter( Context mContext,List<Car.CarData> carDataList,int layouttype) {
        this.mContext = mContext;
        this.carDataList = carDataList;
        this.layouttype=layouttype;
        this.inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return carDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return carDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Car.CarData carData =carDataList.get(position);
        switch (layouttype){
            case 1:
                if (convertView == null){
                    convertView = inflater.inflate(R.layout.drive_car_normal,null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.car_number.setText(carDataList.get(position).getCarnumber());
                holder.car_brand.setText(carDataList.get(position).getBrand());
                holder.car_editor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundle(position, "carinfo", UpdateCarActivity.class, "车辆信息");
                    }
                });
                break;
            case 2:
                if (convertView == null){
                    convertView = inflater.inflate(R.layout.find_order_car_list_item,null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.car_number.setText(carDataList.get(position).getCarnumber());
//                holder.car_brand.setText(carDataList.get(position).getBrand());
                holder.car_editor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundle(position, "carinfo", PayFragment.class, "车辆信息");
                    }
                });
                break;

        }

        return convertView;
    }
    @SuppressWarnings("rawtypes")
    public void getBundle(final int position, String key, Class clazz, String str) {
        Car.CarData cardataInfo = carDataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, cardataInfo);
        Intent intent = new Intent(mContext, clazz);
        intent.putExtra("type","2");
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
    class ViewHolder{
        TextView car_number,car_brand;
        WaveView car_editor;
        public ViewHolder(View convertView) {
            switch (layouttype){
                case 1:
                    car_number = (TextView) convertView.findViewById(R.id.tv_car_number);
                    car_brand = (TextView) convertView.findViewById(R.id.tv_car_brand);
                    car_editor = (WaveView) convertView.findViewById(R.id.ly_car_info);
                    break;
                case 2:
                    car_number = (TextView) convertView.findViewById(R.id.tv_car_number);
                    car_editor = (WaveView) convertView.findViewById(R.id.iv_find_order_car);
                    break;

            }

        }
    }
    public static long getTodayZero() {
        Date date = new Date();
        long l = 24*60*60*1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
    }
}