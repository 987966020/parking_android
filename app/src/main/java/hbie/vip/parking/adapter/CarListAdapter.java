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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.update.UpdateCarActivity;
import hbie.vip.parking.bean.car.CarListInfo;

/**
 * Created by mac on 16/5/16.
 */
public class CarListAdapter extends BaseAdapter {
    private static final int ITEM_LAYOUT_TYPE_COUNT = 3;
    private ArrayList<CarListInfo> indexLists;
    private Context mContext;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public CarListAdapter(Context context, ArrayList<CarListInfo> index) {
        this.mContext = context;
        this.indexLists = index;
//        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.article_img).showImageOnFail(R.drawable.article_img).cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_LAYOUT_TYPE_COUNT;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return Integer.parseInt(indexLists.get(position).getNewsPicState().toString());
//    }

    @Override
    public int getCount() {
        return indexLists.size();
    }

    @Override
    public Object getItem(int position) {
        return indexLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("static-access")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int layoutType = getItemViewType(position);
        CommonViewHolder commonHolder = null;
        LayoutInflater inflater = null;
        // 无convertView，需要new出各个控件
        if (convertView == null) {
            // 按当前所需的样式，确定new的布局
            switch (layoutType) {
                case 0:
                    inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.drive_car_normal, null);
                    commonHolder = new CommonViewHolder();
                    commonHolder.tv_car_number = (TextView) convertView.findViewById(R.id.tv_car_number);
                    commonHolder.tv_car_brand = (TextView) convertView.findViewById(R.id.tv_car_brand);
                    commonHolder.iv_car_edit=(ImageView)convertView.findViewById(R.id.iv_car_editor);
                    convertView.setTag(commonHolder);
                    break;

            }
        } else {
            // 有convertView，按样式，取得不用的布局
            switch (layoutType) {
                case 0:
                    commonHolder = (CommonViewHolder) convertView.getTag();
                    break;
            }
        }
        /**
         * 返回不同的类型设置不同类型的资源
         */
        switch (layoutType) {
            case 0:
                commonHolder.tv_car_number.setText(indexLists.get(position).getCarNumber().toString());
                commonHolder.tv_car_brand.setText(indexLists.get(position).getCarBrand().toString());
                commonHolder.iv_car_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (indexLists.get(position).getCarId().equals("0") || indexLists.get(position).getCarId().equals("null")) {
                            getBundle(position, "id", UpdateCarActivity.class, "车辆信息");
                        }
                    }
                });
                break;
        }
        return convertView;
    }

    @SuppressWarnings("rawtypes")
    public void getBundle(final int position, String key, Class clazz, String str) {
        CarListInfo indexNewsList = indexLists.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, indexNewsList);
        Intent intent = new Intent(mContext, clazz);

        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @SuppressWarnings("rawtypes")
    public void getBundleNewCultivate(String newsClass, Class clazz) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("NewsClass", newsClass);
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 普通
     */
    static class CommonViewHolder {
        LinearLayout layout_recommend_car;
        TextView tv_car_number;
        TextView tv_car_brand;
        ImageView iv_car_edit;
    }


}
