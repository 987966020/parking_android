package hbie.vip.parking.constants;

/**
 * Created by mac on 16/5/9.
 */
public class MessageConstants {
    public static final String[] REFRESHS_PROJECTION = new String[] { MessageConstants.REFRESH, MessageConstants.REFRESH_USER, MessageConstants.REFRESH_CHANNEL, MessageConstants.REFRESH_CITY,
            MessageConstants.REFRESH_USER_TAG, MessageConstants.REFRESH_USER_EXPERIENCE,MessageConstants.REFRESH_PROFESSION_CIRCLE,MessageConstants.REFRESH_SYSTEM_NEWS,MessageConstants.UPGRADED_VERSION};
    // 刷新
    public static final String REFRESH = "com.canyinka.catering.refresh.interface";
    // 刷新user
    public static final String REFRESH_USER = "com.canyinka.catering.refresh.interface.user";
    // 刷新userTag
    public static final String REFRESH_USER_TAG = "com.canyinka.catering.refresh.interface.user.tag";
    // 刷新userExperience
    public static final String REFRESH_USER_EXPERIENCE = "com.canyinka.catering.refresh.interface.user.experience";
    // 刷新city
    public static final String REFRESH_CITY = "com.canyinka.catering.refresh.interface.city";
    // 刷新Channel
    public static final String REFRESH_CHANNEL = "com.canyinka.catering.refresh.interface.channel";
    // 餐饮圈新评论
    public static final String REFRESH_PROFESSION_CIRCLE="com.canyinka.catering.refresh.interface.profession.circle";
    //系统新消息
    public static final String REFRESH_SYSTEM_NEWS="com.canyinka.catering.refresh.interface.system.news";
    //检测版本
    public static final String UPGRADED_VERSION="com.canyinka.catering.refresh.interface.update.version";
    //成功注册
//	public static final String
//	//退出
//	public static final String
}
