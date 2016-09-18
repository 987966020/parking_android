package hbie.vip.parking.ui.listnew;

import android.app.Activity;
import android.os.Bundle;

//import com.handmark.pulltorefresh.library.PullToRefreshListView;

import hbie.vip.parking.R;
import hbie.vip.parking.ui.list.PullToRefreshListView;

/**
 * Created by Administrator on 2016/6/13.
 */
public class PullToRefreshListNew extends Activity {

    PullToRefreshListView pullLstv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baotao);
        iniView();
    }

    private void iniView() {

    }
}
