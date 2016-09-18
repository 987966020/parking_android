package hbie.vip.parking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import hbie.vip.parking.BaseActivity;
import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.task.constants.MessageConstants;
import hbie.vip.parking.ui.ReboundScrollView;
import hbie.vip.parking.ui.RoundImageView;
import hbie.vip.parking.utils.HeadPicture;
import hbie.vip.parking.utils.IntentUtils;

/**
 * Created by mac on 16/5/11.
 */

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout back, edit;
//    private ListViewForScrollView lv_work_experience, lv_comment;
    private TextView tv_name, tv_company, tv_poistion, tv_city;
    private RoundImageView iv_head;
    private ImageView iv_gender;
    private ImageView iv_daKa;
    private ReboundScrollView scrollview;

//    private ArrayList<WorkExperienceInfo> experiencesList;
//    private PersonlExperienceAdapter adapter;
    private UserInfo user;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private static final int SELECT_COMMENT_KEY = 1;
    private Context mContext;

    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SELECT_COMMENT_KEY:

                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_data);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.relativeLayout_personal_data_back);
        back.setOnClickListener(this);
        edit = (RelativeLayout) findViewById(R.id.relativeLayout_personal_data_edit);
        edit.setOnClickListener(this);
        iv_head = (RoundImageView) findViewById(R.id.iv_personal_data_head);
        iv_daKa=(ImageView) findViewById(R.id.iv_personal_data_ka);
        iv_gender = (ImageView) findViewById(R.id.iv_personal_data_gender);
        scrollview = (ReboundScrollView) findViewById(R.id.scrollView_personal_data);
        scrollview.smoothScrollTo(0, 0);
        tv_name = (TextView) findViewById(R.id.tv_personal_data_name);
        tv_company = (TextView) findViewById(R.id.tv_personal_data_company);
        tv_poistion = (TextView) findViewById(R.id.tv_personal_data_position);
        tv_city = (TextView) findViewById(R.id.tv_personal_data_city);

        user = new UserInfo();
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square).cacheInMemory(true).cacheOnDisc(true).build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 同步userExperience
//        Instruction_Utils.sendInstrustion(mContext.getApplicationContext(), Instruction_Utils.SYNCHRONOUS_USER_EXPERIENCE);
        user.readData(mContext);
//        if (!user.getUserImg().equals("")) {
//            imageLoader.displayImage(NetBaseConstant.NET_BASE_HOST + "/" + user.getUserImg(), iv_head, options);
//        }else{
            new HeadPicture().getHeadPicture(iv_head);
//        }
//        if (user.getIsKa().equals("0")) {
//            iv_daKa.setVisibility(View.GONE);
//        }else if (user.getIsKa().equals("1")) {
//            iv_daKa.setVisibility(View.VISIBLE);
//        }
        tv_name.setText(user.getUserName());
//        tv_company.setText(user.getUserCompany());
//        tv_poistion.setText(user.getUserPosition());
//        tv_city.setText(user.getUserCity());
        if (user.getUserSex() != null) {
            if (user.getUserSex().equals("0")) {
                iv_gender.setImageResource(R.drawable.icon_woman);
            } else if (user.getUserSex().equals("1")) {
                iv_gender.setImageResource(R.drawable.icon_man_yellow);
            }
        }
//
        /**
         * 查询评论
         */
        // TODO

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout_personal_data_back:
                finish();
                break;
            case R.id.relativeLayout_personal_data_edit:
                IntentUtils.getIntent(PersonalDataActivity.this, EditorilPersonalDataActivity.class);
                break;
        }

    }

    @Override
    public void dealWithRadio(Intent intent) {
        super.dealWithRadio(intent);
        if (intent != null) {
            String action = intent.getAction();
            if (action.equals(MessageConstants.REFRESH_USER_EXPERIENCE)) {
                user.readData(mContext);
//                experiencesList.clear();
//                for (int i = 0; i < user.getUserExperiences().size(); i++) {
//                    experiencesList.add(user.getUserExperiences().get(i));
//                }
//                adapter.notifyDataSetChanged();
//                if (experiencesList.size() < 3) {
//                    tv_add_experience.setVisibility(View.VISIBLE);
//                } else {
//                    tv_add_experience.setVisibility(View.GONE);
//                }
            }
        }
    }

}

