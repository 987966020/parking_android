package hbie.vip.parking.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import hbie.vip.parking.BaseActivity;

/**
 * Created by mac on 16/5/9.
 */
public class BackGestureListener implements GestureDetector.OnGestureListener {
    BaseActivity activity;

    public BackGestureListener(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if ((e2.getX() - e1.getX()) > 100 && Math.abs(e1.getY() - e2.getY()) < 60) {
            activity.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

}
