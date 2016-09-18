package hbie.vip.parking.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mac on 16/5/9.
 */
public class IntentUtils {
    @SuppressWarnings("rawtypes")
    public static Intent getIntent(Activity activity, Class clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);

        return intent;
    }

//    public static void getIntent(Context mContext, Class<FriendDataActivity> clazz) {
//        Intent intent = new Intent(mContext, clazz);
//        mContext.startActivity(intent);
//    }

    public static Intent getIntents(Context mContext, Class<?> clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
        return intent;
    }

}
