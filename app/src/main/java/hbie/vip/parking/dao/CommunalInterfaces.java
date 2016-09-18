package hbie.vip.parking.dao;

/**
 * Created by mac on 16/5/9.
 */
public interface CommunalInterfaces {
    int STATE = 1;
    String _STATE = "success";
    int UPDATE_NAME = 0x11;
    int CITY = 0x12;
    int MEETSPECIALIIST_STATE = 0x13;
    int SERVICE_LIST = 0x14;
    int GETSERVICEUSERDATA = 0x16;
    int SOUSUO_LIST = 0x17;
    int ADDMAKESERVICE = 0x18;
    String FIRSTKEY = "6be10ac3ed9ea86c";
    int GETLIST = 0x21;
    int ITEM_LAYOUT_TYPE_COUNT = 2;
    int TYPE_ONE = 0;
    int TYPE_TWO = 1;
    int GETUSERDATA = 0x22;
    int USERCITY = 0x23;
    int SPICALISTCITY = 0x24;
    int AUDITYESSERVICE = 0x25;
    int AUDITNOSERVICE = 0x26;
    int UPDATE_BANKCARD = 0x27;
    int ADDREFERRAL = 0x28;
    int DELMEMBERCARD = 0x29;
    int GETMEMBERREFERRAL = 0x30;
    int GETSPECIALISTIMG = 0x31;
    int GETHTTPHEADIMG = 0x32;
    int GETSERVICECITY = 0x33;
    int GETMEMBERWITHLIST = 0x34;

    /* 资讯-专栏——begin */
    int GETNEWLIST = 0xa;
    int MEMBERCOLUMN = 0xb;
    int INFORMATIONLIST = 0xc;
    int NEWSMETTER = 0xd;
    int NEWSRELATED = 0xe;
    int INDEXNEWSLIST =0x39;
    int INDEXSLIDENEWSLIST = 0x35;
    int DISCUSS = 0x36;
    int DISCUSSLIST = 0x37;
    int USERDATE = 0x38;
    int INDEXPUSHNEWSLIST = 0xf;
    int GETPUSHNEWSLIST=16;
    int NETGETSEARCH = 0x40;
    int GETCOLUMN = 0x41;
    int COLUMNLIKE = 0x42;
    int NEWCOLLECT = 0x43;
    int WORKDISCUSS = 0x45;
	/* ——end—— */

    int GETNEWCOLLECT = 0x44;
    int ADDWORKRING = 0x46;
    int GETPROJECTLIST = 0x47;
    int PROJECTCLASS = 0x48;
    int GETTAGPROJECTLIST = 0x49;
    int GETINFORMATIONUPWARD = 0x50;
    int HIM_GETNEWCOLLECT=0x51;

    int DELCOLLECT=0x52;
    int GETPROJECTMATTER=0x53;
    int NEWNETGETSEARCH=0x54;

    /**
     * 6种状态 0=>待审核 1=>未评价 2=>约见成功 3=>审核失败 4=>通过审核 5=>待付款
     */
    String MAKESTATE_CHECKPENDING = "0";
    String MAKESTATE_UNVALUED = "1";
    String MAKESTATE_SUCCESS = "2";
    String MAKESTATE_FAILUREAUDIT = "3";
    String MAKESTATE_STATUSCHECK = "4";
    String MAKESTATE_OBLIGATION = "5";
}
